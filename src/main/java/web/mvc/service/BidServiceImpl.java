package web.mvc.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import web.mvc.domain.*;
import web.mvc.dto.*;
import web.mvc.exception.BidException;
import web.mvc.exception.ErrorCode;
import web.mvc.exception.UserBuyException;
import web.mvc.redis.RedisUtils;
import web.mvc.repository.*;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;

    private final ModelMapper modelMapper;

    private final ManagementRepository managementRepository;

    private final AuctionRepository auctionRepository;

    private final NotificationService notificationService;

    private final NormalUserRepository normalUserRepository;

    private final CompanyUserRepository companyUserRepository;


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisUtils redisUtils;

    private final WalletRepository walletRepository;
    /*
      4.기존 입찰자 존재시(userSeq 0이 아닐시) -> 알림(나중에 구현)
      5. 포인트차감 (role 기준으로 일반인은 100퍼, 업체는 10퍼)
      5.redis watch로 데이터 수정이 이미 된 상태면 rollback
   */
    @Override
    public Bid bid(BidDTO BidderDTO, HighestBidDTO highestBid, UserTempWalletDTO newBidderTempWallet) {
        Bid bid =modelMapper.map(BidderDTO, Bid.class);
        bid.setManagementUser(ManagementUser.builder().userSeq(BidderDTO.getUserSeq()).build());
        bid.setAuction(Auction.builder().auctionSeq(BidderDTO.getAuctionSeq()).build());
        bid = bidRepository.save(bid);
        if(newBidderTempWallet.getPoint()<highestBid.getPrice()){
            throw new UserBuyException(ErrorCode.HIGH_BIDDER);
        }
        redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {

                try {
                    // 특정 키를 모니터링(watch) 설정, multi 전에 호출
                    operations.watch("highestBid:" + highestBid.getAuctionSeq());
                    // 트랜잭션 시작
                    operations.multi();
                    ManagementUser user = managementRepository.findById(BidderDTO.getUserSeq()).orElse(null);
                    System.out.println("최고가 입찰 유저번호"+highestBid.getUserSeq());
                    // 입찰자 존재 시 가상 지갑에 포인트 돌려주기
                    if (highestBid.getUserSeq() != 0) {
                        System.out.println("지갑돈 돌려주기 !!");
                        UserTempWalletDTO oldBidderWallet = redisUtils.getData("userTempWallet:" + highestBid.getUserSeq(), UserTempWalletDTO.class).orElseGet(()-> {
                            System.out.println("유저 가상 정보가 없어요~");
                            UserWallet userWallet = walletRepository.findByUserSeq(highestBid.getUserSeq());
                            UserTempWalletDTO  wallet  = UserTempWalletDTO.builder()
                                    .userWalletSeq(userWallet.getUserWalletSeq())
                                    .userSeq((userWallet.getManagementUser().getUserSeq()))
                                    .point(userWallet.getPoint())
                                    .build();
                            redisUtils.saveData("userTempWallet:"+wallet.getUserWalletSeq(),wallet);
                            return wallet;
                        });
                        if(user.getContent().equals("user")) {
                            System.out.println("유저한테 돈  돌려주기 !!");
                            oldBidderWallet.setPoint(oldBidderWallet.getPoint() + highestBid.getPrice()); // 기존 입찰자에게 포인트 돌려주기
                        }else{
                            System.out.println("업체돈 돌려주기 !!");
                            oldBidderWallet.setPoint(oldBidderWallet.getPoint() + (int)(highestBid.getPrice()*0.1)); // 기존 입찰자에게 포인트 돌려주기
                        }
                        redisUtils.saveData("userTempWallet:" + highestBid.getUserSeq(), oldBidderWallet);

                        System.out.println(oldBidderWallet.getPoint());
                    }


                    // 유저 가상지갑에서 금액 차감 및 상위 입찰 정보 수정
                    Long beforeHigh=highestBid.getUserSeq();
                    if(user.getContent().equals("user")) {
                        Optional<User> beforeUserOpt= normalUserRepository.findById(beforeHigh);
                        newBidderTempWallet.setPoint(newBidderTempWallet.getPoint() - BidderDTO.getPrice());
                        beforeUserOpt.ifPresentOrElse(
                                beforeUser -> {
                                    notificationService.sendMessageToUser(
                                            beforeHigh.toString(),
                                            beforeUser.getName()+"님 "+
                                                    "최고 입찰자가 변경되었습니다.\n" +
                                                    "최고 입찰금액 : " + highestBid.getPrice()
                                    );
                                },
                                () -> {
                                    System.out.println("해당 ID를 가진 사용자가 존재하지 않습니다: " + beforeHigh);
                                }
                        );
                    }else{
                        Optional<CompanyUser> CompanyUser= companyUserRepository.findById(beforeHigh);
                        newBidderTempWallet.setPoint(newBidderTempWallet.getPoint() - (int)(BidderDTO.getPrice()*0.1));
                        CompanyUser.ifPresentOrElse(
                                beforeUser -> {
                                    notificationService.sendMessageToUser(
                                            beforeHigh.toString(),
                                            beforeUser.getComName()+"님 "+
                                                    "최고 입찰자가 변경되었습니다.\n" +
                                                    "최고 입찰금액 : " + highestBid.getPrice()
                                    );
                                },
                                () -> {
                                    System.out.println("해당 ID를 가진 사용자가 존재하지 않습니다: " + beforeHigh);
                                }
                        );
                    }



                    highestBid.setPrice(BidderDTO.getPrice());

                    highestBid.setUserSeq(newBidderTempWallet.getUserSeq());



                    System.out.println("redis에 변경사항 저장");
                    // 변경된 데이터를 Redis 트랜잭션 내에서 저장
                    redisUtils.saveData("userTempWallet:"+newBidderTempWallet.getUserSeq(), newBidderTempWallet);
                    redisUtils.saveData("highestBid:" + highestBid.getAuctionSeq(), highestBid);
                    System.out.println(newBidderTempWallet.getPoint());
                    // 트랜잭션 커밋
                    return operations.exec();
                } catch (Exception exception) {
                    // 트랜잭션 롤백
                    operations.discard();
                    System.out.println("롤백 할께");
                    return null; // 예외 발생 시 null 반환 (적절한 에러 처리 필요)
                }
            }
        });


        //성공용 sout 들 이 밑으로 없애도 됨
        Optional<HighestBidDTO> rollbackBid =redisUtils.getData("highestBid:"+highestBid.getAuctionSeq(), HighestBidDTO.class);
        HighestBidDTO rollbackHighestBid = rollbackBid.orElse(null);


        Optional<UserTempWalletDTO> userTempWal = redisUtils.getData("userTempWallet:"+newBidderTempWallet.getUserWalletSeq(),UserTempWalletDTO.class);
        UserTempWalletDTO rollbackUserTempWallet = userTempWal.orElse(null);

        System.out.println("입찰 성공");
        System.out.println("user 지갑 남은돈 : " + rollbackUserTempWallet.getPoint());
        System.out.println("상위 입찰 유저 번호 : " + rollbackHighestBid.getUserSeq());
        System.out.println("상위 입찰 가격 번호 : " + rollbackHighestBid.getPrice());

        return bid;
    }

    /*
      2.redis에서 highestBid정보 갖고오기(경매 등록과 동시에 캐쉬에 무조건 등록할 것- 없는경우 생각 x)
     */
    public HighestBidDTO checkHighestBid(Long auctionSeq, Long userSeq) {
         HighestBidDTO highestBid =redisUtils.getData("highestBid:"+auctionSeq, HighestBidDTO.class).orElseThrow(()->  new BidException(ErrorCode.NOTFOUND_HIGHESTBID));
         if(highestBid.getUserSeq()==userSeq){
             throw new BidException(ErrorCode.HIGH_BIDDER);
         }
         return highestBid;
    }

    public HighestBidDTO getHighestBid(Long auctionSeq) {
        HighestBidDTO highestBid =redisUtils.getData("highestBid:"+auctionSeq, HighestBidDTO.class).orElseThrow(()-> new BidException(ErrorCode.NOTFOUND_HIGHESTBID));
        return highestBid;
    }


    @Override
    public List<BidListDTO> getComBids(Long auctionSeq) {
        return bidRepository.auctionCompanyBidList(auctionSeq);

    }

    @Override
    public List<BidListDTO> getUserBids(Long auctionSeq) {
        return bidRepository.auctionUerBidList(auctionSeq);
    }


    // 1.redis에 유저 지갑 임시지갑 존재하는지? ->없으면 userWallet 정보를 userTempWallet에 담아서 redis에 저장
    public UserTempWalletDTO checkUserTempWallet(Long userSeq) {
        UserTempWalletDTO userTempWallet = redisUtils.getData("userTempWallet:"+userSeq,UserTempWalletDTO.class).orElseGet(()-> {
            System.out.println("유저 가상 정보가 없어요~");
            UserWallet userWallet = walletRepository.findByUserSeq(userSeq);
            UserTempWalletDTO  wallet  = UserTempWalletDTO.builder()
                    .userWalletSeq(userWallet.getUserWalletSeq())
                    .userSeq((userWallet.getManagementUser().getUserSeq()))
                    .point(userWallet.getPoint())
                    .build();
            redisUtils.saveData("userTempWallet:"+wallet.getUserWalletSeq(),wallet);
            return wallet;
        });

        return userTempWallet;
    }





}

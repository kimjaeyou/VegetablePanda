package web.mvc;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.annotation.Rollback;
import web.mvc.domain.*;
import web.mvc.dto.*;
import web.mvc.exception.BidException;
import web.mvc.exception.ErrorCode;
import web.mvc.redis.RedisUtils;
import web.mvc.repository.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


//@SpringBootTest 통합 테스트
@Slf4j
//@DataJpaTest// 영속성 관련된 테스트
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
//jpa 영속성 context 배울때 plush나 commit때 날라가는데  모아두고 한번에 하는데 롤백될꺼나끼 실행조차 되지않은다.
//시퀀스는 올라간다. 아직 날라가지 않아도 이미 늘려놨기 떄문에
//
@RequiredArgsConstructor
public class BidTest {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private WalletRepository userWalletRepository;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ManagementRepository userManageMentRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private UserBuyRepository userBuyRepository;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();  // 테스트 전용으로 ModelMapper 빈 등록
        }

    }

    @Autowired
    private ModelMapper modelMapper;

    @Test
    @Transactional
    @DisplayName("재고 등록하기")
    public void stock(){

    }




    @Test
    @Transactional
    @DisplayName("경매 등록하기")
    public void auction() {
        Optional<Stock> optStock = stockRepository.findById(1L);
        Stock stock = optStock.orElse(Stock.builder().content("상품정보 없다`").build());
        System.out.println(stock.getProduct());

        StockDTO stockDTO = modelMapper.map(stock, StockDTO.class);
        log.info("stockDTO: {}", stockDTO);

        AuctionDTO auctionDTO = AuctionDTO.builder()
                .stockSeq(stockDTO.getStockSeq())
                .status(0)
                .count(50)
                .startTime("1111")
                .closeTime("1111")
                .build();

        Auction ac = auctionRepository.save(modelMapper.map(auctionDTO, Auction.class));
        //재고 감소 해야할듯
        AuctionDTO auction = modelMapper.map(ac, AuctionDTO.class);
        try {
            redisUtils.saveData("auction:" + auction.getAuctionSeq(), auction);
        } catch (Exception e) {
            System.out.println("Redis 저장 중 직렬화 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }        System.out.println("auction:"+auction.getAuctionSeq());
        System.out.println(ac.getCount());
        System.out.println(ac.getStatus());

        HighestBidDTO highestBidDTO = HighestBidDTO.builder()
                .auctionSeq(ac.getAuctionSeq())
                .userSeq(0L)
                .price(2000)
                .build();

        boolean success = redisUtils.saveData("highestBid:"+String.valueOf(ac.getAuctionSeq()),highestBidDTO);

        if(success){
            System.out.println("송공");
        }else{
            System.out.println("실패");
        }

        Optional<HighestBidDTO> saveHighestBid =redisUtils.getData("highestBid:"+String.valueOf(ac.getAuctionSeq()), HighestBidDTO.class);

        HighestBidDTO highestBid = saveHighestBid.orElse(null);
        if(highestBid!=null) {
            System.out.println(highestBid.getAuctionSeq());
            System.out.println(highestBid.getPrice());
            System.out.println(highestBid.getUserSeq());
        }
    }
    /*
        1.redis에 유저 지갑 임시지갑 존재하는지? ->없으면 userWallet 정보를 userTempWallet에 담아서 redis에 저장
        2.redis에서 highestBid정보 갖고오기(경매 등록과 동시에 캐쉬에 무조건 등록할 것- 없는경우 생각 x)
        3.임시지갑의 포인트가 입찰금 보다 큰지?
        4.기존 입찰자 존재시(userSeq 0이 아닐시) -> 알림(나중에 구현)
        5. 포인트차감 (role 기준으로 일반인은 100퍼, 업체는 10퍼)
        5.redis watch로 데이터 수정이 이미 된 상태면 rollback
     */
    @Test
    @Transactional
    @DisplayName("입찰하기")
    //newBidder로 BidDTO 들어올꺼
    public void bid(){
        String userWalletNo = "2";
        String highestBidNo = "32";

        redisUtils.deleteData("userTempWallet:2");
        Optional<UserTempWalletDTO> userTempWalletDTO = redisUtils.getData("userTempWallet:"+userWalletNo,UserTempWalletDTO.class);

        UserTempWalletDTO userTempWallet = userTempWalletDTO.orElseGet(()-> {
            System.out.println("유저 가상 정보가 없어요~");
            UserWallet userWallet = userWalletRepository.findByUserSeq(Long.valueOf(userWalletNo));
            UserTempWalletDTO  wallet  = UserTempWalletDTO.builder()
                    .userWalletSeq(userWallet.getUserWalletSeq())
                    .userSeq((userWallet.getManagementUser().getUserSeq()))
                    .point(userWallet.getPoint())
                    .build();
                redisUtils.saveData("userTempWallet:"+userWalletNo,wallet);
                return wallet;
        });

        System.out.println("유저 번호" + userTempWallet.getUserSeq());
        System.out.println("유저 가상지갑 : "+userTempWallet.getPoint());


        Optional<HighestBidDTO> saveHighestBid =redisUtils.getData("highestBid:"+highestBidNo, HighestBidDTO.class);
        HighestBidDTO highestBid = saveHighestBid.orElse(null);

        System.out.println("경매 번호" + highestBid.getAuctionSeq());
        System.out.println(highestBid.getPrice());
        System.out.println("상위입찰자"+highestBid.getUserSeq());
        String role = "ROLE_COMPANY";

        //입찰중이면 입찰 못하게 하기
       /* if(highestBid.getUserSeq() == userTempWallet.getUserSeq()){
            throw new BidException(ErrorCode.HIGH_BIDDER);
        }*/

        redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {

                try {
                    // 특정 키를 모니터링(watch) 설정, multi 전에 호출
                    operations.watch("highestBid:" + highestBid.getAuctionSeq());
                    // 트랜잭션 시작
                    operations.multi();

                    // 입찰자 존재 시 가상 지갑에 포인트 돌려주기
                    if (highestBid.getUserSeq() != 0) {
                        UserTempWalletDTO oldBidderWallet = redisUtils.getData("userTempWallet:" + highestBid.getUserSeq(), UserTempWalletDTO.class).orElse(null);
                        if (oldBidderWallet != null) {
                            oldBidderWallet.setPoint(oldBidderWallet.getPoint() + highestBid.getPrice()); // 기존 입찰자에게 포인트 돌려주기
                            // 업데이트된 정보를 저장해야 한다면 추가적으로 save를 호출할 수 있습니다.
                            redisUtils.saveData("userTempWallet:" + highestBid.getUserSeq(), oldBidderWallet);
                        }
                    }
                    if(userTempWallet.getPoint()<2500){//입찰 시도 금액이 지갑보다 더 작을 때 이 트랜잭션 전에 할듯
                        //오류
                        throw new BidException(ErrorCode.LOW_BID);
                    }
                    // 유저 가상지갑에서 금액 차감 및 상위 입찰 정보 수정

                    highestBid.setUserSeq(userTempWallet.getUserSeq());
                    userTempWallet.setPoint(userTempWallet.getPoint() - 2500);
                    highestBid.setPrice(2500);




                    System.out.println("redis에 변경사항 저장");
                    // 변경된 데이터를 Redis 트랜잭션 내에서 저장
                    redisUtils.saveData("userTempWallet:"+userWalletNo, userTempWallet);
                    redisUtils.saveData("highestBid:" + highestBid.getAuctionSeq(), highestBid);

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

        Optional<HighestBidDTO> rollbackBid =redisUtils.getData("highestBid:"+highestBidNo, HighestBidDTO.class);
        HighestBidDTO rollbackHighestBid = rollbackBid.orElse(null);

        Optional<UserTempWalletDTO> userTempWal = redisUtils.getData("userTempWallet:"+userWalletNo,UserTempWalletDTO.class);
        UserTempWalletDTO rollbackUserTempWallet = userTempWal.orElse(null);

        System.out.println("입찰 성공");
        System.out.println("user 지갑 남은돈 : " + rollbackUserTempWallet.getPoint());
        System.out.println("상위 입찰 유저 번호 : " + rollbackHighestBid.getUserSeq());
        System.out.println("상위 입찰 가격 번호 : " + rollbackHighestBid.getPrice());


    }

    @Test
    @Transactional
    @DisplayName("업체입찰하기")
    //newBidder로 BidDTO 들어올꺼
    public void bidCompany(){
        redisUtils.deleteData("userTempWallet:3");
        Optional<UserTempWalletDTO> userTempWalletDTO = redisUtils.getData("userTempWallet:3",UserTempWalletDTO.class);

        UserTempWalletDTO userTempWallet = userTempWalletDTO.orElseGet(()-> {
            System.out.println("유저 가상 정보가 없어요~");
            UserWallet userWallet = userWalletRepository.findByUserSeq(3L);
            UserTempWalletDTO  wallet  = UserTempWalletDTO.builder()
                    .userWalletSeq(userWallet.getUserWalletSeq())
                    .userSeq((userWallet.getManagementUser().getUserSeq()))
                    .point(userWallet.getPoint())
                    .build();
            redisUtils.saveData("userTempWallet:3",wallet);
            return wallet;
        });

        System.out.println("유저 번호" + userTempWallet.getUserSeq());
        System.out.println("유저 가상지갑 : "+userTempWallet.getPoint());


        Optional<HighestBidDTO> saveHighestBid =redisUtils.getData("highestBid:"+"16", HighestBidDTO.class);
        HighestBidDTO highestBid = saveHighestBid.orElse(null);

        System.out.println("경매 번호" + highestBid.getAuctionSeq());
        System.out.println(highestBid.getPrice());
        System.out.println("상위입찰자"+highestBid.getUserSeq());

        //입찰중이면 입찰 못하게 하기
        if(highestBid.getUserSeq() == userTempWallet.getUserSeq()){
            throw new BidException(ErrorCode.HIGH_BIDDER);
        }

        redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {

                try {
                    // 특정 키를 모니터링(watch) 설정, multi 전에 호출
                    operations.watch("highestBid:" + highestBid.getAuctionSeq());
                    // 트랜잭션 시작
                    operations.multi();


                    // 입찰자 존재 시 가상 지갑에 포인트 돌려주기
                    if (highestBid.getUserSeq() != 0) {
                        UserTempWalletDTO oldBidderWallet = redisUtils.getData("userTempWallet:" + highestBid.getUserSeq(), UserTempWalletDTO.class).orElse(null);
                        if (oldBidderWallet != null) {
                            oldBidderWallet.setPoint(oldBidderWallet.getPoint() + (int)(highestBid.getPrice() * 0.1)); // 기존 입찰자에게 포인트 돌려주기
                            // 업데이트된 정보를 저장해야 한다면 추가적으로 save를 호출할 수 있습니다.
                            redisUtils.saveData("userTempWallet:" + highestBid.getUserSeq(), oldBidderWallet);
                        }
                    }
                    highestBid.setUserSeq(userTempWallet.getUserSeq());
                    userTempWallet.setPoint(userTempWallet.getPoint() - (int)(3000*0.1));
                    highestBid.setPrice(3000);

                    if(userTempWallet.getPoint()<2500){//입찰 시도 금액이 지갑보다 더 작을 때 이 트랜잭션 전에 할듯
                        //오류
                        throw new BidException(ErrorCode.LOW_BID);
                    }
                    // 유저 가상지갑에서 금액 차감 및 상위 입찰 정보 수정


                    System.out.println("redis에 변경사항 저장");
                    // 변경된 데이터를 Redis 트랜잭션 내에서 저장
                    redisUtils.saveData("userTempWallet:3", userTempWallet);
                    redisUtils.saveData("highestBid:" + highestBid.getAuctionSeq(), highestBid);

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

        Optional<HighestBidDTO> rollbackBid =redisUtils.getData("highestBid:"+"16", HighestBidDTO.class);
        HighestBidDTO rollbackHighestBid = rollbackBid.orElse(null);

        Optional<UserTempWalletDTO> userTempWal = redisUtils.getData("userTempWallet:3",UserTempWalletDTO.class);
        UserTempWalletDTO rollbackUserTempWallet = userTempWal.orElse(null);

        System.out.println("입찰 성공");
        System.out.println("user 지갑 남은돈 : " + rollbackUserTempWallet.getPoint());
        System.out.println("상위 입찰 유저 번호 : " + rollbackHighestBid.getUserSeq());
        System.out.println("상위 입찰 가격 번호 : " + rollbackHighestBid.getPrice());


    }

    // 경매 정보 갖고오기 (redis 저장할꺼고) 경매 등록 할떄 highestBid , 입찰- 유저 가상지갑 생성 후 거기서 계산
    @Autowired
    private UserBuyDetailRepository userBuyDetailRepository;
    @Test
    @Transactional
    @DisplayName("경매 종료하기")
    public void exit(){
        String auctionNo = "32";
        Optional<AuctionDTO> auctionDTO = redisUtils.getData("auction:"+auctionNo, AuctionDTO.class);
        AuctionDTO auction = auctionDTO.orElse(null);

        auction.setStatus(1);

        Optional<HighestBidDTO> highestBidDTO = redisUtils.getData("highestBid:"+auctionNo, HighestBidDTO.class);
        //입찰자 존재 하는지 여부  1. 존재x 재고 감소x 끝 | 2. 존재시 유저번호로 일반, 업체 구분해서 구매상태 전달! 하기
        HighestBidDTO highestBid =highestBidDTO.orElse(null);

        Long userNo = highestBid.getUserSeq();

        if(userNo==0){
            //입찰자 x 그냥 경매 상태 1돌린 채로 끝나는거
            return;
        }

        ManagementUser user = userManageMentRepository.findById(Long.parseLong(String.valueOf(userNo))).orElse(null);

        String content =  user.getContent();

        if(content.equals("user")){
            //0은 일반 경매 결제
            UserBuy userBuy =userBuyRepository.save(
            UserBuy.builder()
                    .managementUser(user)
                    .state(0)
                    .totalPrice(highestBid.getPrice())
                    .build()
            );
            userBuyDetailRepository.save(modelMapper.map(
                    UserBuyDetailDTO.builder()
                            .userBuySeq(userBuy.getBuySeq())
                            .price(highestBid.getPrice())
                            .count(auction.getCount())
                            .stockSeq(auction.getStockSeq())
                            .build(),UserBuyDetail.class
            ));

            // 가상지갑상태을 db에 반영한다., 경매 팔린만큼 재고 감소시킨다.
            UserTempWalletDTO userTempWalletDTO = redisUtils.getData("userTempWallet:"+String.valueOf(highestBid.getUserSeq()),UserTempWalletDTO.class).orElse(null);
            userWalletRepository.updateWallet(userTempWalletDTO.getUserSeq(),userTempWalletDTO.getPoint());
            stockRepository.reduceCount(auction.getStockSeq(),auction.getCount());

        }else{
            //업체 경매
           /* UserBuy userBuy =userBuyRepository.save(
                    UserBuy.builder()
                            .state(2)
                            .stockPrice(5000)
                            .build()
            );*/

            //주문리스트 1개일 것
//            UserBuyDetail.builder()
//                    .userBuySeq(Math.toIntExact(userBuy.getBuySeq()))
//                    .price(highestBid.getPrice())
//                    .count(auction.getCount())
//                    .build();
            // 가상지갑상태을 db에 반영한다.
           // UserTempWalletDTO userTempWalletDTO = redisUtils.getData("userTempWallet:"+String.valueOf(highestBid.getUserSeq()),UserTempWalletDTO.class).orElse(null);

            //userWalletRepository.updateWallet(userTempWalletDTO.getUserSeq(),userTempWalletDTO.getPoint());
        }

    }


}

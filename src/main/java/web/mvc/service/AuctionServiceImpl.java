package web.mvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.*;
import web.mvc.dto.*;
import web.mvc.exception.AuctionException;
import web.mvc.exception.ErrorCode;
import web.mvc.redis.RedisUtils;
import web.mvc.repository.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");


    private final AuctionRepository auctionRepository;

    private final ModelMapper modelMapper;

    private final ManagementRepository managementRepository;

    private final UserBuyRepository userBuyRepository;

    private final UserBuyDetailRepository userBuyDetailRepository;

    private final StockRepository stockRepository;

    private final WalletRepository userWalletRepository;

    private final BidRepository bidRepository;

    private final StreamingRepository streamingRepository;

    private final StreamingService streamingService;

    private final ShopRepository shopRepository;

    private final NotificationService notificationService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisUtils redisUtils;



    @Override
    public Auction insertAuction(AuctionDTO auction,int price) {
        log.info("insert auction ServiceImpl");

        Stock stock = stockRepository.checkCount(auction.getStockSeq(),auction.getCount());
        if(stock==null) throw new AuctionException(ErrorCode.STOCK_COUNT_LESS);
        Auction saveAc = Auction.builder()
                .stock(stock)
                .closeTime(LocalDateTime.parse(auction.getCloseTime(), FORMATTER))
                .count(auction.getCount())
                .status(auction.getStatus())
                .build();
        Auction ac = auctionRepository.save(saveAc);
        AuctionDTO auctionDTO = AuctionDTO.builder()
                .closeTime(ac.getCloseTime().format(FORMATTER))
                .startTime(ac.getStartTime().format(FORMATTER))
                .status(ac.getStatus())
                .count(ac.getCount())
                .stockSeq(ac.getStock().getStockSeq())
                .auctionSeq(ac.getAuctionSeq())
                .build();
        redisUtils.saveData("auction:" + auctionDTO.getAuctionSeq(), auctionDTO);
        HighestBidDTO highestBidDTO = HighestBidDTO.builder()
                .auctionSeq(ac.getAuctionSeq())
                .userSeq(0L)
                .price(price)
                .build();
        redisUtils.saveData("highestBid:"+ac.getAuctionSeq(),highestBidDTO);

        Optional<HighestBidDTO> saveHighestBid =redisUtils.getData("highestBid:"+String.valueOf(ac.getAuctionSeq()), HighestBidDTO.class);

        HighestBidDTO highestBid = saveHighestBid.orElse(null);
        if(highestBid!=null) {
            System.out.println(highestBid.getAuctionSeq());
            System.out.println(highestBid.getPrice());
            System.out.println(highestBid.getUserSeq());
        }

        return ac;
    }
    /*
        최고가 입찰 유저 번호가 0이면 입찰한 유저가 없으므로  재고 감소, 돈 계산, 구매 정보 필요 없음
        최고가 입찰 유저 존재시 유저 번호로 content에 따라
        일반 입찰 구매 status = 0
        업체 입찰 구매(결제 대기) = 2;
        구매 정보랑 구매 리스트 생성
        최고가 입찰 기록 redis 캐쉬 삭제
        재고 감소
        업체 유저 임시 지갑을 유저 실제 지갑과 연동 시킴
        미구현* 알림 기능
     */

    @Override
    public Auction getAuction(Long userSeq) {
        return auctionRepository.findByUserSeq(userSeq);
    }

    @Override
    public int updateAuction(long auctionSeq, long farmerSeq) {
        Optional<AuctionDTO> auctionDTO = redisUtils.getData("auction:"+auctionSeq, AuctionDTO.class);
        AuctionDTO auction = auctionDTO.orElse(null);
        auction.setStatus(1);
        System.out.println(auction.toString());
        HighestBidDTO highestBidDTO = redisUtils.getData("highestBid:"+auctionSeq, HighestBidDTO.class).orElse(null);
        //입찰자 존재 하는지 여부  1. 존재x 재고 감소x 끝 | 2. 존재시 유저번호로 일반, 업체 구분해서 구매상태 전달! 하기
        auctionRepository.setStatus(highestBidDTO.getAuctionSeq());

        if(highestBidDTO.getUserSeq()==0){
            //입찰자 x 그냥 경매 상태 1돌린 채로 끝나는거
            return 1;
        }

        ManagementUser user = managementRepository.findById(highestBidDTO.getUserSeq()).orElse(null);

        if(user.getContent().equals("user")){
            //0은 일반 경매 결제
            UserBuy userBuy =userBuyRepository.save(
                    UserBuy.builder()
                            .managementUser(user)
                            .state(1)
                            .totalPrice(highestBidDTO.getPrice())
                            .build()
            );
            userBuyDetailRepository.save(modelMapper.map(
                    UserBuyDetailDTO.builder()
                            .userBuySeq(userBuy.getBuySeq())
                            .price(highestBidDTO.getPrice())
                            .count(auction.getCount())
                            .stockSeq(auction.getStockSeq())
                            .build(), UserBuyDetail.class
            ));

            // 가상지갑상태을 db에 반영한다., 경매 팔린만큼 재고 감소시킨다.
            UserTempWalletDTO userTempWalletDTO = redisUtils.getData("userTempWallet:"+highestBidDTO.getUserSeq(),UserTempWalletDTO.class).orElse(null);
            userWalletRepository.updateWallet(userTempWalletDTO.getUserSeq(),userTempWalletDTO.getPoint());
            userWalletRepository.updateWallet(farmerSeq, (long) (highestBidDTO.getPrice()));
            Stock stock = stockRepository.findById(auction.getStockSeq()).orElse(null);
            stock.setCount(stock.getCount()-auction.getCount());
            redisUtils.deleteData("highestBid:"+highestBidDTO.getAuctionSeq());
            redisUtils.deleteData("auction:" + highestBidDTO.getAuctionSeq());
            StockUserSeqDTO checkZeroStock = stockRepository.checkCountZero(auction.getStockSeq());
            if(checkZeroStock!=null){
                Streaming streaming =streamingRepository.findByFarmerUserSeq(checkZeroStock.getUserSeq());
                streamingService.exitRoomById(streaming.getStreamingSeq());
                stock.setStatus(4);
            }
    }else{
            UserBuy userBuy =userBuyRepository.save(
                    UserBuy.builder()
                            .managementUser(user)
                            .state(3)
                            .totalPrice((int) (highestBidDTO.getPrice()))
                            .build()
            );
            userBuyDetailRepository.save(modelMapper.map(
                    UserBuyDetailDTO.builder()
                            .userBuySeq(userBuy.getBuySeq())
                            .price(highestBidDTO.getPrice())
                            .count(auction.getCount())
                            .stockSeq(auction.getStockSeq())
                            .build(), UserBuyDetail.class
            ));

            // 가상지갑상태을 db에 반영한다., 경매 팔린만큼 재고 감소시킨다.
            UserTempWalletDTO userTempWalletDTO = redisUtils.getData("userTempWallet:"+highestBidDTO.getUserSeq(),UserTempWalletDTO.class).orElse(null);
            userWalletRepository.updateWallet(userTempWalletDTO.getUserSeq(),userTempWalletDTO.getPoint());
            userWalletRepository.updateWallet(farmerSeq, (long) (highestBidDTO.getPrice()*0.1));
            Stock stock = stockRepository.findById(auction.getStockSeq()).orElse(null);
            stock.setCount(stock.getCount()-auction.getCount());

            redisUtils.deleteData("highestBid:"+highestBidDTO.getAuctionSeq());
            redisUtils.deleteData("auction:" + highestBidDTO.getAuctionSeq());
            StockUserSeqDTO checkZeroStock = stockRepository.checkCountZero(auction.getStockSeq());
            if(checkZeroStock!=null){
                Streaming streaming =streamingRepository.findByFarmerUserSeq(checkZeroStock.getUserSeq());
                streamingService.exitRoomById(streaming.getStreamingSeq());
                stock.setStatus(4);
            }

        }

        if(highestBidDTO.getUserSeq()!=null){
            System.out.println("여기 종료!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            notificationService.sendMessageToUser(
                    highestBidDTO.getUserSeq().toString(),"낙찰되셨습니다.");
            notificationService.sendMessageToTopic("/end/notifications", "1");
        }

        return 1;
    }

    @Scheduled(cron = "0 * * * * ?")
    //경매 1분마다 남은시간 0 된거 찾아서 종료
    @Scheduled(cron = "0 * * * * ?")
    public void exitAuction() {
        LocalDateTime now = LocalDateTime.now();

        // 현재 시각을 "yyyy-MM-dd HH:mm:ss" 형식으로 출력
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        System.out.println("현재 시각: " + now.format(formatter));
        List<Auction> auctions = auctionRepository.exitAuctions(now);
        for(Auction auction : auctions) {
            System.out.println("경매 종료 번호:"+auction.getAuctionSeq());
            AuctionDTO auctionDTO = redisUtils.getData("auction:" + auction.getAuctionSeq(), AuctionDTO.class).orElseThrow(()-> new AuctionException(ErrorCode.AUCTION_NOTFOUND));
            auction.setStatus(1);
            auctionDTO.setStatus(1);

            //highestBid 꺼내고

            HighestBidDTO highestBidDTO = redisUtils.getData("highestBid:" + auctionDTO.getAuctionSeq(), HighestBidDTO.class).orElseThrow(()->new AuctionException(ErrorCode.AUCTION_NOTFOUND));
            //입찰자 존재 하는지 여부  1. 존재x 재고 감소x 끝 | 2. 존재시 유저번호로 일반, 업체 구분해서 구매상태 전달! 하기

            if (highestBidDTO.getUserSeq() == 0) {
                //입찰자 x 그냥 경매 상태 1돌린 채로 끝나는거
                redisUtils.deleteData("highestBid:" + highestBidDTO.getAuctionSeq());
                return;
            }

            ManagementUser user = managementRepository.findById(highestBidDTO.getUserSeq()).orElse(null);

            if (user.getContent().equals("user")) {
                //0은 일반 경매 결제
                UserBuy userBuy = userBuyRepository.save(
                        UserBuy.builder()
                                .managementUser(user)
                                .state(1)
                                .totalPrice(highestBidDTO.getPrice())
                                .build()
                );
                userBuyDetailRepository.save(modelMapper.map(
                        UserBuyDetailDTO.builder()
                                .userBuySeq(userBuy.getBuySeq())
                                .price(highestBidDTO.getPrice())
                                .count(auctionDTO.getCount())
                                .stockSeq(auctionDTO.getStockSeq())
                                .build(), UserBuyDetail.class
                ));


            } else {
                UserBuy userBuy = userBuyRepository.save(
                        UserBuy.builder()
                                .managementUser(user)
                                .state(3)
                                .totalPrice((highestBidDTO.getPrice()))
                                .build()
                );
                userBuyDetailRepository.save(modelMapper.map(
                        UserBuyDetailDTO.builder()
                                .userBuySeq(userBuy.getBuySeq())
                                .price(highestBidDTO.getPrice())
                                .count(auction.getCount())
                                .stockSeq(auctionDTO.getStockSeq())
                                .build(), UserBuyDetail.class
                ));
            }
            // 가상지갑상태을 db에 반영한다., 경매 팔린만큼 재고 감소시킨다.
            UserTempWalletDTO userTempWalletDTO = redisUtils.getData("userTempWallet:" + highestBidDTO.getUserSeq(), UserTempWalletDTO.class).orElse(null);
            userWalletRepository.updateWallet(userTempWalletDTO.getUserSeq(), userTempWalletDTO.getPoint());
            Stock stock = stockRepository.findById(auction.getStock().getStockSeq()).orElse(null);
            stock.setCount(stock.getCount()-auction.getCount());
            redisUtils.deleteData("highestBid:" + highestBidDTO.getAuctionSeq());
            redisUtils.deleteData("auction:" + highestBidDTO.getAuctionSeq());
            StockUserSeqDTO checkZeroStock = stockRepository.checkCountZero(auction.getStock().getStockSeq());
            if(checkZeroStock!=null){
                Streaming streaming =streamingRepository.findByFarmerUserSeq(checkZeroStock.getUserSeq());
                streamingService.exitRoomById(streaming.getStreamingSeq());
                stock.setStatus(4);
            }
            if(highestBidDTO.getUserSeq()!=null){
                notificationService.sendMessageToUser(
                        highestBidDTO.getUserSeq().toString(),"낙찰되셨습니다.");
                notificationService.sendMessageToTopic("/end/notifications", "1");
            }
        }
    }
    @Scheduled(cron = "0 0 18 * * ?")
   //@Scheduled(cron = "0 * * * * ?")
    public void scheduledTask() {
        System.out.println("매일 오후 6시에 실행됩니다.");
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1); // 현재 날짜에서 하루 전
        List<Stock> stocks = stockRepository.findStocksBeforeYesterday(yesterday);
        for (Stock stock : stocks) {
            stock.setStatus(3);
            Shop shop = new Shop();
            UserBuyDetailGetAvgPriceDTO avgPrice = userBuyDetailRepository.getAvgPrice(stock.getStockSeq());
            shop.setPrice((int)Math.floor(avgPrice.getPrice()));
            shop.setStock(stock.getStockSeq());
            shopRepository.save(shop);
        }
    }


    @Override
    public List<AuctionStatusDTO> getCurrentAuctions() {
        List<Auction> activeAuctions = auctionRepository.findAllActiveAuctions();
        List<AuctionStatusDTO> auctionStatuses = new ArrayList<>();

        for (Auction auction : activeAuctions) {
            AuctionStatusDTO dto = new AuctionStatusDTO();
            Stock stock = auction.getStock();

            dto.setAuctionSeq(auction.getAuctionSeq());
            dto.setStockSeq(stock.getStockSeq());
            dto.setProductName(stock.getProduct().getProductName());
            dto.setContent(stock.getContent());
            dto.setCount(auction.getCount());
            dto.setCloseTime(auction.getCloseTime());
            dto.setStatus(auction.getStatus());
            dto.setStockGrade(stock.getStockGrade().getGrade());
            dto.setStockOrganic(stock.getStockOrganic().getOrganicStatus());

            // 현재 최고 입찰가 조회
            Integer highestBid = bidRepository.findHighestBidPrice(auction.getAuctionSeq());
            dto.setCurrentPrice(highestBid != null ? highestBid : 0);

            // 총 입찰 횟수 조회
            dto.setBidCount(bidRepository.countBidsByAuction(auction.getAuctionSeq()));

            auctionStatuses.add(dto);
        }

        return auctionStatuses;
    }


}

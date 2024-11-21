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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.Rollback;
import web.mvc.domain.*;
import web.mvc.dto.AuctionDTO;
import web.mvc.dto.HighestBidDTO;
import web.mvc.dto.StockDTO;
import web.mvc.dto.UserTempWalletDTO;
import web.mvc.redis.RedisUtils;
import web.mvc.repository.*;

import java.time.LocalDate;
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
                .closeTime(LocalDate.of(2024, 11, 21).atStartOfDay())
                .build();

        Auction ac = auctionRepository.save(modelMapper.map(auctionDTO, Auction.class));
        //재고 감소 해야할듯
        redisUtils.saveData("auction:"+ac.getAuctionSeq(),auctionDTO);
        System.out.println(ac.getAuctionSeq());
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


        Optional<HighestBidDTO> saveHighestBid =redisUtils.getData("highestBid:"+"7", HighestBidDTO.class);
        HighestBidDTO highestBid = saveHighestBid.orElse(null);

        System.out.println("경매 번호" + highestBid.getAuctionSeq());
        System.out.println(highestBid.getPrice());
        System.out.println("상위입찰자"+highestBid.getUserSeq());
        String role = "ROLE_COMPANY";

        // 입찰 전 상태 백업을 위한 변수
        UserTempWalletDTO originalUserTempWallet = UserTempWalletDTO.builder()
                .point(userTempWallet.getPoint())
                .userSeq(userTempWallet.getUserSeq())
                .userWalletSeq(userTempWallet.getUserWalletSeq())
                .build(); // 복사
        HighestBidDTO originalHighestBid = HighestBidDTO.builder()
                .price(highestBid.getPrice())
                .auctionSeq(highestBid.getAuctionSeq())
                .userSeq(highestBid.getUserSeq())
                .build(); // 복사
        System.out.println(originalHighestBid.getPrice());

            if(userTempWallet.getPoint()<250){//입찰 시도 금액이 지갑보다 더 작을 때 이 트랜잭션 전에 할듯
                //오류
                System.out.println("입찰 선금 포인트가 부족합니다.");
            }
            if(highestBid.getPrice()>2500) {// 입찰금이 현재 입찰가 보다 낮으면 안됨
                //오류 발생
                System.out.println("입찰가격이 2500보다 더 커야한다.");
            }
        try {
            redisUtils.watch("highestBid:" + highestBid.getAuctionSeq()); // watch는 multi 전에 호출
            redisUtils.multi();  // 트랜잭션 시작

            // 입찰자 존재시 가상지갑에 포인트 돌려주기
            /*if (highestBid.getUserSeq() != 0) {
                UserTempWalletDTO oldBidderWallet = redisUtils.getData("userTempWallet:" + String.valueOf(highestBid.getUserSeq()), UserTempWalletDTO.class).orElse(null);
                oldBidderWallet.setPoint(oldBidderWallet.getPoint() + (int)(highestBid.getPrice() * 0.1)); // 기존 입찰자에게 포인트 돌려주기
            }*/


            // 변경된 데이터를 Redis 트랜잭션 내에서 저장
            redisUtils.saveData("userTempWallet:3", userTempWallet);
            redisUtils.saveData("highestBid:" + String.valueOf(highestBid.getAuctionSeq()), highestBid);

            // EXEC 명령어로 트랜잭션 실행
            redisUtils.exec();
            System.out.println("트랜잭션 성공");
        } catch (Exception e) {
            e.printStackTrace();
            // 트랜잭션 실패 시
            System.out.println("트랜잭션 실패: 데이터가 변경되었습니다.");
            redisUtils.discard();  // 트랜잭션 취소
            // 데이터 롤백을 수동으로 수
        }



    }

    // 경매 정보 갖고오기 (redis 저장할꺼고) 경매 등록 할떄 highestBid , 입찰- 유저 가상지갑 생성 후 거기서 계산

    @Test
    @DisplayName("경매 종료하기")
    public void exit(){
        Optional<AuctionDTO> auctionDTO = redisUtils.getData("auction:1", AuctionDTO.class);
        AuctionDTO auction = auctionDTO.orElse(null);

        auction.setStatus(1);

        Optional<HighestBidDTO> highestBidDTO = redisUtils.getData("highestBid:"+"1", HighestBidDTO.class);
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
           /* UserBuy userBuy =userBuyRepository.save(
            UserBuy.builder()
                    .state(0)
                    .stockPrice(5000)
                    .build()
            );
*/
            //주문리스트 1개일 것
//            UserBuyDetail.builder()
//                    .userBuySeq(Math.toIntExact(userBuy.getBuySeq()))
//                    .price(highestBid.getPrice())
//                    .count(auction.getCount())
//                    .build();
            // 가상지갑상태을 db에 반영한다.
            UserTempWalletDTO userTempWalletDTO = redisUtils.getData("userTempWallet:"+String.valueOf(highestBid.getUserSeq()),UserTempWalletDTO.class).orElse(null);

            userWalletRepository.updateWallet(userTempWalletDTO.getUserSeq(),userTempWalletDTO.getPoint());

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
            UserTempWalletDTO userTempWalletDTO = redisUtils.getData("userTempWallet:"+String.valueOf(highestBid.getUserSeq()),UserTempWalletDTO.class).orElse(null);

            userWalletRepository.updateWallet(userTempWalletDTO.getUserSeq(),userTempWalletDTO.getPoint());
        }

    }


}

package web.mvc.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@ToString
public class CalcPoint {
    /**
     * 필요한 컬럼
     * : 총 금액,  ,상태값,
     */
    private Long calcPointSeq; // 시퀀스 자동추가
    private Integer totalPoint; // 총 금액
    private Integer pointToCash; // 환급 금액
    private LocalDateTime tradeDate; // 정산승인날짜
    private LocalDateTime insertDate; // 신청날짜
    private Integer state; // 상태값, 0 = 정산신청전, 1 = 정산검토중 , 2 = 정산완료

    public CalcPoint(Long calcPointSeq, Integer totalPoint, Integer pointToCash,  LocalDateTime tradeDate,LocalDateTime insertDate, Integer state) {
        this.calcPointSeq = calcPointSeq;
        this.totalPoint = totalPoint;
        this.pointToCash = pointToCash;
        this.tradeDate = tradeDate;
        this.insertDate = insertDate;
        this.state = state;
    }

}
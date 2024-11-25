package web.mvc.dto;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CalcPoint {
    /**
     * 필요한 컬럼
     * : 총 금액,  ,상태값,
     */
    private Long calcPointSeq; // 시퀀스 자동추가
    private Integer point; // 총 금액
    private Integer pointToCash; // 환급 금액
    private String insertDate; // 신청날짜
    private String transDate; // 정산승인날짜
    private Integer state; // 상태값, 0 = 정산신청전, 1 = 정산검토중 , 2 = 정산완료
}
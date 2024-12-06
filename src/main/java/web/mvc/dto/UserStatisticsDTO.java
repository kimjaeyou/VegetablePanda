package web.mvc.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStatisticsDTO {
    private long userCount;
    private long farmerCount;
    private long companyCount;
}

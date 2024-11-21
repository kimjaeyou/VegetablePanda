package web.mvc.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWalletDTO {
    private int userSeq;
    private int point;
    private int userWalletSeq;
}

package web.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDTO {
    private Long userSeq;
    private String companyId;
    private String comName;
    private String ownerName;
    private String phone;
    private String address;
    private String code;
    private String email;
    private String regName;
    private String regDate;
}
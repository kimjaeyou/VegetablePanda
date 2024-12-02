package web.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
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
    private LocalDateTime regDate;
    private String path;

    public CompanyDTO(Long userSeq, String companyId, String comName, String ownerName, String phone, String address, String code, String email, String regName,LocalDateTime regDate ,String path) {
        this.userSeq = userSeq;
        this.companyId = companyId;
        this.comName = comName;
        this.ownerName = ownerName;
        this.phone = phone;
        this.address = address;
        this.code = code;
        this.email = email;
        this.regName = regName;
        this.regDate = regDate;
        this.path = path;
    }

}
package web.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GarakStructList {
    private Set<String> garakNameList;
    private GarakAuctionRslt garakAuctionRslt;
}

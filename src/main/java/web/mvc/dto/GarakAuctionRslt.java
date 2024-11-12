package web.mvc.dto;

import lombok.Data;

import java.util.List;

@Data
public class GarakAuctionRslt {
    private int list_total_count;
    private RESULT RESULT;
    private List<row> row;
}

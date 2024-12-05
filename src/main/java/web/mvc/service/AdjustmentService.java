package web.mvc.service;

import web.mvc.dto.AdjustmentDTO;

import java.util.List;

public interface AdjustmentService {
    List<AdjustmentDTO> getPendingSettlements();

    void approveSettlement(Long buySeq);
}

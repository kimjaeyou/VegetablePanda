package web.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.dto.AdjustmentDTO;
import web.mvc.service.AdjustmentService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdjustmentController {
    
    @Autowired
    private AdjustmentService adjustmentService;
    
    @GetMapping("/pending")
    public ResponseEntity<List<AdjustmentDTO>> getPendingSettlements() {
        return ResponseEntity.ok(adjustmentService.getPendingSettlements());
    }
    
    @PostMapping("/approve/{buySeq}")
    public ResponseEntity<String> approveSettlement(@PathVariable Long buySeq) {
        adjustmentService.approveSettlement(buySeq);
        return ResponseEntity.ok("정산이 승인되었습니다.");
    }
}
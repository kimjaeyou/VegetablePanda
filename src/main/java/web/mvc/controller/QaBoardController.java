package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.QaBoard;
import web.mvc.service.QaBoardService;

@RestController
@Slf4j
@RequiredArgsConstructor
public class QaBoardController {
    private final QaBoardService qaBoardService;

    /**
     * QA 등록
     * */
    @PostMapping("/QABoard")
    public ResponseEntity<?> qaSave(@RequestBody  QaBoard qaBoard) {
        SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(qaBoardService.qaSave(qaBoard),HttpStatus.CREATED);
    }

    /**
     *QA 수정
     * */
    @PutMapping("/QABoard/{QABoard_seq}")
    public ResponseEntity<?> qaUpdate(@PathVariable Long boardNoSeq, @RequestBody QaBoard qaBoard ) {
        SecurityContextHolder.getContext().getAuthentication();

        return new ResponseEntity<>(qaBoardService.qaUpdate(boardNoSeq, qaBoard),HttpStatus.OK);
    }

    /**
     *QA 조회
     * */
    @GetMapping("/QABoard/{QABoard_seq}")
    public ResponseEntity<?> qaFindBySeq(@PathVariable Long boardNoSeq){

        return new ResponseEntity<>(qaBoardService.qaFindBySeq(boardNoSeq),HttpStatus.OK);
    }

    /**
     * 전체 조회
     * */
    @GetMapping("/QABoard/")
    public ResponseEntity<?> qaFindAll(){

        return new ResponseEntity<>(qaBoardService.qaFindAll(),HttpStatus.OK);
    }

    /**
     *QA 삭제
     * */
    @DeleteMapping("/QABoard/")
    public ResponseEntity<?> qaDelete(@PathVariable Long boardNoSeq){
        SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(qaBoardService.qaDelete(boardNoSeq),HttpStatus.OK);
    }
}

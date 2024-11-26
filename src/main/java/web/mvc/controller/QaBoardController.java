package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import web.mvc.domain.QaBoard;
import web.mvc.service.QaBoardService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/QABoard")
public class QaBoardController {
    private final QaBoardService qaBoardService;

    /**
     * QA 등록
     * */
    @PostMapping("/")
    public ResponseEntity<?> qaSave(@RequestBody  QaBoard qaBoard) {

        return new ResponseEntity<>(qaBoardService.qaSave(qaBoard),HttpStatus.CREATED);
    }

    /**
     *QA 수정
     * */
    @PutMapping("/{boardNoSeq}")
    public ResponseEntity<?> qaUpdate(@PathVariable Long boardNoSeq, @RequestBody QaBoard qaBoard ) {

        return new ResponseEntity<>(qaBoardService.qaUpdate(boardNoSeq, qaBoard),HttpStatus.OK);
    }

    /**
     *QA 조회
     * */
    @GetMapping("/{boardNoSeq}")
    public ResponseEntity<?> qaFindBySeq(@PathVariable Long boardNoSeq){

        return new ResponseEntity<>(qaBoardService.qaFindBySeq(boardNoSeq),HttpStatus.OK);
    }

    /**
     * 전체 조회
     * */
    @GetMapping("/")
    public ResponseEntity<?> qaFindAll(){

        return new ResponseEntity<>(qaBoardService.qaFindAll(),HttpStatus.OK);
    }

    /**
     *QA 삭제
     * */
    @DeleteMapping("/{boardNoSeq}")
    public ResponseEntity<?> qaDelete(@PathVariable Long boardNoSeq){
        return new ResponseEntity<>(qaBoardService.qaDelete(boardNoSeq),HttpStatus.OK);
    }
    
    /**
     *  조회수 증가
     * */
    @PutMapping("/increaseReadnum/{boardNoSeq}")
    public ResponseEntity<?> increaseReadnum(@PathVariable Long boardNoSeq) {
        return new ResponseEntity<>(qaBoardService.increaseReadnum(boardNoSeq), HttpStatus.OK);
    }
}

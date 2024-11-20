package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<?> qaSave(){

        return null;
    }

    /**
     *QA 수정
     * */
    @PutMapping("/QABoard/{QABoard_seq}")
    public ResponseEntity<?> qaUpdate(){
        return null;
    }

    /**
     *QA 조회
     * */
    @GetMapping("/QABoard/{QABoard_seq}")
    public ResponseEntity<?> qaFindBySeq(){

        return null;
    }

    /**
     * 전체 조회
     * */
    @GetMapping("/QABoard/")
    public ResponseEntity<?> qaFindAll(){

        return null;
    }

    /**
     *QA 삭제
     * */
    @DeleteMapping("/QABoard/")
    public ResponseEntity<?> qaDelete(){

        return null;
    }
}

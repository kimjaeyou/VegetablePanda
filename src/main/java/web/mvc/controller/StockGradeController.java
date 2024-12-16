package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import web.mvc.domain.StockGrade;
import web.mvc.dto.StockDTO;
import web.mvc.dto.StockGradeDTO;
import web.mvc.service.StockGradeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StockGradeController {

    private final ModelMapper modelMapper;
    private final StockGradeService stockGradeService;

    // 등급 분류 조회
    @GetMapping("/stockGrade")
    public ResponseEntity<?> findAllGrade() {
        List<StockGrade> gradeList = stockGradeService.findAll();
        List<StockGradeDTO> dtoList = gradeList.stream().map(data -> modelMapper.map(data, StockGradeDTO.class)).toList();
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }
}

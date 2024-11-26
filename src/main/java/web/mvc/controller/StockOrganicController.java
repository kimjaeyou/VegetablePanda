package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import web.mvc.domain.StockOrganic;
import web.mvc.dto.StockGradeDTO;
import web.mvc.dto.StockOrganicDTO;
import web.mvc.service.StockOrganicService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StockOrganicController {

    private final ModelMapper modelMapper;
    private final StockOrganicService stockOrganicService;

    // 유기농 분류 조회
    @GetMapping("/stockOrganic")
    public ResponseEntity<?> findAllOrganic() {
        List<StockOrganic> organicList = stockOrganicService.findAll();
        List<StockOrganicDTO> dtoList = organicList.stream().map(data -> modelMapper.map(data, StockOrganicDTO.class)).toList();

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }
}

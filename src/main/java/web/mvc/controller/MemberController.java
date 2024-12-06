package web.mvc.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.mvc.domain.File;
import web.mvc.dto.GetAllUserDTO;
import web.mvc.security.CustomMemberDetails;
import web.mvc.service.FileService;
import web.mvc.service.MemberService;
import web.mvc.service.S3ImageService;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Slf4j
@Tag(name = "MemberController API", description = "Security Swagger 테스트용  API")
public class MemberController {
    private final MemberService memberService;
    private final FileService fileService;
    private final S3ImageService s3ImageService;

    @GetMapping("/members/{id}")
    public String duplicateIdCheck(@PathVariable String id) {
        return memberService.duplicateCheck(id);
    }

    @PostMapping("/members")
    public String signUp( @RequestPart("userData") GetAllUserDTO user, @RequestPart(value = "image", required = false) MultipartFile image) {
        log.info("user={}",user);

        memberService.signUp(user, image);
        return "ok";
    }

    //추가한 부분
    @GetMapping("/api/user")
    public ResponseEntity<?> getUserInfo() {
        CustomMemberDetails userDetails =
                (CustomMemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, Object> response = new HashMap<>();
        response.put("user_seq", userDetails.getUser().getUserSeq());
        response.put("id", userDetails.getUser().getId());
        response.put("name", userDetails.getUser().getName());
        response.put("role", userDetails.getUser().getRole());
        response.put("phone", userDetails.getUser().getPhone());
        response.put("address", userDetails.getUser().getAddress());

        return ResponseEntity.ok(response);
    }

    // 판매자 명단
    @GetMapping("/farmer")
    public ResponseEntity<?> farmer(){
        return new ResponseEntity<> (memberService.farmer(), HttpStatus.OK);
    }
}
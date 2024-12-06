package web.mvc.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import web.mvc.dto.GetAllUserDTO;
import web.mvc.dto.UserPurchaseStatisticsDTO;
import web.mvc.dto.UserStatisticsDTO;
import web.mvc.security.CustomMemberDetails;
import web.mvc.service.AdminService;

import java.util.Collection;
import java.util.Iterator;

@RestController
@Slf4j
@Tag(name = "AdminController API", description = "Security Swagger 테스트용  API")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/admin")
    public String admin(){
        //시큐리티에 저장된 정보 조회
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Authentication getName =  {} " , name);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomMemberDetails customMemberDetails = (CustomMemberDetails)authentication.getPrincipal();
        GetAllUserDTO m = customMemberDetails.getUser();
        log.info("customMemberDetails =  {} ,{} ,{} " , m.getId(), m.getName(), m.getRole());


        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        while(iter.hasNext()){
            GrantedAuthority auth = iter.next();
            String role = auth.getAuthority();
            log.info("Authentication role =  {} " , role);
        }

        return "admin 입니다.";
    }


    @GetMapping("/user/statistics")
    public ResponseEntity<UserStatisticsDTO> getUserDistribution() {
        UserStatisticsDTO distribution = adminService.userStatistics();
        return ResponseEntity.ok(distribution);
    }

        @GetMapping("/purchase")
        public ResponseEntity<UserPurchaseStatisticsDTO> getPurchaseStatistics() {
            UserPurchaseStatisticsDTO statistics = adminService.getPurchaseStatistics();
            return ResponseEntity.ok(statistics);
        }



//    @GetMapping("/test/testAPI")
//    public String testAPI() throws Exception {
//        long beforeTime = System.currentTimeMillis();
//
//        ApiDataCofig apiDataCofig = new ApiDataCofig();
//        List<GarakDTO> list= ApiDataCofig.calcGarakAvg();//가락 시장 api데이터 평균 계산 결과값 리턴
//        long afterTime = System.currentTimeMillis();
//        long secDiffTime = (afterTime - beforeTime)/1000;
//        System.out.println(list);//두 시간에 차 계산
//        System.out.println("소요시간(s) : "+secDiffTime);
//        return "testAPI";
//    }

}
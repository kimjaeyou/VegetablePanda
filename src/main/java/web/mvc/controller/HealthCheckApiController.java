package web.mvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckApiController {
    @GetMapping("/check")
    public ResponseEntity healthCheck() {
        return ResponseEntity.ok().body("OK");
    }
}


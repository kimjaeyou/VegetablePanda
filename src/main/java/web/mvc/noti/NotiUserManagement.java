package web.mvc.noti;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebFilter
@Configuration
public class NotiUserManagement implements ServletContextListener {
    private final Map<Long,String> notiUserMap = new HashMap<>();

    @Override
    public void contextInitialized(ServletContextEvent e) {
        System.out.println("notiUserManagement.contextInitialized");
        e.getServletContext().setAttribute("notiUserMap", notiUserMap);
    }
}

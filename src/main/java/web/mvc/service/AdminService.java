package web.mvc.service;

import web.mvc.dto.UserPurchaseStatisticsDTO;
import web.mvc.dto.UserStatisticsDTO;

public interface AdminService {
    UserStatisticsDTO userStatistics();

    UserPurchaseStatisticsDTO getPurchaseStatistics();
}

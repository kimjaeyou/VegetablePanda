package web.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.mvc.dto.UserPurchaseStatisticsDTO;
import web.mvc.dto.UserStatisticsDTO;
import web.mvc.repository.ManagementRepository;
import web.mvc.repository.UserBuyRepository;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final ManagementRepository managementUserRepository;
    private final UserBuyRepository userBuyRepository;


    @Override
    public UserStatisticsDTO userStatistics() {
        long userCount = managementUserRepository.countByContent("user");
        long farmerCount = managementUserRepository.countByContent("farmer");
        long companyCount = managementUserRepository.countByContent("company");

        return new UserStatisticsDTO(userCount, farmerCount, companyCount);
    }

    @Override
    public UserPurchaseStatisticsDTO getPurchaseStatistics() {
        long auctionPurchaseCount = userBuyRepository.countAuctionPurchases();
        long productPurchaseCount = userBuyRepository.countProductPurchases();
        long companyAuctionPurchaseCount = userBuyRepository.countCompanyAuctionPurchases();

        return new UserPurchaseStatisticsDTO(auctionPurchaseCount, productPurchaseCount, companyAuctionPurchaseCount);
    }
}
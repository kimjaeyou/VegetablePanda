package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.mvc.domain.UserWallet;

public interface WalletRepository extends JpaRepository<UserWallet , Long> {
}

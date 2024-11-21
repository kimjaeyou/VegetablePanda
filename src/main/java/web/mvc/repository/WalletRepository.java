package web.mvc.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.UserWallet;

public interface WalletRepository extends JpaRepository<UserWallet , Long> {
    @Query("select u from UserWallet u where u.managementUser.userSeq = ?1")
    public UserWallet findByUserSeq(Integer userSeq);

    @Transactional
    @Modifying
    @Query("update UserWallet u set u.point = ?2 where u.managementUser.userSeq=?1")
    public void updateWallet(Integer userSeq, Integer point);
}

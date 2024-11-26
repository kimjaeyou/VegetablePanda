package web.mvc.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.UserWallet;

public interface WalletRepository extends JpaRepository<UserWallet , Long> {
    // 포인트값 조회해서 리턴시켜주자
    @Query("select p.point from UserWallet p where p.managementUser.userSeq=?1")
    int point (Long seq);

    @Query("select u from UserWallet u where u.managementUser.userSeq = ?1")
    public UserWallet findByUserSeq(Long userSeq);

    @Transactional
    @Modifying
    @Query("update UserWallet u set u.point = ?2 where u.managementUser.userSeq=?1")
    public void updateWallet(Long userSeq, Integer point);

}

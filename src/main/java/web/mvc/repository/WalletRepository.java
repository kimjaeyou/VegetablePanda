package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.UserWallet;

public interface WalletRepository extends JpaRepository<UserWallet , Long> {
    // 포인트값 조회해서 리턴시켜주자
    @Query("select p.point from UserWallet p where p.managementUser.userSeq=?1")
    int point (Long seq);


}

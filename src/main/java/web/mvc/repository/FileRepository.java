package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.File;

public interface FileRepository extends JpaRepository<File, Long> {

    @Query("select f.path from File f left JOIN ManagementUser m on f.fileSeq = m.file.fileSeq where m.file.name = ?1")
    String selectFile(String name);

    @Query("select f.path from File f left JOIN ManagementUser m on f.fileSeq = m.file.fileSeq where m.userSeq = ?1 ")
    String selectPath(Long seq);

    @Transactional
    @Modifying
    @Query("update File f set f.path = ?1 WHERE f.name = ?2")
    void updatePath(String updatePath, String id);

    @Transactional
    @Modifying
    @Query("update File f set f.path=null WHERE f.name = ?1")
    void deletePath(String id);

}

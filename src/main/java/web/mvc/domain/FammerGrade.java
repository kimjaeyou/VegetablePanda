package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "fammer_grade")
@Getter
@Setter
@NoArgsConstructor
public class FammerGrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fammer_grade_seq")
    private Integer fammerGradeSeq;

    @Column(name = "grade_content", nullable = false, length = 60)
    private String gradeContent;

    @OneToMany(mappedBy = "fammerGrade", fetch = FetchType.LAZY)
    private List<FarmerUser> farmerUsers;
}

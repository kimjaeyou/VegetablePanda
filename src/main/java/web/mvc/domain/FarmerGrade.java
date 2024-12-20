package web.mvc.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "farmer_grade")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties("farmerUsers")
public class FarmerGrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "farmer_grade_seq")
    private Long farmerGradeSeq;

    @Column(name = "grade_content", nullable = false, length = 60)
    private String gradeContent;

    @OneToMany(mappedBy = "farmerGrade", fetch = FetchType.LAZY)
    private List<FarmerUser> farmerUsers;

    public FarmerGrade(Long farmerGrade) {
        this.farmerGradeSeq =farmerGrade;
    }
    @Override
    public String toString() {
        return gradeContent;
    }
}

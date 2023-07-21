package daehakro.server.domain.member;

import daehakro.server.domain.member.enums.Department;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "univ_info")
public class UnivInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "univ_id")
    private Long univId;

    private String domain;

    private String name;

    @Enumerated(EnumType.STRING)
    private Department department;

    @Builder
    public UnivInfo(String domain, String name, Department department) {
        this.domain = domain;
        this.name = name;
        this.department = department;
    }
}

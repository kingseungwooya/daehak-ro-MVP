package daehakro.server.domain.member;

import daehakro.server.domain.member.enums.Department;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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

    @OneToOne(mappedBy = "univInfo")
    private Member member;

    private String mail;

    private String name;

    @Enumerated(EnumType.STRING)
    private Department department;

    @Builder
    public UnivInfo(String mail, String name, Department department, Member member) {
        this.mail = mail;
        this.name = name;
        this.department = department;
        this.member = member;
    }
}

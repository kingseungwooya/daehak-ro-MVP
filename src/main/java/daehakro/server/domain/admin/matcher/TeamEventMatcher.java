package daehakro.server.domain.admin.matcher;

import daehakro.server.domain.member.Member;
import daehakro.server.domain.member.team.Team;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.Getter;



@Getter
public class TeamEventMatcher {

    private static final int START_NUMBER = 0;
    private static final int MAX_LOOP_COUNT = 10000;
    private List<List<Team>> selectedTeams;


    public TeamEventMatcher(final List<Team> menTeams, final List<Team> womanTeams) {
        this.selectedTeams = makeTeam(menTeams, womanTeams);
    }

    public List<List<Team>> makeTeam(
            final List<Team> menTeams,
            final List<Team> womanTeams
    ) {
        int loopCount = 0;
        List<Team> availableMenTeams = new ArrayList<>(menTeams);
        List<Team> availableWomenTeams = new ArrayList<>(womanTeams);
        List<List<Team>> selectedCouples = new ArrayList<>();

        while (!availableMenTeams.isEmpty() || !availableWomenTeams.isEmpty()) {
            loopCount++;

            // 남자 리스트에서 랜덤하게 한 팀 뽑기
            System.out.println("------------size");
            System.out.println(availableMenTeams.size());
            System.out.println(availableWomenTeams.size());

            int randomManIndex = new Random().nextInt(availableMenTeams.size());
            Team manTeam = availableMenTeams.get(randomManIndex);
            Member applicantOfMan = manTeam.getMembers().stream().
                    filter(m -> m.getMemberId() == manTeam.getApplicantId())
                    .findFirst().get();
            System.out.println("men index  -------" + randomManIndex);
            System.out.println("men applicant  -------" + applicantOfMan.getMemberId());

            // 여자 리스트에서 랜덤하게 한 팀 뽑기
            int randomWomanIndex = new Random().nextInt(availableWomenTeams.size());

            Team womanTeam = availableWomenTeams.get(randomWomanIndex);
            Member applicantOfWoman = womanTeam.getMembers().stream().
                    filter(m -> m.getMemberId() == womanTeam.getApplicantId())
                    .findFirst().get();
            System.out.println("women index  -------" + randomWomanIndex);
            System.out.println("men applicant  -------" + applicantOfWoman.getMemberId());
            System.out.println(manTeam.getExcludedDepartments().size());
            System.out.println(womanTeam.getExcludedDepartments().size());
            System.out.println((manTeam.getExcludedDepartments().stream()
                    .anyMatch(d -> womanTeam.getMembers().stream()
                            .anyMatch(
                                    m -> m.getUnivInfo().getDepartment().equals(d.getExcDepartment())
                            )
                    )
            ));
            if ((womanTeam.getExcludedDepartments().stream()
                    .anyMatch(d -> manTeam.getMembers().stream()
                            .anyMatch(
                                    m -> m.getUnivInfo().getDepartment().equals(d.getExcDepartment())
                            )
                    )
            ) ||
                    (manTeam.getExcludedDepartments().stream()
                            .anyMatch(d -> womanTeam.getMembers().stream()
                                    .anyMatch(
                                            m -> m.getUnivInfo().getDepartment().equals(d.getExcDepartment())
                                    )
                            )
                    )

            ) {
                System.out.println("매칭실패");
                // 무한루프 방지용 추후 수정 필요
                if (loopCount > MAX_LOOP_COUNT) {
                    System.out.println("-------------------------------------------------max loop");
                    break;
                }

                continue;
            }


            // 매칭된 커플을 결과 리스트에 추가하고, 뽑힌 인덱스는 제거
            selectedCouples.add(List.of(manTeam, womanTeam));
            availableMenTeams.remove(randomManIndex);
            availableWomenTeams.remove(randomWomanIndex);
        }


        return selectedCouples;
    }


}

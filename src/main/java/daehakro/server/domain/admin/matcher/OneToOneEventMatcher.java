package daehakro.server.domain.admin.matcher;

import daehakro.server.domain.member.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.Getter;

/**
 * 알고리즘 고도화 필요..
 */
@Getter
public class OneToOneEventMatcher {

    private static final int START_NUMBER = 0;
    private static final int MAX_LOOP_COUNT = 10000;
    private List<List<Member>> selectedCouples;

    public OneToOneEventMatcher(
            final List<Member> mens,
            final List<Member> women) {
        this.selectedCouples = makeCouple(mens, women);
    }

    private List<List<Member>> makeCouple(
            final List<Member> mens,
            final List<Member> women
    ) {
        List<Member> availableMens = new ArrayList<>(mens);
        List<Member> availableWomen = new ArrayList<>(women);
        List<List<Member>> selectedCouples = new ArrayList<>();
        int loopCount = 0;
        while (!availableMens.isEmpty() || !availableWomen.isEmpty()) {
            loopCount++;

            // 남자 리스트에서 랜덤하게 한 명 뽑기
            int randomManIndex = new Random().nextInt(availableMens.size());
            Member man = availableMens.get(randomManIndex);

            // 여자 리스트에서 랜덤하게 한 명 뽑기
            int randomWomanIndex = new Random().nextInt(availableWomen.size());
            Member woman = availableWomen.get(randomWomanIndex);

            // 제외를 원하는 과에 포함되어 있는가
            // 같은 과에 속하는지 확인 -> 함수로 빼내야함 추후에..
            if (man.getUnivInfo().getDepartment().equals(woman.getUnivInfo().getDepartment()) ||
                    man.getExcludedDepartments().stream()
                            .anyMatch(
                                    d -> d.getExcDepartment().equals(woman.getUnivInfo().getDepartment())
                            ) ||
                    woman.getExcludedDepartments().stream()
                            .anyMatch(
                                    d -> d.getExcDepartment().equals(man.getUnivInfo().getDepartment())
                            )
            ) {
                // 무한루프 방지용 추후 수정 필요
                if (loopCount > MAX_LOOP_COUNT) {
                    break;
                }
                // 모든 학생들이 같은 과에 속한 경우 루프를 종료
                boolean allInSameDepartment = availableMens.stream()
                        .allMatch(m -> m.getUnivInfo().getDepartment().equals(man.getUnivInfo().getDepartment()))
                        && availableWomen.stream()
                        .allMatch(w -> w.getUnivInfo().getDepartment().equals(woman.getUnivInfo().getDepartment()));

                if (allInSameDepartment) {
                    break;
                }
                // 같은 과라면 다시 랜덤 매칭 수행
                continue;
            }

            // 매칭된 커플을 결과 리스트에 추가하고, 뽑힌 인덱스는 제거
            selectedCouples.add(List.of(man, woman));
            availableMens.remove(randomManIndex);
            availableWomen.remove(randomWomanIndex);
        }

        return selectedCouples;
    }




}

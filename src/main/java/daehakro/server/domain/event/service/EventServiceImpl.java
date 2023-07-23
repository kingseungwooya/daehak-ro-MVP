package daehakro.server.domain.event.service;

import daehakro.server.domain.admin.controller.dto.response.EventResDto;
import daehakro.server.domain.event.Event;
import daehakro.server.domain.event.EventLog;
import daehakro.server.domain.event.controller.dto.request.MemberApplyForm;
import daehakro.server.domain.event.controller.dto.request.TeamApplyForm;
import daehakro.server.domain.event.repo.EventLogRepository;
import daehakro.server.domain.event.repo.EventRepository;
import daehakro.server.domain.event.repo.TeamEventRepository;
import daehakro.server.domain.event.team.TeamEvent;
import daehakro.server.domain.member.ExcludedDepartment;
import daehakro.server.domain.member.Member;
import daehakro.server.domain.member.enums.Department;
import daehakro.server.domain.member.enums.MemberSex;
import daehakro.server.domain.member.repo.ExcludedDepartmentRepository;
import daehakro.server.domain.member.repo.MemberRepository;
import daehakro.server.domain.member.repo.TeamRepository;
import daehakro.server.domain.member.team.Team;
import daehakro.server.global.exception.ResponseEnum;
import daehakro.server.global.exception.handler.CustomApiException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;
    private final TeamEventRepository teamEventRepository;
    private final TeamRepository teamRepository;
    private final EventLogRepository eventLogRepository;
    private final ExcludedDepartmentRepository excludedDepartmentRepository;

    /**
     * 검증필요
     * 인증된 사용자인가?
     * 쿠폰을 사용하였는가?
     * 결제를 하였는가?
     * 신청기간이 지났는가?
     */

    /**
     * event 신청 절차
     * 존재하는 event인가
     * 존재하는 사용자인가
     * 신청을 위한 최소한의 코인을 가지고 있는가?
     * 중복되는 이벤트를 신청한 경우가 있는가?
     * 이벤트 참여인원 제한이 걸렸는가
     *
     * @param applyForm
     */

    @Override
    public void applyEvent(MemberApplyForm applyForm) {
        Event event = eventRepository.findById(applyForm.getEventId()).orElseThrow(
                () -> new CustomApiException(ResponseEnum.EVENT_NOT_EXIST)
        );
        memberRepository.findById(applyForm.getMemberId());
        Member member = memberRepository.findById(applyForm.getMemberId()).orElseThrow(
                () -> new CustomApiException(ResponseEnum.USER_NOT_FOUND)
        );
        // 멤버가 이미 참여하고 있는 이벤트이거나, 아직 종료되지 않은 이벤트를 신청중일 시 error
        if (isDuplicateApply(member, event.getEventId())) {
            throw new CustomApiException(ResponseEnum.EVENT_APPLICANT_ALREADY_EXIST);
        }
        if (!member.haveCoin()) {
            throw new CustomApiException(ResponseEnum.COIN_DOES_NOT_EXIST);
        }
        if (event.isFull(member.getSex())) {
            throw new CustomApiException(ResponseEnum.EVENT_MATCH_FULL);
        }

        // 신청 로그 저장
        EventLog eventLog = EventLog.builder()
                .member(member)
                .eventId(event.getEventId())
                .build();
        eventLogRepository.save(eventLog);
        // member 필드에 eventLog 저장
        member.applyEvent(eventLog);

        // 제외할 과 저장
        for (Department department : applyForm.getExcludeDepartments()) {
            member.addExcDepartment(excludedDepartmentRepository.save(
                    ExcludedDepartment.builder()
                            .eventId(event.getEventId())
                            .member(member)
                            .excDepartment(department)
                            .build()));
        }
        // event 추가 알아서 save 일어남
        event.apply(member);
    }

    private boolean isDuplicateApply(Member member, Long eventId) {
        if (
                eventLogRepository.existsByEventIdAndMember(eventId, member) ||
                        eventLogRepository.existsByMemberAndIsClose(member, false)
        ) {
            return true;
        }
        return false;
    }

    @Override
    public void applyTeamEvent(TeamApplyForm applyForm) {
        TeamEvent event = teamEventRepository.findById(applyForm.getEventId()).orElseThrow(
                () -> new CustomApiException(ResponseEnum.EVENT_NOT_EXIST)
        );

        // event 신청 인원과 같은지 검증
        if (event.getEventType().getLimitOfEachSex() != applyForm.getMemberIds().size()) {
            throw new CustomApiException(ResponseEnum.EVENT_APPLICANTS_NUM_FAIL);
        }

        List<Member> members = applyForm.getMemberIds().stream().
                map(
                        m -> memberRepository.findById(m).orElseThrow(
                                () -> new CustomApiException(ResponseEnum.USER_NOT_FOUND)
                        )
                ).collect(Collectors.toList());
        // 신청 팀원들 이미 참여하는 event가 있는지 확인
        for (Member member : members) {
            if (isDuplicateApply(member, event.getEventId())) {
                throw new CustomApiException(ResponseEnum.EVENT_APPLICANT_ALREADY_EXIST);
            }
        }

        Member applicant = memberRepository.findById(applyForm.getApplicant()).get();
        // 신청 대표의 coin 소유 여부 확인
        if (!applicant.haveCoin(event.getEventType().getLimitOfEachSex())) {
            throw new CustomApiException(ResponseEnum.COIN_DOES_NOT_EXIST);
        }

        List<MemberSex> memberSexes = members.stream()
                .map(m -> m.getSex())
                .collect(Collectors.toList());
        // 같은 성을 가진사람끼리 지원했는지 확인
        if (!isAllEnumsEqual(memberSexes)) {
            throw new CustomApiException(ResponseEnum.EVENT_APPLICANT_FORM_FAIL);
        }
        // 지원한 이벤트의 자리가 남았는지 확인
        if (event.isFull(memberSexes.get(0))) {
            throw new CustomApiException(ResponseEnum.EVENT_MATCH_FAIL);
        }
        // member들 이벤트 신청 현황 등록
        for (Member member : members) {
            EventLog eventLog = EventLog.builder()
                    .member(member)
                    .eventId(event.getEventId())
                    .build();
            eventLogRepository.save(eventLog);
            member.applyEvent(eventLog);
        }
        // excluded 설정 대표만 설정한다. 굳이 나머지 팀원들도 할 필요는 없다.


        Team team = Team.builder()
                .teamSex(memberSexes.get(0))
                .members(members)
                .event(event)
                .teamName(applyForm.getTeamName())
                .applicantId(applicant.getMemberId())
                .build();
        for (Department department : applyForm.getExcludeDepartments()) {
            team.addExcDepartment(excludedDepartmentRepository.save(
                    ExcludedDepartment.builder()
                            .eventId(event.getEventId())
                            .member(applicant)
                            .excDepartment(department)
                            .build()));
        }

        teamRepository.save(team);
        event.applyTeam(team);
    }

    /**
     * 생성되었던 event들을 가져온다. 생성 순으로
     */
    @Override
    public List<EventResDto> getAllEvents() {
        List<Event> events = eventRepository.findAllByOrderByCreateAtDesc();

        return events.stream()
                .map(e ->
                        EventResDto.builder()
                                .eventId(e.getEventId())
                                .maxApply(e.getMaxApply())
                                .startDate(e.getStartDate())
                                .endDate(e.getEndDate())
                                .createAt(e.getCreateAt())
                                .open(e.isOpen())
                                .manApply(e.getMembersOfMan().size())
                                .womanApply(e.getMembersOfWomen().size())
                                .eventType(e.getEventType())
                                .build())
                .collect(Collectors.toList());
    }


    public boolean isAllEnumsEqual(List<MemberSex> enumList) {
        if (enumList == null || enumList.isEmpty()) {
            return false;
        }

        MemberSex firstEnum = enumList.get(0);

        for (MemberSex enumValue : enumList) {
            if (enumValue != firstEnum) {
                return false;
            }
        }

        return true;
    }


}

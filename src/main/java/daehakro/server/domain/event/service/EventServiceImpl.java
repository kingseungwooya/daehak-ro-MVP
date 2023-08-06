package daehakro.server.domain.event.service;

import daehakro.server.domain.admin.controller.dto.response.EventResDto;
import daehakro.server.domain.event.Event;
import daehakro.server.domain.event.EventLog;
import daehakro.server.domain.event.controller.dto.request.MemberApplyForm;
import daehakro.server.domain.event.controller.dto.request.TeamApplyForm;
import daehakro.server.domain.event.controller.dto.response.MemberEventDto;
import daehakro.server.domain.event.enums.EventType;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
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


    @Override
    @Transactional
    public void applyEvent(MemberApplyForm applyForm) {
        Event event = eventRepository.findById(applyForm.getEventId()).orElseThrow(
                () -> new CustomApiException(ResponseEnum.EVENT_NOT_EXIST)
        );
        memberRepository.findById(applyForm.getMemberId());
        Member member = memberRepository.findById(applyForm.getMemberId()).orElseThrow(
                () -> new CustomApiException(ResponseEnum.USER_NOT_FOUND)
        );
        if (event.isDateWithinRange()) {
            throw new CustomApiException(ResponseEnum.EVENT_ENDED);
        }
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
                .eventType(event.getEventType())
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
        eventRepository.save(event);
    }

    private boolean isDuplicateApply(Member member, Long eventId) {
        if (
                eventLogRepository.existsByEventIdAndMember(eventId, member) || // 해당  event를  참여하고 있는지
                        eventLogRepository.existsByMemberAndIsClose(member, false) // 참여하고 있는게 있을시.. 참여 불가능
        ) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void applyTeamEvent(TeamApplyForm applyForm) {
        TeamEvent event = teamEventRepository.findById(applyForm.getEventId()).orElseThrow(
                () -> new CustomApiException(ResponseEnum.EVENT_NOT_EXIST)
        );

        // event 신청 인원과 같은지 검증
        if (event.getEventType().getLimitOfEachSex() != applyForm.getMemberIds().size()) {
            throw new CustomApiException(ResponseEnum.EVENT_APPLICANTS_NUM_FAIL);
        }
        if (event.isDateWithinRange()) {
            throw new CustomApiException(ResponseEnum.EVENT_ENDED);
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
                    .eventType(event.getEventType())
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
     * 생성되었던 event들을 가져온다. 생성 순으로 -> type별로도
     */
    @Override
    public List<EventResDto> getAllEvents() {
        List<TeamEvent> teamEvents = teamEventRepository.findAllByOrderByCreateAtDesc();
        List<Event> events = eventRepository.findAllByOrderByCreateAtDesc();
        List<EventResDto> responses = new ArrayList<>();
        responses.addAll(teamEvents.stream()
                .map(
                        t -> new EventResDto(t)
                )
                .collect(Collectors.toList())
        );
        responses.addAll(events.stream()
                .map(
                        e -> new EventResDto(e)
                )
                .collect(Collectors.toList())
        );
        return responses;
    }

    /**
     * 내가 참여한 이벤트 목록들 가져오기
     */
    @Override
    public List<MemberEventDto> getMyEvents(String uid) {
        Member member = memberRepository.findById(uid).orElseThrow(
                () -> new CustomApiException(ResponseEnum.USER_NOT_FOUND)
        );
        List<MemberEventDto> memberEventDtos = new ArrayList<>();

        List<EventLog> myEvents = eventLogRepository.findByMember(member);
        for (EventLog eventLog : myEvents) {
            if (eventLog.getEventType().equals(EventType.ONE_ONE)) {
                Event event = eventRepository.findById(eventLog.getEventId()).get();
                MemberEventDto memberEventDto = new MemberEventDto(event);
                memberEventDtos.add(memberEventDto);
                continue;
            }
            TeamEvent event = teamEventRepository.findById(eventLog.getEventId()).get();
            MemberEventDto memberEventDto = new MemberEventDto(event);
            memberEventDtos.add(memberEventDto);
        }
        return memberEventDtos;
    }

    /**
     * endDate가 끝나지 않은 event들 반환 -> user쪾에서 받을 정보
     * @return
     */
    @Override
    public List<EventResDto> getOpenedEvents() {
        LocalDate today = LocalDate.now();
        List<TeamEvent> teamEvents = teamEventRepository.findAllByEndDateGreaterThanEqualOrderByStartDate(today);
        List<Event> events = eventRepository.findAllByEndDateGreaterThanEqualOrderByStartDate(today);
        List<EventResDto> responses = new ArrayList<>();
        responses.addAll(teamEvents.stream()
                .map(
                        t -> new EventResDto(t)
                )
                .collect(Collectors.toList())
        );
        responses.addAll(events.stream()
                .map(
                        e -> new EventResDto(e)
                )
                .collect(Collectors.toList())
        );

        return responses;
    }


    /**
     * User 화면에서 이벤트들 불러오기
     * 현재 참여 가능한 event만
     */


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

package daehakro.server.domain.admin.service;

import daehakro.server.domain.admin.controller.dto.request.EventDto;
import daehakro.server.domain.admin.controller.dto.response.CoupleDto;
import daehakro.server.domain.admin.controller.dto.response.EventMatchDto;
import daehakro.server.domain.admin.controller.dto.response.EventMatchDto.EventMatchDtoBuilder;
import daehakro.server.domain.admin.controller.dto.response.EventResDto;
import daehakro.server.domain.admin.controller.dto.response.MatchedMemberDto;
import daehakro.server.domain.admin.matcher.OneToOneEventMatcher;
import daehakro.server.domain.admin.matcher.TeamEventMatcher;
import daehakro.server.domain.event.Couple;
import daehakro.server.domain.event.Event;
import daehakro.server.domain.event.EventLog;
import daehakro.server.domain.event.enums.EventType;
import daehakro.server.domain.event.repo.CoupleRepository;
import daehakro.server.domain.event.repo.EventLogRepository;
import daehakro.server.domain.event.repo.EventRepository;
import daehakro.server.domain.event.repo.TeamEventRepository;
import daehakro.server.domain.event.team.TeamEvent;
import daehakro.server.domain.member.Member;
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
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
    private final MemberRepository memberRepository;
    private final EventRepository eventRepository;
    private final TeamEventRepository teamEventRepository;
    private final EventLogRepository eventLogRepository;
    private final ExcludedDepartmentRepository excludedDepartmentRepository;
    private final CoupleRepository coupleRepository;

    /**
     * random sorting 조건은? 대학별? 등등 추후 구현해야할듯?
     */
    @Override
    public void randomMatch(Long eventId) {
        if (eventRepository.existsById(eventId)) {
            match(eventId);
            return;
        }
        if (teamEventRepository.existsById(eventId)) {
            teamMatch(eventId);
            return;
        }
        throw new CustomApiException(ResponseEnum.EVENT_NOT_EXIST);
    }

    @Transactional
    public void teamMatch(Long eventId) {
        TeamEvent teamEvent = teamEventRepository.findById(eventId).get();
        if (isEnd(teamEvent.getEndDate())) {
            throw new CustomApiException(ResponseEnum.EVENT_NOT_ENDED);
        }
        TeamEventMatcher memberEventMatcher = new TeamEventMatcher(teamEvent.getManTeams(), teamEvent.getWomenTeams());
        List<List<Team>> selectedCouples = memberEventMatcher.getSelectedTeams();

        for (List<Team> coupleTeam : selectedCouples) {
            List<Member> members = new ArrayList<>();
            coupleTeam.stream().map(
                    t -> members.addAll(t.getMembers())
            );
            // 신청 대표 팀 코인 사용
            coupleTeam.stream()
                    .forEach(
                            s -> memberRepository.findById(s.getApplicantId()).get()
                                    .useCoin(teamEvent.getEventType().getLimitOfEachSex())
                    );

            Couple coupleE = coupleRepository.save(Couple.builder()
                    .members(members)
                    .eventId(teamEvent.getEventId())
                    .eventType(teamEvent.getEventType())
                    .build());
            members.stream().forEach(
                    s -> eventLogRepository.findByMemberAndEventId(s, eventId)
                            .match(coupleE.getCoupleId())
            );
        }
        teamEvent.match();

    }

    @Transactional
    public void match(Long eventId) {
        Event event = eventRepository.findById(eventId).get();
        if (isEnd(event.getEndDate())) {
            throw new CustomApiException(ResponseEnum.EVENT_NOT_ENDED);
        }
        // matching 은 Matcher 에게 넘긴다.
        OneToOneEventMatcher memberEventMatcher =
                new OneToOneEventMatcher(event.getMembersOfMan(),
                        event.getMembersOfWomen());
        List<List<Member>> selectedCouples = memberEventMatcher.getSelectedCouples();

        for (List<Member> couple : selectedCouples) {
            // Couple 객체를 생성한다.
            couple.stream().forEach(Member::useCoin);
            logger.info(" couple  생성 완료!!");
            Couple coupleE = coupleRepository.save(Couple.builder()
                    .members(couple)
                    .eventId(event.getEventId())
                    .eventType(event.getEventType())
                    .build());
            couple.stream().forEach(
                    s -> eventLogRepository.findByMemberAndEventId(s, eventId)
                            .match(coupleE.getCoupleId())
            );
        }
        event.match();
    }


    private boolean isEnd(LocalDate endDate) {
        // 마감일짜보다 일찍 matching을 할 시
        if (endDate.compareTo(LocalDate.now()) <= 0) {
            return false;
        }
        return true;
    }

    @Override
    public void closeEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new CustomApiException(ResponseEnum.EVENT_NOT_EXIST)
        );
        event.close();
        // eventLog도 모두 isClose로 바꿔줄것 삭제는 하지 않는다.
        List<EventLog> logs = eventLogRepository.findByEventId(eventId);
        logs.stream().forEach(EventLog::close);
        // 이벤트마다 ban 할 department를 정하기에 이것도 모두 제거
        excludedDepartmentRepository.deleteAllByEventId(eventId);

    }

    @Transactional
    @Override
    public EventMatchDto getMatchedCouples(Long eventId) {
        // 해당 event 에 대해서 매칭된 커플들
        Event event = eventRepository.findById(eventId).get();
        List<Couple> couples = coupleRepository.findAllByEventIdAndEventType(eventId, event.getEventType());
        return EventMatchDto.builder()
                .couples(couples.stream()
                        .map(
                                c -> new CoupleDto(
                                        c.getMembers()
                                                .stream()
                                                .map(
                                                        s -> new MatchedMemberDto(s)
                                                )
                                                .collect(Collectors.toList()),
                                        c.getCoupleId()
                                )
                        )
                        .collect(Collectors.toList()))
                .eventId(event.getEventId())
                .eventName(event.getEventName())
                .build();
    }

    @Override
    public EventMatchDto getMatchedTeam(Long eventId) {
        TeamEvent event = teamEventRepository.findById(eventId).get();
        List<Couple> couples = coupleRepository.findAllByEventIdAndEventType(eventId, event.getEventType());
        return EventMatchDto.builder()
                .couples(couples.stream()
                        .map(
                                c -> new CoupleDto(
                                        c.getMembers()
                                                .stream()
                                                .map(
                                                        s -> new MatchedMemberDto(s)
                                                )
                                                .collect(Collectors.toList()),
                                        c.getCoupleId()
                                )
                        )
                        .collect(Collectors.toList()))
                .eventId(event.getEventId())
                .eventName(event.getEventName())
                .build();
    }


    /**
     * 관리자 권한으로 이벤트 생성
     */
    @Override
    public EventResDto createEvent(EventDto eventDto) {
        if (eventDto.getEventType().equals(EventType.ONE_ONE)) {
            Event event = eventRepository.save(Event.builder()
                    .eventName(eventDto.getEventName())
                    .maxApply(eventDto.getMaxApply())
                    .startDate(eventDto.getStartDate())
                    .endDate(eventDto.getEndDate())
                    .eventType(eventDto.getEventType())
                    .build());
            return event.to();
        }
        TeamEvent teamEvent = teamEventRepository.save(TeamEvent.builder()
                .eventName(eventDto.getEventName())
                .eventType(eventDto.getEventType())
                .maxApply(eventDto.getMaxApply())
                .startDate(eventDto.getStartDate())
                .endDate(eventDto.getEndDate())
                .build());
        return teamEvent.to();
    }


}

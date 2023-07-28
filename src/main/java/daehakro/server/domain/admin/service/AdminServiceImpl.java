package daehakro.server.domain.admin.service;

import daehakro.server.domain.admin.controller.dto.request.EventDto;
import daehakro.server.domain.admin.controller.dto.response.EventResDto;
import daehakro.server.domain.admin.matcher.OneToOneEventMatcher;
import daehakro.server.domain.admin.matcher.TeamEventMatcher;
import daehakro.server.domain.event.Event;
import daehakro.server.domain.event.EventLog;
import daehakro.server.domain.event.repo.EventLogRepository;
import daehakro.server.domain.event.repo.EventRepository;
import daehakro.server.domain.event.repo.TeamEventRepository;
import daehakro.server.domain.event.team.TeamEvent;
import daehakro.server.domain.member.Member;
import daehakro.server.domain.member.UnivInfo;
import daehakro.server.domain.member.repo.ExcludedDepartmentRepository;
import daehakro.server.domain.member.repo.MemberRepository;
import daehakro.server.domain.member.repo.TeamRepository;
import daehakro.server.domain.member.repo.UnivRepository;
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
    private final UnivRepository univRepository;
    private final EventRepository eventRepository;
    private final TeamEventRepository teamEventRepository;
    private final EventLogRepository eventLogRepository;
    private final TeamRepository teamRepository;
    private final ExcludedDepartmentRepository excludedDepartmentRepository;


    /**
     * random sorting 조건은? 대학별? 등등 추후 구현해야할듯?
     */
    @Override
    public void randomMatch(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new CustomApiException(ResponseEnum.EVENT_NOT_EXIST)
        );
        if (!isEnd(event.getEndDate())) {
            throw new CustomApiException(ResponseEnum.EVENT_NOT_ENDED);
        }
        OneToOneEventMatcher memberEventMatcher =
                new OneToOneEventMatcher(event.getMembersOfMan(),
                        event.getMembersOfWomen());
        List<List<Member>> selectedCouples = memberEventMatcher.getSelectedCouples();

        for(List<Member> couple : selectedCouples) {
            // ChatRoom chatRoom = ChatRoom.builder()
            //         .members(couple)
            //         .title(DEFAULT_ROOM_TITLE)
            //         .eventId(eventId)
            //         .build();

            couple.stream().forEach(Member::useCoin);
            logger.info(" couple  생성 완료!!");

            couple.stream().forEach(
                    s -> eventLogRepository.findByMemberAndEventId(s, eventId).match()
            );
        }
    }


    @Override
    public void randomTeamMatch(Long eventId) {
        TeamEvent teamEvent = teamEventRepository.findById(eventId).orElseThrow(
                () -> new CustomApiException(ResponseEnum.EVENT_NOT_EXIST)
        );
        if (!isEnd(teamEvent.getEndDate())) {
            throw new CustomApiException(ResponseEnum.EVENT_NOT_ENDED);
        }
        TeamEventMatcher memberEventMatcher = new TeamEventMatcher(teamEvent.getManTeams(), teamEvent.getWomenTeams());
        List<List<Team>> selectedCouples = memberEventMatcher.getSelectedTeams();

        for(List<Team> coupleTeam : selectedCouples) {
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

            // ChatRoom chatRoom = ChatRoom.builder()
            //         .members(members)
            //         .title(DEFAULT_ROOM_TITLE)
            //         .eventId(eventId)
            //         .build();

            members.stream().forEach(
                    s -> eventLogRepository.findByMemberAndEventId(s, eventId).match()
            );
            // chatRoomRepository.save(chatRoom);
        }

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
        // eventLog도 모두 isClose로 바꿔줄것
        List<EventLog> logs = eventLogRepository.findByEventId(eventId);
        logs.stream().forEach(EventLog::close);
        // 이벤트마다 ban 할 department를 정하기에 이것도 모두 제거
        excludedDepartmentRepository.deleteAllByEventId(eventId);

    }
    @Override
    public void closeTeamEvent(Long eventId) {
        TeamEvent event = teamEventRepository.findById(eventId).orElseThrow(
                () -> new CustomApiException(ResponseEnum.EVENT_NOT_EXIST)
        );
        event.close();
        // eventLog도 모두 isClose로 바꿔줄것
        List<EventLog> logs = eventLogRepository.findByEventId(eventId);
        logs.stream().forEach(EventLog::close);
        teamRepository.deleteAllByEvent(event);
    }


    /**
     * 관리자 권한으로 이벤트 생성
     */
    @Override
    public void createEvent(EventDto eventDto) {
        // UnivInfo univInfo = univRepository.findById(eventDto.getUnivId()).orElseThrow(
        //         () -> new CustomApiException(ResponseEnum.UNIV_NOT_EXIST)
        // );
        Event event = Event.builder()
                .eventName(eventDto.getEventName())
                .maxApply(eventDto.getMaxApply())
                .startDate(eventDto.getStartDate())
                .endDate(eventDto.getEndDate())
                .eventType(eventDto.getEventType())
                .build();
        eventRepository.save(event);
    }




}

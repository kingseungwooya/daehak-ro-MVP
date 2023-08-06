package daehakro.server.domain.event.controller.dto.response;

import daehakro.server.domain.event.Event;
import daehakro.server.domain.event.enums.EventType;
import daehakro.server.domain.event.team.TeamEvent;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class MemberEventDto {

    private final long eventId;

    private final String eventName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate endDate;

    private final int manApply;

    private final int womanApply;

    private final int maxApply;

    // 관리자가 매칭 다 돌렸는지
    private final boolean match;

    private final String eventType;

    @Builder
    public MemberEventDto(Long eventId, String eventName, LocalDate startDate, LocalDate endDate, int manApply, int womanApply, int maxApply, boolean match, EventType eventType) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.manApply = manApply;
        this.womanApply = womanApply;
        this.maxApply = maxApply;
        this.match = match;
        this.eventType = eventType.name();
    }

    public MemberEventDto(Event event) {
        this.eventId = event.getEventId();
        this.eventName = event.getEventName();
        this.eventType = event.getEventType().name();
        this.startDate = event.getStartDate();
        this.endDate = event.getEndDate();
        this.manApply = event.getMembersOfMan().size();
        this.womanApply = event.getMembersOfWomen().size();
        this.maxApply = event.getMaxApply();
        this.match = event.isMatch();
    }

    public MemberEventDto(TeamEvent event) {
        this.eventId = event.getEventId();
        this.eventName = event.getEventName();
        this.eventType = event.getEventType().name();
        this.startDate = event.getStartDate();
        this.endDate = event.getEndDate();
        this.manApply = event.getManTeams().size();
        this.womanApply = event.getWomenTeams().size();
        this.maxApply = event.getMaxApply();
        this.match = event.isMatch();
    }


}

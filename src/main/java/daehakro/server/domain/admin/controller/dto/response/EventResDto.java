package daehakro.server.domain.admin.controller.dto.response;

import daehakro.server.domain.event.enums.EventType;
import java.sql.Timestamp;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class EventResDto {

    private final long eventId;

    private final String eventName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate endDate;

    private final Timestamp createAt;

    private final int manApply;

    private final int womanApply;

    private final int maxApply;

    private final boolean match;

    private final String eventType;

    @Builder
    public EventResDto(Long eventId, String eventName, LocalDate startDate, LocalDate endDate, Timestamp createAt, int manApply, int womanApply, int maxApply, boolean match, EventType eventType) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createAt = createAt;
        this.manApply = manApply;
        this.womanApply = womanApply;
        this.maxApply = maxApply;
        this.match = match;
        this.eventType = eventType.name();
    }

    // public EventResDto(Event event, int manApply, int womanApply) {
    //     this.eventId = event.getEventId();
    //     this.eventName = event.getEventName();
    //     this.startDate = event.getStartDate();
    //     this.endDate = event.getEndDate();
    //     this.createAt = event.getCreateAt();
    //     this.manApply = manApply;
    //     this.womanApply = womanApply;
    //     this.maxApply = event.getMaxApply();
    //     this.open = event.isOpen();
    // }
}

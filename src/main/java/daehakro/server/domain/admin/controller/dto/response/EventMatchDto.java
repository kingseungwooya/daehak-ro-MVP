package daehakro.server.domain.admin.controller.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EventMatchDto {
    private final Long eventId;
    private final String eventName;
    private final List<CoupleDto> couples;

    @Builder
    public EventMatchDto(Long eventId, String eventName,
                         List<CoupleDto> couples) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.couples = couples;
    }
}

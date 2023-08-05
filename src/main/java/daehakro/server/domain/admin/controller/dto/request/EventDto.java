package daehakro.server.domain.admin.controller.dto.request;

import daehakro.server.domain.event.enums.EventType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

    private String eventName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    // default 200 예상
    private int maxApply;

    private EventType eventType;


}

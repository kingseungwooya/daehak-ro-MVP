package daehakro.server.domain.admin.controller.dto.converter;


import daehakro.server.domain.event.enums.EventType;
import org.springframework.core.convert.converter.Converter;


public class EventTypeConverter implements Converter<String, EventType> {

    @Override
    public EventType convert(String source) {
        return EventType.of(source);
    }

}

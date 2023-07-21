package daehakro.server.domain.event.enums;

import java.util.Arrays;

public enum EventType {
    ONE_ONE(1),
    TWO_TWO(2);

    int limitOfEachSex;

    EventType(int limitOfEachSex) {
        this.limitOfEachSex = limitOfEachSex;
    }

    public static EventType of(String eventType) {
        return Arrays.stream(values())
                .filter(s -> s.name().equals(eventType))
                .findFirst()
                .orElse(ONE_ONE);
    }

    public int getLimitOfEachSex() {
        return limitOfEachSex;
    }
}

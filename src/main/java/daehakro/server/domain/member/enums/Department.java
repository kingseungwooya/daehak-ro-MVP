package daehakro.server.domain.member.enums;

import java.util.Arrays;

public enum Department {
    CSE("컴퓨터공학과"),
    NAOE("선박해양공학과"),
    PIANO("피아노과"),
    DEFAULT("default");

    String fetch;

    Department(String fetch) {
        this.fetch = fetch;
    }

    public static Department of(String department) {
        return Arrays.stream(values())
                .filter(s -> s.fetch.equals(department))
                .findFirst()
                .orElse(DEFAULT);
    }
}

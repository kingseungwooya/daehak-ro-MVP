package daehakro.server.global.exception.handler;

import daehakro.server.global.exception.ResponseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomApiException extends RuntimeException{

    private final ResponseEnum responseEnum;

}
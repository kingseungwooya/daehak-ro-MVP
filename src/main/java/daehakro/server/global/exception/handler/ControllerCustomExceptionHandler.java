package daehakro.server.global.exception.handler;

import daehakro.server.global.exception.ResponseDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerCustomExceptionHandler {

    @ExceptionHandler(CustomValidationApiException.class)
    public HttpEntity<?> validationApiException(CustomValidationApiException e){
        return new ResponseEntity<>(new ResponseDto<>(1, e.getMessage(),e.getErrorMap()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomApiException.class)
    public HttpEntity<?> apiException(CustomApiException e){
        return new ResponseEntity<>(new ResponseDto<>(e.getResponseEnum()), HttpStatus.valueOf(
                e.getResponseEnum().getCode()
        ));
    }

}
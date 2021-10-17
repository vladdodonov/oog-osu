package com.dodonov.oogosu.utils.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Общий обработчик ошибок при работе REST контроллеров
 */
@ControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    private static final String EXCEPTION_TEXT = "Exception handled: ";

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity exceptionHandler(Exception ex) {
        String trace = getTrace(ex);
        log.error(EXCEPTION_TEXT, ex);
        return ResponseBuilder.internalServerError("error", ex.getMessage(), trace);
    }

    /**
     * Разбирает дерево исключений и строит сообщение
     */
    private String getTrace(Throwable e) {
        return ExceptionUtils.getStackTrace(e);
    }
}

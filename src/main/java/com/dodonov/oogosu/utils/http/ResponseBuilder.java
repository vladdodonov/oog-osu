package com.dodonov.oogosu.utils.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;



@Slf4j
public class ResponseBuilder {

    /**
     * Запрос прошел успешно, для одиночного объекта
     *
     * @param <T> одиночный объект
     * @return ResponseEntity <ResponseData>
     */
    public static <T> ResponseEntity<Response<T>> success(T data) {
        return ResponseEntity.ok(Response.<T>builder().data(data).success(true).build());
    }

    public static <T> ResponseEntity<Response<T>> success() {
        return ResponseEntity.ok(Response.<T>builder().data(null).success(true).build());
    }

    /**
     * Запрос прошел успешно, для одиночного объекта. с кэшем
     *
     * @param data               - одиночный объект
     * @param cacheMaxAgeSeconds - срок хранения в кэше в секундах
     * @return ResponseEntity <ResponseData>
     */
    public static <T> ResponseEntity<Response<T>> successCache(T data, int cacheMaxAgeSeconds) {

        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();
        bodyBuilder.cacheControl(CacheControl.maxAge(cacheMaxAgeSeconds, TimeUnit.SECONDS));

        return bodyBuilder.body(Response.<T>builder().data(data).success(true).build());
    }

    /**
     * Запрос прошел успешно, для коллекции объектов
     *
     * @param dataList коллекция объектов
     * @return ResponseEntity <ResponseData>
     */
    public static <T> ResponseEntity<CollectionResponse<T>> success(Collection<T> dataList) {
        return success(dataList, 0, 0, dataList.size());
    }

    public static <T> ResponseEntity<CollectionResponse<T>> success(Collection<T> dataList, LocalDateTime lastUpdateDate) {
        return success(dataList, 0, 0, dataList.size(), lastUpdateDate);
    }

    /**
     * Запрос прошел успешно, для коллекции объектов с кэшем
     *
     * @param dataList           - коллекция объектов
     * @param cacheMaxAgeSeconds - срок хранения в кэше в секундах
     * @param <T>
     * @return ResponseEntity <ResponseData>
     */
    public static <T> ResponseEntity<CollectionResponse<T>> successCache(Collection<T> dataList, int cacheMaxAgeSeconds) {
        return successCache(dataList, 0, 0, dataList.size(), cacheMaxAgeSeconds);
    }

    /**
     * Запрос прошел успешно, для коллекции объектов с пагинацией
     *
     * @param dataList коллекция объектов
     * @param skip     сколько объектов было пропущено
     * @param take     сколько элементов было взято
     * @param size     общее количество элементов
     * @return ResponseEntity <ResponseData>
     */
    public static <T> ResponseEntity<CollectionResponse<T>> success(Collection<T> dataList, long skip, long take, long size) {
        CollectionData<T> arrayData = CollectionData.<T>builder().content(dataList).skip(skip).take(take).size(size).build();
        CollectionResponse<T> respData = CollectionResponse.<T>builder().data(arrayData).success(true).build();
        return ResponseEntity.ok(respData);
    }


    public static <T> ResponseEntity<CollectionResponse<T>> success(Collection<T> dataList, long skip, long take, long size,
                                                                    LocalDateTime lastUpdateDate) {
        CollectionData<T> arrayData = CollectionData.<T>builder().content(dataList).skip(skip).take(take).size(size).build();
        CollectionResponse<T> respData = CollectionResponse.<T>builder()
                .data(arrayData)
                .lastUpdateDate(lastUpdateDate)
                .success(true)
                .build();
        return ResponseEntity.ok(respData);
    }

    public static <T> ResponseEntity<CollectionResponse<T>> success(Page<T> data) {
        return success(data.getContent(), data.getPageable().getOffset(), data.getPageable().getPageSize(), data.getTotalElements());
    }

    /**
     * Запрос прошел успешно, для коллекции объектов с пагинацией и кэшем
     *
     * @param dataList           - коллекция объектов
     * @param skip               - сколько объектов было пропущено
     * @param take               - сколько элементов было взято
     * @param size               -общее количество элементов
     * @param cacheMaxAgeSeconds - срок хранения в кэше в секундах
     * @param <T>
     * @return - ResponseEntity <ResponseData>
     */
    public static <T> ResponseEntity<CollectionResponse<T>> successCache(Collection<T> dataList, long skip, long take, long size, int cacheMaxAgeSeconds) {
        CollectionData<T> arrayData = CollectionData.<T>builder().content(dataList).skip(skip).take(take).size(size).build();
        CollectionResponse<T> respData = CollectionResponse.<T>builder().data(arrayData).success(true).build();

        return ResponseEntity.ok().cacheControl(CacheControl.maxAge(cacheMaxAgeSeconds, TimeUnit.SECONDS)).body(respData);
    }

    /**
     * Логическая ошибка, возвращает 200 код с пометкой ошибки
     *
     * @param code код ошибки
     * @param text текст ошибки
     * @return ResponseEntity <ResponseData>
     */
    @SuppressWarnings("unchecked")
    public static <T> ResponseEntity<T> logicalError(String code, String text) {
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        ResponseError error = ResponseError.builder().code(code).text(text).build();
        Response respData = Response.builder().data(List.of(error.getText())).success(false).error(error).build();
        return builder.body((T) respData);
    }

    /**
     * Логическая ошибка, возвращает 200 код с пометкой ошибки
     *
     * @param code   код ошибки
     * @param text   текст ошибки
     * @param errors список ошибок
     * @return ResponseEntity
     */
    public static <T> ResponseEntity logicalError(String code, String text, Collection<T> errors) {
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        ResponseError error = ResponseError.builder().code(code).text(text).build();
        Response respData = Response.builder().success(false).error(error).data(errors).build();
        return builder.body(respData);
    }

    /**
     * Плохой запрос
     *
     * @param code код ошибки
     * @param text текст ошибки
     * @return ResponseEntity <ResponseData>
     */
    public static <T> ResponseEntity<Response<T>> badRequest(String code, String text) {
        ResponseEntity.BodyBuilder builder = ResponseEntity.badRequest();
        ResponseError error = ResponseError.builder().code(code).text(text).build();
        Response<T> respData = Response.<T>builder().success(false).error(error).build();
        return builder.body(respData);
    }

    /**
     * Внутренняя ошибка сервера
     *
     * @param code код ошибки
     * @param text текст ошибки
     * @return ResponseEntity <ResponseData>
     */
    public static ResponseEntity internalServerError(String code, String text, String trace) {
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        return builder.body(ResponseError.builder().code(code).text(text).trace(trace).build());
    }

    /**
     * Доступ запрещен
     *
     * @param code код ошибки
     * @param text текст ошибки
     * @return ResponseEntity <ResponseData>
     */
    public static <T> ResponseEntity<Response<T>> accessDenied(String code, String text, String trace) {
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(HttpStatus.FORBIDDEN);
        ResponseError error = ResponseError.builder().code(code).text(text).trace(trace).build();
        Response<T> respData = Response.<T>builder().success(false).error(error).build();
        return builder.body(respData);
    }

    /**
     * Внутренняя ошибка сервера
     *
     * @return ResponseEntity
     */
    public static ResponseEntity internalServerError() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * Не авторизован
     *
     * @return ResponseEntity
     */
    public static ResponseEntity unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /**
     * Ошибка, возникающая в приложении
     *
     * @param exception возникшее исключение
     * @return ResponseEntity
     */
    public static ResponseEntity responseStatusError(ResponseStatusException exception, String trace) {
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(exception.getStatus());
        return builder.body(ResponseError.builder().text(exception.getReason()).trace(trace).build());
    }

    public static <T> ResponseEntity<Response<T>> customResponse(HttpStatus httpStatus, String code, String text) {
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(httpStatus);
        ResponseError error = ResponseError.builder().code(code).text(text).build();
        Response<T> respData = Response.<T>builder().success(false).error(error).build();
        return builder.body(respData);
    }
}

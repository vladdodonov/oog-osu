package com.dodonov.oogosu.utils;

import lombok.ToString;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Реализация Pageable без разбивки на реальные страницы
 * Логика разбивки на страницы происходит во вне
 * Выборка по Skip/Take (offset/limit)
 */
@ToString
public class Range implements Pageable, Serializable {

    private final int skip;
    private final int take;
    private final Sort sort;

    private Range(Integer skip, Integer take, Sort sort) {
        this.skip = skip == null ? 0 : skip;
        this.take = take == null ? 0 : take;
        this.sort = sort;
    }

    public static Range of(Integer skip, Integer take) {
        return new Range(skip, take, Sort.unsorted());
    }

    public static Range of(Integer skip, Integer take, Sort sort) {
        return new Range(skip, take, sort);
    }

    public static Range of(Integer skip, Integer take, Sort.Direction direction) {
        return new Range(skip, take, direction == null ? Sort.by(Sort.Direction.DESC, "id") : Sort.by(direction, "id"));
    }

    public static Range of(Sort sort) {
        return new Range(0, Integer.MAX_VALUE, sort);
    }

    @Override
    public boolean isPaged() {
        return false;
    }

    @Override
    public boolean isUnpaged() {
        return false;
    }

    @Override
    public int getPageNumber() {
        return 0;
    }

    /**
     * В данном контексте размер страницы - это количество элементов которые нужно выбрать (limit ?)
     *
     * @return количество элементов для выборки
     */
    @Override
    public int getPageSize() {
        return take;
    }

    /**
     * Количество элементов которые нужно пропустить
     *
     * @return количество элементов для пропуска
     */
    @Override
    public long getOffset() {
        return skip;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Sort getSortOr(Sort sort) {
        return null;
    }

    @Override
    public Pageable next() {
        return null;
    }

    @Override
    public Pageable previousOrFirst() {
        return null;
    }

    @Override
    public Pageable first() {
        return null;
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    @Override
    public Optional<Pageable> toOptional() {
        return Optional.empty();
    }
}

package com.dodonov.oogosu.utils;

import com.dodonov.oogosu.domain.BaseEntity;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@Slf4j
@SuppressWarnings("unchecked")
public class CriteriaUtils {

    public static <T> Join<T, ?> getJoin(Root<T> root, Class clazz) {
        return root.getJoins()
                .stream()
                .filter(join -> clazz.equals(join.getJavaType())).findFirst()
                .orElseThrow();
    }

    public static <T> Join<T, ?> getJoin(Join<?, T> rootJoin, Class clazz) {
        return rootJoin.getJoins()
                .stream()
                .filter(join -> clazz.equals(join.getJavaType())).findFirst()
                .orElseThrow();
    }

    public static <E, T> Join<E, T> leftJoin(final Join<?, E> root, String joinObj) {
        return root.join(joinObj, JoinType.LEFT);
    }

    public static <E, T> Join<E, T> leftJoin(final Root<E> root, String joinObj) {
        return root.join(joinObj, JoinType.LEFT);
    }

    public static <T extends BaseEntity> Predicate predicateAndIn(Path path, Collection<T> objects, CriteriaBuilder cb) {
        if (isNotEmpty(objects)) {
            var ids = objects.stream().map(BaseEntity::getId).collect(Collectors.toList());
            return cb.and(cb.in(path).value(ids));
        }
        return null;
    }

    public static Predicate predicateAndInIds(Path path, Collection<Long> ids, CriteriaBuilder cb) {
        if (isNotEmpty(ids)) {
            return cb.and(cb.in(path).value(ids));
        }
        return null;
    }

    public static Predicate predicateAndLike(Path path, String likeString, CriteriaBuilder cb) {
        if (likeString != null && !likeString.isEmpty()) {
            String value = "%" + likeString.toLowerCase() + "%";
            return cb.and(cb.like(cb.lower(path), value));
        }
        return null;
    }

    public static Predicate predicateAndEqual(Path path, Object obj, CriteriaBuilder cb) {
        return obj != null
                ? cb.and(predicateEqual(path, obj, cb))
                : null;
    }

    public static Predicate predicateAndNotEqual(Path path, Object obj, CriteriaBuilder cb) {
        return obj != null
                ? cb.and(cb.notEqual(path, obj))
                : null;
    }

    public static Predicate predicateAndIsNotNull(Path path, CriteriaBuilder cb) {
        return cb.and(cb.isNotNull(path));
    }

    public static Predicate predicateAndIsNull(Path path, CriteriaBuilder cb) {
        return cb.and(cb.isNull(path));
    }

    public static Predicate predicateEqual(Path path, Object obj, CriteriaBuilder cb) {
        return obj != null
                ? cb.equal(path, obj)
                : null;
    }

    public static Predicate predicateOr(CriteriaBuilder cb, Predicate... predicates) {
        Predicate orPredicate = cb.or();
        for (Predicate predicate : predicates) {
            if (predicate != null) {
                orPredicate = cb.or(orPredicate, predicate);
            }
        }
        return (orPredicate.getExpressions() != null && !orPredicate.getExpressions().isEmpty()) ? orPredicate : null;
    }

    public static Predicate predicateAndEqualsOrNull(CriteriaBuilder cb, Path path, Object value) {
        return cb.and(cb.or(cb.equal(path, value), cb.isNull(path)));
    }
}

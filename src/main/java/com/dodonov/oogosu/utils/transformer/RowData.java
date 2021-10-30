package com.dodonov.oogosu.utils.transformer;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Данные строки из результата SQL select
 */
public interface RowData {
    Object get(Object key);

    Object get(int index);

    default Long getLong(Object key) {
        return SqlUtils.toLong(get(key));
    }

    default Long getLong(int index) {
        return SqlUtils.toLong(get(index));
    }

    default Double getDouble(int index) {
        return SqlUtils.toDouble(get(index));
    }

    default Double getDouble(Object key) {
        return SqlUtils.toDouble(get(key));
    }

    default String getString(Object key) {
        return Optional.ofNullable(get(key)).map(Object::toString).orElse(null);
    }

    default String getString(int index) {
        return Optional.ofNullable(get(index)).map(Object::toString).orElse(null);
    }

    default Boolean getBoolean(Object key) {
        return Optional.ofNullable(get(key)).map(Object::toString).map(Boolean::parseBoolean).orElse(null);
    }

    default ZonedDateTime getZonedDateTime(Object key) {
        return Optional.ofNullable(SqlUtils.toTimeStamp(get(key))).map(tm -> tm.toLocalDateTime().atZone(ZoneId.systemDefault())).orElse(null);
    }
}

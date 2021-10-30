package com.dodonov.oogosu.utils.transformer;

import java.sql.Timestamp;

public class SqlUtils {
    public static final String DATE_FORMAT = "DD.MM.YYYY";

    private SqlUtils() {}

    public static Long toLong(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof Number) {
            return ((Number) obj).longValue();
        } else {
            throw new IllegalArgumentException(String.format("Cannot convert %s to long", obj));
        }
    }

    public static Double toDouble(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        } else {
            throw new IllegalArgumentException(String.format("Cannot convert %s to double", obj));
        }
    }

    public static Timestamp toTimeStamp(Object obj){
        if (obj == null) {
            return null;
        } else if (obj instanceof Timestamp) {
            return (Timestamp) obj;
        } else {
            throw new IllegalArgumentException(String.format("Cannot convert %s to Timestamp", obj));
        }
    }

    /**
     * Формирует select выражение для получения даты в стандартном формате ДД.ММ.ГГГГ
     * @param column Колонка с датой, которую хотим получить
     * @return select выражение для получения даты в стандартном формате ДД.ММ.ГГГГ
     */
    public static String selectDate(String column) {
        return String.format("to_char(%s, '%s')", column, DATE_FORMAT);
    }

    /**
     * Формирует select выражение для получения строки вида {@code <номер> от <ДД.ММ.ГГГГ>}
     * @param tableAlias Алиас таблицы, из которой достаем оба поля: number и указанный {@code dateColumnName}
     * @param dateColumnName Имя колонки с датой (без алиаса таблицы!)
     * @return select выражение для получения строки вида {@code <номер> от <ДД.ММ.ГГГГ>}
     */
    public static String selectNumberFromDate(String tableAlias, String dateColumnName) {
        return String.format("%1$s.number || ' от ' || to_char(%1$s.%2$s, '%3$s')", tableAlias, dateColumnName, DATE_FORMAT);
    }

    /**
     * Формирует select выражение для получения ФИО вида {@code Фамилия И.О.}
     * @param tableAlias Алиас таблицы, из которой достаем данные. Ждем, что таблица содержит текстовые колонки:
     * <ul>
     *                   <li>last_name</li>
     *                   <li>first_name</li>
     *                   <li>middle_name</li>
     * </ul>
     * @return select выражение для получения ФИО вида {@code Фамилия И.О.}
     */
    public static String selectInitials(String tableAlias) {
        return String.format("%1$s.last_name || ' '\n" +
                "|| (case when %1$s.first_name is not null then substring(%1$s.first_name from 1 for 1) || '.' else '' end)\n" +
                "|| (case when %1$s.middle_name is not null then substring(%1$s.middle_name from 1 for 1) || '.' else '' end)\n"
                , tableAlias);
    }

    public static String isNotTrue(String expression) {
        return String.format("%s IS NOT TRUE", expression);
    }
}

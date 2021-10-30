package com.dodonov.oogosu.utils.transformer;

import org.hibernate.transform.ResultTransformer;

import java.util.LinkedHashMap;

public abstract class ResultTransformerWithLocalTuple implements ResultTransformer {

    /**
     * Собирает списки результатов запроса и алиасов в мапу, чтобы значения можно было доставать по алиасу
     * независимо от порядка селектов
     * @param tuple Список результатов запроса
     * @param aliases Список алиасов (Postgres возвращает их в lower case, поэтому на всякий приводим все ключи к lower case)
     * @return Мапа алиас -> значение с возможностью получать значения по индексу
     */
    protected RowData getRowData(Object[] tuple, String[] aliases) {
        return new RowDataImpl(tuple, aliases);
    }

    public static class RowDataImpl extends LinkedHashMap<String, Object> implements RowData {
        private final Object[] tuple;

        private RowDataImpl(Object[] tuple, String[] aliases) {
            this.tuple = tuple;

            for (int i = 0, length = Math.min(tuple.length, aliases.length); i < length; i++) {
                put(aliases[i].toLowerCase(), tuple[i]);
            }
        }

        @Override
        public Object get(Object key) {
            return super.get(String.valueOf(key).toLowerCase());
        }

        public Object get(int index) {
            return tuple[index];
        }
    }
}

package com.dodonov.oogosu.utils.transformer;

import java.util.*;

/**
 * ResultTransformer, предназначенный для формирования объектов с полями-коллекциями. Для каждого значения из поля коллекции
 * в ResultSet будет содержаться отдельная строка. Но на выходе мы хотим получить один объект с коллекцией.
 * @param <T> Тип объектов
 * @param <K> Тип поля, по которому будем идентифицировать объекты в мапе (в большинстве случаев будет Long,
 *           но оставил возможность для кастомизации)
 */
public abstract class CollectionFieldResultTransformer<T, K> extends ResultTransformerWithLocalTuple {

    private final Map<K, T> resultMap = new LinkedHashMap<>();

    @Override
    public T transformTuple(Object[] tuple, String[] aliases) {
        RowData rowData = getRowData(tuple, aliases);

        T result = resultMap.computeIfAbsent(getKey(rowData), key -> createInstance(rowData));
        fillCollections(result, rowData);

        return result;
    }

    @Override
    public List transformList(List collection) {
        return new ArrayList(resultMap.values());
    }

    /**
     * Этот метод должен достать из полученных параметров тот, который используется в качестве идентификатора.
     * @param rowData Данные строки из результата SQL select
     * @return Идентификатор
     */
    protected abstract K getKey(RowData rowData);

    /**
     * Метод для создания инстанса результата. Заполнять поля-коллекции в нем не нужно!
     * @param rowData Данные строки из результата SQL select
     * @return Созданный инстанс
     */
    protected abstract T createInstance(RowData rowData);

    /**
     * Метод для заполнения полей-коллекций
     * @param result Объект результата
     * @param rowData Данные строки из результата SQL select
     */
    protected abstract void fillCollections(T result, RowData rowData);

    /**
     * Приводит переданный объект к типу Long и кладет в переданную коллекцию.
     * @param collection Коллекция
     * @param object Объект для приведения к Long (может быть null)
     */
    protected void addLong(Collection<Long> collection, Object object) {
        Optional.ofNullable(object)
                    .map(SqlUtils::toLong)
                    .ifPresent(collection::add);
    }
}

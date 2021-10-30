package com.dodonov.oogosu.utils.transformer;

/**
 * Наследник CollectionFieldResultTransformer, определяющий Long в качестве типа ключа и ожидающий его значение
 * под индексом 0
 * @param <T>
 */
public abstract class LongIdCollectionFieldResultTransformer<T>
        extends CollectionFieldResultTransformer<T, Long> {

    @Override
    protected Long getKey(RowData rowData) {
        return rowData.getLong(0);
    }
}

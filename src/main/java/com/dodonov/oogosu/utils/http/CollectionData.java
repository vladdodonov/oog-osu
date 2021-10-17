package com.dodonov.oogosu.utils.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionData<T> {
    private Collection<T> content;
    private long skip;
    private long take;
    private long size;
}

package com.dodonov.oogosu.utils.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionResponse<T> {
    private boolean success;
    private CollectionData<T> data;
    private ResponseError error;
    private LocalDateTime lastUpdateDate;
}

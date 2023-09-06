package com.project.stock.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortField {
    CHANGE("change", "가격변동률"),
    VOLUME("volume", "거래량"),
    POPULAR("popular", "인기도");

    private String code;
    private String description;

}

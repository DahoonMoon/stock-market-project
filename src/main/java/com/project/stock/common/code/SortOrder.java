package com.project.stock.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortOrder {
    ASCENDING("ascending", "오름차순"),
    DESCENDING("descending", "내림차순");

    private String code;
    private String description;
}

package com.project.stock.common.util;

import java.math.BigDecimal;

public class PriceUtil {

    public static BigDecimal getAskingPrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.valueOf(2000)) < 0) {
            return BigDecimal.valueOf(1);
        } else if (price.compareTo(BigDecimal.valueOf(5000)) < 0) {
            return BigDecimal.valueOf(5);
        } else if (price.compareTo(BigDecimal.valueOf(20000)) < 0) {
            return BigDecimal.valueOf(10);
        } else if (price.compareTo(BigDecimal.valueOf(50000)) < 0) {
            return BigDecimal.valueOf(50);
        } else if (price.compareTo(BigDecimal.valueOf(200000)) < 0) {
            return BigDecimal.valueOf(100);
        } else if (price.compareTo(BigDecimal.valueOf(500000)) < 0) {
            return BigDecimal.valueOf(500);
        } else {
            return BigDecimal.valueOf(1000);
        }
    }

}

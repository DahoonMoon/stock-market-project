package com.project.stock.common.util;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateTimeUtil {

    public static boolean isTradeDate(LocalDateTime localDateTime) {
        // 주말은 거래 불가
        DayOfWeek day = localDateTime.getDayOfWeek();

        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
    }

    public static LocalDateTime getLastTradeEndDateTime(LocalDateTime localDateTime) {

        if (isTradeDate(localDateTime) && localDateTime.getHour() < 9) {
            log.debug("{} : 거래가능일 9시 이전 -> 이전 거래가능일 16시", localDateTime);
            return getLastTradeEndDateTime(localDateTime.minusDays(1).withHour(16).withMinute(0).withSecond(0));
        } else if (isTradeDate(localDateTime) && localDateTime.getHour() >= 9 && localDateTime.getHour() < 16){
            log.debug("{} : 거래가능일 거래가능시간 -> 현재시각", localDateTime);
            return localDateTime.withNano(0);
        } else if (isTradeDate(localDateTime) && localDateTime.getHour() >= 16){
            log.debug("{} : 거래가능일 16시 이후 -> 당일 16시", localDateTime);
            return localDateTime.withHour(16).withMinute(0).withSecond(0).withNano(0);
        } else {
            log.debug("{} : 거래 불가능일 -> 이전 거래가능일 16시", localDateTime);
            return getLastTradeEndDateTime(localDateTime.minusDays(1).withHour(16).withMinute(0).withSecond(0));
        }
    }


}

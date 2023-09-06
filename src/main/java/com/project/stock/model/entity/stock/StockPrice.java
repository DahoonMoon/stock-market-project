package com.project.stock.model.entity.stock;

import com.project.stock.model.entity.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class StockPrice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_price_id", updatable = false, nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false, updatable = false)
    private Stock stock;

    @Comment("시각")
    @Column(columnDefinition = "TIMESTAMP(6)", nullable = false)
    private LocalDateTime timestamp;

    @Comment("시각 기록 구간")
    @Column(nullable = false)
    private int timeInterval;

    @Comment("시가")
    @Column(nullable = false)
    private BigDecimal openPrice;

    @Comment("종가")
    @Column(nullable = false)
    private BigDecimal closePrice;

    @Comment("고가")
    @Column(nullable = false)
    private BigDecimal highPrice;

    @Comment("저가")
    @Column(nullable = false)
    private BigDecimal lowPrice;

    @Comment("거래량")
    @Column(nullable = false)
    private BigDecimal volume;

    @Comment("비고")
    @Column(length = 200)
    private String remark;

}

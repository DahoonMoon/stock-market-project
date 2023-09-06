package com.project.stock.model.entity.stock;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Data
@Entity
@Immutable
@Subselect("select stock_id, MAX(timestamp) as latest_timestamp "
    + "from stock_price "
    + "group by stock_id")
@Synchronize("stock_price")
public class SubStockPrice {

    @Id
    @Column(name = "stock_id", updatable = false, nullable = false)
    private Integer stockId;

    @Comment("최근 시각")
    @Column(name = "latest_timestamp", columnDefinition = "TIMESTAMP(6)", nullable = false)
    private LocalDateTime latestTimestamp;

}

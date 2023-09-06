package com.project.stock.model.entity.stock;

import com.project.stock.model.entity.BaseEntity;
import com.opencsv.bean.CsvBindByName;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class Stock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id", updatable = false, nullable = false)
    private Integer id;

    @Comment("종목코드")
    @CsvBindByName(column = "code")
    @Column(unique = true, length = 30, nullable = false)
    private String stockCode;

    @Comment("종목명")
    @CsvBindByName(column = "name")
    @Column(nullable = false)
    private String stockName;

    @Comment("비고")
    @Column(length = 200)
    private String remark;

}

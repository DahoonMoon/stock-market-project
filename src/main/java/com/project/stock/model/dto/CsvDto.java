package com.project.stock.model.dto;

import com.opencsv.bean.CsvBindByName;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CsvDto {

	@CsvBindByName(column = "id")
	private Integer id;

	@CsvBindByName(column = "code")
	private String code;

	@CsvBindByName(column = "name")
	private String name;

	@CsvBindByName(column = "price")
	private BigDecimal price;

}

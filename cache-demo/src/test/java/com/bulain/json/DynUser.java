package com.bulain.json;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class DynUser {

    private String name;
    private LocalDate birth;
    private LocalDateTime time;
    private Integer age;
    private Long hight;
    private BigDecimal amount;
    private String empty;

}

package com.bulain.rocket;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RocketEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    private String eventType;
    private BigDecimal eventAmt;
}

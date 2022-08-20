package com.bulain.redis.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Accessors(chain = true)
public class RedisUser {
    private String userId;
    private String userName;
    private Integer userAge;
    private LocalDateTime ldt;
    private LocalDate ld;
    private Date dt;
}
package com.bulain.caffeine.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String text;

}

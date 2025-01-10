package com.bulain.fastjson;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class DynField {
    private String fname;
    private Serializable value;
}

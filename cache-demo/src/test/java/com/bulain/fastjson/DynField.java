package com.bulain.fastjson;

import lombok.Data;

import java.io.Serializable;

@Data
public class DynField {
    private String fname;
    private Serializable value;
}

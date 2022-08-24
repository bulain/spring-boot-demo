package com.bulain.mybatis.audit;

import org.javers.core.Changes;

import java.util.function.BiFunction;

@FunctionalInterface
public interface DataAudit {
    void audit(BiFunction<Object, Object, Changes> func);
}

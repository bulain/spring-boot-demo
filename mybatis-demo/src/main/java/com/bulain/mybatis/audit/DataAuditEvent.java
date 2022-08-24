package com.bulain.mybatis.audit;

import org.springframework.context.ApplicationEvent;

public class DataAuditEvent extends ApplicationEvent {

    public DataAuditEvent(DataAudit source) {
        super(source);
    }

    public void apply() {
        DataAudit source = (DataAudit) super.getSource();
        if (source != null) {
            source.audit((obj1, obj2) -> {
                return DataAuditor.compare(obj1, obj2);
            });
        }

    }
}

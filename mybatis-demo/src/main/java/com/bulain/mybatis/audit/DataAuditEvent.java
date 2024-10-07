package com.bulain.mybatis.audit;

import org.springframework.context.ApplicationEvent;

public class DataAuditEvent extends ApplicationEvent {

    public DataAuditEvent(DataAudit source) {
        super(source);
    }

    public void apply() {
        DataAudit source = (DataAudit) super.getSource();
        if (source != null) {
            source.audit(DataAuditor::compare);
        }

    }
}

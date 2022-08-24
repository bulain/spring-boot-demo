package com.bulain.mybatis.audit;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class DataAuditListener {
    public DataAuditListener() {
    }

    @Async
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT,
            fallbackExecution = true
    )
    public void onEvent(DataAuditEvent evt) {
        evt.apply();
    }

}

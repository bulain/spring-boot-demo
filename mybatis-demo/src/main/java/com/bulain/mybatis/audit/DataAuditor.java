package com.bulain.mybatis.audit;

import org.javers.core.Changes;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.ListCompareAlgorithm;

public class DataAuditor {
    private static Javers JAVERS;

    public DataAuditor() {
    }

    public static Javers getJavers() {
        if (JAVERS == null) {
            synchronized (DataAudit.class) {
                JAVERS = JaversBuilder.javers().withListCompareAlgorithm(ListCompareAlgorithm.LEVENSHTEIN_DISTANCE).build();
            }
        }
        return JAVERS;
    }

    public static Changes compare(Object obj1, Object obj2) {
        return getJavers().compare(obj1, obj2).getChanges();
    }
}

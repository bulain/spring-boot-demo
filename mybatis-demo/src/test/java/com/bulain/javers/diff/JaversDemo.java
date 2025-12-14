package com.bulain.javers.diff;

import com.bulain.mybatis.demo.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.commit.Commit;
import org.javers.core.diff.Diff;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Slf4j
@Disabled
public class JaversDemo {

    @Test
    void testCompare() {
        Javers javers = JaversBuilder.javers().build();

        Order ent1 = new Order()
                .setOrderNo("X00001")
                .setExtnRefNo1("E00001")
                .setExtnRefNo2("E00002")
                .setExtnRefNo3("E00003");
        Order ent2 = new Order()
                .setOrderNo("X00002")
                .setExtnRefNo1("E00001")
                .setExtnRefNo2("E00004")
                .setExtnRefNo3("E00003");

        Diff diff = javers.compare(ent1, ent2);
        log.debug("{}", diff);

        Commit commit = javers.commit("order", ent1);
        log.debug("{}", commit);

        commit = javers.commit("order", ent2);
        log.debug("{}", commit);
    }

}

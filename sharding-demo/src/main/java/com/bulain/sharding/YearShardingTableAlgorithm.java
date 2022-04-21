package com.bulain.sharding;

import com.google.common.collect.Range;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class YearShardingTableAlgorithm implements PreciseShardingAlgorithm<Date>, RangeShardingAlgorithm<Date> {

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Date> shardingValue) {
        String logicTableName = shardingValue.getLogicTableName();
        int curr = LocalDateTime.now().getYear();
        int year = LocalDateTime.ofInstant(shardingValue.getValue().toInstant(), ZoneId.systemDefault()).getYear();
        StringBuilder sb = new StringBuilder(logicTableName);
        if (year >= curr) {
            return sb.toString();
        }
        return sb.append(year).toString();
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Date> shardingValue) {

        String logicTableName = shardingValue.getLogicTableName();
        Range<Date> valueRange = shardingValue.getValueRange();
        int curr = LocalDateTime.now().getYear();
        int lower = LocalDateTime.ofInstant(valueRange.lowerEndpoint().toInstant(), ZoneId.systemDefault()).getYear();
        int upper = LocalDateTime.ofInstant(valueRange.upperEndpoint().toInstant(), ZoneId.systemDefault()).getYear();

        List<String> list = new ArrayList<>();
        while (lower < upper && lower < curr) {
            list.add(new StringBuilder(logicTableName).append(lower).toString());
            lower++;
        }
        if (upper < curr) {
            list.add(new StringBuilder(logicTableName).append(upper).toString());
        }

        Collection<String> ret = CollectionUtils.intersection(availableTargetNames, list);
        if (upper >= curr) {
            ret.add(new StringBuilder(logicTableName).toString());
        }

        return ret;

    }

}

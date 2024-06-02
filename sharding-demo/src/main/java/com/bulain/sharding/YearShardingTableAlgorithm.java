package com.bulain.sharding;

import com.google.common.collect.Range;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class YearShardingTableAlgorithm implements StandardShardingAlgorithm<Date>, HintShardingAlgorithm<Date> {

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
        int lower = 2020;
        int upper = curr;
        if (valueRange.hasLowerBound()) {
            lower = LocalDateTime.ofInstant(valueRange.lowerEndpoint().toInstant(), ZoneId.systemDefault()).getYear();
        }
        if (valueRange.hasUpperBound()) {
            upper = LocalDateTime.ofInstant(valueRange.upperEndpoint().toInstant(), ZoneId.systemDefault()).getYear();
        }

        List<String> list = new ArrayList<>();
        while (lower < upper && lower < curr) {
            list.add(logicTableName + lower);
            lower++;
        }
        if (upper < curr) {
            list.add(logicTableName + upper);
        }

        Collection<String> ret = CollectionUtils.intersection(availableTargetNames, list);
        if (upper >= curr) {
            ret.add(logicTableName);
        }

        return ret;

    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, HintShardingValue<Date> shardingValue) {

        String logicTableName = shardingValue.getLogicTableName();
        Collection<Date> values = shardingValue.getValues();

        int curr = LocalDateTime.now().getYear();
        List<String> list = new ArrayList<>();
        for (Date dt : values) {
            int year = LocalDateTime.ofInstant(dt.toInstant(), ZoneId.systemDefault()).getYear();
            if (year <= curr) {
                list.add(logicTableName + year);
            } else {
                list.add(logicTableName);
            }
        }
        return CollectionUtils.intersection(availableTargetNames, list);
    }

}

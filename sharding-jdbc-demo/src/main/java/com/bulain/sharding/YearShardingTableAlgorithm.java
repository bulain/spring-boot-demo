package com.bulain.sharding;

import com.google.common.collect.Range;
import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.RangeShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import io.shardingsphere.api.algorithm.sharding.standard.RangeShardingAlgorithm;
import org.apache.commons.collections4.CollectionUtils;

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
        int year = LocalDateTime.ofInstant(shardingValue.getValue().toInstant(), ZoneId.systemDefault()).getYear();
        return new StringBuilder(logicTableName).append(year).toString();
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Date> shardingValue) {

        String logicTableName = shardingValue.getLogicTableName();
        Range<Date> valueRange = shardingValue.getValueRange();
        int lower = LocalDateTime.ofInstant(valueRange.lowerEndpoint().toInstant(), ZoneId.systemDefault()).getYear();
        int upper = LocalDateTime.ofInstant(valueRange.upperEndpoint().toInstant(), ZoneId.systemDefault()).getYear();

        List<String> list = new ArrayList<>();
        while (lower < upper) {
            list.add(new StringBuilder(logicTableName).append(lower).toString());
            lower++;
        }
        list.add(new StringBuilder(logicTableName).append(upper).toString());

        return CollectionUtils.intersection(availableTargetNames, list);
    }

}

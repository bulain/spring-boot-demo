package com.bulain.sharding;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> DS_HOLDER = new ThreadLocal<>();

    public static void setDataSourceName(String dataSourceName) {
        DS_HOLDER.set(dataSourceName);
    }

    public static String getDataSourceName() {
        return DS_HOLDER.get();
    }

    public static void clearDataSource() {
        DS_HOLDER.remove();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return getDataSourceName();
    }

}

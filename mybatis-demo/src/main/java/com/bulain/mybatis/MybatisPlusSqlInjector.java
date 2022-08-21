package com.bulain.mybatis;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.AlwaysUpdateSomeColumnById;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import com.baomidou.mybatisplus.extension.injector.methods.Upsert;
import com.bulain.mybatis.core.mybatis.*;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class MybatisPlusSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        Stream.Builder<AbstractMethod> builder = Stream.<AbstractMethod>builder()
                .add(new Upsert())
                .add(new InsertBatchSomeColumn())
                .add(new DirectDelete())
                .add(new DirectUpdate())
                .add(new DirectSelectCount())
                .add(new DirectSelectMaps())
                .add(new DirectSelectMapsPage())
                .add(new DirectSelectList())
                .add(new DirectSelectPage());
        if (tableInfo.havePK()) {
            builder.add(new AlwaysUpdateSomeColumnById())
                    .add(new DirectDeleteById())
                    .add(new DirectDeleteBatchByIds())
                    .add(new DirectUpdateById())
                    .add(new DirectSelectById())
                    .add(new DirectSelectBatchByIds());
        } else {
            logger.warn(String.format("%s ,Not found @TableId annotation, Cannot use Mybatis-Plus 'xxById' Method.",
                    tableInfo.getEntityType()));
        }
        List<AbstractMethod> directs = builder.build().collect(toList());
        methodList.addAll(directs);
        return methodList;
    }

}

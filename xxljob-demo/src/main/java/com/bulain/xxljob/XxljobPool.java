package com.bulain.xxljob;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Component
public class XxljobPool {

    @Autowired
    private XxljobHandler xxljobHandler;
    
    /**
     * 1、简单任务（Bean模式）
     */
    @XxlJob("demoJobHandler")
    public void demoJobHandler() {
        XxlJobHelper.log("XXL-JOB, Hello World.");

        String jobParam = XxlJobHelper.getJobParam();
        XxlJobHelper.log("XXL-JOB, Param: {}", jobParam);

        for (int i = 0; i < 5; i++) {
            XxlJobHelper.log("beat at:" + i);
        }


        String ret = xxljobHandler.handle("call xxljobHandler: {}");

        XxlJobHelper.handleSuccess(ret);

    }


    /**
     * 2、分片广播任务（Bean模式）
     */
    @XxlJob("shardingJobHandler")
    public void shardingJobHandler() {

        // 分片参数
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();

        XxlJobHelper.log("分片参数：当前分片序号 = {}, 总分片数 = {}", shardIndex, shardTotal);

        // 业务逻辑
        for (int i = 0; i < shardTotal; i++) {
            if (i == shardIndex) {
                XxlJobHelper.log("第 {} 片, 命中分片开始处理", i);
            } else {
                XxlJobHelper.log("第 {} 片, 忽略", i);
            }
        }

        XxlJobHelper.handleSuccess();

    }

}

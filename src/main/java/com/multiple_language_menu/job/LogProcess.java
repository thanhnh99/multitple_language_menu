package com.multiple_language_menu.job;

import com.multiple_language_menu.models.request.ReqCreateLog;
import com.multiple_language_menu.repositories.ILogRepository;
import com.multiple_language_menu.repositories.IShopRepository;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogProcess {

    Scheduler scheduler;

    public LogProcess() throws SchedulerException {
        this.scheduler = new StdSchedulerFactory().getScheduler();
    }


    public void createLog(ReqCreateLog requestData,
                          IShopRepository shopRepository,
                          ILogRepository logRepository) throws SchedulerException {
        String identity = "create-log-for-shop_" + requestData.getShopId() + "-" + System.currentTimeMillis();
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("data", requestData);
        jobDataMap.put("shopRepository", shopRepository);
        jobDataMap.put("logRepository", logRepository);
        JobDetail job = JobBuilder.newJob(LogJob.class)
                .withIdentity(identity)
                .withDescription("Create log")
                .usingJobData(jobDataMap)
                .build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(identity)
                .startNow()
                .build();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }
}

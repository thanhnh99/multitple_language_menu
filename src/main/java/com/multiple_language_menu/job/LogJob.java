package com.multiple_language_menu.job;

import com.multiple_language_menu.models.entities.Logs;
import com.multiple_language_menu.models.entities.Shops;
import com.multiple_language_menu.models.request.ReqCreateLog;
import com.multiple_language_menu.repositories.ILogRepository;
import com.multiple_language_menu.repositories.IShopRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class LogJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            ReqCreateLog reqCreateLog = (ReqCreateLog) context.getJobDetail().getJobDataMap().get("data");
            IShopRepository shopRepository = (IShopRepository) context.getJobDetail().getJobDataMap().get("shopRepository");
            ILogRepository logRepository = (ILogRepository) context.getJobDetail().getJobDataMap().get("logRepository");
            //create log
            Shops shop = shopRepository.findById(reqCreateLog.getShopId()).get();
            if(shop != null)
            {
                Logs newLog = new Logs(reqCreateLog);
                newLog.setShop(shop);
                logRepository.save(newLog);
            }
            else
            {
                System.out.println("Shop not found!!!!!!!");
            }
        } catch (Exception e)
        {
            System.out.println("Err in LogJob.execute: " + e.getMessage());
        }
    }
}

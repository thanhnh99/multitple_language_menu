package com.multiple_language_menu.job;

import com.multiple_language_menu.models.entities.Categories;
import com.multiple_language_menu.models.entities.Items;
import com.multiple_language_menu.repositories.ICategoryTranslateRepository;
import com.multiple_language_menu.repositories.IItemTranslateRepository;
import com.multiple_language_menu.repositories.ILogRepository;
import com.multiple_language_menu.repositories.IShopRepository;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TranslateProcess {
    @Autowired
    LogProcess logProcess;

    @Autowired
    MailProcess mailProcess;

    Scheduler scheduler;

    public TranslateProcess() throws SchedulerException {
        this.scheduler = new StdSchedulerFactory().getScheduler();
    }


    public void translateCategory(Categories requestData,
                                  ICategoryTranslateRepository categoryTranslateRepository,
                                  IShopRepository shopRepository,
                                  ILogRepository logRepository,
                                  JavaMailSender emailSender) throws SchedulerException {
        List<String> languageCode = requestData.getShop().getLanguageCodes();
        String identity = "translate-category" + requestData.getId() + "-" + System.currentTimeMillis();
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("data", requestData);
        jobDataMap.put("languageCode", languageCode);
        jobDataMap.put("categoryTranslateRepository", categoryTranslateRepository);
        jobDataMap.put("shopRepository", shopRepository);
        jobDataMap.put("logRepository", logRepository);
        jobDataMap.put("logProcess", logProcess);
        jobDataMap.put("mailProcess", mailProcess);
        jobDataMap.put("emailSender", emailSender);
        JobDetail job = JobBuilder.newJob(TranslateCategoryJob.class)
//                .withIdentity(identity)
                .withDescription("Translate category")
                .usingJobData(jobDataMap)
                .build();
        Trigger trigger = TriggerBuilder.newTrigger()
//                .withIdentity(identity)
                .startNow()
                .build();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }

    public void translateItem(Items requestData,
                              IItemTranslateRepository itemTranslateRepository,
                              IShopRepository shopRepository,
                              ILogRepository logRepository,
                              JavaMailSender emailSender) throws SchedulerException {
        List<String> languageCode = requestData.getCategory().getShop().getLanguageCodes();
        String identity = "translate-item" + requestData.getId() + "-" + System.currentTimeMillis();
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("data", requestData);
        jobDataMap.put("languageCode", languageCode);
        jobDataMap.put("itemTranslateRepository", itemTranslateRepository);
        jobDataMap.put("shopRepository", shopRepository);
        jobDataMap.put("logRepository", logRepository);
        jobDataMap.put("logProcess", logProcess);
        jobDataMap.put("mailProcess", mailProcess);
        jobDataMap.put("emailSender", emailSender);
        JobDetail job = JobBuilder.newJob(TranslateItemJob.class)
//                .withIdentity(identity)
                .withDescription("Translate item")
                .usingJobData(jobDataMap)
                .build();
        Trigger trigger = TriggerBuilder.newTrigger()
//                .withIdentity(identity)
                .startNow()
                .build();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }
}

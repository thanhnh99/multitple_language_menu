package com.multiple_language_menu.job;

import com.multiple_language_menu.enums.LanguageConstant;
import com.multiple_language_menu.models.entities.Categories;
import com.multiple_language_menu.models.entities.Items;
import com.multiple_language_menu.models.request.ReqTranslateItem;
import com.multiple_language_menu.repositories.ICategoryTranslateRepository;
import com.multiple_language_menu.repositories.IItemTranslateRepository;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TranslateProcess {

    Scheduler scheduler;

    public TranslateProcess() throws SchedulerException {
        this.scheduler = new StdSchedulerFactory().getScheduler();
    }


    public void translateCategory(Categories requestData, ICategoryTranslateRepository category) throws SchedulerException {
        List<String> languageCode = LanguageConstant.languageCode();
        String identity = "translate-category" + requestData.getId() + "-" + System.currentTimeMillis();
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("data", requestData);
        jobDataMap.put("languageCode", languageCode);
        jobDataMap.put("category", category);
        JobDetail job = JobBuilder.newJob(TranslateCategoryJob.class)
                .withIdentity(identity)
                .withDescription("Translate category")
                .usingJobData(jobDataMap)
                .build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(identity)
                .startNow()
                .build();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }

    public void translateItem(Items requestData, IItemTranslateRepository category) throws SchedulerException {
        List<String> languageCode = LanguageConstant.languageCode();
        String identity = "translate-item" + requestData.getId() + "-" + System.currentTimeMillis();
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("data", requestData);
        jobDataMap.put("languageCode", languageCode);
        jobDataMap.put("category", category);
        JobDetail job = JobBuilder.newJob(TranslateItemJob.class)
                .withIdentity(identity)
                .withDescription("Translate item")
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

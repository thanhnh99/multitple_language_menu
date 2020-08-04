package com.multiple_language_menu.job;

import com.multiple_language_menu.services.TranslateService;
import lombok.SneakyThrows;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TranslateJob implements Job {

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("Hello TranslateJob!");
        Thread.sleep(5000);
        System.out.println("Hello TranslateJob! 1000");
    }
}

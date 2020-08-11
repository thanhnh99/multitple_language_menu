package com.multiple_language_menu.job;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.multiple_language_menu.models.entities.Items;
import com.multiple_language_menu.models.entities.ItemsTranslates;
import com.multiple_language_menu.models.request.ReqCreateLog;
import com.multiple_language_menu.models.request.ReqSendMail;
import com.multiple_language_menu.models.request.ReqTranslateItem;
import com.multiple_language_menu.repositories.IItemTranslateRepository;
import com.multiple_language_menu.repositories.ILogRepository;
import com.multiple_language_menu.repositories.IShopRepository;
import lombok.SneakyThrows;
import org.hibernate.validator.constraints.UniqueElements;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;
import sun.rmi.runtime.Log;

import java.util.List;

public class TranslateItemJob implements Job {

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Items requestData = (Items) context.getJobDetail().getJobDataMap().get("data");
        ReqTranslateItem reqTranslateItem = new ReqTranslateItem(requestData);
        List<String> languageCodes = (List<String>) context.getJobDetail().getJobDataMap().get("languageCode");
        IItemTranslateRepository itemTranslateRepository = (IItemTranslateRepository) context.getJobDetail().getJobDataMap().get("itemTranslateRepository");
        ILogRepository logRepository = (ILogRepository) context.getJobDetail().getJobDataMap().get("logRepository");
        IShopRepository shopRepository = (IShopRepository) context.getJobDetail().getJobDataMap().get("shopRepository");
        LogProcess logProcess = (LogProcess) context.getJobDetail().getJobDataMap().get("logProcess");
        MailProcess mailProcess = (MailProcess) context.getJobDetail().getJobDataMap().get("mailProcess");
        JavaMailSender emailSender = (JavaMailSender) context.getJobDetail().getJobDataMap().get("emailSender");


        for(String languageCode : languageCodes)
        {
            reqTranslateItem.setItemName(this.translate(reqTranslateItem.getItemName(), languageCode));
            reqTranslateItem.setDescription(this.translate(reqTranslateItem.getDescription(),languageCode));
            try {
                ItemsTranslates itemsTranslate = itemTranslateRepository.findByItemAndLanguageCode(requestData, languageCode);
                if(itemsTranslate != null)
                {
                    itemsTranslate.setName(reqTranslateItem.getItemName());
                    itemsTranslate.setDescription(reqTranslateItem.getDescription());
                    itemTranslateRepository.save(itemsTranslate);
                }
                else {
                    ItemsTranslates newItemsTranslate = new ItemsTranslates(reqTranslateItem);
                    newItemsTranslate.setItem(requestData);
                    newItemsTranslate.setLanguageCode(languageCode);
                    itemTranslateRepository.save(newItemsTranslate);
                }
            } catch (Exception e)
            {
                System.out.println("Err in TranslateItem.execute: " + e.getMessage());
            }
        }
        ReqCreateLog reqCreateLog = new ReqCreateLog(requestData);
        logProcess.createLog(reqCreateLog,shopRepository,logRepository);
        logProcess.createLog(reqCreateLog,shopRepository,logRepository);
        ReqSendMail reqSendMail = new ReqSendMail(requestData.getCategory().getShop().getOwner().getEmail(),
                "ITEM " + requestData.getName() + " HAS TRANSLATED",
                " ITEM " + requestData.getName() + " HAS TRANSLATED");
        mailProcess.sendMail(reqSendMail,emailSender);
    }

    private String translate(String content, String targetLanguageCode)
    {
        try {
            String url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=" + targetLanguageCode + "&dt=t&q=" + content;
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(url, String.class);
            JsonArray convertedObject = new Gson().fromJson(result, JsonArray.class);
            String targetResult = convertedObject.get(0).getAsJsonArray().get(0).getAsJsonArray().get(0).toString();
            return targetResult.substring(1,targetResult.length()-1);
        }catch (Exception e)
        {
            System.out.println("Err in TranslateItemJob.translate: " + e.getMessage());
            return null;
        }
    }
}

package com.multiple_language_menu.job;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.multiple_language_menu.models.entities.Categories;
import com.multiple_language_menu.models.entities.CategoriesTranslates;
import com.multiple_language_menu.models.request.ReqCreateLog;
import com.multiple_language_menu.models.request.ReqSendMail;
import com.multiple_language_menu.models.request.ReqTranslateCategory;
import com.multiple_language_menu.repositories.ICategoryTranslateRepository;
import com.multiple_language_menu.repositories.ILogRepository;
import com.multiple_language_menu.repositories.IShopRepository;
import lombok.SneakyThrows;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class TranslateCategoryJob implements Job {

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Execute Translate category");
        Categories requestData = (Categories) context.getJobDetail().getJobDataMap().get("data");
        ICategoryTranslateRepository categoryTranslateRepository = (ICategoryTranslateRepository) context.getJobDetail().getJobDataMap().get("categoryTranslateRepository");
        ILogRepository logRepository = (ILogRepository) context.getJobDetail().getJobDataMap().get("logRepository");
        IShopRepository shopRepository = (IShopRepository) context.getJobDetail().getJobDataMap().get("shopRepository");
        LogProcess logProcess = (LogProcess) context.getJobDetail().getJobDataMap().get("logProcess");
        MailProcess mailProcess = (MailProcess) context.getJobDetail().getJobDataMap().get("mailProcess");
        JavaMailSender emailSender = (JavaMailSender) context.getJobDetail().getJobDataMap().get("emailSender");
        List<String> languageCodes = (List<String>) context.getJobDetail().getJobDataMap().get("languageCode");
        for(String languageCode : languageCodes)
        {
            ReqTranslateCategory reqTranslateCategory = new ReqTranslateCategory(requestData);
            reqTranslateCategory.setName(this.translate(reqTranslateCategory.getName(), languageCode));
            reqTranslateCategory.setDescription(this.translate(requestData.getDescription(),languageCode));
            try {
                CategoriesTranslates categoriesTranslate = categoryTranslateRepository.findByCategoryAndLanguageCode(requestData,languageCode);
                if(categoriesTranslate != null)
                {
                    categoriesTranslate.setName(reqTranslateCategory.getName());
                    categoriesTranslate.setDescription(reqTranslateCategory.getDescription());
                    categoryTranslateRepository.save(categoriesTranslate);
                }
                else
                {
                    categoriesTranslate = new CategoriesTranslates(reqTranslateCategory);
                    categoriesTranslate.setCategory(requestData);
                    categoriesTranslate.setLanguageCode(languageCode);
                    categoryTranslateRepository.save(categoriesTranslate);
                }

            } catch (Exception e)
            {
                System.out.println("Err in TranslateCategory.excute: " + e.getMessage());
            }
        }
        ReqCreateLog reqCreateLog = new ReqCreateLog(requestData);
        logProcess.createLog(reqCreateLog,shopRepository,logRepository);
        ReqSendMail reqSendMail = new ReqSendMail(requestData.getShop().getOwner().getEmail(),
                "CATEGORY " + requestData.getName() + " HAS TRANSLATED",
                " CATEGORY " + requestData.getName() + " HAS TRANSLATED");
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
        } catch (Exception e)
        {
            System.out.println("Err in TranslateCategoryJob.translate: " + e.getMessage());
            return null;
        }
    }
}

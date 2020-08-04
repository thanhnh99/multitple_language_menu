package com.multiple_language_menu.job;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.multiple_language_menu.models.entities.Items;
import com.multiple_language_menu.models.entities.ItemsTranslates;
import com.multiple_language_menu.models.request.ReqTranslateItem;
import com.multiple_language_menu.repositories.IItemTranslateRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class TranslateItemJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Items requestData = (Items) context.getJobDetail().getJobDataMap().get("data");
        ReqTranslateItem reqTranslateItem = new ReqTranslateItem(requestData);
        List<String> languageCodes = (List<String>) context.getJobDetail().getJobDataMap().get("languageCode");
        IItemTranslateRepository itemTranslateRepository = (IItemTranslateRepository) context.getJobDetail().getJobDataMap().get("category");
        for(String languageCode : languageCodes)
        {
            reqTranslateItem.setItemName(this.translate(reqTranslateItem.getItemName(), languageCode));
            requestData.setDescription(this.translate(requestData.getDescription(),languageCode));
            try {
                ItemsTranslates itemsTranslates = new ItemsTranslates(reqTranslateItem);
                itemsTranslates.setItem(requestData);
                itemsTranslates.setLanguageCode(languageCode);
                itemTranslateRepository.save(itemsTranslates);
            } catch (Exception e)
            {
                System.out.println("Err in TranslateItem.execute: " + e.getMessage());
            }

        }
    }

    private String translate(String content, String targetLanguageCode)
    {
        String url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=" + targetLanguageCode + "&dt=t&q=" + content;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        JsonArray convertedObject = new Gson().fromJson(result, JsonArray.class);
        String targetResult = convertedObject.get(0).getAsJsonArray().get(0).getAsJsonArray().get(0).toString();
        return targetResult;
    }
}

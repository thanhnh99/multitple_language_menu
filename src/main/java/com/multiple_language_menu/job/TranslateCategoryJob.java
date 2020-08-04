package com.multiple_language_menu.job;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.multiple_language_menu.models.entities.Categories;
import com.multiple_language_menu.models.entities.CategoriesTranslates;
import com.multiple_language_menu.models.entities.ItemsTranslates;
import com.multiple_language_menu.models.request.ReqTransCategory;
import com.multiple_language_menu.models.request.ReqTranslateItem;
import com.multiple_language_menu.repositories.ICategoryTranslateRepository;
import lombok.SneakyThrows;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class TranslateCategoryJob implements Job {


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Execute Translate category");
        Categories requestData = (Categories) context.getJobDetail().getJobDataMap().get("data");
        ReqTransCategory reqTransCategory = new ReqTransCategory(requestData);
        ICategoryTranslateRepository categoryTranslateRepository = (ICategoryTranslateRepository) context.getJobDetail().getJobDataMap().get("category");
        List<String> languageCodes = (List<String>) context.getJobDetail().getJobDataMap().get("languageCode");
        for(String languageCode : languageCodes)
        {
            reqTransCategory.setName(this.translate(reqTransCategory.getName(), languageCode));
            reqTransCategory.setDescription(this.translate(requestData.getDescription(),languageCode));
            try {
                CategoriesTranslates categoriesTranslate = new CategoriesTranslates(reqTransCategory);
                categoriesTranslate.setCategory(requestData);
                categoriesTranslate.setLanguageCode(languageCode);
                categoryTranslateRepository.save(categoriesTranslate);
            } catch (Exception e)
            {
                System.out.println("Err in TranslateCategory.excute: " + e.getMessage());
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

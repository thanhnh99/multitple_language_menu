package com.multiple_language_menu.job;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.multiple_language_menu.models.entities.Categories;
import com.multiple_language_menu.models.entities.CategoriesTranslates;
import com.multiple_language_menu.models.request.ReqTranslateCategory;
import com.multiple_language_menu.repositories.ICategoryTranslateRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class TranslateCategoryJob implements Job {


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Execute Translate category");
        Categories requestData = (Categories) context.getJobDetail().getJobDataMap().get("data");
        ICategoryTranslateRepository categoryTranslateRepository = (ICategoryTranslateRepository) context.getJobDetail().getJobDataMap().get("category");
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
                    reqTranslateCategory.setDescription(reqTranslateCategory.getDescription());
                    categoryTranslateRepository.save(categoriesTranslate);
                }
                else
                {
                    CategoriesTranslates newCategoriesTranslate = new CategoriesTranslates(reqTranslateCategory);
                    newCategoriesTranslate.setCategory(requestData);
                    newCategoriesTranslate.setLanguageCode(languageCode);
                    categoryTranslateRepository.save(newCategoriesTranslate);
                }
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

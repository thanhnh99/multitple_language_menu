package com.multiple_language_menu.constants;

import java.util.ArrayList;
import java.util.List;

public class LanguageConstant {
    public static List<String> languageCode()
    {
        List<String> languageCodes = new ArrayList<String>();
        languageCodes.add("vi");
        languageCodes.add("en");
        languageCodes.add("ja");
        return languageCodes;
    }
}

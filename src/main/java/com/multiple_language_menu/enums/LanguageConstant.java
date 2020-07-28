package com.multiple_language_menu.enums;

import java.util.ArrayList;
import java.util.List;

public class LangugeConstant {
    public static List<String> languageCodes = new ArrayList<String>();
    public static List<String> languageCode()
    {
        languageCode().add("vi");
        languageCode().add("en");
        return languageCodes;
    }
}

package com.codecooks.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CountryManager {

  private HashMap<String, String> countryToCodeMap = new HashMap<>();

  public CountryManager() {

    for (String countryCode: Locale.getISOCountries()) {
      Locale locale = new Locale("", countryCode);
      String countryName = locale.getDisplayName();
      countryToCodeMap.put(countryName, countryCode);

    }
  }

  public String getNameFromCode(String code) {
    Locale locale = new Locale("", code);
    return locale.getDisplayName();
  }

  public String getCodeFromName(String name) {
    return countryToCodeMap.get(name);

  }

  public List<String> getCountryNames() {
      return new ArrayList<>(countryToCodeMap.keySet());
  }
}

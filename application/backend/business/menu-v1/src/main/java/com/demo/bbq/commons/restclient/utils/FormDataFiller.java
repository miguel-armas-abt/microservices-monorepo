package com.demo.bbq.commons.restclient.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static com.demo.bbq.commons.restclient.utils.ParameterMapFiller.addProvidedParams;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FormDataFiller {

  public static Map<String, String> fillFormData(Map<String, String> providedParams,
                                                 Map<String, String> currentParams) {

    Map<String, String> params = new HashMap<>(currentParams);
    addProvidedParams(providedParams).accept(params);
    return params;
  }
}
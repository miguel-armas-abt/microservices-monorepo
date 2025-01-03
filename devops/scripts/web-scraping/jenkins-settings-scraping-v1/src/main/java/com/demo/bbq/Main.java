package com.demo.bbq;

import com.demo.bbq.service.SettingService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.demo.bbq.config.ComponentsConfig;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    Injector injector = Guice.createInjector(new ComponentsConfig());

    SettingService service = injector.getInstance(SettingService.class);
    service.scrapSettings();
  }

}
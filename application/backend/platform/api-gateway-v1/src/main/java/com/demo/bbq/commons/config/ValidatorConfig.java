package com.demo.bbq.commons.config;

import com.demo.bbq.commons.toolkit.validator.body.BodyValidator;
import com.demo.bbq.commons.toolkit.validator.headers.HeaderValidator;
import com.demo.bbq.commons.toolkit.validator.params.ParamValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;

@Configuration
public class ValidatorConfig {

  @Bean
  public BodyValidator requestValidator(Validator validator) {
    return new BodyValidator();
  }

  @Bean
  public HeaderValidator headerValidator(BodyValidator bodyValidator) {
    return new HeaderValidator(bodyValidator);
  }

  @Bean
  public ParamValidator paramValidator(BodyValidator bodyValidator) {
    return new ParamValidator(bodyValidator);
  }
}
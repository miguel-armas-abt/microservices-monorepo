package com.demo.poc.commons.custom.properties;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuInfo implements Serializable {

  private Class<?> selectorClass;
}

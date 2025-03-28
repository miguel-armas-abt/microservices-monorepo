package com.demo.poc.entrypoint.calculator.repository.discount.wrapper;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRequestWrapper implements Serializable {

  private Integer quantity;
  private String productCode;
}

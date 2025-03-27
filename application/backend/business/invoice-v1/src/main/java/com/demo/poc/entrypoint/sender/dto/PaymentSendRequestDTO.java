package com.demo.poc.entrypoint.sender.dto;

import com.demo.poc.entrypoint.calculator.dto.request.ProductRequestDTO;
import java.io.Serializable;
import java.util.List;
import jakarta.validation.Valid;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSendRequestDTO implements Serializable {

  @Valid
  private List<ProductRequestDTO> productList;

  @Valid
  private CustomerDTO customer;

  @Valid
  private PaymentDTO payment;
}

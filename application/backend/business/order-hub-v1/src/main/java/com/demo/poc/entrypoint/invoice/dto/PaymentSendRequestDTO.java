package com.demo.poc.entrypoint.invoice.dto;

import java.io.Serializable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSendRequestDTO implements Serializable {

  @NotNull
  private Integer tableNumber;

  @NotNull
  private CustomerDTO customer;

  @NotNull
  private PaymentDTO payment;
}

package com.demo.bbq.entrypoint.processor.repository.processor.wrapper.response;

import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseWrapper implements Serializable {

  private String id;
  private String status;
}
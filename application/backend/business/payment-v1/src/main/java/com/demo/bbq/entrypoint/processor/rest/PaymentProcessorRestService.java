package com.demo.bbq.entrypoint.processor.rest;

import com.demo.bbq.entrypoint.processor.repository.processor.PaymentProcessorRepository;
import com.demo.bbq.entrypoint.processor.repository.processor.wrapper.request.PaymentRequestWrapper;
import com.demo.bbq.entrypoint.processor.repository.processor.wrapper.response.PaymentResponseWrapper;
import io.reactivex.rxjava3.core.Observable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(produces = "application/x-ndjson", value = "/bbq/business/payment/v1")
public class PaymentProcessorRestService {

  private final PaymentProcessorRepository repository;

  @PostMapping("/execute")
  public Observable<PaymentResponseWrapper> execute() {
    return repository.executeFacade(PaymentRequestWrapper.builder().build());
  }

}
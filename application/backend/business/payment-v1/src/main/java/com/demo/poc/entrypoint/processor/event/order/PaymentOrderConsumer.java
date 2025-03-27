package com.demo.poc.entrypoint.processor.event.order;

import com.demo.poc.entrypoint.processor.event.order.message.PaymentOrderMessage;
import com.demo.poc.entrypoint.processor.event.confirmation.PaymentConfirmationProducer;
import com.demo.poc.entrypoint.processor.mapper.PaymentMapper;
import com.demo.poc.entrypoint.processor.repository.payment.PaymentRepository;
import com.demo.poc.entrypoint.processor.repository.processor.PaymentProcessorRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.messaging.Message;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentOrderConsumer {

  private final PaymentRepository paymentRepository;
  private final PaymentMapper paymentMapper;
  private final PaymentProcessorRepository paymentProcessorRepository;
  private final PaymentConfirmationProducer paymentConfirmationProducer;

  @KafkaListener(topics = "${kafka-broker.topic.invoice}")
  public void listen(Message<String> message) {
    String payload = message.getPayload();
    log.info("listening message: " + payload);
    PaymentOrderMessage payment = new Gson().fromJson(payload, PaymentOrderMessage.class);

//    if(paymentProcessorRepository.process(PaymentRequestWrapper.builder().amount(payment.getTotalAmount()).clientCompany("BBQ").build()).getIsSuccessfulTransaction()) {
//      paymentRepository.save(paymentMapper.toEntity(payment));
//      paymentConfirmationProducer.sendMessage(new Gson().toJson(PaymentConfirmationMessage.builder().invoiceId(payment.getInvoiceId()).paidAmount(payment.getTotalAmount())));
//    }
  }
}

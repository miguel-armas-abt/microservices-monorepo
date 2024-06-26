package com.demo.bbq.application.mapper;

import com.demo.bbq.application.dto.sender.request.CustomerDTO;
import com.demo.bbq.application.dto.calculator.request.ProductRequestDTO;
import com.demo.bbq.application.events.producer.message.PaymentMessage;
import com.demo.bbq.application.dto.calculator.response.ProductDTO;
import com.demo.bbq.application.dto.calculator.response.InvoiceResponseDTO;
import com.demo.bbq.repository.invoice.entity.PaymentMethod;
import com.demo.bbq.repository.invoice.entity.InvoiceEntity;
import java.math.BigDecimal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

  @Mapping(target = "customerEntity", source = "customer")
  @Mapping(target = "paymentMethod", expression = "java(getPaymentMethod(paymentMethod))")
  InvoiceEntity toEntity(InvoiceResponseDTO invoice, CustomerDTO customer, String paymentMethod);

  @Mapping(target = "paymentMethod", expression = "java(invoiceEntity.getPaymentMethod().name())")
  @Mapping(target = "invoiceId", source = "invoiceEntity.id")
  PaymentMessage toMessage(InvoiceEntity invoiceEntity, BigDecimal totalAmount);

  @Mapping(target = "subtotal", expression = "java(getSubtotal(product, unitPrice, discount))")
  ProductDTO toResponseDTO(ProductRequestDTO product, BigDecimal unitPrice, BigDecimal discount);

  default BigDecimal getSubtotal(ProductRequestDTO request, BigDecimal unitPrice, BigDecimal discount) {
    BigDecimal subtotal = unitPrice.multiply(new BigDecimal(request.getQuantity()));
    return subtotal.subtract(subtotal.multiply(discount));
  }

  default PaymentMethod getPaymentMethod(String paymentMethod) {
    return PaymentMethod.valueOf(paymentMethod);
  }
}

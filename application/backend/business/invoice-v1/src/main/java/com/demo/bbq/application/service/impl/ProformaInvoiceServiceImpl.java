package com.demo.bbq.application.service.impl;

import com.demo.bbq.application.dto.proformainvoice.request.ProductRequestDTO;
import com.demo.bbq.application.dto.proformainvoice.response.ProductResponseDTO;
import com.demo.bbq.application.dto.proformainvoice.response.ProformaInvoiceResponseDTO;
import com.demo.bbq.application.dto.rules.DiscountRule;
import com.demo.bbq.application.mapper.InvoiceMapper;
import com.demo.bbq.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.application.service.ProformaInvoiceService;
import com.demo.bbq.repository.product.ProductRepository;
import com.demo.bbq.application.rules.service.RuleService;
import io.reactivex.rxjava3.core.Single;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProformaInvoiceServiceImpl implements ProformaInvoiceService {

  private final ServiceConfigurationProperties properties;

  private final InvoiceMapper proformaInvoiceMapper;

  private final ProductRepository productRepository;

  private final RuleService ruleService;

  @Override
  public Single<ProformaInvoiceResponseDTO> generateProformaInvoice(List<ProductRequestDTO> products) {
    AtomicReference<BigDecimal> subtotalInvoice = new AtomicReference<>(BigDecimal.ZERO);
    List<ProductResponseDTO> productList = products.stream()
        .map(productRequest -> {
          BigDecimal unitPrice = productRepository.findByProductCode(productRequest.getProductCode()).blockingGet().getUnitPrice();
          return proformaInvoiceMapper.toResponseDTO(productRequest, unitPrice, applyDiscount(productRequest));
        })
        .peek(product -> subtotalInvoice.set(subtotalInvoice.get().add(product.getSubtotal()))).collect(Collectors.toList());
    return Single.just(toProformaInvoice(productList, subtotalInvoice.get()));
  }

  private ProformaInvoiceResponseDTO toProformaInvoice(List<ProductResponseDTO> productList, BigDecimal subtotal) {
    BigDecimal igv = new BigDecimal(properties.getVariables().get("igv"));
    return ProformaInvoiceResponseDTO.builder()
        .igv(igv)
        .subtotal(subtotal)
        .productList(productList)
        .total(subtotal.add(subtotal.multiply(igv)))
        .build();
  }

  private BigDecimal applyDiscount(ProductRequestDTO request) {
    DiscountRule discountRule = ruleService.process(DiscountRule.builder()
        .quantity(request.getQuantity())
        .productCode(request.getProductCode())
        .build());
    return BigDecimal.valueOf(discountRule.getDiscount());
  }
}
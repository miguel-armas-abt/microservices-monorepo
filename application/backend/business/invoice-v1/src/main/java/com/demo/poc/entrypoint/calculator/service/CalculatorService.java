package com.demo.poc.entrypoint.calculator.service;

import com.demo.poc.entrypoint.calculator.dto.request.ProductRequestDTO;
import com.demo.poc.entrypoint.calculator.dto.response.InvoiceResponseDTO;
import java.util.Map;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CalculatorService {

  Mono<InvoiceResponseDTO> calculateInvoice(Map<String, String> headers, Flux<ProductRequestDTO> products);

}

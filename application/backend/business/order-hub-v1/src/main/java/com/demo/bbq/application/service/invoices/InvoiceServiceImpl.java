package com.demo.bbq.application.service.invoices;

import com.demo.bbq.application.dto.invoices.InvoicePaymentRequestDTO;
import com.demo.bbq.application.mapper.InvoiceMapper;
import com.demo.bbq.repository.invoice.InvoiceRepository;
import com.demo.bbq.repository.invoice.wrapper.request.PaymentRequestWrapper;
import com.demo.bbq.repository.invoice.wrapper.request.ProductRequestWrapper;
import com.demo.bbq.repository.invoice.wrapper.response.ProformaInvoiceResponseWrapper;
import com.demo.bbq.repository.menu.MenuRepositoryHelper;
import com.demo.bbq.repository.tableorder.TableOrderRepository;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

  private final InvoiceRepository invoiceRepository;

  private final TableOrderRepository tableOrderRepository;

  private final MenuRepositoryHelper menuRepositoryHelper;

  private final InvoiceMapper invoiceMapper;

  @Override
  public Single<ProformaInvoiceResponseWrapper> generateProforma(List<ProductRequestWrapper> productList) {
    return invoiceRepository.generateProforma(productList);
  }

  @Override
  public Completable sendToPay(InvoicePaymentRequestDTO invoicePaymentRequestDTO) {
    List<ProductRequestWrapper> productList = tableOrderRepository.findByTableNumber(invoicePaymentRequestDTO.getTableNumber())
        .blockingGet()
        .getMenuOrderList()
        .stream()
        .map(menuOrder -> Optional.of(menuRepositoryHelper.getService().findByProductCode(menuOrder.getProductCode()).blockingGet())
            .map(menuOption -> invoiceMapper.toProduct(menuOrder, menuOption))
            .get())
        .collect(Collectors.toList());

    PaymentRequestWrapper paymentRequestWrapper = invoiceMapper.toPaymentRequest(invoicePaymentRequestDTO);
    paymentRequestWrapper.setProductList(productList);

    return invoiceRepository.sendToPay(paymentRequestWrapper)
        .ignoreElement().andThen(tableOrderRepository.cleanTable(invoicePaymentRequestDTO.getTableNumber()).ignoreElement());
  }

}
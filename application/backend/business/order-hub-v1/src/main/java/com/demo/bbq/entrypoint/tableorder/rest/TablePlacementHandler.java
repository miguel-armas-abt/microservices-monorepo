package com.demo.bbq.entrypoint.tableorder.rest;

import com.demo.bbq.commons.validations.body.BodyValidator;
import com.demo.bbq.commons.validations.headers.DefaultHeaders;
import com.demo.bbq.commons.validations.headers.HeaderValidator;
import com.demo.bbq.commons.validations.params.ParamValidator;
import com.demo.bbq.entrypoint.tableorder.dto.request.MenuOrderRequestDTO;
import com.demo.bbq.entrypoint.tableorder.dto.params.TableNumberParam;
import com.demo.bbq.entrypoint.tableorder.service.TablePlacementService;
import com.demo.bbq.commons.restserver.ServerResponseFactory;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.demo.bbq.commons.restclient.utils.HttpHeadersFiller.extractHeadersAsMap;
import static com.demo.bbq.commons.restclient.utils.QueryParamFiller.extractQueryParamsAsMap;

@Component
@RequiredArgsConstructor
public class TablePlacementHandler {

  private final TablePlacementService tablePlacementService;
  private final BodyValidator bodyValidator;
  private final ParamValidator paramValidator;
  private final HeaderValidator headerValidator;

  public Mono<ServerResponse> generateTableOrder(ServerRequest serverRequest) {
    Map<String, String> headers = extractHeadersAsMap(serverRequest);
    headerValidator.validate(headers, DefaultHeaders.class);
    TableNumberParam tableNumberParam = paramValidator.validateAndRetrieve(extractQueryParamsAsMap(serverRequest), TableNumberParam.class);

    return serverRequest.bodyToFlux(MenuOrderRequestDTO.class)
        .doOnNext(bodyValidator::validate)
        .collectList()
        .flatMap(requestedMenuOrders -> tablePlacementService.generateTableOrder(headers, requestedMenuOrders, tableNumberParam.getTableNumber()))
        .then(ServerResponseFactory.buildEmpty(serverRequest.headers()));
  }

  public Mono<ServerResponse> findByTableNumber(ServerRequest serverRequest) {
    Map<String, String> headers = extractHeadersAsMap(serverRequest);
    headerValidator.validate(headers, DefaultHeaders.class);
    TableNumberParam tableNumberParam = paramValidator.validateAndRetrieve(extractQueryParamsAsMap(serverRequest), TableNumberParam.class);

    return tablePlacementService.findByTableNumber(headers, tableNumberParam.getTableNumber())
        .flatMap(response -> ServerResponseFactory.buildMono(ServerResponse.ok(), serverRequest.headers(), response));
  }
}

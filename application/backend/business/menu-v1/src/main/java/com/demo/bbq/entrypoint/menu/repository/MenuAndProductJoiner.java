package com.demo.bbq.entrypoint.menu.repository;

import com.demo.bbq.commons.errors.exceptions.BusinessException;
import com.demo.bbq.entrypoint.menu.dto.request.MenuSaveRequestDTO;
import com.demo.bbq.entrypoint.menu.dto.request.MenuUpdateRequestDTO;
import com.demo.bbq.entrypoint.menu.mapper.MenuRequestMapper;
import com.demo.bbq.entrypoint.menu.repository.menu.MenuRepository;
import com.demo.bbq.entrypoint.menu.repository.menu.entity.MenuEntity;
import com.demo.bbq.entrypoint.menu.repository.product.ProductRepository;
import com.demo.bbq.entrypoint.menu.repository.product.wrapper.response.ProductResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MenuAndProductJoiner {

  private final String PRODUCT_SCOPE = "MENU";
  private final ProductRepository productRepository;
  private final MenuRepository menuRepository;
  private final MenuRequestMapper mapper;

  public Map<MenuEntity, ProductResponseWrapper> findAll(Map<String, String> headers) {
    Map<String, MenuEntity> menuOptionMap = menuRepository.findAll().stream()
        .collect(Collectors.toMap(MenuEntity::getProductCode, Function.identity()));

    Map<MenuEntity, ProductResponseWrapper> menuAndProductMap = new HashMap<>();
    productRepository.findByScope(headers, PRODUCT_SCOPE)
        .forEach(product -> menuAndProductMap.put(menuOptionMap.get(product.getCode()), product));

    return menuAndProductMap;
  }

  public void save(Map<String, String> headers, MenuSaveRequestDTO menuOption) {
    productRepository.save(headers, mapper.toRequestWrapper(menuOption, PRODUCT_SCOPE));
    menuRepository.save(mapper.toEntity(menuOption));
  }

  public void update(Map<String, String> headers, String productCode, MenuUpdateRequestDTO menuOption) {
    menuRepository.findByProductCode(productCode)
        .map(menuOptionFound -> {
          MenuEntity menuEntity = mapper.toEntity(menuOption, productCode);
          menuEntity.setId(menuOptionFound.getId());
          return menuRepository.save(menuEntity);
        })
        .orElseThrow(() -> new BusinessException("MenuOptionNotFound", "The menu option does not exist"));
    productRepository.update(headers, productCode, mapper.toRequestWrapper(menuOption, PRODUCT_SCOPE));
  }

  @Transactional
  public void deleteByProductCode(Map<String, String> headers, String productCode) {
    menuRepository.findByProductCode(productCode)
        .map(menuOptionFound -> {
          menuRepository.deleteByProductCode(productCode);
          return menuOptionFound;
        })
        .orElseThrow(() -> new BusinessException("MenuOptionNotFound", "The menu option does not exist"));
    productRepository.delete(headers, productCode);
  }

}
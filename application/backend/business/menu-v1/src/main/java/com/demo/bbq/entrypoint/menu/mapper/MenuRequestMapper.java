package com.demo.bbq.entrypoint.menu.mapper;

import com.demo.bbq.entrypoint.menu.dto.request.MenuSaveRequestDTO;
import com.demo.bbq.entrypoint.menu.dto.request.MenuUpdateRequestDTO;
import com.demo.bbq.entrypoint.menu.repository.menu.entity.MenuEntity;
import com.demo.bbq.entrypoint.menu.repository.product.wrapper.request.ProductSaveRequestWrapper;
import com.demo.bbq.entrypoint.menu.repository.product.wrapper.request.ProductUpdateRequestWrapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuRequestMapper {

  MenuEntity toEntity(MenuSaveRequestDTO menuOption);

  MenuEntity toEntity(MenuUpdateRequestDTO menuOption, String productCode);

  @Mapping(target = "code", source = "menuOption.productCode")
  ProductSaveRequestWrapper toRequestWrapper(MenuSaveRequestDTO menuOption, String scope);

  ProductUpdateRequestWrapper toRequestWrapper(MenuUpdateRequestDTO menuOption, String scope);
}

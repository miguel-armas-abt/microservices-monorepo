package com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.menuoption.menuoptionv2.retrofit;

import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.menuoption.dto.MenuOptionDto;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.menuoption.dto.MenuOptionRequestDto;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.*;

public interface MenuOptionV2Api {

  @Streaming
  @Headers({"Accept: application/stream+json"})
  @GET("menu-options/{id}")
  Single<MenuOptionDto> findById(@Path(value = "id") Long id);

  @Streaming
  @Headers({"Accept: application/stream+json"})
  @GET("menu-options")
  Observable<ResponseBody> findByCategory(@Query("category") String category);

  @Streaming
  @POST("menu-options")
  Single<ResponseBody> save(@Body MenuOptionRequestDto menuOptionRequest);

  @Streaming
  @PUT("menu-options/{id}")
  Single<ResponseBody> update(@Path(value = "id") Long id, @Body MenuOptionRequestDto menuOptionRequest);

  @Streaming
  @DELETE("menu-options/{id}")
  Single<ResponseBody> delete(@Path(value = "id") Long id);
}
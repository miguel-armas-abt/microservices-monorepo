package com.demo.poc.commons.core.errors.dto;

import com.demo.poc.commons.core.properties.ConfigurationBaseProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO implements Serializable  {

    public static final String CODE_DEFAULT = "Default";
    public static final String CODE_EMPTY = "Empty";

    @JsonProperty("type")
    private ErrorType type;

    private String code;

    private String message;

    public static ErrorDTO getDefaultError(ConfigurationBaseProperties properties) {
        return ErrorDTO
            .builder()
            .code(CODE_DEFAULT)
            .message(getMatchMessage(properties, CODE_DEFAULT))
            .type(ErrorType.SYSTEM)
            .build();
    }

    public static String getMatchMessage(ConfigurationBaseProperties properties, String errorCode) {
        return properties
            .errorMessages()
            .get(errorCode);
    }
}
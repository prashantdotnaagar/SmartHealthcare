package com.SmartHealthcare.exception;

import com.SmartHealthcare.constants.ServiceCodes;
import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {
  private final String code;
  private final String message;

  public ServiceException(ServiceCodes serviceCode) {
    super(serviceCode.getMessage());
    this.code = serviceCode.getCode();
    this.message = serviceCode.getMessage();
  }

  public ServiceException(ServiceCodes serviceCode, String customMessage) {
    super(customMessage);
    this.code = serviceCode.getCode();
    this.message = customMessage;
  }
}
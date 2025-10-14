package com.SmartHealthcare.exception;

import com.SmartHealthcare.constants.ServiceCodes;

public class BusinessLogicException extends ServiceException {
    public BusinessLogicException(String message) {
        super(ServiceCodes.INTERNAL_ERROR, message);
    }

    public BusinessLogicException(ServiceCodes serviceCode) {
        super(serviceCode);
    }
}
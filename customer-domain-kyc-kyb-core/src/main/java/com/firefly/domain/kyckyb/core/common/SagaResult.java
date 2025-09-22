package com.firefly.domain.kyckyb.core.common;

public class SagaResult {
    private final boolean success;
    private final String message;
    
    public SagaResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public static SagaResult success() {
        return new SagaResult(true, "Operation completed successfully");
    }
    
    public static SagaResult success(String message) {
        return new SagaResult(true, message);
    }
    
    public static SagaResult failure(String message) {
        return new SagaResult(false, message);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
}
package com.faisal.dto.v1;

public class CustomResponse<T> {
    private String code;
    private boolean success;
    private String message;
    private T data;

    public CustomResponse() {}

    public CustomResponse(String code, boolean success, String message, T data) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // Getters and setters

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}

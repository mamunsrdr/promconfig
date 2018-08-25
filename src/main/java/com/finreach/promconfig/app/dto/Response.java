package com.finreach.promconfig.app.dto;

public class Response {
    private boolean success;
    private String message;

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public static Response getSuccessResponse() {
        return new Response(true, null);
    }

    public static Response getErrorResponse() {
        return new Response(false, null);
    }

    public static Response getSuccessResponse(String message) {
        return new Response(true, message);
    }

    public static Response getErrorResponse(String message) {
        return new Response(false, message);
    }
}

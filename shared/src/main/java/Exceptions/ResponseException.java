package Exceptions;

import com.google.gson.Gson;
import results.ExceptionMessage;

import java.util.HashMap;
import java.util.Map;

public class ResponseException extends Exception {

    public enum Code {
        ServerError,
        ClientError,
    }

    final private Code code;

    public ResponseException(Code code, String message) {
        super(message);
        this.code = code;
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage(), "status", code));
    }

    public static ResponseException fromJson(String json, Integer statusCode) {
        var httpStatus = fromHttpStatusCode(statusCode);
        var errorMessage = new Gson().fromJson(json, ExceptionMessage.class);
        return new ResponseException(httpStatus, errorMessage.message());
    }

    public Code code() {
        return code;
    }



    //Make cases for all different exceptions
    public static Code fromHttpStatusCode(int httpStatusCode) {
        return switch (httpStatusCode) {
            case 500 -> Code.ServerError;
            case 400, 403, 401 -> Code.ClientError;

            default -> throw new IllegalArgumentException("Unknown HTTP status code: " + httpStatusCode);
        };
    }

    public int toHttpStatusCode() {
        return switch (code) {
            case ServerError -> 500;
            case ClientError -> 400;
        };
    }
}

package com.example.blog.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiError {

    CATEGORY_ALREADY_EXISTS("/problems/category-already-exists",
            HttpStatus.CONFLICT,
            "Category already exists",
            "The category you are trying to create already exists. Please use a different category name.",
            "CATEGORY_ALREADY_EXISTS"),

    INTERNAL_SERVER_ERROR("/problems/internal-server-error",
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Internal Server Error",
            "Something went wrong on the server. Please try again later or contact support if the problem persists.",
            "INTERNAL_SERVER_ERROR"),

    VALIDATION_FAILED("/problems/validation-failed",
            HttpStatus.BAD_GATEWAY,
            "Validation failed for inputs",
            "Please, check your input and try again",
            "VALIDATION_FAILED"),
    ;

    private final String type;       // URI path for documentation
    private final HttpStatus httpStatus;
    private final String title;      // short error title
    private final String detail;     // user-friendly explanation + fix guidance
    private final String errorCode;  // internal enum code

    ApiError(String type, HttpStatus httpStatus, String title, String detail, String errorCode) {
        this.type = type;
        this.httpStatus = httpStatus;
        this.title = title;
        this.detail = detail;
        this.errorCode = errorCode;
    }

}

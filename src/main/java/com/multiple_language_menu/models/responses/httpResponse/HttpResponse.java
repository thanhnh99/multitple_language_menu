package com.multiple_language_menu.models.responses.httpResponse;

import java.util.Collection;

public abstract class HttpResponse<T> {
    private String statusCode;
    private String message;
    private Collection<T> data;
}

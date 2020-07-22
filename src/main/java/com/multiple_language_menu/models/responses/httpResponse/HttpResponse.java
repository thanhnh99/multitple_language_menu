package com.multiple_language_menu.models.responses.httpResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpResponse<T>{
    private String statusCode;
    private String message;
    private Collection<T> data = new ArrayList<T>();
    public void addData( T data )
    {
        this.data.add(data);
    }
}

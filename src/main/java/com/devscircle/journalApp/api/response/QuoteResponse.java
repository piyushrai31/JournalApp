package com.devscircle.journalApp.api.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuoteResponse {
    private Response response;

    @Getter
    @Setter
    public static class Response{
        private String quote;
        private String author;
        private String work;
        private List<String> categories;
    }
}

package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        CloseableHttpResponse answer = http();
        List<Facts> result = jsonToFacts(answer);
        result.stream().filter(value -> value.getUpvotes() == 0)
                .forEach(System.out::println);
    }

    public static CloseableHttpResponse http() {
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectTimeout(5000)
                            .setSocketTimeout(30000)
                            .setRedirectsEnabled(false)
                            .build())
                    .build();
            HttpGet request = new HttpGet("https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats");
            response = httpClient.execute(request);
            return response;
        } catch (IOException ex) {
            ex.getMessage();
        }
        return response;
    }

    public static List<Facts> jsonToFacts(CloseableHttpResponse answer){
        ObjectMapper mapper = new ObjectMapper();
        List<Facts> result = new ArrayList<>();
        try{
            result = mapper.readValue(new String(answer.getEntity().getContent().readAllBytes()),
                    new TypeReference<>() {});
        } catch (IOException ex){
            ex.getMessage();
        }
        return result;
    }
}
package com.hack;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FoolMeService {

    public String requestAnswerFor(String question) throws IOException {
        StringBuffer answer = new StringBuffer();
        String baseUrl = "http://10.185.29.100:8080/%s";
        if (question.toLowerCase().contains("name")) {
            baseUrl = String.format(baseUrl, "name");
        } else if (question.toLowerCase().contains("evise")) {
            baseUrl = String.format(baseUrl, "Evise");
        } else if (question.toLowerCase().contains("weather")) {
            baseUrl = String.format(baseUrl, "weather");
        } else {
            return "Do you speak English?? ask me something else";
        }
        System.out.println("calling url " + baseUrl);
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(baseUrl);
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader
                (new InputStreamReader(
                        response.getEntity().getContent()));

        String line;
        while ((line = rd.readLine()) != null) {
            answer.append(line);
        }
        return answer.toString();
    }
}

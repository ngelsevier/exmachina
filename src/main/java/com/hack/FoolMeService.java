package com.hack;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static java.lang.Math.round;

@Service
public class FoolMeService {

    private final Random random = new Random();

    private static int count = 0;

    @Value("${fake.ip}")
    private String fakeUserIp;

    @Value("${real.ip}")
    private String realUserIp;

    @Value("${response.delay.millisec}")
    private int responseDelay;

    List<String> userIps = new ArrayList<String>();

    public String requestResponseFor(String question) {
        if (userIps.size() == 0) {
            userIps.add(fakeUserIp);
            userIps.add(realUserIp);
        }

        int index = count % 2;
        count++;
        String ipAddress = userIps.get(index);
        String baseUrl = String.format("http://%s:8080/%s", ipAddress, "%s");
        String baseUrlWithContext;
        String answer;

        baseUrlWithContext = buildUrlFrom(baseUrl, question);

        if (baseUrlWithContext == null) {
            return "Thats a great question, but I don't have an answer to it!";
        }

        System.out.println("calling url " + baseUrlWithContext);
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(baseUrlWithContext);
        try {

            answer = getResponseFromUser(client, request);
            long expiryTime = System.currentTimeMillis() + responseDelay;

            while (StringUtils.isEmpty(answer) && System.currentTimeMillis() < expiryTime) {
                String responseUrl = String.format(baseUrl, "response/poll");
                Thread.sleep(500);
                answer = getResponseFromUser(client, new HttpGet(responseUrl));
            }

        } catch (Exception e) {
            e.printStackTrace();
            answer = "Whoops, something went wrong !!";
        }

        if (StringUtils.isEmpty(answer)) {
            answer = "User took too long to answer, sod it";
        }
        return answer;
    }

    private String getResponseFromUser(HttpClient client, HttpGet request) throws IOException {
        StringBuffer answer = new StringBuffer();
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

    private String buildUrlFrom(String baseUrl, String question) {
        String baseUrlWithContext = null;

        if (question.toLowerCase().contains("name")) {
            baseUrlWithContext = String.format(baseUrl, "name");
        } else if (question.toLowerCase().contains("evise")) {
            baseUrlWithContext = String.format(baseUrl, "Evise");
        } else if (question.toLowerCase().contains("weather")) {
            baseUrlWithContext = String.format(baseUrl, "weather");
        }

        return baseUrlWithContext;
    }
}

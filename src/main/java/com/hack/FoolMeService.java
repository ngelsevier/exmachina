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
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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

        baseUrlWithContext = buildUrlFrom(baseUrl, question.split(" "));

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
                Thread.sleep(1000);
                answer = getResponseFromUser(client, new HttpGet(responseUrl));
            }

            while (System.currentTimeMillis() < expiryTime) {
                Thread.sleep(1000);
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

    String getResponseFromUser(HttpClient client, HttpGet request) throws IOException {
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

    String buildUrlFrom(String baseUrl, String[] wordsToMatch) {
        String baseUrlWithContext = null;
        List<String> words = Arrays.asList(wordsToMatch).stream().map(String::toLowerCase).collect(Collectors.toList());

        if (words.contains("name")) {
            baseUrlWithContext = String.format(baseUrl, "name");
        } else if (words.contains("evise")) {
            baseUrlWithContext = String.format(baseUrl, "Evise");
        } else if (words.contains("weather")) {
            baseUrlWithContext = String.format(baseUrl, "weather");
        } else if (words.contains("who") && words.contains("annoying")) {
            baseUrlWithContext = String.format(baseUrl, "annoying_response");
        } else if (words.contains("what") && words.contains("drink")) {
            baseUrlWithContext = String.format(baseUrl, "favedrink_response");
        } else if (words.contains("where") && words.contains("live")) {
            baseUrlWithContext = String.format(baseUrl, "live_response");
        } else if (words.contains("who") && words.contains("you")) {
            baseUrlWithContext = String.format(baseUrl, "whoareyou_response");
        } else if (words.contains("hi")) {
            baseUrlWithContext = String.format(baseUrl, "hi_response");
        }

        return baseUrlWithContext;
    }

}

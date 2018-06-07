package com.hack;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@org.springframework.stereotype.Controller
@RequestMapping("/")
public class RequestController {

    TuringService turingService = new TuringService();

    final String homePageQuestion = "<html>" +
            "<form method=\"post\" action=\"/\">" +
            "  Question: <input type=\"text\" name=\"question\">" +
            "  <br/>" +
            "  <br/>" +
            "  Answer: <input type=\"text\" name=\"answer\" readonly>" +
            "  <br/>" +
            "  <br/>" +
            "  <input type=\"submit\" value=\"Submit\">" +
            "</form>" +
            "</html>";


    @GetMapping
    @ResponseBody
    public String startPage() {
        return homePageQuestion;
    }

    @PostMapping
    @ResponseBody
    String handleRequest(@RequestParam String question) {
        String homePageAnswer = "<html>" +
                "<form method=\"post\" action=\"/\">" +
                "  Question: <input type=\"text\" name=\"question\" value=\"%s\">" +
                "  <br/>" +
                "  <br/>" +
                "  Answer: <input type=\"text\" name=\"answer\" value=\"%s\"readonly>" +
                "  <br/>" +
                "  <br/>" +
                "  <input type=\"submit\" value=\"Submit\">" +
                "</form>" +
                "</html>";

        String answer = null;
        try {
            answer = turingService.requestAnswerFor(question);
            Thread.sleep(5000l);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format(homePageAnswer, question, answer);
    }

}

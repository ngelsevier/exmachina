package com.hack;

import org.springframework.web.bind.annotation.*;

@org.springframework.stereotype.Controller
@RequestMapping("/")
public class RequestController {

    FoolMeService turingService = new FoolMeService();

    final String homePageQuestion = "<html>" +
            "<form method=\"post\" action=\"/\">" +
            "  Question: <input type=\"text\" name=\"question\" size=\"80\">" +
            "  <br/>" +
            "  <br/>" +
            "  Answer: <input type=\"text\" name=\"answer\" size=\\\"100\\\" readonly>" +
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
                "  Question: <input type=\"text\" name=\"question\" value=\"%s\" size=\"80\">" +
                "  <br/>" +
                "  <br/>" +
                "  Answer: <input type=\"text\" name=\"answer\" value=\"%s\" size=\"100\" readonly>" +
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

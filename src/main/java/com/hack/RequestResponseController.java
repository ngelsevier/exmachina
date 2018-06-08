package com.hack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@org.springframework.stereotype.Controller
public class RequestResponseController {

    private final BlockingQueue<String> queuedResponses = new LinkedBlockingQueue<String>();

    private final String homePage = "<html>" +
            "<form method=\"post\" action=\"/\">" +
            "  Question: <input type=\"text\" name=\"question\" value=\"%s\" size=\"80\">" +
            "  <br/>" +
            "  <br/>" +
            "  Answer: <input type=\"text\" name=\"answer\" size=\"100\" %s>" +
            "  <br/>" +
            "  <br/>" +
            "  <input type=\"submit\" value=\"Submit\">" +
            "</form>" +
            "</html>";

    @Autowired
    private FoolMeService foolMeService;

    @Value("${answer.mode}")
    private boolean answerMode;



    @GetMapping("/")
    @ResponseBody
    public String startPage() {
        if (!answerMode) {
            return String.format(homePage, "", "readonly");
        } else {
            return String.format(homePage, "Awaiting question... ", "");
        }
    }

    @GetMapping("/{context}")
    @ResponseBody
    public String startPage(@PathVariable("context") String context) {
        if (!answerMode) {
            return String.format(homePage, "", "readonly");
        } else {
            return null;
        }
    }

    @PostMapping
    @ResponseBody
    String handleRequest(@RequestParam String question, @RequestParam String answer) {
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

        if (answerMode) {
            queuedResponses.add(answer);
            return String.format(homePage, "Awaiting question... ", "");
        }

        String response = foolMeService.requestResponseFor(question);
        return String.format(homePageAnswer, question, response);
    }

    @GetMapping(path = "/response/poll")
    @ResponseBody
    public String requestResponse() {
        return queuedResponses.poll();
    }
}

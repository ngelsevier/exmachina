package com.hack;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class FoolMeServiceTest {

    FoolMeService foolMeService;

    @Before
    public void setup(){
        foolMeService = new FoolMeService(){
            @Override
            String getResponseFromUser(HttpClient client, HttpGet request) throws IOException {
                return "some answer";
            }
        };
    }


    @Test
    public void expectDefaultResultWhenNoMatchingTermsFoundInQuestion() {
        String answer = foolMeService.requestResponseFor("No matching nonsense");
        assertEquals("Thats a great question, but I don't have an answer to it!", answer);
    }

    @Test
    public void expectValidResponseWhenQuestionContainsKeyWordsNameInAnyCapitalization() {
        String answer = foolMeService.requestResponseFor("the NAme to FIND");
        assertEquals("some answer", answer);
    }

    @Test
    public void expectValidResponseWhenQuestionContainsKeyWordsWhoAndAnnoyingInAnyCapitalization() {
        String answer = foolMeService.requestResponseFor("the WHO to annOy is up to you");
        assertEquals("some answer", answer);
    }

    @Test
    public void expectDefaultResponseWhenQuestionIsNotWrittenInProperSentence() {
        String answer = foolMeService.requestResponseFor("theWHO to annOy is up to you");
        assertEquals("Thats a great question, but I don't have an answer to it!", answer);
    }

    @Test
    public void expectDefaultResponseWhenQuestionDoesNotContainAllTheKeyWordsInCombination() {
        String answer = foolMeService.requestResponseFor("LivE wherever you want");
        assertEquals("Thats a great question, but I don't have an answer to it!", answer);
    }
}
package com.hack;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FoolMeServiceTest {

    FoolMeService foolMeService;

    @Before
    public void setup(){
        foolMeService = new FoolMeService();
    }


    @Test
    public void expectDefaultResultWhenNoMatchingTermsFoundInQuestion() {
        String answer = foolMeService.requestResponseFor("No matching nonsense");
        assertEquals("Thats a great question, but I don't have an answer to it!", answer);
    }
}
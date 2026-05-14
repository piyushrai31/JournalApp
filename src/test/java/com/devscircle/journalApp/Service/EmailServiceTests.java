package com.devscircle.journalApp.Service;

import com.devscircle.journalApp.service.EmailService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {

    @Autowired
    private EmailService emailService;

    @Test
    @Disabled
    void testMail(){
        emailService.sendMail("piyushraivns31@gmail.com","test Mail","Hi, sending mail through JavaMailSender");
    }
}

package com.devscircle.journalApp.Service;

import com.devscircle.journalApp.scheduler.UserScheduler;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserSchedulerTests {

    @Autowired
    private UserScheduler userScheduler;

    @Test
    @Disabled
    public void fetchUsersAndSendSaMailTest(){
        userScheduler.fetchUsersAndSendSaMail();
    }
}

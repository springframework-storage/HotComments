package com.naver.hackday.launcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.naver.hackday.service.PCountService;

@Component
@EnableScheduling
public class UpdatePCountLauncher {

  @Autowired
  private PCountService pCountService;

  @Scheduled(cron = "*/1 * * * * *")
  private void launchUpdatePCount() {
    pCountService.updatePCount();
  }
}

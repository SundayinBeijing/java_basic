package com.sl.utils.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


//<!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
//        <dependency>
//        <groupId>org.projectlombok</groupId>
//        <artifactId>lombok</artifactId>
//        <version>1.18.10</version>
//        <scope>provided</scope>
//        <optional>true</optional><!--  只影响该项目 不传递-->
//        </dependency>
/**
 * Description(logback测试)
 * author: Gao xueyong
 * Create at: 2019/9/18 10:39 PM
 */

@Slf4j
@Component
public class LogDemoTest {

    @Scheduled(cron = "* * * * * ?")
    public void LogTest(){
        log.info("info log ...");
        log.error("error log ...");
        log.debug("debug log ...");
        log.warn("warn log ...");
    }
}

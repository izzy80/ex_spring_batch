package com.study.ex_spring_batch.controller;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


//api를 통해서 배치 실행
@Controller
@ResponseBody //페이지가 아닌 문자열이라
public class MainController {
    private final JobLauncher jobLauncher; //job을 실행시키기위한 시작 시간
    private final JobRegistry jobRegistry; //firstjob처럼 빈으로 등록한 특정 배치를 가지고 올 수 있는 것

    public MainController(JobLauncher jobLauncher, JobRegistry jobRegistry) {
        this.jobLauncher = jobLauncher;
        this.jobRegistry = jobRegistry;
    }

    @GetMapping("/first")
    public String firstApi(@RequestParam("value") String value) throws Exception {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", value)
                .toJobParameters();

        //jobRegistry.getJob("firstJob")를 가져와서 jobParameters처럼 특정 옵션을 주어서 작업을 동작하게 함
        jobLauncher.run(jobRegistry.getJob("firstJob"), jobParameters);


        return "ok";
    }

    //테이블에 데이터를 읽어 “win” 컬럼 값이 10이 넘으면 “reward” 컬럼에 true 값을 준다.
    @GetMapping("/second")
    public String secondApi(@RequestParam("value") String value) throws Exception {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", value)
                .toJobParameters();

        jobLauncher.run(jobRegistry.getJob("secondJob"), jobParameters);

        return "ok";
    }

}

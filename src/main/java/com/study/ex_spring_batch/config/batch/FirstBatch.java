package com.study.ex_spring_batch.config.batch;

import com.study.ex_spring_batch.entity.AfterEntity;
import com.study.ex_spring_batch.entity.BeforeEntity;
import com.study.ex_spring_batch.repository.AfterRepository;
import com.study.ex_spring_batch.repository.BeforeRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Configuration
public class FirstBatch {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final BeforeRepository beforeRepository;
    private final AfterRepository afterRepository;

    public FirstBatch(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, BeforeRepository beforeRepository, AfterRepository afterRepository) {

        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.beforeRepository = beforeRepository;
        this.afterRepository = afterRepository;
    }

    @Bean
    public Job firstJob() {

        System.out.println("first job");

        return new JobBuilder("firstJob", jobRepository) //해당 job에 대한 이름, 해당 작업에 대한 트래킹. 이거 메타데이터에 저장됨.
                .start(firstStep()) //스텝들어갈자리 : step이 1개 이상이면 .next().next()....로 쭉 쓸 수 있음. 즉 여러개가 들어갈 수 있음
                .build();
    }

    @Bean
    public Step firstStep() {

        System.out.println("first step");

        return new StepBuilder("firstStep", jobRepository)
                .<BeforeEntity, AfterEntity> chunk(10, platformTransactionManager)
                //10 => 데이터 10개씩 처리
                .reader(beforeReader()) //읽는메소드자리 : beforeEntity에서 읽어들이기
                .processor(middleProcessor()) //처리메소드자리 : 읽어들인 데이터 처리
                .writer(afterWriter()) //쓰기메소드자리 : afterEntity에 담음
                .build();
    }

    //Read : BeforeEntity 테이블에서 읽어오는 Reader
    //JPA 기반 -> RepositoryItemReader
    @Bean
    public RepositoryItemReader<BeforeEntity> beforeReader() {

        return new RepositoryItemReaderBuilder<BeforeEntity>()
                .name("beforeReader")
                .pageSize(10)
                //자원 낭비를 방지하기 위해 Sort를 진행하고 pageSize() 단위를 설정해 findAll이 아닌 페이지 만큼 읽어올 수 있도록 설정함.
                .methodName("findAll")
                .repository(beforeRepository)
                .sorts(Map.of("id", Sort.Direction.ASC))
                //id가 작은 순서대로 읽어옴
                .build();
    }

    //Process : 읽어온 데이터를 처리하는 Process (큰 작업을 수행하지 않을 경우 생략 가능, 지금과 같이 단순 이동은 사실 필요 없음. 예시를 위해 일단 넣어둔 것)
    @Bean
    public ItemProcessor<BeforeEntity, AfterEntity> middleProcessor() {

        return new ItemProcessor<BeforeEntity, AfterEntity>() {

            @Override
            public AfterEntity process(BeforeEntity item) throws Exception {

                //지금 데이터는 중복으로 저장되고 있음
                AfterEntity afterEntity = new AfterEntity();
                afterEntity.setUsername(item.getUsername());

                return afterEntity;
            }
        };
    }

    //Write : AfterEntity에 처리한 결과를 저장하는 Writer
    @Bean
    public RepositoryItemWriter<AfterEntity> afterWriter() {

        return new RepositoryItemWriterBuilder<AfterEntity>()
                .repository(afterRepository)
                .methodName("save")
                .build();
    }

}

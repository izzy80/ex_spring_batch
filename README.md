# 배치 vs 스케줄러
1. 배치
    - 대량의 데이터를 한 번에 처리하는 작업
    - 프레임워크인 Spring Batch를 통해 구현 가능
    - 트랜잭션 단위로 처리 가능
    - 실패 시 재시도 및 복구 가능
    - 메타 테이블이 존재
2. 스케줄러
    - 특정 시간 간격마다 실행되는 작업
    - Spring에서는 `@Scheduled`을 사용하여 **주기적으로 작업을 실행**
    - 대량 데이터 처리 불가능

=> 크게 데이터를 트래킹 해야 하는가? 대량의 데이터인가? 에 따라 나뉘는 듯

# 구현 방식
1. 배치를 어떻게 주기적으로 실행?
    - 스케줄(크론식) 기반 실행
    - 웹 핸들링 기반 실행
        - 특정 엔드포인트에 접근해서 실행시키는 방법

2. Spring Batch에서 **Job을 구성하는 Step을 구현하는 방식**
    - Chunk 방식 처리 (Read → Process → Write)
        - 트랜잭션은 Chunk단위로 처리
        - 예를 들어 Chunk의 단위가 10이면 read 10번 -> process 10번 -> write 한 번만 실행됨.
        - 그러니까 일단 1번씩 처리하고 메모리에 저장했다가 한꺼번에 write
    - Tasklet 방식 처리
        - 아주 간단한 동작만 함.

#  구현
1. application.properties
```
spring.application.name=ex_spring_batch  
#Spring Boot에서 애플리케이션 시작 시 자동으로 배치(Job)를 실행할지 여부를 결정  
#스케줄링을 해놓으면 이거 옵션 T/F 상관없이 배치는 실행이 된다.  
spring.batch.job.enabled=false  
  
#jpa의 ddl-auto와 비슷  
spring.batch.jdbc.initialize-schema=always  
spring.batch.jdbc.schema=classpath:org/springframework/batch/core/schema-mysql.sql  
  
#DB  
#DB서버 주소는 local에서 진행함
spring.datasource-meta.driver-class-name=com.mysql.cj.jdbc.Driver  
spring.datasource-meta.jdbc-url=jdbc:mysql://[local_ip]:3306/meta_db?useSSL=false&useUnicode=true&serverTimezone=Asia/Seoul&allowPublicKeyRetrieval=true  
spring.datasource-meta.username=root  
spring.datasource-meta.password=ssafy  
  
spring.datasource-data.driver-class-name=com.mysql.cj.jdbc.Driver  
spring.datasource-data.jdbc-url=jdbc:mysql://[local_ip]:3306/db?useSSL=false&useUnicode=true&serverTimezone=Asia/Seoul&allowPublicKeyRetrieval=true  
spring.datasource-data.username=root  
spring.datasource-data.password=ssafy
```
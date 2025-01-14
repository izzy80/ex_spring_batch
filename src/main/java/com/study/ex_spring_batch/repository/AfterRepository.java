package com.study.ex_spring_batch.repository;

import com.study.ex_spring_batch.entity.AfterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AfterRepository extends JpaRepository<AfterEntity, Long> {

}

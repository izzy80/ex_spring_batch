package com.study.ex_spring_batch.repository;

import com.study.ex_spring_batch.entity.WinEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WinRepository extends JpaRepository<WinEntity, Long> {
    Page<WinEntity> findByWinGreaterThanEqual(Long win, Pageable pageable);
}
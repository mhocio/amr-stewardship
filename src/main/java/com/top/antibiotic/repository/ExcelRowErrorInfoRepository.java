package com.top.antibiotic.repository;

import com.top.antibiotic.entities.ExcelRowErrorInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcelRowErrorInfoRepository extends JpaRepository<ExcelRowErrorInfo, Long>  {
}

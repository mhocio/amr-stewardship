package com.top.antibiotic.repository;

import com.top.antibiotic.entities.ImportDataInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportDataInfoRepository extends JpaRepository<ImportDataInfo, Long> {
}

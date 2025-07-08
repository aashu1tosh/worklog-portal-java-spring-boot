package com.backend.hrms.repository.company.worklog;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.hrms.entity.company.worklog.WorklogEntity;

@Repository
public interface WorklogRepository extends JpaRepository<WorklogEntity, UUID> {

  Page<WorklogEntity> findByCompanyEmployeeId(Pageable pageable, UUID employeeId);

  @Query("""
          SELECT w FROM WorklogEntity w
          WHERE w.companyEmployee.id = :employeeId
            AND w.companyEmployee.company.id = :companyId
      """)
  Page<WorklogEntity> getByEmployeeAndCompany(Pageable pageable, UUID employeeId, UUID companyId);

  @Query("""
          SELECT w FROM WorklogEntity w
          WHERE w.companyEmployee.id = :employeeId
            AND w.id = :id
      """)
  Optional<WorklogEntity> findById(UUID id, UUID employeeId);

  @Query("""
          SELECT COUNT(w) > 0 FROM WorklogEntity w
          WHERE w.companyEmployee.id = :employeeId
            AND w.createdAt = CURRENT_DATE
      """)
  boolean checksIfCanCreateNewWorklog(UUID employeeId);

}

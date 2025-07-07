package com.backend.hrms.entity.company.worklog;

import com.backend.hrms.entity.base.BaseEntity;
import com.backend.hrms.entity.company.CompanyEmployeeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorklogEntity extends BaseEntity {

    @Column(name = "task_completed")
    private String taskCompleted;

    @Column(name = "task_planned")
    private String taskPlanned;

    @Column(name = "challenging_task", nullable = true)
    private String challengingTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_employee_id", nullable = false)
    private CompanyEmployeeEntity companyEmployee;
}

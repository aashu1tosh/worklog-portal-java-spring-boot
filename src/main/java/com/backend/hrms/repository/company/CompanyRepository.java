package com.backend.hrms.repository.company;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.hrms.entity.company.CompanyEntity;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, UUID> {

    Optional<CompanyEntity> findByName(String name);

    Optional<CompanyEntity> findByEmail(String email);

    Optional<CompanyEntity> findByPhone(String phone);

    @Query(value = """
            SELECT *
            FROM   company_entity c
            WHERE  (:search = ''
                    OR :search IS NULL
                    OR c.company_name ILIKE '%' || :search || '%')
            """, countQuery = """
            SELECT COUNT(*)
            FROM   company_entity c
            WHERE  (:search = ''
                    OR :search IS NULL
                    OR c.company_name ILIKE '%' || :search || '%')
            """, nativeQuery = true)
    Page<CompanyEntity> findAll(Pageable pageable, @Param("search") String search);

}

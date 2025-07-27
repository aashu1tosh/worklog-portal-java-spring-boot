package com.backend.hrms.contracts.auth;

import java.util.UUID;

import com.backend.hrms.dto.auth.AdminDTO;
import com.backend.hrms.dto.auth.AuthDTO;
import com.backend.hrms.dto.company.CompanyAdminDTO;
import com.backend.hrms.dto.company.CompanyEmployeeDTO;
import com.backend.hrms.entity.AdminEntity;
import com.backend.hrms.entity.auth.AuthEntity;
import com.backend.hrms.entity.company.CompanyAdminEntity;
import com.backend.hrms.entity.company.CompanyEmployeeEntity;

public interface IAuthService {

    AuthEntity login(AuthDTO.LoginDTO data);

    AuthEntity me(UUID id);

    AuthEntity checkById(UUID id);

    AuthEntity findByEmail(String id);

    void registerAdmin(AdminDTO.RegisterDTO data, AdminEntity adminEntity);

    void registerCompanyAdmin(CompanyAdminDTO.RegisterDTO data, CompanyAdminEntity companyAdminEntity);

    void registerCompanyEmployee(CompanyEmployeeDTO.RegisterDTO data, CompanyEmployeeEntity companyEmployeeEntity);

    void updatePassword(AuthDTO.UpdatePasswordDTO data, UUID id);

    void restorePassword(AuthDTO.RestorePasswordDTO data, AuthEntity authEntity);

}

package com.backend.hrms.contracts.auth;

import com.backend.hrms.entity.auth.AuthEntity;
import com.backend.hrms.entity.auth.ResetPasswordEntity;

public interface IResetPasswordService {
    ResetPasswordEntity create(AuthEntity data);

}

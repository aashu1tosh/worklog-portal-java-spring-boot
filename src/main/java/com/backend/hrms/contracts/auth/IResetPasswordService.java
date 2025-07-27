package com.backend.hrms.contracts.auth;

import java.util.UUID;

import com.backend.hrms.entity.auth.AuthEntity;
import com.backend.hrms.entity.auth.ResetPasswordEntity;

public interface IResetPasswordService {
    ResetPasswordEntity create(AuthEntity data);

    ResetPasswordEntity findById(UUID id);

    void mailSend(UUID id);
}

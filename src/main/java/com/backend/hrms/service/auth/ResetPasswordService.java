package com.backend.hrms.service.auth;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.backend.hrms.contracts.auth.IResetPasswordService;
import com.backend.hrms.entity.auth.AuthEntity;
import com.backend.hrms.entity.auth.ResetPasswordEntity;
import com.backend.hrms.exception.HttpException;
import com.backend.hrms.repository.auth.ResetPasswordRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
class ResetPasswordService implements IResetPasswordService {

    private final ResetPasswordRepository resetPasswordRepository;

    public ResetPasswordEntity create(AuthEntity data) {
        resetPasswordRepository.existsByAuthIdAndExpiryDate(data.getId());
        // if (check)
        // throw HttpException.badRequest(
        // "A reset password request already exists for this user and has not expired
        // yet. If not received check in span or try after 1 hour.");
        ResetPasswordEntity resetPasswordEntity = new ResetPasswordEntity();
        resetPasswordEntity.setAuth(data);

        resetPasswordEntity.setExpiresAt(
                Instant.now().plusSeconds(3600)); // expiry time to 1 hour from now

        return resetPasswordRepository.save(resetPasswordEntity);
    }

    public ResetPasswordEntity findById(UUID id) {
        var check = resetPasswordRepository.findById(id)
                .orElseThrow(() -> HttpException.badRequest("Reset password request not found"));

        if (check.getExpiresAt().isBefore(Instant.now())) {
            throw HttpException.badRequest("Reset password request has expired");
        }

        if (check.isUsed()) {
            throw HttpException.badRequest("Reset password request has already been used");
        }

        check.setUsed(true);
        check.setUsedAt(Instant.now());
        resetPasswordRepository.save(check);
        return check;
    }

    public void mailSend(UUID id) {
        var check = resetPasswordRepository.findById(id)
                .orElseThrow(() -> HttpException.badRequest("Reset password request not found"));

        check.setSent(true);
        check.setSentAt(Instant.now());
        resetPasswordRepository.save(check);
    }

}

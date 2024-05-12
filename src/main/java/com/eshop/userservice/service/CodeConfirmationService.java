package com.eshop.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.eshop.userservice.model.CodeConfirmation;
import com.eshop.userservice.repository.CodeConfirmationRepository;

@Service
@RequiredArgsConstructor
public class CodeConfirmationService {
    private final CodeConfirmationRepository codeConfirmationRepository;

    public void saveCodeConfirmation(CodeConfirmation codeConfirmation){
        codeConfirmationRepository.save(codeConfirmation);
    }
}

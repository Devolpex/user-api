package com.eshop.userbackend.service;

import com.eshop.userbackend.model.CodeConfirmation;
import com.eshop.userbackend.repository.CodeConfirmationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CodeConfirmationService {
    private final CodeConfirmationRepository codeConfirmationRepository;

    public void saveCodeConfirmation(CodeConfirmation codeConfirmation){
        codeConfirmationRepository.save(codeConfirmation);
    }
}

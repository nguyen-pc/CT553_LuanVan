package com.vn.beta_testing.feature.chatbot.DTO;

import java.util.List;

public record AskRequest(
        String sessionId,
        String mode,
        String message,
        Long userId
        ) {
}


package com.vn.beta_testing.feature.chatbot.DTO;

public record AskResponse(
    String sessionUuid,
    String reply
) {}
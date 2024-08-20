package com.tinqinacademy.authentication.core.security;

import com.tinqinacademy.authentication.persistence.model.BlacklistedToken;
import com.tinqinacademy.authentication.persistence.repository.BlacklistedTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlacklistedTokenCleanupService {
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    //@Scheduled(cron = "*/5 * * * * *") // each 5 seconds
    @Scheduled(cron = "0 00 11 * * ?") // 11:00 AM every day
    public void cleanUpBlacklistedTokens() {
        log.warn("Starting cleanup of blacklisted tokens.");

        for (BlacklistedToken token : blacklistedTokenRepository.findAll()) {
            if (token.getCreatedAt().plusMinutes(5).isBefore(LocalDateTime.now())) {
                blacklistedTokenRepository.delete(token);
            }
        }

        log.warn("Completed cleanup of blacklisted tokens.");
    }
}


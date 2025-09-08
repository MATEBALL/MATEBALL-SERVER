package at.mateball.domain.webhook;

import at.mateball.domain.user.core.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class WebhookService {

    @Value("${discord.webhook.url}")
    private String discordWebhookUrl;

    public void sendDiscordNotification(String message) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("content", message);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(discordWebhookUrl, requestEntity, String.class);
            log.info("Discord Webhook 전송 성공: {}", message);
        } catch (Exception e) {
            log.error("Discord Webhook 전송 실패", e);
        }
    }
}


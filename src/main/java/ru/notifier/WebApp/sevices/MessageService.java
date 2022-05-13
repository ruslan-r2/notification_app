package ru.notifier.WebApp.sevices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.notifier.WebApp.domain.Message;

@Service
public class MessageService {

    @Autowired
    @Qualifier("messageRestTemplate")
    private RestTemplate restTemplate;

    public HttpStatus sendMessage(Message message) {
        HttpEntity<Message> requestBody = new HttpEntity<>(message);
        ResponseEntity<String> responseAddMessage = restTemplate.exchange("https://probe.fbrq.cloud/v1/send/" +
                message.getId(), HttpMethod.POST, requestBody, String.class);
        return responseAddMessage.getStatusCode();

    }

}
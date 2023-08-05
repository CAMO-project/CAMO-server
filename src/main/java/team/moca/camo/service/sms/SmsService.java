package team.moca.camo.service.sms;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsService {

    private final SmsApiProperties smsApiProperties;
    private final DefaultMessageService defaultMessageService;

    public SmsService(SmsApiProperties smsApiProperties) {
        this.smsApiProperties = smsApiProperties;
        this.defaultMessageService = NurigoApp.INSTANCE
                .initialize(smsApiProperties.getApiKey(), smsApiProperties.getApiSecretKey(), smsApiProperties.getUrl());
    }

    @Async
    public void sendSimpleMessage(String incomingNumber, StringBuilder contents) {
        Message message = new Message();
        message.setFrom(smsApiProperties.getOutgoingNumber());
        message.setTo(incomingNumber);
        message.setText(contents.toString());

        SingleMessageSentResponse sentSingleMessage =
                defaultMessageService.sendOne(new SingleMessageSendingRequest(message));
        log.info("Send message = {}", sentSingleMessage);
    }
}

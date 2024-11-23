package com.maan.veh.claim.notification;

import java.util.Arrays;
import java.util.function.Consumer;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class MailJob implements Consumer<Mail> {

	private String kafkaLink;

	public MailJob(String kafkaLink) {
		super();
		this.kafkaLink = kafkaLink;
	}

	public void pushMail(Mail m) {

		try {

			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Basic dmlzaW9uOnZpc2lvbkAxMjMj");
			HttpEntity<Object> entityReq = new HttpEntity<>(m, headers);
			System.out.println(entityReq.getBody());
			ResponseEntity<Object> response = restTemplate.postForEntity(kafkaLink, entityReq, Object.class);
			System.out.println(response.getBody());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void accept(Mail t) {
		pushMail(t);
	}

}

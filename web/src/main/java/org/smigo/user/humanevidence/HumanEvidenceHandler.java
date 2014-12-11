package org.smigo.user.humanevidence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Component
public class HumanEvidenceHandler {

    @Autowired
    private RestTemplate restTemplate;

    public boolean verifyCaptchaChallenge(String reCaptchaChallange) {
        final String url = "https://www.google.com/recaptcha/api/siteverify?secret=6LeO6_4SAAAAAIFH-JaSHoekHiENVkHGfZv4uxvQ&response=" + reCaptchaChallange;//&remoteip=user_ip_address";
        final ResponseEntity<ReCaptchaResponse> response = restTemplate.getForEntity(url, ReCaptchaResponse.class);
        final ReCaptchaResponse body = response.getBody();
        return body.isSuccess();
    }

    public boolean isVerifiedHuman(HttpServletRequest request) {
        return (System.currentTimeMillis() - request.getSession().getCreationTime()) > 5000;
    }

}

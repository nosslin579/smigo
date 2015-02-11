package org.smigo.user.humanevidence;

/*
 * #%L
 * Smigo
 * %%
 * Copyright (C) 2015 Christian Nilsson
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Component
public class HumanEvidenceHandler {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${sessionAgeLimitToSkipCaptcha}")
    private int sessionAgeLimitToSkipCaptcha;
    @Value("${googleRecaptchaSecretKey}")
    private String googleRecaptchaSecretKey;

    public boolean verifyCaptchaChallenge(String reCaptchaChallange) {
        final String url = "https://www.google.com/recaptcha/api/siteverify?secret=" + googleRecaptchaSecretKey +
                "&response=" + reCaptchaChallange;//&remoteip=user_ip_address";
        final ResponseEntity<ReCaptchaResponse> response = restTemplate.getForEntity(url, ReCaptchaResponse.class);
        final ReCaptchaResponse body = response.getBody();
        return body.isSuccess();
    }

    public boolean isVerifiedHuman(HttpServletRequest request) {
        final long sessionAge = System.currentTimeMillis() - request.getSession().getCreationTime();
        return sessionAge > sessionAgeLimitToSkipCaptcha;
    }

}

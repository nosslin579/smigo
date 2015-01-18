package org.smigo.user;

import javax.validation.constraints.AssertTrue;

public class AcceptTermsOfService {

    @AssertTrue
    private boolean termsOfService;

    public boolean isTermsOfService() {
        return termsOfService;
    }

    public void setTermsOfService(boolean termsOfService) {
        this.termsOfService = termsOfService;
    }
}
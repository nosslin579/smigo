package org.smigo.formbean;

import org.hibernate.validator.constraints.Email;

public class ResetFormBean {

    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

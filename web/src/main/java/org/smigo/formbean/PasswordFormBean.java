package org.smigo.formbean;

import org.smigo.constraints.CurrentPassword;
import org.smigo.constraints.NewPassword;

public class PasswordFormBean {
    @CurrentPassword
    private String oldPassword;

    @NewPassword
    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

}

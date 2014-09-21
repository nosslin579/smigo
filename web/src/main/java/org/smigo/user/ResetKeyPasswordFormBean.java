package org.smigo.user;

import javax.validation.constraints.NotNull;

class ResetKeyPasswordFormBean {
    @NotNull
    private String resetKey;
    @NewPassword
    private String password;

    public ResetKeyPasswordFormBean() {
    }

    public ResetKeyPasswordFormBean(String resetKey) {
        this.resetKey = resetKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ResetPasswordFormBean{" +
                ", resetKey='" + resetKey + '\'' +
                '}';
    }
}

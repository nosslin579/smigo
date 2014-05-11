package org.smigo.formbean;

import org.smigo.constraints.Matches;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Matches(field = "newPassword", verifyField = "newPasswordAgain")
public class PasswordFormBean {
  private String oldPassword;
  private String newPassword;
  private String newPasswordAgain;

  public String getOldPassword() {
    return oldPassword;
  }

  public void setOldPassword(String oldPassword) {
    this.oldPassword = oldPassword;
  }

  @NotNull
  @Size(min = 1, max = 56)
  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  public String getNewPasswordAgain() {
    return newPasswordAgain;
  }

  public void setNewPasswordAgain(String newPasswordAgain) {
    this.newPasswordAgain = newPasswordAgain;
  }
}

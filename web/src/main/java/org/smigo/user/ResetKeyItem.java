package org.smigo.user;

import java.util.concurrent.TimeUnit;

class ResetKeyItem {
    private final String id;
    private final String email;
    private final long createDate = System.currentTimeMillis();
    private boolean pristine = true;

    public ResetKeyItem(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public boolean isValid() {
        return pristine && ((createDate + TimeUnit.MINUTES.toMillis(15)) > System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "ResetPasswordKey{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", createDate=" + createDate +
                ", pristine=" + pristine +
                '}';
    }

    public void invalidate() {
        this.pristine = false;
    }
}

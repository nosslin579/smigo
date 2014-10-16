package org.smigo.user;

public class PublicInfoUserBean {
    private String displayName;

    private String username;

    private String about;

    public PublicInfoUserBean(UserBean userBean) {
        this.displayName = userBean.getDisplayName();
        this.username = userBean.getUsername();
        this.about = userBean.getAbout();
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUsername() {
        return username;
    }

    public String getAbout() {
        return about;
    }
}

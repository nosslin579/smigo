package org.smigo.user;

public class Guest implements User {
    private final int userId;

    public Guest(int userId) {
        this.userId = userId;
    }

    @Override
    public int getId() {
        return userId;
    }
}

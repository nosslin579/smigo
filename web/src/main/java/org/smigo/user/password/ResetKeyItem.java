package org.smigo.user.password;

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

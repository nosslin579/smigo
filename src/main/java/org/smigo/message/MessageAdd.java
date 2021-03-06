package org.smigo.message;

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

public class MessageAdd {

    private String locale;
    private String text;
    private int submitterUserId;

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSubmitterUserId() {
        return submitterUserId;
    }

    public void setSubmitterUserId(int submitterUserId) {
        this.submitterUserId = submitterUserId;
    }

    @Override
    public String toString() {
        return "MessageAdd{" +
                "locale='" + locale + '\'' +
                ", text='" + text + '\'' +
                ", submitterUserId=" + submitterUserId +
                '}';
    }
}

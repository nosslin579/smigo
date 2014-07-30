/**
 * Kitchen garden aid is a planning tool for kitchengardeners.
 * Copyright (C) 2010 Christian Nilsson
 *
 * This file is part of Kitchen garden aid.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 * Email contact christian1195@gmail.com
 */

package kga;

/**
 * If you break a warning type rule or follow a benefit type rule you will get a
 * hint. The hint tells the user if she is doing right or wrong.
 *
 * @author Christian Nilsson
 */
public class Hint {
    private Plant affectedPlant;
    private Plant causingPlant;
    private String messageKey;
    private String[] messageKeyArguments;

    public Hint(Plant affectedPlant, Plant causingPlant, String messageKey, String[] messageKeyArguments) {
        this.affectedPlant = affectedPlant;
        this.causingPlant = causingPlant;
        this.messageKey = messageKey;
        this.messageKeyArguments = messageKeyArguments;
    }

    public Plant getAffectedPlant() {
        return affectedPlant;
    }

    public Plant getCausingPlant() {
        return causingPlant;
    }

    @Override
    public String toString() {
        return "[hint at " + causingPlant + "]";
    }

    public String getMessageKey() {
        return messageKey;
    }

    public String[] getMessageKeyArguments() {
        return messageKeyArguments;
    }

    public static Hint createHint(Plant affected, Plant causing, String hintMessageKey, String[] messageKeyArguments) {
        return new Hint(affected, causing, hintMessageKey, messageKeyArguments);
    }
}

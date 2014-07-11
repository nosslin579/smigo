package org.smigo.species;


import kga.rules.RuleType;

import java.beans.PropertyEditorSupport;

public class RuleTypePropertyEditor extends PropertyEditorSupport {

    public RuleTypePropertyEditor() {
    }

    @Override
    public String getAsText() {
        Object value = getValue();
        if (value instanceof RuleType) {
            return String.valueOf(((RuleType) value).getId());
        } else {
            return super.getAsText();
        }
    }

    @Override
    public void setAsText(final String text) throws IllegalArgumentException {
        setValue(text.equals("-1") ? null : RuleType.valueOfId(text));
    }
}
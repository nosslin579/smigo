package kga.rules;

import kga.Hint;
import kga.Square;

public interface IRule {
    /**
     * Return the hint for rule if the warning rule type is violated or the
     * advice rule type is followed. Else it will return null.
     *
     * @param s the square that the species is located at
     * @return a hint or null
     */
    Hint getHint(Square s);

    int getId();

    RuleType getRuleType();

    String getMessageKey();
}

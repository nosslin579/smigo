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

package org.sourceforge.kga;

import org.sourceforge.kga.rules.Rule;

/**
 * If you break a warning type rule or follow a benefit type rule you will get a
 * hint. The hint tells the user if she is doing right or wrong.
 *
 * @author Christian Nilsson
 */
public class Hint {
  /**
   * The square which tells where the cause is.
   */
  private Square source;
  /**
   * The rule that this hint is based upon.
   */
  private Rule rule;
  /**
   * The square that gets affected.
   */
  private Square target;
  /**
   * The hint in words.
   */
  private String text;

  /**
   * The species that is the reason this hint exists.
   */
  private Species causer;

  /**
   * Creates a new hint with a single source.
   *
   * @param rule   he rule that this hint is based upon.
   * @param source the square where the cause is
   * @param target the square that is affected
   */
  public Hint(Rule rule, Square source, Square target, String text, Species causer) {
    this.rule = rule;
    this.source = source;
    this.target = target;
    this.text = text;
    this.causer = causer;
  }

  /**
   * Returns the species that this hint is applies to
   *
   * @return the source
   */
  public Square getSource() {
    return source;
  }

  /**
   * Returns the square that is affected.
   *
   * @return the target
   */
  public Square getTarget() {
    return target;
  }

  /**
   * Returns the rule.
   *
   * @return the rule
   */
  public Rule getRule() {
    return rule;
  }

  @Override
  public String toString() {
    return "[hint at " + target + rule + "]";
  }

  /**
   * Returns the hint in a word that can be translated.
   */
  public String getText() {
    return text;
  }

  /**
   * Returns the species that is the reason this hint exists.
   */
  public Species getCauser() {
    return causer;
  }

}

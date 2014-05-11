package org.smigo.formbean;

import org.smigo.constraints.ValidRule;
import org.sourceforge.kga.Family;
import org.sourceforge.kga.rules.RuleType;

@ValidRule
public class RuleFormModel {
  private int gap;
  private Family causerfamily;
  private RuleType type;
  private int host, causer;

  public int getGap() {
    return gap;
  }

  public void setGap(int gap) {
    this.gap = gap;
  }

  public Family getCauserfamily() {
    return causerfamily;
  }

  public void setCauserfamily(Family causerfamily) {
    this.causerfamily = causerfamily;
  }

  public RuleType getType() {
    return type;
  }

  public void setType(RuleType type) {
    this.type = type;
  }

  public int getHost() {
    return host;
  }

  public void setHost(int host) {
    this.host = host;
  }

  public int getCauser() {
    return causer;
  }

  public void setCauser(int causer) {
    this.causer = causer;
  }
}

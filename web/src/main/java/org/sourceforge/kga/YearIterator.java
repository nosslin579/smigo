package org.sourceforge.kga;

import java.util.Calendar;
import java.util.Iterator;
import java.util.SortedSet;

public class YearIterator implements Iterator<SquareIterator> {

  private Garden garden;
  private SortedSet<Integer> years;
  private Integer currentYear;

  public YearIterator(Garden garden) {
    this.garden = garden;
    years = garden.getYears();
    if (years.isEmpty())
      years.add(Calendar.getInstance().get(Calendar.YEAR));
    currentYear = years.first();
    new Boolean(true);
  }

  @Override
  public boolean hasNext() {
    return years.contains(currentYear);
  }

  @Override
  public SquareIterator next() {
    return new SquareIterator(garden, currentYear++);
  }

  @Override
  public void remove() {
  }

  public Integer getCurrentYear() {
    return currentYear;
  }
}

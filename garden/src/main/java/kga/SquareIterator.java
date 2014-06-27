package kga;

import java.util.Iterator;

public class SquareIterator implements Iterator<Square> {

  private Garden garden;
  private Integer year;
  private int x, y;
  private Bounds bounds;
  private boolean first;

  public SquareIterator(Garden garden, Integer year) {
    this.garden = garden;
    this.year = year;
    bounds = garden.getBoundsFor(year);
    x = bounds.getMinx();
    y = bounds.getMiny();
    first = true;
  }

  @Override
  public boolean hasNext() {
    if ((x + 1) > bounds.getMaxx()) {
      return bounds.isWithin(bounds.getMinx(), y + 1);
    } else {
      return bounds.isWithin(x + 1, y);
    }
  }

  @Override
  public Square next() {
    if (first) {
      first = false;
      return garden.addOrGetSquare(year, x, y);
    }
    if (++x > bounds.getMaxx()) {
      x = bounds.getMinx();
      y++;
    }
    return garden.addOrGetSquare(year, x, y);
  }

  @Override
  public void remove() {

  }

  public boolean isEndOfRow() {
    return x == bounds.getMaxx();
  }

  public boolean isStartOfRow() {
    return x == bounds.getMinx();
  }

  public Integer getYear() {
    return year;
  }
}

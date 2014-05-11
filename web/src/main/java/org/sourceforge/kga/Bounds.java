package org.sourceforge.kga;

/**
 * Bounds
 *
 * @author Christian Nilsson
 */
public class Bounds {
  private int minx, miny, maxx, maxy;

  public Bounds(int minx, int miny, int maxx, int maxy) {
    this.miny = miny;
    this.minx = minx;
    this.maxy = maxy;
    this.maxx = maxx;
  }

  public int getMaxx() {
    return maxx;
  }

  public int getMiny() {
    return miny;
  }

  public int getMinx() {
    return minx;
  }

  public int getMaxy() {
    return maxy;
  }

  public void setMiny(int miny) {
    this.miny = miny;
  }

  public void setMinx(int minx) {
    this.minx = minx;
  }

  public void setMaxy(int maxy) {
    this.maxy = maxy;
  }

  public void setMaxx(int maxx) {
    this.maxx = maxx;
  }

  public void increseBounds() {
    minx--;
    miny--;
    maxx++;
    maxy++;
  }

  public int getWidth() {
    return maxx - minx;
  }

  @Override
  public String toString() {
    return minx + ";" + maxx + ";" + miny + ";" + maxy + ";";
  }

  public boolean isWithin(int x, int y) {
    return x >= minx && x <= maxx && y >= miny && y <= maxy;

  }

  public boolean isOnTheEdge(int x, int y) {
    return x == minx || x == maxx || y == miny || y == maxy;
  }

  public boolean isRightBottomCorner(int x, int y) {
    return x == maxx && y == miny;
  }
}

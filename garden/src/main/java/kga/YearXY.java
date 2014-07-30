package kga;


/**
 * Represent a location and year.
 *
 * @author Christian Nilsson
 */
public class YearXY implements Location, Comparable<YearXY> {
    private int year, x, y;

    public YearXY(int year, int x, int y) {
        this.year = year;
        this.y = y;
        this.x = x;
    }

    @Override
    public int hashCode() {
        return (((year + 37) + 37) * x + 37) * y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof YearXY) {
            YearXY yearXY = (YearXY) obj;
            return yearXY.x == x && yearXY.y == y && yearXY.year == year;
        }
        return false;
    }

    @Override
    public int compareTo(YearXY o) {
        if (o.year != year)
            return year - o.year;
        if (o.y != y)
            return y - o.y;
        if (o.x != x)
            return x - o.x;
        return 0;
    }
}

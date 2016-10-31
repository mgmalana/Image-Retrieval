package model;

/**
 * Created by mgmalana on 30/10/2016.
 */
public class LUV {
    private double l;
    private double u;
    private double v;

    public LUV(double l, double u, double v) {
        this.l = l;
        this.u = u;
        this.v = v;
    }

    public double getL() {
        return l;
    }

    public double getU() {
        return u;
    }

    public double getV() {
        return v;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LUV luv = (LUV) o;

        if (Double.compare(luv.l, l) != 0) return false;
        if (Double.compare(luv.u, u) != 0) return false;
        return Double.compare(luv.v, v) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(l);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(u);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(v);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

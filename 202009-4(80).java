import java.math.*;
import java.io.*;
import java.text.*;
import java.util.*;

public class Main {
    static int dims, n;
    static int r;

    public static double getSqrt(double x) {
        if (x < 0) return 0;
        return Math.sqrt(x);
    }

    public static double getArccos(double x) {
        if (x < -1) x = -1;
        if (x > 1) x = 1;
        return Math.acos(x);
    }
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        dims = scan.nextInt();
        n = scan.nextInt();
        r = scan.nextInt();
        Point O = new Point(scan, dims);
        Point P[] = new Point[n];
        Point PO[] = new Point[n];
        double[] lpo = new double[n];
        double[] lpp2 = new double[n];
        double[] jopp = new double[n];
        for (int i = 0; i < n; ++ i) {
            P[i] = new Point(scan, dims);
            PO[i] = O.subtract(P[i]);
            lpo[i] = PO[i].len();
            lpp2[i] = getSqrt(lpo[i]*lpo[i] - r*r);
            jopp[i] = getArccos(lpp2[i] / lpo[i]);
        }
        double[] dist = new double[n];
        Arrays.fill(dist, 0.0);
        for (int i = 0; i < n; ++ i) {
            for (int j = i+1; j < n; ++ j) {
                Point AB = P[j].subtract(P[i]);
                double dAB = AB.len();
                if (lpp2[i]+lpp2[j]<dAB) {
                    double jA = jopp[i]-getArccos(AB.dot(PO[i]) / (dAB * lpo[i]));
                    double jB = jopp[j]-getArccos(-AB.dot(PO[j]) / (dAB * lpo[j]));
                    double jl = lpp2[i]+lpp2[j]+r*(jA+jB);
                    dist[i] += jl;
                    dist[j] += jl;
                } else {
                    dist[i] += dAB;
                    dist[j] += dAB;
                }
            }
        }
        for (int i = 0; i < n; ++ i) {
            System.out.println(String.format("%.12f", dist[i]));
        }
    }
}

class Point {
    int d;
    int[] a;

    public Point(int _d) {
        d = _d;
        a = new int[d];
    }

    public Point(Scanner scan, int _d) {
        d = _d;
        a = new int[d];
        for (int i = 0; i < d; ++ i) a[i] = scan.nextInt();
    }

    public Point subtract(Point x) {
        Point ret = new Point(d);
        for (int i = 0; i < d; ++ i) ret.a[i] = a[i]-x.a[i];
        return ret;
    }

    public double len() {
        int ret = 0;
        for (int x : a) ret += (x*x);
        return Math.sqrt((double)ret);
    }

    public int dot(Point x) {
        int ret = 0;
        for (int i = 0; i < d; ++ i) ret += (a[i]*x.a[i]);
        return ret;
    }
}
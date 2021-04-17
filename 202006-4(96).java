import java.util.*;
import java.math.*;
import java.io.*;
import java.text.*;

public class Main {

    private static Matrix ksm(Matrix A, int n) {
        if (n==0) return new Matrix(A.n, 1);
        if (n==1) return A;
        Matrix B = ksm(A, n>>1);
        if (n%2==0) return B.multiply(B);
        return B.multiply(B).multiply(A);
    }
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();
        String s = scan.next();
        int[][] d = new int[7][];
        d[1] = new int[]{2};
        d[2] = new int[]{4};
        d[4] = new int[]{1, 6};
        d[6] = new int[]{6, 4};
        Matrix A = new Matrix(67);
        int[] st = new int[67];
        st[1] = 1;
        int[] candidate = new int[]{1,2,4,6};
        for (int x : candidate) {
            for (int y : d[x]) {
                A.a[x][y] ++;
            }
            if (x==4) A.a[4][16] ++;
            if (x==6) A.a[6][64] ++;
        }
        for (int x1 : candidate) {
            for (int x2 : candidate) {
                int x = x1*10+x2;
                int z = d[x1][d[x1].length-1]*10+d[x2][0];
                A.a[x][z] ++;
            }
        }
        Matrix AF = ksm(A, n);
        int[] ed = AF.multiply(st);
        System.out.println(ed[Integer.valueOf(s)]);
        scan.close();
    }
}

class Matrix {
    int[][] a;
    int n;

    public Matrix(int dims) {
        n = dims;
        a = new int[n][n];
    }

    public Matrix(int dims, int x) {
        n = dims;
        a = new int[n][n];
        for (int i = 0; i < n; ++ i) a[i][i] = x;
    }

    public Matrix multiply(Matrix B) {
        Matrix C = new Matrix(B.n);
        for (int i = 0; i < n; ++ i) {
            for (int j = 0; j < n; ++ j) {
                for (int k = 0; k < n; ++ k) {
                    C.a[i][j] = Calc.jia(C.a[i][j], Calc.cheng(a[i][k], B.a[k][j]));
                }
            }
        }
        return C;
    }

    public int[] multiply(int[] vec) {
        int[] ret = new int[n];
        for (int i = 0; i < n; ++ i) {
            for (int j = 0; j < n; ++ j) {
                ret[i] = Calc.jia(ret[i], Calc.cheng(vec[j], a[j][i]));
            }
        }
        return ret;
    }
}

class Calc {
    static int MOD = 998244353;
    static int jia(int x, int y) {
        return (x+y) % MOD;
    }
    static int cheng(int x, int y) {
        return (int)(((long)x * (long)y) % (long)MOD);
    }
}
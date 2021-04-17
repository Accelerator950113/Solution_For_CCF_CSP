import java.util.*;
import java.io.*;
import java.text.*;
import java.math.*;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();
        int m = scan.nextInt();
        int[][] rec = new int[m][];
        for (int i = 0; i < m; ++ i) {
            int typ = scan.nextInt();
            if (typ == 1) {
                rec[i] = new int[6];
                rec[i][0] = typ;
                for (int j = 1; j <= 5; ++ j) rec[i][j] = scan.nextInt();
            } else if (typ == 2) {
                rec[i] = new int[4];
                rec[i][0] = typ;
                for (int j = 1; j <= 3; ++ j) rec[i][j] = scan.nextInt();
            } else {
                rec[i] = new int[3];
                rec[i][0] = typ;
                for (int j = 1; j <= 2; ++ j) rec[i][j] = scan.nextInt();
            }
        }
        LSH.build(n, rec);
        XDTree T = new XDTree(1, LSH.N);
        for (int i = 0; i < m; ++ i) {
            int typ = rec[i][0];
            int L = LSH.Index.get(rec[i][1]);
            int R = LSH.Index.get(rec[i][2]);
            if (typ == 1) {
                int[] ad = new int[3];
                ad[0] = rec[i][3];
                ad[1] = rec[i][4];
                ad[2] = rec[i][5];
                T.addElement(L, R, ad);
            } else if (typ == 2) {
                int k = rec[i][3];
                T.multiplyElement(L, R, k);
            } else if (typ == 3) {
                T.transElement(L, R);
            } else {
                System.out.println(T.query(L, R));
            }
        }
        scan.close();
    }
}

class LSH {
    public static HashMap<Integer, Integer> Index;
    public static ArrayList<Integer> Left;
    public static ArrayList<Integer> Right;
    public static int N;

    public static void build(int n, int[][] rec) {
        TreeSet<Integer> S = new TreeSet<>();
        for (int[] x : rec) {
            for (int i = 1; i <= 2; ++ i) {
                for (int y = Math.max(1, x[i]-1); y <= Math.min(n, x[i]+1); ++ y) {
                    S.add(y);
                }
            }
        }
        ArrayList<Integer> temp = new ArrayList<>();
        if (!S.contains(1)) S.add(1);
        if (!S.contains(n)) S.add(n);
        for (int x : S) temp.add(x);
        temp.add(n+1);
        Left = new ArrayList<>();
        Right = new ArrayList<>();
        Left.add(-1);
        Right.add(-1);
        Index = new HashMap<>();
        N = 0;
        for (int i = 0; i < temp.size()-1; ++ i) {
            Left.add(temp.get(i));
            Right.add(temp.get(i+1)-1);
            N ++;
            Index.put(temp.get(i), N);
        }
    }
}

class XDTree {
    TreeNode root;
    static int MOD = 1000000007;

    public XDTree(int l, int r) {
        root = build(l, r);
    }

    private TreeNode build(int l, int r) {
        if (l > r) return null;
        if (l == r) return new TreeNode(l, r);
        int mid = (l+r) >> 1;
        TreeNode cnt = new TreeNode(l, r);
        cnt.left = build(l, mid);
        cnt.right = build(mid+1, r);
        return cnt;
    }

    private void update(TreeNode cnt) {
        if (cnt.l == cnt.r) return;
        for (int i = 0; i < 3; ++ i) {
            cnt.s[i] = (cnt.left.s[i] + cnt.right.s[i]) % MOD;
        }
    }

    private void passDown(TreeNode cnt) {
        if (cnt.l == cnt.r) return;
        if (cnt.k != 1) {
            multiply(cnt.left, cnt.left.l, cnt.left.r, cnt.k);
            multiply(cnt.right, cnt.right.l, cnt.right.r, cnt.k);
            cnt.k = 1;
        }
        if (cnt.ad[0]>0 || cnt.ad[1]>0 || cnt.ad[2]>0) {
            add(cnt.left, cnt.left.l, cnt.left.r, cnt.ad);
            add(cnt.right, cnt.right.l, cnt.right.r, cnt.ad);
            Arrays.fill(cnt.ad, 0);
        }
        if (cnt.rev != 0) {
            trans(cnt.left, cnt.left.l, cnt.left.r, cnt.rev);
            trans(cnt.right, cnt.right.l, cnt.right.r, cnt.rev);
            cnt.rev = 0;
        }
    }

    private void add(TreeNode cnt, int l, int r, int[] ad) {
        if (cnt.l > r || cnt.r < l) return;
        if (l <= cnt.l && cnt.r <= r) {
            for (int i = 0; i < 3; ++ i) {
                cnt.ad[(i + cnt.rev) % 3] = (cnt.ad[(i + cnt.rev) % 3] + ad[i]) % MOD;
                cnt.s[i] = (cnt.s[i] + cheng(ad[i], LSH.Right.get(cnt.r)-LSH.Left.get(cnt.l)+1)) % MOD;
            }
            return;
        }
        passDown(cnt);
        add(cnt.left, l, r, ad);
        add(cnt.right, l, r, ad);
        update(cnt);
    }

    public void addElement(int l, int r, int[] ad) {
        add(root, l, r, ad);
    }

    private int cheng(int x, int y) {
        long z = ((long)x * (long)y) % (long)MOD;
        return (int)z;
    }

    private void multiply(TreeNode cnt, int l, int r, int k) {
        if (cnt.l > r || cnt.r < l) return;
        if (l <= cnt.l && cnt.r <= r) {
            cnt.k = cheng(cnt.k, k);
            for (int i = 0; i < 3; ++ i) {
                cnt.ad[i] = cheng(cnt.ad[i], k);
                cnt.s[i] = cheng(cnt.s[i], k);
            }
            return;
        }
        passDown(cnt);
        multiply(cnt.left, l, r, k);
        multiply(cnt.right, l, r, k);
        update(cnt);
    }

    public void multiplyElement(int l, int r, int k) {
        multiply(root, l, r, k);
    }

    private void trans(TreeNode cnt, int l, int r, int t) {
        if (cnt.l > r || cnt.r < l) return;
        if (l <= cnt.l && cnt.r <= r) {
            cnt.rev = (cnt.rev + t) % 3;
            for (int i = 0; i < t; ++ i) {
                int key = cnt.s[0];
                cnt.s[0] = cnt.s[1];
                cnt.s[1] = cnt.s[2];
                cnt.s[2] = key;
            }
            return;
        }
        passDown(cnt);
        trans(cnt.left, l, r, t);
        trans(cnt.right, l, r, t);
        update(cnt);
    }

    public void transElement(int l, int r) {
        trans(root, l, r, 1);
    }

    private int[] getSum(TreeNode cnt, int l, int r) {
        if (cnt.l > r || cnt.r < l) return new int[]{0, 0, 0};
        if (l <= cnt.l && cnt.r <= r) return cnt.s;
        passDown(cnt);
        int[] ll = getSum(cnt.left, l, r);
        int[] rr = getSum(cnt.right, l, r);
        int[] ret = new int[3];
        for (int i = 0; i < 3; ++ i) {
            ret[i] = (ll[i] + rr[i]) % MOD;
        }
        return ret;
    }

    public int query(int l, int r) {
        int[] x = getSum(root, l, r);
        int ret = 0;
        for (int i = 0; i < 3; ++ i) {
            ret = (ret + cheng(x[i], x[i])) % MOD;
        }
        return ret;
    }
}

class TreeNode {
    int l, r;
    int[] s;
    int[] ad;
    int k;
    int rev;
    TreeNode left, right;
    
    public TreeNode(int _l, int _r) {
        l = _l;
        r = _r;
        s = new int[3];
        ad = new int[3];
        k = 1;
        rev = 0;
        left = null;
        right = null;
    }
}
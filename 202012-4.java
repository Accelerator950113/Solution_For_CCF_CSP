import java.util.*;
import java.io.*;
import java.text.*;
import java.math.*;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();
        int m = scan.nextInt();
        int k = scan.nextInt();
        Graph G = new Graph(n, k);
        for (int i = 1; i <= n; ++ i) {
            for (int j = 0; j < k; ++ j) {
                int x = scan.nextInt();
                G.putTag(i, j, x);
            }
        }
        for (int i = 1; i < n; ++ i) {
            int u = scan.nextInt();
            int v = scan.nextInt();
            int w = scan.nextInt();
            G.addEdge(u, v, w);
        }
        G.solveAns();
        System.out.println(G.solveM(m));
        scan.close();
    }
}

class Graph {
    static int INF = Integer.MAX_VALUE >> 2;
    List<List<int[]>> g;
    int[] tag;
    int n, k;
    int[][] ans;

    public Graph(int _n, int _k) {
        k = _k;
        n = _n;
        g = new ArrayList<>();
        tag = new int[n+1];
        for (int i = 0; i <= n; ++ i) g.add(new ArrayList<>());
        ans = new int[n+1][k];
        for (int i = 0; i <= n; ++ i) Arrays.fill(ans[i], INF);
    }

    public void addEdge(int u, int v, int w) {
        g.get(u).add(new int[]{v, w});
        g.get(v).add(new int[]{u, w});
    }

    public void putTag(int u, int pos, int x) {
        tag[u] |= (x << pos);
    }

    private int getTag(int u, int num) {
        return (tag[u] >> num) & 1;
    }

    private int[] dfs(int u, int fa, int faw, int cntTag) {
        int maxProfit = 0;
        int utag = getTag(u, cntTag);
        int sumLen = 0;
        for (int[] edge : g.get(u)) {
            int v = edge[0], w = edge[1];
            if (v == fa) continue;
            int[] x = dfs(v, u, w, cntTag);
            utag |= x[0];
            sumLen += x[1];
            maxProfit = Math.max(maxProfit, x[1]-x[2]);
        }
        if (utag == 0) return new int[]{0, 0, 0};
        return new int[]{1, sumLen+2*faw, sumLen+faw-maxProfit};
    }

    public void solveAns() {
        for (int i = 1; i <= n; ++ i) {
            for (int j = 0; j < k; ++ j) {
                int[] temp = dfs(i, -1, 0, j);
                ans[i][j] = temp[2];
            }
        }
    }

    private int getAns(int u, int state) {
        int ret = 0;
        for (int i = 0; i < k; ++ i) {
            if (((state >> i) & 1) == 1) {
                ret = Math.max(ret, ans[u][i]);
            }
        }
        return ret;
    }

    public int solveM(int m) {
        int[][] dp = new int[1 << k][m+1];
        for (int state = 0; state < (1 << k); ++ state) {
            dp[state][1] = INF;
            for (int i = 1; i <= n; ++ i) {
                dp[state][1] = Math.min(dp[state][1], getAns(i, state));
            }
        }
        for (int i = 2; i <= m; ++ i) {
            for (int state = 0; state < (1 << k); ++ state) {
                dp[state][i] = INF;
                for (int sub = state&(state-1); sub != 0; sub = (sub-1)&state) {
                    dp[state][i] = Math.min(dp[state][i], Math.max(dp[sub][i-1], dp[state^sub][1]));
                }
            }
        }
        int ret = INF;
        for (int i = 1; i <= m; ++ i) {
            ret = Math.min(ret, dp[(1 << k)-1][i]);
        }
        return ret;
    }
}
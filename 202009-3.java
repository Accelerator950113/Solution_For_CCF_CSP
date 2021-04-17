import java.io.*;
import java.math.*;
import java.util.*;
import java.text.*;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int T = scan.nextInt();
        for (int i = 0; i < T; ++ i) {
            Logic C = new Logic(scan);
            C.readQuery(scan);
            C.solve();
        }
        scan.close();
    }
}

class Logic {
    int M, N;
    Node[] Component;
    int[] Input;
    ArrayList<ArrayList<Integer>> InputConnect;
    ArrayList<ArrayList<Integer>> ComponentConnect;
    int S;
    int[][] sIn;
    int[][] sQuery;

    public Logic(Scanner scan) {
        M = scan.nextInt();
        N = scan.nextInt();
        Input = new int[M+1];
        InputConnect = new ArrayList<>();
        for (int i = 0; i <= M; ++ i) InputConnect.add(new ArrayList<>());
        Component = new Node[N+1];
        ComponentConnect = new ArrayList<>();
        for (int i = 0; i <= N; ++ i) ComponentConnect.add(new ArrayList<>());
        for (int i = 1; i <= N; ++ i) {
            String temp = scan.next();
            int indeg = scan.nextInt();
            Component[i] = new Node(temp, indeg);
            for (int j = 0; j < indeg; ++ j) {
                String tp = scan.next();
                char c = tp.charAt(0);
                int u = Integer.valueOf(tp.substring(1, tp.length()));
                if (c == 'I') {
                    InputConnect.get(u).add(i);
                } else {
                    ComponentConnect.get(u).add(i);
                }
            }
        }
    }

    public void readQuery(Scanner scan) {
        S = scan.nextInt();
        sIn = new int[S][M+1];
        sQuery = new int[S][];
        for (int i = 0; i < S; ++ i) {
            for (int j = 1; j <= M; ++ j) {
                sIn[i][j] = scan.nextInt();
            }
        }
        for (int i = 0; i < S; ++ i) {
            int nn = scan.nextInt();
            sQuery[i] = new int[nn];
            for (int j = 0; j < nn; ++ j) {
                sQuery[i][j] = scan.nextInt();
            }
        }
    }

    private boolean hasLOOP() {
        for (int i = 1; i <= N; ++ i) Component[i].flush();
        Queue<Integer> Q = new LinkedList<>();
        for (int i = 1; i <= M; ++ i) {
            for (int u : InputConnect.get(i)) {
                Component[u].add(Input[i]);
                if (Component[u].isAct) Q.offer(u);
            }
        }
        while (!Q.isEmpty()) {
            int u = Q.poll();
            int uo = Component[u].output;
            for (int v : ComponentConnect.get(u)) {
                Component[v].add(uo);
                if (Component[v].isAct) Q.offer(v);
            }
        }
        for (int i = 1; i <= N; ++ i) {
            if (!Component[i].isAct) return true;
        }
        return false;
    }

    public void solve() {
        for (int i = 0; i < S; ++ i) {
            for (int j = 1; j <= M; ++ j) {
                Input[j] = sIn[i][j];
            }
            if (hasLOOP()) {
                System.out.println("LOOP");
                return;
            }
            int nn = sQuery[i].length;
            for (int j = 0; j < nn; ++ j) {
                int ans = Component[sQuery[i][j]].output;
                if (j == nn-1) {
                    System.out.println(ans);
                } else {
                    System.out.print(ans+" ");
                }
            }
        }
    }
}

class Node {
    static int NOT = 0;
    static int AND = 1;
    static int OR = 2;
    static int XOR = 3;
    static int NAND = 4;
    static int NOR = 5;

    int type;
    ArrayList<Integer> input;
    int output;
    int indeg;
    boolean isAct;

    private int getType(String s) {
        if (s.equals("NOT")) return 0;
        if (s.equals("AND")) return 1;
        if (s.equals("OR")) return 2;
        if (s.equals("XOR")) return 3;
        if (s.equals("NAND")) return 4;
        return 5;
    }

    public Node(String s, int _in) {
        type = getType(s);
        input = new ArrayList<>();
        output = 0;
        indeg = _in;
        isAct = false;
    }

    private void active() {
        isAct = true;
        if (type == NOT) {
            output = input.get(0) ^ 1;
            return;
        }
        if (type == AND) {
            output = 1;
            for (int x : input) {
                output &= x;
            }
            return;
        }
        if (type == OR) {
            output = 0;
            for (int x : input) {
                output |= x;
            }
            return;
        }
        if (type == XOR) {
            output = 0;
            for (int x : input) {
                output ^= x;
            }
            return;
        }
        if (type == NAND) {
            output = 1;
            for (int x : input) {
                output &= x;
            }
            output ^= 1;
            return;
        }
        if (type == NOR) {
            output = 0;
            for (int x : input) {
                output |= x;
            }
            output ^= 1;
            return;
        }
    }

    public void add(int x) {
        input.add(x);
        if (input.size() == indeg) active();
    }

    public void flush() {
        input.clear();
        isAct = false;
    }
}
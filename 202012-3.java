import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String buf = scan.nextLine();
        int m = Integer.valueOf(buf);
        FileSystem fsys = new FileSystem();
        HashMap<Boolean, String> map = new HashMap<>();
        map.put(true, "Y");
        map.put(false, "N");
        for (int i = 0; i < m; ++ i) {
            buf = scan.nextLine();
            String[] temp = buf.split(" ");
            if (temp[0].equals("C")) {
                System.out.println(map.get(fsys.createFile(temp[1], Long.valueOf(temp[2]))));
            } else if (temp[0].equals("R")) {
                System.out.println(map.get(fsys.removeFile(temp[1])));
            } else {
                long x = Long.valueOf(temp[2]);
                long y = Long.valueOf(temp[3]);
                System.out.println(map.get(fsys.setDir(temp[1], x, y)));
            }
        }
    }
}

class FileSystem {
    FileNode root;

    public FileSystem() {
        root = new FileNode(FileNode.DIR);
    }

    private long findFile(String[] path) {
        int n = path.length;
        FileNode cnt = root;
        for (int i = 1; i < n-1; ++ i) {
            if (cnt.H.containsKey(path[i])) {
                cnt = cnt.H.get(path[i]);
                if (cnt.type == FileNode.FILE) return -2;
            } else {
                return -1;
            }
        }
        if (cnt.H.containsKey(path[n-1])) {
            cnt = cnt.H.get(path[n-1]);
            if (cnt.type == FileNode.DIR) return -2;
            return cnt.uml;
        } else {
            return -1;
        }
    }

    private boolean checkPE(String[] path, long sz) {
        int n = path.length;
        FileNode cnt = root;
        for (int i = 1; i < n; ++ i) {
            if (cnt.hd-cnt.uhd < sz) return false;
            if (i == n-1) break;
            if (cnt.H.containsKey(path[i])) {
                cnt = cnt.H.get(path[i]);
            } else {
                return true;
            }
        }
        if (cnt.ml-cnt.uml < sz) return false;
        return true;
    }

    private void create(String[] path, long sz) {
        int n = path.length;
        FileNode cnt = root;
        for (int i = 1; i < n; ++ i) {
            cnt.uhd += sz;
            if (i == n-1) break;
            if (cnt.H.containsKey(path[i])) {
                cnt = cnt.H.get(path[i]);
            } else {
                FileNode temp = new FileNode(FileNode.DIR);
                cnt.H.put(path[i], temp);
                cnt = temp;
            }
        }
        cnt.uml += sz;
        if (cnt.H.containsKey(path[n-1])) {
            cnt = cnt.H.get(path[n-1]);
            cnt.uml += sz;
        } else {
            FileNode temp = new FileNode(FileNode.FILE);
            cnt.H.put(path[n-1], temp);
            temp.uml += sz;
        }
    }

    public boolean createFile(String x, long size) {
        String[] path = x.split("/");
        long old = findFile(path);
        if (old == -2) return false;
        if (old > 0) size -= old;
        if (checkPE(path, size)) {
            create(path, size);
            return true;
        }
        return false;
    }

    private void remove(String[] path) {
        int n = path.length;
        FileNode cnt = root;
        for (int i = 1; i < n; ++ i) {
            if (cnt.H.containsKey(path[i])) {
                cnt = cnt.H.get(path[i]);
            } else {
                return;
            }
        }
        FileNode tar = cnt;
        long dec = Math.max(cnt.uml, cnt.uhd);
        cnt = root;
        for (int i = 1; i < n; ++ i) {
            cnt.uhd -= dec;
            if (i == n-1) break;
            cnt = cnt.H.get(path[i]);
        }
        if (tar.type == FileNode.FILE) cnt.uml -= dec;
        cnt.H.remove(path[n-1]);
    }

    public boolean removeFile(String x) {
        remove(x.split("/"));
        return true;
    }

    private boolean setPE(String[] path, long x, long y) {
        int n = path.length;
        FileNode cnt = root;
        for (int i = 1; i < n; ++ i) {
            if (cnt.H.containsKey(path[i])) {
                cnt = cnt.H.get(path[i]);
            } else {
                return false;
            }
        }
        if (cnt.type == FileNode.FILE) return false;
        if (cnt.uml>x || cnt.uhd>y) return false;
        cnt.ml = x;
        cnt.hd = y;
        return true;
    }

    public boolean setDir(String x, long a, long b) {
        if (a == 0) a = FileNode.INF;
        if (b == 0) b = FileNode.INF;
        return setPE(x.split("/"), a, b);
    }
}

class FileNode {
    static boolean FILE = true;
    static boolean DIR = false;
    static long INF = Long.MAX_VALUE >> 2;

    long ml, hd;
    long uml, uhd;
    boolean type;
    HashMap<String, FileNode> H;

    public FileNode(boolean _type) {
        ml = INF;
        hd = INF;
        uml = 0;
        uhd = 0;
        type = _type;
        H = new HashMap<>();
    }
}
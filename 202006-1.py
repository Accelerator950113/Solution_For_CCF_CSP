[n, m] = [int(x) for x in input().split(' ')]
A = []
for i in range(n) :
    [xs, ys, c] = [x for x in input().split(' ')]
    x, y = int(xs), int(ys)
    tp = 0 if c=='A' else 1
    A.append([x, y, tp])
for _ in range(m) :
    [aa, bb, cc] = [int(x) for x in input().split(' ')]
    d = set()
    for [x, y, c] in A :
        cnt = 0 if aa+bb*x+cc*y>0 else 1
        d.add(cnt ^ c)
    print("Yes" if len(d)==1 else "No")
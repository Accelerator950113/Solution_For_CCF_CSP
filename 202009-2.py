[n, k, t, xl, yd, xr, yu] = [int(_) for _ in input().split(' ')]
jg, dl = 0, 0
for _ in range(n) :
    ts = 0
    flag1, flag2 = 0, 0
    A = [int(_) for _ in input().split(' ')]
    for i in range(t) :
        x, y = A[i*2], A[i*2+1]
        if xl<=x<=xr and yd<=y<=yu :
            flag1 = 1
            ts += 1
            if ts>=k : flag2 = 1
        else :
            ts = 0
    jg += flag1
    dl += flag2
print(jg)
print(dl)
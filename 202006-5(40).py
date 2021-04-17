[n, m] = [int(x) for x in input().split(' ')]
A = []
for i in range(n) :
    A.append([int(x) for x in input().split(' ')])
fc = [x[2:] for x in A]
B = []
for i in range(m) :
    B.append([int(x) for x in input().split(' ')])

def calc(x, a, b, c) :
    return a*x*x+b*x+c

best = -2147483644
for state in range(1<<n) :
    flag = True
    rg = [x[:2] for x in A]
    for [c, x, y] in B :
        if (state & (1<<(x-1)))==0 and (state & (1<<(y-1)))!=0 :
            if c==1 : 
                flag = False
                break
            else :
                rg[y-1][0], rg[y-1][1] = A[y-1][0]+1, A[y-1][1]-1
                if rg[y-1][0]>rg[y-1][1] :
                    flag = False
                    break
    if not flag : continue
    cnt = 0
    for i, [a, b, c] in enumerate(fc) :
        if (state & (1<<i))==0 : continue
        [l, r] = rg[i]
        cnt += max(calc(x, a, b, c) for x in range(l, r+1))
    best = max(best, cnt)
print(best)
n = int(input())
A = []
for i in range(n) :
    buf = input().split(' ')
    A.append((int(buf[0]), -int(buf[1])))
A.sort()
tot = sum(-x[1] for x in A)
ans, best, ts = 0, 0, 0
for i in range(n) :
    tmp = (i-ts) + (tot-ts)
    if tmp>=best :
        best = tmp
        ans = A[i][0]
    ts -= A[i][1]
print(ans)
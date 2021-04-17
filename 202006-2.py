[n, a, b] = [int(x) for x in input().split(' ')]
da = {}
db = {}
for _ in range(a) :
    [i, v] = [int(x) for x in input().split(' ')]
    da[i] = v
for _ in range(b) :
    [i, v] = [int(x) for x in input().split(' ')]
    db[i] = v
ret = 0
for x in da.keys() :
    if db.get(x)!=None : ret += (da[x] * db[x])
print(ret)
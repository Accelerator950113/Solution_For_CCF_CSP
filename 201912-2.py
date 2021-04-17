n = int(input())
s = set()
for i in range(n) :
    [x, y] = [int(x) for x in input().split(' ')]
    s.add((x, y))
d1 = [(0, 1), (0, -1), (1, 0), (-1, 0)]
d2 = [(1, 1), (-1, -1), (1, -1), (-1, 1)]
ret = [0] * 5
for x,y in s :
    flag = all((x+dx,y+dy) in s for dx,dy in d1)
    if flag :
        temp = sum(1 for dx,dy in d2 if (x+dx, y+dy) in s)
        ret[temp] += 1
for x in ret : print(x)
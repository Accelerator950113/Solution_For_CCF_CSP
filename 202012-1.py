n = int(input())
ans = 0
for _ in range(n) :
    [w, s] = [int(x) for x in input().split(' ')]
    ans += w*s
print(max(ans,0))
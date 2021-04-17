[n, X, Y] = [int(_) for _ in input().split(' ')]
A = []
for i in range(n) :
    [x, y] = [int(_) for _ in input().split(' ')]
    A.append(((x-X)**2+(y-Y)**2, i+1))
A.sort()
print(A[0][1])
print(A[1][1])
print(A[2][1])

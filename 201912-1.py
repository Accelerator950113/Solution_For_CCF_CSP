n = int(input())
A = [0] * 4
i = 1
while i <= n :
    x = str(i)
    if i%7==0 or x.find('7')!=-1 :
        A[i%4] += 1
        n += 1
    i += 1
print(A[1])
print(A[2])
print(A[3])
print(A[0])
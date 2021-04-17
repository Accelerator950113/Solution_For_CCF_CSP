def inputText() :
    text = []
    try :
        while True :
            buf = input()
            text.append(buf)
    except EOFError :
        pass
    return text

def isSpaceLine(s) :
    return all(x==' ' for x in s)

def isListHead(s) :
    return len(s)>=2 and s[:2]=='* '

def isListBody(s) :
    return len(s)>=2 and s[:2]=='  '

def stripSpace(s) :
    l, r = 0, len(s)-1
    while l<r and s[l]==' ' : l += 1
    while l<r and s[r]==' ' : r -= 1
    return s[l:r+1]

def calcPara(s, maxw) :
    cnt, x = 1, 0
    for i in range(len(s)) :
        if x==0 and s[i]==' ' : continue
        x += 1
        if x==maxw :
            cnt += 1
            x = 0
    if x==0 : cnt -= 1
    return cnt

w = int(input())
A = inputText()
i, n = 0, len(A)
ret = 0
while i<n :
    if isSpaceLine(A[i]) :
        i += 1
        continue
    else :
        if ret>0 : ret += 1
    if isListHead(A[i]) :
        while i<n and isListHead(A[i]) :
            j = i
            while j+1<n and isListBody(A[j+1]) and (not isSpaceLine(A[j+1])): j += 1
            B = ' '.join([stripSpace(x[2:]) for x in A[i:j+1]])
            ret += max(calcPara(B, w-3), 1)
            i = j+1
    else :
        k = i
        while k+1<n and (not (isSpaceLine(A[k+1]) or isListHead(A[k+1]))) : k += 1
        C = ' '.join([stripSpace(x) for x in A[i:k+1]])
        ret += calcPara(C, w)
        i = k+1
print(ret)
    

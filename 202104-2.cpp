#include<iostream>
#include<cstdio>
#include<cstring>
#include<cstdlib>

using namespace std;

int main() {
	int n, L, r, t;
	scanf("%d%d%d%d", &n, &L, &r, &t);
	int A[n+1][n+1];
	int S[n+1][n+1];
	memset(S, 0, sizeof(S));
	for (int i = 1; i <= n; ++ i) {
		for (int j = 1; j <= n; ++ j) {
			scanf("%d", &A[i][j]);
			S[i][j] = A[i][j] + S[i-1][j] + S[i][j-1] - S[i-1][j-1];
		}
	}
	int ans = 0;
	for (int i = 1; i <= n; ++ i) {
		for (int j = 1; j <= n; ++ j) {
			int up = max(i-r, 1);
			int down = min(i+r, n);
			int left = max(j-r, 1);
			int right = min(j+r, n);
			int area = (down-up+1)*(right-left+1);
			int sum = S[down][right] - S[down][left-1] - S[up-1][right] + S[up-1][left-1];
			if (area*t >= sum) ans ++;
		}
	}
	printf("%d\n", ans);
	return 0;
}
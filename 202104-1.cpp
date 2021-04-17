#include<iostream>
#include<cstdio>
#include<cstring>
#include<cstdlib>

using namespace std;

int main() {
	int n, m, L;
	cin >> n >> m >> L;
	int ret[L];
	memset(ret, 0, sizeof(ret));
	for (int i = 0; i < n; ++ i) {
		for (int j = 0; j < m; ++ j) {
			int x;
			cin >> x;
			ret[x] ++;
		}
	}
	printf("%d", ret[0]);
	for (int i = 1; i < L; ++ i) printf(" %d", ret[i]);
	printf("\n");
	return 0;
}
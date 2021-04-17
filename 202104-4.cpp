#include<iostream>
#include<cstdio>
#include<cstring>
#include<cstdlib>
#include<vector>
#include<set>
#define MOD 1000000007

using namespace std;

vector<int> ans[100001];
int n;
int N;
int F[1024][1024];
int G[1024];
int a[1024];

void init() {
	for (int i = 2; i <= N; ++ i) {
		ans[i].push_back(1);
		for (int j = 2; j*j <= i; ++ j) {
			if (i%j==0) {
				ans[i].push_back(j);
				if (j != i/j) ans[i].push_back(i/j);
			}
		}
	}
}

int cheng(int x, int y) {
	return (int)((long long)x * (long long)y % (long long)MOD);
}

int main() {
	cin >> n;
	for (int i = 0; i < n; ++ i) cin >> a[i];
	for (int i = 1; i < n; ++ i) a[i] -= a[0];
	a[0] = 0;
	N = a[n-1];
	init();
	for (int i = 0; i < n; ++ i) {
		set<int> temps;
		for (int j = i+1; j < n; ++ j) {
			int len = a[j]-a[i];
			for (int k = 0; k < ans[len].size(); ++ k) {
				int x = ans[len][k];
				if (temps.count(x)) continue;
				F[i][j] ++;
				temps.insert(x);
			}
			temps.insert(len);
		}
	}
	
	/*for (int i = 0; i < n; ++ i) {
			for (int j = 0; j < n; ++ j) {
				cout << F[i][j] << " ";
		}
		cout << endl;
	}*/
	
	G[1] = F[0][1];
	for (int i = 2; i < n; ++ i) {
		G[i] = F[0][i];
		for (int j = 1; j < i; ++ j) {
			G[i] = (G[i] + cheng(G[j], F[j][i])) % MOD;
		}
	}
	
	cout << G[n-1] << endl;
	return 0;
}
#include<iostream>
#include<cstdio>
#include<cstring>
#include<cstdlib>

using namespace std;

int n, m;
int nxt[30][1000001];
bool v[31][1000001];
int dist[32];
int l;
int zd[32];
int sj[32];

void dfs(int cnt, int t) {
	if (t > 1000000) return;
	if (v[cnt][t]) return;
	v[cnt][t] = true;
	if (dist[cnt] > t) dist[cnt] = t;
	for (int i = 0; i < m; ++ i) {
		if (nxt[i][t]/1000000 == cnt) {
			int y = nxt[i][t] % 1000000;
			//cout << y << endl;
			dfs(y / 10000, t + (y%10000));
		}
	}
}

int main() {
	cin >> n >> m;
	for (int i = 1; i <= n; ++ i) dist[i] = 1047483644;
	dist[1] = 0;
	for (int i = 0; i < m; ++ i) {
		cin >> l;
		for (int j = 0; j < l; ++ j) cin >> zd[j] >> sj[j];
		int t = 0, x = 0;
		while (t <= 1000000) {
			nxt[i][t] = zd[x] * 1000000 + zd[(x+1)%l] * 10000 + sj[x];
			t = t + sj[x];
			x = (x+1) % l;
		}
	}
	for (int i = 0; i <=1000000; ++ i) dfs(1, i);
	for (int i = 2; i <= n; ++ i) {
		if (dist[i]==1047483644) {
			cout << "inf" << endl;
		} else {
			cout << dist[i] << endl;
		}
	}
	return 0;
}
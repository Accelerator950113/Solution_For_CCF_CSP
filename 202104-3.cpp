#include<iostream>
#include<cstdio>
#include<cstring>
#include<cstdlib>
#include<string>

using namespace std;

int N, Tdef, Tmax, Tmin;
int n;
int t;
string sendH, receiveH, Mtype;
int IPadr, endT;
string H;
int state[16384];
int endTime[16384];
string occupier[16384];
int setT;

int check(string& x) {
	for (int i = 1; i <= N; ++ i) {
		if (state[i]!=0 && occupier[i]==x) return i;
	}
	return -1;
}

int getZero() {
	for (int i = 1; i <= N; ++ i) {
		if (state[i]==0) return i;
	}
	return -1;
}

int getThree() {
	for (int i = 1; i <= N; ++ i) {
		if (state[i]==3) return i;
	}
	return -1;
}

void clearName(string& x) {
	for (int i = 1; i <= N; ++ i) {
		if (state[i]==1 && occupier[i]==x) {
			state[i] = 0;
			occupier[i] = "";
			endTime[i] = 0;
		}
	}
}

void flushTime(int t) {
	for (int i = 1; i <= N; ++ i) {
		if (state[i] == 1 || state[i] == 2) {
			if (endTime[i]<=t) {
				if (state[i] == 1) {
					state[i] = 0;
					occupier[i] = "";
					endTime[i] = 0;
				} else {
					state[i] = 3;
					endTime[i] = 0;
				}
			}
		}
	}
}

int main() {
	cin >> N >> Tdef >> Tmax >> Tmin >> H;
	for (int i = 1; i <= N; ++ i) occupier[i] = "";
	memset(state, 0, sizeof(state));
	cin >> n;
	for (int rep = 0; rep < n; ++ rep) {
		cin >> t >> sendH >> receiveH >> Mtype >> IPadr >> endT;
		flushTime(t);
		if (receiveH != H && receiveH != "*" && Mtype != "REQ") continue;
		if (Mtype != "DIS" && Mtype != "REQ") continue;
		if (receiveH == "*" && Mtype != "DIS") continue;
		if (receiveH == H && Mtype == "DIS") continue;
		if (Mtype == "DIS") {
			int selec = check(sendH);
			if (selec == -1) selec = getZero();
			if (selec == -1) selec = getThree();
			if (selec == -1) continue;
			state[selec] = 1;
			occupier[selec] = sendH;
			setT = t + Tdef;
			if (endT != 0) {
				setT = endT;
				if (setT < t + Tmin) setT = t + Tmin;
				if (setT > t + Tmax) setT = t + Tmax;
			}
			endTime[selec] = setT;
			cout << H << " " << sendH << " OFR " << selec << " " << setT << endl;
		} else {
			if (receiveH != H) {
				clearName(sendH);
				continue;
			}
			if (!(1 <= IPadr && IPadr <= N && occupier[IPadr]==sendH)) {
				cout << H << " " << sendH << " NAK " << IPadr << " " << 0 << endl;
				continue;
			}
			state[IPadr] = 2;
			setT = t + Tdef;
			if (endT != 0) {
				setT = endT;
				if (setT < t + Tmin) setT = t + Tmin;
				if (setT > t + Tmax) setT = t + Tmax;
			}
			endTime[IPadr] = setT;
			cout << H << " " << sendH << " ACK " << IPadr << " " << setT << endl;
		}
	}
	return 0;
}
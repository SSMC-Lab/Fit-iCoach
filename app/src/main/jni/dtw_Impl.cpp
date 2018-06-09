#include "dtw_impl.h"
#include <math.h>
#include <cstring>
#define REALMAX  17977000000.0
#define TAG "jni" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型

double getValue(double *t, double *r, int N, int M) {
    double **distance = new double*[M];
    double **dtwpath = new double*[M];

    for (int i = 0; i < M; ++i) {
        distance[i] = new double[N];
        dtwpath[i] = new double[N];
        for (int j = 0; j < N; ++j) {
            dtwpath[i][j] = REALMAX;
            distance[i][j] = (r[i] - t[j]) * (r[i] - t[j]);// 计算两点之间的距离
        }
    }
    dtwpath[0][0] = distance[0][0];

    double D1 = 0;
    double D2 = 0;
    double D3 = 0;
    for (int i = 0; i < M; i++) {
        for (int j = 0; j < N; j++) {
            if (i == 0 && j == 0)
                continue;

            if (i > 0)
                D1 = dtwpath[i - 1][j];
            else
                D1 = REALMAX;

            if (j > 0 && i > 0)
                D2 = dtwpath[i - 1][j - 1];
            else
                D2 = REALMAX;

            if (j > 0)
                D3 = dtwpath[i][j - 1];
            else
                D3 = REALMAX;
            dtwpath[i][j] = distance[i][j] + fmin(fmin(D1, D2), D3);
        }
    }

    double Dist = dtwpath[M - 1][N - 1];
    int MAX = M > N ? M : N;
    Dist = Dist / MAX;

    for(int m=0;m <M;m++){
        delete[] distance[m];
        delete[] dtwpath[m];
    }
    delete[] distance;
    delete[] dtwpath;

    return Dist;
}


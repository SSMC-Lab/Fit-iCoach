//
// Created by Franklin on 4/25/2018.
//

#include <jni.h>
#include <cmath>
#include "emd.h"
#define REALMAX  17977000000.0

#define TAG "jni" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型


#ifdef __cplusplus
extern "C" {
#endif

float dist(feature_t *F1,feature_t *F2)
{
    double dMLA = F1->MLA - F2->MLA;

    return sqrt(dMLA * dMLA);
}

JNIEXPORT jfloat JNICALL
Java_fruitbasket_com_bodyfit_analysis_EarthMoverDistance_getEarthMoverDistance(JNIEnv *env, jobject instance, jdoubleArray s1_,jdoubleArray s2_,jint n1_,jint n2_) {

    jdouble *s1 = env->GetDoubleArrayElements(s1_, NULL);
    jdouble *s2 = env->GetDoubleArrayElements(s2_, NULL);

    jint N = n1_;
    jint M = n2_;

    int i;
    feature_t *f1 = new feature_t[8];
    float *w1 = new float[8]{-20,-15,-10,-5,0,5,10,15};
    int *count1 = new int[8]{0,0,0,0,0,0,0,0};
    for(i = 0;i<N;i++)
    {
        int temp = 1000;
        int index = 0;
        for(int j = 0;j<8;j++){
            index = abs(s1[i] - w1[j]) < temp? j:index;
            temp = abs(s1[i] - w1[j]) < temp? abs(s1[i] - w1[j]) : temp;
        }
        count1[index]++;
    }
    for(int j = 0;j<8;j++){
        f1[j].MLA = w1[j];
        w1[j] = (float)count1[j] / (float)n1_;
    }

    feature_t *f2 = new feature_t[8];
    float *w2 = new float[8]{-20,-15,-10,-5,0,5,10,15};
    int *count2 = new int[8]{0,0,0,0,0,0,0,0};
    for(i = 0;i<M;i++)
    {
        int temp = 1000;
        int index = 0;
        for(int j = 0;j<8;j++){
            index = abs(s2[i] - w2[j]) < temp? j:index;
            temp = abs(s2[i] - w2[j]) < temp? abs(s2[i] - w2[j]) : temp;
        }
        count2[index]++;
    }
    for(int j = 0;j<8;j++){
        f2[j].MLA = w2[j];
        w2[j] = (float)count2[j] / (float)n2_;
    }
    signature_t signature_1 = {8,f1,w1};
    signature_t signature_2 = {8,f2,w2};

    float Dist = emd(&signature_1,&signature_2,dist,0,0);

    env->ReleaseDoubleArrayElements(s1_,s1,0);
    env->ReleaseDoubleArrayElements(s2_,s2,0);

    delete []f1;
    delete []f2;
    delete []w1;
    delete []w2;
    delete []count1;
    delete []count2;

    return Dist;
}


#ifdef __cplusplus
}
#endif
#include <jni.h>
#include <dtw_Impl.h>
#include <test.h>
#define REALMAX  17977000000.0

#define TAG "jni" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型


#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jdouble JNICALL
Java_fruitbasket_com_bodyfit_analysis_DynamicTimeWarping_getDtwValue(JNIEnv *env, jobject instance, jdoubleArray t_, jint n,jdoubleArray r_) {

    if (t_ ==NULL||r_==NULL){
        return -1000000;
    }

    jdouble *t = env->GetDoubleArrayElements(t_, NULL);
    jdouble *r = env->GetDoubleArrayElements(r_, NULL);

    jint N = n;
    jint M = env->GetArrayLength(r_);

    jdouble Dist = getValue(t,r,N,M);
    env->ReleaseDoubleArrayElements(t_,t,0);
    env->ReleaseDoubleArrayElements(r_,r,0);

    return Dist;

}

JNIEXPORT jdouble JNICALL
Java_fruitbasket_com_bodyfit_analysis_DynamicTimeWarping_getfastDtwValue(JNIEnv *env, jobject instance, jdoubleArray t_, jint n,jdoubleArray r_) {

    if (t_ ==NULL||r_==NULL){
        return -1000000;
    }

    jdouble *t = env->GetDoubleArrayElements(t_, NULL);
    jdouble *r = env->GetDoubleArrayElements(r_, NULL);

    jint N = n;
    jint M = env->GetArrayLength(r_);

    jdouble Dist = testFastDTW(t,N,r,M);
    env->ReleaseDoubleArrayElements(t_,t,0);
    env->ReleaseDoubleArrayElements(r_,r,0);

    return Dist;

}

#ifdef __cplusplus
}
#endif
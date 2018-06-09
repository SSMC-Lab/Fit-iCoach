#include <jni.h>
#include <math.h>
#include <android/log.h>
#include <cstring>
#include <dtw_Impl.h>

#define TAG "jni" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // 定义LOGE类型

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jdoubleArray JNICALL
Java_fruitbasket_com_bodyfit_analysis_SingleExerciseAnalysis_downSample(JNIEnv *env, jobject instance,jdoubleArray singleVector, jint srcLength,jint scale) {
    int i = 0;
    int dstLength = srcLength / scale + srcLength % scale;
    jdouble *singleVector_ = env->GetDoubleArrayElements(singleVector,NULL);
    jdoubleArray retVector = env->NewDoubleArray(dstLength);
    jdouble * retVector_ = env->GetDoubleArrayElements(retVector,NULL);
    for (i = 0; i < dstLength; i += scale) {
        retVector_[i] = singleVector_[i];
    }
    env->ReleaseDoubleArrayElements(singleVector,singleVector_,0);
    env->ReleaseDoubleArrayElements(retVector,retVector_,0);
    return retVector;
}

JNIEXPORT jdoubleArray JNICALL
Java_fruitbasket_com_bodyfit_analysis_SingleExerciseAnalysis_ewmaFilter(JNIEnv *env, jobject instance,jdoubleArray singleVector_,jdouble weight_,jint firstrun_,jdoubleArray dataBefore_,jint index){

    jint Nsamples = env->GetArrayLength(singleVector_);
    jdouble *singleVector = env->GetDoubleArrayElements(singleVector_,NULL);
    jdoubleArray filted_Signal_ = env->NewDoubleArray(Nsamples);
    jdouble * filted_Signal = env->GetDoubleArrayElements(filted_Signal_,NULL);
    jdouble * dataBefore = env->GetDoubleArrayElements(dataBefore_,NULL);
    int i = 0;
    for(i=0;i< Nsamples;i++)
    {
        if (firstrun_ == 0) {
//            filted_Signal[i] = singleVector[i];
            filted_Signal[i] = singleVector[i] * weight_ + (1 - weight_) * dataBefore[index];
            firstrun_ = 1;
        }
        else {
            filted_Signal[i] = singleVector[i] * weight_ + (1 - weight_) * filted_Signal[i - 1];
        }
    }
//    firstrun_ = 0;
    env->ReleaseDoubleArrayElements(filted_Signal_,filted_Signal,0);
    env->ReleaseDoubleArrayElements(singleVector_,singleVector,0);
    env->ReleaseDoubleArrayElements(dataBefore_,dataBefore,0);
    return filted_Signal_;
}

JNIEXPORT jdouble JNICALL
Java_fruitbasket_com_bodyfit_analysis_SingleExerciseAnalysis_absAvg(JNIEnv *env, jobject instance,
                                                                    jdoubleArray a_) {
    jdouble *a = env->GetDoubleArrayElements(a_, NULL);
    jint length = env->GetArrayLength(a_);
    double sum = 0, avg;
    int i;
    for (i = 0; i < length; i++) {
        sum += fabs(a[i]);
    }
    avg = sum / length;

    env->ReleaseDoubleArrayElements(a_,a,0);

    return avg;
}

JNIEXPORT jdouble JNICALL
Java_fruitbasket_com_bodyfit_analysis_SingleExerciseAnalysis_Var(JNIEnv *env,jobject instance,jdoubleArray singleVector_){
    double var = 0;
    double square_Sum = 0;
    double Sum = 0;
    int i;
    jint length = env->GetArrayLength(singleVector_);
    jdouble *singleVector = env->GetDoubleArrayElements(singleVector_,NULL);
    for (i = 0; i < length; i++) {
        square_Sum += singleVector[i] * singleVector[i];
        Sum += singleVector[i];
    }

    env->ReleaseDoubleArrayElements(singleVector_,singleVector,0);

    var = square_Sum / length - Sum / length * Sum / length;

    return var;
}

JNIEXPORT jdouble JNICALL
Java_fruitbasket_com_bodyfit_analysis_SingleExerciseAnalysis_mean(JNIEnv *env,jobject instance,jdoubleArray data_){
    jdouble *data = env->GetDoubleArrayElements(data_,NULL);
    jint length = env->GetArrayLength(data_);
    double sum = 0;
    for(int i = 0;i<length;i++)
    {
        sum += data[i];
    }
    env->ReleaseDoubleArrayElements(data_,data,0);
    sum = sum / length;
    return sum;
}

JNIEXPORT jint JNICALL
Java_fruitbasket_com_bodyfit_analysis_SingleExerciseAnalysis_rms(JNIEnv *env, jobject instance, jdoubleArray data_){
    jdouble *data = env->GetDoubleArrayElements(data_,NULL);
    jint length = env->GetArrayLength(data_);
    double sum = 0;
    double min = 233333333;
    int idxReturn = 0;
    for(int i =0;i<length/5;i+=1) {
        sum = 0;
        sum += data[i * 5] * data[i * 5];
        sum += data[i * 5 + 1] * data[i * 5 + 1];
        sum += data[i * 5 + 2] * data[i * 5 + 2];
        sum += data[i * 5 + 3] * data[i * 5 + 3];
        sum += data[i * 5 + 4] * data[i * 5 + 4];
        sum = sqrt(sum/5);
        if (sum < min) {
            min = sum;
            idxReturn = i;
        }

    }
    env->ReleaseDoubleArrayElements(data_,data,0);
    return idxReturn;
}


double dataBuf[3];

JNIEXPORT jdoubleArray JNICALL
Java_fruitbasket_com_bodyfit_analysis_SingleExerciseAnalysis_filter(
        JNIEnv *env, jobject instance, jdoubleArray sensorDatas_, jint span, jint firstrun) {

    jdouble *sensorDatas = env->GetDoubleArrayElements(sensorDatas_, NULL);
    memset(dataBuf, 0, sizeof(dataBuf));
    jint Nsamples = env->GetArrayLength(sensorDatas_);
    jdoubleArray _afterFilted = env->NewDoubleArray(Nsamples);
    jdouble *afterFilted = env->GetDoubleArrayElements(_afterFilted, NULL);

    int i, j;
    double temp;

    for (i = 0; i < Nsamples; i++) {
        temp = sensorDatas[i];
        double sum = 0, avg;

        if (firstrun == 0) {
            for (j = 0; j < span; j++)
                dataBuf[j] = temp;
            firstrun = 1;
        }

        for (j = 0; j < span - 1; j++) {
            sum += dataBuf[j + 1];
            dataBuf[j] = dataBuf[j + 1];
        }
        dataBuf[span - 1] = temp;
        sum += temp;
        avg = sum / span;
        afterFilted[i] = avg;
    }
    firstrun = 0;
    env->ReleaseDoubleArrayElements(sensorDatas_, sensorDatas, 0);
    env->ReleaseDoubleArrayElements(_afterFilted, afterFilted, 0);
    return _afterFilted;

}


JNIEXPORT jdouble JNICALL
Java_fruitbasket_com_bodyfit_ui_DataTableActivity_Var(JNIEnv *env,jobject instance,jdoubleArray singleVector_){
    double var = 0;
    double square_Sum = 0;
    double Sum = 0;
    int i;
    jint length = env->GetArrayLength(singleVector_);
    jdouble *singleVector = env->GetDoubleArrayElements(singleVector_,NULL);
    for (i = 0; i < length; i++) {
        square_Sum += singleVector[i] * singleVector[i];
        Sum += singleVector[i];
    }

    env->ReleaseDoubleArrayElements(singleVector_,singleVector,0);

    var = square_Sum / length - Sum / length * Sum / length;
    return var;
}

JNIEXPORT jdouble JNICALL
Java_fruitbasket_com_bodyfit_ui_DataTableActivity_mean(JNIEnv *env,jobject instance,jdoubleArray data_){
    jdouble *data = env->GetDoubleArrayElements(data_,NULL);
    jint length = env->GetArrayLength(data_);
    double sum = 0;
    for(int i = 0;i<length;i++)
    {
        sum += data[i];
    }
    env->ReleaseDoubleArrayElements(data_,data,0);
    sum = sum / length;
    return sum;
}
#ifdef __cplusplus
}
#endif

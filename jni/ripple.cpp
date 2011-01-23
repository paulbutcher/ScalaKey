#include <jni.h>
#include <android/log.h>

#define  LOG_TAG    "ripple"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

extern "C" {
    JNIEXPORT void JNICALL Java_com_paulbutcher_scalakey_RippleRenderer_onSurfaceChanged(JNIEnv *, jobject, jobject, jint, jint);
    JNIEXPORT void JNICALL Java_com_paulbutcher_scalakey_RippleRenderer_onDrawFrame(JNIEnv *, jobject, jobject);
};

void Java_com_paulbutcher_scalakey_RippleRenderer_onSurfaceChanged(JNIEnv *, jobject, jobject, jint, jint)
{
    LOGI("onSurfaceChanged");
}

void Java_com_paulbutcher_scalakey_RippleRenderer_onDrawFrame(JNIEnv *, jobject, jobject)
{
    LOGI("onDrawFrame");
}

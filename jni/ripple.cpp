#include <jni.h>
#include <android/log.h>
#include <GLES/gl.h>

#define  LOG_TAG    "ripple"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

extern "C" {
    JNIEXPORT void JNICALL Java_com_paulbutcher_scalakey_RippleRenderer_onSurfaceChanged(JNIEnv*, jobject, jobject, jint, jint);
    JNIEXPORT void JNICALL Java_com_paulbutcher_scalakey_RippleRenderer_onDrawFrame(JNIEnv*, jobject, jobject);
};

void Java_com_paulbutcher_scalakey_RippleRenderer_onSurfaceChanged(JNIEnv*, jobject, jobject, jint width, jint height)
{
    LOGI("onSurfaceChanged: %d, %d", width, height);
    
    glViewport(0, 0, width, height);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    // GLU.gluPerspective(gl, 45.0f, width.asInstanceOf[Float] / height, 0.1f, 100.0f)
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();

    glClearColor(0.0, 0.0, 0.0, 0.5);
    glShadeModel(GL_SMOOTH);
    glClearDepthf(1.0);
    glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LEQUAL);
    glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
}

float vertices[] = {
        -1.0f,  1.0f, 0.0f,
        -1.0f, -1.0f, 0.0f,
         1.0f, -1.0f, 0.0f,
         1.0f,  1.0f, 0.5f
    };
    
unsigned short indices[] = {0, 1, 2, 0, 2, 3};

void Java_com_paulbutcher_scalakey_RippleRenderer_onDrawFrame(JNIEnv*, jobject, jobject)
{
    LOGI("onDrawFrame");

    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glLoadIdentity();
	glTranslatef(0, 0, -40); 

    glFrontFace(GL_CCW);
    glEnable(GL_CULL_FACE);
    glCullFace(GL_BACK);
    
    glEnableClientState(GL_VERTEX_ARRAY);
    glVertexPointer(3, GL_FLOAT, 0, vertices);
    glDrawElements(GL_TRIANGLES, sizeof(indices) / sizeof(unsigned short),
        GL_UNSIGNED_SHORT, indices);
      
    glDisableClientState(GL_VERTEX_ARRAY);
    glDisable(GL_CULL_FACE);
}

#include <jni.h>
#include <android/log.h>
#include <GLES/gl.h>

#include "com_paulbutcher_scalakey_Mesh.h"

#define  LOG_TAG    "ripple"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

JNIEXPORT void JNICALL
Java_com_paulbutcher_scalakey_Mesh_initializeBuffers(JNIEnv* env, jobject obj)
{
    LOGI("initializeBuffers");

    jclass clazz = env->GetObjectClass(obj);

    jmethodID columnsId = env->GetMethodID(clazz, "columns", "()I");
    jint columns = env->CallIntMethod(obj, columnsId);

    jmethodID rowsId = env->GetMethodID(clazz, "rows", "()I");
    jint rows = env->CallIntMethod(obj, rowsId);

    jmethodID widthId = env->GetMethodID(clazz, "width", "()F");
    jfloat width = env->CallFloatMethod(obj, widthId);
    
    jmethodID heightId = env->GetMethodID(clazz, "height", "()F");
    jfloat height = env->CallFloatMethod(obj, heightId);

    jmethodID vertexBufferId = env->GetMethodID(clazz, "vertexBuffer", "()Ljava/nio/FloatBuffer;");
    jobject vertexBufferObj = env->CallObjectMethod(obj, vertexBufferId);
    float* vertexBuffer = reinterpret_cast<float*>(env->GetDirectBufferAddress(vertexBufferObj));

    // Generate vertices in the range [-width/2 ... 0 ... width/2]
    // and similarly for height
    float* vertex = vertexBuffer;
    for(int i = 0; i < rows; ++i) {
        jfloat y = -height / 2 + height * (float)i / (float)(rows - 1);
        for(int j = 0; j < columns; ++j) {
            jfloat x = -width / 2 + width * (float)j / (float)(columns - 1);
            
            vertex[0] = x;
            vertex[1] = y;
            vertex[2] = 0.0;

            vertex += 3;
        }
    }
    
    jmethodID indexBufferId = env->GetMethodID(clazz, "indexBuffer", "()Ljava/nio/ShortBuffer;");
    jobject indexBufferObj = env->CallObjectMethod(obj, indexBufferId);
    short* indexBuffer = reinterpret_cast<short*>(env->GetDirectBufferAddress(indexBufferObj));
    
    // Indices for a single triangle strip. See:
    // http://marc.blog.atpurpose.com/2009/10/24/programatically-generating-a-rectangular-mesh-using-single-gl_triangle_strip/
    short* index = indexBuffer;
    for(int row = 0; row < rows - 1; ++row) {
        for(int column = 0; column < columns; ++column) {
            *(index++) = row * columns + column;
            *(index++) = (row + 1) * columns + column;
        }
        
        // Extra vertices (of degenerate triangles) at the end of this row connecting it to the start of the next one
        if(row < rows - 2) {
            *(index++) = (row + 1) * columns + (columns - 1);
            *(index++) = (row + 1) * columns;
        }
    }
    
    jmethodID textureBufferId = env->GetMethodID(clazz, "textureBuffer", "()Ljava/nio/FloatBuffer;");
    jobject textureBufferObj = env->CallObjectMethod(obj, textureBufferId);
    float* textureBuffer = reinterpret_cast<float*>(env->GetDirectBufferAddress(textureBufferObj));
    
    float* textureCoord = textureBuffer;
    for(int i = 0; i < rows; ++i) {
        float v = 1.0 - (float)i / (float)(rows - 1);
        for(int j = 0; j < columns; ++j) {
            float u = 0.0 + (float)j / (float)(columns - 1);
            
            textureCoord[0] = u;
            textureCoord[1] = v;
            
            textureCoord += 2;
        }
    }
}

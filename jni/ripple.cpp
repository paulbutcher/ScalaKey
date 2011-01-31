#include <jni.h>
#include <android/log.h>
#include <GLES/gl.h>
#include <cmath>

#include "com_paulbutcher_scalakey_Mesh.h"

#define  LOG_TAG    "ripple"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

static const float amplitude = 0.1;
static const float wavelength = 0.1;
static const float wavepacket = 0.15;
static const float speed = 0.5;
static const float attenuation = 1.0;

static jclass clazz = 0;
static jmethodID columnsId = 0;
static jmethodID rowsId = 0;
static jmethodID widthId = 0;
static jmethodID heightId = 0;
static jmethodID vertexBufferId = 0;
static jmethodID indexBufferId = 0;
static jmethodID textureBufferId = 0;
static jmethodID rippleXsId = 0;
static jmethodID rippleYsId = 0;
static jmethodID rippleTimesId = 0;
static jmethodID rippleCountId = 0;

static void initIDs(JNIEnv* env, jobject obj) {
    clazz = env->GetObjectClass(obj);

    columnsId = env->GetMethodID(clazz, "columns", "()I");
    rowsId = env->GetMethodID(clazz, "rows", "()I");
    widthId = env->GetMethodID(clazz, "width", "()F");
    heightId = env->GetMethodID(clazz, "height", "()F");
    vertexBufferId = env->GetMethodID(clazz, "vertexBuffer", "()Ljava/nio/FloatBuffer;");
    indexBufferId = env->GetMethodID(clazz, "indexBuffer", "()Ljava/nio/ShortBuffer;");
    textureBufferId = env->GetMethodID(clazz, "textureBuffer", "()Ljava/nio/FloatBuffer;");
    rippleXsId = env->GetMethodID(clazz, "rippleXs", "()[F");
    rippleYsId = env->GetMethodID(clazz, "rippleYs", "()[F");
    rippleTimesId = env->GetMethodID(clazz, "rippleTimes", "()[J");
    rippleCountId = env->GetMethodID(clazz, "rippleCount", "()I");
}

JNIEXPORT void JNICALL
Java_com_paulbutcher_scalakey_Mesh_initializeBuffers(JNIEnv* env, jobject obj)
{
    LOGI("initializeBuffers");
    
    initIDs(env, obj);

    jint columns = env->CallIntMethod(obj, columnsId);
    jint rows = env->CallIntMethod(obj, rowsId);

    jobject vertexBufferObj = env->CallObjectMethod(obj, vertexBufferId);
    float* vertexBuffer = reinterpret_cast<float*>(env->GetDirectBufferAddress(vertexBufferObj));
    
    // Just zero everything for now - vertices are actually calculated in ripple below
    for(int i = 0; i < columns * rows; ++i) {
        vertexBuffer[i] = 0.0;
    }
    
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

JNIEXPORT void JNICALL
Java_com_paulbutcher_scalakey_Mesh_ripple(JNIEnv* env, jobject obj, jlong now)
{
    jint columns = env->CallIntMethod(obj, columnsId);
    jint rows = env->CallIntMethod(obj, rowsId);
    jfloat width = env->CallFloatMethod(obj, widthId);
    jfloat height = env->CallFloatMethod(obj, heightId);
    
    jint rippleCount = env->CallIntMethod(obj, rippleCountId);
    
    jfloatArray rippleXsArray = static_cast<jfloatArray>(env->CallObjectMethod(obj, rippleXsId));
    jfloat* rippleXs = env->GetFloatArrayElements(rippleXsArray, 0);

    jfloatArray rippleYsArray = static_cast<jfloatArray>(env->CallObjectMethod(obj, rippleYsId));
    jfloat* rippleYs = env->GetFloatArrayElements(rippleYsArray, 0);

    jlongArray rippleTimesArray = static_cast<jlongArray>(env->CallObjectMethod(obj, rippleTimesId));
    jlong* rippleTimes = env->GetLongArrayElements(rippleTimesArray, 0);
    
    jobject vertexBufferObj = env->CallObjectMethod(obj, vertexBufferId);
    float* vertexBuffer = reinterpret_cast<float*>(env->GetDirectBufferAddress(vertexBufferObj));
    
    // Distance ripple has travelled (i.e. of ripple peak)
    float distances[rippleCount];
    
    // Ripple amplitude after attenuation
    float waveHeights[rippleCount];

    for(int k = 0; k < rippleCount; ++k) {
        jfloat elapsed = (now - rippleTimes[k]) / 1000.0;
        distances[k] = elapsed * speed;
        waveHeights[k] = amplitude * exp(-(distances[k] * distances[k])) / attenuation;
    }
    
    // Generate vertices in the range [-width/2 ... 0 ... width/2]
    // and similarly for height
    float* vertex = vertexBuffer;
    for(int i = 0; i < rows; ++i) {
        jfloat y = -height / 2 + height * (float)i / (float)(rows - 1);

        for(int j = 0; j < columns; ++j) {
            jfloat x = -width / 2 + width * (float)j / (float)(columns - 1);
            
            jfloat z = 0.0;
            for(int k = 0; k < rippleCount; ++k) {
                
                // Radius from ripple centre
                jfloat xdelta = x - rippleXs[k];
                jfloat ydelta = y - rippleYs[k];
                jfloat r = sqrt(xdelta * xdelta + ydelta * ydelta);
            
                // Delta from ripple peak
                jfloat delta = distances[k] - r;
                jfloat packetDelta = delta / wavepacket;
            
                z += waveHeights[k] * cos(delta * 3.14 / wavelength) * exp(-(packetDelta * packetDelta));
            }
            
            vertex[0] = x;
            vertex[1] = y;
            vertex[2] = z;

            vertex += 3;
        }
    }
    
    env->ReleaseFloatArrayElements(rippleXsArray, rippleXs, 0);
    env->ReleaseFloatArrayElements(rippleYsArray, rippleYs, 0);
    env->ReleaseLongArrayElements(rippleTimesArray, rippleTimes, 0);
}

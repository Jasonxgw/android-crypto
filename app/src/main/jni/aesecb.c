#include <jni.h>

//JNIEXPORT jstring JNICALL

jstring Java_tsou_com_encryption_jnimode_AESecb_getKey(JNIEnv
                                                       *env, jobject instance) {

    return (*env)->NewStringUTF(env, "7t3e506j9z10xbd4");
}
jstring Java_tsou_com_encryption_jnimode_AESecb_getIvParameter(JNIEnv
                                                       *env, jobject instance) {

    return (*env)->NewStringUTF(env, "16-Bytes--String");
}


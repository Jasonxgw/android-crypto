#include <jni.h>

//JNIEXPORT jstring JNICALL

jstring Java_tsou_cn_mylibrary_AEScbc_getKey(JNIEnv
                                                       *env, jobject instance) {

    return (*env)->NewStringUTF(env, "983edfea7z1btrf4");
}
jstring Java_tsou_cn_mylibrary_AEScbc_getIvParameter(JNIEnv
                                                       *env, jobject instance) {

    return (*env)->NewStringUTF(env, "16-Bytes--String");
}
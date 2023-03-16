//#include <jni.h>
//#include <stdio.h>
//#include <stdlib.h>
//#include <string.h>
//#include <time.h>
//
//
//
//
///* 求区间在[mmin, mmax]的随机数 */
//int random(JNIEnv * env, jobject obj,int mmin, int mmax) {
//    return rand() % (mmax - mmin + 1) + mmin;
//}
//
//
///* 交换两个值 */
//void _swap(JNIEnv * env, jobject obj,unsigned char *arr, int i1, int i2) {
//    unsigned char tmp = arr[i1];
//    arr[i1] = arr[i2];
//    arr[i2] = tmp;
//}
//
///* 随机生成编码表 */
//void cipher_GenEncTable(JNIEnv * env, jobject obj,unsigned char strTable[0x100]) {
//    int i, n1, n2;
//
//    /* 初始化随机数种子 */
//    srand(time(0));
//
//    /* 先生成一张按顺序的ASCII码表 */
//    for (i = 0; i < 0x100; i++)
//        strTable[i] = i;
//
//    /* 再随机交换其中的两个值 */
//    for (i = 0; i < 0x100; i++) {
//        n1 = random(env,obj,0, 0xFF);
//
//        /* 防止生成的 n1 == n2 如果他们相等了那就没意义了 */
//        while (n1 == (n2 = random(env,obj,0, 0xFF)));
//
//        /* 交换第n1个和第n2个值 */
//        _swap(env,obj,strTable, n1, n2);
//    }
//}
//
///* 根据编码表得到其解码表 */
//void
//cipher_EncToDecTable(JNIEnv * env, jobject obj,const unsigned char strEncTable[0x100], unsigned char strDecTable[0x100]) {
//    int i;
//    for (i = 0; i < 0x100; i++) {
//        strDecTable[strEncTable[i]] = i;
//    }
//}
//
///* 打印出表内数据 */
//void cipher_PrintTable(JNIEnv * env, jobject obj,unsigned char strTable[0x100]) {
//    int i, j, count;
//    count = 0;
//
//    printf("unsigned char Table[] = \n{\n");
//    for (i = 0; i < 0x10; i++) {
//        printf("\t");
//        for (j = 0; j < 0x10; j++) {
//            printf("0x%02X, ", strTable[count]);
//            count++;
//        }
//        printf("\n");
//    }
//    printf("};\n");
//}
//
///*
// * 根据给的表的不同进行数据编码(解码)
// * 其实, 编码表和解码表的意义是相同的,
// * 编码表可以用来解码, 解码表可以用来编码,
// * 只是叫法不同而已.
// */
//void cipher_Coding(JNIEnv * env, jobject obj,const unsigned char *src, unsigned char *dst, int len,
//                   const unsigned char strTable[0x100]) {
//    int i;
//    for (i = 0; i < len; i++) {
//        dst[i] = strTable[src[i]];
//    }
//}
//
////JNIEXPORT jstring JNICALL
////加密
//jstring  Java_tsou_com_encryption_jnimode_Encryption_encryption(JNIEnv *env, jobject instance,jstring str ) {
//    unsigned char tblEnc[0x100];
//    cipher_GenEncTable(env,obj,tblEnc);
//    char strdst[50] = {0};
//    cipher_Coding(env,obj,(unsigned char *) str, (unsigned char *) strdst, 23, tblEnc);
//    return (*env)->NewStringUTF(env, strdst);
//}
//
//
////解密
//jstring Java_tsou_com_encryption_jnimode_Encryption_decryption(JNIEnv *env, jobject instance,jstring strdst) {
//    unsigned char tblEnc[0x100], tblDec[0x100];
//    cipher_EncToDecTable(env,obj,tblEnc, tblDec);
//
//    cipher_Coding(env,obj,(unsigned char *) strdst, (unsigned char *) strdst, 23, tblDec);
//    return (*env)->NewStringUTF(env, strdst);
//}
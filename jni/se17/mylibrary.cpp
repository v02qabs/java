#include "NativeExample.h"
#include <iostream>

JNIEXPORT void JNICALL Java_NativeExample_nativeMethod(JNIEnv *env, jobject obj) {
    std::cout << "Hello from Native Code!" << std::endl;
}

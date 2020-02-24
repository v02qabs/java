#include <gtk/gtk.h>
#include <jni.h>

JNIEXPORT void JNICALL Java_gtk_show(JNIEnv *env, jobject *obj)
{
	g_print("Hello.\n");
}


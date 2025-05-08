public class NativeExample {
    static {
        System.loadLibrary("mylibrary"); // ネイティブライブラリ名
    }

    public native void nativeMethod(); // ネイティブメソッドの宣言

    public static void main(String[] args) {
        new NativeExample().nativeMethod();
    }
}

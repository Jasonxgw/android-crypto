package tsou.cn.mylibrary;

/**
 * Created by Administrator on 2017/9/22 0022.
 */

public class AEScbc {

    static {
        System.loadLibrary("aescbc");
    }

    public static native String getKey();

    public static native String getIvParameter();
}

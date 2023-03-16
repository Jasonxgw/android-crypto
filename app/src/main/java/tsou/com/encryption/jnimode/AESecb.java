package tsou.com.encryption.jnimode;

/**
 * Created by Administrator on 2017/9/22 0022.
 */

public class AESecb {

    static {
        System.loadLibrary("aesecb");
    }

    public native String getKey();
    public native String getIvParameter();
}

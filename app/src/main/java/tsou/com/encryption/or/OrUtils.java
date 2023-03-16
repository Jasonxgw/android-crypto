package tsou.com.encryption.or;

/**
 * Created by Administrator on 2017/9/20 0020.
 */

public class OrUtils {

    private final static int orKey = 0x520;

    /**
     * 固定key的方式,
     * <p>
     * 这种方式加密解密 算法一样
     *
     * @param bytes
     * @return
     */
    public static byte[] encrypt(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        int len = bytes.length;
        int key = orKey;
        //int key = 0x12;
        for (int i = 0; i < len; i++) {
            bytes[i] ^= key;
        }
        return bytes;
    }

    /**
     * 不固定key的方式
     * <p>
     * 加密实现
     *
     * @param bytes
     * @return
     */
    public static byte[] encode(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        int len = bytes.length;
        int key = orKey;
        //int key = 0x12;
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) (bytes[i] ^ key);
            key = bytes[i];
        }
        return bytes;
    }

    /**
     * 不固定key的方式
     * <p>
     * 解密实现
     *
     * @param bytes
     * @return
     */
    public static byte[] decode(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        int len = bytes.length;
        //int key = 0x12;
        int key = orKey;
        for (int i = len - 1; i > 0; i--) {
            bytes[i] = (byte) (bytes[i] ^ bytes[i - 1]);
        }
        bytes[0] = (byte) (bytes[0] ^ key);
        return bytes;
    }
}

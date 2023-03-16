package tsou.com.encryption.base64;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2017/9/20 0020.
 * <p>
 * 无论是编码还是解码都会有一个参数Flags，Android提供了以下几种
 * <p>
 * DEFAULT 这个参数是默认，使用默认的方法来加密
 * <p>
 * NO_PADDING 这个参数是略去加密字符串最后的”=”
 * <p>
 * NO_WRAP 这个参数意思是略去所有的换行符（设置后CRLF就没用了）
 * <p>
 * CRLF 这个参数看起来比较眼熟，它就是Win风格的换行符，意思就是使用CR LF这一对作为一行的结尾而不是Unix风格的LF
 * <p>
 * URL_SAFE 这个参数意思是加密时不使用对URL和文件名有特殊意义的字符来作为加密字符，具体就是以-和_取代+和/
 */

public class Base64Utils {
    /**
     * 字符串进行Base64编码加密
     *
     * @param str
     * @return
     */
    public static String encodeString(String str) {
        return Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
    }

    /**
     * 字符串进行Base64解码解密
     *
     * @param encodedString
     * @return
     */

    public static String decodeString(String encodedString) {
        return new String(Base64.decode(encodedString, Base64.DEFAULT));
    }

    /**
     * 对文件进行Base64编码加密
     *
     * @param path
     * @return
     */
    public static String encodeFile(String path) {
        File file = new File(path);
        FileInputStream inputFile = null;
        try {
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            return Base64.encodeToString(buffer, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 对文件进行Base64解码解密
     *
     * @param encodedPath
     * @return
     */

    public static void decodeFile(String encodedPath) {
        File desFile = new File(encodedPath);
        FileOutputStream fos = null;
        try {
            byte[] decodeBytes = Base64.decode(encodedPath.getBytes(), Base64.DEFAULT);
            fos = new FileOutputStream(desFile);
            fos.write(decodeBytes);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package tsou.com.encryption.androidkeystoresign;

import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.security.auth.x500.X500Principal;

/**
 * Created by Administrator on 2017/10/10 0010.
 */

public class KeyStoneUtils {
    /**
     * 自己给你的别名，方便在keystore中查找秘钥
     */
    public static final String SAMPLE_ALIAS = "xiaoGuoKey";

    /**
     * 自己给你的别名  就是SAMPLE_ALIAS
     */
    private static String mAlias = null;

    public static void setAlias(String alias) {
        mAlias = alias;
    }

    /**
     * 创建一个公共和私人密钥,并将其存储使用Android密钥存储库中,因此,只有
     * 这个应用程序将能够访问键。
     *
     * @param context
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void createKeys(Context context) throws InvalidAlgorithmParameterException,
            NoSuchProviderException, NoSuchAlgorithmException {
        setAlias(SAMPLE_ALIAS);
        //创建一个开始和结束时间,有效范围内的密钥对才会生成。
        Calendar start = new GregorianCalendar();
        Calendar end = new GregorianCalendar();
        end.add(Calendar.YEAR, 1);//往后加一年
        AlgorithmParameterSpec spec;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //使用别名来检索的key 。这是一个key 的key !
            spec = new KeyPairGeneratorSpec.Builder(context)
                    //使用别名来检索的关键。这是一个关键的关键!
                    .setAlias(mAlias)
                    // 用于生成自签名证书的主题 X500Principal 接受 RFC 1779/2253的专有名词
                    .setSubject(new X500Principal("CN=" + mAlias))
                    //用于自签名证书的序列号生成的一对。
                    .setSerialNumber(BigInteger.valueOf(1337))
                    // 签名在有效日期范围内
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();
        } else {
            //Android 6.0(或者以上)使用KeyGenparameterSpec.Builder 方式来创建,
            // 允许你自定义允许的的关键属性和限制
//            String AES_MODE_CBC = KeyProperties.KEY_ALGORITHM_AES + "/" +
//                    KeyProperties.BLOCK_MODE_CBC + "/" +
//                    KeyProperties.ENCRYPTION_PADDING_PKCS7;
            spec = new KeyGenParameterSpec.Builder(mAlias, KeyProperties.PURPOSE_SIGN)
                    .setCertificateSubject(new X500Principal("CN=" + mAlias))
                    .setDigests(KeyProperties.DIGEST_SHA256)
                    .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM, KeyProperties.BLOCK_MODE_CTR,
                            KeyProperties.BLOCK_MODE_CBC, KeyProperties.BLOCK_MODE_ECB)
//                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM/CTR/CBC/ECB)
                    .setCertificateSerialNumber(BigInteger.valueOf(1337))
                    .setCertificateNotBefore(start.getTime())
                    .setCertificateNotAfter(end.getTime())
                    .build();
        }
        KeyPairGenerator kpGenerator = KeyPairGenerator
                .getInstance(SecurityConstants.TYPE_RSA,
                        SecurityConstants.KEYSTORE_PROVIDER_ANDROID_KEYSTORE);
        kpGenerator.initialize(spec);
        KeyPair kp = kpGenerator.generateKeyPair();
        Log.d("huangxiaoguo", "公共密钥: " + kp.getPublic().toString());
        Log.d("huangxiaoguo", "私钥: " + kp.getPrivate().toString());

    }

    /**
     * 签名
     *
     * @param inputStr
     * @return
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws UnrecoverableEntryException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static String signData(String inputStr) throws KeyStoreException, CertificateException,
            NoSuchAlgorithmException, IOException, UnrecoverableEntryException,
            InvalidKeyException, SignatureException {
        byte[] data = inputStr.getBytes();
        //AndroidKeyStore
        KeyStore ks = KeyStore.getInstance(SecurityConstants.KEYSTORE_PROVIDER_ANDROID_KEYSTORE);
        // 如果你没有InputStream加载,你仍然需要
        //称之为“负载”,或者它会崩溃
        ks.load(null);
        if (mAlias == null) {
            setAlias(SAMPLE_ALIAS);
        }
        //从Android加载密钥对密钥存储库中
        KeyStore.Entry entry = ks.getEntry(mAlias, null);
         /* *
         *进行判断处理钥匙是不是存储的当前别名下 不存在要遍历别名列表Keystore.aliases()
         */
        if (entry == null) {
            Log.w("huangxiaoguo", "No key found under alias: " + mAlias);
            Log.w("huangxiaoguo", "Exiting signData()...");
            return null;
        }
        if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
            Log.w("huangxiaoguo", "Not an instance of a PrivateKeyEntry");
            Log.w("huangxiaoguo", "Exiting signData()...");
            return null;
        }
        // 开始签名
        Signature s = Signature.getInstance(SecurityConstants.SIGNATURE_SHA256withRSA);
        //初始化使用指定的私钥签名
        s.initSign(((KeyStore.PrivateKeyEntry) entry).getPrivateKey());
        // 签名并存储结果作为Base64编码的字符串。
        s.update(data);
        byte[] signature = s.sign();
        String result = Base64.encodeToString(signature, Base64.DEFAULT);
        return result;
    }

    /**
     * 校验签名的字符串
     *
     * @param input
     * @param signatureStr
     * @return
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws UnrecoverableEntryException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static boolean verifyData(String input, String signatureStr) throws KeyStoreException,
            CertificateException, NoSuchAlgorithmException, IOException,
            UnrecoverableEntryException, InvalidKeyException, SignatureException {

        //要验证的数据
        byte[] data = input.getBytes();
        //签名
        byte[] signature;


        //判断签名是否存在
        if (signatureStr == null) {
            Log.w("huangxiaoguo", "Invalid signature.");
            Log.w("huangxiaoguo", "Exiting verifyData()...");
            return false;
        }

        try {

            //Base64解码字符串
            signature = Base64.decode(signatureStr, Base64.DEFAULT);
        } catch (IllegalArgumentException e) {
            return false;
        }
        KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
        ks.load(null);

        // Load the key pair from the Android Key Store
        if (mAlias == null) {
            setAlias(SAMPLE_ALIAS);
        }
        //从Android加载密钥对密钥存储库中
        KeyStore.Entry entry = ks.getEntry(mAlias, null);
        if (entry == null) {
            Log.w("huangxiaoguo", "No key found under alias: " + mAlias);
            Log.w("huangxiaoguo", "Exiting verifyData()...");
            return false;
        }
        if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
            Log.w("huangxiaoguo", "Not an instance of a PrivateKeyEntry");
            return false;
        }
        Signature s = Signature.getInstance(SecurityConstants.SIGNATURE_SHA256withRSA);
        // 开始校验签名
        s.initVerify(((KeyStore.PrivateKeyEntry) entry).getCertificate());
        s.update(data);
        boolean valid = s.verify(signature);
        return valid;//true 签名一致

    }

    /**
     * 判断是否创建过秘钥
     *
     * @return
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws UnrecoverableEntryException
     */
    public static boolean isHaveKeyStore() {
        try {
            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);
            if (mAlias == null) {
                setAlias(SAMPLE_ALIAS);
            }
            // Load the key pair from the Android Key Store
            //从Android加载密钥对密钥存储库中
            KeyStore.Entry entry = ks.getEntry(mAlias, null);
            if (entry == null) {
                return false;
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
            return false;
        } catch (CertificateException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}

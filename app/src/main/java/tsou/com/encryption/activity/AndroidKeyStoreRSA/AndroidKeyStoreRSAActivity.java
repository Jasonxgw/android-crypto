package tsou.com.encryption.activity.AndroidKeyStoreRSA;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.RSAPublicKey;

import tsou.com.encryption.AndroidKeyStoreRSA.AndroidKeyStoreRSAUtils;
import tsou.com.encryption.R;
import tsou.com.encryption.aescbc.Base64Decoder;
import tsou.com.encryption.aescbc.Base64Encoder;

/**
 * 非对称RSA加密解密
 * <p>
 * <p>
 * 一、什么是Rsa加密？
 * <p>
 * RSA算法是最流行的公钥密码算法，使用长度可以变化的密钥。
 * RSA是第一个既能用于数据加密也能用于数字签名的算法。
 * RSA算法原理如下：
 * 1.随机选择两个大质数p和q，p不等于q，计算N=pq；
 * 2.选择一个大于1小于N的自然数e，e必须与(p-1)(q-1)互素。
 * 3.用公式计算出d：d×e = 1 (mod (p-1)(q-1)) 。
 * 4.销毁p和q。
 * 最终得到的N和e就是“公钥”，d就是“私钥”，发送方使用N去加密数据，接收方只有使用d才能解开数据内容。
 * RSA的安全性依赖于大数分解，小于1024位的N已经被证明是不安全的，而且由于RSA算法进行的都是大数计算，
 * 使得RSA最快的情况也比DES慢上倍，这是RSA最大的缺陷，因此通常只能用于加密少量数据或者加密密钥，
 * 但RSA仍然不失为一种高强度的算法。
 * <p>
 * <p>
 * 二、app与后台的tokenRSA加密登录认证与安全方式
 * <p>
 * 客户端向服务器第一次发起登录请求（不传输用户名和密码）。
 * <p>
 * 服务器利用RSA算法产生一对公钥和私钥。并保留私钥， 将公钥发送给客户端。
 * <p>
 * 客户端收到公钥后， 加密用户密码，向服务器发送用户名和加密后的用户密码；
 * 同时另外产生一对公钥和私钥，自己保留私钥, 向服务器发送公钥；
 * 于是第二次登录请求传输了用户名和加密后的密码以及客户端生成的公钥。
 * <p>
 * 服务器利用保留的私钥对密文进行解密，得到真正的密码。 经过判断，
 * 确定用户可以登录后，生成sessionId和token，同时利用客户端发送的公钥，对token进行加密。
 * 最后将sessionId和加密后的token返还给客户端。
 * <p>
 * 客户端利用自己生成的私钥对token密文解密， 得到真正的token。
 * <p>
 * 三、博客地址 http://blog.csdn.net/huangxiaoguo1/article/details/78031798
 */
public class AndroidKeyStoreRSAActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText encryptionContext;
    private Button encryption;
    private TextView tvEncryption;
    private Button decode;
    private TextView tvDecode;
    private Activity mActivity;
    private Context mContext;
    private RSAPublicKey publicKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aes);
        mActivity = this;
        mContext = this;

        encryptionContext = (EditText) findViewById(R.id.et_encryption_context);
        encryption = (Button) findViewById(R.id.btn_encryption);
        tvEncryption = (TextView) findViewById(R.id.tv_encryption);
        decode = (Button) findViewById(R.id.btn_decode);
        tvDecode = (TextView) findViewById(R.id.tv_decode);
        encryption.setText("公钥加密");
        decode.setText("私钥解密");
        initListener();
    }

    /**
     * 在应用安装后第一次运行时，生成一个随机密钥，并存入 KeyStore
     * 当你想存储一个数据，便从 KeyStore 中取出之前生成的随机密钥，对你的数据进行加密，
     * 加密完成后，已完成加密的数据可以随意存储在任意地方，比如 SharePreferences，
     * 此时即使它被他人读取到，也无法解密出你的原数据，因为他人取不到你的密钥
     * 当你需要拿到你的原数据，只需要从 SharePreferences 中读取你加密后的数据，
     * 并从 KeyStore 取出加密密钥，使用加密密钥对 “加密后的数据” 进行解密即可
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (AndroidKeyStoreRSAUtils.isHaveKeyStore()) {//是否有秘钥
            publicKey = (RSAPublicKey) AndroidKeyStoreRSAUtils.getLocalPublicKey();
            if (publicKey != null) {
                Toast.makeText(mContext, "已经生成过密钥对", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        try {//在项目中放在application或启动页中
            KeyPair keyPair = AndroidKeyStoreRSAUtils.generateRSAKeyPair(mContext);
            // 公钥
            publicKey = (RSAPublicKey) keyPair.getPublic();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }


    private void initListener() {
        encryption.setOnClickListener(this);
        decode.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_encryption://公钥加密
                //获取客户端公钥的base64编码的String,登录时将公钥传递给后台
                // String localPublicKey = EncodeUtils.base64Encode2String(publicKey.getEncoded());

//                String encryptionString = encryptionContext.getText().toString().trim();
                String encryptionString = "{\"imsi\":\"1297727855f679968349b9c85bc844b7d88c4a98\",\"osVer\":\"Android_26-2392*1440-AOSP on msm8996\",\"userAgent\":\"Android_Plugin\",\"mac\":\"b9ec61c5e5028077\",\"mobileNo\":\"13549388953\",\"timestamp\":\"1678913501482\",\"appId\":\"GAOYANG06\",\"deviceId\":\"08DDD8D4DF243C51D7B2F71EA422AEAD\",\"source\":\"plugin\",\"platform\":\"3\",\"appName\":\"Android_Pay_Plugin\",\"secretId\":\"1678913501482\",\"ver\":\"9.11.290\",\"sessionId\":\"36d8f991-b925-4bce-9438-62d9712b8001\",\"imei\":\"b9ec61c5e5028077\",\"random\":\"1678913501482\",\"orderId\":\"PES30314PES230314625100740\",\"paymentPassword\":\"8D653F15F59CE5F3\",\"payKey\":\"8A37D75854E01B8EB8EE8990E76BDA1A213F77DF8886803AB8E4B7306BBCA98A8C4C64EAD67AB5268CA5DE444FDE01C00AB1F4040944FCF7B63638CDAB6E6712EEA915A6ED71D46A6BFE2127AF1A548A33C925BFF34DEF495707F0E56941EC0E98085EFFE028F00DDB6002BF1E3ACAD5881E16BA5BAA1A6D22A759B4632F5CC3\",\"barCodeFlag\":\"false\"}";

                if (TextUtils.isEmpty(encryptionString)) {
                    Toast.makeText(mContext, "请输入加密内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    byte[] encryptBytes = AndroidKeyStoreRSAUtils.encryptByPublicKeyForSpilt(encryptionString.getBytes(),
                            publicKey.getEncoded());
//                    byte[] encryptBytes = AndroidKeyStoreRSAUtils.encryptByPublicKey(encryptionString.getBytes(),
//                            publicKey.getEncoded());
                    String encryStr = Base64Encoder.encode(encryptBytes);
                    tvEncryption.setText(encryStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
            case R.id.btn_decode://私钥解密

                String decodeString = tvEncryption.getText().toString().trim();
                if (TextUtils.isEmpty(decodeString)) {
                    Toast.makeText(mContext, "请先加密", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    byte[] decryptBytes = AndroidKeyStoreRSAUtils.decryptByPrivateKeyForSpilt(
                            Base64Decoder.decodeToBytes(decodeString));
//                    byte[] decryptBytes = AndroidKeyStoreRSAUtils.decryptByPrivateKey(
//                            Base64Decoder.decodeToBytes(decodeString));
                    tvDecode.setText(new String(decryptBytes));
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
        }
    }
}

package tsou.com.encryption.activity.RSA;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import tsou.com.encryption.R;
import tsou.com.encryption.aescbc.Base64Decoder;
import tsou.com.encryption.aescbc.Base64Encoder;
import tsou.com.encryption.rsa.RSAUtils;

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
 *
 * 三、博客地址 http://blog.csdn.net/huangxiaoguo1/article/details/78031798
 *
 *
 */
public class RSAAsymmetricActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText encryptionContext;
    private Button encryption;
    private TextView tvEncryption;
    private Button decode;
    private TextView tvDecode;
    private Activity mActivity;
    private Context mContext;
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        KeyPair keyPair = RSAUtils.generateRSAKeyPair(RSAUtils.DEFAULT_KEY_SIZE);
        // 公钥
        publicKey = (RSAPublicKey) keyPair.getPublic();
        // 私钥
        privateKey = (RSAPrivateKey) keyPair.getPrivate();
    }

    private void initListener() {
        encryption.setOnClickListener(this);
        decode.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_encryption://公钥加密
                //获取客服端公钥的base64编码的String,登录时将公钥传递给后台
                // String localPublicKey = EncodeUtils.base64Encode2String(publicKey.getEncoded());

                //保存客户端私钥,传递给服务器时保存到本地备用，可以使用AES加密保存
                //String localprivateKey = EncodeUtils.base64Encode2String(privateKey.getEncoded());
                String encryptionString = encryptionContext.getText().toString().trim();

                if (TextUtils.isEmpty(encryptionString)) {
                    Toast.makeText(mContext, "请输入加密内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    byte[] encryptBytes = RSAUtils.encryptByPublicKeyForSpilt(encryptionString.getBytes(),
                            publicKey.getEncoded());
                    String encryStr = Base64Encoder.encode(encryptBytes);
                    tvEncryption.setText(encryStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
            case R.id.btn_decode://私钥解密

//                String decodeString = tvEncryption.getText().toString().trim();
                String decodeString = "QPtfZZWSa2gzxEP998LtBqRspmv/PPLW9hGdBDHdGIpovYpEm3q038aerh+40vIjWsjjEBDYzLJ5d+3mtKjPd2GQe1Rlsmxr8kOiTNZ9FWBPIuSKRBSW8cB9geB5Fhdi/6tT+tkfgxjyxLY/j5YNGnRE9eFicrrTr5IhojxRKMpvDPy6bPdRQNV8NUapDfxbths32spicrHjzpwnddp3EE+6vnPoz1iS9WkY+E3IpgxeOqp2Fd5BdIaHkM/CPqdEor5ys3S4HOmyg/blX+31ZugbIPcEW5uBLNCOdhfpWzxEwG7mYO9vZ/rzbBpqA2UfEr9O6hCBHwhlOjY17ie1cQ==";
                if (TextUtils.isEmpty(decodeString)) {
                    Toast.makeText(mContext, "请先加密", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Toast.makeText(mContext, decodeString, Toast.LENGTH_SHORT).show();
                    byte[] decryptBytes = RSAUtils.decryptByPrivateKeyForSpilt(
                            Base64Decoder.decodeToBytes(decodeString), privateKey.getEncoded());

                    tvDecode.setText(new String(decryptBytes));
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
        }
    }
}

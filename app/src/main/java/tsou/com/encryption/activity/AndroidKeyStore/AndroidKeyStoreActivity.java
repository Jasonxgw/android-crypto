package tsou.com.encryption.activity.AndroidKeyStore;

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

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import tsou.com.encryption.R;
import tsou.com.encryption.aescbc.Base64Encoder;
import tsou.com.encryption.androidkeystoresign.KeyStoneUtils;

/**
 * Android AndroidKeyStore 对数据进行签名和验证
 */
public class AndroidKeyStoreActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText encryptionContext;
    private Button encryption;
    private TextView tvEncryption;
    private Button decode;
    private TextView tvDecode;
    private Activity mActivity;
    private Context mContext;
    private Button encryptionNotFixed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_or);
        mActivity = this;
        mContext = this;
        encryptionContext = (EditText) findViewById(R.id.et_encryption_context);
        encryption = (Button) findViewById(R.id.btn_encryption);
        encryptionNotFixed = (Button) findViewById(R.id.btn_encryption_not_fixed);
        tvEncryption = (TextView) findViewById(R.id.tv_encryption);
        decode = (Button) findViewById(R.id.btn_decode);
        tvDecode = (TextView) findViewById(R.id.tv_decode);
        encryption.setText("创建Key");
        encryptionNotFixed.setText("签名");
        decode.setText("验证");
        initListener();
    }

    private void initListener() {
        encryption.setOnClickListener(this);
        encryptionNotFixed.setOnClickListener(this);
        decode.setOnClickListener(this);
    }


    private String mSignatureStr = null;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            /**
             * 在应用安装后第一次运行时，生成一个随机密钥，并存入 KeyStore
             当你想存储一个数据，便从 KeyStore 中取出之前生成的随机密钥，对你的数据进行加密，
             加密完成后，已完成加密的数据可以随意存储在任意地方，比如 SharePreferences，
             此时即使它被他人读取到，也无法解密出你的原数据，因为他人取不到你的密钥
             当你需要拿到你的原数据，只需要从 SharePreferences 中读取你加密后的数据，
             并从 KeyStore 取出加密密钥，使用加密密钥对 “加密后的数据” 进行解密即可
             */
            case R.id.btn_encryption://创建Key,在项目中放在application或启动页中
                if (KeyStoneUtils.isHaveKeyStore()) {//是否有秘钥
                    Toast.makeText(mContext, "已经创建过秘钥", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    KeyStoneUtils.createKeys(mContext);
                    Toast.makeText(mContext, "秘钥创建成功", Toast.LENGTH_SHORT).show();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "不支持RSA", Toast.LENGTH_SHORT).show();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "没有提供：AndroidKeyStore", Toast.LENGTH_SHORT).show();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "无效的算法参数异常", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_encryption_not_fixed://签名
                String encryptionString = encryptionContext.getText().toString().trim();
                if (TextUtils.isEmpty(encryptionString)) {
                    Toast.makeText(mContext, "请输入签名内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!KeyStoneUtils.isHaveKeyStore()) {//是否有秘钥
                    Toast.makeText(mContext, "没有生成秘钥对,请先创建key", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    mSignatureStr = KeyStoneUtils.signData(encryptionString);
                    tvEncryption.setText(Base64Encoder.encode(mSignatureStr));
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "密钥存储库没有初始化,可能没有生成秘钥对", Toast.LENGTH_SHORT).show();
                } catch (CertificateException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "加载证书时发生错误", Toast.LENGTH_SHORT).show();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "不支持RSA", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "IO Exception", Toast.LENGTH_SHORT).show();
                } catch (UnrecoverableEntryException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "密钥对没有恢复", Toast.LENGTH_SHORT).show();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "无效的key", Toast.LENGTH_SHORT).show();
                } catch (SignatureException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "无效的签名", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_decode://验证

                String encryptionverifyString = encryptionContext.getText().toString().trim();

                if (TextUtils.isEmpty(encryptionverifyString)) {
                    Toast.makeText(mContext, "请输入验证内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mSignatureStr)) {
                    Toast.makeText(mContext, "请先签名", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    boolean b = KeyStoneUtils.verifyData(encryptionverifyString, mSignatureStr);
                    if (b) {
                        tvDecode.setText("签名一致");
                    } else {
                        tvDecode.setText("签名不一致");
                    }
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "密钥存储库没有初始化,可能没有生成秘钥对", Toast.LENGTH_SHORT).show();
                } catch (CertificateException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "加载证书时发生错误", Toast.LENGTH_SHORT).show();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "不支持RSA", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "IO Exception", Toast.LENGTH_SHORT).show();
                } catch (UnrecoverableEntryException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "密钥对没有恢复", Toast.LENGTH_SHORT).show();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "无效的key", Toast.LENGTH_SHORT).show();
                } catch (SignatureException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "无效的签名", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}

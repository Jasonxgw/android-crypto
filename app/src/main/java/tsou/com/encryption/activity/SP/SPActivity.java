package tsou.com.encryption.activity.SP;

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
import tsou.com.encryption.aescbc.Base64Encoder;
import tsou.com.encryption.sp.SPSecuredUtils;

public class SPActivity extends AppCompatActivity implements View.OnClickListener {

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
        initListener();
    }

    private void initListener() {
        encryption.setOnClickListener(this);
        decode.setOnClickListener(this);
    }

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_encryption://加密
                String encryptionString = encryptionContext.getText().toString().trim();
                if (TextUtils.isEmpty(encryptionString)) {
                    Toast.makeText(mContext, "请输入加密内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                SPSecuredUtils.put(mContext, "huangxiaoguo", encryptionString, publicKey);
//                SPSecuredUtils.put(mContext, "huangxiaoguo", 1, publicKey);
//                SPSecuredUtils.put(mContext, "huangxiaoguo", 0.01, publicKey);
//                SPSecuredUtils.put(mContext, "huangxiaoguo", true, publicKey);

                /**
                 * 下面是显示用的
                 */
                try {
                    byte[] encryptBytes = AndroidKeyStoreRSAUtils.encryptByPublicKey(encryptionString.getBytes(),
                            publicKey.getEncoded());
                    String encryStr = Base64Encoder.encode(encryptBytes);
                    tvEncryption.setText(encryStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_decode://解密
                String decodeString = tvEncryption.getText().toString().trim();
                if (TextUtils.isEmpty(decodeString)) {
                    Toast.makeText(mContext, "请先加密", Toast.LENGTH_SHORT).show();
                    return;
                }
                String huangxiaoguo = (String) SPSecuredUtils.get(mContext, "huangxiaoguo", "");
//                int huangxiaoguo = (int) SPSecuredUtils.get(mContext, "huangxiaoguo", 0);
//                double huangxiaoguo = (double) SPSecuredUtils.get(mContext, "huangxiaoguo", 0.0);
//                boolean huangxiaoguo = (boolean) SPSecuredUtils.get(mContext, "huangxiaoguo",false);
                tvDecode.setText(huangxiaoguo+"");
                break;
        }
    }


}

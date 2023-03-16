package tsou.com.encryption.activity.AES;

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

import tsou.com.encryption.R;
import tsou.com.encryption.aescbc.Base64Encoder;
import tsou.com.encryption.aesecb.AESUtils;
import tsou.com.encryption.jnimode.AESecb;

/**
 * ECB模式自设定秘钥
 */
public class AESECBActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText encryptionContext;
    private Button encryption;
    private TextView tvEncryption;
    private Button decode;
    private TextView tvDecode;
    private Activity mActivity;
    private Context mContext;
    private AESecb aeSecb;
    private String key;
    //    private String key = "huangxiaoguo1234";//必须16位

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
        aeSecb = new AESecb();
        key = aeSecb.getKey();
        initListener();
    }

    private byte[] encrypt;

    private void initListener() {
        encryption.setOnClickListener(this);
        decode.setOnClickListener(this);
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
                encrypt = AESUtils.encrypt(encryptionString.getBytes(), key.getBytes());
                tvEncryption.setText(Base64Encoder.encode(encrypt));
                break;
            case R.id.btn_decode://解密
                String decodeString = tvEncryption.getText().toString().trim();
                if (TextUtils.isEmpty(decodeString)) {
                    Toast.makeText(mContext, "请先加密", Toast.LENGTH_SHORT).show();
                    return;
                }
                byte[] decrypt = AESUtils.decrypt(encrypt, key.getBytes());
                tvDecode.setText(new String(decrypt));
                break;
        }
    }
}

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

import java.io.UnsupportedEncodingException;

import tsou.com.encryption.R;
import tsou.com.encryption.aescbc.AES;

/**
 * CBC模式自设定秘钥,需要设置偏移量
 * <p>
 * 什么是aes加密？
 * <p>
 * 高级加密标准（英语：Advanced Encryption Standard，缩写：AES），
 * 在密码学中又称Rijndael加密法，
 * 是美国联邦政府采用的一种区块加密标准。这个标准用来替代原先的DES，
 * 已经被多方分析且广为全世界所使用
 */
public class AESCBCActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText encryptionContext;
    private Button encryption;
    private TextView tvEncryption;
    private Button decode;
    private TextView tvDecode;
    private Activity mActivity;
    private Context mContext;

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_encryption://加密
                String encryptionString = encryptionContext.getText().toString().trim();
                if (TextUtils.isEmpty(encryptionString)) {
                    Toast.makeText(mContext, "请输入加密内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    String encrypt = AES.getInstance().encrypt(encryptionString.getBytes("UTF8"));

                    tvEncryption.setText(encrypt);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.btn_decode://解密
                String decodeString = tvEncryption.getText().toString().trim();
                if (TextUtils.isEmpty(decodeString)) {
                    Toast.makeText(mContext, "请先加密", Toast.LENGTH_SHORT).show();
                    return;
                }
                String decrypt = AES.getInstance().decrypt(decodeString);

                tvDecode.setText(decrypt);
                break;
        }
    }
}

package tsou.com.encryption.tink;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.Config;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadFactory;
import com.google.crypto.tink.aead.AeadKeyTemplates;
import com.google.crypto.tink.config.TinkConfig;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import tsou.com.encryption.R;

/**

 */
public class TinkActivity extends AppCompatActivity implements View.OnClickListener {


    private Activity mActivity;
    private Context mContext;
    private EditText et_encryption_context;
    private Button btn_aes128_gcm_encryption;
    private Button btn_aes128_gcm_encryption1;
    private TextView tv_aes128_gcm_encryption;
    private Button btn_aes128_gcm_decode;
    private TextView tv_aes128_gcm_decode;
    private Button btn_aes256_gcm_encryption;
    private TextView tv_aes256_gcm_encryption;
    private Button btn_aes256_gcm_decode;
    private TextView tv_aes256_gcm_decode;
    private static final byte[] EMPTY_ASSOCIATED_DATA = new byte[0];
    private static final byte[] EMPTY_ASSOCIATED_DATA_256 = new byte[0];
    private Aead primitive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tink);
        mActivity = this;
        mContext = this;
        try {
            Config.register(TinkConfig.TINK_1_0_0);

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        initView();
        initListener();
    }

    private void initView() {
        et_encryption_context = (EditText) findViewById(R.id.et_encryption_context);
        btn_aes128_gcm_encryption1 = (Button) findViewById(R.id.btn_aes128_gcm_encryption);
        tv_aes128_gcm_encryption = (TextView) findViewById(R.id.tv_aes128_gcm_encryption);
        btn_aes128_gcm_decode = (Button) findViewById(R.id.btn_aes128_gcm_decode);
        tv_aes128_gcm_decode = (TextView) findViewById(R.id.tv_aes128_gcm_decode);
        btn_aes256_gcm_encryption = (Button) findViewById(R.id.btn_aes256_gcm_encryption);
        tv_aes256_gcm_encryption = (TextView) findViewById(R.id.tv_aes256_gcm_encryption);
        btn_aes256_gcm_decode = (Button) findViewById(R.id.btn_aes256_gcm_decode);
        tv_aes256_gcm_decode = (TextView) findViewById(R.id.tv_aes256_gcm_decode);

    }

    private void initListener() {
        btn_aes128_gcm_encryption1.setOnClickListener(this);
        btn_aes128_gcm_decode.setOnClickListener(this);
        btn_aes256_gcm_encryption.setOnClickListener(this);
        btn_aes256_gcm_decode.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_aes128_gcm_encryption://AES128_GCM加密
                String encryptionString = et_encryption_context.getText().toString().trim();
                if (TextUtils.isEmpty(encryptionString)) {
                    Toast.makeText(mContext, "请输入加密内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    KeysetHandle keysetHandle = KeysetHandle.generateNew(
                            AeadKeyTemplates.AES128_GCM);
                    primitive = AeadFactory.getPrimitive(keysetHandle);
                    byte[] plaintext = encryptionString.getBytes("UTF-8");
                    byte[] ciphertext = primitive.encrypt(plaintext, EMPTY_ASSOCIATED_DATA);
                    tv_aes128_gcm_encryption.setText(base64Encode(ciphertext));
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                break;
            case R.id.btn_aes128_gcm_decode://AES128_GCM解密
                byte[] ciphertext = base64Decode(tv_aes128_gcm_encryption.getText().toString());
                try {
                    byte[] plaintext =primitive.decrypt(ciphertext, EMPTY_ASSOCIATED_DATA);
                    tv_aes128_gcm_decode.setText(new String(plaintext, "UTF-8"));
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_aes256_gcm_encryption://AES256_GCM加密
                String encryptionString1 = et_encryption_context.getText().toString().trim();
                if (TextUtils.isEmpty(encryptionString1)) {
                    Toast.makeText(mContext, "请输入加密内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    KeysetHandle keysetHandle = KeysetHandle.generateNew(
                            AeadKeyTemplates.AES256_GCM);
                    primitive = AeadFactory.getPrimitive(keysetHandle);
                    byte[] plaintext = encryptionString1.getBytes("UTF-8");
                    byte[] ciphertext1 = primitive.encrypt(plaintext, EMPTY_ASSOCIATED_DATA_256);
                    tv_aes256_gcm_encryption.setText(base64Encode(ciphertext1));
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_aes256_gcm_decode://AES256_GCM解密
                byte[] ciphertext1 = base64Decode(tv_aes256_gcm_encryption.getText().toString());
                try {
                    byte[] plaintext =primitive.decrypt(ciphertext1, EMPTY_ASSOCIATED_DATA_256);
                    tv_aes256_gcm_decode.setText(new String(plaintext, "UTF-8"));
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private static String base64Encode(final byte[] input) {
        return Base64.encodeToString(input, Base64.DEFAULT);
    }

    private static byte[] base64Decode(String input) {
        return Base64.decode(input, Base64.DEFAULT);
    }
}

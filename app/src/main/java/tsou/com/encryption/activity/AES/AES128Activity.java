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
import tsou.com.encryption.aes128.AlgorithmUtil;

/**
 * 自动生成base64密钥
 */
public class AES128Activity extends AppCompatActivity implements View.OnClickListener {

    private EditText encryptionContext;
    private Button encryption;
    private TextView tvEncryption;
    private Button decode;
    private TextView tvDecode;
    private Activity mActivity;
    private Context mContext;
    private byte[] encoded;

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
                    String hexKey = new AlgorithmUtil().getAESKey();
                    // 注意，这里的encoded是不能强转成string类型字符串的
                    encoded = AlgorithmUtil.getAESEncode(hexKey, encryptionString);
                    tvEncryption.setText(hexKey);
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
                try {
                    byte[] decoded = AlgorithmUtil.getAESDecode(decodeString, encoded);
                    tvDecode.setText(new String(decoded));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }
}

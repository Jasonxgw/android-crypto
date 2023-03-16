package tsou.com.encryption.activity.OR;

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
import tsou.com.encryption.aescbc.Base64Decoder;
import tsou.com.encryption.aescbc.Base64Encoder;
import tsou.com.encryption.or.OrUtils;

/**
 * 一、什么是异或加密？
 * <p>
 * 异或运算中，如果某个字符（或数值）x 与 一个数值m 进行异或运算得到y，则再用y 与 m 进行异或运算就可以还原为 x ，
 * 因此应用这个原理可以实现数据的加密解密功能。
 * <p>
 * 二、异或运算使用场景？
 * <p>
 * 两个变量的互换（不借助第三个变量）
 * <p>
 * 数据的简单加密解密
 */
public class ORActivity extends AppCompatActivity implements View.OnClickListener {

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
        initListener();
    }

    private void initListener() {
        encryption.setOnClickListener(this);
        encryptionNotFixed.setOnClickListener(this);
        decode.setOnClickListener(this);
    }

    private boolean isEncryption = true;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_encryption://固定加密
                String encryptionString = encryptionContext.getText().toString().trim();

                if (TextUtils.isEmpty(encryptionString)) {
                    Toast.makeText(mContext, "请输入加密内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                byte[] encode = OrUtils.encrypt(encryptionString.getBytes());
                tvEncryption.setText(Base64Encoder.encode(encode));
                isEncryption = true;
                break;

            case R.id.btn_encryption_not_fixed://不固定加密
                String encryptionStringNotFixed = encryptionContext.getText().toString().trim();
                if (TextUtils.isEmpty(encryptionStringNotFixed)) {
                    Toast.makeText(mContext, "请输入加密内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                byte[] encodeNotFixed = OrUtils.encode(encryptionStringNotFixed.getBytes());
                tvEncryption.setText(Base64Encoder.encode(encodeNotFixed));
                isEncryption = false;
                break;

            case R.id.btn_decode://解密

                String decodeString = tvEncryption.getText().toString().trim();
                if (TextUtils.isEmpty(decodeString)) {
                    Toast.makeText(mContext, "请先加密", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEncryption) {
                    byte[] encrypt = OrUtils.encrypt(Base64Decoder.decodeToBytes(decodeString));
                    tvDecode.setText(new String(encrypt));
                } else {
                    byte[] decode = OrUtils.decode(Base64Decoder.decodeToBytes(decodeString));
                    tvDecode.setText(new String(decode));
                }


                break;
        }
    }
}

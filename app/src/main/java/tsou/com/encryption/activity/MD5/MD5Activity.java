package tsou.com.encryption.activity.MD5;

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
import tsou.com.encryption.md5.MD5Utils;

/**
 * 一、什么是MD5加密？
 * <p>
 * MD5英文全称“Message-Digest Algorithm 5”，
 * 翻译过来是“消息摘要算法5”，
 * 由MD2、MD3、MD4演变过来的，
 * 是一种单向加密算法，是不可逆的一种的加密方式。
 * <p>
 * 二、MD5加密有哪些特点？
 * <p>
 * 压缩性：任意长度的数据，算出的MD5值长度都是固定的。
 * <p>
 * 容易计算：从原数据计算出MD5值很容易。
 * <p>
 * 抗修改性：对原数据进行任何改动，哪怕只修改1个字节，所得到的MD5值都有很大区别。
 * <p>
 * 强抗碰撞：已知原数据和其MD5值，想找到一个具有相同MD5值的数据（即伪造数据）是非常困难的。
 * <p>
 * 三、MD5应用场景：
 * <p>
 * 一致性验证
 * <p>
 * 数字签名
 * <p>
 * 安全访问认证
 */
public class MD5Activity extends AppCompatActivity implements View.OnClickListener {

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
        decode.setVisibility(View.GONE);
        tvDecode.setVisibility(View.GONE);
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
                String encode = MD5Utils.md5(encryptionString);
                tvEncryption.setText(encode);

                break;
        }
    }
}

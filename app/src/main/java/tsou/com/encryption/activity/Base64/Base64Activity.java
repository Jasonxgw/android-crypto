package tsou.com.encryption.activity.Base64;

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
import tsou.com.encryption.base64.Base64Utils;

/**
 * 一、什么Base64算法？
 * <p>
 * Base64是网络上最常见的用于传输8Bit字节代码的编码方式之一，
 * Base64并不是安全领域的加密算法，其实Base64只能算是一个编码算法，
 * 对数据内容进行编码来适合传输。标准Base64编码解码无需额外信息即完全可逆，
 * 即使你自己自定义字符集设计一种类Base64的编码方式用于数据加密，
 * 在多数场景下也较容易破解。Base64编码本质上是一种将二进制数据转成文本数据的方案。
 * 对于非二进制数据，是先将其转换成二进制形式，然后每连续6比特（2的6次方=64）
 * 计算其十进制值，根据该值在A--Z,a--z,0--9,+,/ 这64个字符中找到对应的字符，
 * 最终得到一个文本字符串。基本规则如下几点：
 * <p>
 * 1. 标准Base64只有64个字符（英文大小写、数字和+、/）以及用作后缀等号；
 * <p>
 * 2. Base64是把3个字节变成4个可打印字符，
 * 所以Base64编码后的字符串一定能被4整除（不算用作后缀的等号）；
 * <p>
 * 3. 等号一定用作后缀，且数目一定是0个、1个或2个。这是因为如果原文长度不能被3整除，
 * Base64要在后面添加\0凑齐3n位。为了正确还原，添加了几个\0就加上几个等号。
 * 显然添加等号的数目只能是0、1或2；
 * <p>
 * 4. 严格来说Base64不能算是一种加密，只能说是编码转换。。
 */
public class Base64Activity extends AppCompatActivity implements View.OnClickListener {

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
                String encode = Base64Utils.encodeString(encryptionString);
                tvEncryption.setText(encode);

                break;
            case R.id.btn_decode://解密

                String decodeString = tvEncryption.getText().toString().trim();
                if (TextUtils.isEmpty(decodeString)) {
                    Toast.makeText(mContext, "请先加密", Toast.LENGTH_SHORT).show();
                    return;
                }
                String decode = Base64Utils.decodeString(decodeString);
                tvDecode.setText(decode);


                break;
        }
    }
}

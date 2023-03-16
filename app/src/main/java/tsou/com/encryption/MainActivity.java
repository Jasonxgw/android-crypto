package tsou.com.encryption;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tsou.com.encryption.activity.AES.AES128Activity;
import tsou.com.encryption.activity.AES.AESCBCActivity;
import tsou.com.encryption.activity.AES.AESECBActivity;
import tsou.com.encryption.activity.AndroidKeyStore.AndroidKeyStoreActivity;
import tsou.com.encryption.activity.AndroidKeyStoreRSA.AndroidKeyStoreRSAActivity;
import tsou.com.encryption.activity.Base64.Base64Activity;
import tsou.com.encryption.activity.DES.DESActivity;
import tsou.com.encryption.activity.MD5.MD5Activity;
import tsou.com.encryption.activity.OR.ORActivity;
import tsou.com.encryption.activity.RSA.RSAAsymmetricActivity;
import tsou.com.encryption.activity.SP.SPActivity;
import tsou.com.encryption.tink.TinkActivity;

public class MainActivity extends AppCompatActivity {

    private ListView listview;
    private List<String> titles = new ArrayList<>();
    private Activity mActivity;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        mContext = this;
        listview = (ListView) findViewById(R.id.listview);
        titles.add("Android 对称AES128加密解密");
        titles.add("Android 对称AESECB加密解密");
        titles.add("Android 对称AESCBC加密解密");
        titles.add("Android 非对称RSA加密解密");
        titles.add("Android 对称DES加密解密");
        titles.add("Android MD5加密");
        titles.add("Android Base64加密解密");
        titles.add("Android 异或加密解密");
        titles.add("Android sharedpreferences 数据进行加密");
        titles.add("Android AndroidKeyStore 对数据进行签名和验证");
        titles.add("Android AndroidKeyStore 非对称RSA加密解密");
        titles.add("google  tink加密解密");
        listview.setAdapter(new myAdapter());
        initListener();

    }

    private void initListener() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0://AES128加密解密
                        startActivity(new Intent(mContext, AES128Activity.class));
                        break;
                    case 1://AESECB加密解密
                        startActivity(new Intent(mContext, AESECBActivity.class));
                        break;
                    case 2://AESCBC加密解密
                        startActivity(new Intent(mContext, AESCBCActivity.class));
                        break;
                    case 3://非对称RSA加密解密
                        startActivity(new Intent(mContext, RSAAsymmetricActivity.class));
                        break;
                    case 4://对称DES加密解密
                        startActivity(new Intent(mContext, DESActivity.class));
                        break;
                    case 5://MD5加密
                        startActivity(new Intent(mContext, MD5Activity.class));
                        break;
                    case 6://Base64加密解密
                        startActivity(new Intent(mContext, Base64Activity.class));
                        break;
                    case 7://异或加密解密
                        startActivity(new Intent(mContext, ORActivity.class));
                        break;
                    case 8://Android sharedpreferences 数据进行加密
                        startActivity(new Intent(mContext, SPActivity.class));
                        break;
                    case 9://Android AndroidKeyStore 对数据进行签名和验证
                        startActivity(new Intent(mContext,AndroidKeyStoreActivity.class));
                        break;
                    case 10://Android AndroidKeyStore 非对称RSA加密解密
                        startActivity(new Intent(mContext, AndroidKeyStoreRSAActivity.class));
                        break;
                    case 11://google  tink加密解密
                        startActivity(new Intent(mContext, TinkActivity.class));
                        break;
                }
            }
        });
    }

    private class myAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view = null;
            ViewHolder holder = null;
            if (convertView == null) {
                view = View.inflate(mContext, R.layout.textview_context, null);
                holder = new myAdapter.ViewHolder(view);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }
            holder.textview.setText(titles.get(position));
            return view;
        }

        class ViewHolder {
            TextView textview;

            ViewHolder(View view) {
                textview = view.findViewById(R.id.textview);
            }
        }
    }
}

package com.example.hi2.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.hi2.utils.LocalUserInfo;

/**
 * Created by Administrator on 2016/1/28.
 */
public class SetActivity extends Activity {
    private final static String TAG = "SetActivity";
    private EditText address;
    private EditText port;
    private Button save;
    private static String ADDRESS = "address";
    private static String PORT = "port";
    private LocalUserInfo localUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        address = (EditText) this.findViewById(R.id.address);
        port = (EditText) this.findViewById(R.id.port);
        save = (Button) this.findViewById(R.id.btn_save);

        localUserInfo = LocalUserInfo.getInstance(this);
        address.setText(localUserInfo.getUserInfo(ADDRESS));
        port.setText(localUserInfo.getUserInfo(PORT));
        address.addTextChangedListener(new TextChange());
        port.addTextChangedListener(new TextChange());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localUserInfo.setUserInfo(ADDRESS, address.getText().toString().trim());
                localUserInfo.setUserInfo(PORT, port.getText().toString().trim());
                startActivity(new Intent(SetActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    /**
     * EditText¼àÌýÆ÷
     */
    private class TextChange implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean flag1 = address.getText().length()>0;
            boolean flag2 = port.getText().length()>0;
            if(flag1 && flag2){
                save.setTextColor(0xFFFFFFFF);
                save.setEnabled(true);
            }else {
                save.setTextColor(0xFFD0EFC6);
                save.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public void back(View view) {
        Log.d(TAG, "start to back");
        finish();
    }
}

package com.example.hi2.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.hi2.Hi2Application;
import com.example.hi2.utils.FileService;
import com.example.hi2.utils.SmackClient;

/**
 * Created by Administrator on 2016/1/28.
 */
public class LoginActivity extends Activity {

    private final static String TAG = "LoginActivity";
    private EditText user;
    private EditText password;
    private Button login;
    private Button register;
    private ImageView iv_set;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = (EditText) findViewById(R.id.et_usertel);
        password = (EditText) findViewById(R.id.et_password);
        login = (Button) findViewById(R.id.btn_login);
        register = (Button) findViewById(R.id.btn_qtlogin);
        iv_set = (ImageView) findViewById(R.id.iv_set);
        dialog = new ProgressDialog(LoginActivity.this);

        user.addTextChangedListener(new TextChange());
        password.addTextChangedListener(new TextChange());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hi2Application.getInstance().setUsernamePassword(user.getText().toString().trim(), password.getText().toString().trim());

                dialog.setMessage("loading...");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();

                MyTask myTask = new MyTask();
                myTask.execute();
            }
        });
        iv_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SetActivity.class));
                finish();
            }
        });
    }

    /**
     * EditText������
     */
    private class TextChange implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean flag1 = user.getText().length()>0;
            boolean flag2 = password.getText().length()>0;
            if(flag1 && flag2){
                login.setTextColor(0xFFFFFFFF);
                login.setEnabled(true);
            }else {
                login.setTextColor(0xFFD0EFC6);
                login.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private class MyTask extends AsyncTask<String, Integer, String>{
        private boolean flag;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "start to login");
        }

        @Override
        protected String doInBackground(String... params) {
            flag = Hi2Application.getInstance().login();

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if(flag){
                Hi2Application.getInstance().addPacketListener();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }else {
                startActivity(new Intent(LoginActivity.this, SetActivity.class));
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}

package com.example.hi2.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

/**
 * Created by Administrator on 2016/2/2.
 */
public class FXAlertDialog extends Activity {
    private TextView mTextView;
    private Button mButton;
    private int position;
    private ImageView imageView;
    private EditText editText;
    private boolean isEditextShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog);
        mTextView = (TextView) findViewById(R.id.title);
        mButton = (Button) findViewById(R.id.btn_cancel);
        imageView = (ImageView) findViewById(R.id.image);
        editText = (EditText) findViewById(R.id.edit);
        //��ʾ����
        String msg = getIntent().getStringExtra("msg");
        //��ʾ����
        String title = getIntent().getStringExtra("title");
        position = getIntent().getIntExtra("position", -1);
        //�Ƿ���ʾȡ������
        boolean isCanceTitle=getIntent().getBooleanExtra("titleIsCancel", false);
        //�Ƿ���ʾȡ����ť
        boolean isCanceShow = getIntent().getBooleanExtra("cancel", false);
        //�Ƿ���ʾ�ı��༭��
        isEditextShow = getIntent().getBooleanExtra("editTextShow",false);
        //ת�����Ƶ�ͼƬ��path
        String path = getIntent().getStringExtra("forwardImage");
        //
        String edit_text = getIntent().getStringExtra("edit_text");

        if(msg != null)
            ((TextView)findViewById(R.id.alert_message)).setText(msg);
        if(title != null)
            mTextView.setText(title);
        if(isCanceTitle){
            mTextView.setVisibility(View.GONE);
        }
        if(isCanceShow)
            mButton.setVisibility(View.VISIBLE);
//        if(path != null){
//            //�����ô�ͼ��û��ȥȡ����ͼ
//            if(!new File(path).exists())
//                path = DownloadImageTask.getThumbnailImagePath(path);
//            imageView.setVisibility(View.VISIBLE);
//            ((TextView)findViewById(R.id.alert_message)).setVisibility(View.GONE);
//            if(ImageCache.getInstance().get(path) != null){
//                imageView.setImageBitmap(ImageCache.getInstance().get(path));
//            }else{
//                Bitmap bm = ImageUtils.decodeScaleImage(path, 150, 150);
//                imageView.setImageBitmap(bm);
//                ImageCache.getInstance().put(path, bm);
//            }
//
//        }
        if(isEditextShow){
            editText.setVisibility(View.VISIBLE);
            editText.setText(edit_text);
        }
    }

    public void ok(View view){
        setResult(RESULT_OK,new Intent().putExtra("position", position).
                        putExtra("edittext", editText.getText().toString())
				/*.putExtra("voicePath", voicePath)*/);
        if(position != -1)
            ChatActivity.resendPos = position;
        finish();

    }

    public void cancel(View view){
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        finish();
        return true;
    }





}
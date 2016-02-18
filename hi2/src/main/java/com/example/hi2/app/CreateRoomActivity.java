package com.example.hi2.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.hi2.Hi2Application;
import com.example.hi2.utils.SmackClient;

/**
 * Created by Administrator on 2016/2/18.
 */
public class CreateRoomActivity extends Activity {
    private static final String TAG = "CreateRoomActivity";
    private SmackClient smackClient;
    private EditText chat_room_name;
    private EditText description;
    private EditText maxusers;
    private Button submit;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_room);
        smackClient = Hi2Application.getInstance().getSmackClient();
        chat_room_name = (EditText) this.findViewById(R.id.editText2);
        description = (EditText) this.findViewById(R.id.editText3);
        maxusers = (EditText) this.findViewById(R.id.editText4);
        submit = (Button) this.findViewById(R.id.button);
        cancel = (Button) this.findViewById(R.id.button2);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomName = chat_room_name.getText().toString().trim();
                String desc = description.getText().toString().trim();
                smackClient.createRoom(Hi2Application.getInstance().getUsername(), roomName, "123456", desc);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

package com.example.hi2.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.example.hi2.adapter.ExpressionAdapter;
import com.example.hi2.adapter.ExpressionPagerAdapter;
import com.example.hi2.adapter.MessageAdapter;
import com.example.hi2.db.HiMessage;
import com.example.hi2.utils.CommonUtils;
import com.example.hi2.utils.SmackClient;
import com.example.hi2.utils.SmileUtils;
import com.example.hi2.widget.ExpandGridView;
import com.example.hi2.widget.PasteEditText;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/1.
 */
public class ChatActivity extends Activity implements View.OnClickListener {
    private final static String TAG = "ChatActivity";

    private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
    public static final int REQUEST_CODE_CONTEXT_MENU = 3;
    private static final int REQUEST_CODE_MAP = 4;
    public static final int REQUEST_CODE_TEXT = 5;
    public static final int REQUEST_CODE_VOICE = 6;
    public static final int REQUEST_CODE_PICTURE = 7;
    public static final int REQUEST_CODE_LOCATION = 8;
    public static final int REQUEST_CODE_NET_DISK = 9;
    public static final int REQUEST_CODE_FILE = 10;
    public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
    public static final int REQUEST_CODE_PICK_VIDEO = 12;
    public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
    public static final int REQUEST_CODE_VIDEO = 14;
    public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
    public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
    public static final int REQUEST_CODE_SEND_USER_CARD = 17;
    public static final int REQUEST_CODE_CAMERA = 18;
    public static final int REQUEST_CODE_LOCAL = 19;
    public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
    public static final int REQUEST_CODE_GROUP_DETAIL = 21;
    public static final int REQUEST_CODE_SELECT_VIDEO = 23;
    public static final int REQUEST_CODE_SELECT_FILE = 24;
    public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;

    public static final int RESULT_CODE_COPY = 1;
    public static final int RESULT_CODE_DELETE = 2;
    public static final int RESULT_CODE_FORWARD = 3;
    public static final int RESULT_CODE_OPEN = 4;
    public static final int RESULT_CODE_DWONLOAD = 5;
    public static final int RESULT_CODE_TO_CLOUD = 6;
    public static final int RESULT_CODE_EXIT_GROUP = 7;

    public static final int CHATTYPE_SINGLE = 1;
    public static final int CHATTYPE_GROUP = 2;
    public static final int CHATTYPE_CHATROOM = 3;

    private static final String TYPE_RECEIVE = "receive";
    private static final String TYPE_SEND = "send";

    public static final String COPY_IMAGE = "EASEMOBIMG";
    private SmackClient smackClient;
    private HiMessage hiMessage;
    private View recordingContainer;
    private ImageView micImage;
    private TextView recordingHint;
    private ListView listView;
    private PasteEditText mEditTextContent;
    private View buttonSetModeKeyboard;
    private View buttonSetModeVoice;
    private View buttonSend;
    private View buttonPressToSpeak;
    // private ViewPager expressionViewpager;
    private LinearLayout emojiIconContainer;
    private LinearLayout btnContainer;

    private View more;
    private ClipboardManager clipboard;
    private ViewPager expressionViewpager;
    private InputMethodManager manager;
    private List<String> reslist;
    private Drawable[] micImages;
    private int chatType;
    private NewMessageBroadcastReceiver receiver;
    public static ChatActivity activityInstance = null;
    // ��˭������Ϣ
    private String toChatUsername;
    private MessageAdapter adapter;
    private File cameraFile;
    public static int resendPos;
    private PowerManager.WakeLock wakeLock;

    private ImageView iv_emoticons_normal;
    private ImageView iv_emoticons_checked;
    private RelativeLayout edittext_layout;
    private ProgressBar loadmorePB;
    private boolean isloading;
    private final int pagesize = 20;
    private boolean haveMoreData = true;
    private Button btnMore;
    public String playMsgId;

    String myUser = "";
    String myUserNick = "";
    String myUserAvatar = "";
    String toUserNick="";
    String toUserAvatar="";
    String toUser = "";
    // �������Ƭ
    String iamge_path = null;
    // ���ð�ť
    private ImageView iv_setting;
    private ImageView iv_setting_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        smackClient = new SmackClient();
        hiMessage = new HiMessage(this);

        myUser = "shnanyang";
        myUserNick = "shnanyang";
        myUserAvatar = "shnanyang";

        toUser = this.getIntent().getStringExtra("user");
        final String nick = this.getIntent().getStringExtra("nick");
        final String avatar = this.getIntent().getStringExtra("avatar");

        initView();
        setUpView();
    }

    private void initView(){
        recordingContainer = findViewById(R.id.recording_container);
        micImage = (ImageView) findViewById(R.id.mic_image);
        recordingHint = (TextView) findViewById(R.id.recording_hint);
        listView = (ListView) findViewById(R.id.list);

        mEditTextContent = (PasteEditText) findViewById(R.id.et_sendmessage);
        buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
        edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
        buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
        buttonSend = findViewById(R.id.btn_send);
        buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
        expressionViewpager = (ViewPager) findViewById(R.id.vPager);
        emojiIconContainer = (LinearLayout) findViewById(R.id.ll_face_container);
        btnContainer = (LinearLayout) findViewById(R.id.ll_btn_container);
        iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
        iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
        loadmorePB = (ProgressBar) findViewById(R.id.pb_load_more);
        btnMore = (Button) findViewById(R.id.btn_more);
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.INVISIBLE);
        more = findViewById(R.id.more);
        edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);

        // ������Դ�ļ�,����¼������ʱ
        micImages = new Drawable[] {
                getResources().getDrawable(R.drawable.record_animate_01),
                getResources().getDrawable(R.drawable.record_animate_02),
                getResources().getDrawable(R.drawable.record_animate_03),
                getResources().getDrawable(R.drawable.record_animate_04),
                getResources().getDrawable(R.drawable.record_animate_05),
                getResources().getDrawable(R.drawable.record_animate_06),
                getResources().getDrawable(R.drawable.record_animate_07),
                getResources().getDrawable(R.drawable.record_animate_08),
                getResources().getDrawable(R.drawable.record_animate_09),
                getResources().getDrawable(R.drawable.record_animate_10),
                getResources().getDrawable(R.drawable.record_animate_11),
                getResources().getDrawable(R.drawable.record_animate_12),
                getResources().getDrawable(R.drawable.record_animate_13),
                getResources().getDrawable(R.drawable.record_animate_14), };

        // ����list
        reslist = getExpressionRes(35);
        // ��ʼ������viewpager
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        views.add(gv1);
        views.add(gv2);
        expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));

        edittext_layout.requestFocus();

        mEditTextContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edittext_layout
                            .setBackgroundResource(R.drawable.input_bar_bg_active);
                } else {
                    edittext_layout
                            .setBackgroundResource(R.drawable.input_bar_bg_normal);
                }
            }
        });
        mEditTextContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edittext_layout
                        .setBackgroundResource(R.drawable.input_bar_bg_active);
                more.setVisibility(View.GONE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.GONE);
            }
        });
        // �������ֿ�
        mEditTextContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!TextUtils.isEmpty(s)) {
                    btnMore.setVisibility(View.GONE);
                    buttonSend.setVisibility(View.VISIBLE);
                } else {
                    btnMore.setVisibility(View.VISIBLE);
                    buttonSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setUpView(){
        activityInstance = this;
        iv_emoticons_normal.setOnClickListener(this);
        iv_emoticons_checked.setOnClickListener(this);
        // position = getIntent().getIntExtra("position", -1);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
                .newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
        // �жϵ��Ļ���Ⱥ��
        chatType = getIntent().getIntExtra("chatType", CHATTYPE_SINGLE);
        // type=getIntent().getIntExtra("type", 0);

        if (chatType == CHATTYPE_SINGLE) { // ����
            toChatUsername = getIntent().getStringExtra("user");
            String toChatUserNick = getIntent().getStringExtra("user");
            ((TextView) findViewById(R.id.name)).setText(toChatUserNick);
            toUserNick=getIntent().getStringExtra("user");
            toUserAvatar=getIntent().getStringExtra("user");
        } else {
            findViewById(R.id.container_voice_call).setVisibility(View.GONE);
            toChatUsername = getIntent().getStringExtra("user");
            String groupName = getIntent().getStringExtra("user");
            ((TextView) findViewById(R.id.name)).setText(groupName);
        }

        adapter = new MessageAdapter(this, "sh", myUser);
        // ��ʾ��Ϣ
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new ListScrollListener());
        int count = listView.getCount();
        if (count > 0) {
            listView.setSelection(count - 1);
        }

        listView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                more.setVisibility(View.GONE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.GONE);
                return false;
            }
        });

        // ע�������Ϣ�㲥
        receiver = new NewMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter("message");
        // ���ù㲥�����ȼ������Mainacitivity,���������Ϣ����ʱ��������chatҳ�棬ֱ����ʾ��Ϣ����������ʾ��Ϣδ��
        intentFilter.setPriority(5);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 100){
            finish();
        }

        if(resultCode == RESULT_CODE_EXIT_GROUP){
            setResult(RESULT_OK);
            finish();
            return;
        }
        if(resultCode == REQUEST_CODE_CONTEXT_MENU){
            switch (resultCode){
                case RESULT_CODE_COPY: // ������Ϣ
                    Map<String, String> copyMsg = adapter.getItem(data
                            .getIntExtra("position", -1));
                    // clipboard.setText(SmileUtils.getSmiledText(ChatActivity.this,
                    // ((TextMessageBody) copyMsg.getBody()).getMessage()));
                    clipboard.setText(copyMsg.get("text"));
                    break;
                case RESULT_CODE_DELETE: // ɾ����Ϣ
                    Map<String, String> deleteMsg = adapter.getItem(data
                            .getIntExtra("position", -1));
                    hiMessage.removeMessage(deleteMsg);
                    adapter.refresh();
                    listView.setSelection(data.getIntExtra("position",
                            adapter.getCount()) - 1);
                    break;

                case RESULT_CODE_FORWARD: // ת����Ϣ
                    Map<String, String> forwardMsg = adapter.getItem(data
                            .getIntExtra("position", -1));
                    Intent intent = new Intent(this, ForwardMessageActivity.class);
                    intent.putExtra("forward_msg_id", forwardMsg.get("id"));
                    startActivity(intent);

                    break;

                default:
                    break;
            }
        }
        if(resultCode==RESULT_OK){
            if(requestCode==REQUEST_CODE_EMPTY_HISTORY){

            }else if(requestCode==REQUEST_CODE_CAMERA){//������Ƭ

            }else if(requestCode == REQUEST_CODE_SELECT_VIDEO){//���ͱ�����Ƶ

            } else if (requestCode == REQUEST_CODE_LOCAL){//���ͱ���ͼƬ

            } else if (requestCode == REQUEST_CODE_SELECT_FILE){//����ѡ����ļ�

            } else if (requestCode == REQUEST_CODE_MAP){//��ͼ

            } else if (requestCode == REQUEST_CODE_TEXT
                    || requestCode == REQUEST_CODE_VOICE
                    || requestCode == REQUEST_CODE_PICTURE
                    || requestCode == REQUEST_CODE_LOCATION
                    || requestCode == REQUEST_CODE_VIDEO
                    || requestCode == REQUEST_CODE_FILE){
                resendMessage();
            } else if (requestCode == REQUEST_CODE_COPY_AND_PASTE){//ճ��
                if (!TextUtils.isEmpty(clipboard.getText())) {
                    String pasteText = clipboard.getText().toString();
                }
            } else if (requestCode == REQUEST_CODE_ADD_TO_BLACKLIST){//���������

            } else if (hiMessage.getMsgCount(toUser, myUser) > 0){
                adapter.refresh();
                setResult(RESULT_OK);
            } else if (requestCode == REQUEST_CODE_GROUP_DETAIL){
                adapter.refresh();
            }
        }
    }

    /**
     * �ط���Ϣ
     */
    private void resendMessage() {
        adapter.refresh();
        listView.setSelection(resendPos);
    }

    /**
     * listview��������listener
     *
     */
    private class ListScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

        }

    }

    /**
     * ��Ϣ�㲥������
     *
     */
    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // �ǵðѹ㲥���ս��
            abortBroadcast();
            String from = intent.getStringExtra("from");
            String to = intent.getStringExtra("to");
            String type = intent.getStringExtra("type");
            Log.d(TAG, "-->> chat "+type+" data from " + from + " to " + to);
            adapter.refresh();
            listView.setSelection(listView.getCount() - 1);
        }
    }

    /**
     * ��Ϣͼ�����¼�
     *
     * @param view
     */
    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.btn_send) {// ������Ͱ�ť(�����ֺͱ���)
            String s = mEditTextContent.getText().toString();
            sendText(s, toUser);
        } else if (id == R.id.btn_take_picture) {
            selectPicFromCamera();// �������ͼ��
        } else if (id == R.id.btn_picture) {
            selectPicFromLocal(); // ���ͼƬͼ��
        } else if (id == R.id.btn_location) { // λ��
            startActivityForResult(new Intent(this, BaiduMapActivity.class),
                    REQUEST_CODE_MAP);
        } else if (id == R.id.iv_emoticons_normal) { // �����ʾ�����
            more.setVisibility(View.VISIBLE);
            iv_emoticons_normal.setVisibility(View.INVISIBLE);
            iv_emoticons_checked.setVisibility(View.VISIBLE);
            btnContainer.setVisibility(View.GONE);
            emojiIconContainer.setVisibility(View.VISIBLE);
            hideKeyboard();
        } else if (id == R.id.iv_emoticons_checked) { // ������ر����
            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.INVISIBLE);
            btnContainer.setVisibility(View.VISIBLE);
            emojiIconContainer.setVisibility(View.GONE);
            more.setVisibility(View.GONE);

        } else if (id == R.id.btn_video) {
            // �������ͼ��
            Intent intent = new Intent(ChatActivity.this,
                    ImageGridActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
        } else if (id == R.id.btn_file) { // ����ļ�ͼ��
            selectFileFromLocal();
        } else if (id == R.id.btn_voice_call) { // ��������绰ͼ��
            Toast.makeText(this, "can not voice", Toast.LENGTH_SHORT)
                        .show();
        }
    }

    /**
     * ����
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
        // ע���㲥
        try {
            unregisterReceiver(receiver);
            receiver = null;
        } catch (Exception e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wakeLock.isHeld())
            wakeLock.release();
    }

    @Override
    public void onBackPressed() {
        if (more.getVisibility() == View.VISIBLE) {
            more.setVisibility(View.GONE);
            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.INVISIBLE);
        } else {
            finish();
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // ���notification bar��������ҳ�棬��ֻ֤��һ������ҳ��
        String username = intent.getStringExtra("user");
        if (toUser.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }
    }

    /**
     * ��ʾ����ͼ�갴ť
     *
     * @param view
     */
    public void setModeVoice(View view) {
        hideKeyboard();
        edittext_layout.setVisibility(View.GONE);
        more.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        buttonSetModeKeyboard.setVisibility(View.VISIBLE);
        buttonSend.setVisibility(View.GONE);
        btnMore.setVisibility(View.VISIBLE);
        buttonPressToSpeak.setVisibility(View.VISIBLE);
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.INVISIBLE);
        btnContainer.setVisibility(View.VISIBLE);
        emojiIconContainer.setVisibility(View.GONE);

    }

    /**
     * ��ʾ����ͼ��
     *
     * @param view
     */
    public void setModeKeyboard(View view) {
        // mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener()
        // {
        // @Override
        // public void onFocusChange(View v, boolean hasFocus) {
        // if(hasFocus){
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        // }
        // }
        // });
        edittext_layout.setVisibility(View.VISIBLE);
        more.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        buttonSetModeVoice.setVisibility(View.VISIBLE);
        // mEditTextContent.setVisibility(View.VISIBLE);
        mEditTextContent.requestFocus();
        // buttonSend.setVisibility(View.VISIBLE);
        buttonPressToSpeak.setVisibility(View.GONE);
        if (TextUtils.isEmpty(mEditTextContent.getText())) {
            btnMore.setVisibility(View.VISIBLE);
            buttonSend.setVisibility(View.GONE);
        } else {
            btnMore.setVisibility(View.GONE);
            buttonSend.setVisibility(View.VISIBLE);
        }

    }

    /**
     * ��ʾ������ͼ�갴ťҳ
     *
     * @param view
     */
    public void more(View view) {
        if (more.getVisibility() == View.GONE) {
            System.out.println("more gone");
            hideKeyboard();
            more.setVisibility(View.VISIBLE);
            btnContainer.setVisibility(View.VISIBLE);
            emojiIconContainer.setVisibility(View.GONE);
        } else {
            if (emojiIconContainer.getVisibility() == View.VISIBLE) {
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.VISIBLE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
            } else {
                more.setVisibility(View.GONE);
            }

        }

    }

    /**
     * ������������
     *
     * @param v
     */
    public void editClick(View v) {
        listView.setSelection(listView.getCount() - 1);
        if (more.getVisibility() == View.VISIBLE) {
            more.setVisibility(View.GONE);
            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * �����ȡͼƬ
     */
    public void selectPicFromCamera() {
        if (!CommonUtils.isExitsSdcard()) {
            Toast.makeText(getApplicationContext(), "SD�������ڣ���������",
                    Toast.LENGTH_SHORT).show();
            return;
        }

//        cameraFile = new File(PathUtil.getInstance().getImagePath(),
//                FangYuanApplication.getInstance().getUserName()
//                        + System.currentTimeMillis() + ".jpg");
//        cameraFile.getParentFile().mkdirs();
//        startActivityForResult(
//                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
//                        MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
//                REQUEST_CODE_CAMERA);
    }

    /**
     * ѡ���ļ�
     */
    private void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

    /**
     * ��ͼ���ȡͼƬ
     */
    public void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }

    /**
     * �����ı���Ϣ
     *
     * @param content
     *            message content
     *            boolean resend
     */
    private void sendText(String content, String toUser) {
        if (content.length() > 0) {
            Log.d(TAG, "-------->>>>>sent to user "+toUser);
            smackClient.sendMessage(content);
            listView.setSelection(listView.getCount() - 1);
            mEditTextContent.setText("");
            hiMessage.saveMessage(toUser.split("@")[0], myUser, content, TYPE_SEND);
            adapter.refresh();
            setResult(RESULT_OK);
        }
    }

    /**
     * ��ȡ�����gridview����view
     *
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = View.inflate(this, R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        if (i == 1) {

            List<String> list1 = reslist.subList(0, 20);
            list.addAll(list1);
        } else if (i == 2) {
            list.addAll(reslist.subList(20, reslist.size()));
        }
        list.add("delete_expression");
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this,
                1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String filename = expressionAdapter.getItem(position);
                try {
                    // ���������ɼ�ʱ���ſ��������
                    // ��ס˵���ɼ��������������
                    if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {

                        if (filename != "delete_expression") { // ����ɾ��������ʾ����
                            // �����õķ��䣬���Ի�����ʱ��Ҫ����SmileUtils�����
                            @SuppressWarnings("rawtypes")
                            Class clz = Class
                                    .forName("com.example.hi2.utils.SmileUtils");
                            Field field = clz.getField(filename);
                            mEditTextContent.append(SmileUtils.getSmiledText(
                                    ChatActivity.this, (String) field.get(null)));
                        } else { // ɾ�����ֻ��߱���
                            if (!TextUtils.isEmpty(mEditTextContent.getText())) {

                                int selectionStart = mEditTextContent
                                        .getSelectionStart();// ��ȡ����λ��
                                if (selectionStart > 0) {
                                    String body = mEditTextContent.getText()
                                            .toString();
                                    String tempStr = body.substring(0,
                                            selectionStart);
                                    int i = tempStr.lastIndexOf("[");// ��ȡ���һ�������λ��
                                    if (i != -1) {
                                        CharSequence cs = tempStr.substring(i,
                                                selectionStart);
                                        if (SmileUtils.containsKey(cs
                                                .toString()))
                                            mEditTextContent.getEditableText()
                                                    .delete(i, selectionStart);
                                        else
                                            mEditTextContent.getEditableText()
                                                    .delete(selectionStart - 1,
                                                            selectionStart);
                                    } else {
                                        mEditTextContent.getEditableText()
                                                .delete(selectionStart - 1,
                                                        selectionStart);
                                    }
                                }
                            }

                        }
                    }
                } catch (Exception e) {
                }

            }
        });
        return view;
    }

    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "ee_" + x;
            reslist.add(filename);
        }
        return reslist;
    }

    /**
     * ���������
     */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}

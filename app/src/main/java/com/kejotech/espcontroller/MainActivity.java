package com.kejotech.espcontroller;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {
    private TextView mTextViewReplyFromServer;
    private EditText mEditTextSendMessage;
    private Button buttonUp;
    private Button buttonDown;
    private Button buttonLeft;
    private Button buttonRight;
    private Button buttonPick;
    private MaterialButtonToggleGroup mode;
    private Handler stopHandler = new Handler();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        buttonUp = findViewById(R.id.button_up);
        buttonDown = findViewById(R.id.button_down);
        buttonLeft = findViewById(R.id.button_left);
        buttonRight = findViewById(R.id.button_right);
        buttonPick = findViewById(R.id.pick_trash);
        mode = findViewById(R.id.toggle);
        mTextViewReplyFromServer = findViewById(R.id.bin_status);

//        if (!buttonUp.isInTouchMode()
//                && !buttonDown.isInTouchMode()
//                && !buttonLeft.isInTouchMode()
//                && !buttonRight.isInTouchMode()){

        mStop.run();
//        }
        mode.check(R.id.button_manual);
        mode.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                switch (group.getCheckedButtonId()){
                    case R.id.button_auto:
                        if (isChecked) {
                            sendMessage("auto mode");
                            Toast.makeText(MainActivity.this, "auto mode", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case R.id.button_manual:
                        if (isChecked) {
                            sendMessage("manual mode");
                            Toast.makeText(MainActivity.this, "manual mode", Toast.LENGTH_SHORT).show();
                        }
                        break;


                }
            }
        });

        buttonUp.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 10);
                        stopHandler.removeCallbacks(mStop);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        mStop.run();
                        break;
                }

                return true;
            }

            final Runnable mAction = new Runnable() {
                @Override
                public void run() {
                    // Do continuous task.
                    Log.d(TAG, "run: pressed");
                    sendMessage("move forward");
                    mHandler.postDelayed(this, 10);
                }
            };
        });

        buttonDown.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        stopHandler.removeCallbacks(mStop);
                        mHandler.postDelayed(mAction, 10);

                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        mStop.run();
                        break;
                }

                return true;
            }

            final Runnable mAction = new Runnable() {
                @Override
                public void run() {
                    // Do continuous task.
                    Log.d(TAG, "run: pressed");
                    sendMessage("move backwards");
                    mHandler.postDelayed(this, 10);
                }
            };
        });

        buttonLeft.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        stopHandler.removeCallbacks(mStop);
                        mHandler.postDelayed(mAction, 10);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        mStop.run();
                        break;
                }

                return true;
            }

            final Runnable mAction = new Runnable() {
                @Override
                public void run() {
                    // Do continuous task.
                    Log.d(TAG, "run: pressed");
                    sendMessage("turn left");
                    mHandler.postDelayed(this, 10);
                }
            };
        });

        buttonRight.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        stopHandler.removeCallbacks(mStop);
                        mHandler.postDelayed(mAction, 10);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        mStop.run();
                        break;

                }

                return true;
            }

            final Runnable mAction = new Runnable() {
                @Override
                public void run() {
                    // Do continuous task.
                    Log.d(TAG, "run: pressed");
                    sendMessage("turn right");
                    mHandler.postDelayed(this, 10);
                }
            };
        });

        buttonPick.setOnClickListener(view -> {
            stopHandler.removeCallbacks(mStop);
            sendMessage("pick trash");
        });

    }


    private void sendMessage(final String message) {

        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {

            String string = "";

            @Override
            public void run() {
                try (DatagramSocket ds = new DatagramSocket()) {

                        // IP Address below is the IP address of that Device where server socket is opened.
                        InetAddress serverAddr = InetAddress.getByName("192.168.43.125");
                        DatagramPacket dp;
                        dp = new DatagramPacket(message.getBytes(), message.length(), serverAddr, 8888);
                        ds.send(dp);

                        byte[] lMsg = new byte[1000];
                        dp = new DatagramPacket(lMsg, lMsg.length);
                        ds.receive(dp);
                        string = new String(lMsg, 0, dp.getLength());


                } catch (IOException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mTextViewReplyFromServer.getText().toString().contains(string)
                                || string.trim().length() > 0){
                            mTextViewReplyFromServer.setText( "Device:\n" + string);
                        }else {
                            mTextViewReplyFromServer.setText( "Device:\n");
                        }
                    }
                });

            }
        });

        thread.start();
    }

    private final Runnable mStop = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "run: getting sensor value");
            sendMessage("stop");
            stopHandler.postDelayed(this,10);
        }
    };
}

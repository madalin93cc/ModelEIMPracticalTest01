package ro.pub.cs.systems.eim.practicaltest01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest01MainActivity extends AppCompatActivity {

    private EditText leftEditText = null;
    private EditText rightEditText = null;
    private Button leftButton = null;
    private Button rightButton = null;
    private Button switchButton = null;
    private boolean serviceStarted = false;
    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private IntentFilter intentFilter = new IntentFilter();

    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("[Message]", intent.getStringExtra("message"));
        }
    }

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button: {
                    Integer value = Integer.parseInt(leftEditText.getText().toString());
                    value++;
                    leftEditText.setText(value.toString());
                    break;
                }
                case R.id.button2: {
                    Integer value = Integer.parseInt(rightEditText.getText().toString());
                    value++;
                    rightEditText.setText(value.toString());
                    break;
                }
                case R.id.buttonSwitch: {
                    Intent intent = new Intent("ro.pub.cs.systems.eim.practicaltest01.PracticalTest01SecondaryActivity");
                    Integer nr = Integer.parseInt(leftEditText.getText().toString())+ Integer.parseInt(rightEditText.getText().toString());
                    intent.putExtra(Constants.TOTAL_CLICKS, nr.toString());
                    startActivityForResult(intent,1);
                    break;
                }
            }
            Integer sum = Integer.parseInt(leftEditText.getText().toString()) + Integer.parseInt(rightEditText.getText().toString());
            if (sum > Constants.limit && !serviceStarted) {
                Intent intent = new Intent(getApplicationContext(), PracticalTest01Service.class);
                intent.putExtra(Constants.NR_1, leftEditText.getText().toString());
                intent.putExtra(Constants.NR_2, rightEditText.getText().toString());
                getApplicationContext().startService(intent);
                serviceStarted = true;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1: {
                Toast.makeText(this, "The activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(Constants.TEXT_FIELD_LEFT, leftEditText.getText().toString());
        outState.putString(Constants.TEXT_FIELD_RIGHT, rightEditText.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_main);

//        initializari
        leftEditText = (EditText) findViewById(R.id.editText);
        rightEditText = (EditText) findViewById(R.id.editText2);
        leftButton = (Button) findViewById(R.id.button);
        rightButton = (Button) findViewById(R.id.button2);
        switchButton = (Button) findViewById(R.id.buttonSwitch);
//        listeneri butoane
        leftButton.setOnClickListener(buttonClickListener);
        rightButton.setOnClickListener(buttonClickListener);
        switchButton.setOnClickListener(buttonClickListener);

        for(int i = 0; i < Constants.actionTypes.length; i++) {
            intentFilter.addAction(Constants.actionTypes[i]);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //        restaurare stare
        leftEditText.setText(savedInstanceState.getString(Constants.TEXT_FIELD_LEFT));
        rightEditText.setText(savedInstanceState.getString(Constants.TEXT_FIELD_RIGHT));
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, PracticalTest01Service.class);
        stopService(intent);
        super.onDestroy();
    }
}

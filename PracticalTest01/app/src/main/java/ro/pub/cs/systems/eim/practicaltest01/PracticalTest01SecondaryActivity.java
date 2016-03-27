package ro.pub.cs.systems.eim.practicaltest01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PracticalTest01SecondaryActivity extends AppCompatActivity {
    private EditText editText = null;
    private Button buttonOk = null;
    private Button buttonCancel = null;
    private ButtonClickListener buttonClickListener = new ButtonClickListener();

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonOk: {
                    setResult(RESULT_OK, null);
                    break;
                }
                case R.id.buttonCancel: {
                    setResult(RESULT_CANCELED, null);
                    break;
                }
            }
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_secondary);

        editText = (EditText)findViewById(R.id.editText3);
        buttonOk = (Button)findViewById(R.id.buttonOk);
        buttonCancel = (Button) findViewById(R.id.buttonCancel);

        buttonOk.setOnClickListener(buttonClickListener);
        buttonCancel.setOnClickListener(buttonClickListener);

        Intent intent = getIntent();
        if (intent != null && intent.getExtras().containsKey(Constants.TOTAL_CLICKS)) {
            editText.setText(intent.getExtras().getString(Constants.TOTAL_CLICKS));
        }
    }
}

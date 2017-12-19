package com.bryanbeale.altest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int MAX_VALUE = -8192;
    private static final int MIN_VALUE = 8191;
    private static final int TRANS_VALUE = 8192;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView encodedText = (TextView) findViewById(R.id.textViewEncodedValue);
        final TextView decodedText = (TextView) findViewById(R.id.textViewDecodedValue);
        final EditText inputText = (EditText) findViewById(R.id.editTextInteger);
        Button executeButton = (Button) findViewById(R.id.buttonExecute);

        inputText.setFilters(new InputFilter[]{new InputFilterMinMax(MIN_VALUE, MAX_VALUE)});

        executeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputText.getText().length() <= 0){
                    encodedText.setText(encodeInteger(0));
                }
                else {
                    encodedText.setText(encodeInteger(Integer.parseInt(inputText.getText().toString())));
                }
            }
        });

    }

    private String encodeInteger(int input) {
        String bytes = String.format("%16s", Integer.toBinaryString( input + TRANS_VALUE)).replace(" ", "0");

        String byte1 = bytes.substring(0, 7);
        byte1 = Integer.toHexString(Integer.parseInt(byte1, 16));
        String byte2 = bytes.substring(8);

        return byte1 + " " + byte2;
    }
}

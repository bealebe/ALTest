package com.bryanbeale.altest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity
{

    private static final int MIN_VALUE = -8192;
    private static final int MAX_VALUE = 8191;
    private static final int TRANS_VALUE = 8192;
    private static final int EDGE_BIT_MASK = 129;
    private static final int LOW_ORDER_SIG_BIT = 7;
    private static final int RADIX_HEX = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText integerInput = (EditText) findViewById(R.id.editTextInteger);
        final EditText hexInputOne = setupHexText((EditText) findViewById(R.id.editTextHex1));
        final EditText hexInputTwo = setupHexText((EditText) findViewById(R.id.editTextHex2));
        final Button buttonEncode = (Button) findViewById(R.id.buttonEncode);
        final Button buttonDecode = (Button) findViewById(R.id.buttonDecode);

        integerInput.setFilters(new InputFilter[]{new InputFilterMinMax(MIN_VALUE, MAX_VALUE)});

        buttonEncode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (integerInput.getText().length() <= 0)
                {
                    buttonEncode.setText("Encoded " + encode(0));
                } else
                {
                    buttonEncode.setText("Encoded " + encode(Integer.parseInt(integerInput.getText().toString())));
                }
            }
        });

        buttonDecode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                String strInputOne = String.format("%2s", hexInputOne.getText().toString().replace(PrefixSpanner.PREFIX, "")).replace(" ", "0");
                String strInputTwo = String.format("%2s", hexInputTwo.getText().toString().replace(PrefixSpanner.PREFIX, "")).replace(" ", "0");

                buttonDecode.setText("Decoded " + decode(strInputOne, strInputTwo));

            }
        });
    }

    /**
     * Sets up an EditText to be used for inputting only Hexadecimal numbers
     *
     * @param pView - EditText to be set up.
     * @return
     */
    private EditText setupHexText(final EditText pView)
    {
        pView.setText(PrefixSpanner.PREFIX);
        pView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        pView.getText().setSpan(new PrefixSpanner(), 0, 0, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        pView.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (!s.toString().startsWith(PrefixSpanner.PREFIX))
                {
                    pView.setText(PrefixSpanner.PREFIX);
                    Selection.setSelection(pView.getText(), pView.getText().length());
                }
            }
        });

        return pView;
    }

    /**
     * Encodes an integer, according to the AL Algorithm.
     *
     * @param input - Integer to be encoded.
     * @return
     */
    private String encode(int input)
    {
        input += TRANS_VALUE;
        input <<= 1;

        if ((input & (1 << LOW_ORDER_SIG_BIT)) != 0)
        {
            input ^= EDGE_BIT_MASK;
        }

        return String.format("%4s", Integer.toHexString(input)).replace(" ", "0");
    }

    /**
     * Decodes 2 hexadecimal numbers according to the AL Algorithm.
     *
     * @param pInputOne - A 2 digit Hexadecimal number in a String format.
     * @param pInputTwo - A 2 digit Hexadecimal number in a String format.
     * @return
     */
    private String decode(String pInputOne, String pInputTwo)
    {
        String value = pInputOne + pInputTwo;
        int decodedValue = Integer.parseInt(value, RADIX_HEX);

        if (value.endsWith("F") || value.endsWith("f"))
        {
            decodedValue = decodedValue ^ 1 << LOW_ORDER_SIG_BIT;
        }

        decodedValue >>= 1;

        return String.valueOf(decodedValue - TRANS_VALUE);

    }
}

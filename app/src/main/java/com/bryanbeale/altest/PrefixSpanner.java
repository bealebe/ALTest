package com.bryanbeale.altest;

import android.text.Selection;
import android.text.SpanWatcher;
import android.text.Spannable;

/**
 * Created by Bryan Beale on 12/21/17.
 * <p>
 * This class is meant to be used for hexadecimal EditTexts.
 * It's purpose is to prevent the user from selecting the prefix of '0x' that is
 * pre-populated in the EditText.
 */

public class PrefixSpanner implements SpanWatcher
{
    public static final String PREFIX = "0x";


    @Override
    public void onSpanAdded(Spannable text, Object what, int start, int end)
    {

    }

    @Override
    public void onSpanRemoved(Spannable text, Object what, int start, int end)
    {

    }

    @Override
    public void onSpanChanged(Spannable text, Object what, int ostart, int oend, int nstart, int nend)
    {
        if (what == Selection.SELECTION_START)
        {
            if (nstart < PREFIX.length())
            {
                final int end = Math.max(PREFIX.length(), Selection.getSelectionEnd(text));
                Selection.setSelection(text, PREFIX.length(), end);
            }
        } else if (what == Selection.SELECTION_END)
        {
            final int start = Math.max(PREFIX.length(), Selection.getSelectionEnd(text));
            final int end = Math.max(start, nstart);
            if (end != nstart)
            {
                Selection.setSelection(text, start, end);
            }
        }
    }
}

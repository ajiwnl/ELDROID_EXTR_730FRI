package com.eldroidfri730.extr.utils;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;

public class TextUtil {
    public static SpannableString getUnderlinedText(String text) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        return spannableString;
    }
}

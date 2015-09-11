package com.example.vengatr.consumer_services_android_20.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by vengat.r on 9/9/2015.
 */
public class OmeleeTextView extends TextView {

    public OmeleeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/comic.ttf"));
    }
}

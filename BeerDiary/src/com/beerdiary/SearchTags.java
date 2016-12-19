package com.beerdiary;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SearchTags extends Activity {
	public void OnCreate(Bundle icicle) {
		super.onCreate(icicle);

		TextView textview = new TextView(this);
        textview.setText("This is the Beverage tab");
        setContentView(textview);

	}
}

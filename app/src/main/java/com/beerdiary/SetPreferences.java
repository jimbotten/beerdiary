package com.beerdiary;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class SetPreferences extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        final Spinner s = (Spinner) findViewById(R.id.spinner);
        final Button b = (Button) findViewById(R.id.pref_button);
        
        final SharedPreferences settings = getSharedPreferences("theme",0);
        int theme = settings.getInt("theme", 0);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.themes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setSelection(theme);
        
        b.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		  
        	      SharedPreferences.Editor editor = settings.edit();
        	      editor.putInt("theme", s.getSelectedItemPosition());

        	      editor.commit();
        	      finish();
        	      
        	}
        });
        
	}
}

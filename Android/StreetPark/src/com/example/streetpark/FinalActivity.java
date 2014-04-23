package com.example.streetpark;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class FinalActivity extends Activity {
	
	TextView textview2, textview4, textview6;
	TextView textview8, textview10;
	String parking_name="", parking_distance="", parking_arret="", arret_name="", arret_distance="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_final);
		
		textview2 = (TextView) findViewById(R.id.textView2);
		textview4 = (TextView) findViewById(R.id.textView4);
		textview6 = (TextView) findViewById(R.id.textView6);
		textview8 = (TextView) findViewById(R.id.textView8);
		textview10 = (TextView) findViewById(R.id.textView10);
		
		Bundle extras = getIntent().getExtras();
		 
        
		if (extras != null) {
		             
		     //---------Récupère informations------------
		     parking_name = extras.getString("parking_name");
		     parking_distance = extras.getString("parking_distance");
		     parking_arret = extras.getString("parking_arret");
		     arret_name = extras.getString("arret_name");
		     arret_distance = extras.getString("arret_distance");
		     Log.e("","");
		}
		
		textview2.setText(parking_name);
		textview4.setText(parking_distance);
		textview6.setText(parking_arret);
		textview8.setText(arret_name);
		textview10.setText(arret_distance);
}
}

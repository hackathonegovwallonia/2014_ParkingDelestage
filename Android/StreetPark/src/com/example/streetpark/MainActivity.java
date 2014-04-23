package com.example.streetpark;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;

public class MainActivity extends Activity implements OnClickListener{
	
	 // Create object of SharedPreferences.
    private SharedPreferences sharedPref;
    private String ip;
    String url;
	private static final String TAG_RESULT = "predictions";
	JSONObject json;
	JSONArray contacts = null;
	AutoCompleteTextView ed;
	String search_text;
	ArrayList<String> names;
	ArrayAdapter<String> adp;
	double longitude, latitude;
	Button button1;
	TimePicker timePicker;
	String hour;
	String minutes;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = 
			        new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			}

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		this.setTitle(String.format("BusPark"));	 
		
		ed=(AutoCompleteTextView)findViewById(R.id.AutoCompleteTextView01);
		
		ed.setOnItemClickListener(new OnItemClickListener() {

	        @Override
	        public void onItemClick(AdapterView<?> parent, View arg1, int pos,
	                long id) {
	        	   String geocode = ed.getText().toString();
				   url="https://maps.googleapis.com/maps/api/place/autocomplete/json?reference="+geocode+"&sensor=true&key=AIzaSyBvhQtiqBI6ckF7YAwdfzIc4H43jL4qpeg";
				   try {
					geoLocate();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				   Log.e("",(String.valueOf(latitude)));
				   Log.e("",(String.valueOf(longitude)));
				   Log.e("",url);
	        }        
	    });
		
		
		ed.setThreshold(0);
		names=new ArrayList<String>();
			ed.addTextChangedListener(new TextWatcher()
		  {

		   @Override
		public void afterTextChanged(Editable s)
		   {

		   }

		   @Override
		public void beforeTextChanged(CharSequence s, int start,
		    int count, int after)
		   {

		   }

		   @Override
		public void onTextChanged(CharSequence s, int start,
		    int before, int count)
		   {
			   
			   search_text= ed.getText().toString();
			   url="https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+search_text+"&location=50.467089,4.860707&radius=500&sensor=true&key=AIzaSyBvhQtiqBI6ckF7YAwdfzIc4H43jL4qpeg";
			   if(search_text.length()<=1){
				   names=new ArrayList<String>();
				   Log.d("URL",url);
					paserdata parse=new paserdata();
					parse.execute();
			   }
			 
		   }
		  });
	
			button1 = (Button) findViewById(R.id.button1);
			timePicker = (TimePicker) findViewById(R.id.timePicker1);
			hour = timePicker.getCurrentHour().toString();
			minutes = timePicker.getCurrentMinute().toString();
			// add click listener to Button "POST"
	        button1.setOnClickListener(this);
	}
	
	public void geoLocate() throws IOException{
		Geocoder geocoder = new Geocoder(this, Locale.getDefault());
		String geocode = ed.getText().toString();
		List<Address> addresses = geocoder.getFromLocationName(geocode, 1);
		Address address = addresses.get(0);
		longitude = address.getLongitude();
		latitude = address.getLatitude();
	}
	
	public class paserdata extends AsyncTask<Void, Integer, Void>{
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			
			JSONParser jParser = new JSONParser();

			// getting JSON string from URL
			 json = jParser.getJSONFromUrl(url.toString());
			if(json !=null)
			{
			try {
				// Getting Array of Contacts
				contacts = json.getJSONArray(TAG_RESULT);
				
				for(int i = 0; i < contacts.length(); i++){
					JSONObject c = contacts.getJSONObject(i);
					String description = c.getString("description");
					Log.d("description", description);
					names.add(description);
				
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			}
			
			return null;
		}
		
		
		@Override
		protected void onPostExecute(Void result) {
			adp=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,names);
			ed.setAdapter(adp);	
		}
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	@Override
	public void onResume(){
		
		super.onResume();
		
		sharedPref = getSharedPreferences("mypref", 0);
	    //now get Editor
	    SharedPreferences.Editor editor= sharedPref.edit();
	    
	    //put your constants value
	     editor.putString("http://172.17.127.214:8888/parkingRequest", ip);
	     
	   //commits your edits
	     editor.commit();
	     
	     String[] values = new String[] { "Namur", "Bruxelles", "Charleroi", "Liege" };
	     
	     Spinner lv = (Spinner)findViewById(R.id.spinner1);
	     ArrayAdapter<String> myarrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
	     lv.setAdapter(myarrayAdapter);
	     
	     String[] values2 = new String[] { "Quick mode", "Night mode", "Low price mode"};
	     
	     Spinner lv2 = (Spinner)findViewById(R.id.spinner2);
	     ArrayAdapter<String> myarrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values2);
	     lv2.setAdapter(myarrayAdapter2);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		try {
			RestRequest r = new RestRequest("http://172.17.127.214:8888/parkingRequest");
			String json = 
					"{\"long\":\"%s\",\"lat\":\"%s\",\"city\":\"%s\",\"option\":\"%s\",\"hour\":\"%s\",\"minutes\":\"%s\"}";
			String jsonFormatted = String.format(json, String.valueOf(longitude), String.valueOf(latitude), "Namur", "Quick mode", hour, 
					minutes);
			
			try {
				String response = r.post(jsonFormatted);
				Log.e("", response);
				
				String[] tab = response.split("\\},");
				String parking = tab[0];
				String arret=tab[1];

				String parking_name="";
				String parking_distance="";
				String parking_arret="";
				String arret_name="";
				String arret_distance="";
				for(String a : parking.split(",")){
				if (a.contains("name"))
				parking_name = a.replace("\"name\":\"", 
				"").replace("\"", "").replace("\n", "").replace("{parking:{", "").trim();
				if (a.contains("distancem"))
				parking_distance = a.replace("\"distancem\":\"", 
				"").replace("\"", "").trim();
				if (a.contains("arret"))
				parking_arret = a.replace("\"arret\":\"", 
				"").replace("\"", "").replace("\n", "").trim();
				}
				for(String a : arret.split(",")){
				if (a.contains("name"))
				arret_name = a.replace("\"name\":\"", "").replace("\"", 
				"").replace("\n", "").replace("arret:{", "").trim();
				if (a.contains("distancem"))
				arret_distance = a.replace("\"distancem\":\"", 
				"").replace("\"", "").replace("}}", "").trim();
				}
				
				Intent intent = new Intent(MainActivity.this, FinalActivity.class);
				intent.putExtra("parking_name", parking_name);
				intent.putExtra("parking_distance", parking_distance);
				intent.putExtra("parking_arret", parking_arret);
				intent.putExtra("arret_name", arret_name);
				intent.putExtra("arret_distance", arret_distance);
				
				startActivity(intent);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

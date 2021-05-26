package info.androidhive.jsonparsing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    private static String url = "http://api.geonames.org/countryInfoJSON?username=sidma";

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

        new GetContacts().execute();


    }

    public void clickEvent(View v)
    {
        Button b = (Button)v;
        Intent i = new Intent(MainActivity.this, ActivityTwo.class);

        TextView textView = null;
        ViewGroup row = (ViewGroup) v.getParent();
        TextView textView1 = (TextView) row.findViewById(R.id.mobile);

//        Log.e("mobile",(textView1.getText().toString()));

        i.putExtra("Population",(textView1.getText().toString()));

        for (int itemPos = 0; itemPos < row.getChildCount(); itemPos++) {
            View view = row.getChildAt(itemPos);
            if (view instanceof TextView) {

                textView = (TextView) view; //Found it!
//                Log.e("TAGA",(textView.getText().toString()));
                i.putExtra("Name",(textView.getText().toString()));

                break;
            }
        }

//        Log.e("TAGA",b.getText().toString());
//        Log.e("TAGA",b.getParent());
//        Log.e("TAGA",((TextView)findViewById(R.id.mobile)).getText().toString());


//        i.putExtra("Code",b.getText().toString().toLowerCase());
//        i.putExtra("Area",area_m);
//        i.putExtra("Population",population_m);
//        i.putExtra("Name",name_m);



        i.putExtra("Code",b.getText().toString().toLowerCase());
        i.putExtra("Area",((TextView)findViewById(R.id.email)).getText().toString());

        startActivity(i);


    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("geonames");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String name = c.getString("countryName");
                        String email = c.getString("areaInSqKm");
                        String address = c.getString("population");
                        String id = c.getString("countryCode").toLowerCase();

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("mobile", address);
                        contact.put("id",id);

                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }



        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, contactList,
                    R.layout.list_item, new String[]{"id","name", "email",
                    "mobile"}, new int[]{R.id.bt1,R.id.name,
                    R.id.email, R.id.mobile});

            lv.setAdapter(adapter);
        }

    }
}

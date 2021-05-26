package com.example.rss;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerCurrency;
    private Spinner spinnerCurrency1;

    private String from_cur="Australian Dollar";
    private String to_url="Australian Dollar";

    private String from_code_name="aud";

    String plan = "";


    TextView  mTextViewResult;


    //Full url
    //https://dop.fxexchangerate.com/rss.xml

    private String url_rss = ".fxexchangerate.com/rss.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.spinnerCurrency = (Spinner) findViewById(R.id.spinner_employee);
        this.spinnerCurrency1 = (Spinner) findViewById(R.id.spinner_employee1);

        Currrency[] currrencies = CurrencyDataUtils.getCurrency();

        // (@resource) android.R.layout.simple_spinner_item:
        //   The resource ID for a layout file containing a TextView to use when instantiating views.
        //    (Layout for one ROW of Spinner)
        ArrayAdapter<Currrency> adapter = new ArrayAdapter<Currrency>(this,
                android.R.layout.simple_spinner_item,
                currrencies);

        ArrayAdapter<Currrency> adapter1 = new ArrayAdapter<Currrency>(this,
                android.R.layout.simple_spinner_item,
                currrencies);

        // Layout for All ROWs of Spinner.  (Optional for ArrayAdapter).
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spinnerCurrency.setAdapter(adapter);
        this.spinnerCurrency1.setAdapter(adapter1);

        // When user select a List-Item.
        this.spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onItemSelectedHandler(parent, view, position, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        this.spinnerCurrency1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onItemSelectedHandler1(parent, view, position, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        public AsyncResponse delegate = null;

        public DownloadFileFromURL(AsyncResponse asyncResponse) {
            this.delegate = asyncResponse;
        }

        /**
         * Before starting background thread Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {

            String str = "";
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection con = url.openConnection();
                InputStream is = con.getInputStream();

                BufferedReader br = null;

                try {
                    br = new BufferedReader(new InputStreamReader(is));
                    String line = br.readLine();
                    Log.e("FACT CHECK", "I am here");
                    if (line != null) {
                        str += line;
                        line = br.readLine();
                    }
//                    Log.e("DATA", str);
                } finally {
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            return str;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String result) {
            delegate.onComplete(result);
        }
    }

    public interface AsyncResponse {
        void onComplete(String output);
    }

    public void clickEvent(View v)
    {
        new DownloadFileFromURL(
                new AsyncResponse() {


                    @Override
                    public void onComplete(String output) {
                        plan = output;
                    }
                }
        ).execute("https://"+from_code_name+url_rss);

        Log.e("URL","https://"+from_code_name+url_rss);

        if (from_cur.equals(to_url)) //Neu chung mot nuoc
        {
            Toast.makeText(getApplicationContext(), "Giá không đổi" ,Toast.LENGTH_SHORT).show();
        }else
        {
            Log.e("From:",from_cur);
            Log.e("To",to_url);

            Log.e("PLAN",plan);

            String myPattern = from_cur+" = (\\d+(?:\\.\\d+)?) "+to_url;
            Log.e("PAn",myPattern);
            Pattern p = Pattern.compile(myPattern);
            Matcher m = p.matcher(plan);

            EditText mEdit   = (EditText)findViewById(R.id.textviewinput);
            float number_entered = Float.parseFloat(mEdit.getText().toString());
            Log.e("Entered",String.valueOf(number_entered));
            setContentView(R.layout.activity_main);

            if(m.find()){
//                Log.e(" RESULT",m.group(1));
                float super_value = 0;
                super_value = Float.parseFloat(m.group(1).substring(1));
                Log.e("Float",String.valueOf(super_value));
                float result_final_f = number_entered * super_value;
                String m_result_xx = String.valueOf(result_final_f);
                Log.e("RESULT",m_result_xx);
                Toast.makeText(getApplicationContext(),number_entered+" "+from_cur+": "+ m_result_xx+" "+to_url ,Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Đang tính toán,vui lòng đợi" ,Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void clickEvent2(View v)
    {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position, long id) {
        Adapter adapter = adapterView.getAdapter();
        Currrency employee = (Currrency) adapter.getItem(position);
        from_cur = employee.getName();
        from_code_name = employee.getCountry_code();
    }

    private void onItemSelectedHandler1(AdapterView<?> adapterView, View view, int position, long id) {
        Adapter adapter = adapterView.getAdapter();
        Currrency employee = (Currrency) adapter.getItem(position);
        to_url = employee.getName();
    }

}
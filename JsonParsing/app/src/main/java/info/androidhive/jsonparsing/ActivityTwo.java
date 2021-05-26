package info.androidhive.jsonparsing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ActivityTwo extends Activity {

    private String url_picture ="https://dyclassroom.com/image/flags/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_view);

        final Intent iin= getIntent();


        //.load("https://dyclassroom.com/image/flags/al.png")
        ImageView  imageView = (ImageView) this.findViewById(R.id.image);

        Glide.with(ActivityTwo.this)
                .load(url_picture + iin.getExtras().getString("Code") + ".png")
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);




        Log.e("SDFKJDSJFSDFKJL",url_picture + iin.getExtras().getString("Code") + ".gif");
        Log.e("SDFKJDSJFSDFKJL",url_picture + iin.getExtras().getString("Name") + ".gif");
        Log.e("SDFKJDSJFSDFKJL",url_picture + iin.getExtras().getString("Area") + ".gif");



//        new DownloadImageTask((ImageView) findViewById(R.id.image))
//                .execute( url_picture + iin.getExtras().getString("Code") + ".gif");

        TextView mName = (TextView) findViewById(R.id.name_c);
        TextView mArea = (TextView) findViewById(R.id.area);
        TextView mPopulation = (TextView) findViewById(R.id.population);

        mName.setText("Country name: " + iin.getExtras().getString("Name"));
        mArea.setText("Square kilometers: " + iin.getExtras().getString("Area"));
        mPopulation.setText("Population : " + iin.getExtras().getString("Population"));

        Button closeButton = (Button) findViewById(R.id.bClose);
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();

            }
        });


    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}

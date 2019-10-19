package himankgupta.bustrackingsystem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import himankgupta.bustrackingsystem.modal.Bus_info_details_modal;

public class route_info_details extends AppCompatActivity {

    TextView tvnobuses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_info_details);

        tvnobuses = (TextView) findViewById(R.id.textViewNoRouteInfo);

        DisplayBuses dsi = new DisplayBuses();
        dsi.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    public class DisplayBuses extends AsyncTask<String,Void,String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(route_info_details.this);
            progressDialog.show();
            progressDialog.setMessage("Loading Data");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {

            InputStream is = null;
            Intent intent = getIntent();
            String selectedsource = intent.getStringExtra("selectedsource");
            String selecteddestination = intent.getStringExtra("selecteddestination");
            System.out.println("Selected Source:" + selectedsource);
            System.out.println("Selected Destination:" + selecteddestination);

            String response1 = null;
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("selectedsource",selectedsource));
            nameValuePairs.add(new BasicNameValuePair("selecteddestination",selecteddestination));

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://guptahimank01.000webhostapp.com/route_info_buses.php");
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();

                is = entity.getContent();

                StringWriter writer = new StringWriter();
                IOUtils.copy(is, writer, "UTF-8");
                response1 = writer.toString();
                System.out.println("Response:" + response1);


            } catch (ClientProtocolException e) {
                Log.e("ClientProtocol", "Log_tag");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("Log_tag", "IOException");
                e.printStackTrace();
            }

            return response1;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.cancel();

            if (result != "null") {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    JSONArray jsonArray = jsonObject.getJSONArray("details");
                    LinearLayout linearLayout = (LinearLayout)findViewById(R.id.ll2);
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject details = jsonArray.getJSONObject(i);

                        TextView textView = new TextView(route_info_details.this);
                        TextView textView2 = new TextView(route_info_details.this);
                        TextView textView3 = new TextView(route_info_details.this);
                        textView.setTextSize(20);
                        textView2.setTextSize(20);
                        textView3.setTextSize(20);
                        String sn="\nBus Number : ",at="Source : ",dt="Destination : ";

                        sn+=details.getString("bus_no");
                        at+=details.getString("start");
                        dt+=details.getString("end");

                        textView.setText(sn);
                        textView2.setText(at);
                        textView3.setText(dt);

                        linearLayout.addView(textView);
                        linearLayout.addView(textView2);
                        linearLayout.addView(textView3);
                        Log.e("test","sn"+details.getString("bus_no")+details.getString("start")+details.getString("end"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else{
                tvnobuses.setVisibility(View.VISIBLE);
            }


        }
    }

}

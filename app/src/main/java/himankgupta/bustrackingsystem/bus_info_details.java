package himankgupta.bustrackingsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import himankgupta.bustrackingsystem.adapter.Bus_info_details_adapter;
import himankgupta.bustrackingsystem.modal.Bus_info_details_modal;

public class bus_info_details extends AppCompatActivity {


    TextView tvbus_no,tvstart,tvend;
    TextView tvnoinfo;
    ListView lvdisplayinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_info_details);
        tvbus_no = (TextView) findViewById(R.id.textView4);
        tvstart = (TextView) findViewById(R.id.textView5);
        tvend = (TextView) findViewById(R.id.textView6);


        lvdisplayinfo = (ListView) findViewById(R.id.listViewDisplayBusInfo);
        tvnoinfo = (TextView) findViewById(R.id.textViewNoBusInfo);

        Intent intent = getIntent();
        String bus_no = intent.getStringExtra("bus_no");
        tvbus_no.setText(bus_no);

        DisplayBusInfo dsi=new DisplayBusInfo();
        dsi.execute(bus_no);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public class DisplayBusInfo extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(bus_info_details.this);
            progressDialog.show();
            progressDialog.setMessage("Loading Data");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {

            InputStream is = null;
            String bus_no=args[0];
            String response1 = null;
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("bus_no", bus_no));

            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://guptahimank01.000webhostapp.com/bus_info.php");
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
                List<Bus_info_details_modal> list = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                   // Bus_info_details_modal bus_info_details_modal = new Bus_info_details_modal();

                    JSONArray jsonArray = jsonObject.getJSONArray("details");
                    LinearLayout linearLayout = (LinearLayout)findViewById(R.id.ll1);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        //bus_info_details_modal = new Bus_info_details_modal();
                        JSONObject details = jsonArray.getJSONObject(i);


                        TextView textView = new TextView(bus_info_details.this);
                        TextView textView2 = new TextView(bus_info_details.this);
                        TextView textView3 = new TextView(bus_info_details.this);
                        textView.setTextSize(20);
                        textView2.setTextSize(20);
                        textView3.setTextSize(20);
                        String ss,es;
                        String sn="\nStop ID : ",at="Arrival Time : ",dt="Departure Time : ";
                        ss= details.getString("start");
                        tvstart.setText(ss);
                        es= details.getString("end");
                        tvend.setText(es);


                       sn+=details.getString("stop_no");
                       at+=details.getString("arrival_time");
                       dt+=details.getString("departure_time");

                       textView.setText(sn);
                       textView2.setText(at);
                       textView3.setText(dt);

                        linearLayout.addView(textView);
                        linearLayout.addView(textView2);
                        linearLayout.addView(textView3);
                        Log.e("test","sn"+details.getString("stop_no")+details.getString("arrival_time")+details.getString("departure_time"));
//                        list.add(bus_info_details_modal);
                    }


                    //Bus_info_details_adapter adapter = new Bus_info_details_adapter(bus_info_details.this, list);
                    //lvdisplayinfo.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                tvnoinfo.setVisibility(View.VISIBLE);
            }


        }
    }


}

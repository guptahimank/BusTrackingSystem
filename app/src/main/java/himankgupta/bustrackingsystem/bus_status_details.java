package himankgupta.bustrackingsystem;

import android.app.ProgressDialog;
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

import himankgupta.bustrackingsystem.modal.Bus_info_details_modal;

public class bus_status_details extends AppCompatActivity {

    TextView tvbus_no,tvstart,tvend,tvls,tvld,tvlt;
    TextView tvnoinfo;
    ListView lvdisplayinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_status_details);

        tvbus_no = (TextView) findViewById(R.id.textView13);
        tvstart = (TextView) findViewById(R.id.textView14);
        tvend = (TextView) findViewById(R.id.textView15);
        tvls = (TextView) findViewById(R.id.textView22);
        tvld = (TextView) findViewById(R.id.textView25);
        tvlt = (TextView) findViewById(R.id.textView28);

        lvdisplayinfo = (ListView) findViewById(R.id.listViewDisplayBusInfo);
        tvnoinfo = (TextView) findViewById(R.id.textViewNoBusInfo);

        Intent intent = getIntent();
        String bus_no = intent.getStringExtra("bus_no");
        tvbus_no.setText(bus_no);

        DisplayBusStatus dsi = new DisplayBusStatus();
        dsi.execute(bus_no);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public class DisplayBusStatus extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(bus_status_details.this);
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
                HttpPost httpPost = new HttpPost("http://guptahimank01.000webhostapp.com/bus_status.php");
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

                    JSONArray jsonArray = jsonObject.getJSONArray("details");
                    LinearLayout linearLayout = (LinearLayout)findViewById(R.id.ll1);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);

                        String ss,es,ls,ld,lt;

                        ss= details.getString("start");
                        tvstart.setText(ss);
                        es= details.getString("end");
                        tvend.setText(es);
                        ls= details.getString("stop_no");
                        tvls.setText(ls);
                        ld= details.getString("lastdate");
                        tvld.setText(ld);
                        lt= details.getString("lasttime");
                        tvlt.setText(lt);


                        Log.e("test","ss"+details.getString("start")+details.getString("end")+details.getString("stop_no")+details.getString("lastdatetime")+details.getString("lasttime"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                tvnoinfo.setVisibility(View.VISIBLE);
            }


        }
    }


}

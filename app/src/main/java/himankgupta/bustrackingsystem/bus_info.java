package himankgupta.bustrackingsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class bus_info extends AppCompatActivity {

    Button btsubmitbusinfo;
    EditText etbusnoinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_info);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        etbusnoinfo = (EditText) findViewById(R.id.busnoinfo);
        btsubmitbusinfo = (Button) findViewById(R.id.btsubmitbusinfo);

        btsubmitbusinfo.setOnClickListener(new View.OnClickListener() {
            InputStream is = null;
            @Override
            public void onClick(View view) {

                String bus_no = etbusnoinfo.getText().toString();

                if (bus_no.equals("")){
                    String msg = "Enter Valid Bus No.";
                    etbusnoinfo.setText("");
                    Toast.makeText(bus_info.this,msg,Toast.LENGTH_SHORT).show();
                }
                else {

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("bus_no", bus_no));

                    try {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost("http://guptahimank01.000webhostapp.com/bus_no_validity.php");
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        HttpResponse response = httpClient.execute(httpPost);
                        HttpEntity entity = response.getEntity();
                        is = entity.getContent();

                        StringWriter writer = new StringWriter();
                        IOUtils.copy(is, writer, "UTF-8");
                        String response1 = writer.toString();
                        System.out.println("Response:"+ response1);
                        try {
                            JSONObject jsonObject = new JSONObject(response1);
                            String msg= jsonObject.getString("query_result");
                            if(msg.equals("Welcome"))
                            {
                                Intent i=new Intent(bus_info.this,bus_info_details.class);
                                i.putExtra("bus_no",bus_no);
                                startActivity(i);

                            }
                            else if(msg.equals("Denied"))
                            {
                                Toast.makeText(bus_info.this, "Enter Valid Bus No.", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (ClientProtocolException e) {
                        Log.e("ClientProtocol", "Log_tag");
                        e.printStackTrace();
                    } catch (IOException e) {
                        Log.e("Log_tag", "IOException");
                        e.printStackTrace();
                    }


                }

            }
        });

    }

}


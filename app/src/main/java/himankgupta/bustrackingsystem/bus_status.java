package himankgupta.bustrackingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class bus_status extends AppCompatActivity {


    EditText etbusnostatus;
    Button btsubmitbusstatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_status);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        etbusnostatus = (EditText) findViewById(R.id.busnostatus);
        btsubmitbusstatus = (Button) findViewById(R.id.btsubmitbusstatus);

        btsubmitbusstatus.setOnClickListener(new View.OnClickListener() {
            InputStream is = null;
            @Override
            public void onClick(View view) {

                String bus_no = etbusnostatus.getText().toString();

                if (bus_no.equals("")) {
                    String msg = "Enter Valid Bus No.";
                    etbusnostatus.setText("");
                    Toast.makeText(bus_status.this, msg, Toast.LENGTH_SHORT).show();
                } else {

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
                                Intent i = new Intent(bus_status.this, bus_status_details.class);
                                i.putExtra("bus_no",bus_no);
                                startActivity(i);
                            }
                            else if(msg.equals("Denied"))
                            {
                                Toast.makeText(bus_status.this, "Enter Valid Bus No.", Toast.LENGTH_LONG).show();
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

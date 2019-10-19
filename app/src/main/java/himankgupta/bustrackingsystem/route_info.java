package himankgupta.bustrackingsystem;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.InputStream;

public class route_info extends AppCompatActivity {

    Spinner sspinner, dspinner;
    String source[] = {"Select Bus Stop", "1", "2", "3", "4", "5"};
    String destination[] = {"Select Bus Stop", "1", "2", "3", "4", "5"};
    String selectedsource, selecteddestination;
    Button btsubmitrouteinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_info);
        btsubmitrouteinfo = (Button) findViewById(R.id.btsubmitrouteinfo);

        sspinner = (Spinner) findViewById(R.id.sspinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, source);
        sspinner.setAdapter(adapter);

        dspinner = (Spinner) findViewById(R.id.dspinner);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, destination);
        dspinner.setAdapter(adapter1);


        sspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedsource = sspinner.getSelectedItem().toString();
                System.out.println("Selected Source:" + selectedsource);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selecteddestination = dspinner.getSelectedItem().toString();
                System.out.println("Selected Destination:" + selecteddestination);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btsubmitrouteinfo.setOnClickListener(new View.OnClickListener() {
            InputStream is = null;
            @Override
            public void onClick(View view) {
                if (selectedsource.equals("Select Bus Stop") || selecteddestination.equals("Select Bus Stop")) {
                    Toast.makeText(route_info.this, "Please select valid Stop", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent i=new Intent(route_info.this,route_info_details.class);
                    i.putExtra("selectedsource",selectedsource);
                    i.putExtra("selecteddestination",selecteddestination);
                    startActivity(i);
                }

            }
        });
    }
}

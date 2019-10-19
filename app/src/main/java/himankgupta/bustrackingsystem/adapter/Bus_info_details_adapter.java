package himankgupta.bustrackingsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import himankgupta.bustrackingsystem.R;
import himankgupta.bustrackingsystem.modal.Bus_info_details_modal;


public class Bus_info_details_adapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    List<Bus_info_details_modal> list = new ArrayList<>();

    public Bus_info_details_adapter(Context context,List<Bus_info_details_modal> list) {

        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        convertView = inflater.inflate(R.layout.display_inner_bus_info,null,false);
        TextView textViewStopNumber,textViewArrivalTime,textViewDepartureTime;
        textViewStopNumber = (TextView) convertView.findViewById(R.id.textViewStopNumber);
        textViewArrivalTime= (TextView) convertView.findViewById(R.id.textViewArrivalTime);
        textViewDepartureTime = (TextView) convertView.findViewById(R.id.textViewDepartureTime);
        textViewStopNumber.setText(list.get(position).getStop_no());
        textViewArrivalTime.setText(list.get(position).getArrival_time());
        textViewDepartureTime.setText(list.get(position).getDeparture_time());
        return convertView;
    }
}

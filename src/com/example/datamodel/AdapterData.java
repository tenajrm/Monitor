package com.example.datamodel;

import java.util.ArrayList;
import java.util.List;

import com.example.monitor.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AdapterData extends ArrayAdapter<SumaryDataContainer>{
	
	private Context context;
	private TextView title;
	private TextView magnitude;
	private TextView timestamp;
	private RelativeLayout item;
	
	
	private List<SumaryDataContainer> items = new ArrayList<SumaryDataContainer>();
	
	public AdapterData(Context context, int textViewResourceId,
			List<SumaryDataContainer> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.items = objects;
	}
	
	public int getCount() {
		return this.items.size();
	}

	public SumaryDataContainer getItem(int index) {
		return this.items.get(index);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			row = inflater.inflate(R.layout.adapter_list_item, parent, false);
		}
		// Get item
		SumaryDataContainer metadata = getItem(position);
		
		title = (TextView) row.findViewById(R.id.adapter_list_place);
		magnitude = (TextView) row.findViewById(R.id.adapter_list_magnitude);
		timestamp = (TextView) row.findViewById(R.id.adapter_list_timestamp);
		item = (RelativeLayout) row.findViewById(R.id.adapter_row);
		
		
		title.setText(metadata.getTitle());
		magnitude.setText(Double.toString(metadata.getMagnitude()));
		timestamp.setText(metadata.getTime());
		item.setBackgroundColor(metadata.getColor());

		return row;
	}
	
}

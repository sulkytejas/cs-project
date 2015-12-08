package bevy.mobile.android.pdiary;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class GridViewDayOfWeekAdapter extends BaseAdapter implements ListAdapter{

	private Context c;
	private int size;
	private static final String[] dayName = {"Sun", "Mon", "Tue", "Wed",
	"Thu", "Fri", "Sat"};
	public GridViewDayOfWeekAdapter(Context c){
		this.c = c;
		size = dayName.length;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			TextView v = new TextView(c);
			v.setText(dayName[position]);
			v.setTextColor(Color.BLACK);
			v.setGravity(Gravity.CENTER);
			return v;
		} else{
			return convertView;
		}
	} 

}

/*
Copyright 2004 The Apache Software Foundation

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Developed by : Sandeep Soni [ http://sonisandeep.blogspot.com, 
    			      Email : Sandeep.Soni at yahoo.com 
    			    ]
*/
package bevy.mobile.android.pdiary;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * @author Sandeep Soni
*/

public class GridViewDayAdapter extends BaseAdapter implements ListAdapter {

    private Context _context;
    private Calendar _cal;
    private PersonalDiaryDB _db;
    
    private List<String> _daysHavingEvents;
    SimpleDateFormat _sdf = new SimpleDateFormat();
    private String dateStr = "";
	public static int days_in_month[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
		31, 30, 31 };
    
    public GridViewDayAdapter(Context c, Date date, PersonalDiaryDB db) {
	_context = c;
	_cal = Calendar.getInstance();
	_cal.setTime(date);
	_db = db;
	_daysHavingEvents = _db.getDaysHavingEventsInMonth(_cal.getTime());
	_sdf.applyPattern("yyyy-MM-dd");
	
	Log.d("PDIARY", "Days having events : " + _daysHavingEvents);
    }

    @Override
    public int getCount() {
    	return getTotalDaysCount(_cal);
	//return _cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    @Override
    public Object getItem(int arg0) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public long getItemId(int position) {
	// TODO Auto-generated method stub
	return 0;
    }

    public View getViewd(int position, View convertView, ViewGroup parent) {
	TextView tv;

	if (convertView == null) {
	    tv = new TextView(_context);
	    tv.setText(position + "");

	} else {
	    tv = (TextView) convertView;
	}
	return tv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

	TextView tv;
	
	Calendar cal = new GregorianCalendar(_cal.get(Calendar.YEAR), _cal.get(Calendar.MONTH), 1);
	int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
	
	position = (position + 1) - (firstDayOfWeek - 1);
	if (convertView == null) {
	    tv = new TextView(_context);
	    tv.setGravity(Gravity.CENTER);
	    if (position < 1) {
	    	tv.setText("" + "");
	    } else {
	    	tv.setText(position + "");
	    }
	} else {
	    tv = (TextView) convertView;
	}
	tv.setBackgroundResource(R.drawable.grid);
	tv.setTextColor(Color.BLACK);
	if (dayHasEvents(position)) {
		tv.setBackgroundResource(R.drawable.grid_event);
	}

	tv.setMinLines(3);
	tv.setGravity(Gravity.CENTER);
	return tv;
    }

    private int getTotalDaysCount(Calendar cal) {
    	Calendar calForDayOne = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
    	int days = days_in_month[cal.get(Calendar.MONTH)];
    	if(cal.get(Calendar.YEAR)%4==0 && cal.get(Calendar.MONTH)==1){
    		days = 29;
    	}
    	int firstDayOfWeek = calForDayOne.get(Calendar.DAY_OF_WEEK);
    	return (days + (firstDayOfWeek -1)); 
    }

    private boolean dayHasEvents(int position) {
	String date = Utils.getStringFromDate(_cal.getTime());
	String toTest = date.replaceFirst("[0-9]{2}$", String.format("%02d",position));
	    
	if ( _daysHavingEvents.contains(toTest)) {
	    return true;
	} else {
	    return false;
	}
    }


}
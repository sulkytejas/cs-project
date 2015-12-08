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
package bevy.mobile.android.pdiary.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import bevy.mobile.android.pdiary.GridViewDayAdapter;
import bevy.mobile.android.pdiary.GridViewDayNotesAdapter;
import bevy.mobile.android.pdiary.GridViewDayOfWeekAdapter;
import bevy.mobile.android.pdiary.PersonalDiaryDB;
import bevy.mobile.android.pdiary.R;
import bevy.mobile.android.pdiary.Utils;
import bevy.mobile.android.pdiary.models.DiaryEntry;

/**
 * @author Sandeep Soni
*/

public class MainScreen extends Activity implements AdapterView.OnItemClickListener{

    private PersonalDiaryDB _db;
    private TextView tv;
    private Button mPrev;
    private Button mNext;
    private Calendar _cal;
    private String currentSelectedDateString = "";
    
    private static final String[] monthName = {"January", "February",
    		           "March", "April", "May", "June", "July",
    		            "August", "September", "October", "November",
    		            "December"};

    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
    private static final int ACTIVITY_VIEW_ALL=2;
    
    public static final int EDIT_ID = Menu.FIRST;
    public static final int HOME_ID = Menu.FIRST + 1;
    public static final int LIST_ID = Menu.FIRST + 3;
    public static final int DELETE_ID = Menu.FIRST + 4;
    public static final int ABOUT_ID = Menu.FIRST + 5;
    public static final int EXPORT_ID = Menu.FIRST + 6;
    public static final int IMPORT_ID = Menu.FIRST + 7;
    public static final int EMAIL_ID = Menu.FIRST + 8;
    
    Map<Long,Integer> idMap = new HashMap<Long,Integer>();
    int selectedItem = -1;
    GridView calView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	_db = new PersonalDiaryDB(this);
	
	setContentView(R.layout.main);
	_cal = Calendar.getInstance();
	mPrev = (Button) findViewById(R.id.prevMonthButton);
	mNext = (Button) findViewById(R.id.nextMonthButton);
	
	// add a click listener to the Previous button
    mPrev.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            _cal.add(Calendar.MONTH, -1);
        	System.out.println("Current time: " + _cal.getTime());
        	showContent(_cal);
        }
    });
    
    // add a click listener to the Next button
    mNext.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	_cal.add(Calendar.MONTH, +1);
        	System.out.println("Current time: " + _cal.getTime());
        	showContent(_cal);        	
        }
    });
    
    //showContent(_cal);
	}

	private void showContent(Calendar cal) {
		tv = (TextView) findViewById(R.id.thisMonth);
		
		String month = monthName[cal.get(Calendar.MONTH)];
		int year = cal.get(Calendar.YEAR);
		month = month + "  "+ year;
		tv.setText(month);
		tv.setTextColor(Color.BLACK);
		setUpDayView();
		setUpCalendar(cal);
	}

	private void setUpDayView() {
		GridView dayView = (GridView) findViewById(R.id.calDayGridView);
		GridViewDayOfWeekAdapter dayViewAdapter = new GridViewDayOfWeekAdapter(this);
		dayView.setAdapter(dayViewAdapter);
		dayView.setGravity(Gravity.CENTER);
	}
    private void setUpCalendar(Calendar cal) {
		calView = (GridView) findViewById(R.id.calGridView);
		GridViewDayAdapter calViewAdapter = new GridViewDayAdapter(this,cal.getTime(),_db);
		calView.setAdapter(calViewAdapter);
		calView.setClickable(true);
		calView.setFocusable(true);
		calView.setFastScrollEnabled(true);
		calView.setOnItemClickListener(this);
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	super.onCreateOptionsMenu(menu);
	menu.add(0, EDIT_ID, 0, R.string.menu_edit).setShortcut('1', 'e')
	    .setIcon(R.drawable.icon_delete);
	menu.add(0, HOME_ID, 0, R.string.menu_start).setShortcut('4', 'h')
	    .setIcon(R.drawable.icon_start);
	menu.add(0, LIST_ID, 0, R.string.menu_recent).setShortcut('3', 'r')
	    .setIcon(R.drawable.icon_recent);
	menu.add(0, DELETE_ID, 0, R.string.menu_delete).setShortcut('2', 'd')
	    .setIcon(R.drawable.icon_delete);
	menu.add(0, ABOUT_ID, 0, R.string.menu_about).setShortcut('5', 'a')
	    .setIcon(android.R.drawable.ic_dialog_info);
	menu.add(0, EXPORT_ID, 0, R.string.menu_export).setShortcut('7', 'x')
	    .setIcon(android.R.drawable.ic_dialog_info);
	menu.add(0, IMPORT_ID, 0, R.string.menu_import).setShortcut('8', 'm')
	    .setIcon(android.R.drawable.ic_dialog_info);	
	menu.add(0, EMAIL_ID, 0, R.string.menu_email).setShortcut('6', 'm')
	    .setIcon(android.R.drawable.ic_dialog_info);
	return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
        case EDIT_ID:
            createNote();
            return true;
        case LIST_ID:
        	listAllNotes();
        	return true;
        }
       
        return super.onMenuItemSelected(featureId, item);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if(_db != null){
        	_db.close();
        }
        _db = new PersonalDiaryDB(this);
        showContent(_cal);
        showNotesContent(currentSelectedDateString);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(_db != null){
        	_db.close();
        }
    }
    
    private void createNote() {
        Intent i = new Intent(this, NoteEditActivity.class);
        startActivityForResult(i, ACTIVITY_EDIT);
    }
    
    private void listAllNotes(){
    	Intent i = new Intent(this, ListAllNotesActivity.class);
    	startActivityForResult(i, ACTIVITY_VIEW_ALL);
    }
    
    private void showNotesContent(String dateString){
    	
		Date date = null;
		currentSelectedDateString=dateString;
		if(currentSelectedDateString != null && !currentSelectedDateString.equals("")){
			GridView notesView = (GridView) findViewById(R.id.notesGridView);
			final Context c = this;
			GridViewDayNotesAdapter notesAdapter = new GridViewDayNotesAdapter(c,dateString, _db, notesView, calView);
		}
    }

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2,
			long arg3) {
		TextView textView = (TextView)v;
		String date = Utils.getStringFromDate(_cal.getTime());
		int position = new Integer(textView.getText().toString()).intValue();
		if (position > 0) {
		String dateStr = date.replaceFirst("[0-9]{2}$", String.format("%02d",position));
		showContent(_cal);
		showNotesContent(dateStr);
		}
	}

}

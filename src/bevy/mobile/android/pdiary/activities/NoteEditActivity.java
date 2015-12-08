/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bevy.mobile.android.pdiary.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import bevy.mobile.android.pdiary.PersonalDiaryDB;
import bevy.mobile.android.pdiary.R;
import bevy.mobile.android.pdiary.Utils;

public class NoteEditActivity extends Activity {

	private EditText mTitleText;
    private EditText mBodyText;
    private TextView mDateDisplay;
    private Long mRowId;
    private PersonalDiaryDB mDbHelper;
    private int mMonth;
    private int mDay;
    private int mYear;   
    private int mHH;
    private int mMM;
    private int mSS;
    private StringBuilder mTimestamp;
    private Button mPickDate;
    static final int DATE_DIALOG_ID = 0;
    private long id = -1;
    private String dateStr = "";
    private Calendar c;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new PersonalDiaryDB(this);
        
        setContentView(R.layout.note_edit);
        
       
        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);
        mPickDate = (Button) findViewById(R.id.pickDate);
        mDateDisplay = (TextView) findViewById(R.id.date);
        
        Bundle extra = getIntent().getExtras();
		if(extra != null){
			id = extra.getLong("id");
			dateStr = extra.getString("date");
			if(id >= 0){
				SQLiteDatabase db = mDbHelper.getReadableDatabase();
				Cursor c = db.query("entries", null, "id = "+id, null, null, null, null);
				if(c.moveToFirst()){
					String title = c.getString(c.getColumnIndex("title"));
					String createdDate = c.getString(c.getColumnIndex("date_added"));
					String body = c.getString(c.getColumnIndex("entry"));
					String lastModified = c.getString(c.getColumnIndex("last_modified"));
					if(title != null){
						mTitleText.setText(title);
						
					}
	//				if(createdDate != null){
	//					mCreatedDateValue.setText(createdDate);
	//				}
					if(body != null){
						mBodyText.setText(body);
					}
					if(lastModified != null){
						mDateDisplay.setText(lastModified);
					}
				}
			}
			
		}
        
     // add a click listener to the button
        mPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        
        // get the current date
        c = Calendar.getInstance();
        if(dateStr != null && !dateStr.equals("")){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date = sdf.parse(dateStr);
				c.setTime(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHH = c.get(Calendar.HOUR_OF_DAY);
        mMM = c.get(Calendar.MINUTE);
        mSS = c.get(Calendar.SECOND);
        // display the current date
        updateDisplay();
        
         
        Button confirmButton = (Button) findViewById(R.id.save);
       
        mRowId = savedInstanceState != null ? savedInstanceState.getLong(PersonalDiaryDB.KEY_ROWID) 
                							: null;
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();            
			mRowId = extras != null ? extras.getLong(PersonalDiaryDB.KEY_ROWID) 
									: null;
		}

        confirmButton.setOnClickListener(new View.OnClickListener() {

        	public void onClick(View view) {
        		String title = mTitleText.getText().toString();
				title = title.trim();
				if(title.equalsIgnoreCase("")){
					DialogInterface.OnClickListener ok = new DialogInterface.OnClickListener() {
					    @Override
					    public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					    }
					};
					AlertDialog alert = Utils.getOkDialog(view.getContext(),"Title can't be empty", ok);
					alert.show();
					return;
				}
        		saveState();
        	    setResult(RESULT_OK);
        	    finish();
        	}
          
        });
        
        Button cancelButton = (Button) findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
    }
    
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	if(mDbHelper != null)
    		mDbHelper.close();
    }

    
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID:
            return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        }
        return null;
    }
   
 // updates the date we display in the TextView
    private void updateDisplay() {
    	mTimestamp = new StringBuilder()
        .append(mYear).append("-");
        
    	paddTwoDigits(mTimestamp, mMonth + 1);
        
        mTimestamp.append("-");
        paddTwoDigits(mTimestamp,mDay);
        mTimestamp.append(" ");
        paddTwoDigits(mTimestamp, mHH);
        mTimestamp.append(":");
        paddTwoDigits(mTimestamp,mMM);
        mTimestamp.append(":");
        paddTwoDigits(mTimestamp,mSS);
    	
    	mDateDisplay.setText(mTimestamp);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(PersonalDiaryDB.KEY_ROWID, mRowId);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        //populateFields();
    }
    
    private void saveState() {
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();
        String date = mTimestamp.toString();//mDateText.getText().toString();

        if (id <= 0) {
            long id = mDbHelper.createNote(title, body, date);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateNote(id, title, body);
        }
        
    }
    
    /**
	 * Padds a number with an extra zero if it is less than 10
	 * 
	 */
	private static void paddTwoDigits(StringBuilder sBuilder, int number) {
		
		if(number < 10) {
			sBuilder.append("0");
			sBuilder.append(number);
		}
		else {
			sBuilder.append(number);
		}
	}

    
 // the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, 
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };
}

package bevy.mobile.android.pdiary.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import bevy.mobile.android.pdiary.PersonalDiaryDB;
import bevy.mobile.android.pdiary.R;


public class NoteViewActivity extends Activity {
	private PersonalDiaryDB mDbHelper;
	private TextView mTitleValue;
	private TextView mBodyValue;
	private TextView mCreatedDateValue;
	private TextView mLastModifiedValue;

	public static final int EDIT_ID = Menu.FIRST;
    public static final int DELETE_ID = Menu.FIRST + 1;
    
    public static final int ACTIVITY_EDIT = 0;
    public static final int ACTIVITY_DELETE = 1;
    private long id;
	
	
	@Override 
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
	super.onCreateOptionsMenu(menu);
	menu.add(0, EDIT_ID, 0, R.string.menu_edit).setShortcut('1', 'e')
	    .setIcon(R.drawable.icon_delete);
	menu.add(0, DELETE_ID, 0, R.string.menu_delete).setShortcut('2', 'h')
	    .setIcon(R.drawable.icon_delete);
	return true;
	}
	
	@Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
        case EDIT_ID:
            editNote();
            return true;
        case DELETE_ID:
        	deleteNote();
        	return true;
        }
       
        return super.onMenuItemSelected(featureId, item);
    }
	
	@Override
    protected void onPause() {
        super.onPause();
    	if(mDbHelper != null){
    		mDbHelper.close();
    	}
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        show();
    }
    
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	if(mDbHelper != null){
    		mDbHelper.close();
    	}
    }
	private void editNote(){
		Intent i = new Intent(this, NoteEditActivity.class);
		i.putExtra("id", id);
        startActivityForResult(i, ACTIVITY_EDIT);
	}
	
	private void deleteNote(){
		mDbHelper.deleteNote(id);
		this.finish();
	}
	
	private void show(){
		mDbHelper = new PersonalDiaryDB(this);
		setContentView(R.layout.note_view);
		mTitleValue = (TextView) findViewById(R.id.titlevalue); 
		mBodyValue = (TextView) findViewById(R.id.bodyvalue);
		mCreatedDateValue = (TextView) findViewById(R.id.createdvalue);
		mLastModifiedValue = (TextView) findViewById(R.id.lastmodifiedvalue);
		
		Typeface face = Typeface.createFromAsset(getAssets(), "fonts/verdana.ttf");
		mBodyValue.setTypeface(face);
		
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			id = extras.getInt("id");
			SQLiteDatabase db = mDbHelper.getReadableDatabase();
			Cursor c = db.query("entries", null, "id = "+id, null, null, null, null);
			if(c.moveToFirst()){
				String title = c.getString(c.getColumnIndex("title"));
				String createdDate = c.getString(c.getColumnIndex("date_added"));
				String body = c.getString(c.getColumnIndex("entry"));
				String lastModified = c.getString(c.getColumnIndex("last_modified"));
				if(title != null){
					mTitleValue.setText(title);
					
				}
				if(createdDate != null){
					mCreatedDateValue.setText(createdDate);
				}
				if(body != null){
					mBodyValue.setText(body);
					mBodyValue.setTextSize(12);
					//mBodyValue.setLineSpacing(0.5f, 1);
					
				}
				if(lastModified != null){
					mLastModifiedValue.setText(lastModified);
				}
			}
			c.close();
			
		}		
	}

}

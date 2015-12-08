package bevy.mobile.android.pdiary.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import bevy.mobile.android.pdiary.PersonalDiaryDB;
import bevy.mobile.android.pdiary.R;
import bevy.mobile.android.pdiary.models.DiaryEntry;

public class ListNotesForDayActivity extends ListActivity {

	private PersonalDiaryDB mDbHelper;
	
	public static final int NEW_NOTE = Menu.FIRST;
    public static final int DELETE_ID = Menu.FIRST + 1;
    private static final int ACTIVITY_CREATE=0;
    private long selectedItem = -1;
    Map<Long,Integer> idMap = new HashMap<Long,Integer>();
    private String dateString = "";
    
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mDbHelper = new PersonalDiaryDB(this);
		showContent();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
	super.onCreateOptionsMenu(menu);
	menu.add(0, NEW_NOTE, 0, R.string.menu_edit).setShortcut('1', 'e')
	    .setIcon(R.drawable.icon_delete);
	menu.add(0, DELETE_ID, 0, R.string.menu_delete).setShortcut('2', 'h')
	    .setIcon(R.drawable.icon_delete);
	return true;
	}
	
	@Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
        case NEW_NOTE:
            newNote();
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
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        showContent();
    }	
    
    private void newNote() {
        Intent i = new Intent(this, NoteEditActivity.class);
        i.putExtra("date", dateString);
        startActivityForResult(i, ACTIVITY_CREATE);
    }
    
    private void deleteNote(){
    	
    	if(mDbHelper==null){
    		mDbHelper = new PersonalDiaryDB(this);
    	}
    	mDbHelper.deleteNote(selectedItem);
    	showContent();
    }
    
    private void showContent(){
    	
		Bundle extras = getIntent().getExtras();
		
		if(extras!=null){
			 dateString = extras.getString("date");
			 System.out.println("dateString = "+dateString);
		}
		PersonalDiaryDB db = new PersonalDiaryDB(this);
		Date date = null;
		try{
		date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
		}catch(ParseException pex){
			System.out.println(pex.getMessage());
		}
		
		
		List<DiaryEntry> entriesList = db.getEntriesForDay(date);
		Iterator<DiaryEntry> itr = entriesList.iterator();
		Map<Integer,String> notesMap = new HashMap<Integer,String>();
		while(itr.hasNext()){
			DiaryEntry entry = itr.next();
			
			notesMap.put(entry.getId(),entry.getTitle());
		}

		Set<Integer> keyList = notesMap.keySet();
		Collection<String> titleColl = notesMap.values();
		List<String> titleList = new ArrayList<String>(titleColl);
		String[] notes = new String[titleList.size()];
		notes = titleList.toArray(notes);
		
		idMap.clear();
		Iterator<Integer> it = keyList.iterator();
		long value = 0;
		while(it.hasNext()){
			idMap.put(value++, it.next());
		}
		if(notes != null && notes.length > 0){
		setListAdapter(new ArrayAdapter<String>(this,
		          android.R.layout.simple_list_item_1, notes));
		  getListView().setTextFilterEnabled(true);
		  getListView().setClickable(true);
		  ListView v = getListView();
		  final Context c = this;
		  
		  v.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent i = new Intent(c, NoteViewActivity.class);
				int val = idMap.get(id);
				i.putExtra("id", idMap.get(id));
				c.startActivity(i);
			}
			  
		  });
		  
		  v.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				selectedItem = idMap.get(id);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			  
		  });
		  		  
		}else{
			Intent i = new Intent(this,NoteEditActivity.class);
			i.putExtra("date", dateString);
			this.startActivity(i);
			finish();
//			notes = new String[1];
//			notes[0] = "No notes present";
//			setListAdapter(new ArrayAdapter<String>(this,
//			      android.R.layout.simple_list_item_1, notes));
//			getListView().setTextFilterEnabled(true);
		}
   	
    }
	
}

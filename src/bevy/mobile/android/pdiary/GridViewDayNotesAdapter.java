package bevy.mobile.android.pdiary;

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

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import bevy.mobile.android.pdiary.activities.NoteViewActivity;
import bevy.mobile.android.pdiary.models.DiaryEntry;

public class GridViewDayNotesAdapter extends BaseAdapter implements ListAdapter{
	
	private Map<Integer,String> notesMap = new HashMap<Integer,String>();
	private Map<Long,Integer> idMap = new HashMap<Long,Integer>();
	private PersonalDiaryDB _db;
	private Context c;
	private int size;
	private GridView gridView;
	private GridView calView;
	private String dateString;
	private Date date;
	Calendar calendar;
	
	public GridViewDayNotesAdapter(Context c, String dateStr, PersonalDiaryDB _db, GridView gview, GridView cView){
		this.c = c;
		this._db = _db;
		gridView = gview;
		dateString = dateStr;
		calView = cView;
		prepareAdapter(dateString);
		
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
		if(convertView == null){
			Long l = new Long(position+"");
			l = l/2;
			//int id = ids.get(l);
			final int val = idMap.get(l);
			if(position%2 == 0){
				TextView v = new TextView(c);
				String textToSet = notesMap.get(val);
				v.setText(textToSet);
				v.setClickable(true);
				v.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						Intent i = new Intent(c, NoteViewActivity.class);
						i.putExtra("id", val);
						c.startActivity(i);
						
					}
					
				});
				return v;
			}else{
				ImageView image = new ImageView(c);
				image.setImageResource(R.drawable.delete);
				image.setClickable(true);
				image.setOnClickListener(new OnClickListener(){
					
					@Override
					public void onClick(View v) {
						_db.deleteNote(val);
						prepareAdapter(dateString);
						gridView.refreshDrawableState();
												
					}
				});
				return image;
			}
		}else{
			return convertView;
		}
	}
	
	private void prepareAdapter(String dateString){
		try{
			date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
			}catch(ParseException pex){
				System.out.println(pex.getMessage());
			}
			
			
			List<DiaryEntry> entriesList = _db.getEntriesForDay(date);
			Iterator<DiaryEntry> itr = entriesList.iterator();
			//Map<Integer,String> notesMap = new HashMap<Integer,String>();
			notesMap.clear();
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
			size = notes.length * 2;
			if(notesMap.size()>0){
				gridView.setAdapter(this);
			}else{
				calView.setAdapter(new GridViewDayAdapter(c, Calendar.getInstance().getTime(), _db));
				notes = new String[1];
				notes[0] = "No notes present";
				gridView.setAdapter(new ArrayAdapter<String>(c,
				      android.R.layout.simple_list_item_1, notes));
				gridView.setTextFilterEnabled(true);

			}

	}
	
}

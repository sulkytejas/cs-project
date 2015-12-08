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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import bevy.mobile.android.pdiary.PersonalDiaryDB;
import bevy.mobile.android.pdiary.R;
import bevy.mobile.android.pdiary.Utils;
import bevy.mobile.android.pdiary.models.Avatar;

/**
 * @author Sandeep Soni
*/

public class LoginActivity extends Activity implements OnClickListener {

    private Button buttonViewOk;
    private TextView txtViewAvatarName, txtViewAvatarPassword;
    private PersonalDiaryDB _db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	_db = new PersonalDiaryDB(this);

	setContentView(R.layout.login);

	setupViewsAndHandlers();
    }

    private void setupViewsAndHandlers() {
	buttonViewOk = (Button) findViewById(R.id.ButtonOk);
	buttonViewOk.setOnClickListener(this);

	txtViewAvatarName = (TextView) findViewById(R.id.avatarName);
	txtViewAvatarPassword = (TextView) findViewById(R.id.avatarPassword);

	TextView createAccountLink = (TextView) findViewById(R.id.createAccount);
	
	final Context c = this ;
	createAccountLink.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent intent = new Intent( c , CreateAccountActivity.class);
		c.startActivity(intent);
		finish();
	    }
	    
	});
    }

    
    @Override
    public void onClick(View v) {
	if (v == buttonViewOk) {
	    String avatarName = txtViewAvatarName.getText().toString();
	    String avatarPasword = txtViewAvatarPassword.getText().toString();

	    Avatar avatar = _db.getAvatar(avatarName);
	    if (avatarName.equals(avatar.getAvatarName())
		    && avatarPasword.equals(avatar.getPassword())) {
		// Login Sucess!
		setContentView(R.layout.main);
		Intent i = new Intent(this, MainScreen.class);

	        Log.i(Utils.APPLICATION_LOG_KEY, "About to launch main screen");
	        finish();
	        // the results are called on widgetActivityCallback
	        this.startActivity(i);
		
		//finish();
	    } else {
		DialogInterface.OnClickListener ok = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			
		    }
		};
		
		AlertDialog alert = Utils.getOkDialog(this,"Login Failed!", ok);
		alert.show();
	    }
	} 
    }
    
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	if(_db != null) {
    		_db.close();	
    	}
    }


}

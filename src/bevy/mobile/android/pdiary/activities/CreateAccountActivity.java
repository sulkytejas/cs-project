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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import bevy.mobile.android.pdiary.PersonalDiaryDB;
import bevy.mobile.android.pdiary.R;

/**
 * @author Sandeep Soni
*/

public class CreateAccountActivity extends Activity {
    private PersonalDiaryDB _db;
    private Button buttonCreateAccount;
    private TextView txtViewAvatarName;
    private TextView txtViewAvatarPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	_db = new PersonalDiaryDB(this);

	setContentView(R.layout.createaccount);

	setupViewsAndHandlers();
    }

    private void setupViewsAndHandlers() {
	final Context c = this ;
	
	txtViewAvatarName = (TextView) findViewById(R.id.avatarName);
	txtViewAvatarPassword = (TextView) findViewById(R.id.avatarPassword);

	buttonCreateAccount = (Button) findViewById(R.id.ButtonCreate);
	buttonCreateAccount.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		try {
		    _db.createAccount(txtViewAvatarName.getText().toString(),
			    txtViewAvatarPassword.getText().toString());
		    showLoginScreen();
		} catch (Exception e) {
		    showMessage();
		}
	    }

	    private void showMessage() {

		
	    }

	    private void showLoginScreen() {
		Intent intent = new Intent(c,LoginActivity.class);
		c.startActivity(intent);
	    }
	});
    }
}

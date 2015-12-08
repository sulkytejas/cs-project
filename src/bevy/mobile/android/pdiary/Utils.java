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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * @author Sandeep Soni
*/

public class Utils {
    public static final String BLANK = "";
    
    public static final String APPLICATION_LOG_KEY = "PDIARY";
    public static final String DATABASE_NAME = "PDIARY";
    public static final int DATABASE_VERSION = 1;
    

    private static SimpleDateFormat sdf = new SimpleDateFormat();

    static {
	sdf.applyPattern("yyyy-MM-dd");
    }

    public static Date getDateFromString(String dateString)
	    throws ParseException {
	return sdf.parse(dateString);
    }

    public static String getStringFromDate(Date d) {
	return sdf.format(d);
    }

    public static AlertDialog getOkDialog(Context c, String text,
	    DialogInterface.OnClickListener yes) {
	AlertDialog.Builder builder = new AlertDialog.Builder(c);
	
	builder.setMessage(text)
	       .setCancelable(false)
	       .setPositiveButton("Yes",yes);
	
	return builder.create();
    }

    public static AlertDialog getYesNoDialog(Context c, String text,
	    DialogInterface.OnClickListener yes,
	    DialogInterface.OnClickListener no) {
	AlertDialog.Builder builder = new AlertDialog.Builder(c);
	
	builder.setMessage(text)
	       .setCancelable(false)
	       .setPositiveButton("Yes",yes)
	       .setNegativeButton("No", no);
	
	return builder.create();
    }
}

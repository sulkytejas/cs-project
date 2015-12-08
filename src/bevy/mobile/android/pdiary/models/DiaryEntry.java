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
package bevy.mobile.android.pdiary.models;

import java.util.Date;

/**
 * @author Sandeep Soni
*/

public class DiaryEntry {
    private String _entry;
    private String _title;
    private Date _createdDate;
    private Date _lastModified;
    private Avatar _createdBy;
    private int _id;

    public String getTitle() {
    	return _title;
    }

    public void setTitle(String title) {
    	this._title = title;
    }
        
    public String getEntry() {
	return _entry;
    }

    public void setEntry(String entry) {
	this._entry = entry;
    }

    public Date getCreatedDate() {
	return _createdDate;
    }

    public void setCreatedDate(Date createdDate) {
	this._createdDate = createdDate;
    }

    public Date getLastModified() {
	return _lastModified;
    }

    public void setLastModified(Date lastModified) {
	this._lastModified = lastModified;
    }

    public Avatar getCreatedBy() {
	return _createdBy;
    }

    public void setCreatedBy(Avatar createdBy) {
	this._createdBy = createdBy;
    }

	public void setId(int id) {
		this._id = id;
	}

	public int getId() {
		return _id;
	}
}

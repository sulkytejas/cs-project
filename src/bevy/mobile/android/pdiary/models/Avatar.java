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

import bevy.mobile.android.pdiary.Utils;

/**
 * @author Sandeep Soni
*/

public class Avatar {
    private String _id = Utils.BLANK;
    private String _avatarName = Utils.BLANK;
    private String _password = "****";

    public String getId() {
	return _id;
    }

    public void setId(String id) {
	this._id = id;
    }

    public String getAvatarName() {
	return _avatarName;
    }

    public void setAvatarName(String avatarName) {
	this._avatarName = avatarName;
    }

    public String getPassword() {
	return _password;
    }

    public void setPassword(String password) {
	this._password = password;
    }

}

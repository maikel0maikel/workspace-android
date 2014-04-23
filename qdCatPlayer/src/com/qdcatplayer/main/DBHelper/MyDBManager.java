package com.qdcatplayer.main.DBHelper;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import android.content.Context;
public class MyDBManager {
    private MySQLiteHelper _dbHelper = null;
    public MySQLiteHelper getHelper()
    {
    	if (_dbHelper != null) {
            return _dbHelper;
    	}
    	return null;
    }
    //gets a helper once one is created ensures it doesnt create a new one
    public MySQLiteHelper getHelper(Context context)
    {
        if (_dbHelper == null) {
            _dbHelper =
                    OpenHelperManager.getHelper(context, MySQLiteHelper.class);
        }
        return _dbHelper;
    }
 
    //releases the helper once usages has ended
    public void releaseHelper()
    {
        if (_dbHelper != null) {
            _dbHelper.close();
        	OpenHelperManager.releaseHelper();
            _dbHelper = null;
        }
    }
}

/*
 * Copyright (c) 2017. YPY Global - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at.
 *
 *         http://ypyglobal.com/sourcecode/policy
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.edelweiss.stationapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * @author:YPY Global
 * @Email: bl911vn@gmail.com
 * @Website: www.ypyglobal.com
 * @Date:Oct 20, 2017
 */

public class RadioSettingManager implements IXRadioSettingConstants {
	
	public static final String TAG = RadioSettingManager.class.getSimpleName();
	
	private static final String NAME_SHARPREFS = "app_prefs";

	private static void saveSetting(Context mContext, String mKey, String mValue){
		try{
			if(mContext!=null){
				SharedPreferences mSharedPreferences =mContext.getSharedPreferences(NAME_SHARPREFS, Context.MODE_PRIVATE);
				if(mSharedPreferences!=null){
					Editor editor = mSharedPreferences.edit();
					editor.putString(mKey, mValue);
					editor.apply();
				}
			}


		}
		catch (Exception e){
			e.printStackTrace();
		}

	}
	
	private static String getSetting(Context mContext, String mKey, String mDefValue){
		try{
			if(mContext!=null){
				SharedPreferences mSharedPreferences =mContext.getSharedPreferences(NAME_SHARPREFS, Context.MODE_PRIVATE);
				if(mSharedPreferences!=null){
					return mSharedPreferences.getString(mKey, mDefValue);
				}
			}

		}
		catch (Exception e){
			e.printStackTrace();
		}
		return mDefValue;

	}

	public static int getSleepMode(Context mContext){
		return Integer.parseInt(getSetting(mContext, KEY_TIME_SLEEP, "0"));
	}

	public static void setSleepMode(Context mContext, int mValue){
		saveSetting(mContext, KEY_TIME_SLEEP, String.valueOf(mValue));
	}


}

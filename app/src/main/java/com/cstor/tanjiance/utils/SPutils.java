package com.cstor.tanjiance.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.cstor.tanjiance.activity.MyApp;

import java.lang.reflect.Method;

/**
 * SP存储
 */
public class SPutils {
	private static SharedPreferences mSharedPreferences;//轻量级存储sp
	private static SPutils sPutils;

	private SPutils(SharedPreferences mShareds) {
		mSharedPreferences = mShareds;
	}

	/**
	 * 初始化SP
	 */
	public static SPutils getInstans(Context context) {
		if(sPutils==null){
	        sPutils = new SPutils(PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()));
		}
		return sPutils;
	}

	/**
	 * 初始化SP
	 */
	public static SPutils getInstans(MyApp app) {
		if(sPutils==null){
			sPutils = new SPutils(PreferenceManager.getDefaultSharedPreferences(MyApp.getmContext()));
		}
		return sPutils;
	}

	public void putStringValue(String key, String value) {
		SharedPreferencesCompat.apply(mSharedPreferences.edit().putString(key, value));
	}

	public void putBooleanValue(String key, boolean value) {
		SharedPreferencesCompat.apply(mSharedPreferences.edit().putBoolean(key, value));
	}

	public void putIntValue(String key, int value) {
		SharedPreferencesCompat.apply(mSharedPreferences.edit().putInt(key, value));
	}

	public void putFloatValue(String key, float value) {
		SharedPreferencesCompat.apply(mSharedPreferences.edit().putFloat(key, value));
	}

	public void putLongValue(String key, long value) {
		SharedPreferencesCompat.apply(mSharedPreferences.edit().putLong(key, value));
	}


	public String getStringValue(String key) {
		return mSharedPreferences.getString(key, "");
	}

	public boolean getBooleanValue(String key) {
		return mSharedPreferences.getBoolean(key, false);
	}

	public int getIntValue(String key) {
		return mSharedPreferences.getInt(key, -1);
	}

	public float getFloatValue(String key) {
		return mSharedPreferences.getFloat(key,0f);
	}
	public long getLongValue(String key) {
		return mSharedPreferences.getLong(key, Long.valueOf(0));
	}


	/**
	 * 删除单个sp数据
	 */
	public void remove(String key) {
		mSharedPreferences.edit().remove(key).commit();
	}

	/**
	 * 删除sp数据
	 */
	public void clear() {
		mSharedPreferences.edit().clear().commit();
	}

	/**
	 * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
	 * 因为commit方法是同步的，并且我们很多时候的commit操作都是UI线程中，毕竟是IO操作，尽可能异步；
	 * 所以我们使用apply进行替代，apply异步的进行写入；
	 */
	private static class SharedPreferencesCompat {
		private static final Method sApplyMethod = findApplyMethod();
		/**
		 * 反射查找apply的方法
		 *
		 * @return
		 */
		@SuppressWarnings({"unchecked", "rawtypes"})
		private static Method findApplyMethod() {
			try {
				Class clz = SharedPreferences.Editor.class;
				return clz.getMethod("apply");
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * 如果找到则使用apply执行，否则使用commit
		 *
		 * @param editor
		 */
		public static void apply(SharedPreferences.Editor editor) {
			try {
				if (sApplyMethod != null) {
					sApplyMethod.invoke(editor);
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			editor.commit();
		}
	}

}

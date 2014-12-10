package com.savinkoff.ktt;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.util.Log;

public class PrefsFragment extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {

//	private ListPreference soundPreference;
	private EditTextPreference namePreference;
	private EditTextPreference phoneNumberPreference;
//	private EditTextPreference bellPreference;
//	private EditTextPreference runPreference;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.ktt_preferences);

	}

	@Override
	public void onResume() {
		super.onResume();
		// Set up a listener whenever a key changes
		Log.i("xxx", "PrefsFragment:onResume");
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
		updateSettingsScreen();
	}

	@Override
	public void onPause() {
		super.onPause();
		// Set up a listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// Update the preferences screen
		Log.i("xxx", "onSharedPreferenceChanged... Updating summaries");
		updateSettingsScreen();
	}

	private void updateSettingsScreen() {
		
		
//		bellPreference = (EditTextPreference) getPreferenceScreen()
//				.findPreference("ktt_bell_checkbox_key");
//		runPreference = (EditTextPreference) getPreferenceScreen()
//				.findPreference("run_checkbox_key");
		namePreference = (EditTextPreference) getPreferenceScreen().findPreference("partners_name_key");
		phoneNumberPreference = (EditTextPreference) getPreferenceScreen().findPreference("phone_number_key");

		SharedPreferences appPrefs = getPreferenceScreen().getSharedPreferences();
		
		boolean sound = appPrefs.getBoolean("sound_checkbox", true);
//		soundPreference.setSummary("Current sound file is " + soundType);
//		CheckboxPreference = prefs.getBoolean("sound_checkbox", true);
		
		String name = appPrefs.getString("partners_name_key", getString(R.string.name_not_set));
		namePreference.setSummary(name);
		
		String phone = appPrefs.getString("phone_number_key", getString(R.string.phone_not_set));
		phoneNumberPreference.setSummary(phone);

	}

}
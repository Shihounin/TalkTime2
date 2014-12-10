package com.savinkoff.ktt;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SeekBarPreference extends Preference implements OnSeekBarChangeListener {

	private final String TAG = getClass().getName();
	private int DEFAULT_VALUE;
	private SeekBar mSeekBar;
	public int FrequencySliderDefaultValue;
//	private ListPreference soundPreference;
//	private EditTextPreference namePreference;

	public SeekBarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPreference(context, attrs);
	}

	public SeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initPreference(context, attrs);
	}

	private void initPreference(Context context, AttributeSet attrs) {
		mSeekBar = new SeekBar(context, attrs);
		mSeekBar.setOnSeekBarChangeListener(this);
		setWidgetLayoutResource(R.layout.seek_bar_preference);
	}

	
	@Override
	protected View onCreateView(ViewGroup parent) {
		View view = super.onCreateView(parent);

		// The basic preference layout puts the widget frame to the right of the
		// title and summary,
		// so we need to change it a bit - the seekbar should be under them.
		LinearLayout layout = (LinearLayout) view;
		layout.setOrientation(LinearLayout.VERTICAL);

		return view;
	}

	@Override
	public void onBindView(View view) {
		super.onBindView(view);
		try {
			// move our seekbar to the new view we've been given
			ViewParent oldContainer = mSeekBar.getParent();
			ViewGroup newContainer = (ViewGroup) view
					.findViewById(R.id.seekBarPrefBarContainer);

			if (oldContainer != newContainer) {
				// remove the seekbar from the old view
				if (oldContainer != null) {
					((ViewGroup) oldContainer).removeView(mSeekBar);
				}
				// remove the existing seekbar (there may not be one) and add
				// ours
				newContainer.removeAllViews();
				newContainer.addView(mSeekBar,
						ViewGroup.LayoutParams.FILL_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
			}
		} catch (Exception ex) {
			Log.e(TAG, "Error binding view: " + ex.toString());
		}

		// if dependency is false from the beginning, disable the seek bar
		if (view != null && !view.isEnabled()) {
			mSeekBar.setEnabled(false);
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
/*
		if (progress >= 95) {
			progress = 100;
		}else if (progress < 95 && progress >= 85) {
			progress = 90;
		}else if (progress < 85 && progress >= 75) {
			progress = 80;
		}else if (progress < 75 && progress >= 65) {
			progress = 70;
		}else if (progress < 65 && progress >= 55) {
			progress = 60;
		}else if (progress < 55 && progress >= 45) {
			progress = 50;
		}else if (progress < 45 && progress >= 35) {
			progress = 40;
		}else if (progress < 35 && progress >= 25) {
			progress = 30;
		}else if (progress < 25 && progress >= 15) {
			progress = 20;
		}else if (progress < 15 && progress >= 5) {
			progress = 10;
		}else if (progress < 5 ){
			progress = 0;
		}
/*
*  change:  convert using  a logarithmic scale from 10 - 500000 = 10 seconds to 5.7 days
*
		int logVal = (int) logslider(progress);
		Log.i("xxx", "onProgressChanged.logVal  = " + logVal);
*/

		// set the thumb pointer
		seekBar.setProgress(progress);

		// change accepted, store it
		persistInt(progress);

	}
	
	
	private int logslider(int position) {
		  // position will be between 0 and 100
		  int minp = 0;
		  int maxp = 100;

		  // The result should be between 10 an 5000000
		  double minv = (double) Math.log(10);
		  double maxv = (double) Math.log(5000000);

		  // calculate adjustment factor
		  double scale = (maxv-minv) / (maxp-minp);

		  return (int) Math.exp(minv + scale*(position-minp));
		}

	
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		
		int val =	logslider(seekBar.getProgress());
		
		Log.i("xxx", "onStopTrackingTouch.val  = " + val);
		Log.i("xxx", "onStopTrackingTouch.logVal  = " + logslider(val));

			Toast.makeText(getContext(), String.valueOf(val)+ " second delay", Toast.LENGTH_SHORT).show();
		notifyChanged();
	}

/***************************************************************************************************
 * Providing a default value
 * If the instance of your Preference class specifies a default value (with the android:defaultValue 
 * attribute), then the system calls onGetDefaultValue() when it instantiates the object in order to 
 * retrieve the value. You must implement this method in order for the system to save the default 
 * value in the SharedPreferences.
 ***************************************************************************************************/
	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		Log.i("xxx", "onGetDefaultValue = " + a.getInteger(index, DEFAULT_VALUE));
		FrequencySliderDefaultValue = a.getInteger(index, DEFAULT_VALUE);
	    return FrequencySliderDefaultValue;
	}
	
/*******************************************************************************************************
 * restorePersistedValue	True to restore the persisted value; false to use the given defaultValue.
 * defaultValue	The default value for this Preference. Only use this if restorePersistedValue is false.
 * 
 * Caution: You cannot use the defaultValue as the default value in the getPersisted*() method, because 
 * its value is always null when restorePersistedValue is true.
 *****************************************************************************************************/
	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		
		Log.i("xxx", "onSetInitialValue. getPersistedInt  = " + getPersistedInt(FrequencySliderDefaultValue));
		mSeekBar.setProgress(getPersistedInt(FrequencySliderDefaultValue));
	}
	
	/**
	 * make sure that the seekbar is disabled if the preference is disabled
	 */
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		mSeekBar.setEnabled(enabled);
	}

	@Override
	public void onDependencyChanged(Preference dependency,
			boolean disableDependent) {
		super.onDependencyChanged(dependency, disableDependent);

		// Disable movement of seek bar when dependency is false
		if (mSeekBar != null) {
			mSeekBar.setEnabled(!disableDependent);
		}
	}
	private void updateSummaries() {
		/*
		 * Get the current settings and put them in the summary fields
		 */
		Log.i("xxx", "updateSummaries = ");

//		soundPreference = (ListPreference) getPreferenceScreen()
//				.findPreference("sound_type");
//		namePreference = (EditTextPreference) getPreferenceScreen()
//				.findPreference("partners_name");
//		phoneNumberPreference = (EditTextPreference) getPreferenceScreen()
//				.findPreference("phone_number");
//		smsNumberPreference = (EditTextPreference) getPreferenceScreen()
//				.findPreference("sms_number");
//
//		SharedPreferences appPrefs = getSharedPreferences("kttPreferences",	MODE_PRIVATE);
//		soundType = appPrefs.getString("sound_type", "None");
//		soundPreference.setSummary("Current sound file is " + soundType);
//
//		String name = appPrefs.getString("partners_name",
//				"Not set. Tap to set name.");
//		Log.i("xxx", "partners name = " + name);
//		namePreference.setSummary(name);
//
//		String phone = appPrefs.getString("phone_number",
//				"Not set. Tap to set phone number.");
//		phoneNumberPreference.setSummary(phone);
//		Log.i("xxx", "phone #  = " + phone);
//
//		String sms = appPrefs.getString("sms_number",
//				"Not set. Tap to set number for texting.");
//		smsNumberPreference.setSummary(sms);
//		Log.i("xxx", "sms # = " + sms);

	}


}

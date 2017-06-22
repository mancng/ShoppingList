package com.mancng.shoppinglist.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.mancng.shoppinglist.R;
import com.mancng.shoppinglist.infrastructure.Utils;

public class SettingActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SortPreferenceFragment())
                .commit();
    }

    public static class SortPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_general);
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_sort_list_name)));
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            setPreferenceSummary(preference, newValue);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Utils.LIST_ORDER_PREFERENCE, newValue.toString()).apply();
            return true;
        }

        private void bindPreferenceSummaryToValue (Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            setPreferenceSummary(preference,
                    PreferenceManager
            .getDefaultSharedPreferences(preference.getContext())
            .getString(preference.getKey(),""));
        }

        private void setPreferenceSummary (Preference preference,Object value) {
            String stringValue = value.toString();

            if(preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int preferenceIndex = listPreference.findIndexOfValue(stringValue);

                //Bind the string into the preference
                if(preferenceIndex >= 0) {
                    preference.setSummary(listPreference.getEntries()[preferenceIndex]);
                }
            }
        }
    }
}

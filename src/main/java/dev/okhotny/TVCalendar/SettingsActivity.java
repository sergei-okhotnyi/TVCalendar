package dev.okhotny.TVCalendar;

import android.app.ActionBar;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

import dev.okhotny.TVCalendar.providers.thetvdb.TheTVDBApi;

public class SettingsActivity extends PreferenceActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.settings);

        addPreferencesFromResource(R.xml.preferences);

        final ListPreference language = (ListPreference) findPreference("preferred_language");
        language.setSummary(String.format("Preferred content language: %s", language.getEntries()[language.findIndexOfValue(language.getValue())]));
        language.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                language.setSummary(String.format("Preferred content language: %s", language.getEntries()[language.findIndexOfValue(newValue.toString())]));
                TheTVDBApi.updatePreferredLanguage();
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
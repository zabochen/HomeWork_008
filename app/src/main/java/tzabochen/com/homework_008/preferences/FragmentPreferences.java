package tzabochen.com.homework_008.preferences;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import tzabochen.com.homework_008.R;

public class FragmentPreferences extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}

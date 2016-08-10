package space.hamsters.fspammer;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hamster on 16/8/10.
 * <p/>
 * Dummy preference to prevent SwitchPreference losing animation
 * when it's the first one in PreferenceScreen.
 */
public class DummyPreference extends Preference {
    public DummyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public View getView(View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new View(getContext());
        }
        convertView.setVisibility(View.GONE);
        return convertView;
    }
}

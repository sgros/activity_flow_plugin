package menion.android.whereyougo.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;

public class PreviewPreference extends Preference {
    protected CharSequence defaultValue;
    protected String mValue;
    protected CharSequence previewTemplate;
    protected CharSequence summaryTemplate;

    public PreviewPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.summaryTemplate = "";
        this.previewTemplate = "";
        this.defaultValue = "";
        this.mValue = "";
        this.summaryTemplate = super.getSummary();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attr = attrs.getAttributeName(i);
            String val = attrs.getAttributeValue(i);
            if (attr.equalsIgnoreCase("previewTemplate")) {
                this.previewTemplate = val;
            }
            if (attr.equalsIgnoreCase("previewTemplate")) {
                this.previewTemplate = val;
            }
            if (attr.equalsIgnoreCase("defaultValue")) {
                this.defaultValue = val;
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    /* Access modifiers changed, original: protected */
    public void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setValue(restoreValue ? getPersistedString((String) defaultValue) : (String) defaultValue);
    }

    public void setValue(String value) {
        this.mValue = value;
        persistString(this.mValue);
        notifyChanged();
    }

    public CharSequence getSummary() {
        String preview = this.previewTemplate.toString();
        if (preview.length() == 0) {
            preview = "(" + this.mValue + ")";
        } else {
            preview = preview.replace("%1$", this.mValue);
        }
        return preview + " " + this.summaryTemplate;
    }
}

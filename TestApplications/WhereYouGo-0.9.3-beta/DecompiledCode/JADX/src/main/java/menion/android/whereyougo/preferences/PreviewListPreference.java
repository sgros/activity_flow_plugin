package menion.android.whereyougo.preferences;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;

public class PreviewListPreference extends ListPreference {
    protected CharSequence previewTemplate;
    protected CharSequence summaryTemplate;

    public PreviewListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.summaryTemplate = "";
        this.previewTemplate = "";
        this.summaryTemplate = super.getSummary();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attr = attrs.getAttributeName(i);
            String val = attrs.getAttributeValue(i);
            if (attr.equalsIgnoreCase("previewTemplate")) {
                this.previewTemplate = val;
            }
        }
    }

    public CharSequence getSummary() {
        String preview = this.previewTemplate.toString();
        if (preview.length() == 0) {
            preview = "(" + getEntry() + ")";
        } else {
            preview = preview.replace("%1$", getEntry());
        }
        return preview + " " + this.summaryTemplate;
    }

    /* Access modifiers changed, original: protected */
    public void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        setSummary(getSummary());
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.widget;

import java.text.Collator;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.text.TextUtils;
import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import java.util.HashSet;
import org.mozilla.focus.locale.LocaleManager;
import org.mozilla.focus.locale.Locales;
import android.util.Log;
import android.util.AttributeSet;
import android.content.Context;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import android.preference.ListPreference;

public class LocaleListPreference extends ListPreference
{
    private static final Map<String, String> languageCodeToNameMap;
    private CharacterValidator characterValidator;
    private volatile Locale entriesLocale;
    
    static {
        (languageCodeToNameMap = new HashMap<String, String>()).put("ast", "Asturianu");
        LocaleListPreference.languageCodeToNameMap.put("cak", "Kaqchikel");
        LocaleListPreference.languageCodeToNameMap.put("ia", "Interlingua");
        LocaleListPreference.languageCodeToNameMap.put("meh", "Tu´un savi \u00f1uu Yasi'\u00ed Yuku Iti");
        LocaleListPreference.languageCodeToNameMap.put("mix", "Tu'un savi");
        LocaleListPreference.languageCodeToNameMap.put("trs", "Triqui");
        LocaleListPreference.languageCodeToNameMap.put("zam", "D\u00ed\u0241zt\u00e8");
        LocaleListPreference.languageCodeToNameMap.put("oc", "occitan");
        LocaleListPreference.languageCodeToNameMap.put("an", "Aragon\u00e9s");
        LocaleListPreference.languageCodeToNameMap.put("tt", "\u0442\u0430\u0442\u0430\u0440\u0447\u0430");
        LocaleListPreference.languageCodeToNameMap.put("wo", "Wolof");
        LocaleListPreference.languageCodeToNameMap.put("anp", "\u0905\u0902\u0917\u093f\u0915\u093e");
        LocaleListPreference.languageCodeToNameMap.put("ixl", "Ixil");
        LocaleListPreference.languageCodeToNameMap.put("pai", "Paa ipai");
        LocaleListPreference.languageCodeToNameMap.put("quy", "Chanka Qhichwa");
        LocaleListPreference.languageCodeToNameMap.put("ay", "Aimara");
        LocaleListPreference.languageCodeToNameMap.put("quc", "K'iche'");
        LocaleListPreference.languageCodeToNameMap.put("tsz", "P'urhepecha");
        LocaleListPreference.languageCodeToNameMap.put("mai", "\u092e\u0948\u0925\u093f\u0932\u0940/\u09ae\u09c8\u09a5\u09bf\u09b2\u09c0");
        LocaleListPreference.languageCodeToNameMap.put("jv", "Basa Jawa");
        LocaleListPreference.languageCodeToNameMap.put("su", "Basa Sunda");
        LocaleListPreference.languageCodeToNameMap.put("ace", "Basa Ac\u00e8h");
        LocaleListPreference.languageCodeToNameMap.put("gor", "Bahasa Hulontalo");
    }
    
    public LocaleListPreference(final Context context) {
        this(context, null);
    }
    
    public LocaleListPreference(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    private void buildList() {
        final Locale default1 = Locale.getDefault();
        final StringBuilder sb = new StringBuilder();
        sb.append("Building locales list. Current locale: ");
        sb.append(default1);
        Log.d("GeckoLocaleList", sb.toString());
        if (default1.equals(this.entriesLocale) && this.getEntries() != null) {
            Log.v("GeckoLocaleList", "No need to build list.");
            return;
        }
        final LocaleDescriptor[] usableLocales = this.getUsableLocales();
        final int length = usableLocales.length;
        this.entriesLocale = default1;
        final int n = length + 1;
        final String[] entries = new String[n];
        final String[] entryValues = new String[n];
        final String string = this.getContext().getString(2131755348);
        int i = 0;
        entries[0] = string;
        entryValues[0] = "";
        while (i < length) {
            final String displayName = usableLocales[i].getDisplayName();
            final String tag = usableLocales[i].getTag();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(displayName);
            sb2.append(" => ");
            sb2.append(tag);
            Log.v("GeckoLocaleList", sb2.toString());
            ++i;
            entries[i] = displayName;
            entryValues[i] = tag;
        }
        this.setEntries((CharSequence[])entries);
        this.setEntryValues((CharSequence[])entryValues);
    }
    
    private Locale getSelectedLocale() {
        final String value = this.getValue();
        if (value != null && !value.equals("")) {
            return Locales.parseLocaleCode(value);
        }
        return Locale.getDefault();
    }
    
    private LocaleDescriptor[] getUsableLocales() {
        final Collection<String> packagedLocaleTags = LocaleManager.getPackagedLocaleTags(this.getContext());
        final HashSet set = new HashSet<LocaleDescriptor>(packagedLocaleTags.size());
        for (final String str : packagedLocaleTags) {
            final LocaleDescriptor localeDescriptor = new LocaleDescriptor(str);
            if (!localeDescriptor.isUsable(this.characterValidator)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Skipping locale ");
                sb.append(str);
                sb.append(" on this device.");
                Log.w("GeckoLocaleList", sb.toString());
            }
            else {
                set.add(localeDescriptor);
            }
        }
        final int size = set.size();
        final LocaleDescriptor[] a = set.toArray(new LocaleDescriptor[size]);
        Arrays.sort(a, 0, size);
        return a;
    }
    
    public CharSequence getSummary() {
        final String value = this.getValue();
        if (TextUtils.isEmpty((CharSequence)value)) {
            return this.getContext().getString(2131755348);
        }
        return new LocaleDescriptor(value).getDisplayName();
    }
    
    protected void onAttachedToActivity() {
        super.onAttachedToActivity();
        this.characterValidator = new CharacterValidator(" ");
        this.buildList();
    }
    
    protected void onDialogClosed(final boolean b) {
        super.onDialogClosed(b);
        LocaleManager.getInstance().updateConfiguration(this.getContext(), this.getSelectedLocale());
    }
    
    private static class CharacterValidator
    {
        private final byte[] missingCharacter;
        private final Paint paint;
        
        public CharacterValidator(final String s) {
            this.paint = new Paint();
            this.missingCharacter = getPixels(this.drawBitmap(s));
        }
        
        private Bitmap drawBitmap(final String s) {
            final Bitmap bitmap = Bitmap.createBitmap(32, 48, Bitmap$Config.ALPHA_8);
            new Canvas(bitmap).drawText(s, 0.0f, 24.0f, this.paint);
            return bitmap;
        }
        
        private static byte[] getPixels(final Bitmap bitmap) {
            final ByteBuffer allocate = ByteBuffer.allocate(bitmap.getAllocationByteCount());
            try {
                bitmap.copyPixelsToBuffer((Buffer)allocate);
                return allocate.array();
            }
            catch (RuntimeException ex) {
                if ("Buffer not large enough for pixels".equals(ex.getMessage())) {
                    return allocate.array();
                }
                throw ex;
            }
        }
        
        public boolean characterIsMissingInFont(final String s) {
            return Arrays.equals(getPixels(this.drawBitmap(s)), this.missingCharacter);
        }
    }
    
    private static final class LocaleDescriptor implements Comparable<LocaleDescriptor>
    {
        private static final Collator COLLATOR;
        private final String nativeName;
        public final String tag;
        
        static {
            COLLATOR = Collator.getInstance(Locale.US);
        }
        
        public LocaleDescriptor(final String s) {
            this(Locales.parseLocaleCode(s), s);
        }
        
        public LocaleDescriptor(final Locale locale, String displayName) {
            this.tag = displayName;
            if (LocaleListPreference.languageCodeToNameMap.containsKey(locale.getLanguage())) {
                displayName = LocaleListPreference.languageCodeToNameMap.get(locale.getLanguage());
            }
            else {
                displayName = locale.getDisplayName(locale);
            }
            if (TextUtils.isEmpty((CharSequence)displayName)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Display name is empty. Using ");
                sb.append(locale.toString());
                Log.w("GeckoLocaleList", sb.toString());
                this.nativeName = locale.toString();
                return;
            }
            if (Character.getDirectionality(displayName.charAt(0)) == 0) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(displayName.substring(0, 1).toUpperCase(locale));
                sb2.append(displayName.substring(1));
                this.nativeName = sb2.toString();
                return;
            }
            this.nativeName = displayName;
        }
        
        @Override
        public int compareTo(final LocaleDescriptor localeDescriptor) {
            return LocaleDescriptor.COLLATOR.compare(this.nativeName, localeDescriptor.nativeName);
        }
        
        @Override
        public boolean equals(final Object o) {
            final boolean b = o instanceof LocaleDescriptor;
            boolean b2 = false;
            if (b) {
                if (this.compareTo((LocaleDescriptor)o) == 0) {
                    b2 = true;
                }
                return b2;
            }
            return false;
        }
        
        public String getDisplayName() {
            return this.nativeName;
        }
        
        public String getTag() {
            return this.tag;
        }
        
        @Override
        public int hashCode() {
            return this.tag.hashCode();
        }
        
        public boolean isUsable(final CharacterValidator characterValidator) {
            return (!this.tag.equals("bn-IN") || this.nativeName.startsWith("\u09ac\u09be\u0982\u09b2\u09be")) && ((!this.tag.equals("or") && !this.tag.equals("my") && !this.tag.equals("pa-IN") && !this.tag.equals("gu-IN") && !this.tag.equals("bn-IN")) || !characterValidator.characterIsMissingInFont(this.nativeName.substring(0, 1)));
        }
        
        @Override
        public String toString() {
            return this.nativeName;
        }
    }
}

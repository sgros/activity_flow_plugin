package org.mozilla.focus.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.preference.ListPreference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import java.nio.ByteBuffer;
import java.text.Collator;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import org.mozilla.focus.locale.LocaleManager;
import org.mozilla.focus.locale.Locales;
import org.mozilla.rocket.C0769R;

public class LocaleListPreference extends ListPreference {
    private static final Map<String, String> languageCodeToNameMap = new HashMap();
    private CharacterValidator characterValidator;
    private volatile Locale entriesLocale;

    private static class CharacterValidator {
        private final byte[] missingCharacter;
        private final Paint paint = new Paint();

        public CharacterValidator(String str) {
            this.missingCharacter = getPixels(drawBitmap(str));
        }

        private Bitmap drawBitmap(String str) {
            Bitmap createBitmap = Bitmap.createBitmap(32, 48, Config.ALPHA_8);
            new Canvas(createBitmap).drawText(str, 0.0f, 24.0f, this.paint);
            return createBitmap;
        }

        private static byte[] getPixels(Bitmap bitmap) {
            ByteBuffer allocate = ByteBuffer.allocate(bitmap.getAllocationByteCount());
            try {
                bitmap.copyPixelsToBuffer(allocate);
                return allocate.array();
            } catch (RuntimeException e) {
                if ("Buffer not large enough for pixels".equals(e.getMessage())) {
                    return allocate.array();
                }
                throw e;
            }
        }

        public boolean characterIsMissingInFont(String str) {
            return Arrays.equals(getPixels(drawBitmap(str)), this.missingCharacter);
        }
    }

    private static final class LocaleDescriptor implements Comparable<LocaleDescriptor> {
        private static final Collator COLLATOR = Collator.getInstance(Locale.US);
        private final String nativeName;
        public final String tag;

        public LocaleDescriptor(String str) {
            this(Locales.parseLocaleCode(str), str);
        }

        public LocaleDescriptor(Locale locale, String str) {
            CharSequence charSequence;
            this.tag = str;
            if (LocaleListPreference.languageCodeToNameMap.containsKey(locale.getLanguage())) {
                charSequence = (String) LocaleListPreference.languageCodeToNameMap.get(locale.getLanguage());
            } else {
                charSequence = locale.getDisplayName(locale);
            }
            if (TextUtils.isEmpty(charSequence)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Display name is empty. Using ");
                stringBuilder.append(locale.toString());
                Log.w("GeckoLocaleList", stringBuilder.toString());
                this.nativeName = locale.toString();
            } else if (Character.getDirectionality(charSequence.charAt(0)) == (byte) 0) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(charSequence.substring(0, 1).toUpperCase(locale));
                stringBuilder2.append(charSequence.substring(1));
                this.nativeName = stringBuilder2.toString();
            } else {
                this.nativeName = charSequence;
            }
        }

        public String getTag() {
            return this.tag;
        }

        public String getDisplayName() {
            return this.nativeName;
        }

        public String toString() {
            return this.nativeName;
        }

        public boolean equals(Object obj) {
            boolean z = false;
            if (!(obj instanceof LocaleDescriptor)) {
                return false;
            }
            if (compareTo((LocaleDescriptor) obj) == 0) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            return this.tag.hashCode();
        }

        public int compareTo(LocaleDescriptor localeDescriptor) {
            return COLLATOR.compare(this.nativeName, localeDescriptor.nativeName);
        }

        public boolean isUsable(CharacterValidator characterValidator) {
            if (this.tag.equals("bn-IN") && !this.nativeName.startsWith("বাংলা")) {
                return false;
            }
            if ((this.tag.equals("or") || this.tag.equals("my") || this.tag.equals("pa-IN") || this.tag.equals("gu-IN") || this.tag.equals("bn-IN")) && characterValidator.characterIsMissingInFont(this.nativeName.substring(0, 1))) {
                return false;
            }
            return true;
        }
    }

    static {
        languageCodeToNameMap.put("ast", "Asturianu");
        languageCodeToNameMap.put("cak", "Kaqchikel");
        languageCodeToNameMap.put("ia", "Interlingua");
        languageCodeToNameMap.put("meh", "Tu´un savi ñuu Yasi'í Yuku Iti");
        languageCodeToNameMap.put("mix", "Tu'un savi");
        languageCodeToNameMap.put("trs", "Triqui");
        languageCodeToNameMap.put("zam", "DíɁztè");
        languageCodeToNameMap.put("oc", "occitan");
        languageCodeToNameMap.put("an", "Aragonés");
        languageCodeToNameMap.put("tt", "татарча");
        languageCodeToNameMap.put("wo", "Wolof");
        languageCodeToNameMap.put("anp", "अंगिका");
        languageCodeToNameMap.put("ixl", "Ixil");
        languageCodeToNameMap.put("pai", "Paa ipai");
        languageCodeToNameMap.put("quy", "Chanka Qhichwa");
        languageCodeToNameMap.put("ay", "Aimara");
        languageCodeToNameMap.put("quc", "K'iche'");
        languageCodeToNameMap.put("tsz", "P'urhepecha");
        languageCodeToNameMap.put("mai", "मैथिली/মৈথিলী");
        languageCodeToNameMap.put("jv", "Basa Jawa");
        languageCodeToNameMap.put("su", "Basa Sunda");
        languageCodeToNameMap.put("ace", "Basa Acèh");
        languageCodeToNameMap.put("gor", "Bahasa Hulontalo");
    }

    public LocaleListPreference(Context context) {
        this(context, null);
    }

    public LocaleListPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToActivity() {
        super.onAttachedToActivity();
        this.characterValidator = new CharacterValidator(" ");
        buildList();
    }

    private LocaleDescriptor[] getUsableLocales() {
        Collection<String> packagedLocaleTags = LocaleManager.getPackagedLocaleTags(getContext());
        HashSet hashSet = new HashSet(packagedLocaleTags.size());
        for (String str : packagedLocaleTags) {
            LocaleDescriptor localeDescriptor = new LocaleDescriptor(str);
            if (localeDescriptor.isUsable(this.characterValidator)) {
                hashSet.add(localeDescriptor);
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Skipping locale ");
                stringBuilder.append(str);
                stringBuilder.append(" on this device.");
                Log.w("GeckoLocaleList", stringBuilder.toString());
            }
        }
        int size = hashSet.size();
        LocaleDescriptor[] localeDescriptorArr = (LocaleDescriptor[]) hashSet.toArray(new LocaleDescriptor[size]);
        Arrays.sort(localeDescriptorArr, 0, size);
        return localeDescriptorArr;
    }

    /* Access modifiers changed, original: protected */
    public void onDialogClosed(boolean z) {
        super.onDialogClosed(z);
        Locale selectedLocale = getSelectedLocale();
        LocaleManager.getInstance().updateConfiguration(getContext(), selectedLocale);
    }

    private Locale getSelectedLocale() {
        String value = getValue();
        if (value == null || value.equals("")) {
            return Locale.getDefault();
        }
        return Locales.parseLocaleCode(value);
    }

    public CharSequence getSummary() {
        String value = getValue();
        if (TextUtils.isEmpty(value)) {
            return getContext().getString(C0769R.string.preference_language_systemdefault);
        }
        return new LocaleDescriptor(value).getDisplayName();
    }

    private void buildList() {
        Locale locale = Locale.getDefault();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Building locales list. Current locale: ");
        stringBuilder.append(locale);
        Log.d("GeckoLocaleList", stringBuilder.toString());
        if (!locale.equals(this.entriesLocale) || getEntries() == null) {
            LocaleDescriptor[] usableLocales = getUsableLocales();
            int length = usableLocales.length;
            this.entriesLocale = locale;
            int i = length + 1;
            String[] strArr = new String[i];
            String[] strArr2 = new String[i];
            int i2 = 0;
            strArr[0] = getContext().getString(C0769R.string.preference_language_systemdefault);
            strArr2[0] = "";
            while (i2 < length) {
                String displayName = usableLocales[i2].getDisplayName();
                String tag = usableLocales[i2].getTag();
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(displayName);
                stringBuilder2.append(" => ");
                stringBuilder2.append(tag);
                Log.v("GeckoLocaleList", stringBuilder2.toString());
                i2++;
                strArr[i2] = displayName;
                strArr2[i2] = tag;
            }
            setEntries(strArr);
            setEntryValues(strArr2);
            return;
        }
        Log.v("GeckoLocaleList", "No need to build list.");
    }
}

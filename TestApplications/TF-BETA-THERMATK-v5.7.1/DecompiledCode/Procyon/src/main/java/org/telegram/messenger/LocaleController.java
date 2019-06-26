// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.content.Intent;
import java.util.TimeZone;
import java.util.Iterator;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.NumberFormat;
import java.util.Currency;
import android.content.res.Configuration;
import java.util.Map;
import android.content.SharedPreferences$Editor;
import android.content.SharedPreferences;
import android.content.Context;
import org.xmlpull.v1.XmlPullParser;
import java.io.InputStream;
import java.io.FileInputStream;
import android.util.Xml;
import java.io.File;
import android.text.TextUtils;
import java.util.Date;
import java.util.Calendar;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.text.format.DateFormat;
import java.util.ArrayList;
import java.util.Locale;
import org.telegram.messenger.time.FastDateFormat;
import java.util.HashMap;

public class LocaleController
{
    private static volatile LocaleController Instance;
    static final int QUANTITY_FEW = 8;
    static final int QUANTITY_MANY = 16;
    static final int QUANTITY_ONE = 2;
    static final int QUANTITY_OTHER = 0;
    static final int QUANTITY_TWO = 4;
    static final int QUANTITY_ZERO = 1;
    public static boolean is24HourFormat = false;
    public static boolean isRTL = false;
    public static int nameDisplayOrder = 1;
    private HashMap<String, PluralRules> allRules;
    private boolean changingConfiguration;
    public FastDateFormat chatDate;
    public FastDateFormat chatFullDate;
    private HashMap<String, String> currencyValues;
    private Locale currentLocale;
    private LocaleInfo currentLocaleInfo;
    private PluralRules currentPluralRules;
    private String currentSystemLocale;
    public FastDateFormat formatterBannedUntil;
    public FastDateFormat formatterBannedUntilThisYear;
    public FastDateFormat formatterDay;
    public FastDateFormat formatterDayMonth;
    public FastDateFormat formatterScheduleDay;
    public FastDateFormat formatterStats;
    public FastDateFormat formatterWeek;
    public FastDateFormat formatterYear;
    public FastDateFormat formatterYearMax;
    private String languageOverride;
    public ArrayList<LocaleInfo> languages;
    public HashMap<String, LocaleInfo> languagesDict;
    private boolean loadingRemoteLanguages;
    private HashMap<String, String> localeValues;
    private ArrayList<LocaleInfo> otherLanguages;
    private boolean reloadLastFile;
    public ArrayList<LocaleInfo> remoteLanguages;
    public HashMap<String, LocaleInfo> remoteLanguagesDict;
    private HashMap<String, String> ruTranslitChars;
    private Locale systemDefaultLocale;
    private HashMap<String, String> translitChars;
    public ArrayList<LocaleInfo> unofficialLanguages;
    
    public LocaleController() {
        this.allRules = new HashMap<String, PluralRules>();
        this.localeValues = new HashMap<String, String>();
        boolean b = false;
        this.changingConfiguration = false;
        this.languages = new ArrayList<LocaleInfo>();
        this.unofficialLanguages = new ArrayList<LocaleInfo>();
        this.remoteLanguages = new ArrayList<LocaleInfo>();
        this.remoteLanguagesDict = new HashMap<String, LocaleInfo>();
        this.languagesDict = new HashMap<String, LocaleInfo>();
        this.otherLanguages = new ArrayList<LocaleInfo>();
        this.addRules(new String[] { "bem", "brx", "da", "de", "el", "en", "eo", "es", "et", "fi", "fo", "gl", "he", "iw", "it", "nb", "nl", "nn", "no", "sv", "af", "bg", "bn", "ca", "eu", "fur", "fy", "gu", "ha", "is", "ku", "lb", "ml", "mr", "nah", "ne", "om", "or", "pa", "pap", "ps", "so", "sq", "sw", "ta", "te", "tk", "ur", "zu", "mn", "gsw", "chr", "rm", "pt", "an", "ast" }, (PluralRules)new PluralRules_One());
        this.addRules(new String[] { "cs", "sk" }, (PluralRules)new PluralRules_Czech());
        this.addRules(new String[] { "ff", "fr", "kab" }, (PluralRules)new PluralRules_French());
        this.addRules(new String[] { "hr", "ru", "sr", "uk", "be", "bs", "sh" }, (PluralRules)new PluralRules_Balkan());
        this.addRules(new String[] { "lv" }, (PluralRules)new PluralRules_Latvian());
        this.addRules(new String[] { "lt" }, (PluralRules)new PluralRules_Lithuanian());
        this.addRules(new String[] { "pl" }, (PluralRules)new PluralRules_Polish());
        this.addRules(new String[] { "ro", "mo" }, (PluralRules)new PluralRules_Romanian());
        this.addRules(new String[] { "sl" }, (PluralRules)new PluralRules_Slovenian());
        this.addRules(new String[] { "ar" }, (PluralRules)new PluralRules_Arabic());
        this.addRules(new String[] { "mk" }, (PluralRules)new PluralRules_Macedonian());
        this.addRules(new String[] { "cy" }, (PluralRules)new PluralRules_Welsh());
        this.addRules(new String[] { "br" }, (PluralRules)new PluralRules_Breton());
        this.addRules(new String[] { "lag" }, (PluralRules)new PluralRules_Langi());
        this.addRules(new String[] { "shi" }, (PluralRules)new PluralRules_Tachelhit());
        this.addRules(new String[] { "mt" }, (PluralRules)new PluralRules_Maltese());
        this.addRules(new String[] { "ga", "se", "sma", "smi", "smj", "smn", "sms" }, (PluralRules)new PluralRules_Two());
        this.addRules(new String[] { "ak", "am", "bh", "fil", "tl", "guw", "hi", "ln", "mg", "nso", "ti", "wa" }, (PluralRules)new PluralRules_Zero());
        this.addRules(new String[] { "az", "bm", "fa", "ig", "hu", "ja", "kde", "kea", "ko", "my", "ses", "sg", "to", "tr", "vi", "wo", "yo", "zh", "bo", "dz", "id", "jv", "jw", "ka", "km", "kn", "ms", "th", "in" }, (PluralRules)new PluralRules_None());
        final LocaleInfo localeInfo = new LocaleInfo();
        localeInfo.name = "English";
        localeInfo.nameEnglish = "English";
        localeInfo.pluralLangCode = "en";
        localeInfo.shortName = "en";
        localeInfo.pathToFile = null;
        localeInfo.builtIn = true;
        this.languages.add(localeInfo);
        this.languagesDict.put(localeInfo.shortName, localeInfo);
        final LocaleInfo localeInfo2 = new LocaleInfo();
        localeInfo2.name = "Italiano";
        localeInfo2.nameEnglish = "Italian";
        localeInfo2.pluralLangCode = "it";
        localeInfo2.shortName = "it";
        localeInfo2.pathToFile = null;
        localeInfo2.builtIn = true;
        this.languages.add(localeInfo2);
        this.languagesDict.put(localeInfo2.shortName, localeInfo2);
        final LocaleInfo localeInfo3 = new LocaleInfo();
        localeInfo3.name = "Espa\u00f1ol";
        localeInfo3.nameEnglish = "Spanish";
        localeInfo3.pluralLangCode = "es";
        localeInfo3.shortName = "es";
        localeInfo3.builtIn = true;
        this.languages.add(localeInfo3);
        this.languagesDict.put(localeInfo3.shortName, localeInfo3);
        final LocaleInfo localeInfo4 = new LocaleInfo();
        localeInfo4.name = "Deutsch";
        localeInfo4.nameEnglish = "German";
        localeInfo4.pluralLangCode = "de";
        localeInfo4.shortName = "de";
        localeInfo4.pathToFile = null;
        localeInfo4.builtIn = true;
        this.languages.add(localeInfo4);
        this.languagesDict.put(localeInfo4.shortName, localeInfo4);
        final LocaleInfo localeInfo5 = new LocaleInfo();
        localeInfo5.name = "Nederlands";
        localeInfo5.nameEnglish = "Dutch";
        localeInfo5.pluralLangCode = "nl";
        localeInfo5.shortName = "nl";
        localeInfo5.pathToFile = null;
        localeInfo5.builtIn = true;
        this.languages.add(localeInfo5);
        this.languagesDict.put(localeInfo5.shortName, localeInfo5);
        final LocaleInfo localeInfo6 = new LocaleInfo();
        localeInfo6.name = "\u0627\u0644\u0639\u0631\u0628\u064a\u0629";
        localeInfo6.nameEnglish = "Arabic";
        localeInfo6.pluralLangCode = "ar";
        localeInfo6.shortName = "ar";
        localeInfo6.pathToFile = null;
        localeInfo6.builtIn = true;
        localeInfo6.isRtl = true;
        this.languages.add(localeInfo6);
        this.languagesDict.put(localeInfo6.shortName, localeInfo6);
        final LocaleInfo localeInfo7 = new LocaleInfo();
        localeInfo7.name = "Portugu\u00eas (Brasil)";
        localeInfo7.nameEnglish = "Portuguese (Brazil)";
        localeInfo7.pluralLangCode = "pt_br";
        localeInfo7.shortName = "pt_br";
        localeInfo7.pathToFile = null;
        localeInfo7.builtIn = true;
        this.languages.add(localeInfo7);
        this.languagesDict.put(localeInfo7.shortName, localeInfo7);
        final LocaleInfo localeInfo8 = new LocaleInfo();
        localeInfo8.name = "\ud55c\uad6d\uc5b4";
        localeInfo8.nameEnglish = "Korean";
        localeInfo8.pluralLangCode = "ko";
        localeInfo8.shortName = "ko";
        localeInfo8.pathToFile = null;
        localeInfo8.builtIn = true;
        this.languages.add(localeInfo8);
        this.languagesDict.put(localeInfo8.shortName, localeInfo8);
        this.loadOtherLanguages();
        if (this.remoteLanguages.isEmpty()) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$LocaleController$NNZIDoHieDUSrd9BgBq36GRonDE(this));
        }
        for (int i = 0; i < this.otherLanguages.size(); ++i) {
            final LocaleInfo localeInfo9 = this.otherLanguages.get(i);
            this.languages.add(localeInfo9);
            this.languagesDict.put(localeInfo9.getKey(), localeInfo9);
        }
        for (int j = 0; j < this.remoteLanguages.size(); ++j) {
            final LocaleInfo localeInfo10 = this.remoteLanguages.get(j);
            final LocaleInfo languageFromDict = this.getLanguageFromDict(localeInfo10.getKey());
            if (languageFromDict != null) {
                languageFromDict.pathToFile = localeInfo10.pathToFile;
                languageFromDict.version = localeInfo10.version;
                languageFromDict.baseVersion = localeInfo10.baseVersion;
                languageFromDict.serverIndex = localeInfo10.serverIndex;
                this.remoteLanguages.set(j, languageFromDict);
            }
            else {
                this.languages.add(localeInfo10);
                this.languagesDict.put(localeInfo10.getKey(), localeInfo10);
            }
        }
        for (int k = 0; k < this.unofficialLanguages.size(); ++k) {
            final LocaleInfo value = this.unofficialLanguages.get(k);
            final LocaleInfo languageFromDict2 = this.getLanguageFromDict(value.getKey());
            if (languageFromDict2 != null) {
                languageFromDict2.pathToFile = value.pathToFile;
                languageFromDict2.version = value.version;
                languageFromDict2.baseVersion = value.baseVersion;
                languageFromDict2.serverIndex = value.serverIndex;
                this.unofficialLanguages.set(k, languageFromDict2);
            }
            else {
                this.languagesDict.put(value.getKey(), value);
            }
        }
        this.systemDefaultLocale = Locale.getDefault();
        LocaleController.is24HourFormat = DateFormat.is24HourFormat(ApplicationLoader.applicationContext);
        try {
            final String string = MessagesController.getGlobalMainSettings().getString("language", (String)null);
            LocaleInfo localeInfo11;
            if (string != null) {
                final LocaleInfo languageFromDict3 = this.getLanguageFromDict(string);
                if ((localeInfo11 = languageFromDict3) != null) {
                    b = true;
                    localeInfo11 = languageFromDict3;
                }
            }
            else {
                localeInfo11 = null;
            }
            LocaleInfo languageFromDict4;
            if ((languageFromDict4 = localeInfo11) == null) {
                languageFromDict4 = localeInfo11;
                if (this.systemDefaultLocale.getLanguage() != null) {
                    languageFromDict4 = this.getLanguageFromDict(this.systemDefaultLocale.getLanguage());
                }
            }
            LocaleInfo localeInfo12;
            if ((localeInfo12 = languageFromDict4) == null && (localeInfo12 = this.getLanguageFromDict(this.getLocaleString(this.systemDefaultLocale))) == null) {
                localeInfo12 = this.getLanguageFromDict("en");
            }
            this.applyLanguage(localeInfo12, b, true, UserConfig.selectedAccount);
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        try {
            ApplicationLoader.applicationContext.registerReceiver((BroadcastReceiver)new TimeZoneChangedReceiver(), new IntentFilter("android.intent.action.TIMEZONE_CHANGED"));
        }
        catch (Exception ex2) {
            FileLog.e(ex2);
        }
        AndroidUtilities.runOnUIThread(new _$$Lambda$LocaleController$U712xrnt9cCu1iJYrRj7Tc_xRYo(this));
    }
    
    public static String addNbsp(final String s) {
        return s.replace(' ', ' ');
    }
    
    private void addRules(final String[] array, final PluralRules value) {
        for (int length = array.length, i = 0; i < length; ++i) {
            this.allRules.put(array[i], value);
        }
    }
    
    private void applyRemoteLanguage(final LocaleInfo localeInfo, final String s, final boolean b, final int n) {
        if (localeInfo != null) {
            if (localeInfo == null || localeInfo.isRemote() || localeInfo.isUnofficial()) {
                if (localeInfo.hasBaseLang() && (s == null || s.equals(localeInfo.baseLangCode))) {
                    if (localeInfo.baseVersion != 0 && !b) {
                        if (localeInfo.hasBaseLang()) {
                            final TLRPC.TL_langpack_getDifference tl_langpack_getDifference = new TLRPC.TL_langpack_getDifference();
                            tl_langpack_getDifference.from_version = localeInfo.baseVersion;
                            tl_langpack_getDifference.lang_code = localeInfo.getBaseLangCode();
                            tl_langpack_getDifference.lang_pack = "";
                            ConnectionsManager.getInstance(n).sendRequest(tl_langpack_getDifference, new _$$Lambda$LocaleController$s_X_ikLryDU_N2xVuPEFJJqkOk0(this, localeInfo, n), 8);
                        }
                    }
                    else {
                        final TLRPC.TL_langpack_getLangPack tl_langpack_getLangPack = new TLRPC.TL_langpack_getLangPack();
                        tl_langpack_getLangPack.lang_code = localeInfo.getBaseLangCode();
                        ConnectionsManager.getInstance(n).sendRequest(tl_langpack_getLangPack, new _$$Lambda$LocaleController$C7GeSsDILXp_14TJUT5eNNANB6o(this, localeInfo, n), 8);
                    }
                }
                if (s == null || s.equals(localeInfo.shortName)) {
                    if (localeInfo.version != 0 && !b) {
                        final TLRPC.TL_langpack_getDifference tl_langpack_getDifference2 = new TLRPC.TL_langpack_getDifference();
                        tl_langpack_getDifference2.from_version = localeInfo.version;
                        tl_langpack_getDifference2.lang_code = localeInfo.getLangCode();
                        tl_langpack_getDifference2.lang_pack = "";
                        ConnectionsManager.getInstance(n).sendRequest(tl_langpack_getDifference2, new _$$Lambda$LocaleController$JT38tfE7_oOUNrZDivXXSv_b_vg(this, localeInfo, n), 8);
                    }
                    else {
                        for (int i = 0; i < 3; ++i) {
                            ConnectionsManager.setLangCode(localeInfo.getLangCode());
                        }
                        final TLRPC.TL_langpack_getLangPack tl_langpack_getLangPack2 = new TLRPC.TL_langpack_getLangPack();
                        tl_langpack_getLangPack2.lang_code = localeInfo.getLangCode();
                        ConnectionsManager.getInstance(n).sendRequest(tl_langpack_getLangPack2, new _$$Lambda$LocaleController$h0eSozyCLW0Mpog1omLBJ_00K_I(this, localeInfo, n), 8);
                    }
                }
            }
        }
    }
    
    private FastDateFormat createFormatter(Locale locale, final String s, final String s2) {
        Label_0017: {
            if (s != null) {
                final String s3 = s;
                if (s.length() != 0) {
                    break Label_0017;
                }
            }
            final String s3 = s2;
            try {
                locale = (Locale)FastDateFormat.getInstance(s3, locale);
            }
            catch (Exception ex) {
                locale = (Locale)FastDateFormat.getInstance(s2, locale);
            }
        }
        return (FastDateFormat)locale;
    }
    
    private String escapeString(final String s) {
        if (s.contains("[CDATA")) {
            return s;
        }
        return s.replace("<", "&lt;").replace(">", "&gt;").replace("& ", "&amp; ");
    }
    
    public static String formatCallDuration(int n) {
        if (n > 3600) {
            final String formatPluralString = formatPluralString("Hours", n / 3600);
            n = n % 3600 / 60;
            String string = formatPluralString;
            if (n > 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append(formatPluralString);
                sb.append(", ");
                sb.append(formatPluralString("Minutes", n));
                string = sb.toString();
            }
            return string;
        }
        if (n > 60) {
            return formatPluralString("Minutes", n / 60);
        }
        return formatPluralString("Seconds", n);
    }
    
    public static String formatDate(long n) {
        n *= 1000L;
        try {
            final Calendar instance = Calendar.getInstance();
            final int value = instance.get(6);
            final int value2 = instance.get(1);
            instance.setTimeInMillis(n);
            final int value3 = instance.get(6);
            final int value4 = instance.get(1);
            if (value3 == value && value2 == value4) {
                return getInstance().formatterDay.format(new Date(n));
            }
            if (value3 + 1 == value && value2 == value4) {
                return getString("Yesterday", 2131561134);
            }
            if (Math.abs(System.currentTimeMillis() - n) < 31536000000L) {
                return getInstance().formatterDayMonth.format(new Date(n));
            }
            return getInstance().formatterYear.format(new Date(n));
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return "LOC_ERR: formatDate";
        }
    }
    
    public static String formatDateAudio(long abs) {
        final long date = abs * 1000L;
        try {
            final Calendar instance = Calendar.getInstance();
            final int value = instance.get(6);
            final int value2 = instance.get(1);
            instance.setTimeInMillis(date);
            final int value3 = instance.get(6);
            final int value4 = instance.get(1);
            if (value3 == value && value2 == value4) {
                return formatString("TodayAtFormatted", 2131560908, getInstance().formatterDay.format(new Date(date)));
            }
            if (value3 + 1 == value && value2 == value4) {
                return formatString("YesterdayAtFormatted", 2131561136, getInstance().formatterDay.format(new Date(date)));
            }
            abs = Math.abs(System.currentTimeMillis() - date);
            if (abs < 31536000000L) {
                return formatString("formatDateAtTime", 2131561210, getInstance().formatterDayMonth.format(new Date(date)), getInstance().formatterDay.format(new Date(date)));
            }
            return formatString("formatDateAtTime", 2131561210, getInstance().formatterYear.format(new Date(date)), getInstance().formatterDay.format(new Date(date)));
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return "LOC_ERR";
        }
    }
    
    public static String formatDateCallLog(long date) {
        date *= 1000L;
        try {
            final Calendar instance = Calendar.getInstance();
            final int value = instance.get(6);
            final int value2 = instance.get(1);
            instance.setTimeInMillis(date);
            final int value3 = instance.get(6);
            final int value4 = instance.get(1);
            if (value3 == value && value2 == value4) {
                return getInstance().formatterDay.format(new Date(date));
            }
            if (value3 + 1 == value && value2 == value4) {
                return formatString("YesterdayAtFormatted", 2131561136, getInstance().formatterDay.format(new Date(date)));
            }
            if (Math.abs(System.currentTimeMillis() - date) < 31536000000L) {
                return formatString("formatDateAtTime", 2131561210, getInstance().chatDate.format(new Date(date)), getInstance().formatterDay.format(new Date(date)));
            }
            return formatString("formatDateAtTime", 2131561210, getInstance().chatFullDate.format(new Date(date)), getInstance().formatterDay.format(new Date(date)));
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return "LOC_ERR";
        }
    }
    
    public static String formatDateChat(long timeInMillis) {
        try {
            final Calendar instance = Calendar.getInstance();
            timeInMillis *= 1000L;
            instance.setTimeInMillis(timeInMillis);
            if (Math.abs(System.currentTimeMillis() - timeInMillis) < 31536000000L) {
                return getInstance().chatDate.format(timeInMillis);
            }
            return getInstance().chatFullDate.format(timeInMillis);
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return "LOC_ERR: formatDateChat";
        }
    }
    
    public static String formatDateForBan(long date) {
        date *= 1000L;
        try {
            final Calendar instance = Calendar.getInstance();
            final int value = instance.get(1);
            instance.setTimeInMillis(date);
            if (value == instance.get(1)) {
                return getInstance().formatterBannedUntilThisYear.format(new Date(date));
            }
            return getInstance().formatterBannedUntil.format(new Date(date));
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return "LOC_ERR";
        }
    }
    
    public static String formatDateOnline(long abs) {
        final long date = abs * 1000L;
        try {
            final Calendar instance = Calendar.getInstance();
            final int value = instance.get(6);
            final int value2 = instance.get(1);
            instance.setTimeInMillis(date);
            final int value3 = instance.get(6);
            final int value4 = instance.get(1);
            if (value3 == value && value2 == value4) {
                return formatString("LastSeenFormatted", 2131559738, formatString("TodayAtFormatted", 2131560908, getInstance().formatterDay.format(new Date(date))));
            }
            if (value3 + 1 == value && value2 == value4) {
                return formatString("LastSeenFormatted", 2131559738, formatString("YesterdayAtFormatted", 2131561136, getInstance().formatterDay.format(new Date(date))));
            }
            abs = Math.abs(System.currentTimeMillis() - date);
            if (abs < 31536000000L) {
                return formatString("LastSeenDateFormatted", 2131559735, formatString("formatDateAtTime", 2131561210, getInstance().formatterDayMonth.format(new Date(date)), getInstance().formatterDay.format(new Date(date))));
            }
            return formatString("LastSeenDateFormatted", 2131559735, formatString("formatDateAtTime", 2131561210, getInstance().formatterYear.format(new Date(date)), getInstance().formatterDay.format(new Date(date))));
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return "LOC_ERR";
        }
    }
    
    public static String formatLocationLeftTime(int n) {
        final int n2 = n / 60 / 60;
        n -= n2 * 60 * 60;
        final int n3 = n / 60;
        final int i = n - n3 * 60;
        final int n4 = 1;
        n = 1;
        String s;
        if (n2 != 0) {
            if (n3 <= 30) {
                n = 0;
            }
            s = String.format("%dh", n2 + n);
        }
        else if (n3 != 0) {
            if (i > 30) {
                n = n4;
            }
            else {
                n = 0;
            }
            s = String.format("%d", n3 + n);
        }
        else {
            s = String.format("%d", i);
        }
        return s;
    }
    
    public static String formatLocationUpdateDate(long abs) {
        final long date = abs * 1000L;
        try {
            final Calendar instance = Calendar.getInstance();
            final int value = instance.get(6);
            final int value2 = instance.get(1);
            instance.setTimeInMillis(date);
            final int value3 = instance.get(6);
            final int value4 = instance.get(1);
            if (value3 == value && value2 == value4) {
                final int n = (int)(ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime() - date / 1000L) / 60;
                if (n < 1) {
                    return getString("LocationUpdatedJustNow", 2131559782);
                }
                if (n < 60) {
                    return formatPluralString("UpdatedMinutes", n);
                }
                return formatString("LocationUpdatedFormatted", 2131559781, formatString("TodayAtFormatted", 2131560908, getInstance().formatterDay.format(new Date(date))));
            }
            else {
                if (value3 + 1 == value && value2 == value4) {
                    return formatString("LocationUpdatedFormatted", 2131559781, formatString("YesterdayAtFormatted", 2131561136, getInstance().formatterDay.format(new Date(date))));
                }
                abs = Math.abs(System.currentTimeMillis() - date);
                if (abs < 31536000000L) {
                    return formatString("LocationUpdatedFormatted", 2131559781, formatString("formatDateAtTime", 2131561210, getInstance().formatterDayMonth.format(new Date(date)), getInstance().formatterDay.format(new Date(date))));
                }
                return formatString("LocationUpdatedFormatted", 2131559781, formatString("formatDateAtTime", 2131561210, getInstance().formatterYear.format(new Date(date)), getInstance().formatterDay.format(new Date(date))));
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return "LOC_ERR";
        }
    }
    
    public static String formatPluralString(String string, final int i) {
        if (string != null && string.length() != 0 && getInstance().currentPluralRules != null) {
            final String stringForQuantity = getInstance().stringForQuantity(getInstance().currentPluralRules.quantityForNumber(i));
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append("_");
            sb.append(stringForQuantity);
            string = sb.toString();
            return formatString(string, ApplicationLoader.applicationContext.getResources().getIdentifier(string, "string", ApplicationLoader.applicationContext.getPackageName()), i);
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("LOC_ERR:");
        sb2.append(string);
        return sb2.toString();
    }
    
    public static String formatSectionDate(final long n) {
        try {
            final Calendar instance = Calendar.getInstance();
            final int value = instance.get(1);
            instance.setTimeInMillis(n * 1000L);
            final int value2 = instance.get(1);
            final int value3 = instance.get(2);
            final String[] array = { getString("January", 2131559702), getString("February", 2131559481), getString("March", 2131559806), getString("April", 2131558641), getString("May", 2131559818), getString("June", 2131559708), getString("July", 2131559706), getString("August", 2131558739), getString("September", 2131560723), getString("October", 2131560098), getString("November", 2131560095), getString("December", 2131559222) };
            if (value == value2) {
                return array[value3];
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(array[value3]);
            sb.append(" ");
            sb.append(value2);
            return sb.toString();
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return "LOC_ERR";
        }
    }
    
    public static String formatShortNumber(int n, final int[] array) {
        final StringBuilder sb = new StringBuilder();
        final int n2 = 0;
        int n3 = n;
        n = n2;
        while (true) {
            final int n4 = n3 / 1000;
            if (n4 <= 0) {
                break;
            }
            sb.append("K");
            n = n3 % 1000 / 100;
            n3 = n4;
        }
        if (array != null) {
            final double v = n3;
            final double v2 = n;
            Double.isNaN(v2);
            final double n5 = v2 / 10.0;
            Double.isNaN(v);
            double n6 = v + n5;
            for (int i = 0; i < sb.length(); ++i) {
                n6 *= 1000.0;
            }
            array[0] = (int)n6;
        }
        if (n != 0 && sb.length() > 0) {
            if (sb.length() == 2) {
                return String.format(Locale.US, "%d.%dM", n3, n);
            }
            return String.format(Locale.US, "%d.%d%s", n3, n, sb.toString());
        }
        else {
            if (sb.length() == 2) {
                return String.format(Locale.US, "%dM", n3);
            }
            return String.format(Locale.US, "%d%s", n3, sb.toString());
        }
    }
    
    public static String formatString(final String s, final int n, final Object... array) {
        try {
            String s2;
            if (BuildVars.USE_CLOUD_STRINGS) {
                s2 = getInstance().localeValues.get(s);
            }
            else {
                s2 = null;
            }
            String string = s2;
            if (s2 == null) {
                string = ApplicationLoader.applicationContext.getString(n);
            }
            if (getInstance().currentLocale != null) {
                return String.format(getInstance().currentLocale, string, array);
            }
            return String.format(string, array);
        }
        catch (Exception ex) {
            FileLog.e(ex);
            final StringBuilder sb = new StringBuilder();
            sb.append("LOC_ERR: ");
            sb.append(s);
            return sb.toString();
        }
    }
    
    public static String formatStringSimple(final String str, final Object... array) {
        try {
            if (getInstance().currentLocale != null) {
                return String.format(getInstance().currentLocale, str, array);
            }
            return String.format(str, array);
        }
        catch (Exception ex) {
            FileLog.e(ex);
            final StringBuilder sb = new StringBuilder();
            sb.append("LOC_ERR: ");
            sb.append(str);
            return sb.toString();
        }
    }
    
    public static String formatTTLString(final int n) {
        if (n < 60) {
            return formatPluralString("Seconds", n);
        }
        if (n < 3600) {
            return formatPluralString("Minutes", n / 60);
        }
        if (n < 86400) {
            return formatPluralString("Hours", n / 60 / 60);
        }
        if (n < 604800) {
            return formatPluralString("Days", n / 60 / 60 / 24);
        }
        final int n2 = n / 60 / 60 / 24;
        if (n % 7 == 0) {
            return formatPluralString("Weeks", n2 / 7);
        }
        return String.format("%s %s", formatPluralString("Weeks", n2 / 7), formatPluralString("Days", n2 % 7));
    }
    
    public static String formatUserStatus(final int n, final TLRPC.User user) {
        return formatUserStatus(n, user, null);
    }
    
    public static String formatUserStatus(int expires, final TLRPC.User user, final boolean[] array) {
        if (user != null) {
            final TLRPC.UserStatus status = user.status;
            if (status != null && status.expires == 0) {
                if (status instanceof TLRPC.TL_userStatusRecently) {
                    status.expires = -100;
                }
                else if (status instanceof TLRPC.TL_userStatusLastWeek) {
                    status.expires = -101;
                }
                else if (status instanceof TLRPC.TL_userStatusLastMonth) {
                    status.expires = -102;
                }
            }
        }
        if (user != null) {
            final TLRPC.UserStatus status2 = user.status;
            if (status2 != null && status2.expires <= 0 && MessagesController.getInstance(expires).onlinePrivacy.containsKey(user.id)) {
                if (array != null) {
                    array[0] = true;
                }
                return getString("Online", 2131560100);
            }
        }
        if (user != null) {
            final TLRPC.UserStatus status3 = user.status;
            if (status3 != null && status3.expires != 0 && !UserObject.isDeleted(user)) {
                if (!(user instanceof TLRPC.TL_userEmpty)) {
                    final int currentTime = ConnectionsManager.getInstance(expires).getCurrentTime();
                    expires = user.status.expires;
                    if (expires > currentTime) {
                        if (array != null) {
                            array[0] = true;
                        }
                        return getString("Online", 2131560100);
                    }
                    if (expires == -1) {
                        return getString("Invisible", 2131559675);
                    }
                    if (expires == -100) {
                        return getString("Lately", 2131559742);
                    }
                    if (expires == -101) {
                        return getString("WithinAWeek", 2131561124);
                    }
                    if (expires == -102) {
                        return getString("WithinAMonth", 2131561123);
                    }
                    return formatDateOnline(expires);
                }
            }
        }
        return getString("ALongTimeAgo", 2131558400);
    }
    
    public static String getCurrentLanguageName() {
        final LocaleInfo currentLocaleInfo = getInstance().currentLocaleInfo;
        String s;
        if (currentLocaleInfo != null && !TextUtils.isEmpty((CharSequence)currentLocaleInfo.name)) {
            s = currentLocaleInfo.name;
        }
        else {
            s = getString("LanguageName", 2131559720);
        }
        return s;
    }
    
    public static LocaleController getInstance() {
        final LocaleController instance;
        if ((instance = LocaleController.Instance) == null) {
            synchronized (LocaleController.class) {
                if (LocaleController.Instance == null) {
                    LocaleController.Instance = new LocaleController();
                }
            }
        }
        return instance;
    }
    
    public static String getLocaleAlias(final String s) {
        if (s == null) {
            return null;
        }
        final int hashCode = s.hashCode();
        int n = 0;
        Label_0282: {
            if (hashCode != 3325) {
                if (hashCode != 3355) {
                    if (hashCode != 3365) {
                        if (hashCode != 3374) {
                            if (hashCode != 3391) {
                                if (hashCode != 3508) {
                                    if (hashCode != 3521) {
                                        if (hashCode != 3704) {
                                            if (hashCode != 3856) {
                                                if (hashCode != 101385) {
                                                    if (hashCode != 3404) {
                                                        if (hashCode == 3405) {
                                                            if (s.equals("jw")) {
                                                                n = 2;
                                                                break Label_0282;
                                                            }
                                                        }
                                                    }
                                                    else if (s.equals("jv")) {
                                                        n = 8;
                                                        break Label_0282;
                                                    }
                                                }
                                                else if (s.equals("fil")) {
                                                    n = 10;
                                                    break Label_0282;
                                                }
                                            }
                                            else if (s.equals("yi")) {
                                                n = 11;
                                                break Label_0282;
                                            }
                                        }
                                        else if (s.equals("tl")) {
                                            n = 4;
                                            break Label_0282;
                                        }
                                    }
                                    else if (s.equals("no")) {
                                        n = 3;
                                        break Label_0282;
                                    }
                                }
                                else if (s.equals("nb")) {
                                    n = 9;
                                    break Label_0282;
                                }
                            }
                            else if (s.equals("ji")) {
                                n = 5;
                                break Label_0282;
                            }
                        }
                        else if (s.equals("iw")) {
                            n = 1;
                            break Label_0282;
                        }
                    }
                    else if (s.equals("in")) {
                        n = 0;
                        break Label_0282;
                    }
                }
                else if (s.equals("id")) {
                    n = 6;
                    break Label_0282;
                }
            }
            else if (s.equals("he")) {
                n = 7;
                break Label_0282;
            }
            n = -1;
        }
        switch (n) {
            default: {
                return null;
            }
            case 11: {
                return "ji";
            }
            case 10: {
                return "tl";
            }
            case 9: {
                return "no";
            }
            case 8: {
                return "jw";
            }
            case 7: {
                return "iw";
            }
            case 6: {
                return "in";
            }
            case 5: {
                return "yi";
            }
            case 4: {
                return "fil";
            }
            case 3: {
                return "nb";
            }
            case 2: {
                return "jv";
            }
            case 1: {
                return "he";
            }
            case 0: {
                return "id";
            }
        }
    }
    
    private HashMap<String, String> getLocaleFileStrings(final File file) {
        return this.getLocaleFileStrings(file, false);
    }
    
    private HashMap<String, String> getLocaleFileStrings(final File file, final boolean b) {
        this.reloadLastFile = false;
        Object name = null;
        Object attributeValue;
        Object text = attributeValue = null;
        String s;
        try {
            try {
                if (!file.exists()) {
                    attributeValue = text;
                    return new HashMap<String, String>();
                }
                attributeValue = text;
                attributeValue = text;
                final HashMap<String, String> hashMap = new HashMap<String, String>();
                attributeValue = text;
                final XmlPullParser pullParser = Xml.newPullParser();
                attributeValue = text;
                final FileInputStream fileInputStream = new FileInputStream(file);
                try {
                    pullParser.setInput((InputStream)fileInputStream, "UTF-8");
                    int i = pullParser.getEventType();
                    attributeValue = null;
                    Object replace;
                    Object o = replace = attributeValue;
                    while (i != 1) {
                        Label_0358: {
                            if (i == 2) {
                                name = pullParser.getName();
                                if (pullParser.getAttributeCount() > 0) {
                                    attributeValue = pullParser.getAttributeValue(0);
                                }
                                text = attributeValue;
                            }
                            else if (i == 4) {
                                text = attributeValue;
                                name = o;
                                if (attributeValue != null) {
                                    text = pullParser.getText();
                                    if ((replace = text) != null) {
                                        final String trim = ((String)text).trim();
                                        if (b) {
                                            replace = trim.replace("<", "&lt;").replace(">", "&gt;").replace("'", "\\'").replace("& ", "&amp; ");
                                        }
                                        else {
                                            final String replace2 = trim.replace("\\n", "\n").replace("\\", "");
                                            final String replace3 = replace2.replace("&lt;", "<");
                                            text = attributeValue;
                                            name = o;
                                            replace = replace3;
                                            if (this.reloadLastFile) {
                                                break Label_0358;
                                            }
                                            text = attributeValue;
                                            name = o;
                                            replace = replace3;
                                            if (!replace3.equals(replace2)) {
                                                this.reloadLastFile = true;
                                                text = attributeValue;
                                                name = o;
                                                replace = replace3;
                                            }
                                            break Label_0358;
                                        }
                                    }
                                    text = attributeValue;
                                    name = o;
                                }
                            }
                            else {
                                text = attributeValue;
                                name = o;
                                if (i == 3) {
                                    text = null;
                                    name = (replace = text);
                                }
                            }
                        }
                        attributeValue = text;
                        o = name;
                        Object o2 = replace;
                        if (name != null) {
                            attributeValue = text;
                            o = name;
                            o2 = replace;
                            if (((String)name).equals("string")) {
                                attributeValue = text;
                                o = name;
                                if ((o2 = replace) != null) {
                                    attributeValue = text;
                                    o = name;
                                    o2 = replace;
                                    if (text != null) {
                                        attributeValue = text;
                                        o = name;
                                        o2 = replace;
                                        if (((String)replace).length() != 0) {
                                            attributeValue = text;
                                            o = name;
                                            o2 = replace;
                                            if (((String)text).length() != 0) {
                                                hashMap.put((String)text, (String)replace);
                                                attributeValue = null;
                                                o = (o2 = attributeValue);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        i = pullParser.next();
                        replace = o2;
                    }
                    try {
                        fileInputStream.close();
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                    }
                    return hashMap;
                }
                catch (Exception text) {}
            }
            finally {
                final Object o3 = attributeValue;
            }
        }
        catch (Exception text) {
            s = (String)name;
        }
        FileLog.e((Throwable)text);
        this.reloadLastFile = true;
        if (s != null) {
            try {
                ((FileInputStream)s).close();
            }
            catch (Exception ex2) {
                FileLog.e(ex2);
            }
        }
        return new HashMap<String, String>();
        Object o3 = null;
        if (o3 != null) {
            try {
                ((FileInputStream)o3).close();
            }
            catch (Exception ex3) {
                FileLog.e(ex3);
            }
        }
    }
    
    private String getLocaleString(final Locale locale) {
        if (locale == null) {
            return "en";
        }
        final String language = locale.getLanguage();
        final String country = locale.getCountry();
        final String variant = locale.getVariant();
        if (language.length() == 0 && country.length() == 0) {
            return "en";
        }
        final StringBuilder sb = new StringBuilder(11);
        sb.append(language);
        if (country.length() > 0 || variant.length() > 0) {
            sb.append('_');
        }
        sb.append(country);
        if (variant.length() > 0) {
            sb.append('_');
        }
        sb.append(variant);
        return sb.toString();
    }
    
    public static String getLocaleStringIso639() {
        final Locale currentLocale = getInstance().currentLocale;
        if (currentLocale == null) {
            return "en";
        }
        final String language = currentLocale.getLanguage();
        final String country = currentLocale.getCountry();
        final String variant = currentLocale.getVariant();
        if (language.length() == 0 && country.length() == 0) {
            return "en";
        }
        final StringBuilder sb = new StringBuilder(11);
        sb.append(language);
        if (country.length() > 0 || variant.length() > 0) {
            sb.append('-');
        }
        sb.append(country);
        if (variant.length() > 0) {
            sb.append('_');
        }
        sb.append(variant);
        return sb.toString();
    }
    
    public static String getPluralString(String string, final int n) {
        if (string != null && string.length() != 0 && getInstance().currentPluralRules != null) {
            final String stringForQuantity = getInstance().stringForQuantity(getInstance().currentPluralRules.quantityForNumber(n));
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append("_");
            sb.append(stringForQuantity);
            string = sb.toString();
            return getString(string, ApplicationLoader.applicationContext.getResources().getIdentifier(string, "string", ApplicationLoader.applicationContext.getPackageName()));
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("LOC_ERR:");
        sb2.append(string);
        return sb2.toString();
    }
    
    public static String getServerString(final String key) {
        String string;
        final String s = string = getInstance().localeValues.get(key);
        if (s == null) {
            final int identifier = ApplicationLoader.applicationContext.getResources().getIdentifier(key, "string", ApplicationLoader.applicationContext.getPackageName());
            string = s;
            if (identifier != 0) {
                string = ApplicationLoader.applicationContext.getString(identifier);
            }
        }
        return string;
    }
    
    public static String getString(final String s, final int n) {
        return getInstance().getStringInternal(s, n);
    }
    
    private String getStringInternal(final String s, final int n) {
        String s2;
        if (BuildVars.USE_CLOUD_STRINGS) {
            s2 = this.localeValues.get(s);
        }
        else {
            s2 = null;
        }
        String string = s2;
        if (s2 == null) {
            try {
                string = ApplicationLoader.applicationContext.getString(n);
            }
            catch (Exception ex) {
                FileLog.e(ex);
                string = s2;
            }
        }
        String string2;
        if ((string2 = string) == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("LOC_ERR:");
            sb.append(s);
            string2 = sb.toString();
        }
        return string2;
    }
    
    public static String getSystemLocaleStringIso639() {
        final Locale systemDefaultLocale = getInstance().getSystemDefaultLocale();
        if (systemDefaultLocale == null) {
            return "en";
        }
        final String language = systemDefaultLocale.getLanguage();
        final String country = systemDefaultLocale.getCountry();
        final String variant = systemDefaultLocale.getVariant();
        if (language.length() == 0 && country.length() == 0) {
            return "en";
        }
        final StringBuilder sb = new StringBuilder(11);
        sb.append(language);
        if (country.length() > 0 || variant.length() > 0) {
            sb.append('-');
        }
        sb.append(country);
        if (variant.length() > 0) {
            sb.append('_');
        }
        sb.append(variant);
        return sb.toString();
    }
    
    public static boolean isRTLCharacter(final char c) {
        final byte directionality = Character.getDirectionality(c);
        boolean b2;
        final boolean b = b2 = true;
        if (directionality != 1) {
            b2 = b;
            if (Character.getDirectionality(c) != 2) {
                b2 = b;
                if (Character.getDirectionality(c) != 16) {
                    b2 = (Character.getDirectionality(c) == 17 && b);
                }
            }
        }
        return b2;
    }
    
    private void loadOtherLanguages() {
        final Context applicationContext = ApplicationLoader.applicationContext;
        final int n = 0;
        final SharedPreferences sharedPreferences = applicationContext.getSharedPreferences("langconfig", 0);
        final String string = sharedPreferences.getString("locales", (String)null);
        if (!TextUtils.isEmpty((CharSequence)string)) {
            final String[] split = string.split("&");
            for (int length = split.length, i = 0; i < length; ++i) {
                final LocaleInfo withString = LocaleInfo.createWithString(split[i]);
                if (withString != null) {
                    this.otherLanguages.add(withString);
                }
            }
        }
        final String string2 = sharedPreferences.getString("remote", (String)null);
        if (!TextUtils.isEmpty((CharSequence)string2)) {
            final String[] split2 = string2.split("&");
            for (int length2 = split2.length, j = 0; j < length2; ++j) {
                final LocaleInfo withString2 = LocaleInfo.createWithString(split2[j]);
                withString2.shortName = withString2.shortName.replace("-", "_");
                if (!this.remoteLanguagesDict.containsKey(withString2.getKey())) {
                    if (withString2 != null) {
                        this.remoteLanguages.add(withString2);
                        this.remoteLanguagesDict.put(withString2.getKey(), withString2);
                    }
                }
            }
        }
        final String string3 = sharedPreferences.getString("unofficial", (String)null);
        if (!TextUtils.isEmpty((CharSequence)string3)) {
            final String[] split3 = string3.split("&");
            for (int length3 = split3.length, k = n; k < length3; ++k) {
                final LocaleInfo withString3 = LocaleInfo.createWithString(split3[k]);
                withString3.shortName = withString3.shortName.replace("-", "_");
                if (withString3 != null) {
                    this.unofficialLanguages.add(withString3);
                }
            }
        }
    }
    
    private void saveOtherLanguages() {
        final Context applicationContext = ApplicationLoader.applicationContext;
        final int n = 0;
        final SharedPreferences$Editor edit = applicationContext.getSharedPreferences("langconfig", 0).edit();
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.otherLanguages.size(); ++i) {
            final String saveString = this.otherLanguages.get(i).getSaveString();
            if (saveString != null) {
                if (sb.length() != 0) {
                    sb.append("&");
                }
                sb.append(saveString);
            }
        }
        edit.putString("locales", sb.toString());
        sb.setLength(0);
        for (int j = 0; j < this.remoteLanguages.size(); ++j) {
            final String saveString2 = this.remoteLanguages.get(j).getSaveString();
            if (saveString2 != null) {
                if (sb.length() != 0) {
                    sb.append("&");
                }
                sb.append(saveString2);
            }
        }
        edit.putString("remote", sb.toString());
        sb.setLength(0);
        for (int k = n; k < this.unofficialLanguages.size(); ++k) {
            final String saveString3 = this.unofficialLanguages.get(k).getSaveString();
            if (saveString3 != null) {
                if (sb.length() != 0) {
                    sb.append("&");
                }
                sb.append(saveString3);
            }
        }
        edit.putString("unofficial", sb.toString());
        edit.commit();
    }
    
    public static String stringForMessageListDate(long date) {
        date *= 1000L;
        try {
            final Calendar instance = Calendar.getInstance();
            final int value = instance.get(6);
            instance.setTimeInMillis(date);
            final int value2 = instance.get(6);
            if (Math.abs(System.currentTimeMillis() - date) >= 31536000000L) {
                return getInstance().formatterYear.format(new Date(date));
            }
            final int n = value2 - value;
            if (n == 0 || (n == -1 && System.currentTimeMillis() - date < 28800000L)) {
                return getInstance().formatterDay.format(new Date(date));
            }
            if (n > -7 && n <= -1) {
                return getInstance().formatterWeek.format(new Date(date));
            }
            return getInstance().formatterDayMonth.format(new Date(date));
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return "LOC_ERR";
        }
    }
    
    private String stringForQuantity(final int n) {
        if (n == 1) {
            return "zero";
        }
        if (n == 2) {
            return "one";
        }
        if (n == 4) {
            return "two";
        }
        if (n == 8) {
            return "few";
        }
        if (n != 16) {
            return "other";
        }
        return "many";
    }
    
    public void applyLanguage(final LocaleInfo localeInfo, final boolean b, final boolean b2, final int n) {
        this.applyLanguage(localeInfo, b, b2, false, false, n);
    }
    
    public void applyLanguage(final LocaleInfo currentLocaleInfo, final boolean b, final boolean b2, final boolean b3, final boolean b4, final int n) {
        if (currentLocaleInfo == null) {
            return;
        }
        final boolean hasBaseLang = currentLocaleInfo.hasBaseLang();
        final File pathToFile = currentLocaleInfo.getPathToFile();
        final File pathToBaseFile = currentLocaleInfo.getPathToBaseFile();
        final String shortName = currentLocaleInfo.shortName;
        if (!b2) {
            ConnectionsManager.setLangCode(currentLocaleInfo.getLangCode());
        }
        if (this.getLanguageFromDict(currentLocaleInfo.getKey()) == null) {
            if (currentLocaleInfo.isRemote()) {
                this.remoteLanguages.add(currentLocaleInfo);
                this.remoteLanguagesDict.put(currentLocaleInfo.getKey(), currentLocaleInfo);
                this.languages.add(currentLocaleInfo);
                this.languagesDict.put(currentLocaleInfo.getKey(), currentLocaleInfo);
                this.saveOtherLanguages();
            }
            else if (currentLocaleInfo.isUnofficial()) {
                this.unofficialLanguages.add(currentLocaleInfo);
                this.languagesDict.put(currentLocaleInfo.getKey(), currentLocaleInfo);
                this.saveOtherLanguages();
            }
        }
        if ((currentLocaleInfo.isRemote() || currentLocaleInfo.isUnofficial()) && (b4 || !pathToFile.exists() || (hasBaseLang && !pathToBaseFile.exists()))) {
            if (BuildVars.LOGS_ENABLED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("reload locale because one of file doesn't exist");
                sb.append(pathToFile);
                sb.append(" ");
                sb.append(pathToBaseFile);
                FileLog.d(sb.toString());
            }
            if (b2) {
                AndroidUtilities.runOnUIThread(new _$$Lambda$LocaleController$rPqRyQsgkE1_kSvpx5ngOlMyIY4(this, currentLocaleInfo, n));
            }
            else {
                this.applyRemoteLanguage(currentLocaleInfo, null, true, n);
            }
        }
        try {
            String[] array;
            if (!TextUtils.isEmpty((CharSequence)currentLocaleInfo.pluralLangCode)) {
                array = currentLocaleInfo.pluralLangCode.split("_");
            }
            else if (!TextUtils.isEmpty((CharSequence)currentLocaleInfo.baseLangCode)) {
                array = currentLocaleInfo.baseLangCode.split("_");
            }
            else {
                array = currentLocaleInfo.shortName.split("_");
            }
            Locale currentLocale;
            if (array.length == 1) {
                currentLocale = new Locale(array[0]);
            }
            else {
                currentLocale = new Locale(array[0], array[1]);
            }
            if (b) {
                this.languageOverride = currentLocaleInfo.shortName;
                final SharedPreferences$Editor edit = MessagesController.getGlobalMainSettings().edit();
                edit.putString("language", currentLocaleInfo.getKey());
                edit.commit();
            }
            if (pathToFile == null) {
                this.localeValues.clear();
            }
            else if (!b3) {
                File file;
                if (hasBaseLang) {
                    file = currentLocaleInfo.getPathToBaseFile();
                }
                else {
                    file = currentLocaleInfo.getPathToFile();
                }
                this.localeValues = this.getLocaleFileStrings(file);
                if (hasBaseLang) {
                    this.localeValues.putAll(this.getLocaleFileStrings(currentLocaleInfo.getPathToFile()));
                }
            }
            this.currentLocale = currentLocale;
            this.currentLocaleInfo = currentLocaleInfo;
            if (this.currentLocaleInfo != null && !TextUtils.isEmpty((CharSequence)this.currentLocaleInfo.pluralLangCode)) {
                this.currentPluralRules = this.allRules.get(this.currentLocaleInfo.pluralLangCode);
            }
            if (this.currentPluralRules == null) {
                this.currentPluralRules = this.allRules.get(array[0]);
                if (this.currentPluralRules == null) {
                    this.currentPluralRules = this.allRules.get(this.currentLocale.getLanguage());
                    if (this.currentPluralRules == null) {
                        this.currentPluralRules = (PluralRules)new PluralRules_None();
                    }
                }
            }
            this.changingConfiguration = true;
            Locale.setDefault(this.currentLocale);
            final Configuration configuration = new Configuration();
            configuration.locale = this.currentLocale;
            ApplicationLoader.applicationContext.getResources().updateConfiguration(configuration, ApplicationLoader.applicationContext.getResources().getDisplayMetrics());
            this.changingConfiguration = false;
            if (this.reloadLastFile) {
                if (b2) {
                    AndroidUtilities.runOnUIThread(new _$$Lambda$LocaleController$TG7Fd9ju6kk9LWo1RB9e1CZ_6mk(this, n));
                }
                else {
                    this.reloadCurrentRemoteLocale(n, null);
                }
                this.reloadLastFile = false;
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
            this.changingConfiguration = false;
        }
        this.recreateFormatters();
    }
    
    public boolean applyLanguageFile(final File file, final int n) {
        try {
            final HashMap<String, String> localeFileStrings = this.getLocaleFileStrings(file);
            final String name = localeFileStrings.get("LanguageName");
            final String nameEnglish = localeFileStrings.get("LanguageNameInEnglish");
            final String str = localeFileStrings.get("LanguageCode");
            if (name != null && name.length() > 0 && nameEnglish != null && nameEnglish.length() > 0 && str != null && str.length() > 0) {
                if (!name.contains("&")) {
                    if (!name.contains("|")) {
                        if (!nameEnglish.contains("&")) {
                            if (!nameEnglish.contains("|")) {
                                if (!str.contains("&") && !str.contains("|") && !str.contains("/")) {
                                    if (!str.contains("\\")) {
                                        final File filesDirFixed = ApplicationLoader.getFilesDirFixed();
                                        final StringBuilder sb = new StringBuilder();
                                        sb.append(str);
                                        sb.append(".xml");
                                        final File file2 = new File(filesDirFixed, sb.toString());
                                        if (!AndroidUtilities.copyFile(file, file2)) {
                                            return false;
                                        }
                                        final StringBuilder sb2 = new StringBuilder();
                                        sb2.append("local_");
                                        sb2.append(str.toLowerCase());
                                        LocaleInfo languageFromDict;
                                        if ((languageFromDict = this.getLanguageFromDict(sb2.toString())) == null) {
                                            languageFromDict = new LocaleInfo();
                                            languageFromDict.name = name;
                                            languageFromDict.nameEnglish = nameEnglish;
                                            languageFromDict.shortName = str.toLowerCase();
                                            languageFromDict.pluralLangCode = languageFromDict.shortName;
                                            languageFromDict.pathToFile = file2.getAbsolutePath();
                                            this.languages.add(languageFromDict);
                                            this.languagesDict.put(languageFromDict.getKey(), languageFromDict);
                                            this.otherLanguages.add(languageFromDict);
                                            this.saveOtherLanguages();
                                        }
                                        this.localeValues = localeFileStrings;
                                        this.applyLanguage(languageFromDict, true, false, true, false, n);
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
                return false;
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        return false;
    }
    
    public void checkUpdateForCurrentRemoteLocale(final int n, final int n2, final int n3) {
        final LocaleInfo currentLocaleInfo = this.currentLocaleInfo;
        if (currentLocaleInfo != null) {
            if (currentLocaleInfo == null || currentLocaleInfo.isRemote() || this.currentLocaleInfo.isUnofficial()) {
                if (this.currentLocaleInfo.hasBaseLang()) {
                    final LocaleInfo currentLocaleInfo2 = this.currentLocaleInfo;
                    if (currentLocaleInfo2.baseVersion < n3) {
                        this.applyRemoteLanguage(currentLocaleInfo2, currentLocaleInfo2.baseLangCode, false, n);
                    }
                }
                final LocaleInfo currentLocaleInfo3 = this.currentLocaleInfo;
                if (currentLocaleInfo3.version < n2) {
                    this.applyRemoteLanguage(currentLocaleInfo3, currentLocaleInfo3.shortName, false, n);
                }
            }
        }
    }
    
    public boolean deleteLanguage(final LocaleInfo localeInfo, final int n) {
        if (localeInfo.pathToFile != null && (!localeInfo.isRemote() || localeInfo.serverIndex == Integer.MAX_VALUE)) {
            if (this.currentLocaleInfo == localeInfo) {
                LocaleInfo languageFromDict = null;
                if (this.systemDefaultLocale.getLanguage() != null) {
                    languageFromDict = this.getLanguageFromDict(this.systemDefaultLocale.getLanguage());
                }
                LocaleInfo languageFromDict2;
                if ((languageFromDict2 = languageFromDict) == null) {
                    languageFromDict2 = this.getLanguageFromDict(this.getLocaleString(this.systemDefaultLocale));
                }
                LocaleInfo languageFromDict3;
                if ((languageFromDict3 = languageFromDict2) == null) {
                    languageFromDict3 = this.getLanguageFromDict("en");
                }
                this.applyLanguage(languageFromDict3, true, false, n);
            }
            this.unofficialLanguages.remove(localeInfo);
            this.remoteLanguages.remove(localeInfo);
            this.remoteLanguagesDict.remove(localeInfo.getKey());
            this.otherLanguages.remove(localeInfo);
            this.languages.remove(localeInfo);
            this.languagesDict.remove(localeInfo.getKey());
            new File(localeInfo.pathToFile).delete();
            this.saveOtherLanguages();
            return true;
        }
        return false;
    }
    
    public String formatCurrencyDecimalString(long abs, String string, final boolean b) {
        final String upperCase = string.toUpperCase();
        abs = Math.abs(abs);
        int n = 0;
        Label_0808: {
            switch (upperCase.hashCode()) {
                case 87118: {
                    if (upperCase.equals("XPF")) {
                        n = 28;
                        break Label_0808;
                    }
                    break;
                }
                case 87087: {
                    if (upperCase.equals("XOF")) {
                        n = 27;
                        break Label_0808;
                    }
                    break;
                }
                case 86653: {
                    if (upperCase.equals("XAF")) {
                        n = 26;
                        break Label_0808;
                    }
                    break;
                }
                case 85367: {
                    if (upperCase.equals("VUV")) {
                        n = 25;
                        break Label_0808;
                    }
                    break;
                }
                case 85132: {
                    if (upperCase.equals("VND")) {
                        n = 24;
                        break Label_0808;
                    }
                    break;
                }
                case 84517: {
                    if (upperCase.equals("UYI")) {
                        n = 23;
                        break Label_0808;
                    }
                    break;
                }
                case 83974: {
                    if (upperCase.equals("UGX")) {
                        n = 22;
                        break Label_0808;
                    }
                    break;
                }
                case 83210: {
                    if (upperCase.equals("TND")) {
                        n = 8;
                        break Label_0808;
                    }
                    break;
                }
                case 81569: {
                    if (upperCase.equals("RWF")) {
                        n = 21;
                        break Label_0808;
                    }
                    break;
                }
                case 79710: {
                    if (upperCase.equals("PYG")) {
                        n = 20;
                        break Label_0808;
                    }
                    break;
                }
                case 78388: {
                    if (upperCase.equals("OMR")) {
                        n = 7;
                        break Label_0808;
                    }
                    break;
                }
                case 76618: {
                    if (upperCase.equals("MRO")) {
                        n = 29;
                        break Label_0808;
                    }
                    break;
                }
                case 76263: {
                    if (upperCase.equals("MGA")) {
                        n = 19;
                        break Label_0808;
                    }
                    break;
                }
                case 75863: {
                    if (upperCase.equals("LYD")) {
                        n = 6;
                        break Label_0808;
                    }
                    break;
                }
                case 74840: {
                    if (upperCase.equals("KWD")) {
                        n = 5;
                        break Label_0808;
                    }
                    break;
                }
                case 74704: {
                    if (upperCase.equals("KRW")) {
                        n = 18;
                        break Label_0808;
                    }
                    break;
                }
                case 74532: {
                    if (upperCase.equals("KMF")) {
                        n = 17;
                        break Label_0808;
                    }
                    break;
                }
                case 73683: {
                    if (upperCase.equals("JPY")) {
                        n = 16;
                        break Label_0808;
                    }
                    break;
                }
                case 73631: {
                    if (upperCase.equals("JOD")) {
                        n = 4;
                        break Label_0808;
                    }
                    break;
                }
                case 72801: {
                    if (upperCase.equals("ISK")) {
                        n = 15;
                        break Label_0808;
                    }
                    break;
                }
                case 72777: {
                    if (upperCase.equals("IRR")) {
                        n = 1;
                        break Label_0808;
                    }
                    break;
                }
                case 72732: {
                    if (upperCase.equals("IQD")) {
                        n = 3;
                        break Label_0808;
                    }
                    break;
                }
                case 70719: {
                    if (upperCase.equals("GNF")) {
                        n = 14;
                        break Label_0808;
                    }
                    break;
                }
                case 67712: {
                    if (upperCase.equals("DJF")) {
                        n = 13;
                        break Label_0808;
                    }
                    break;
                }
                case 67122: {
                    if (upperCase.equals("CVE")) {
                        n = 12;
                        break Label_0808;
                    }
                    break;
                }
                case 66823: {
                    if (upperCase.equals("CLP")) {
                        n = 11;
                        break Label_0808;
                    }
                    break;
                }
                case 66813: {
                    if (upperCase.equals("CLF")) {
                        n = 0;
                        break Label_0808;
                    }
                    break;
                }
                case 66267: {
                    if (upperCase.equals("BYR")) {
                        n = 10;
                        break Label_0808;
                    }
                    break;
                }
                case 65759: {
                    if (upperCase.equals("BIF")) {
                        n = 9;
                        break Label_0808;
                    }
                    break;
                }
                case 65726: {
                    if (upperCase.equals("BHD")) {
                        n = 2;
                        break Label_0808;
                    }
                    break;
                }
            }
            n = -1;
        }
        string = " %.0f";
        double d = 0.0;
        switch (n) {
            default: {
                final double v = (double)abs;
                Double.isNaN(v);
                d = v / 100.0;
                string = " %.2f";
                break;
            }
            case 29: {
                final double v2 = (double)abs;
                Double.isNaN(v2);
                d = v2 / 10.0;
                string = " %.1f";
                break;
            }
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28: {
                d = (double)abs;
                break;
            }
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8: {
                final double v3 = (double)abs;
                Double.isNaN(v3);
                d = v3 / 1000.0;
                string = " %.3f";
                break;
            }
            case 1: {
                d = abs / 100.0f;
                if (abs % 100L != 0L) {
                    string = " %.2f";
                }
                break;
            }
            case 0: {
                final double v4 = (double)abs;
                Double.isNaN(v4);
                d = v4 / 10000.0;
                string = " %.4f";
                break;
            }
        }
        final Locale us = Locale.US;
        if (b) {
            string = upperCase;
        }
        else {
            final StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(string);
            string = sb.toString();
        }
        return String.format(us, string, d).trim();
    }
    
    public String formatCurrencyString(long abs, String str) {
        final String upperCase = str.toUpperCase();
        final boolean b = abs < 0L;
        abs = Math.abs(abs);
        final Currency instance = Currency.getInstance(upperCase);
        int n = -1;
        switch (upperCase.hashCode()) {
            case 87118: {
                if (upperCase.equals("XPF")) {
                    n = 28;
                    break;
                }
                break;
            }
            case 87087: {
                if (upperCase.equals("XOF")) {
                    n = 27;
                    break;
                }
                break;
            }
            case 86653: {
                if (upperCase.equals("XAF")) {
                    n = 26;
                    break;
                }
                break;
            }
            case 85367: {
                if (upperCase.equals("VUV")) {
                    n = 25;
                    break;
                }
                break;
            }
            case 85132: {
                if (upperCase.equals("VND")) {
                    n = 24;
                    break;
                }
                break;
            }
            case 84517: {
                if (upperCase.equals("UYI")) {
                    n = 23;
                    break;
                }
                break;
            }
            case 83974: {
                if (upperCase.equals("UGX")) {
                    n = 22;
                    break;
                }
                break;
            }
            case 83210: {
                if (upperCase.equals("TND")) {
                    n = 8;
                    break;
                }
                break;
            }
            case 81569: {
                if (upperCase.equals("RWF")) {
                    n = 21;
                    break;
                }
                break;
            }
            case 79710: {
                if (upperCase.equals("PYG")) {
                    n = 20;
                    break;
                }
                break;
            }
            case 78388: {
                if (upperCase.equals("OMR")) {
                    n = 7;
                    break;
                }
                break;
            }
            case 76618: {
                if (upperCase.equals("MRO")) {
                    n = 29;
                    break;
                }
                break;
            }
            case 76263: {
                if (upperCase.equals("MGA")) {
                    n = 19;
                    break;
                }
                break;
            }
            case 75863: {
                if (upperCase.equals("LYD")) {
                    n = 6;
                    break;
                }
                break;
            }
            case 74840: {
                if (upperCase.equals("KWD")) {
                    n = 5;
                    break;
                }
                break;
            }
            case 74704: {
                if (upperCase.equals("KRW")) {
                    n = 18;
                    break;
                }
                break;
            }
            case 74532: {
                if (upperCase.equals("KMF")) {
                    n = 17;
                    break;
                }
                break;
            }
            case 73683: {
                if (upperCase.equals("JPY")) {
                    n = 16;
                    break;
                }
                break;
            }
            case 73631: {
                if (upperCase.equals("JOD")) {
                    n = 4;
                    break;
                }
                break;
            }
            case 72801: {
                if (upperCase.equals("ISK")) {
                    n = 15;
                    break;
                }
                break;
            }
            case 72777: {
                if (upperCase.equals("IRR")) {
                    n = 1;
                    break;
                }
                break;
            }
            case 72732: {
                if (upperCase.equals("IQD")) {
                    n = 3;
                    break;
                }
                break;
            }
            case 70719: {
                if (upperCase.equals("GNF")) {
                    n = 14;
                    break;
                }
                break;
            }
            case 67712: {
                if (upperCase.equals("DJF")) {
                    n = 13;
                    break;
                }
                break;
            }
            case 67122: {
                if (upperCase.equals("CVE")) {
                    n = 12;
                    break;
                }
                break;
            }
            case 66823: {
                if (upperCase.equals("CLP")) {
                    n = 11;
                    break;
                }
                break;
            }
            case 66813: {
                if (upperCase.equals("CLF")) {
                    n = 0;
                    break;
                }
                break;
            }
            case 66267: {
                if (upperCase.equals("BYR")) {
                    n = 10;
                    break;
                }
                break;
            }
            case 65759: {
                if (upperCase.equals("BIF")) {
                    n = 9;
                    break;
                }
                break;
            }
            case 65726: {
                if (upperCase.equals("BHD")) {
                    n = 2;
                    break;
                }
                break;
            }
        }
        str = " %.0f";
        double n2 = 0.0;
        switch (n) {
            default: {
                final double v = (double)abs;
                Double.isNaN(v);
                n2 = v / 100.0;
                str = " %.2f";
                break;
            }
            case 29: {
                final double v2 = (double)abs;
                Double.isNaN(v2);
                n2 = v2 / 10.0;
                str = " %.1f";
                break;
            }
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28: {
                n2 = (double)abs;
                break;
            }
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8: {
                final double v3 = (double)abs;
                Double.isNaN(v3);
                n2 = v3 / 1000.0;
                str = " %.3f";
                break;
            }
            case 1: {
                n2 = abs / 100.0f;
                if (abs % 100L != 0L) {
                    str = " %.2f";
                }
                break;
            }
            case 0: {
                final double v4 = (double)abs;
                Double.isNaN(v4);
                n2 = v4 / 10000.0;
                str = " %.4f";
                break;
            }
        }
        String s = "-";
        if (instance != null) {
            Locale inLocale = this.currentLocale;
            if (inLocale == null) {
                inLocale = this.systemDefaultLocale;
            }
            final NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(inLocale);
            currencyInstance.setCurrency(instance);
            if (upperCase.equals("IRR")) {
                currencyInstance.setMaximumFractionDigits(0);
            }
            final StringBuilder sb = new StringBuilder();
            if (!b) {
                s = "";
            }
            sb.append(s);
            sb.append(currencyInstance.format(n2));
            return sb.toString();
        }
        final StringBuilder sb2 = new StringBuilder();
        if (!b) {
            s = "";
        }
        sb2.append(s);
        final Locale us = Locale.US;
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(upperCase);
        sb3.append(str);
        sb2.append(String.format(us, sb3.toString(), n2));
        return sb2.toString();
    }
    
    public LocaleInfo getCurrentLocaleInfo() {
        return this.currentLocaleInfo;
    }
    
    public LocaleInfo getLanguageFromDict(final String s) {
        if (s == null) {
            return null;
        }
        return this.languagesDict.get(s.toLowerCase().replace("-", "_"));
    }
    
    public Locale getSystemDefaultLocale() {
        return this.systemDefaultLocale;
    }
    
    public String getTranslitString(final String s) {
        return this.getTranslitString(s, true, false);
    }
    
    public String getTranslitString(final String s, final boolean b) {
        return this.getTranslitString(s, true, b);
    }
    
    public String getTranslitString(final String s, final boolean b, final boolean b2) {
        if (s == null) {
            return null;
        }
        if (this.ruTranslitChars == null) {
            (this.ruTranslitChars = new HashMap<String, String>(33)).put("\u0430", "a");
            this.ruTranslitChars.put("\u0431", "b");
            this.ruTranslitChars.put("\u0432", "v");
            this.ruTranslitChars.put("\u0433", "g");
            this.ruTranslitChars.put("\u0434", "d");
            this.ruTranslitChars.put("\u0435", "e");
            this.ruTranslitChars.put("\u0451", "yo");
            this.ruTranslitChars.put("\u0436", "zh");
            this.ruTranslitChars.put("\u0437", "z");
            this.ruTranslitChars.put("\u0438", "i");
            this.ruTranslitChars.put("\u0439", "i");
            this.ruTranslitChars.put("\u043a", "k");
            this.ruTranslitChars.put("\u043b", "l");
            this.ruTranslitChars.put("\u043c", "m");
            this.ruTranslitChars.put("\u043d", "n");
            this.ruTranslitChars.put("\u043e", "o");
            this.ruTranslitChars.put("\u043f", "p");
            this.ruTranslitChars.put("\u0440", "r");
            this.ruTranslitChars.put("\u0441", "s");
            this.ruTranslitChars.put("\u0442", "t");
            this.ruTranslitChars.put("\u0443", "u");
            this.ruTranslitChars.put("\u0444", "f");
            this.ruTranslitChars.put("\u0445", "h");
            this.ruTranslitChars.put("\u0446", "ts");
            this.ruTranslitChars.put("\u0447", "ch");
            this.ruTranslitChars.put("\u0448", "sh");
            this.ruTranslitChars.put("\u0449", "sch");
            this.ruTranslitChars.put("\u044b", "i");
            this.ruTranslitChars.put("\u044c", "");
            this.ruTranslitChars.put("\u044a", "");
            this.ruTranslitChars.put("\u044d", "e");
            this.ruTranslitChars.put("\u044e", "yu");
            this.ruTranslitChars.put("\u044f", "ya");
        }
        if (this.translitChars == null) {
            (this.translitChars = new HashMap<String, String>(487)).put("\u023c", "c");
            this.translitChars.put("\u1d87", "n");
            this.translitChars.put("\u0256", "d");
            this.translitChars.put("\u1eff", "y");
            this.translitChars.put("\u1d13", "o");
            this.translitChars.put("\u00f8", "o");
            this.translitChars.put("\u1e01", "a");
            this.translitChars.put("\u02af", "h");
            this.translitChars.put("\u0177", "y");
            this.translitChars.put("\u029e", "k");
            this.translitChars.put("\u1eeb", "u");
            this.translitChars.put("\ua733", "aa");
            this.translitChars.put("\u0133", "ij");
            this.translitChars.put("\u1e3d", "l");
            this.translitChars.put("\u026a", "i");
            this.translitChars.put("\u1e07", "b");
            this.translitChars.put("\u0280", "r");
            this.translitChars.put("\u011b", "e");
            this.translitChars.put("\ufb03", "ffi");
            this.translitChars.put("\u01a1", "o");
            this.translitChars.put("\u2c79", "r");
            this.translitChars.put("\u1ed3", "o");
            this.translitChars.put("\u01d0", "i");
            this.translitChars.put("\ua755", "p");
            this.translitChars.put("\u00fd", "y");
            this.translitChars.put("\u1e1d", "e");
            this.translitChars.put("\u2092", "o");
            this.translitChars.put("\u2c65", "a");
            this.translitChars.put("\u0299", "b");
            this.translitChars.put("\u1e1b", "e");
            this.translitChars.put("\u0188", "c");
            this.translitChars.put("\u0266", "h");
            this.translitChars.put("\u1d6c", "b");
            this.translitChars.put("\u1e63", "s");
            this.translitChars.put("\u0111", "d");
            this.translitChars.put("\u1ed7", "o");
            this.translitChars.put("\u025f", "j");
            this.translitChars.put("\u1e9a", "a");
            this.translitChars.put("\u024f", "y");
            this.translitChars.put("\u028c", "v");
            this.translitChars.put("\ua753", "p");
            this.translitChars.put("\ufb01", "fi");
            this.translitChars.put("\u1d84", "k");
            this.translitChars.put("\u1e0f", "d");
            this.translitChars.put("\u1d0c", "l");
            this.translitChars.put("\u0117", "e");
            this.translitChars.put("\u1d0b", "k");
            this.translitChars.put("\u010b", "c");
            this.translitChars.put("\u0281", "r");
            this.translitChars.put("\u0195", "hv");
            this.translitChars.put("\u0180", "b");
            this.translitChars.put("\u1e4d", "o");
            this.translitChars.put("\u0223", "ou");
            this.translitChars.put("\u01f0", "j");
            this.translitChars.put("\u1d83", "g");
            this.translitChars.put("\u1e4b", "n");
            this.translitChars.put("\u0249", "j");
            this.translitChars.put("\u01e7", "g");
            this.translitChars.put("\u01f3", "dz");
            this.translitChars.put("\u017a", "z");
            this.translitChars.put("\ua737", "au");
            this.translitChars.put("\u01d6", "u");
            this.translitChars.put("\u1d79", "g");
            this.translitChars.put("\u022f", "o");
            this.translitChars.put("\u0250", "a");
            this.translitChars.put("\u0105", "a");
            this.translitChars.put("\u00f5", "o");
            this.translitChars.put("\u027b", "r");
            this.translitChars.put("\ua74d", "o");
            this.translitChars.put("\u01df", "a");
            this.translitChars.put("\u0234", "l");
            this.translitChars.put("\u0282", "s");
            this.translitChars.put("\ufb02", "fl");
            this.translitChars.put("\u0209", "i");
            this.translitChars.put("\u2c7b", "e");
            this.translitChars.put("\u1e49", "n");
            this.translitChars.put("\u00ef", "i");
            this.translitChars.put("\u00f1", "n");
            this.translitChars.put("\u1d09", "i");
            this.translitChars.put("\u0287", "t");
            this.translitChars.put("\u1e93", "z");
            this.translitChars.put("\u1ef7", "y");
            this.translitChars.put("\u0233", "y");
            this.translitChars.put("\u1e69", "s");
            this.translitChars.put("\u027d", "r");
            this.translitChars.put("\u011d", "g");
            this.translitChars.put("\u1d1d", "u");
            this.translitChars.put("\u1e33", "k");
            this.translitChars.put("\ua76b", "et");
            this.translitChars.put("\u012b", "i");
            this.translitChars.put("\u0165", "t");
            this.translitChars.put("\ua73f", "c");
            this.translitChars.put("\u029f", "l");
            this.translitChars.put("\ua739", "av");
            this.translitChars.put("\u00fb", "u");
            this.translitChars.put("\u00e6", "ae");
            this.translitChars.put("\u0103", "a");
            this.translitChars.put("\u01d8", "u");
            this.translitChars.put("\ua785", "s");
            this.translitChars.put("\u1d63", "r");
            this.translitChars.put("\u1d00", "a");
            this.translitChars.put("\u0183", "b");
            this.translitChars.put("\u1e29", "h");
            this.translitChars.put("\u1e67", "s");
            this.translitChars.put("\u2091", "e");
            this.translitChars.put("\u029c", "h");
            this.translitChars.put("\u1e8b", "x");
            this.translitChars.put("\ua745", "k");
            this.translitChars.put("\u1e0b", "d");
            this.translitChars.put("\u01a3", "oi");
            this.translitChars.put("\ua751", "p");
            this.translitChars.put("\u0127", "h");
            this.translitChars.put("\u2c74", "v");
            this.translitChars.put("\u1e87", "w");
            this.translitChars.put("\u01f9", "n");
            this.translitChars.put("\u026f", "m");
            this.translitChars.put("\u0261", "g");
            this.translitChars.put("\u0274", "n");
            this.translitChars.put("\u1d18", "p");
            this.translitChars.put("\u1d65", "v");
            this.translitChars.put("\u016b", "u");
            this.translitChars.put("\u1e03", "b");
            this.translitChars.put("\u1e57", "p");
            this.translitChars.put("\u00e5", "a");
            this.translitChars.put("\u0255", "c");
            this.translitChars.put("\u1ecd", "o");
            this.translitChars.put("\u1eaf", "a");
            this.translitChars.put("\u0192", "f");
            this.translitChars.put("\u01e3", "ae");
            this.translitChars.put("\ua761", "vy");
            this.translitChars.put("\ufb00", "ff");
            this.translitChars.put("\u1d89", "r");
            this.translitChars.put("\u00f4", "o");
            this.translitChars.put("\u01ff", "o");
            this.translitChars.put("\u1e73", "u");
            this.translitChars.put("\u0225", "z");
            this.translitChars.put("\u1e1f", "f");
            this.translitChars.put("\u1e13", "d");
            this.translitChars.put("\u0207", "e");
            this.translitChars.put("\u0215", "u");
            this.translitChars.put("\u0235", "n");
            this.translitChars.put("\u02a0", "q");
            this.translitChars.put("\u1ea5", "a");
            this.translitChars.put("\u01e9", "k");
            this.translitChars.put("\u0129", "i");
            this.translitChars.put("\u1e75", "u");
            this.translitChars.put("\u0167", "t");
            this.translitChars.put("\u027e", "r");
            this.translitChars.put("\u0199", "k");
            this.translitChars.put("\u1e6b", "t");
            this.translitChars.put("\ua757", "q");
            this.translitChars.put("\u1ead", "a");
            this.translitChars.put("\u0284", "j");
            this.translitChars.put("\u019a", "l");
            this.translitChars.put("\u1d82", "f");
            this.translitChars.put("\u1d74", "s");
            this.translitChars.put("\ua783", "r");
            this.translitChars.put("\u1d8c", "v");
            this.translitChars.put("\u0275", "o");
            this.translitChars.put("\u1e09", "c");
            this.translitChars.put("\u1d64", "u");
            this.translitChars.put("\u1e91", "z");
            this.translitChars.put("\u1e79", "u");
            this.translitChars.put("\u0148", "n");
            this.translitChars.put("\u028d", "w");
            this.translitChars.put("\u1ea7", "a");
            this.translitChars.put("\u01c9", "lj");
            this.translitChars.put("\u0253", "b");
            this.translitChars.put("\u027c", "r");
            this.translitChars.put("\u00f2", "o");
            this.translitChars.put("\u1e98", "w");
            this.translitChars.put("\u0257", "d");
            this.translitChars.put("\ua73d", "ay");
            this.translitChars.put("\u01b0", "u");
            this.translitChars.put("\u1d80", "b");
            this.translitChars.put("\u01dc", "u");
            this.translitChars.put("\u1eb9", "e");
            this.translitChars.put("\u01e1", "a");
            this.translitChars.put("\u0265", "h");
            this.translitChars.put("\u1e4f", "o");
            this.translitChars.put("\u01d4", "u");
            this.translitChars.put("\u028e", "y");
            this.translitChars.put("\u0231", "o");
            this.translitChars.put("\u1ec7", "e");
            this.translitChars.put("\u1ebf", "e");
            this.translitChars.put("\u012d", "i");
            this.translitChars.put("\u2c78", "e");
            this.translitChars.put("\u1e6f", "t");
            this.translitChars.put("\u1d91", "d");
            this.translitChars.put("\u1e27", "h");
            this.translitChars.put("\u1e65", "s");
            this.translitChars.put("\u00eb", "e");
            this.translitChars.put("\u1d0d", "m");
            this.translitChars.put("\u00f6", "o");
            this.translitChars.put("\u00e9", "e");
            this.translitChars.put("\u0131", "i");
            this.translitChars.put("\u010f", "d");
            this.translitChars.put("\u1d6f", "m");
            this.translitChars.put("\u1ef5", "y");
            this.translitChars.put("\u0175", "w");
            this.translitChars.put("\u1ec1", "e");
            this.translitChars.put("\u1ee9", "u");
            this.translitChars.put("\u01b6", "z");
            this.translitChars.put("\u0135", "j");
            this.translitChars.put("\u1e0d", "d");
            this.translitChars.put("\u016d", "u");
            this.translitChars.put("\u029d", "j");
            this.translitChars.put("\u00ea", "e");
            this.translitChars.put("\u01da", "u");
            this.translitChars.put("\u0121", "g");
            this.translitChars.put("\u1e59", "r");
            this.translitChars.put("\u019e", "n");
            this.translitChars.put("\u1e17", "e");
            this.translitChars.put("\u1e9d", "s");
            this.translitChars.put("\u1d81", "d");
            this.translitChars.put("\u0137", "k");
            this.translitChars.put("\u1d02", "ae");
            this.translitChars.put("\u0258", "e");
            this.translitChars.put("\u1ee3", "o");
            this.translitChars.put("\u1e3f", "m");
            this.translitChars.put("\ua730", "f");
            this.translitChars.put("\u1eb5", "a");
            this.translitChars.put("\ua74f", "oo");
            this.translitChars.put("\u1d86", "m");
            this.translitChars.put("\u1d7d", "p");
            this.translitChars.put("\u1eef", "u");
            this.translitChars.put("\u2c6a", "k");
            this.translitChars.put("\u1e25", "h");
            this.translitChars.put("\u0163", "t");
            this.translitChars.put("\u1d71", "p");
            this.translitChars.put("\u1e41", "m");
            this.translitChars.put("\u00e1", "a");
            this.translitChars.put("\u1d0e", "n");
            this.translitChars.put("\ua75f", "v");
            this.translitChars.put("\u00e8", "e");
            this.translitChars.put("\u1d8e", "z");
            this.translitChars.put("\ua77a", "d");
            this.translitChars.put("\u1d88", "p");
            this.translitChars.put("\u026b", "l");
            this.translitChars.put("\u1d22", "z");
            this.translitChars.put("\u0271", "m");
            this.translitChars.put("\u1e5d", "r");
            this.translitChars.put("\u1e7d", "v");
            this.translitChars.put("\u0169", "u");
            this.translitChars.put("\u00df", "ss");
            this.translitChars.put("\u0125", "h");
            this.translitChars.put("\u1d75", "t");
            this.translitChars.put("\u0290", "z");
            this.translitChars.put("\u1e5f", "r");
            this.translitChars.put("\u0272", "n");
            this.translitChars.put("\u00e0", "a");
            this.translitChars.put("\u1e99", "y");
            this.translitChars.put("\u1ef3", "y");
            this.translitChars.put("\u1d14", "oe");
            this.translitChars.put("\u2093", "x");
            this.translitChars.put("\u0217", "u");
            this.translitChars.put("\u2c7c", "j");
            this.translitChars.put("\u1eab", "a");
            this.translitChars.put("\u0291", "z");
            this.translitChars.put("\u1e9b", "s");
            this.translitChars.put("\u1e2d", "i");
            this.translitChars.put("\ua735", "ao");
            this.translitChars.put("\u0240", "z");
            this.translitChars.put("\u00ff", "y");
            this.translitChars.put("\u01dd", "e");
            this.translitChars.put("\u01ed", "o");
            this.translitChars.put("\u1d05", "d");
            this.translitChars.put("\u1d85", "l");
            this.translitChars.put("\u00f9", "u");
            this.translitChars.put("\u1ea1", "a");
            this.translitChars.put("\u1e05", "b");
            this.translitChars.put("\u1ee5", "u");
            this.translitChars.put("\u1eb1", "a");
            this.translitChars.put("\u1d1b", "t");
            this.translitChars.put("\u01b4", "y");
            this.translitChars.put("\u2c66", "t");
            this.translitChars.put("\u2c61", "l");
            this.translitChars.put("\u0237", "j");
            this.translitChars.put("\u1d76", "z");
            this.translitChars.put("\u1e2b", "h");
            this.translitChars.put("\u2c73", "w");
            this.translitChars.put("\u1e35", "k");
            this.translitChars.put("\u1edd", "o");
            this.translitChars.put("\u00ee", "i");
            this.translitChars.put("\u0123", "g");
            this.translitChars.put("\u0205", "e");
            this.translitChars.put("\u0227", "a");
            this.translitChars.put("\u1eb3", "a");
            this.translitChars.put("\u024b", "q");
            this.translitChars.put("\u1e6d", "t");
            this.translitChars.put("\ua778", "um");
            this.translitChars.put("\u1d04", "c");
            this.translitChars.put("\u1e8d", "x");
            this.translitChars.put("\u1ee7", "u");
            this.translitChars.put("\u1ec9", "i");
            this.translitChars.put("\u1d1a", "r");
            this.translitChars.put("\u015b", "s");
            this.translitChars.put("\ua74b", "o");
            this.translitChars.put("\u1ef9", "y");
            this.translitChars.put("\u1e61", "s");
            this.translitChars.put("\u01cc", "nj");
            this.translitChars.put("\u0201", "a");
            this.translitChars.put("\u1e97", "t");
            this.translitChars.put("\u013a", "l");
            this.translitChars.put("\u017e", "z");
            this.translitChars.put("\u1d7a", "th");
            this.translitChars.put("\u018c", "d");
            this.translitChars.put("\u0219", "s");
            this.translitChars.put("\u0161", "s");
            this.translitChars.put("\u1d99", "u");
            this.translitChars.put("\u1ebd", "e");
            this.translitChars.put("\u1e9c", "s");
            this.translitChars.put("\u0247", "e");
            this.translitChars.put("\u1e77", "u");
            this.translitChars.put("\u1ed1", "o");
            this.translitChars.put("\u023f", "s");
            this.translitChars.put("\u1d20", "v");
            this.translitChars.put("\ua76d", "is");
            this.translitChars.put("\u1d0f", "o");
            this.translitChars.put("\u025b", "e");
            this.translitChars.put("\u01fb", "a");
            this.translitChars.put("\ufb04", "ffl");
            this.translitChars.put("\u2c7a", "o");
            this.translitChars.put("\u020b", "i");
            this.translitChars.put("\u1d6b", "ue");
            this.translitChars.put("\u0221", "d");
            this.translitChars.put("\u2c6c", "z");
            this.translitChars.put("\u1e81", "w");
            this.translitChars.put("\u1d8f", "a");
            this.translitChars.put("\ua787", "t");
            this.translitChars.put("\u011f", "g");
            this.translitChars.put("\u0273", "n");
            this.translitChars.put("\u029b", "g");
            this.translitChars.put("\u1d1c", "u");
            this.translitChars.put("\u1ea9", "a");
            this.translitChars.put("\u1e45", "n");
            this.translitChars.put("\u0268", "i");
            this.translitChars.put("\u1d19", "r");
            this.translitChars.put("\u01ce", "a");
            this.translitChars.put("\u017f", "s");
            this.translitChars.put("\u022b", "o");
            this.translitChars.put("\u027f", "r");
            this.translitChars.put("\u01ad", "t");
            this.translitChars.put("\u1e2f", "i");
            this.translitChars.put("\u01fd", "ae");
            this.translitChars.put("\u2c71", "v");
            this.translitChars.put("\u0276", "oe");
            this.translitChars.put("\u1e43", "m");
            this.translitChars.put("\u017c", "z");
            this.translitChars.put("\u0115", "e");
            this.translitChars.put("\ua73b", "av");
            this.translitChars.put("\u1edf", "o");
            this.translitChars.put("\u1ec5", "e");
            this.translitChars.put("\u026c", "l");
            this.translitChars.put("\u1ecb", "i");
            this.translitChars.put("\u1d6d", "d");
            this.translitChars.put("\ufb06", "st");
            this.translitChars.put("\u1e37", "l");
            this.translitChars.put("\u0155", "r");
            this.translitChars.put("\u1d15", "ou");
            this.translitChars.put("\u0288", "t");
            this.translitChars.put("\u0101", "a");
            this.translitChars.put("\u1e19", "e");
            this.translitChars.put("\u1d11", "o");
            this.translitChars.put("\u00e7", "c");
            this.translitChars.put("\u1d8a", "s");
            this.translitChars.put("\u1eb7", "a");
            this.translitChars.put("\u0173", "u");
            this.translitChars.put("\u1ea3", "a");
            this.translitChars.put("\u01e5", "g");
            this.translitChars.put("\ua741", "k");
            this.translitChars.put("\u1e95", "z");
            this.translitChars.put("\u015d", "s");
            this.translitChars.put("\u1e15", "e");
            this.translitChars.put("\u0260", "g");
            this.translitChars.put("\ua749", "l");
            this.translitChars.put("\ua77c", "f");
            this.translitChars.put("\u1d8d", "x");
            this.translitChars.put("\u01d2", "o");
            this.translitChars.put("\u0119", "e");
            this.translitChars.put("\u1ed5", "o");
            this.translitChars.put("\u01ab", "t");
            this.translitChars.put("\u01eb", "o");
            this.translitChars.put("i\u0307", "i");
            this.translitChars.put("\u1e47", "n");
            this.translitChars.put("\u0107", "c");
            this.translitChars.put("\u1d77", "g");
            this.translitChars.put("\u1e85", "w");
            this.translitChars.put("\u1e11", "d");
            this.translitChars.put("\u1e39", "l");
            this.translitChars.put("\u0153", "oe");
            this.translitChars.put("\u1d73", "r");
            this.translitChars.put("\u013c", "l");
            this.translitChars.put("\u0211", "r");
            this.translitChars.put("\u022d", "o");
            this.translitChars.put("\u1d70", "n");
            this.translitChars.put("\u1d01", "ae");
            this.translitChars.put("\u0140", "l");
            this.translitChars.put("\u00e4", "a");
            this.translitChars.put("\u01a5", "p");
            this.translitChars.put("\u1ecf", "o");
            this.translitChars.put("\u012f", "i");
            this.translitChars.put("\u0213", "r");
            this.translitChars.put("\u01c6", "dz");
            this.translitChars.put("\u1e21", "g");
            this.translitChars.put("\u1e7b", "u");
            this.translitChars.put("\u014d", "o");
            this.translitChars.put("\u013e", "l");
            this.translitChars.put("\u1e83", "w");
            this.translitChars.put("\u021b", "t");
            this.translitChars.put("\u0144", "n");
            this.translitChars.put("\u024d", "r");
            this.translitChars.put("\u0203", "a");
            this.translitChars.put("\u00fc", "u");
            this.translitChars.put("\ua781", "l");
            this.translitChars.put("\u1d10", "o");
            this.translitChars.put("\u1edb", "o");
            this.translitChars.put("\u1d03", "b");
            this.translitChars.put("\u0279", "r");
            this.translitChars.put("\u1d72", "r");
            this.translitChars.put("\u028f", "y");
            this.translitChars.put("\u1d6e", "f");
            this.translitChars.put("\u2c68", "h");
            this.translitChars.put("\u014f", "o");
            this.translitChars.put("\u00fa", "u");
            this.translitChars.put("\u1e5b", "r");
            this.translitChars.put("\u02ae", "h");
            this.translitChars.put("\u00f3", "o");
            this.translitChars.put("\u016f", "u");
            this.translitChars.put("\u1ee1", "o");
            this.translitChars.put("\u1e55", "p");
            this.translitChars.put("\u1d96", "i");
            this.translitChars.put("\u1ef1", "u");
            this.translitChars.put("\u00e3", "a");
            this.translitChars.put("\u1d62", "i");
            this.translitChars.put("\u1e71", "t");
            this.translitChars.put("\u1ec3", "e");
            this.translitChars.put("\u1eed", "u");
            this.translitChars.put("\u00ed", "i");
            this.translitChars.put("\u0254", "o");
            this.translitChars.put("\u027a", "r");
            this.translitChars.put("\u0262", "g");
            this.translitChars.put("\u0159", "r");
            this.translitChars.put("\u1e96", "h");
            this.translitChars.put("\u0171", "u");
            this.translitChars.put("\u020d", "o");
            this.translitChars.put("\u1e3b", "l");
            this.translitChars.put("\u1e23", "h");
            this.translitChars.put("\u0236", "t");
            this.translitChars.put("\u0146", "n");
            this.translitChars.put("\u1d92", "e");
            this.translitChars.put("\u00ec", "i");
            this.translitChars.put("\u1e89", "w");
            this.translitChars.put("\u0113", "e");
            this.translitChars.put("\u1d07", "e");
            this.translitChars.put("\u0142", "l");
            this.translitChars.put("\u1ed9", "o");
            this.translitChars.put("\u026d", "l");
            this.translitChars.put("\u1e8f", "y");
            this.translitChars.put("\u1d0a", "j");
            this.translitChars.put("\u1e31", "k");
            this.translitChars.put("\u1e7f", "v");
            this.translitChars.put("\u0229", "e");
            this.translitChars.put("\u00e2", "a");
            this.translitChars.put("\u015f", "s");
            this.translitChars.put("\u0157", "r");
            this.translitChars.put("\u028b", "v");
            this.translitChars.put("\u2090", "a");
            this.translitChars.put("\u2184", "c");
            this.translitChars.put("\u1d93", "e");
            this.translitChars.put("\u0270", "m");
            this.translitChars.put("\u1d21", "w");
            this.translitChars.put("\u020f", "o");
            this.translitChars.put("\u010d", "c");
            this.translitChars.put("\u01f5", "g");
            this.translitChars.put("\u0109", "c");
            this.translitChars.put("\u1d97", "o");
            this.translitChars.put("\ua743", "k");
            this.translitChars.put("\ua759", "q");
            this.translitChars.put("\u1e51", "o");
            this.translitChars.put("\ua731", "s");
            this.translitChars.put("\u1e53", "o");
            this.translitChars.put("\u021f", "h");
            this.translitChars.put("\u0151", "o");
            this.translitChars.put("\ua729", "tz");
            this.translitChars.put("\u1ebb", "e");
        }
        final StringBuilder sb = new StringBuilder(s.length());
        final int length = s.length();
        boolean b3 = false;
        int endIndex;
        for (int i = 0; i < length; i = endIndex) {
            endIndex = i + 1;
            final String substring = s.substring(i, endIndex);
            String lowerCase;
            if (b2) {
                lowerCase = substring.toLowerCase();
                b3 = (substring.equals(lowerCase) ^ true);
            }
            else {
                lowerCase = substring;
            }
            String s3;
            final String s2 = s3 = this.translitChars.get(lowerCase);
            if (s2 == null) {
                s3 = s2;
                if (b) {
                    s3 = this.ruTranslitChars.get(lowerCase);
                }
            }
            if (s3 != null) {
                String str = s3;
                if (b2) {
                    str = s3;
                    if (b3) {
                        if (s3.length() > 1) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append(s3.substring(0, 1).toUpperCase());
                            sb2.append(s3.substring(1));
                            str = sb2.toString();
                        }
                        else {
                            str = s3.toUpperCase();
                        }
                    }
                }
                sb.append(str);
            }
            else {
                String upperCase = lowerCase;
                if (b2) {
                    final char char1 = lowerCase.charAt(0);
                    if ((char1 < 'a' || char1 > 'z' || char1 < '0' || char1 > '9') && char1 != ' ' && char1 != '\'' && char1 != ',' && char1 != '.' && char1 != '&' && char1 != '-' && char1 != '/') {
                        return null;
                    }
                    upperCase = lowerCase;
                    if (b3) {
                        upperCase = lowerCase.toUpperCase();
                    }
                }
                sb.append(upperCase);
            }
        }
        return sb.toString();
    }
    
    public boolean isCurrentLocalLocale() {
        return this.currentLocaleInfo.isLocal();
    }
    
    public void loadRemoteLanguages(final int n) {
        if (this.loadingRemoteLanguages) {
            return;
        }
        this.loadingRemoteLanguages = true;
        ConnectionsManager.getInstance(n).sendRequest(new TLRPC.TL_langpack_getLanguages(), new _$$Lambda$LocaleController$OO_St8W4lBDCp1N4EzTA2EggA1M(this, n), 8);
    }
    
    public void onDeviceConfigurationChange(final Configuration configuration) {
        if (this.changingConfiguration) {
            return;
        }
        LocaleController.is24HourFormat = DateFormat.is24HourFormat(ApplicationLoader.applicationContext);
        final Locale locale = configuration.locale;
        this.systemDefaultLocale = locale;
        if (this.languageOverride != null) {
            final LocaleInfo currentLocaleInfo = this.currentLocaleInfo;
            this.currentLocaleInfo = null;
            this.applyLanguage(currentLocaleInfo, false, false, UserConfig.selectedAccount);
        }
        else if (locale != null) {
            final String displayName = locale.getDisplayName();
            final String displayName2 = this.currentLocale.getDisplayName();
            if (displayName != null && displayName2 != null && !displayName.equals(displayName2)) {
                this.recreateFormatters();
            }
            this.currentLocale = locale;
            final LocaleInfo currentLocaleInfo2 = this.currentLocaleInfo;
            if (currentLocaleInfo2 != null && !TextUtils.isEmpty((CharSequence)currentLocaleInfo2.pluralLangCode)) {
                this.currentPluralRules = this.allRules.get(this.currentLocaleInfo.pluralLangCode);
            }
            if (this.currentPluralRules == null) {
                this.currentPluralRules = this.allRules.get(this.currentLocale.getLanguage());
                if (this.currentPluralRules == null) {
                    this.currentPluralRules = this.allRules.get("en");
                }
            }
        }
        final String systemLocaleStringIso639 = getSystemLocaleStringIso639();
        final String currentSystemLocale = this.currentSystemLocale;
        if (currentSystemLocale != null && !systemLocaleStringIso639.equals(currentSystemLocale)) {
            ConnectionsManager.setSystemLangCode(this.currentSystemLocale = systemLocaleStringIso639);
        }
    }
    
    public void recreateFormatters() {
        Locale locale;
        if ((locale = this.currentLocale) == null) {
            locale = Locale.getDefault();
        }
        String language;
        if ((language = locale.getLanguage()) == null) {
            language = "en";
        }
        final String lowerCase = language.toLowerCase();
        final int length = lowerCase.length();
        int nameDisplayOrder = 1;
        boolean isRTL = false;
        Label_0155: {
            if ((length != 2 || (!lowerCase.equals("ar") && !lowerCase.equals("fa") && !lowerCase.equals("he") && !lowerCase.equals("iw"))) && !lowerCase.startsWith("ar_") && !lowerCase.startsWith("fa_") && !lowerCase.startsWith("he_") && !lowerCase.startsWith("iw_")) {
                final LocaleInfo currentLocaleInfo = this.currentLocaleInfo;
                if (currentLocaleInfo == null || !currentLocaleInfo.isRtl) {
                    isRTL = false;
                    break Label_0155;
                }
            }
            isRTL = true;
        }
        LocaleController.isRTL = isRTL;
        if (lowerCase.equals("ko")) {
            nameDisplayOrder = 2;
        }
        LocaleController.nameDisplayOrder = nameDisplayOrder;
        this.formatterDayMonth = this.createFormatter(locale, this.getStringInternal("formatterMonth", 2131561223), "dd MMM");
        this.formatterYear = this.createFormatter(locale, this.getStringInternal("formatterYear", 2131561229), "dd.MM.yy");
        this.formatterYearMax = this.createFormatter(locale, this.getStringInternal("formatterYearMax", 2131561230), "dd.MM.yyyy");
        this.chatDate = this.createFormatter(locale, this.getStringInternal("chatDate", 2131561204), "d MMMM");
        this.chatFullDate = this.createFormatter(locale, this.getStringInternal("chatFullDate", 2131561205), "d MMMM yyyy");
        this.formatterWeek = this.createFormatter(locale, this.getStringInternal("formatterWeek", 2131561228), "EEE");
        this.formatterScheduleDay = this.createFormatter(locale, this.getStringInternal("formatDateScheduleDay", 2131561211), "EEE MMM d");
        Locale us;
        if (!lowerCase.toLowerCase().equals("ar") && !lowerCase.toLowerCase().equals("ko")) {
            us = Locale.US;
        }
        else {
            us = locale;
        }
        int n;
        String s;
        if (LocaleController.is24HourFormat) {
            n = 2131561222;
            s = "formatterDay24H";
        }
        else {
            n = 2131561221;
            s = "formatterDay12H";
        }
        final String stringInternal = this.getStringInternal(s, n);
        String s2;
        if (LocaleController.is24HourFormat) {
            s2 = "HH:mm";
        }
        else {
            s2 = "h:mm a";
        }
        this.formatterDay = this.createFormatter(us, stringInternal, s2);
        int n2;
        String s3;
        if (LocaleController.is24HourFormat) {
            n2 = 2131561227;
            s3 = "formatterStats24H";
        }
        else {
            n2 = 2131561226;
            s3 = "formatterStats12H";
        }
        final String stringInternal2 = this.getStringInternal(s3, n2);
        final boolean is24HourFormat = LocaleController.is24HourFormat;
        final String s4 = "MMM dd yyyy, HH:mm";
        String s5;
        if (is24HourFormat) {
            s5 = "MMM dd yyyy, HH:mm";
        }
        else {
            s5 = "MMM dd yyyy, h:mm a";
        }
        this.formatterStats = this.createFormatter(locale, stringInternal2, s5);
        int n3;
        String s6;
        if (LocaleController.is24HourFormat) {
            n3 = 2131561218;
            s6 = "formatterBannedUntil24H";
        }
        else {
            n3 = 2131561217;
            s6 = "formatterBannedUntil12H";
        }
        final String stringInternal3 = this.getStringInternal(s6, n3);
        String s7;
        if (LocaleController.is24HourFormat) {
            s7 = s4;
        }
        else {
            s7 = "MMM dd yyyy, h:mm a";
        }
        this.formatterBannedUntil = this.createFormatter(locale, stringInternal3, s7);
        int n4;
        String s8;
        if (LocaleController.is24HourFormat) {
            n4 = 2131561220;
            s8 = "formatterBannedUntilThisYear24H";
        }
        else {
            n4 = 2131561219;
            s8 = "formatterBannedUntilThisYear12H";
        }
        final String stringInternal4 = this.getStringInternal(s8, n4);
        String s9;
        if (LocaleController.is24HourFormat) {
            s9 = "MMM dd, HH:mm";
        }
        else {
            s9 = "MMM dd, h:mm a";
        }
        this.formatterBannedUntilThisYear = this.createFormatter(locale, stringInternal4, s9);
    }
    
    public void reloadCurrentRemoteLocale(final int n, final String s) {
        String replace = s;
        if (s != null) {
            replace = s.replace("-", "_");
        }
        if (replace != null) {
            final LocaleInfo currentLocaleInfo = this.currentLocaleInfo;
            if (currentLocaleInfo == null || (!replace.equals(currentLocaleInfo.shortName) && !replace.equals(this.currentLocaleInfo.baseLangCode))) {
                return;
            }
        }
        this.applyRemoteLanguage(this.currentLocaleInfo, replace, true, n);
    }
    
    public void saveRemoteLocaleStrings(final LocaleInfo localeInfo, final TLRPC.TL_langPackDifference tl_langPackDifference, int n) {
        if (tl_langPackDifference == null || tl_langPackDifference.strings.isEmpty() || localeInfo == null) {
            return;
        }
        if (localeInfo.isLocal()) {
            return;
        }
        final String lowerCase = tl_langPackDifference.lang_code.replace('-', '_').toLowerCase();
        if (lowerCase.equals(localeInfo.shortName)) {
            n = 0;
        }
        else if (lowerCase.equals(localeInfo.baseLangCode)) {
            n = 1;
        }
        else {
            n = -1;
        }
        if (n == -1) {
            return;
        }
        File file;
        if (n == 0) {
            file = localeInfo.getPathToFile();
        }
        else {
            file = localeInfo.getPathToBaseFile();
        }
        try {
            HashMap<String, String> localeFileStrings;
            if (tl_langPackDifference.from_version == 0) {
                localeFileStrings = new HashMap<String, String>();
            }
            else {
                localeFileStrings = this.getLocaleFileStrings(file, true);
            }
            for (int i = 0; i < tl_langPackDifference.strings.size(); ++i) {
                final TLRPC.LangPackString langPackString = tl_langPackDifference.strings.get(i);
                if (langPackString instanceof TLRPC.TL_langPackString) {
                    localeFileStrings.put(langPackString.key, this.escapeString(langPackString.value));
                }
                else if (langPackString instanceof TLRPC.TL_langPackStringPluralized) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(langPackString.key);
                    sb.append("_zero");
                    final String string = sb.toString();
                    final String zero_value = langPackString.zero_value;
                    final String s = "";
                    String escapeString;
                    if (zero_value != null) {
                        escapeString = this.escapeString(langPackString.zero_value);
                    }
                    else {
                        escapeString = "";
                    }
                    localeFileStrings.put(string, escapeString);
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(langPackString.key);
                    sb2.append("_one");
                    final String string2 = sb2.toString();
                    String escapeString2;
                    if (langPackString.one_value != null) {
                        escapeString2 = this.escapeString(langPackString.one_value);
                    }
                    else {
                        escapeString2 = "";
                    }
                    localeFileStrings.put(string2, escapeString2);
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append(langPackString.key);
                    sb3.append("_two");
                    final String string3 = sb3.toString();
                    String escapeString3;
                    if (langPackString.two_value != null) {
                        escapeString3 = this.escapeString(langPackString.two_value);
                    }
                    else {
                        escapeString3 = "";
                    }
                    localeFileStrings.put(string3, escapeString3);
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append(langPackString.key);
                    sb4.append("_few");
                    final String string4 = sb4.toString();
                    String escapeString4;
                    if (langPackString.few_value != null) {
                        escapeString4 = this.escapeString(langPackString.few_value);
                    }
                    else {
                        escapeString4 = "";
                    }
                    localeFileStrings.put(string4, escapeString4);
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append(langPackString.key);
                    sb5.append("_many");
                    final String string5 = sb5.toString();
                    String escapeString5;
                    if (langPackString.many_value != null) {
                        escapeString5 = this.escapeString(langPackString.many_value);
                    }
                    else {
                        escapeString5 = "";
                    }
                    localeFileStrings.put(string5, escapeString5);
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append(langPackString.key);
                    sb6.append("_other");
                    final String string6 = sb6.toString();
                    String escapeString6 = s;
                    if (langPackString.other_value != null) {
                        escapeString6 = this.escapeString(langPackString.other_value);
                    }
                    localeFileStrings.put(string6, escapeString6);
                }
                else if (langPackString instanceof TLRPC.TL_langPackStringDeleted) {
                    localeFileStrings.remove(langPackString.key);
                }
            }
            if (BuildVars.LOGS_ENABLED) {
                final StringBuilder sb7 = new StringBuilder();
                sb7.append("save locale file to ");
                sb7.append(file);
                FileLog.d(sb7.toString());
            }
            final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
            bufferedWriter.write("<resources>\n");
            for (final Map.Entry<String, String> entry : localeFileStrings.entrySet()) {
                bufferedWriter.write(String.format("<string name=\"%1$s\">%2$s</string>\n", entry.getKey(), entry.getValue()));
            }
            bufferedWriter.write("</resources>");
            bufferedWriter.close();
            final boolean hasBaseLang = localeInfo.hasBaseLang();
            File file2;
            if (hasBaseLang) {
                file2 = localeInfo.getPathToBaseFile();
            }
            else {
                file2 = localeInfo.getPathToFile();
            }
            final HashMap<String, String> localeFileStrings2 = this.getLocaleFileStrings(file2);
            if (hasBaseLang) {
                localeFileStrings2.putAll(this.getLocaleFileStrings(localeInfo.getPathToFile()));
            }
            AndroidUtilities.runOnUIThread(new _$$Lambda$LocaleController$byYfLkXSOjQxKwpHcMJU1BFCcao(this, localeInfo, n, tl_langPackDifference, localeFileStrings2));
        }
        catch (Exception ex) {}
    }
    
    public void saveRemoteLocaleStringsForCurrentLocale(final TLRPC.TL_langPackDifference tl_langPackDifference, final int n) {
        if (this.currentLocaleInfo == null) {
            return;
        }
        final String lowerCase = tl_langPackDifference.lang_code.replace('-', '_').toLowerCase();
        if (!lowerCase.equals(this.currentLocaleInfo.shortName) && !lowerCase.equals(this.currentLocaleInfo.baseLangCode)) {
            return;
        }
        this.saveRemoteLocaleStrings(this.currentLocaleInfo, tl_langPackDifference, n);
    }
    
    public static class LocaleInfo
    {
        public String baseLangCode;
        public int baseVersion;
        public boolean builtIn;
        public boolean isRtl;
        public String name;
        public String nameEnglish;
        public String pathToFile;
        public String pluralLangCode;
        public int serverIndex;
        public String shortName;
        public int version;
        
        public static LocaleInfo createWithString(String shortName) {
            LocaleInfo localeInfo2;
            final LocaleInfo localeInfo = localeInfo2 = null;
            if (shortName != null) {
                if (shortName.length() == 0) {
                    localeInfo2 = localeInfo;
                }
                else {
                    final String[] split = shortName.split("\\|");
                    localeInfo2 = localeInfo;
                    if (split.length >= 4) {
                        final LocaleInfo localeInfo3 = new LocaleInfo();
                        boolean isRtl = false;
                        localeInfo3.name = split[0];
                        localeInfo3.nameEnglish = split[1];
                        localeInfo3.shortName = split[2].toLowerCase();
                        localeInfo3.pathToFile = split[3];
                        if (split.length >= 5) {
                            localeInfo3.version = Utilities.parseInt(split[4]);
                        }
                        if (split.length >= 6) {
                            shortName = split[5];
                        }
                        else {
                            shortName = "";
                        }
                        localeInfo3.baseLangCode = shortName;
                        if (split.length >= 7) {
                            shortName = split[6];
                        }
                        else {
                            shortName = localeInfo3.shortName;
                        }
                        localeInfo3.pluralLangCode = shortName;
                        if (split.length >= 8) {
                            if (Utilities.parseInt(split[7]) == 1) {
                                isRtl = true;
                            }
                            localeInfo3.isRtl = isRtl;
                        }
                        if (split.length >= 9) {
                            localeInfo3.baseVersion = Utilities.parseInt(split[8]);
                        }
                        if (split.length >= 10) {
                            localeInfo3.serverIndex = Utilities.parseInt(split[9]);
                        }
                        else {
                            localeInfo3.serverIndex = Integer.MAX_VALUE;
                        }
                        localeInfo2 = localeInfo3;
                        if (!TextUtils.isEmpty((CharSequence)localeInfo3.baseLangCode)) {
                            localeInfo3.baseLangCode = localeInfo3.baseLangCode.replace("-", "_");
                            localeInfo2 = localeInfo3;
                        }
                    }
                }
            }
            return localeInfo2;
        }
        
        public String getBaseLangCode() {
            final String baseLangCode = this.baseLangCode;
            String replace;
            if (baseLangCode == null) {
                replace = "";
            }
            else {
                replace = baseLangCode.replace("_", "-");
            }
            return replace;
        }
        
        public String getKey() {
            if (this.pathToFile != null && !this.isRemote() && !this.isUnofficial()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("local_");
                sb.append(this.shortName);
                return sb.toString();
            }
            if (this.isUnofficial()) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("unofficial_");
                sb2.append(this.shortName);
                return sb2.toString();
            }
            return this.shortName;
        }
        
        public String getLangCode() {
            return this.shortName.replace("_", "-");
        }
        
        public File getPathToBaseFile() {
            if (this.isUnofficial()) {
                final File filesDirFixed = ApplicationLoader.getFilesDirFixed();
                final StringBuilder sb = new StringBuilder();
                sb.append("unofficial_base_");
                sb.append(this.shortName);
                sb.append(".xml");
                return new File(filesDirFixed, sb.toString());
            }
            return null;
        }
        
        public File getPathToFile() {
            if (this.isRemote()) {
                final File filesDirFixed = ApplicationLoader.getFilesDirFixed();
                final StringBuilder sb = new StringBuilder();
                sb.append("remote_");
                sb.append(this.shortName);
                sb.append(".xml");
                return new File(filesDirFixed, sb.toString());
            }
            if (this.isUnofficial()) {
                final File filesDirFixed2 = ApplicationLoader.getFilesDirFixed();
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("unofficial_");
                sb2.append(this.shortName);
                sb2.append(".xml");
                return new File(filesDirFixed2, sb2.toString());
            }
            File file;
            if (!TextUtils.isEmpty((CharSequence)this.pathToFile)) {
                file = new File(this.pathToFile);
            }
            else {
                file = null;
            }
            return file;
        }
        
        public String getSaveString() {
            String baseLangCode;
            if ((baseLangCode = this.baseLangCode) == null) {
                baseLangCode = "";
            }
            if (TextUtils.isEmpty((CharSequence)this.pluralLangCode)) {
                final String shortName = this.shortName;
            }
            else {
                final String pluralLangCode = this.pluralLangCode;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(this.name);
            sb.append("|");
            sb.append(this.nameEnglish);
            sb.append("|");
            sb.append(this.shortName);
            sb.append("|");
            sb.append(this.pathToFile);
            sb.append("|");
            sb.append(this.version);
            sb.append("|");
            sb.append(baseLangCode);
            sb.append("|");
            sb.append(this.pluralLangCode);
            sb.append("|");
            sb.append(this.isRtl ? 1 : 0);
            sb.append("|");
            sb.append(this.baseVersion);
            sb.append("|");
            sb.append(this.serverIndex);
            return sb.toString();
        }
        
        public boolean hasBaseLang() {
            return this.isUnofficial() && !TextUtils.isEmpty((CharSequence)this.baseLangCode) && !this.baseLangCode.equals(this.shortName);
        }
        
        public boolean isBuiltIn() {
            return this.builtIn;
        }
        
        public boolean isLocal() {
            return !TextUtils.isEmpty((CharSequence)this.pathToFile) && !this.isRemote() && !this.isUnofficial();
        }
        
        public boolean isRemote() {
            return "remote".equals(this.pathToFile);
        }
        
        public boolean isUnofficial() {
            return "unofficial".equals(this.pathToFile);
        }
    }
    
    public abstract static class PluralRules
    {
        abstract int quantityForNumber(final int p0);
    }
    
    public static class PluralRules_Arabic extends PluralRules
    {
        public int quantityForNumber(final int n) {
            final int n2 = n % 100;
            if (n == 0) {
                return 1;
            }
            if (n == 1) {
                return 2;
            }
            if (n == 2) {
                return 4;
            }
            if (n2 >= 3 && n2 <= 10) {
                return 8;
            }
            if (n2 >= 11 && n2 <= 99) {
                return 16;
            }
            return 0;
        }
    }
    
    public static class PluralRules_Balkan extends PluralRules
    {
        public int quantityForNumber(int n) {
            final int n2 = n % 100;
            n %= 10;
            if (n == 1 && n2 != 11) {
                return 2;
            }
            if (n >= 2 && n <= 4 && (n2 < 12 || n2 > 14)) {
                return 8;
            }
            if (n != 0 && (n < 5 || n > 9) && (n2 < 11 || n2 > 14)) {
                return 0;
            }
            return 16;
        }
    }
    
    public static class PluralRules_Breton extends PluralRules
    {
        public int quantityForNumber(final int n) {
            if (n == 0) {
                return 1;
            }
            if (n == 1) {
                return 2;
            }
            if (n == 2) {
                return 4;
            }
            if (n == 3) {
                return 8;
            }
            if (n == 6) {
                return 16;
            }
            return 0;
        }
    }
    
    public static class PluralRules_Czech extends PluralRules
    {
        public int quantityForNumber(final int n) {
            if (n == 1) {
                return 2;
            }
            if (n >= 2 && n <= 4) {
                return 8;
            }
            return 0;
        }
    }
    
    public static class PluralRules_French extends PluralRules
    {
        public int quantityForNumber(final int n) {
            if (n >= 0 && n < 2) {
                return 2;
            }
            return 0;
        }
    }
    
    public static class PluralRules_Langi extends PluralRules
    {
        public int quantityForNumber(final int n) {
            if (n == 0) {
                return 1;
            }
            if (n > 0 && n < 2) {
                return 2;
            }
            return 0;
        }
    }
    
    public static class PluralRules_Latvian extends PluralRules
    {
        public int quantityForNumber(final int n) {
            if (n == 0) {
                return 1;
            }
            if (n % 10 == 1 && n % 100 != 11) {
                return 2;
            }
            return 0;
        }
    }
    
    public static class PluralRules_Lithuanian extends PluralRules
    {
        public int quantityForNumber(int n) {
            final int n2 = n % 100;
            n %= 10;
            if (n == 1 && (n2 < 11 || n2 > 19)) {
                return 2;
            }
            if (n >= 2 && n <= 9 && (n2 < 11 || n2 > 19)) {
                return 8;
            }
            return 0;
        }
    }
    
    public static class PluralRules_Macedonian extends PluralRules
    {
        public int quantityForNumber(final int n) {
            if (n % 10 == 1 && n != 11) {
                return 2;
            }
            return 0;
        }
    }
    
    public static class PluralRules_Maltese extends PluralRules
    {
        public int quantityForNumber(final int n) {
            final int n2 = n % 100;
            if (n == 1) {
                return 2;
            }
            if (n == 0 || (n2 >= 2 && n2 <= 10)) {
                return 8;
            }
            if (n2 >= 11 && n2 <= 19) {
                return 16;
            }
            return 0;
        }
    }
    
    public static class PluralRules_None extends PluralRules
    {
        public int quantityForNumber(final int n) {
            return 0;
        }
    }
    
    public static class PluralRules_One extends PluralRules
    {
        public int quantityForNumber(int n) {
            if (n == 1) {
                n = 2;
            }
            else {
                n = 0;
            }
            return n;
        }
    }
    
    public static class PluralRules_Polish extends PluralRules
    {
        public int quantityForNumber(final int n) {
            final int n2 = n % 100;
            final int n3 = n % 10;
            if (n == 1) {
                return 2;
            }
            if (n3 >= 2 && n3 <= 4 && (n2 < 12 || n2 > 14)) {
                return 8;
            }
            if ((n3 >= 0 && n3 <= 1) || (n3 >= 5 && n3 <= 9) || (n2 >= 12 && n2 <= 14)) {
                return 16;
            }
            return 0;
        }
    }
    
    public static class PluralRules_Romanian extends PluralRules
    {
        public int quantityForNumber(final int n) {
            final int n2 = n % 100;
            if (n == 1) {
                return 2;
            }
            if (n != 0 && (n2 < 1 || n2 > 19)) {
                return 0;
            }
            return 8;
        }
    }
    
    public static class PluralRules_Slovenian extends PluralRules
    {
        public int quantityForNumber(int n) {
            n %= 100;
            if (n == 1) {
                return 2;
            }
            if (n == 2) {
                return 4;
            }
            if (n >= 3 && n <= 4) {
                return 8;
            }
            return 0;
        }
    }
    
    public static class PluralRules_Tachelhit extends PluralRules
    {
        public int quantityForNumber(final int n) {
            if (n >= 0 && n <= 1) {
                return 2;
            }
            if (n >= 2 && n <= 10) {
                return 8;
            }
            return 0;
        }
    }
    
    public static class PluralRules_Two extends PluralRules
    {
        public int quantityForNumber(final int n) {
            if (n == 1) {
                return 2;
            }
            if (n == 2) {
                return 4;
            }
            return 0;
        }
    }
    
    public static class PluralRules_Welsh extends PluralRules
    {
        public int quantityForNumber(final int n) {
            if (n == 0) {
                return 1;
            }
            if (n == 1) {
                return 2;
            }
            if (n == 2) {
                return 4;
            }
            if (n == 3) {
                return 8;
            }
            if (n == 6) {
                return 16;
            }
            return 0;
        }
    }
    
    public static class PluralRules_Zero extends PluralRules
    {
        public int quantityForNumber(final int n) {
            if (n != 0 && n != 1) {
                return 0;
            }
            return 2;
        }
    }
    
    private class TimeZoneChangedReceiver extends BroadcastReceiver
    {
        public void onReceive(final Context context, final Intent intent) {
            ApplicationLoader.applicationHandler.post((Runnable)new _$$Lambda$LocaleController$TimeZoneChangedReceiver$_tF936vTwTBeR7FivZoJHGJKUvY(this));
        }
    }
}

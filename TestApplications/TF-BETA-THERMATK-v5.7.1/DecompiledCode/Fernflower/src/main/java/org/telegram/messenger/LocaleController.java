package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.text.format.DateFormat;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Map.Entry;
import org.telegram.messenger.time.FastDateFormat;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

public class LocaleController {
   private static volatile LocaleController Instance;
   static final int QUANTITY_FEW = 8;
   static final int QUANTITY_MANY = 16;
   static final int QUANTITY_ONE = 2;
   static final int QUANTITY_OTHER = 0;
   static final int QUANTITY_TWO = 4;
   static final int QUANTITY_ZERO = 1;
   public static boolean is24HourFormat;
   public static boolean isRTL;
   public static int nameDisplayOrder;
   private HashMap allRules = new HashMap();
   private boolean changingConfiguration;
   public FastDateFormat chatDate;
   public FastDateFormat chatFullDate;
   private HashMap currencyValues;
   private Locale currentLocale;
   private LocaleController.LocaleInfo currentLocaleInfo;
   private LocaleController.PluralRules currentPluralRules;
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
   public ArrayList languages;
   public HashMap languagesDict;
   private boolean loadingRemoteLanguages;
   private HashMap localeValues = new HashMap();
   private ArrayList otherLanguages;
   private boolean reloadLastFile;
   public ArrayList remoteLanguages;
   public HashMap remoteLanguagesDict;
   private HashMap ruTranslitChars;
   private Locale systemDefaultLocale;
   private HashMap translitChars;
   public ArrayList unofficialLanguages;

   public LocaleController() {
      boolean var1 = false;
      this.changingConfiguration = false;
      this.languages = new ArrayList();
      this.unofficialLanguages = new ArrayList();
      this.remoteLanguages = new ArrayList();
      this.remoteLanguagesDict = new HashMap();
      this.languagesDict = new HashMap();
      this.otherLanguages = new ArrayList();
      LocaleController.PluralRules_One var2 = new LocaleController.PluralRules_One();
      this.addRules(new String[]{"bem", "brx", "da", "de", "el", "en", "eo", "es", "et", "fi", "fo", "gl", "he", "iw", "it", "nb", "nl", "nn", "no", "sv", "af", "bg", "bn", "ca", "eu", "fur", "fy", "gu", "ha", "is", "ku", "lb", "ml", "mr", "nah", "ne", "om", "or", "pa", "pap", "ps", "so", "sq", "sw", "ta", "te", "tk", "ur", "zu", "mn", "gsw", "chr", "rm", "pt", "an", "ast"}, var2);
      LocaleController.PluralRules_Czech var13 = new LocaleController.PluralRules_Czech();
      this.addRules(new String[]{"cs", "sk"}, var13);
      LocaleController.PluralRules_French var14 = new LocaleController.PluralRules_French();
      this.addRules(new String[]{"ff", "fr", "kab"}, var14);
      LocaleController.PluralRules_Balkan var15 = new LocaleController.PluralRules_Balkan();
      this.addRules(new String[]{"hr", "ru", "sr", "uk", "be", "bs", "sh"}, var15);
      LocaleController.PluralRules_Latvian var16 = new LocaleController.PluralRules_Latvian();
      this.addRules(new String[]{"lv"}, var16);
      LocaleController.PluralRules_Lithuanian var17 = new LocaleController.PluralRules_Lithuanian();
      this.addRules(new String[]{"lt"}, var17);
      LocaleController.PluralRules_Polish var18 = new LocaleController.PluralRules_Polish();
      this.addRules(new String[]{"pl"}, var18);
      LocaleController.PluralRules_Romanian var19 = new LocaleController.PluralRules_Romanian();
      this.addRules(new String[]{"ro", "mo"}, var19);
      LocaleController.PluralRules_Slovenian var20 = new LocaleController.PluralRules_Slovenian();
      this.addRules(new String[]{"sl"}, var20);
      LocaleController.PluralRules_Arabic var21 = new LocaleController.PluralRules_Arabic();
      this.addRules(new String[]{"ar"}, var21);
      LocaleController.PluralRules_Macedonian var22 = new LocaleController.PluralRules_Macedonian();
      this.addRules(new String[]{"mk"}, var22);
      LocaleController.PluralRules_Welsh var23 = new LocaleController.PluralRules_Welsh();
      this.addRules(new String[]{"cy"}, var23);
      LocaleController.PluralRules_Breton var24 = new LocaleController.PluralRules_Breton();
      this.addRules(new String[]{"br"}, var24);
      LocaleController.PluralRules_Langi var25 = new LocaleController.PluralRules_Langi();
      this.addRules(new String[]{"lag"}, var25);
      LocaleController.PluralRules_Tachelhit var26 = new LocaleController.PluralRules_Tachelhit();
      this.addRules(new String[]{"shi"}, var26);
      LocaleController.PluralRules_Maltese var28 = new LocaleController.PluralRules_Maltese();
      this.addRules(new String[]{"mt"}, var28);
      LocaleController.PluralRules_Two var29 = new LocaleController.PluralRules_Two();
      this.addRules(new String[]{"ga", "se", "sma", "smi", "smj", "smn", "sms"}, var29);
      LocaleController.PluralRules_Zero var30 = new LocaleController.PluralRules_Zero();
      this.addRules(new String[]{"ak", "am", "bh", "fil", "tl", "guw", "hi", "ln", "mg", "nso", "ti", "wa"}, var30);
      LocaleController.PluralRules_None var31 = new LocaleController.PluralRules_None();
      this.addRules(new String[]{"az", "bm", "fa", "ig", "hu", "ja", "kde", "kea", "ko", "my", "ses", "sg", "to", "tr", "vi", "wo", "yo", "zh", "bo", "dz", "id", "jv", "jw", "ka", "km", "kn", "ms", "th", "in"}, var31);
      LocaleController.LocaleInfo var32 = new LocaleController.LocaleInfo();
      var32.name = "English";
      var32.nameEnglish = "English";
      var32.pluralLangCode = "en";
      var32.shortName = "en";
      var32.pathToFile = null;
      var32.builtIn = true;
      this.languages.add(var32);
      this.languagesDict.put(var32.shortName, var32);
      var32 = new LocaleController.LocaleInfo();
      var32.name = "Italiano";
      var32.nameEnglish = "Italian";
      var32.pluralLangCode = "it";
      var32.shortName = "it";
      var32.pathToFile = null;
      var32.builtIn = true;
      this.languages.add(var32);
      this.languagesDict.put(var32.shortName, var32);
      var32 = new LocaleController.LocaleInfo();
      var32.name = "Español";
      var32.nameEnglish = "Spanish";
      var32.pluralLangCode = "es";
      var32.shortName = "es";
      var32.builtIn = true;
      this.languages.add(var32);
      this.languagesDict.put(var32.shortName, var32);
      var32 = new LocaleController.LocaleInfo();
      var32.name = "Deutsch";
      var32.nameEnglish = "German";
      var32.pluralLangCode = "de";
      var32.shortName = "de";
      var32.pathToFile = null;
      var32.builtIn = true;
      this.languages.add(var32);
      this.languagesDict.put(var32.shortName, var32);
      var32 = new LocaleController.LocaleInfo();
      var32.name = "Nederlands";
      var32.nameEnglish = "Dutch";
      var32.pluralLangCode = "nl";
      var32.shortName = "nl";
      var32.pathToFile = null;
      var32.builtIn = true;
      this.languages.add(var32);
      this.languagesDict.put(var32.shortName, var32);
      var32 = new LocaleController.LocaleInfo();
      var32.name = "العربية";
      var32.nameEnglish = "Arabic";
      var32.pluralLangCode = "ar";
      var32.shortName = "ar";
      var32.pathToFile = null;
      var32.builtIn = true;
      var32.isRtl = true;
      this.languages.add(var32);
      this.languagesDict.put(var32.shortName, var32);
      var32 = new LocaleController.LocaleInfo();
      var32.name = "Português (Brasil)";
      var32.nameEnglish = "Portuguese (Brazil)";
      var32.pluralLangCode = "pt_br";
      var32.shortName = "pt_br";
      var32.pathToFile = null;
      var32.builtIn = true;
      this.languages.add(var32);
      this.languagesDict.put(var32.shortName, var32);
      var32 = new LocaleController.LocaleInfo();
      var32.name = "한국어";
      var32.nameEnglish = "Korean";
      var32.pluralLangCode = "ko";
      var32.shortName = "ko";
      var32.pathToFile = null;
      var32.builtIn = true;
      this.languages.add(var32);
      this.languagesDict.put(var32.shortName, var32);
      this.loadOtherLanguages();
      if (this.remoteLanguages.isEmpty()) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$LocaleController$NNZIDoHieDUSrd9BgBq36GRonDE(this));
      }

      int var3;
      for(var3 = 0; var3 < this.otherLanguages.size(); ++var3) {
         var32 = (LocaleController.LocaleInfo)this.otherLanguages.get(var3);
         this.languages.add(var32);
         this.languagesDict.put(var32.getKey(), var32);
      }

      LocaleController.LocaleInfo var4;
      for(var3 = 0; var3 < this.remoteLanguages.size(); ++var3) {
         var4 = (LocaleController.LocaleInfo)this.remoteLanguages.get(var3);
         var32 = this.getLanguageFromDict(var4.getKey());
         if (var32 != null) {
            var32.pathToFile = var4.pathToFile;
            var32.version = var4.version;
            var32.baseVersion = var4.baseVersion;
            var32.serverIndex = var4.serverIndex;
            this.remoteLanguages.set(var3, var32);
         } else {
            this.languages.add(var4);
            this.languagesDict.put(var4.getKey(), var4);
         }
      }

      for(var3 = 0; var3 < this.unofficialLanguages.size(); ++var3) {
         var4 = (LocaleController.LocaleInfo)this.unofficialLanguages.get(var3);
         var32 = this.getLanguageFromDict(var4.getKey());
         if (var32 != null) {
            var32.pathToFile = var4.pathToFile;
            var32.version = var4.version;
            var32.baseVersion = var4.baseVersion;
            var32.serverIndex = var4.serverIndex;
            this.unofficialLanguages.set(var3, var32);
         } else {
            this.languagesDict.put(var4.getKey(), var4);
         }
      }

      this.systemDefaultLocale = Locale.getDefault();
      is24HourFormat = DateFormat.is24HourFormat(ApplicationLoader.applicationContext);

      label90: {
         Exception var10000;
         label114: {
            boolean var10001;
            String var33;
            try {
               var33 = MessagesController.getGlobalMainSettings().getString("language", (String)null);
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
               break label114;
            }

            if (var33 != null) {
               try {
                  var4 = this.getLanguageFromDict(var33);
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label114;
               }

               var32 = var4;
               if (var4 != null) {
                  var1 = true;
                  var32 = var4;
               }
            } else {
               var32 = null;
            }

            var4 = var32;
            if (var32 == null) {
               var4 = var32;

               try {
                  if (this.systemDefaultLocale.getLanguage() != null) {
                     var4 = this.getLanguageFromDict(this.systemDefaultLocale.getLanguage());
                  }
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label114;
               }
            }

            var32 = var4;
            if (var4 == null) {
               try {
                  var4 = this.getLanguageFromDict(this.getLocaleString(this.systemDefaultLocale));
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label114;
               }

               var32 = var4;
               if (var4 == null) {
                  try {
                     var32 = this.getLanguageFromDict("en");
                  } catch (Exception var8) {
                     var10000 = var8;
                     var10001 = false;
                     break label114;
                  }
               }
            }

            try {
               this.applyLanguage(var32, var1, true, UserConfig.selectedAccount);
               break label90;
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
            }
         }

         Exception var34 = var10000;
         FileLog.e((Throwable)var34);
      }

      try {
         IntentFilter var5 = new IntentFilter("android.intent.action.TIMEZONE_CHANGED");
         Context var35 = ApplicationLoader.applicationContext;
         LocaleController.TimeZoneChangedReceiver var27 = new LocaleController.TimeZoneChangedReceiver();
         var35.registerReceiver(var27, var5);
      } catch (Exception var6) {
         FileLog.e((Throwable)var6);
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$LocaleController$U712xrnt9cCu1iJYrRj7Tc_xRYo(this));
   }

   public static String addNbsp(String var0) {
      return var0.replace(' ', ' ');
   }

   private void addRules(String[] var1, LocaleController.PluralRules var2) {
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var1[var4];
         this.allRules.put(var5, var2);
      }

   }

   private void applyRemoteLanguage(LocaleController.LocaleInfo var1, String var2, boolean var3, int var4) {
      if (var1 != null && (var1 == null || var1.isRemote() || var1.isUnofficial())) {
         if (var1.hasBaseLang() && (var2 == null || var2.equals(var1.baseLangCode))) {
            if (var1.baseVersion != 0 && !var3) {
               if (var1.hasBaseLang()) {
                  TLRPC.TL_langpack_getDifference var9 = new TLRPC.TL_langpack_getDifference();
                  var9.from_version = var1.baseVersion;
                  var9.lang_code = var1.getBaseLangCode();
                  var9.lang_pack = "";
                  ConnectionsManager.getInstance(var4).sendRequest(var9, new _$$Lambda$LocaleController$s_X_ikLryDU_N2xVuPEFJJqkOk0(this, var1, var4), 8);
               }
            } else {
               TLRPC.TL_langpack_getLangPack var5 = new TLRPC.TL_langpack_getLangPack();
               var5.lang_code = var1.getBaseLangCode();
               ConnectionsManager.getInstance(var4).sendRequest(var5, new _$$Lambda$LocaleController$C7GeSsDILXp_14TJUT5eNNANB6o(this, var1, var4), 8);
            }
         }

         if (var2 == null || var2.equals(var1.shortName)) {
            if (var1.version != 0 && !var3) {
               TLRPC.TL_langpack_getDifference var8 = new TLRPC.TL_langpack_getDifference();
               var8.from_version = var1.version;
               var8.lang_code = var1.getLangCode();
               var8.lang_pack = "";
               ConnectionsManager.getInstance(var4).sendRequest(var8, new _$$Lambda$LocaleController$JT38tfE7_oOUNrZDivXXSv_b_vg(this, var1, var4), 8);
            } else {
               for(int var6 = 0; var6 < 3; ++var6) {
                  ConnectionsManager.setLangCode(var1.getLangCode());
               }

               TLRPC.TL_langpack_getLangPack var7 = new TLRPC.TL_langpack_getLangPack();
               var7.lang_code = var1.getLangCode();
               ConnectionsManager.getInstance(var4).sendRequest(var7, new _$$Lambda$LocaleController$h0eSozyCLW0Mpog1omLBJ_00K_I(this, var1, var4), 8);
            }
         }
      }

   }

   private FastDateFormat createFormatter(Locale var1, String var2, String var3) {
      String var4;
      label20: {
         if (var2 != null) {
            var4 = var2;
            if (var2.length() != 0) {
               break label20;
            }
         }

         var4 = var3;
      }

      FastDateFormat var6;
      FastDateFormat var7;
      try {
         var7 = FastDateFormat.getInstance(var4, var1);
      } catch (Exception var5) {
         var6 = FastDateFormat.getInstance(var3, var1);
         return var6;
      }

      var6 = var7;
      return var6;
   }

   private String escapeString(String var1) {
      return var1.contains("[CDATA") ? var1 : var1.replace("<", "&lt;").replace(">", "&gt;").replace("& ", "&amp; ");
   }

   public static String formatCallDuration(int var0) {
      if (var0 > 3600) {
         String var1 = formatPluralString("Hours", var0 / 3600);
         var0 = var0 % 3600 / 60;
         String var2 = var1;
         if (var0 > 0) {
            StringBuilder var3 = new StringBuilder();
            var3.append(var1);
            var3.append(", ");
            var3.append(formatPluralString("Minutes", var0));
            var2 = var3.toString();
         }

         return var2;
      } else {
         return var0 > 60 ? formatPluralString("Minutes", var0 / 60) : formatPluralString("Seconds", var0);
      }
   }

   public static String formatDate(long var0) {
      var0 *= 1000L;

      Exception var10000;
      label53: {
         int var3;
         int var4;
         int var5;
         int var6;
         boolean var10001;
         try {
            Calendar var2 = Calendar.getInstance();
            var3 = var2.get(6);
            var4 = var2.get(1);
            var2.setTimeInMillis(var0);
            var5 = var2.get(6);
            var6 = var2.get(1);
         } catch (Exception var11) {
            var10000 = var11;
            var10001 = false;
            break label53;
         }

         FastDateFormat var7;
         Date var13;
         if (var5 == var3 && var4 == var6) {
            try {
               var7 = getInstance().formatterDay;
               var13 = new Date(var0);
               return var7.format(var13);
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
            }
         } else if (var5 + 1 == var3 && var4 == var6) {
            try {
               return getString("Yesterday", 2131561134);
            } catch (Exception var9) {
               var10000 = var9;
               var10001 = false;
            }
         } else {
            label55: {
               try {
                  if (Math.abs(System.currentTimeMillis() - var0) < 31536000000L) {
                     FastDateFormat var15 = getInstance().formatterDayMonth;
                     Date var17 = new Date(var0);
                     return var15.format(var17);
                  }
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label55;
               }

               try {
                  var7 = getInstance().formatterYear;
                  var13 = new Date(var0);
                  String var14 = var7.format(var13);
                  return var14;
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
               }
            }
         }
      }

      Exception var16 = var10000;
      FileLog.e((Throwable)var16);
      return "LOC_ERR: formatDate";
   }

   public static String formatDateAudio(long var0) {
      long var2 = var0 * 1000L;

      Exception var10000;
      label58: {
         int var5;
         int var6;
         int var7;
         int var8;
         boolean var10001;
         try {
            Calendar var4 = Calendar.getInstance();
            var5 = var4.get(6);
            var6 = var4.get(1);
            var4.setTimeInMillis(var2);
            var7 = var4.get(6);
            var8 = var4.get(1);
         } catch (Exception var16) {
            var10000 = var16;
            var10001 = false;
            break label58;
         }

         FastDateFormat var9;
         Date var17;
         if (var7 == var5 && var6 == var8) {
            try {
               var9 = getInstance().formatterDay;
               var17 = new Date(var2);
               return formatString("TodayAtFormatted", 2131560908, var9.format(var17));
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
            }
         } else {
            FastDateFormat var18;
            Date var22;
            if (var7 + 1 == var5 && var6 == var8) {
               try {
                  var18 = getInstance().formatterDay;
                  var22 = new Date(var2);
                  return formatString("YesterdayAtFormatted", 2131561136, var18.format(var22));
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
               }
            } else {
               label60: {
                  try {
                     var0 = Math.abs(System.currentTimeMillis() - var2);
                  } catch (Exception var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label60;
                  }

                  if (var0 < 31536000000L) {
                     try {
                        var9 = getInstance().formatterDayMonth;
                        var17 = new Date(var2);
                        String var21 = var9.format(var17);
                        FastDateFormat var10 = getInstance().formatterDay;
                        var17 = new Date(var2);
                        return formatString("formatDateAtTime", 2131561210, var21, var10.format(var17));
                     } catch (Exception var13) {
                        var10000 = var13;
                        var10001 = false;
                     }
                  } else {
                     try {
                        var18 = getInstance().formatterYear;
                        var22 = new Date(var2);
                        String var23 = var18.format(var22);
                        var18 = getInstance().formatterDay;
                        var22 = new Date(var2);
                        String var19 = formatString("formatDateAtTime", 2131561210, var23, var18.format(var22));
                        return var19;
                     } catch (Exception var14) {
                        var10000 = var14;
                        var10001 = false;
                     }
                  }
               }
            }
         }
      }

      Exception var20 = var10000;
      FileLog.e((Throwable)var20);
      return "LOC_ERR";
   }

   public static String formatDateCallLog(long var0) {
      var0 *= 1000L;

      Exception var10000;
      label58: {
         int var3;
         int var4;
         int var5;
         int var6;
         boolean var10001;
         try {
            Calendar var2 = Calendar.getInstance();
            var3 = var2.get(6);
            var4 = var2.get(1);
            var2.setTimeInMillis(var0);
            var5 = var2.get(6);
            var6 = var2.get(1);
         } catch (Exception var16) {
            var10000 = var16;
            var10001 = false;
            break label58;
         }

         if (var5 == var3 && var4 == var6) {
            try {
               FastDateFormat var22 = getInstance().formatterDay;
               Date var20 = new Date(var0);
               return var22.format(var20);
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
            }
         } else {
            Date var7;
            FastDateFormat var17;
            if (var5 + 1 == var3 && var4 == var6) {
               try {
                  var17 = getInstance().formatterDay;
                  var7 = new Date(var0);
                  return formatString("YesterdayAtFormatted", 2131561136, var17.format(var7));
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
               }
            } else {
               label60: {
                  long var8;
                  try {
                     var8 = Math.abs(System.currentTimeMillis() - var0);
                  } catch (Exception var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label60;
                  }

                  String var18;
                  if (var8 < 31536000000L) {
                     try {
                        var17 = getInstance().chatDate;
                        var7 = new Date(var0);
                        var18 = var17.format(var7);
                        FastDateFormat var10 = getInstance().formatterDay;
                        var7 = new Date(var0);
                        return formatString("formatDateAtTime", 2131561210, var18, var10.format(var7));
                     } catch (Exception var13) {
                        var10000 = var13;
                        var10001 = false;
                     }
                  } else {
                     try {
                        var17 = getInstance().chatFullDate;
                        var7 = new Date(var0);
                        String var21 = var17.format(var7);
                        var17 = getInstance().formatterDay;
                        Date var23 = new Date(var0);
                        var18 = formatString("formatDateAtTime", 2131561210, var21, var17.format(var23));
                        return var18;
                     } catch (Exception var14) {
                        var10000 = var14;
                        var10001 = false;
                     }
                  }
               }
            }
         }
      }

      Exception var19 = var10000;
      FileLog.e((Throwable)var19);
      return "LOC_ERR";
   }

   public static String formatDateChat(long param0) {
      // $FF: Couldn't be decompiled
   }

   public static String formatDateForBan(long var0) {
      var0 *= 1000L;

      try {
         Calendar var2 = Calendar.getInstance();
         int var3 = var2.get(1);
         var2.setTimeInMillis(var0);
         Date var4;
         FastDateFormat var6;
         if (var3 == var2.get(1)) {
            var6 = getInstance().formatterBannedUntilThisYear;
            var4 = new Date(var0);
            return var6.format(var4);
         } else {
            var6 = getInstance().formatterBannedUntil;
            var4 = new Date(var0);
            String var7 = var6.format(var4);
            return var7;
         }
      } catch (Exception var5) {
         FileLog.e((Throwable)var5);
         return "LOC_ERR";
      }
   }

   public static String formatDateOnline(long var0) {
      long var2 = var0 * 1000L;

      Exception var10000;
      label58: {
         int var5;
         int var6;
         int var7;
         int var8;
         boolean var10001;
         try {
            Calendar var4 = Calendar.getInstance();
            var5 = var4.get(6);
            var6 = var4.get(1);
            var4.setTimeInMillis(var2);
            var7 = var4.get(6);
            var8 = var4.get(1);
         } catch (Exception var16) {
            var10000 = var16;
            var10001 = false;
            break label58;
         }

         FastDateFormat var9;
         Date var17;
         if (var7 == var5 && var6 == var8) {
            try {
               var9 = getInstance().formatterDay;
               var17 = new Date(var2);
               return formatString("LastSeenFormatted", 2131559738, formatString("TodayAtFormatted", 2131560908, var9.format(var17)));
            } catch (Exception var11) {
               var10000 = var11;
               var10001 = false;
            }
         } else if (var7 + 1 == var5 && var6 == var8) {
            try {
               var9 = getInstance().formatterDay;
               var17 = new Date(var2);
               return formatString("LastSeenFormatted", 2131559738, formatString("YesterdayAtFormatted", 2131561136, var9.format(var17)));
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
            }
         } else {
            label60: {
               try {
                  var0 = Math.abs(System.currentTimeMillis() - var2);
               } catch (Exception var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label60;
               }

               FastDateFormat var18;
               if (var0 < 31536000000L) {
                  try {
                     var9 = getInstance().formatterDayMonth;
                     var17 = new Date(var2);
                     String var21 = var9.format(var17);
                     var18 = getInstance().formatterDay;
                     Date var10 = new Date(var2);
                     return formatString("LastSeenDateFormatted", 2131559735, formatString("formatDateAtTime", 2131561210, var21, var18.format(var10)));
                  } catch (Exception var13) {
                     var10000 = var13;
                     var10001 = false;
                  }
               } else {
                  try {
                     var9 = getInstance().formatterYear;
                     var17 = new Date(var2);
                     String var23 = var9.format(var17);
                     var18 = getInstance().formatterDay;
                     Date var22 = new Date(var2);
                     String var19 = formatString("LastSeenDateFormatted", 2131559735, formatString("formatDateAtTime", 2131561210, var23, var18.format(var22)));
                     return var19;
                  } catch (Exception var14) {
                     var10000 = var14;
                     var10001 = false;
                  }
               }
            }
         }
      }

      Exception var20 = var10000;
      FileLog.e((Throwable)var20);
      return "LOC_ERR";
   }

   public static String formatLocationLeftTime(int var0) {
      int var1 = var0 / 60 / 60;
      var0 -= var1 * 60 * 60;
      int var2 = var0 / 60;
      int var3 = var0 - var2 * 60;
      byte var4 = 1;
      byte var6 = 1;
      String var5;
      if (var1 != 0) {
         if (var2 <= 30) {
            var6 = 0;
         }

         var5 = String.format("%dh", var1 + var6);
      } else if (var2 != 0) {
         if (var3 > 30) {
            var6 = var4;
         } else {
            var6 = 0;
         }

         var5 = String.format("%d", var2 + var6);
      } else {
         var5 = String.format("%d", var3);
      }

      return var5;
   }

   public static String formatLocationUpdateDate(long var0) {
      long var2 = var0 * 1000L;

      Exception var10000;
      label78: {
         int var5;
         int var6;
         int var7;
         int var8;
         boolean var10001;
         try {
            Calendar var4 = Calendar.getInstance();
            var5 = var4.get(6);
            var6 = var4.get(1);
            var4.setTimeInMillis(var2);
            var7 = var4.get(6);
            var8 = var4.get(1);
         } catch (Exception var19) {
            var10000 = var19;
            var10001 = false;
            break label78;
         }

         Date var9;
         FastDateFormat var20;
         if (var7 == var5 && var6 == var8) {
            label53: {
               try {
                  var5 = (int)((long)ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime() - var2 / 1000L) / 60;
               } catch (Exception var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label53;
               }

               if (var5 < 1) {
                  try {
                     return getString("LocationUpdatedJustNow", 2131559782);
                  } catch (Exception var11) {
                     var10000 = var11;
                     var10001 = false;
                  }
               } else if (var5 < 60) {
                  try {
                     return formatPluralString("UpdatedMinutes", var5);
                  } catch (Exception var12) {
                     var10000 = var12;
                     var10001 = false;
                  }
               } else {
                  try {
                     var20 = getInstance().formatterDay;
                     var9 = new Date(var2);
                     return formatString("LocationUpdatedFormatted", 2131559781, formatString("TodayAtFormatted", 2131560908, var20.format(var9)));
                  } catch (Exception var13) {
                     var10000 = var13;
                     var10001 = false;
                  }
               }
            }
         } else if (var7 + 1 == var5 && var6 == var8) {
            try {
               var20 = getInstance().formatterDay;
               var9 = new Date(var2);
               return formatString("LocationUpdatedFormatted", 2131559781, formatString("YesterdayAtFormatted", 2131561136, var20.format(var9)));
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
            }
         } else {
            label80: {
               try {
                  var0 = Math.abs(System.currentTimeMillis() - var2);
               } catch (Exception var18) {
                  var10000 = var18;
                  var10001 = false;
                  break label80;
               }

               Date var10;
               if (var0 < 31536000000L) {
                  try {
                     var20 = getInstance().formatterDayMonth;
                     var9 = new Date(var2);
                     String var23 = var20.format(var9);
                     var20 = getInstance().formatterDay;
                     var10 = new Date(var2);
                     return formatString("LocationUpdatedFormatted", 2131559781, formatString("formatDateAtTime", 2131561210, var23, var20.format(var10)));
                  } catch (Exception var16) {
                     var10000 = var16;
                     var10001 = false;
                  }
               } else {
                  try {
                     var20 = getInstance().formatterYear;
                     var9 = new Date(var2);
                     String var21 = var20.format(var9);
                     FastDateFormat var24 = getInstance().formatterDay;
                     var10 = new Date(var2);
                     var21 = formatString("LocationUpdatedFormatted", 2131559781, formatString("formatDateAtTime", 2131561210, var21, var24.format(var10)));
                     return var21;
                  } catch (Exception var17) {
                     var10000 = var17;
                     var10001 = false;
                  }
               }
            }
         }
      }

      Exception var22 = var10000;
      FileLog.e((Throwable)var22);
      return "LOC_ERR";
   }

   public static String formatPluralString(String var0, int var1) {
      if (var0 != null && var0.length() != 0 && getInstance().currentPluralRules != null) {
         String var4 = getInstance().stringForQuantity(getInstance().currentPluralRules.quantityForNumber(var1));
         StringBuilder var3 = new StringBuilder();
         var3.append(var0);
         var3.append("_");
         var3.append(var4);
         var0 = var3.toString();
         return formatString(var0, ApplicationLoader.applicationContext.getResources().getIdentifier(var0, "string", ApplicationLoader.applicationContext.getPackageName()), var1);
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("LOC_ERR:");
         var2.append(var0);
         return var2.toString();
      }
   }

   public static String formatSectionDate(long param0) {
      // $FF: Couldn't be decompiled
   }

   public static String formatShortNumber(int var0, int[] var1) {
      StringBuilder var2 = new StringBuilder();
      byte var3 = 0;
      int var4 = var0;
      var0 = var3;

      while(true) {
         int var9 = var4 / 1000;
         if (var9 <= 0) {
            if (var1 != null) {
               double var5 = (double)var4;
               double var7 = (double)var0;
               Double.isNaN(var7);
               var7 /= 10.0D;
               Double.isNaN(var5);
               var5 += var7;

               for(var9 = 0; var9 < var2.length(); ++var9) {
                  var5 *= 1000.0D;
               }

               var1[0] = (int)var5;
            }

            if (var0 != 0 && var2.length() > 0) {
               return var2.length() == 2 ? String.format(Locale.US, "%d.%dM", var4, var0) : String.format(Locale.US, "%d.%d%s", var4, var0, var2.toString());
            } else {
               return var2.length() == 2 ? String.format(Locale.US, "%dM", var4) : String.format(Locale.US, "%d%s", var4, var2.toString());
            }
         }

         var2.append("K");
         var0 = var4 % 1000 / 100;
         var4 = var9;
      }
   }

   public static String formatString(String var0, int var1, Object... var2) {
      Exception var10000;
      label44: {
         String var3;
         boolean var10001;
         label41: {
            try {
               if (BuildVars.USE_CLOUD_STRINGS) {
                  var3 = (String)getInstance().localeValues.get(var0);
                  break label41;
               }
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label44;
            }

            var3 = null;
         }

         String var4 = var3;
         if (var3 == null) {
            try {
               var4 = ApplicationLoader.applicationContext.getString(var1);
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
               break label44;
            }
         }

         try {
            if (getInstance().currentLocale != null) {
               return String.format(getInstance().currentLocale, var4, var2);
            }
         } catch (Exception var7) {
            var10000 = var7;
            var10001 = false;
            break label44;
         }

         try {
            String var11 = String.format(var4, var2);
            return var11;
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
         }
      }

      Exception var9 = var10000;
      FileLog.e((Throwable)var9);
      StringBuilder var10 = new StringBuilder();
      var10.append("LOC_ERR: ");
      var10.append(var0);
      return var10.toString();
   }

   public static String formatStringSimple(String var0, Object... var1) {
      try {
         if (getInstance().currentLocale != null) {
            return String.format(getInstance().currentLocale, var0, var1);
         } else {
            String var4 = String.format(var0, var1);
            return var4;
         }
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
         StringBuilder var3 = new StringBuilder();
         var3.append("LOC_ERR: ");
         var3.append(var0);
         return var3.toString();
      }
   }

   public static String formatTTLString(int var0) {
      if (var0 < 60) {
         return formatPluralString("Seconds", var0);
      } else if (var0 < 3600) {
         return formatPluralString("Minutes", var0 / 60);
      } else if (var0 < 86400) {
         return formatPluralString("Hours", var0 / 60 / 60);
      } else if (var0 < 604800) {
         return formatPluralString("Days", var0 / 60 / 60 / 24);
      } else {
         int var1 = var0 / 60 / 60 / 24;
         return var0 % 7 == 0 ? formatPluralString("Weeks", var1 / 7) : String.format("%s %s", formatPluralString("Weeks", var1 / 7), formatPluralString("Days", var1 % 7));
      }
   }

   public static String formatUserStatus(int var0, TLRPC.User var1) {
      return formatUserStatus(var0, var1, (boolean[])null);
   }

   public static String formatUserStatus(int var0, TLRPC.User var1, boolean[] var2) {
      TLRPC.UserStatus var3;
      if (var1 != null) {
         var3 = var1.status;
         if (var3 != null && var3.expires == 0) {
            if (var3 instanceof TLRPC.TL_userStatusRecently) {
               var3.expires = -100;
            } else if (var3 instanceof TLRPC.TL_userStatusLastWeek) {
               var3.expires = -101;
            } else if (var3 instanceof TLRPC.TL_userStatusLastMonth) {
               var3.expires = -102;
            }
         }
      }

      if (var1 != null) {
         var3 = var1.status;
         if (var3 != null && var3.expires <= 0 && MessagesController.getInstance(var0).onlinePrivacy.containsKey(var1.id)) {
            if (var2 != null) {
               var2[0] = true;
            }

            return getString("Online", 2131560100);
         }
      }

      if (var1 != null) {
         var3 = var1.status;
         if (var3 != null && var3.expires != 0 && !UserObject.isDeleted(var1) && !(var1 instanceof TLRPC.TL_userEmpty)) {
            int var4 = ConnectionsManager.getInstance(var0).getCurrentTime();
            var0 = var1.status.expires;
            if (var0 > var4) {
               if (var2 != null) {
                  var2[0] = true;
               }

               return getString("Online", 2131560100);
            }

            if (var0 == -1) {
               return getString("Invisible", 2131559675);
            }

            if (var0 == -100) {
               return getString("Lately", 2131559742);
            }

            if (var0 == -101) {
               return getString("WithinAWeek", 2131561124);
            }

            if (var0 == -102) {
               return getString("WithinAMonth", 2131561123);
            }

            return formatDateOnline((long)var0);
         }
      }

      return getString("ALongTimeAgo", 2131558400);
   }

   public static String getCurrentLanguageName() {
      LocaleController.LocaleInfo var0 = getInstance().currentLocaleInfo;
      String var1;
      if (var0 != null && !TextUtils.isEmpty(var0.name)) {
         var1 = var0.name;
      } else {
         var1 = getString("LanguageName", 2131559720);
      }

      return var1;
   }

   public static LocaleController getInstance() {
      LocaleController var0 = Instance;
      LocaleController var1 = var0;
      if (var0 == null) {
         synchronized(LocaleController.class){}

         Throwable var10000;
         boolean var10001;
         label206: {
            try {
               var0 = Instance;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label206;
            }

            var1 = var0;
            if (var0 == null) {
               try {
                  var1 = new LocaleController();
                  Instance = var1;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label206;
               }
            }

            label193:
            try {
               return var1;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               break label193;
            }
         }

         while(true) {
            Throwable var22 = var10000;

            try {
               throw var22;
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               continue;
            }
         }
      } else {
         return var1;
      }
   }

   public static String getLocaleAlias(String var0) {
      if (var0 == null) {
         return null;
      } else {
         byte var2;
         label85: {
            int var1 = var0.hashCode();
            if (var1 != 3325) {
               if (var1 != 3355) {
                  if (var1 != 3365) {
                     if (var1 != 3374) {
                        if (var1 != 3391) {
                           if (var1 != 3508) {
                              if (var1 != 3521) {
                                 if (var1 != 3704) {
                                    if (var1 != 3856) {
                                       if (var1 != 101385) {
                                          if (var1 != 3404) {
                                             if (var1 == 3405 && var0.equals("jw")) {
                                                var2 = 2;
                                                break label85;
                                             }
                                          } else if (var0.equals("jv")) {
                                             var2 = 8;
                                             break label85;
                                          }
                                       } else if (var0.equals("fil")) {
                                          var2 = 10;
                                          break label85;
                                       }
                                    } else if (var0.equals("yi")) {
                                       var2 = 11;
                                       break label85;
                                    }
                                 } else if (var0.equals("tl")) {
                                    var2 = 4;
                                    break label85;
                                 }
                              } else if (var0.equals("no")) {
                                 var2 = 3;
                                 break label85;
                              }
                           } else if (var0.equals("nb")) {
                              var2 = 9;
                              break label85;
                           }
                        } else if (var0.equals("ji")) {
                           var2 = 5;
                           break label85;
                        }
                     } else if (var0.equals("iw")) {
                        var2 = 1;
                        break label85;
                     }
                  } else if (var0.equals("in")) {
                     var2 = 0;
                     break label85;
                  }
               } else if (var0.equals("id")) {
                  var2 = 6;
                  break label85;
               }
            } else if (var0.equals("he")) {
               var2 = 7;
               break label85;
            }

            var2 = -1;
         }

         switch(var2) {
         case 0:
            return "id";
         case 1:
            return "he";
         case 2:
            return "jv";
         case 3:
            return "nb";
         case 4:
            return "fil";
         case 5:
            return "yi";
         case 6:
            return "in";
         case 7:
            return "iw";
         case 8:
            return "jw";
         case 9:
            return "no";
         case 10:
            return "tl";
         case 11:
            return "ji";
         default:
            return null;
         }
      }
   }

   private HashMap getLocaleFileStrings(File var1) {
      return this.getLocaleFileStrings(var1, false);
   }

   private HashMap getLocaleFileStrings(File param1, boolean param2) {
      // $FF: Couldn't be decompiled
   }

   private String getLocaleString(Locale var1) {
      if (var1 == null) {
         return "en";
      } else {
         String var2 = var1.getLanguage();
         String var3 = var1.getCountry();
         String var5 = var1.getVariant();
         if (var2.length() == 0 && var3.length() == 0) {
            return "en";
         } else {
            StringBuilder var4 = new StringBuilder(11);
            var4.append(var2);
            if (var3.length() > 0 || var5.length() > 0) {
               var4.append('_');
            }

            var4.append(var3);
            if (var5.length() > 0) {
               var4.append('_');
            }

            var4.append(var5);
            return var4.toString();
         }
      }
   }

   public static String getLocaleStringIso639() {
      Locale var0 = getInstance().currentLocale;
      if (var0 == null) {
         return "en";
      } else {
         String var1 = var0.getLanguage();
         String var2 = var0.getCountry();
         String var3 = var0.getVariant();
         if (var1.length() == 0 && var2.length() == 0) {
            return "en";
         } else {
            StringBuilder var4 = new StringBuilder(11);
            var4.append(var1);
            if (var2.length() > 0 || var3.length() > 0) {
               var4.append('-');
            }

            var4.append(var2);
            if (var3.length() > 0) {
               var4.append('_');
            }

            var4.append(var3);
            return var4.toString();
         }
      }
   }

   public static String getPluralString(String var0, int var1) {
      if (var0 != null && var0.length() != 0 && getInstance().currentPluralRules != null) {
         String var4 = getInstance().stringForQuantity(getInstance().currentPluralRules.quantityForNumber(var1));
         StringBuilder var3 = new StringBuilder();
         var3.append(var0);
         var3.append("_");
         var3.append(var4);
         var0 = var3.toString();
         return getString(var0, ApplicationLoader.applicationContext.getResources().getIdentifier(var0, "string", ApplicationLoader.applicationContext.getPackageName()));
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("LOC_ERR:");
         var2.append(var0);
         return var2.toString();
      }
   }

   public static String getServerString(String var0) {
      String var1 = (String)getInstance().localeValues.get(var0);
      String var2 = var1;
      if (var1 == null) {
         int var3 = ApplicationLoader.applicationContext.getResources().getIdentifier(var0, "string", ApplicationLoader.applicationContext.getPackageName());
         var2 = var1;
         if (var3 != 0) {
            var2 = ApplicationLoader.applicationContext.getString(var3);
         }
      }

      return var2;
   }

   public static String getString(String var0, int var1) {
      return getInstance().getStringInternal(var0, var1);
   }

   private String getStringInternal(String var1, int var2) {
      String var3;
      if (BuildVars.USE_CLOUD_STRINGS) {
         var3 = (String)this.localeValues.get(var1);
      } else {
         var3 = null;
      }

      String var4 = var3;
      if (var3 == null) {
         try {
            var4 = ApplicationLoader.applicationContext.getString(var2);
         } catch (Exception var5) {
            FileLog.e((Throwable)var5);
            var4 = var3;
         }
      }

      var3 = var4;
      if (var4 == null) {
         StringBuilder var6 = new StringBuilder();
         var6.append("LOC_ERR:");
         var6.append(var1);
         var3 = var6.toString();
      }

      return var3;
   }

   public static String getSystemLocaleStringIso639() {
      Locale var0 = getInstance().getSystemDefaultLocale();
      if (var0 == null) {
         return "en";
      } else {
         String var1 = var0.getLanguage();
         String var2 = var0.getCountry();
         String var3 = var0.getVariant();
         if (var1.length() == 0 && var2.length() == 0) {
            return "en";
         } else {
            StringBuilder var4 = new StringBuilder(11);
            var4.append(var1);
            if (var2.length() > 0 || var3.length() > 0) {
               var4.append('-');
            }

            var4.append(var2);
            if (var3.length() > 0) {
               var4.append('_');
            }

            var4.append(var3);
            return var4.toString();
         }
      }
   }

   public static boolean isRTLCharacter(char var0) {
      byte var1 = Character.getDirectionality(var0);
      boolean var2 = true;
      boolean var3 = var2;
      if (var1 != 1) {
         var3 = var2;
         if (Character.getDirectionality(var0) != 2) {
            var3 = var2;
            if (Character.getDirectionality(var0) != 16) {
               if (Character.getDirectionality(var0) == 17) {
                  var3 = var2;
               } else {
                  var3 = false;
               }
            }
         }
      }

      return var3;
   }

   private void loadOtherLanguages() {
      Context var1 = ApplicationLoader.applicationContext;
      byte var2 = 0;
      SharedPreferences var7 = var1.getSharedPreferences("langconfig", 0);
      String var3 = var7.getString("locales", (String)null);
      int var5;
      int var6;
      LocaleController.LocaleInfo var10;
      if (!TextUtils.isEmpty(var3)) {
         String[] var4 = var3.split("&");
         var5 = var4.length;

         for(var6 = 0; var6 < var5; ++var6) {
            var10 = LocaleController.LocaleInfo.createWithString(var4[var6]);
            if (var10 != null) {
               this.otherLanguages.add(var10);
            }
         }
      }

      var3 = var7.getString("remote", (String)null);
      if (!TextUtils.isEmpty(var3)) {
         String[] var11 = var3.split("&");
         var5 = var11.length;

         for(var6 = 0; var6 < var5; ++var6) {
            LocaleController.LocaleInfo var12 = LocaleController.LocaleInfo.createWithString(var11[var6]);
            var12.shortName = var12.shortName.replace("-", "_");
            if (!this.remoteLanguagesDict.containsKey(var12.getKey()) && var12 != null) {
               this.remoteLanguages.add(var12);
               this.remoteLanguagesDict.put(var12.getKey(), var12);
            }
         }
      }

      String var8 = var7.getString("unofficial", (String)null);
      if (!TextUtils.isEmpty(var8)) {
         String[] var9 = var8.split("&");
         var5 = var9.length;

         for(var6 = var2; var6 < var5; ++var6) {
            var10 = LocaleController.LocaleInfo.createWithString(var9[var6]);
            var10.shortName = var10.shortName.replace("-", "_");
            if (var10 != null) {
               this.unofficialLanguages.add(var10);
            }
         }
      }

   }

   private void saveOtherLanguages() {
      Context var1 = ApplicationLoader.applicationContext;
      byte var2 = 0;
      Editor var6 = var1.getSharedPreferences("langconfig", 0).edit();
      StringBuilder var3 = new StringBuilder();

      int var4;
      String var5;
      for(var4 = 0; var4 < this.otherLanguages.size(); ++var4) {
         var5 = ((LocaleController.LocaleInfo)this.otherLanguages.get(var4)).getSaveString();
         if (var5 != null) {
            if (var3.length() != 0) {
               var3.append("&");
            }

            var3.append(var5);
         }
      }

      var6.putString("locales", var3.toString());
      var3.setLength(0);

      for(var4 = 0; var4 < this.remoteLanguages.size(); ++var4) {
         var5 = ((LocaleController.LocaleInfo)this.remoteLanguages.get(var4)).getSaveString();
         if (var5 != null) {
            if (var3.length() != 0) {
               var3.append("&");
            }

            var3.append(var5);
         }
      }

      var6.putString("remote", var3.toString());
      var3.setLength(0);

      for(var4 = var2; var4 < this.unofficialLanguages.size(); ++var4) {
         var5 = ((LocaleController.LocaleInfo)this.unofficialLanguages.get(var4)).getSaveString();
         if (var5 != null) {
            if (var3.length() != 0) {
               var3.append("&");
            }

            var3.append(var5);
         }
      }

      var6.putString("unofficial", var3.toString());
      var6.commit();
   }

   public static String stringForMessageListDate(long var0) {
      var0 *= 1000L;

      Exception var10000;
      label57: {
         int var3;
         int var4;
         Date var13;
         FastDateFormat var15;
         boolean var10001;
         try {
            Calendar var2 = Calendar.getInstance();
            var3 = var2.get(6);
            var2.setTimeInMillis(var0);
            var4 = var2.get(6);
            if (Math.abs(System.currentTimeMillis() - var0) >= 31536000000L) {
               var15 = getInstance().formatterYear;
               var13 = new Date(var0);
               return var15.format(var13);
            }
         } catch (Exception var10) {
            var10000 = var10;
            var10001 = false;
            break label57;
         }

         label49: {
            var3 = var4 - var3;
            if (var3 != 0) {
               if (var3 != -1) {
                  break label49;
               }

               try {
                  if (System.currentTimeMillis() - var0 >= 28800000L) {
                     break label49;
                  }
               } catch (Exception var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label57;
               }
            }

            try {
               FastDateFormat var11 = getInstance().formatterDay;
               Date var5 = new Date(var0);
               String var12 = var11.format(var5);
               return var12;
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label57;
            }
         }

         if (var3 > -7 && var3 <= -1) {
            try {
               var15 = getInstance().formatterWeek;
               var13 = new Date(var0);
               return var15.format(var13);
            } catch (Exception var6) {
               var10000 = var6;
               var10001 = false;
            }
         } else {
            try {
               var15 = getInstance().formatterDayMonth;
               var13 = new Date(var0);
               return var15.format(var13);
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
            }
         }
      }

      Exception var14 = var10000;
      FileLog.e((Throwable)var14);
      return "LOC_ERR";
   }

   private String stringForQuantity(int var1) {
      if (var1 != 1) {
         if (var1 != 2) {
            if (var1 != 4) {
               if (var1 != 8) {
                  return var1 != 16 ? "other" : "many";
               } else {
                  return "few";
               }
            } else {
               return "two";
            }
         } else {
            return "one";
         }
      } else {
         return "zero";
      }
   }

   public void applyLanguage(LocaleController.LocaleInfo var1, boolean var2, boolean var3, int var4) {
      this.applyLanguage(var1, var2, var3, false, false, var4);
   }

   public void applyLanguage(LocaleController.LocaleInfo var1, boolean var2, boolean var3, boolean var4, boolean var5, int var6) {
      if (var1 != null) {
         boolean var7 = var1.hasBaseLang();
         File var8 = var1.getPathToFile();
         File var9 = var1.getPathToBaseFile();
         String var10 = var1.shortName;
         if (!var3) {
            ConnectionsManager.setLangCode(var1.getLangCode());
         }

         if (this.getLanguageFromDict(var1.getKey()) == null) {
            if (var1.isRemote()) {
               this.remoteLanguages.add(var1);
               this.remoteLanguagesDict.put(var1.getKey(), var1);
               this.languages.add(var1);
               this.languagesDict.put(var1.getKey(), var1);
               this.saveOtherLanguages();
            } else if (var1.isUnofficial()) {
               this.unofficialLanguages.add(var1);
               this.languagesDict.put(var1.getKey(), var1);
               this.saveOtherLanguages();
            }
         }

         if ((var1.isRemote() || var1.isUnofficial()) && (var5 || !var8.exists() || var7 && !var9.exists())) {
            if (BuildVars.LOGS_ENABLED) {
               StringBuilder var35 = new StringBuilder();
               var35.append("reload locale because one of file doesn't exist");
               var35.append(var8);
               var35.append(" ");
               var35.append(var9);
               FileLog.d(var35.toString());
            }

            if (var3) {
               AndroidUtilities.runOnUIThread(new _$$Lambda$LocaleController$rPqRyQsgkE1_kSvpx5ngOlMyIY4(this, var1, var6));
            } else {
               this.applyRemoteLanguage(var1, (String)null, true, var6);
            }
         }

         label179: {
            Exception var10000;
            label199: {
               boolean var10001;
               try {
                  var5 = TextUtils.isEmpty(var1.pluralLangCode);
               } catch (Exception var29) {
                  var10000 = var29;
                  var10001 = false;
                  break label199;
               }

               String[] var34;
               if (!var5) {
                  try {
                     var34 = var1.pluralLangCode.split("_");
                  } catch (Exception var27) {
                     var10000 = var27;
                     var10001 = false;
                     break label199;
                  }
               } else {
                  label205: {
                     try {
                        if (!TextUtils.isEmpty(var1.baseLangCode)) {
                           var34 = var1.baseLangCode.split("_");
                           break label205;
                        }
                     } catch (Exception var28) {
                        var10000 = var28;
                        var10001 = false;
                        break label199;
                     }

                     try {
                        var34 = var1.shortName.split("_");
                     } catch (Exception var26) {
                        var10000 = var26;
                        var10001 = false;
                        break label199;
                     }
                  }
               }

               Locale var36;
               label161: {
                  try {
                     if (var34.length == 1) {
                        var36 = new Locale(var34[0]);
                        break label161;
                     }
                  } catch (Exception var25) {
                     var10000 = var25;
                     var10001 = false;
                     break label199;
                  }

                  try {
                     var36 = new Locale(var34[0], var34[1]);
                  } catch (Exception var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label199;
                  }
               }

               if (var2) {
                  try {
                     this.languageOverride = var1.shortName;
                     Editor var11 = MessagesController.getGlobalMainSettings().edit();
                     var11.putString("language", var1.getKey());
                     var11.commit();
                  } catch (Exception var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label199;
                  }
               }

               if (var8 == null) {
                  try {
                     this.localeValues.clear();
                  } catch (Exception var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label199;
                  }
               } else if (!var4) {
                  if (var7) {
                     try {
                        var8 = var1.getPathToBaseFile();
                     } catch (Exception var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label199;
                     }
                  } else {
                     try {
                        var8 = var1.getPathToFile();
                     } catch (Exception var20) {
                        var10000 = var20;
                        var10001 = false;
                        break label199;
                     }
                  }

                  try {
                     this.localeValues = this.getLocaleFileStrings(var8);
                  } catch (Exception var19) {
                     var10000 = var19;
                     var10001 = false;
                     break label199;
                  }

                  if (var7) {
                     try {
                        this.localeValues.putAll(this.getLocaleFileStrings(var1.getPathToFile()));
                     } catch (Exception var18) {
                        var10000 = var18;
                        var10001 = false;
                        break label199;
                     }
                  }
               }

               try {
                  this.currentLocale = var36;
                  this.currentLocaleInfo = var1;
                  if (this.currentLocaleInfo != null && !TextUtils.isEmpty(this.currentLocaleInfo.pluralLangCode)) {
                     this.currentPluralRules = (LocaleController.PluralRules)this.allRules.get(this.currentLocaleInfo.pluralLangCode);
                  }
               } catch (Exception var17) {
                  var10000 = var17;
                  var10001 = false;
                  break label199;
               }

               try {
                  if (this.currentPluralRules == null) {
                     this.currentPluralRules = (LocaleController.PluralRules)this.allRules.get(var34[0]);
                     if (this.currentPluralRules == null) {
                        this.currentPluralRules = (LocaleController.PluralRules)this.allRules.get(this.currentLocale.getLanguage());
                        if (this.currentPluralRules == null) {
                           LocaleController.PluralRules_None var30 = new LocaleController.PluralRules_None();
                           this.currentPluralRules = var30;
                        }
                     }
                  }
               } catch (Exception var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label199;
               }

               try {
                  this.changingConfiguration = true;
                  Locale.setDefault(this.currentLocale);
                  Configuration var31 = new Configuration();
                  var31.locale = this.currentLocale;
                  ApplicationLoader.applicationContext.getResources().updateConfiguration(var31, ApplicationLoader.applicationContext.getResources().getDisplayMetrics());
                  this.changingConfiguration = false;
                  if (!this.reloadLastFile) {
                     break label179;
                  }
               } catch (Exception var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label199;
               }

               if (var3) {
                  try {
                     _$$Lambda$LocaleController$TG7Fd9ju6kk9LWo1RB9e1CZ_6mk var32 = new _$$Lambda$LocaleController$TG7Fd9ju6kk9LWo1RB9e1CZ_6mk(this, var6);
                     AndroidUtilities.runOnUIThread(var32);
                  } catch (Exception var14) {
                     var10000 = var14;
                     var10001 = false;
                     break label199;
                  }
               } else {
                  try {
                     this.reloadCurrentRemoteLocale(var6, (String)null);
                  } catch (Exception var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label199;
                  }
               }

               try {
                  this.reloadLastFile = false;
                  break label179;
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
               }
            }

            Exception var33 = var10000;
            FileLog.e((Throwable)var33);
            this.changingConfiguration = false;
         }

         this.recreateFormatters();
      }
   }

   public boolean applyLanguageFile(File var1, int var2) {
      Exception var10000;
      label111: {
         HashMap var3;
         String var4;
         String var5;
         String var6;
         boolean var10001;
         try {
            var3 = this.getLocaleFileStrings(var1);
            var4 = (String)var3.get("LanguageName");
            var5 = (String)var3.get("LanguageNameInEnglish");
            var6 = (String)var3.get("LanguageCode");
         } catch (Exception var16) {
            var10000 = var16;
            var10001 = false;
            break label111;
         }

         if (var4 == null) {
            return false;
         }

         try {
            if (var4.length() <= 0) {
               return false;
            }
         } catch (Exception var15) {
            var10000 = var15;
            var10001 = false;
            break label111;
         }

         if (var5 == null) {
            return false;
         }

         try {
            if (var5.length() <= 0) {
               return false;
            }
         } catch (Exception var14) {
            var10000 = var14;
            var10001 = false;
            break label111;
         }

         if (var6 == null) {
            return false;
         }

         label106: {
            label112: {
               try {
                  if (var6.length() <= 0) {
                     return false;
                  }

                  if (var4.contains("&") || var4.contains("|")) {
                     break label112;
                  }
               } catch (Exception var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label111;
               }

               try {
                  if (var5.contains("&") || var5.contains("|")) {
                     break label112;
                  }
               } catch (Exception var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label111;
               }

               try {
                  if (!var6.contains("&") && !var6.contains("|") && !var6.contains("/") && !var6.contains("\\")) {
                     break label106;
                  }
               } catch (Exception var18) {
                  var10000 = var18;
                  var10001 = false;
                  break label111;
               }
            }

            try {
               return false;
            } catch (Exception var10) {
               var10000 = var10;
               var10001 = false;
               break label111;
            }
         }

         File var7;
         try {
            File var8 = ApplicationLoader.getFilesDirFixed();
            StringBuilder var9 = new StringBuilder();
            var9.append(var6);
            var9.append(".xml");
            var7 = new File(var8, var9.toString());
            if (!AndroidUtilities.copyFile(var1, var7)) {
               return false;
            }
         } catch (Exception var17) {
            var10000 = var17;
            var10001 = false;
            break label111;
         }

         LocaleController.LocaleInfo var24;
         try {
            StringBuilder var21 = new StringBuilder();
            var21.append("local_");
            var21.append(var6.toLowerCase());
            var24 = this.getLanguageFromDict(var21.toString());
         } catch (Exception var13) {
            var10000 = var13;
            var10001 = false;
            break label111;
         }

         LocaleController.LocaleInfo var22 = var24;
         if (var24 == null) {
            try {
               var22 = new LocaleController.LocaleInfo();
               var22.name = var4;
               var22.nameEnglish = var5;
               var22.shortName = var6.toLowerCase();
               var22.pluralLangCode = var22.shortName;
               var22.pathToFile = var7.getAbsolutePath();
               this.languages.add(var22);
               this.languagesDict.put(var22.getKey(), var22);
               this.otherLanguages.add(var22);
               this.saveOtherLanguages();
            } catch (Exception var12) {
               var10000 = var12;
               var10001 = false;
               break label111;
            }
         }

         try {
            this.localeValues = var3;
            this.applyLanguage(var22, true, false, true, false, var2);
            return true;
         } catch (Exception var11) {
            var10000 = var11;
            var10001 = false;
         }
      }

      Exception var23 = var10000;
      FileLog.e((Throwable)var23);
      return false;
   }

   public void checkUpdateForCurrentRemoteLocale(int var1, int var2, int var3) {
      LocaleController.LocaleInfo var4 = this.currentLocaleInfo;
      if (var4 != null && (var4 == null || var4.isRemote() || this.currentLocaleInfo.isUnofficial())) {
         if (this.currentLocaleInfo.hasBaseLang()) {
            var4 = this.currentLocaleInfo;
            if (var4.baseVersion < var3) {
               this.applyRemoteLanguage(var4, var4.baseLangCode, false, var1);
            }
         }

         var4 = this.currentLocaleInfo;
         if (var4.version < var2) {
            this.applyRemoteLanguage(var4, var4.shortName, false, var1);
         }
      }

   }

   public boolean deleteLanguage(LocaleController.LocaleInfo var1, int var2) {
      if (var1.pathToFile == null || var1.isRemote() && var1.serverIndex != Integer.MAX_VALUE) {
         return false;
      } else {
         if (this.currentLocaleInfo == var1) {
            LocaleController.LocaleInfo var3 = null;
            if (this.systemDefaultLocale.getLanguage() != null) {
               var3 = this.getLanguageFromDict(this.systemDefaultLocale.getLanguage());
            }

            LocaleController.LocaleInfo var4 = var3;
            if (var3 == null) {
               var4 = this.getLanguageFromDict(this.getLocaleString(this.systemDefaultLocale));
            }

            var3 = var4;
            if (var4 == null) {
               var3 = this.getLanguageFromDict("en");
            }

            this.applyLanguage(var3, true, false, var2);
         }

         this.unofficialLanguages.remove(var1);
         this.remoteLanguages.remove(var1);
         this.remoteLanguagesDict.remove(var1.getKey());
         this.otherLanguages.remove(var1);
         this.languages.remove(var1);
         this.languagesDict.remove(var1.getKey());
         (new File(var1.pathToFile)).delete();
         this.saveOtherLanguages();
         return true;
      }
   }

   public String formatCurrencyDecimalString(long var1, String var3, boolean var4) {
      String var5;
      byte var6;
      label120: {
         var5 = var3.toUpperCase();
         var1 = Math.abs(var1);
         switch(var5.hashCode()) {
         case 65726:
            if (var5.equals("BHD")) {
               var6 = 2;
               break label120;
            }
            break;
         case 65759:
            if (var5.equals("BIF")) {
               var6 = 9;
               break label120;
            }
            break;
         case 66267:
            if (var5.equals("BYR")) {
               var6 = 10;
               break label120;
            }
            break;
         case 66813:
            if (var5.equals("CLF")) {
               var6 = 0;
               break label120;
            }
            break;
         case 66823:
            if (var5.equals("CLP")) {
               var6 = 11;
               break label120;
            }
            break;
         case 67122:
            if (var5.equals("CVE")) {
               var6 = 12;
               break label120;
            }
            break;
         case 67712:
            if (var5.equals("DJF")) {
               var6 = 13;
               break label120;
            }
            break;
         case 70719:
            if (var5.equals("GNF")) {
               var6 = 14;
               break label120;
            }
            break;
         case 72732:
            if (var5.equals("IQD")) {
               var6 = 3;
               break label120;
            }
            break;
         case 72777:
            if (var5.equals("IRR")) {
               var6 = 1;
               break label120;
            }
            break;
         case 72801:
            if (var5.equals("ISK")) {
               var6 = 15;
               break label120;
            }
            break;
         case 73631:
            if (var5.equals("JOD")) {
               var6 = 4;
               break label120;
            }
            break;
         case 73683:
            if (var5.equals("JPY")) {
               var6 = 16;
               break label120;
            }
            break;
         case 74532:
            if (var5.equals("KMF")) {
               var6 = 17;
               break label120;
            }
            break;
         case 74704:
            if (var5.equals("KRW")) {
               var6 = 18;
               break label120;
            }
            break;
         case 74840:
            if (var5.equals("KWD")) {
               var6 = 5;
               break label120;
            }
            break;
         case 75863:
            if (var5.equals("LYD")) {
               var6 = 6;
               break label120;
            }
            break;
         case 76263:
            if (var5.equals("MGA")) {
               var6 = 19;
               break label120;
            }
            break;
         case 76618:
            if (var5.equals("MRO")) {
               var6 = 29;
               break label120;
            }
            break;
         case 78388:
            if (var5.equals("OMR")) {
               var6 = 7;
               break label120;
            }
            break;
         case 79710:
            if (var5.equals("PYG")) {
               var6 = 20;
               break label120;
            }
            break;
         case 81569:
            if (var5.equals("RWF")) {
               var6 = 21;
               break label120;
            }
            break;
         case 83210:
            if (var5.equals("TND")) {
               var6 = 8;
               break label120;
            }
            break;
         case 83974:
            if (var5.equals("UGX")) {
               var6 = 22;
               break label120;
            }
            break;
         case 84517:
            if (var5.equals("UYI")) {
               var6 = 23;
               break label120;
            }
            break;
         case 85132:
            if (var5.equals("VND")) {
               var6 = 24;
               break label120;
            }
            break;
         case 85367:
            if (var5.equals("VUV")) {
               var6 = 25;
               break label120;
            }
            break;
         case 86653:
            if (var5.equals("XAF")) {
               var6 = 26;
               break label120;
            }
            break;
         case 87087:
            if (var5.equals("XOF")) {
               var6 = 27;
               break label120;
            }
            break;
         case 87118:
            if (var5.equals("XPF")) {
               var6 = 28;
               break label120;
            }
         }

         var6 = -1;
      }

      var3 = " %.0f";
      double var7;
      switch(var6) {
      case 0:
         var7 = (double)var1;
         Double.isNaN(var7);
         var7 /= 10000.0D;
         var3 = " %.4f";
         break;
      case 1:
         var7 = (double)((float)var1 / 100.0F);
         if (var1 % 100L != 0L) {
            var3 = " %.2f";
         }
         break;
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
         var7 = (double)var1;
         Double.isNaN(var7);
         var7 /= 1000.0D;
         var3 = " %.3f";
         break;
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
      case 28:
         var7 = (double)var1;
         break;
      case 29:
         var7 = (double)var1;
         Double.isNaN(var7);
         var7 /= 10.0D;
         var3 = " %.1f";
         break;
      default:
         var7 = (double)var1;
         Double.isNaN(var7);
         var7 /= 100.0D;
         var3 = " %.2f";
      }

      Locale var9 = Locale.US;
      if (var4) {
         var3 = var5;
      } else {
         StringBuilder var10 = new StringBuilder();
         var10.append("");
         var10.append(var3);
         var3 = var10.toString();
      }

      return String.format(var9, var3, var7).trim();
   }

   public String formatCurrencyString(long var1, String var3) {
      String var4 = var3.toUpperCase();
      boolean var5;
      if (var1 < 0L) {
         var5 = true;
      } else {
         var5 = false;
      }

      var1 = Math.abs(var1);
      Currency var6 = Currency.getInstance(var4);
      byte var7 = -1;
      switch(var4.hashCode()) {
      case 65726:
         if (var4.equals("BHD")) {
            var7 = 2;
         }
         break;
      case 65759:
         if (var4.equals("BIF")) {
            var7 = 9;
         }
         break;
      case 66267:
         if (var4.equals("BYR")) {
            var7 = 10;
         }
         break;
      case 66813:
         if (var4.equals("CLF")) {
            var7 = 0;
         }
         break;
      case 66823:
         if (var4.equals("CLP")) {
            var7 = 11;
         }
         break;
      case 67122:
         if (var4.equals("CVE")) {
            var7 = 12;
         }
         break;
      case 67712:
         if (var4.equals("DJF")) {
            var7 = 13;
         }
         break;
      case 70719:
         if (var4.equals("GNF")) {
            var7 = 14;
         }
         break;
      case 72732:
         if (var4.equals("IQD")) {
            var7 = 3;
         }
         break;
      case 72777:
         if (var4.equals("IRR")) {
            var7 = 1;
         }
         break;
      case 72801:
         if (var4.equals("ISK")) {
            var7 = 15;
         }
         break;
      case 73631:
         if (var4.equals("JOD")) {
            var7 = 4;
         }
         break;
      case 73683:
         if (var4.equals("JPY")) {
            var7 = 16;
         }
         break;
      case 74532:
         if (var4.equals("KMF")) {
            var7 = 17;
         }
         break;
      case 74704:
         if (var4.equals("KRW")) {
            var7 = 18;
         }
         break;
      case 74840:
         if (var4.equals("KWD")) {
            var7 = 5;
         }
         break;
      case 75863:
         if (var4.equals("LYD")) {
            var7 = 6;
         }
         break;
      case 76263:
         if (var4.equals("MGA")) {
            var7 = 19;
         }
         break;
      case 76618:
         if (var4.equals("MRO")) {
            var7 = 29;
         }
         break;
      case 78388:
         if (var4.equals("OMR")) {
            var7 = 7;
         }
         break;
      case 79710:
         if (var4.equals("PYG")) {
            var7 = 20;
         }
         break;
      case 81569:
         if (var4.equals("RWF")) {
            var7 = 21;
         }
         break;
      case 83210:
         if (var4.equals("TND")) {
            var7 = 8;
         }
         break;
      case 83974:
         if (var4.equals("UGX")) {
            var7 = 22;
         }
         break;
      case 84517:
         if (var4.equals("UYI")) {
            var7 = 23;
         }
         break;
      case 85132:
         if (var4.equals("VND")) {
            var7 = 24;
         }
         break;
      case 85367:
         if (var4.equals("VUV")) {
            var7 = 25;
         }
         break;
      case 86653:
         if (var4.equals("XAF")) {
            var7 = 26;
         }
         break;
      case 87087:
         if (var4.equals("XOF")) {
            var7 = 27;
         }
         break;
      case 87118:
         if (var4.equals("XPF")) {
            var7 = 28;
         }
      }

      var3 = " %.0f";
      double var8;
      switch(var7) {
      case 0:
         var8 = (double)var1;
         Double.isNaN(var8);
         var8 /= 10000.0D;
         var3 = " %.4f";
         break;
      case 1:
         var8 = (double)((float)var1 / 100.0F);
         if (var1 % 100L != 0L) {
            var3 = " %.2f";
         }
         break;
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
         var8 = (double)var1;
         Double.isNaN(var8);
         var8 /= 1000.0D;
         var3 = " %.3f";
         break;
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
      case 28:
         var8 = (double)var1;
         break;
      case 29:
         var8 = (double)var1;
         Double.isNaN(var8);
         var8 /= 10.0D;
         var3 = " %.1f";
         break;
      default:
         var8 = (double)var1;
         Double.isNaN(var8);
         var8 /= 100.0D;
         var3 = " %.2f";
      }

      String var10 = "-";
      if (var6 != null) {
         Locale var13 = this.currentLocale;
         if (var13 == null) {
            var13 = this.systemDefaultLocale;
         }

         NumberFormat var15 = NumberFormat.getCurrencyInstance(var13);
         var15.setCurrency(var6);
         if (var4.equals("IRR")) {
            var15.setMaximumFractionDigits(0);
         }

         StringBuilder var12 = new StringBuilder();
         if (!var5) {
            var10 = "";
         }

         var12.append(var10);
         var12.append(var15.format(var8));
         return var12.toString();
      } else {
         StringBuilder var14 = new StringBuilder();
         if (!var5) {
            var10 = "";
         }

         var14.append(var10);
         Locale var11 = Locale.US;
         StringBuilder var16 = new StringBuilder();
         var16.append(var4);
         var16.append(var3);
         var14.append(String.format(var11, var16.toString(), var8));
         return var14.toString();
      }
   }

   public LocaleController.LocaleInfo getCurrentLocaleInfo() {
      return this.currentLocaleInfo;
   }

   public LocaleController.LocaleInfo getLanguageFromDict(String var1) {
      return var1 == null ? null : (LocaleController.LocaleInfo)this.languagesDict.get(var1.toLowerCase().replace("-", "_"));
   }

   public Locale getSystemDefaultLocale() {
      return this.systemDefaultLocale;
   }

   public String getTranslitString(String var1) {
      return this.getTranslitString(var1, true, false);
   }

   public String getTranslitString(String var1, boolean var2) {
      return this.getTranslitString(var1, true, var2);
   }

   public String getTranslitString(String var1, boolean var2, boolean var3) {
      if (var1 == null) {
         return null;
      } else {
         if (this.ruTranslitChars == null) {
            this.ruTranslitChars = new HashMap(33);
            this.ruTranslitChars.put("а", "a");
            this.ruTranslitChars.put("б", "b");
            this.ruTranslitChars.put("в", "v");
            this.ruTranslitChars.put("г", "g");
            this.ruTranslitChars.put("д", "d");
            this.ruTranslitChars.put("е", "e");
            this.ruTranslitChars.put("ё", "yo");
            this.ruTranslitChars.put("ж", "zh");
            this.ruTranslitChars.put("з", "z");
            this.ruTranslitChars.put("и", "i");
            this.ruTranslitChars.put("й", "i");
            this.ruTranslitChars.put("к", "k");
            this.ruTranslitChars.put("л", "l");
            this.ruTranslitChars.put("м", "m");
            this.ruTranslitChars.put("н", "n");
            this.ruTranslitChars.put("о", "o");
            this.ruTranslitChars.put("п", "p");
            this.ruTranslitChars.put("р", "r");
            this.ruTranslitChars.put("с", "s");
            this.ruTranslitChars.put("т", "t");
            this.ruTranslitChars.put("у", "u");
            this.ruTranslitChars.put("ф", "f");
            this.ruTranslitChars.put("х", "h");
            this.ruTranslitChars.put("ц", "ts");
            this.ruTranslitChars.put("ч", "ch");
            this.ruTranslitChars.put("ш", "sh");
            this.ruTranslitChars.put("щ", "sch");
            this.ruTranslitChars.put("ы", "i");
            this.ruTranslitChars.put("ь", "");
            this.ruTranslitChars.put("ъ", "");
            this.ruTranslitChars.put("э", "e");
            this.ruTranslitChars.put("ю", "yu");
            this.ruTranslitChars.put("я", "ya");
         }

         if (this.translitChars == null) {
            this.translitChars = new HashMap(487);
            this.translitChars.put("ȼ", "c");
            this.translitChars.put("ᶇ", "n");
            this.translitChars.put("ɖ", "d");
            this.translitChars.put("ỿ", "y");
            this.translitChars.put("ᴓ", "o");
            this.translitChars.put("ø", "o");
            this.translitChars.put("ḁ", "a");
            this.translitChars.put("ʯ", "h");
            this.translitChars.put("ŷ", "y");
            this.translitChars.put("ʞ", "k");
            this.translitChars.put("ừ", "u");
            this.translitChars.put("ꜳ", "aa");
            this.translitChars.put("ĳ", "ij");
            this.translitChars.put("ḽ", "l");
            this.translitChars.put("ɪ", "i");
            this.translitChars.put("ḇ", "b");
            this.translitChars.put("ʀ", "r");
            this.translitChars.put("ě", "e");
            this.translitChars.put("ﬃ", "ffi");
            this.translitChars.put("ơ", "o");
            this.translitChars.put("ⱹ", "r");
            this.translitChars.put("ồ", "o");
            this.translitChars.put("ǐ", "i");
            this.translitChars.put("ꝕ", "p");
            this.translitChars.put("ý", "y");
            this.translitChars.put("ḝ", "e");
            this.translitChars.put("ₒ", "o");
            this.translitChars.put("ⱥ", "a");
            this.translitChars.put("ʙ", "b");
            this.translitChars.put("ḛ", "e");
            this.translitChars.put("ƈ", "c");
            this.translitChars.put("ɦ", "h");
            this.translitChars.put("ᵬ", "b");
            this.translitChars.put("ṣ", "s");
            this.translitChars.put("đ", "d");
            this.translitChars.put("ỗ", "o");
            this.translitChars.put("ɟ", "j");
            this.translitChars.put("ẚ", "a");
            this.translitChars.put("ɏ", "y");
            this.translitChars.put("ʌ", "v");
            this.translitChars.put("ꝓ", "p");
            this.translitChars.put("ﬁ", "fi");
            this.translitChars.put("ᶄ", "k");
            this.translitChars.put("ḏ", "d");
            this.translitChars.put("ᴌ", "l");
            this.translitChars.put("ė", "e");
            this.translitChars.put("ᴋ", "k");
            this.translitChars.put("ċ", "c");
            this.translitChars.put("ʁ", "r");
            this.translitChars.put("ƕ", "hv");
            this.translitChars.put("ƀ", "b");
            this.translitChars.put("ṍ", "o");
            this.translitChars.put("ȣ", "ou");
            this.translitChars.put("ǰ", "j");
            this.translitChars.put("ᶃ", "g");
            this.translitChars.put("ṋ", "n");
            this.translitChars.put("ɉ", "j");
            this.translitChars.put("ǧ", "g");
            this.translitChars.put("ǳ", "dz");
            this.translitChars.put("ź", "z");
            this.translitChars.put("ꜷ", "au");
            this.translitChars.put("ǖ", "u");
            this.translitChars.put("ᵹ", "g");
            this.translitChars.put("ȯ", "o");
            this.translitChars.put("ɐ", "a");
            this.translitChars.put("ą", "a");
            this.translitChars.put("õ", "o");
            this.translitChars.put("ɻ", "r");
            this.translitChars.put("ꝍ", "o");
            this.translitChars.put("ǟ", "a");
            this.translitChars.put("ȴ", "l");
            this.translitChars.put("ʂ", "s");
            this.translitChars.put("ﬂ", "fl");
            this.translitChars.put("ȉ", "i");
            this.translitChars.put("ⱻ", "e");
            this.translitChars.put("ṉ", "n");
            this.translitChars.put("ï", "i");
            this.translitChars.put("ñ", "n");
            this.translitChars.put("ᴉ", "i");
            this.translitChars.put("ʇ", "t");
            this.translitChars.put("ẓ", "z");
            this.translitChars.put("ỷ", "y");
            this.translitChars.put("ȳ", "y");
            this.translitChars.put("ṩ", "s");
            this.translitChars.put("ɽ", "r");
            this.translitChars.put("ĝ", "g");
            this.translitChars.put("ᴝ", "u");
            this.translitChars.put("ḳ", "k");
            this.translitChars.put("ꝫ", "et");
            this.translitChars.put("ī", "i");
            this.translitChars.put("ť", "t");
            this.translitChars.put("ꜿ", "c");
            this.translitChars.put("ʟ", "l");
            this.translitChars.put("ꜹ", "av");
            this.translitChars.put("û", "u");
            this.translitChars.put("æ", "ae");
            this.translitChars.put("ă", "a");
            this.translitChars.put("ǘ", "u");
            this.translitChars.put("ꞅ", "s");
            this.translitChars.put("ᵣ", "r");
            this.translitChars.put("ᴀ", "a");
            this.translitChars.put("ƃ", "b");
            this.translitChars.put("ḩ", "h");
            this.translitChars.put("ṧ", "s");
            this.translitChars.put("ₑ", "e");
            this.translitChars.put("ʜ", "h");
            this.translitChars.put("ẋ", "x");
            this.translitChars.put("ꝅ", "k");
            this.translitChars.put("ḋ", "d");
            this.translitChars.put("ƣ", "oi");
            this.translitChars.put("ꝑ", "p");
            this.translitChars.put("ħ", "h");
            this.translitChars.put("ⱴ", "v");
            this.translitChars.put("ẇ", "w");
            this.translitChars.put("ǹ", "n");
            this.translitChars.put("ɯ", "m");
            this.translitChars.put("ɡ", "g");
            this.translitChars.put("ɴ", "n");
            this.translitChars.put("ᴘ", "p");
            this.translitChars.put("ᵥ", "v");
            this.translitChars.put("ū", "u");
            this.translitChars.put("ḃ", "b");
            this.translitChars.put("ṗ", "p");
            this.translitChars.put("å", "a");
            this.translitChars.put("ɕ", "c");
            this.translitChars.put("ọ", "o");
            this.translitChars.put("ắ", "a");
            this.translitChars.put("ƒ", "f");
            this.translitChars.put("ǣ", "ae");
            this.translitChars.put("ꝡ", "vy");
            this.translitChars.put("ﬀ", "ff");
            this.translitChars.put("ᶉ", "r");
            this.translitChars.put("ô", "o");
            this.translitChars.put("ǿ", "o");
            this.translitChars.put("ṳ", "u");
            this.translitChars.put("ȥ", "z");
            this.translitChars.put("ḟ", "f");
            this.translitChars.put("ḓ", "d");
            this.translitChars.put("ȇ", "e");
            this.translitChars.put("ȕ", "u");
            this.translitChars.put("ȵ", "n");
            this.translitChars.put("ʠ", "q");
            this.translitChars.put("ấ", "a");
            this.translitChars.put("ǩ", "k");
            this.translitChars.put("ĩ", "i");
            this.translitChars.put("ṵ", "u");
            this.translitChars.put("ŧ", "t");
            this.translitChars.put("ɾ", "r");
            this.translitChars.put("ƙ", "k");
            this.translitChars.put("ṫ", "t");
            this.translitChars.put("ꝗ", "q");
            this.translitChars.put("ậ", "a");
            this.translitChars.put("ʄ", "j");
            this.translitChars.put("ƚ", "l");
            this.translitChars.put("ᶂ", "f");
            this.translitChars.put("ᵴ", "s");
            this.translitChars.put("ꞃ", "r");
            this.translitChars.put("ᶌ", "v");
            this.translitChars.put("ɵ", "o");
            this.translitChars.put("ḉ", "c");
            this.translitChars.put("ᵤ", "u");
            this.translitChars.put("ẑ", "z");
            this.translitChars.put("ṹ", "u");
            this.translitChars.put("ň", "n");
            this.translitChars.put("ʍ", "w");
            this.translitChars.put("ầ", "a");
            this.translitChars.put("ǉ", "lj");
            this.translitChars.put("ɓ", "b");
            this.translitChars.put("ɼ", "r");
            this.translitChars.put("ò", "o");
            this.translitChars.put("ẘ", "w");
            this.translitChars.put("ɗ", "d");
            this.translitChars.put("ꜽ", "ay");
            this.translitChars.put("ư", "u");
            this.translitChars.put("ᶀ", "b");
            this.translitChars.put("ǜ", "u");
            this.translitChars.put("ẹ", "e");
            this.translitChars.put("ǡ", "a");
            this.translitChars.put("ɥ", "h");
            this.translitChars.put("ṏ", "o");
            this.translitChars.put("ǔ", "u");
            this.translitChars.put("ʎ", "y");
            this.translitChars.put("ȱ", "o");
            this.translitChars.put("ệ", "e");
            this.translitChars.put("ế", "e");
            this.translitChars.put("ĭ", "i");
            this.translitChars.put("ⱸ", "e");
            this.translitChars.put("ṯ", "t");
            this.translitChars.put("ᶑ", "d");
            this.translitChars.put("ḧ", "h");
            this.translitChars.put("ṥ", "s");
            this.translitChars.put("ë", "e");
            this.translitChars.put("ᴍ", "m");
            this.translitChars.put("ö", "o");
            this.translitChars.put("é", "e");
            this.translitChars.put("ı", "i");
            this.translitChars.put("ď", "d");
            this.translitChars.put("ᵯ", "m");
            this.translitChars.put("ỵ", "y");
            this.translitChars.put("ŵ", "w");
            this.translitChars.put("ề", "e");
            this.translitChars.put("ứ", "u");
            this.translitChars.put("ƶ", "z");
            this.translitChars.put("ĵ", "j");
            this.translitChars.put("ḍ", "d");
            this.translitChars.put("ŭ", "u");
            this.translitChars.put("ʝ", "j");
            this.translitChars.put("ê", "e");
            this.translitChars.put("ǚ", "u");
            this.translitChars.put("ġ", "g");
            this.translitChars.put("ṙ", "r");
            this.translitChars.put("ƞ", "n");
            this.translitChars.put("ḗ", "e");
            this.translitChars.put("ẝ", "s");
            this.translitChars.put("ᶁ", "d");
            this.translitChars.put("ķ", "k");
            this.translitChars.put("ᴂ", "ae");
            this.translitChars.put("ɘ", "e");
            this.translitChars.put("ợ", "o");
            this.translitChars.put("ḿ", "m");
            this.translitChars.put("ꜰ", "f");
            this.translitChars.put("ẵ", "a");
            this.translitChars.put("ꝏ", "oo");
            this.translitChars.put("ᶆ", "m");
            this.translitChars.put("ᵽ", "p");
            this.translitChars.put("ữ", "u");
            this.translitChars.put("ⱪ", "k");
            this.translitChars.put("ḥ", "h");
            this.translitChars.put("ţ", "t");
            this.translitChars.put("ᵱ", "p");
            this.translitChars.put("ṁ", "m");
            this.translitChars.put("á", "a");
            this.translitChars.put("ᴎ", "n");
            this.translitChars.put("ꝟ", "v");
            this.translitChars.put("è", "e");
            this.translitChars.put("ᶎ", "z");
            this.translitChars.put("ꝺ", "d");
            this.translitChars.put("ᶈ", "p");
            this.translitChars.put("ɫ", "l");
            this.translitChars.put("ᴢ", "z");
            this.translitChars.put("ɱ", "m");
            this.translitChars.put("ṝ", "r");
            this.translitChars.put("ṽ", "v");
            this.translitChars.put("ũ", "u");
            this.translitChars.put("ß", "ss");
            this.translitChars.put("ĥ", "h");
            this.translitChars.put("ᵵ", "t");
            this.translitChars.put("ʐ", "z");
            this.translitChars.put("ṟ", "r");
            this.translitChars.put("ɲ", "n");
            this.translitChars.put("à", "a");
            this.translitChars.put("ẙ", "y");
            this.translitChars.put("ỳ", "y");
            this.translitChars.put("ᴔ", "oe");
            this.translitChars.put("ₓ", "x");
            this.translitChars.put("ȗ", "u");
            this.translitChars.put("ⱼ", "j");
            this.translitChars.put("ẫ", "a");
            this.translitChars.put("ʑ", "z");
            this.translitChars.put("ẛ", "s");
            this.translitChars.put("ḭ", "i");
            this.translitChars.put("ꜵ", "ao");
            this.translitChars.put("ɀ", "z");
            this.translitChars.put("ÿ", "y");
            this.translitChars.put("ǝ", "e");
            this.translitChars.put("ǭ", "o");
            this.translitChars.put("ᴅ", "d");
            this.translitChars.put("ᶅ", "l");
            this.translitChars.put("ù", "u");
            this.translitChars.put("ạ", "a");
            this.translitChars.put("ḅ", "b");
            this.translitChars.put("ụ", "u");
            this.translitChars.put("ằ", "a");
            this.translitChars.put("ᴛ", "t");
            this.translitChars.put("ƴ", "y");
            this.translitChars.put("ⱦ", "t");
            this.translitChars.put("ⱡ", "l");
            this.translitChars.put("ȷ", "j");
            this.translitChars.put("ᵶ", "z");
            this.translitChars.put("ḫ", "h");
            this.translitChars.put("ⱳ", "w");
            this.translitChars.put("ḵ", "k");
            this.translitChars.put("ờ", "o");
            this.translitChars.put("î", "i");
            this.translitChars.put("ģ", "g");
            this.translitChars.put("ȅ", "e");
            this.translitChars.put("ȧ", "a");
            this.translitChars.put("ẳ", "a");
            this.translitChars.put("ɋ", "q");
            this.translitChars.put("ṭ", "t");
            this.translitChars.put("ꝸ", "um");
            this.translitChars.put("ᴄ", "c");
            this.translitChars.put("ẍ", "x");
            this.translitChars.put("ủ", "u");
            this.translitChars.put("ỉ", "i");
            this.translitChars.put("ᴚ", "r");
            this.translitChars.put("ś", "s");
            this.translitChars.put("ꝋ", "o");
            this.translitChars.put("ỹ", "y");
            this.translitChars.put("ṡ", "s");
            this.translitChars.put("ǌ", "nj");
            this.translitChars.put("ȁ", "a");
            this.translitChars.put("ẗ", "t");
            this.translitChars.put("ĺ", "l");
            this.translitChars.put("ž", "z");
            this.translitChars.put("ᵺ", "th");
            this.translitChars.put("ƌ", "d");
            this.translitChars.put("ș", "s");
            this.translitChars.put("š", "s");
            this.translitChars.put("ᶙ", "u");
            this.translitChars.put("ẽ", "e");
            this.translitChars.put("ẜ", "s");
            this.translitChars.put("ɇ", "e");
            this.translitChars.put("ṷ", "u");
            this.translitChars.put("ố", "o");
            this.translitChars.put("ȿ", "s");
            this.translitChars.put("ᴠ", "v");
            this.translitChars.put("ꝭ", "is");
            this.translitChars.put("ᴏ", "o");
            this.translitChars.put("ɛ", "e");
            this.translitChars.put("ǻ", "a");
            this.translitChars.put("ﬄ", "ffl");
            this.translitChars.put("ⱺ", "o");
            this.translitChars.put("ȋ", "i");
            this.translitChars.put("ᵫ", "ue");
            this.translitChars.put("ȡ", "d");
            this.translitChars.put("ⱬ", "z");
            this.translitChars.put("ẁ", "w");
            this.translitChars.put("ᶏ", "a");
            this.translitChars.put("ꞇ", "t");
            this.translitChars.put("ğ", "g");
            this.translitChars.put("ɳ", "n");
            this.translitChars.put("ʛ", "g");
            this.translitChars.put("ᴜ", "u");
            this.translitChars.put("ẩ", "a");
            this.translitChars.put("ṅ", "n");
            this.translitChars.put("ɨ", "i");
            this.translitChars.put("ᴙ", "r");
            this.translitChars.put("ǎ", "a");
            this.translitChars.put("ſ", "s");
            this.translitChars.put("ȫ", "o");
            this.translitChars.put("ɿ", "r");
            this.translitChars.put("ƭ", "t");
            this.translitChars.put("ḯ", "i");
            this.translitChars.put("ǽ", "ae");
            this.translitChars.put("ⱱ", "v");
            this.translitChars.put("ɶ", "oe");
            this.translitChars.put("ṃ", "m");
            this.translitChars.put("ż", "z");
            this.translitChars.put("ĕ", "e");
            this.translitChars.put("ꜻ", "av");
            this.translitChars.put("ở", "o");
            this.translitChars.put("ễ", "e");
            this.translitChars.put("ɬ", "l");
            this.translitChars.put("ị", "i");
            this.translitChars.put("ᵭ", "d");
            this.translitChars.put("ﬆ", "st");
            this.translitChars.put("ḷ", "l");
            this.translitChars.put("ŕ", "r");
            this.translitChars.put("ᴕ", "ou");
            this.translitChars.put("ʈ", "t");
            this.translitChars.put("ā", "a");
            this.translitChars.put("ḙ", "e");
            this.translitChars.put("ᴑ", "o");
            this.translitChars.put("ç", "c");
            this.translitChars.put("ᶊ", "s");
            this.translitChars.put("ặ", "a");
            this.translitChars.put("ų", "u");
            this.translitChars.put("ả", "a");
            this.translitChars.put("ǥ", "g");
            this.translitChars.put("ꝁ", "k");
            this.translitChars.put("ẕ", "z");
            this.translitChars.put("ŝ", "s");
            this.translitChars.put("ḕ", "e");
            this.translitChars.put("ɠ", "g");
            this.translitChars.put("ꝉ", "l");
            this.translitChars.put("ꝼ", "f");
            this.translitChars.put("ᶍ", "x");
            this.translitChars.put("ǒ", "o");
            this.translitChars.put("ę", "e");
            this.translitChars.put("ổ", "o");
            this.translitChars.put("ƫ", "t");
            this.translitChars.put("ǫ", "o");
            this.translitChars.put("i̇", "i");
            this.translitChars.put("ṇ", "n");
            this.translitChars.put("ć", "c");
            this.translitChars.put("ᵷ", "g");
            this.translitChars.put("ẅ", "w");
            this.translitChars.put("ḑ", "d");
            this.translitChars.put("ḹ", "l");
            this.translitChars.put("œ", "oe");
            this.translitChars.put("ᵳ", "r");
            this.translitChars.put("ļ", "l");
            this.translitChars.put("ȑ", "r");
            this.translitChars.put("ȭ", "o");
            this.translitChars.put("ᵰ", "n");
            this.translitChars.put("ᴁ", "ae");
            this.translitChars.put("ŀ", "l");
            this.translitChars.put("ä", "a");
            this.translitChars.put("ƥ", "p");
            this.translitChars.put("ỏ", "o");
            this.translitChars.put("į", "i");
            this.translitChars.put("ȓ", "r");
            this.translitChars.put("ǆ", "dz");
            this.translitChars.put("ḡ", "g");
            this.translitChars.put("ṻ", "u");
            this.translitChars.put("ō", "o");
            this.translitChars.put("ľ", "l");
            this.translitChars.put("ẃ", "w");
            this.translitChars.put("ț", "t");
            this.translitChars.put("ń", "n");
            this.translitChars.put("ɍ", "r");
            this.translitChars.put("ȃ", "a");
            this.translitChars.put("ü", "u");
            this.translitChars.put("ꞁ", "l");
            this.translitChars.put("ᴐ", "o");
            this.translitChars.put("ớ", "o");
            this.translitChars.put("ᴃ", "b");
            this.translitChars.put("ɹ", "r");
            this.translitChars.put("ᵲ", "r");
            this.translitChars.put("ʏ", "y");
            this.translitChars.put("ᵮ", "f");
            this.translitChars.put("ⱨ", "h");
            this.translitChars.put("ŏ", "o");
            this.translitChars.put("ú", "u");
            this.translitChars.put("ṛ", "r");
            this.translitChars.put("ʮ", "h");
            this.translitChars.put("ó", "o");
            this.translitChars.put("ů", "u");
            this.translitChars.put("ỡ", "o");
            this.translitChars.put("ṕ", "p");
            this.translitChars.put("ᶖ", "i");
            this.translitChars.put("ự", "u");
            this.translitChars.put("ã", "a");
            this.translitChars.put("ᵢ", "i");
            this.translitChars.put("ṱ", "t");
            this.translitChars.put("ể", "e");
            this.translitChars.put("ử", "u");
            this.translitChars.put("í", "i");
            this.translitChars.put("ɔ", "o");
            this.translitChars.put("ɺ", "r");
            this.translitChars.put("ɢ", "g");
            this.translitChars.put("ř", "r");
            this.translitChars.put("ẖ", "h");
            this.translitChars.put("ű", "u");
            this.translitChars.put("ȍ", "o");
            this.translitChars.put("ḻ", "l");
            this.translitChars.put("ḣ", "h");
            this.translitChars.put("ȶ", "t");
            this.translitChars.put("ņ", "n");
            this.translitChars.put("ᶒ", "e");
            this.translitChars.put("ì", "i");
            this.translitChars.put("ẉ", "w");
            this.translitChars.put("ē", "e");
            this.translitChars.put("ᴇ", "e");
            this.translitChars.put("ł", "l");
            this.translitChars.put("ộ", "o");
            this.translitChars.put("ɭ", "l");
            this.translitChars.put("ẏ", "y");
            this.translitChars.put("ᴊ", "j");
            this.translitChars.put("ḱ", "k");
            this.translitChars.put("ṿ", "v");
            this.translitChars.put("ȩ", "e");
            this.translitChars.put("â", "a");
            this.translitChars.put("ş", "s");
            this.translitChars.put("ŗ", "r");
            this.translitChars.put("ʋ", "v");
            this.translitChars.put("ₐ", "a");
            this.translitChars.put("ↄ", "c");
            this.translitChars.put("ᶓ", "e");
            this.translitChars.put("ɰ", "m");
            this.translitChars.put("ᴡ", "w");
            this.translitChars.put("ȏ", "o");
            this.translitChars.put("č", "c");
            this.translitChars.put("ǵ", "g");
            this.translitChars.put("ĉ", "c");
            this.translitChars.put("ᶗ", "o");
            this.translitChars.put("ꝃ", "k");
            this.translitChars.put("ꝙ", "q");
            this.translitChars.put("ṑ", "o");
            this.translitChars.put("ꜱ", "s");
            this.translitChars.put("ṓ", "o");
            this.translitChars.put("ȟ", "h");
            this.translitChars.put("ő", "o");
            this.translitChars.put("ꜩ", "tz");
            this.translitChars.put("ẻ", "e");
         }

         StringBuilder var4 = new StringBuilder(var1.length());
         int var5 = var1.length();
         boolean var6 = false;

         int var8;
         for(int var7 = 0; var7 < var5; var7 = var8) {
            var8 = var7 + 1;
            String var9 = var1.substring(var7, var8);
            String var10;
            if (var3) {
               var10 = var9.toLowerCase();
               var6 = var9.equals(var10) ^ true;
            } else {
               var10 = var9;
            }

            String var11 = (String)this.translitChars.get(var10);
            var9 = var11;
            if (var11 == null) {
               var9 = var11;
               if (var2) {
                  var9 = (String)this.ruTranslitChars.get(var10);
               }
            }

            if (var9 != null) {
               var10 = var9;
               if (var3) {
                  var10 = var9;
                  if (var6) {
                     if (var9.length() > 1) {
                        StringBuilder var13 = new StringBuilder();
                        var13.append(var9.substring(0, 1).toUpperCase());
                        var13.append(var9.substring(1));
                        var10 = var13.toString();
                     } else {
                        var10 = var9.toUpperCase();
                     }
                  }
               }

               var4.append(var10);
            } else {
               var9 = var10;
               if (var3) {
                  char var12 = var10.charAt(0);
                  if ((var12 < 'a' || var12 > 'z' || var12 < '0' || var12 > '9') && var12 != ' ' && var12 != '\'' && var12 != ',' && var12 != '.' && var12 != '&' && var12 != '-' && var12 != '/') {
                     return null;
                  }

                  var9 = var10;
                  if (var6) {
                     var9 = var10.toUpperCase();
                  }
               }

               var4.append(var9);
            }
         }

         return var4.toString();
      }
   }

   public boolean isCurrentLocalLocale() {
      return this.currentLocaleInfo.isLocal();
   }

   // $FF: synthetic method
   public void lambda$applyLanguage$2$LocaleController(LocaleController.LocaleInfo var1, int var2) {
      this.applyRemoteLanguage(var1, (String)null, true, var2);
   }

   // $FF: synthetic method
   public void lambda$applyLanguage$3$LocaleController(int var1) {
      this.reloadCurrentRemoteLocale(var1, (String)null);
   }

   // $FF: synthetic method
   public void lambda$applyRemoteLanguage$10$LocaleController(LocaleController.LocaleInfo var1, int var2, TLObject var3, TLRPC.TL_error var4) {
      if (var3 != null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$LocaleController$e6CsiR21q6PI2LDNrAzXcAWuMTk(this, var1, var3, var2));
      }

   }

   // $FF: synthetic method
   public void lambda$applyRemoteLanguage$12$LocaleController(LocaleController.LocaleInfo var1, int var2, TLObject var3, TLRPC.TL_error var4) {
      if (var3 != null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$LocaleController$BieyCwrY21__jPaUyC1vcjWcFcw(this, var1, var3, var2));
      }

   }

   // $FF: synthetic method
   public void lambda$applyRemoteLanguage$14$LocaleController(LocaleController.LocaleInfo var1, int var2, TLObject var3, TLRPC.TL_error var4) {
      if (var3 != null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$LocaleController$9Gyg_n_UT33Nf2VkVVHA1KrlD90(this, var1, var3, var2));
      }

   }

   // $FF: synthetic method
   public void lambda$applyRemoteLanguage$8$LocaleController(LocaleController.LocaleInfo var1, int var2, TLObject var3, TLRPC.TL_error var4) {
      if (var3 != null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$LocaleController$sjCg_VQEumFUYAgaZUDI3dk_dz4(this, var1, var3, var2));
      }

   }

   // $FF: synthetic method
   public void lambda$loadRemoteLanguages$6$LocaleController(int var1, TLObject var2, TLRPC.TL_error var3) {
      if (var2 != null) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$LocaleController$Y6efM2co6N3bq4nUuZDi5VasNV4(this, var2, var1));
      }

   }

   // $FF: synthetic method
   public void lambda$new$0$LocaleController() {
      this.loadRemoteLanguages(UserConfig.selectedAccount);
   }

   // $FF: synthetic method
   public void lambda$new$1$LocaleController() {
      this.currentSystemLocale = getSystemLocaleStringIso639();
   }

   // $FF: synthetic method
   public void lambda$null$11$LocaleController(LocaleController.LocaleInfo var1, TLObject var2, int var3) {
      this.saveRemoteLocaleStrings(var1, (TLRPC.TL_langPackDifference)var2, var3);
   }

   // $FF: synthetic method
   public void lambda$null$13$LocaleController(LocaleController.LocaleInfo var1, TLObject var2, int var3) {
      this.saveRemoteLocaleStrings(var1, (TLRPC.TL_langPackDifference)var2, var3);
   }

   // $FF: synthetic method
   public void lambda$null$5$LocaleController(TLObject var1, int var2) {
      this.loadingRemoteLanguages = false;
      TLRPC.Vector var3 = (TLRPC.Vector)var1;
      int var4 = this.remoteLanguages.size();

      int var5;
      for(var5 = 0; var5 < var4; ++var5) {
         ((LocaleController.LocaleInfo)this.remoteLanguages.get(var5)).serverIndex = Integer.MAX_VALUE;
      }

      var4 = var3.objects.size();

      StringBuilder var8;
      LocaleController.LocaleInfo var10;
      for(var5 = 0; var5 < var4; ++var5) {
         TLRPC.TL_langPackLanguage var6 = (TLRPC.TL_langPackLanguage)var3.objects.get(var5);
         if (BuildVars.LOGS_ENABLED) {
            var8 = new StringBuilder();
            var8.append("loaded lang ");
            var8.append(var6.name);
            FileLog.d(var8.toString());
         }

         LocaleController.LocaleInfo var9 = new LocaleController.LocaleInfo();
         var9.nameEnglish = var6.name;
         var9.name = var6.native_name;
         var9.shortName = var6.lang_code.replace('-', '_').toLowerCase();
         String var7 = var6.base_lang_code;
         if (var7 != null) {
            var9.baseLangCode = var7.replace('-', '_').toLowerCase();
         } else {
            var9.baseLangCode = "";
         }

         var9.pluralLangCode = var6.plural_code.replace('-', '_').toLowerCase();
         var9.isRtl = var6.rtl;
         var9.pathToFile = "remote";
         var9.serverIndex = var5;
         var10 = this.getLanguageFromDict(var9.getKey());
         if (var10 == null) {
            this.languages.add(var9);
            this.languagesDict.put(var9.getKey(), var9);
         } else {
            var10.nameEnglish = var9.nameEnglish;
            var10.name = var9.name;
            var10.baseLangCode = var9.baseLangCode;
            var10.pluralLangCode = var9.pluralLangCode;
            var10.pathToFile = var9.pathToFile;
            var10.serverIndex = var9.serverIndex;
            var9 = var10;
         }

         if (!this.remoteLanguagesDict.containsKey(var9.getKey())) {
            this.remoteLanguages.add(var9);
            this.remoteLanguagesDict.put(var9.getKey(), var9);
         }
      }

      for(var5 = 0; var5 < this.remoteLanguages.size(); var5 = var4 + 1) {
         var10 = (LocaleController.LocaleInfo)this.remoteLanguages.get(var5);
         var4 = var5;
         if (var10.serverIndex == Integer.MAX_VALUE) {
            if (var10 == this.currentLocaleInfo) {
               var4 = var5;
            } else {
               if (BuildVars.LOGS_ENABLED) {
                  var8 = new StringBuilder();
                  var8.append("remove lang ");
                  var8.append(var10.getKey());
                  FileLog.d(var8.toString());
               }

               this.remoteLanguages.remove(var5);
               this.remoteLanguagesDict.remove(var10.getKey());
               this.languages.remove(var10);
               this.languagesDict.remove(var10.getKey());
               var4 = var5 - 1;
            }
         }
      }

      this.saveOtherLanguages();
      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.suggestedLangpack);
      this.applyLanguage(this.currentLocaleInfo, true, false, var2);
   }

   // $FF: synthetic method
   public void lambda$null$7$LocaleController(LocaleController.LocaleInfo var1, TLObject var2, int var3) {
      this.saveRemoteLocaleStrings(var1, (TLRPC.TL_langPackDifference)var2, var3);
   }

   // $FF: synthetic method
   public void lambda$null$9$LocaleController(LocaleController.LocaleInfo var1, TLObject var2, int var3) {
      this.saveRemoteLocaleStrings(var1, (TLRPC.TL_langPackDifference)var2, var3);
   }

   // $FF: synthetic method
   public void lambda$saveRemoteLocaleStrings$4$LocaleController(LocaleController.LocaleInfo var1, int var2, TLRPC.TL_langPackDifference var3, HashMap var4) {
      if (var1 != null) {
         if (var2 == 0) {
            var1.version = var3.version;
         } else {
            var1.baseVersion = var3.version;
         }
      }

      this.saveOtherLanguages();

      label96: {
         Exception var10000;
         label102: {
            boolean var5;
            boolean var10001;
            try {
               if (this.currentLocaleInfo != var1) {
                  break label96;
               }

               var5 = TextUtils.isEmpty(var1.pluralLangCode);
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
               break label102;
            }

            String[] var18;
            if (!var5) {
               try {
                  var18 = var1.pluralLangCode.split("_");
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label102;
               }
            } else {
               label103: {
                  try {
                     if (!TextUtils.isEmpty(var1.baseLangCode)) {
                        var18 = var1.baseLangCode.split("_");
                        break label103;
                     }
                  } catch (Exception var14) {
                     var10000 = var14;
                     var10001 = false;
                     break label102;
                  }

                  try {
                     var18 = var1.shortName.split("_");
                  } catch (Exception var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label102;
                  }
               }
            }

            Locale var19;
            label78: {
               Locale var6;
               label77: {
                  try {
                     if (var18.length == 1) {
                        var6 = new Locale(var18[0]);
                        break label77;
                     }
                  } catch (Exception var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label102;
                  }

                  try {
                     var19 = new Locale(var18[0], var18[1]);
                     break label78;
                  } catch (Exception var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label102;
                  }
               }

               var19 = var6;
            }

            try {
               this.languageOverride = var1.shortName;
               Editor var20 = MessagesController.getGlobalMainSettings().edit();
               var20.putString("language", var1.getKey());
               var20.commit();
               this.localeValues = var4;
               this.currentLocale = var19;
               this.currentLocaleInfo = var1;
               if (this.currentLocaleInfo != null && !TextUtils.isEmpty(this.currentLocaleInfo.pluralLangCode)) {
                  this.currentPluralRules = (LocaleController.PluralRules)this.allRules.get(this.currentLocaleInfo.pluralLangCode);
               }
            } catch (Exception var9) {
               var10000 = var9;
               var10001 = false;
               break label102;
            }

            try {
               if (this.currentPluralRules == null) {
                  this.currentPluralRules = (LocaleController.PluralRules)this.allRules.get(this.currentLocale.getLanguage());
                  if (this.currentPluralRules == null) {
                     this.currentPluralRules = (LocaleController.PluralRules)this.allRules.get("en");
                  }
               }
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label102;
            }

            try {
               this.changingConfiguration = true;
               Locale.setDefault(this.currentLocale);
               Configuration var17 = new Configuration();
               var17.locale = this.currentLocale;
               ApplicationLoader.applicationContext.getResources().updateConfiguration(var17, ApplicationLoader.applicationContext.getResources().getDisplayMetrics());
               this.changingConfiguration = false;
               break label96;
            } catch (Exception var7) {
               var10000 = var7;
               var10001 = false;
            }
         }

         Exception var16 = var10000;
         FileLog.e((Throwable)var16);
         this.changingConfiguration = false;
      }

      this.recreateFormatters();
      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.reloadInterface);
   }

   public void loadRemoteLanguages(int var1) {
      if (!this.loadingRemoteLanguages) {
         this.loadingRemoteLanguages = true;
         TLRPC.TL_langpack_getLanguages var2 = new TLRPC.TL_langpack_getLanguages();
         ConnectionsManager.getInstance(var1).sendRequest(var2, new _$$Lambda$LocaleController$OO_St8W4lBDCp1N4EzTA2EggA1M(this, var1), 8);
      }
   }

   public void onDeviceConfigurationChange(Configuration var1) {
      if (!this.changingConfiguration) {
         is24HourFormat = DateFormat.is24HourFormat(ApplicationLoader.applicationContext);
         Locale var2 = var1.locale;
         this.systemDefaultLocale = var2;
         String var3;
         LocaleController.LocaleInfo var4;
         String var5;
         if (this.languageOverride != null) {
            var4 = this.currentLocaleInfo;
            this.currentLocaleInfo = null;
            this.applyLanguage(var4, false, false, UserConfig.selectedAccount);
         } else if (var2 != null) {
            var3 = var2.getDisplayName();
            var5 = this.currentLocale.getDisplayName();
            if (var3 != null && var5 != null && !var3.equals(var5)) {
               this.recreateFormatters();
            }

            this.currentLocale = var2;
            var4 = this.currentLocaleInfo;
            if (var4 != null && !TextUtils.isEmpty(var4.pluralLangCode)) {
               this.currentPluralRules = (LocaleController.PluralRules)this.allRules.get(this.currentLocaleInfo.pluralLangCode);
            }

            if (this.currentPluralRules == null) {
               this.currentPluralRules = (LocaleController.PluralRules)this.allRules.get(this.currentLocale.getLanguage());
               if (this.currentPluralRules == null) {
                  this.currentPluralRules = (LocaleController.PluralRules)this.allRules.get("en");
               }
            }
         }

         var3 = getSystemLocaleStringIso639();
         var5 = this.currentSystemLocale;
         if (var5 != null && !var3.equals(var5)) {
            this.currentSystemLocale = var3;
            ConnectionsManager.setSystemLangCode(this.currentSystemLocale);
         }

      }
   }

   public void recreateFormatters() {
      Locale var1 = this.currentLocale;
      Locale var2 = var1;
      if (var1 == null) {
         var2 = Locale.getDefault();
      }

      String var3 = var2.getLanguage();
      String var8 = var3;
      if (var3 == null) {
         var8 = "en";
      }

      byte var5;
      boolean var6;
      label106: {
         var8 = var8.toLowerCase();
         int var4 = var8.length();
         var5 = 1;
         if ((var4 != 2 || !var8.equals("ar") && !var8.equals("fa") && !var8.equals("he") && !var8.equals("iw")) && !var8.startsWith("ar_") && !var8.startsWith("fa_") && !var8.startsWith("he_") && !var8.startsWith("iw_")) {
            LocaleController.LocaleInfo var9 = this.currentLocaleInfo;
            if (var9 == null || !var9.isRtl) {
               var6 = false;
               break label106;
            }
         }

         var6 = true;
      }

      isRTL = var6;
      if (var8.equals("ko")) {
         var5 = 2;
      }

      nameDisplayOrder = var5;
      this.formatterDayMonth = this.createFormatter(var2, this.getStringInternal("formatterMonth", 2131561223), "dd MMM");
      this.formatterYear = this.createFormatter(var2, this.getStringInternal("formatterYear", 2131561229), "dd.MM.yy");
      this.formatterYearMax = this.createFormatter(var2, this.getStringInternal("formatterYearMax", 2131561230), "dd.MM.yyyy");
      this.chatDate = this.createFormatter(var2, this.getStringInternal("chatDate", 2131561204), "d MMMM");
      this.chatFullDate = this.createFormatter(var2, this.getStringInternal("chatFullDate", 2131561205), "d MMMM yyyy");
      this.formatterWeek = this.createFormatter(var2, this.getStringInternal("formatterWeek", 2131561228), "EEE");
      this.formatterScheduleDay = this.createFormatter(var2, this.getStringInternal("formatDateScheduleDay", 2131561211), "EEE MMM d");
      if (!var8.toLowerCase().equals("ar") && !var8.toLowerCase().equals("ko")) {
         var1 = Locale.US;
      } else {
         var1 = var2;
      }

      int var10;
      if (is24HourFormat) {
         var10 = 2131561222;
         var3 = "formatterDay24H";
      } else {
         var10 = 2131561221;
         var3 = "formatterDay12H";
      }

      String var7 = this.getStringInternal(var3, var10);
      if (is24HourFormat) {
         var3 = "HH:mm";
      } else {
         var3 = "h:mm a";
      }

      this.formatterDay = this.createFormatter(var1, var7, var3);
      if (is24HourFormat) {
         var10 = 2131561227;
         var8 = "formatterStats24H";
      } else {
         var10 = 2131561226;
         var8 = "formatterStats12H";
      }

      var7 = this.getStringInternal(var8, var10);
      var6 = is24HourFormat;
      var3 = "MMM dd yyyy, HH:mm";
      if (var6) {
         var8 = "MMM dd yyyy, HH:mm";
      } else {
         var8 = "MMM dd yyyy, h:mm a";
      }

      this.formatterStats = this.createFormatter(var2, var7, var8);
      if (is24HourFormat) {
         var10 = 2131561218;
         var8 = "formatterBannedUntil24H";
      } else {
         var10 = 2131561217;
         var8 = "formatterBannedUntil12H";
      }

      var7 = this.getStringInternal(var8, var10);
      if (is24HourFormat) {
         var8 = var3;
      } else {
         var8 = "MMM dd yyyy, h:mm a";
      }

      this.formatterBannedUntil = this.createFormatter(var2, var7, var8);
      if (is24HourFormat) {
         var10 = 2131561220;
         var8 = "formatterBannedUntilThisYear24H";
      } else {
         var10 = 2131561219;
         var8 = "formatterBannedUntilThisYear12H";
      }

      var3 = this.getStringInternal(var8, var10);
      if (is24HourFormat) {
         var8 = "MMM dd, HH:mm";
      } else {
         var8 = "MMM dd, h:mm a";
      }

      this.formatterBannedUntilThisYear = this.createFormatter(var2, var3, var8);
   }

   public void reloadCurrentRemoteLocale(int var1, String var2) {
      String var3 = var2;
      if (var2 != null) {
         var3 = var2.replace("-", "_");
      }

      if (var3 != null) {
         LocaleController.LocaleInfo var4 = this.currentLocaleInfo;
         if (var4 == null || !var3.equals(var4.shortName) && !var3.equals(this.currentLocaleInfo.baseLangCode)) {
            return;
         }
      }

      this.applyRemoteLanguage(this.currentLocaleInfo, var3, true, var1);
   }

   public void saveRemoteLocaleStrings(LocaleController.LocaleInfo var1, TLRPC.TL_langPackDifference var2, int var3) {
      if (var2 != null && !var2.strings.isEmpty() && var1 != null && !var1.isLocal()) {
         String var4 = var2.lang_code.replace('-', '_').toLowerCase();
         byte var34;
         if (var4.equals(var1.shortName)) {
            var34 = 0;
         } else if (var4.equals(var1.baseLangCode)) {
            var34 = 1;
         } else {
            var34 = -1;
         }

         if (var34 == -1) {
            return;
         }

         File var35;
         if (var34 == 0) {
            var35 = var1.getPathToFile();
         } else {
            var35 = var1.getPathToBaseFile();
         }

         HashMap var5;
         boolean var10001;
         label238: {
            try {
               if (var2.from_version == 0) {
                  var5 = new HashMap();
                  break label238;
               }
            } catch (Exception var33) {
               var10001 = false;
               return;
            }

            try {
               var5 = this.getLocaleFileStrings(var35, true);
            } catch (Exception var32) {
               var10001 = false;
               return;
            }
         }

         int var6 = 0;

         StringBuilder var8;
         while(true) {
            label252: {
               TLRPC.LangPackString var7;
               try {
                  if (var6 >= var2.strings.size()) {
                     break;
                  }

                  var7 = (TLRPC.LangPackString)var2.strings.get(var6);
                  if (var7 instanceof TLRPC.TL_langPackString) {
                     var5.put(var7.key, this.escapeString(var7.value));
                     break label252;
                  }
               } catch (Exception var31) {
                  var10001 = false;
                  return;
               }

               label255: {
                  String var40;
                  String var9;
                  try {
                     if (!(var7 instanceof TLRPC.TL_langPackStringPluralized)) {
                        break label255;
                     }

                     var8 = new StringBuilder();
                     var8.append(var7.key);
                     var8.append("_zero");
                     var9 = var8.toString();
                     var40 = var7.zero_value;
                  } catch (Exception var30) {
                     var10001 = false;
                     return;
                  }

                  String var10 = "";
                  if (var40 != null) {
                     try {
                        var40 = this.escapeString(var7.zero_value);
                     } catch (Exception var24) {
                        var10001 = false;
                        return;
                     }
                  } else {
                     var40 = "";
                  }

                  label209: {
                     try {
                        var5.put(var9, var40);
                        var8 = new StringBuilder();
                        var8.append(var7.key);
                        var8.append("_one");
                        var9 = var8.toString();
                        if (var7.one_value != null) {
                           var40 = this.escapeString(var7.one_value);
                           break label209;
                        }
                     } catch (Exception var29) {
                        var10001 = false;
                        return;
                     }

                     var40 = "";
                  }

                  label200: {
                     try {
                        var5.put(var9, var40);
                        var8 = new StringBuilder();
                        var8.append(var7.key);
                        var8.append("_two");
                        var9 = var8.toString();
                        if (var7.two_value != null) {
                           var40 = this.escapeString(var7.two_value);
                           break label200;
                        }
                     } catch (Exception var28) {
                        var10001 = false;
                        return;
                     }

                     var40 = "";
                  }

                  label191: {
                     try {
                        var5.put(var9, var40);
                        var8 = new StringBuilder();
                        var8.append(var7.key);
                        var8.append("_few");
                        var9 = var8.toString();
                        if (var7.few_value != null) {
                           var40 = this.escapeString(var7.few_value);
                           break label191;
                        }
                     } catch (Exception var27) {
                        var10001 = false;
                        return;
                     }

                     var40 = "";
                  }

                  label182: {
                     try {
                        var5.put(var9, var40);
                        var8 = new StringBuilder();
                        var8.append(var7.key);
                        var8.append("_many");
                        var9 = var8.toString();
                        if (var7.many_value != null) {
                           var40 = this.escapeString(var7.many_value);
                           break label182;
                        }
                     } catch (Exception var26) {
                        var10001 = false;
                        return;
                     }

                     var40 = "";
                  }

                  try {
                     var5.put(var9, var40);
                     var8 = new StringBuilder();
                     var8.append(var7.key);
                     var8.append("_other");
                     var9 = var8.toString();
                  } catch (Exception var23) {
                     var10001 = false;
                     return;
                  }

                  var40 = var10;

                  try {
                     if (var7.other_value != null) {
                        var40 = this.escapeString(var7.other_value);
                     }
                  } catch (Exception var25) {
                     var10001 = false;
                     return;
                  }

                  try {
                     var5.put(var9, var40);
                     break label252;
                  } catch (Exception var22) {
                     var10001 = false;
                     return;
                  }
               }

               try {
                  if (var7 instanceof TLRPC.TL_langPackStringDeleted) {
                     var5.remove(var7.key);
                  }
               } catch (Exception var21) {
                  var10001 = false;
                  return;
               }
            }

            ++var6;
         }

         try {
            if (BuildVars.LOGS_ENABLED) {
               var8 = new StringBuilder();
               var8.append("save locale file to ");
               var8.append(var35);
               FileLog.d(var8.toString());
            }
         } catch (Exception var20) {
            var10001 = false;
            return;
         }

         Iterator var37;
         BufferedWriter var42;
         try {
            FileWriter var41 = new FileWriter(var35);
            var42 = new BufferedWriter(var41);
            var42.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
            var42.write("<resources>\n");
            var37 = var5.entrySet().iterator();
         } catch (Exception var18) {
            var10001 = false;
            return;
         }

         while(true) {
            try {
               if (!var37.hasNext()) {
                  break;
               }

               Entry var36 = (Entry)var37.next();
               var42.write(String.format("<string name=\"%1$s\">%2$s</string>\n", var36.getKey(), var36.getValue()));
            } catch (Exception var19) {
               var10001 = false;
               return;
            }
         }

         boolean var11;
         try {
            var42.write("</resources>");
            var42.close();
            var11 = var1.hasBaseLang();
         } catch (Exception var17) {
            var10001 = false;
            return;
         }

         if (var11) {
            try {
               var35 = var1.getPathToBaseFile();
            } catch (Exception var16) {
               var10001 = false;
               return;
            }
         } else {
            try {
               var35 = var1.getPathToFile();
            } catch (Exception var15) {
               var10001 = false;
               return;
            }
         }

         HashMap var38;
         try {
            var38 = this.getLocaleFileStrings(var35);
         } catch (Exception var14) {
            var10001 = false;
            return;
         }

         if (var11) {
            try {
               var38.putAll(this.getLocaleFileStrings(var1.getPathToFile()));
            } catch (Exception var13) {
               var10001 = false;
               return;
            }
         }

         try {
            _$$Lambda$LocaleController$byYfLkXSOjQxKwpHcMJU1BFCcao var39 = new _$$Lambda$LocaleController$byYfLkXSOjQxKwpHcMJU1BFCcao(this, var1, var34, var2, var38);
            AndroidUtilities.runOnUIThread(var39);
         } catch (Exception var12) {
            var10001 = false;
         }
      }

   }

   public void saveRemoteLocaleStringsForCurrentLocale(TLRPC.TL_langPackDifference var1, int var2) {
      if (this.currentLocaleInfo != null) {
         String var3 = var1.lang_code.replace('-', '_').toLowerCase();
         if (var3.equals(this.currentLocaleInfo.shortName) || var3.equals(this.currentLocaleInfo.baseLangCode)) {
            this.saveRemoteLocaleStrings(this.currentLocaleInfo, var1, var2);
         }
      }
   }

   public static class LocaleInfo {
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

      public static LocaleController.LocaleInfo createWithString(String var0) {
         LocaleController.LocaleInfo var1 = null;
         LocaleController.LocaleInfo var2 = var1;
         if (var0 != null) {
            if (var0.length() == 0) {
               var2 = var1;
            } else {
               String[] var3 = var0.split("\\|");
               var2 = var1;
               if (var3.length >= 4) {
                  var1 = new LocaleController.LocaleInfo();
                  boolean var4 = false;
                  var1.name = var3[0];
                  var1.nameEnglish = var3[1];
                  var1.shortName = var3[2].toLowerCase();
                  var1.pathToFile = var3[3];
                  if (var3.length >= 5) {
                     var1.version = Utilities.parseInt(var3[4]);
                  }

                  if (var3.length >= 6) {
                     var0 = var3[5];
                  } else {
                     var0 = "";
                  }

                  var1.baseLangCode = var0;
                  if (var3.length >= 7) {
                     var0 = var3[6];
                  } else {
                     var0 = var1.shortName;
                  }

                  var1.pluralLangCode = var0;
                  if (var3.length >= 8) {
                     if (Utilities.parseInt(var3[7]) == 1) {
                        var4 = true;
                     }

                     var1.isRtl = var4;
                  }

                  if (var3.length >= 9) {
                     var1.baseVersion = Utilities.parseInt(var3[8]);
                  }

                  if (var3.length >= 10) {
                     var1.serverIndex = Utilities.parseInt(var3[9]);
                  } else {
                     var1.serverIndex = Integer.MAX_VALUE;
                  }

                  var2 = var1;
                  if (!TextUtils.isEmpty(var1.baseLangCode)) {
                     var1.baseLangCode = var1.baseLangCode.replace("-", "_");
                     var2 = var1;
                  }
               }
            }
         }

         return var2;
      }

      public String getBaseLangCode() {
         String var1 = this.baseLangCode;
         if (var1 == null) {
            var1 = "";
         } else {
            var1 = var1.replace("_", "-");
         }

         return var1;
      }

      public String getKey() {
         StringBuilder var1;
         if (this.pathToFile != null && !this.isRemote() && !this.isUnofficial()) {
            var1 = new StringBuilder();
            var1.append("local_");
            var1.append(this.shortName);
            return var1.toString();
         } else if (this.isUnofficial()) {
            var1 = new StringBuilder();
            var1.append("unofficial_");
            var1.append(this.shortName);
            return var1.toString();
         } else {
            return this.shortName;
         }
      }

      public String getLangCode() {
         return this.shortName.replace("_", "-");
      }

      public File getPathToBaseFile() {
         if (this.isUnofficial()) {
            File var1 = ApplicationLoader.getFilesDirFixed();
            StringBuilder var2 = new StringBuilder();
            var2.append("unofficial_base_");
            var2.append(this.shortName);
            var2.append(".xml");
            return new File(var1, var2.toString());
         } else {
            return null;
         }
      }

      public File getPathToFile() {
         File var1;
         StringBuilder var2;
         if (this.isRemote()) {
            var1 = ApplicationLoader.getFilesDirFixed();
            var2 = new StringBuilder();
            var2.append("remote_");
            var2.append(this.shortName);
            var2.append(".xml");
            return new File(var1, var2.toString());
         } else if (this.isUnofficial()) {
            var1 = ApplicationLoader.getFilesDirFixed();
            var2 = new StringBuilder();
            var2.append("unofficial_");
            var2.append(this.shortName);
            var2.append(".xml");
            return new File(var1, var2.toString());
         } else {
            if (!TextUtils.isEmpty(this.pathToFile)) {
               var1 = new File(this.pathToFile);
            } else {
               var1 = null;
            }

            return var1;
         }
      }

      public String getSaveString() {
         String var1 = this.baseLangCode;
         String var2 = var1;
         if (var1 == null) {
            var2 = "";
         }

         if (TextUtils.isEmpty(this.pluralLangCode)) {
            var1 = this.shortName;
         } else {
            var1 = this.pluralLangCode;
         }

         StringBuilder var3 = new StringBuilder();
         var3.append(this.name);
         var3.append("|");
         var3.append(this.nameEnglish);
         var3.append("|");
         var3.append(this.shortName);
         var3.append("|");
         var3.append(this.pathToFile);
         var3.append("|");
         var3.append(this.version);
         var3.append("|");
         var3.append(var2);
         var3.append("|");
         var3.append(this.pluralLangCode);
         var3.append("|");
         var3.append(this.isRtl);
         var3.append("|");
         var3.append(this.baseVersion);
         var3.append("|");
         var3.append(this.serverIndex);
         return var3.toString();
      }

      public boolean hasBaseLang() {
         boolean var1;
         if (this.isUnofficial() && !TextUtils.isEmpty(this.baseLangCode) && !this.baseLangCode.equals(this.shortName)) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public boolean isBuiltIn() {
         return this.builtIn;
      }

      public boolean isLocal() {
         boolean var1;
         if (!TextUtils.isEmpty(this.pathToFile) && !this.isRemote() && !this.isUnofficial()) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public boolean isRemote() {
         return "remote".equals(this.pathToFile);
      }

      public boolean isUnofficial() {
         return "unofficial".equals(this.pathToFile);
      }
   }

   public abstract static class PluralRules {
      abstract int quantityForNumber(int var1);
   }

   public static class PluralRules_Arabic extends LocaleController.PluralRules {
      public int quantityForNumber(int var1) {
         int var2 = var1 % 100;
         if (var1 == 0) {
            return 1;
         } else if (var1 == 1) {
            return 2;
         } else if (var1 == 2) {
            return 4;
         } else if (var2 >= 3 && var2 <= 10) {
            return 8;
         } else {
            return var2 >= 11 && var2 <= 99 ? 16 : 0;
         }
      }
   }

   public static class PluralRules_Balkan extends LocaleController.PluralRules {
      public int quantityForNumber(int var1) {
         int var2 = var1 % 100;
         var1 %= 10;
         if (var1 == 1 && var2 != 11) {
            return 2;
         } else if (var1 >= 2 && var1 <= 4 && (var2 < 12 || var2 > 14)) {
            return 8;
         } else {
            return var1 != 0 && (var1 < 5 || var1 > 9) && (var2 < 11 || var2 > 14) ? 0 : 16;
         }
      }
   }

   public static class PluralRules_Breton extends LocaleController.PluralRules {
      public int quantityForNumber(int var1) {
         if (var1 == 0) {
            return 1;
         } else if (var1 == 1) {
            return 2;
         } else if (var1 == 2) {
            return 4;
         } else if (var1 == 3) {
            return 8;
         } else {
            return var1 == 6 ? 16 : 0;
         }
      }
   }

   public static class PluralRules_Czech extends LocaleController.PluralRules {
      public int quantityForNumber(int var1) {
         if (var1 == 1) {
            return 2;
         } else {
            return var1 >= 2 && var1 <= 4 ? 8 : 0;
         }
      }
   }

   public static class PluralRules_French extends LocaleController.PluralRules {
      public int quantityForNumber(int var1) {
         return var1 >= 0 && var1 < 2 ? 2 : 0;
      }
   }

   public static class PluralRules_Langi extends LocaleController.PluralRules {
      public int quantityForNumber(int var1) {
         if (var1 == 0) {
            return 1;
         } else {
            return var1 > 0 && var1 < 2 ? 2 : 0;
         }
      }
   }

   public static class PluralRules_Latvian extends LocaleController.PluralRules {
      public int quantityForNumber(int var1) {
         if (var1 == 0) {
            return 1;
         } else {
            return var1 % 10 == 1 && var1 % 100 != 11 ? 2 : 0;
         }
      }
   }

   public static class PluralRules_Lithuanian extends LocaleController.PluralRules {
      public int quantityForNumber(int var1) {
         int var2 = var1 % 100;
         var1 %= 10;
         if (var1 != 1 || var2 >= 11 && var2 <= 19) {
            return var1 < 2 || var1 > 9 || var2 >= 11 && var2 <= 19 ? 0 : 8;
         } else {
            return 2;
         }
      }
   }

   public static class PluralRules_Macedonian extends LocaleController.PluralRules {
      public int quantityForNumber(int var1) {
         return var1 % 10 == 1 && var1 != 11 ? 2 : 0;
      }
   }

   public static class PluralRules_Maltese extends LocaleController.PluralRules {
      public int quantityForNumber(int var1) {
         int var2 = var1 % 100;
         if (var1 == 1) {
            return 2;
         } else if (var1 == 0 || var2 >= 2 && var2 <= 10) {
            return 8;
         } else {
            return var2 >= 11 && var2 <= 19 ? 16 : 0;
         }
      }
   }

   public static class PluralRules_None extends LocaleController.PluralRules {
      public int quantityForNumber(int var1) {
         return 0;
      }
   }

   public static class PluralRules_One extends LocaleController.PluralRules {
      public int quantityForNumber(int var1) {
         byte var2;
         if (var1 == 1) {
            var2 = 2;
         } else {
            var2 = 0;
         }

         return var2;
      }
   }

   public static class PluralRules_Polish extends LocaleController.PluralRules {
      public int quantityForNumber(int var1) {
         int var2 = var1 % 100;
         int var3 = var1 % 10;
         if (var1 == 1) {
            return 2;
         } else if (var3 < 2 || var3 > 4 || var2 >= 12 && var2 <= 14) {
            return (var3 < 0 || var3 > 1) && (var3 < 5 || var3 > 9) && (var2 < 12 || var2 > 14) ? 0 : 16;
         } else {
            return 8;
         }
      }
   }

   public static class PluralRules_Romanian extends LocaleController.PluralRules {
      public int quantityForNumber(int var1) {
         int var2 = var1 % 100;
         if (var1 == 1) {
            return 2;
         } else {
            return var1 == 0 || var2 >= 1 && var2 <= 19 ? 8 : 0;
         }
      }
   }

   public static class PluralRules_Slovenian extends LocaleController.PluralRules {
      public int quantityForNumber(int var1) {
         var1 %= 100;
         if (var1 == 1) {
            return 2;
         } else if (var1 == 2) {
            return 4;
         } else {
            return var1 >= 3 && var1 <= 4 ? 8 : 0;
         }
      }
   }

   public static class PluralRules_Tachelhit extends LocaleController.PluralRules {
      public int quantityForNumber(int var1) {
         if (var1 >= 0 && var1 <= 1) {
            return 2;
         } else {
            return var1 >= 2 && var1 <= 10 ? 8 : 0;
         }
      }
   }

   public static class PluralRules_Two extends LocaleController.PluralRules {
      public int quantityForNumber(int var1) {
         if (var1 == 1) {
            return 2;
         } else {
            return var1 == 2 ? 4 : 0;
         }
      }
   }

   public static class PluralRules_Welsh extends LocaleController.PluralRules {
      public int quantityForNumber(int var1) {
         if (var1 == 0) {
            return 1;
         } else if (var1 == 1) {
            return 2;
         } else if (var1 == 2) {
            return 4;
         } else if (var1 == 3) {
            return 8;
         } else {
            return var1 == 6 ? 16 : 0;
         }
      }
   }

   public static class PluralRules_Zero extends LocaleController.PluralRules {
      public int quantityForNumber(int var1) {
         return var1 != 0 && var1 != 1 ? 0 : 2;
      }
   }

   private class TimeZoneChangedReceiver extends BroadcastReceiver {
      private TimeZoneChangedReceiver() {
      }

      // $FF: synthetic method
      TimeZoneChangedReceiver(Object var2) {
         this();
      }

      // $FF: synthetic method
      public void lambda$onReceive$0$LocaleController$TimeZoneChangedReceiver() {
         if (!LocaleController.this.formatterDayMonth.getTimeZone().equals(TimeZone.getDefault())) {
            LocaleController.getInstance().recreateFormatters();
         }

      }

      public void onReceive(Context var1, Intent var2) {
         ApplicationLoader.applicationHandler.post(new _$$Lambda$LocaleController$TimeZoneChangedReceiver$_tF936vTwTBeR7FivZoJHGJKUvY(this));
      }
   }
}

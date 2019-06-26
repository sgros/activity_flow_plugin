// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.os.Handler;
import android.content.SharedPreferences$Editor;
import android.database.ContentObserver;
import android.provider.ContactsContract$Contacts;
import android.content.ContentProviderOperation$Builder;
import android.provider.ContactsContract$Data;
import android.provider.ContactsContract$Groups;
import android.content.ContentProviderOperation;
import android.content.SharedPreferences;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ConnectionsManager;
import java.util.Iterator;
import java.util.Map;
import android.database.Cursor;
import org.telegram.messenger.support.SparseLongArray;
import org.telegram.tgnet.TLObject;
import android.util.SparseArray;
import android.content.ContentValues;
import java.util.Collection;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.ContactsContract$RawContacts;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import android.text.TextUtils;
import android.accounts.Account;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import org.telegram.tgnet.TLRPC;
import java.util.ArrayList;

public class ContactsController
{
    private static volatile ContactsController[] Instance;
    public static final int PRIVACY_RULES_TYPE_CALLS = 2;
    public static final int PRIVACY_RULES_TYPE_COUNT = 7;
    public static final int PRIVACY_RULES_TYPE_FORWARDS = 5;
    public static final int PRIVACY_RULES_TYPE_INVITE = 1;
    public static final int PRIVACY_RULES_TYPE_LASTSEEN = 0;
    public static final int PRIVACY_RULES_TYPE_P2P = 3;
    public static final int PRIVACY_RULES_TYPE_PHONE = 6;
    public static final int PRIVACY_RULES_TYPE_PHOTO = 4;
    private ArrayList<TLRPC.PrivacyRule> callPrivacyRules;
    private int completedRequestsCount;
    public ArrayList<TLRPC.TL_contact> contacts;
    public HashMap<String, Contact> contactsBook;
    private boolean contactsBookLoaded;
    public HashMap<String, Contact> contactsBookSPhones;
    public HashMap<String, TLRPC.TL_contact> contactsByPhone;
    public HashMap<String, TLRPC.TL_contact> contactsByShortPhone;
    public ConcurrentHashMap<Integer, TLRPC.TL_contact> contactsDict;
    public boolean contactsLoaded;
    private boolean contactsSyncInProgress;
    private int currentAccount;
    private ArrayList<Integer> delayedContactsUpdate;
    private int deleteAccountTTL;
    private ArrayList<TLRPC.PrivacyRule> forwardsPrivacyRules;
    private ArrayList<TLRPC.PrivacyRule> groupPrivacyRules;
    private boolean ignoreChanges;
    private String inviteLink;
    private String lastContactsVersions;
    private ArrayList<TLRPC.PrivacyRule> lastseenPrivacyRules;
    private final Object loadContactsSync;
    private boolean loadingContacts;
    private int loadingDeleteInfo;
    private int[] loadingPrivacyInfo;
    private boolean migratingContacts;
    private final Object observerLock;
    private ArrayList<TLRPC.PrivacyRule> p2pPrivacyRules;
    public ArrayList<Contact> phoneBookContacts;
    public ArrayList<String> phoneBookSectionsArray;
    public HashMap<String, ArrayList<Object>> phoneBookSectionsDict;
    private ArrayList<TLRPC.PrivacyRule> phonePrivacyRules;
    private ArrayList<TLRPC.PrivacyRule> profilePhotoPrivacyRules;
    private String[] projectionNames;
    private String[] projectionPhones;
    private HashMap<String, String> sectionsToReplace;
    public ArrayList<String> sortedUsersMutualSectionsArray;
    public ArrayList<String> sortedUsersSectionsArray;
    private Account systemAccount;
    private boolean updatingInviteLink;
    public HashMap<String, ArrayList<TLRPC.TL_contact>> usersMutualSectionsDict;
    public HashMap<String, ArrayList<TLRPC.TL_contact>> usersSectionsDict;
    
    static {
        ContactsController.Instance = new ContactsController[3];
    }
    
    public ContactsController(final int currentAccount) {
        this.loadContactsSync = new Object();
        this.observerLock = new Object();
        this.lastContactsVersions = "";
        this.delayedContactsUpdate = new ArrayList<Integer>();
        this.sectionsToReplace = new HashMap<String, String>();
        this.loadingPrivacyInfo = new int[7];
        this.projectionPhones = new String[] { "lookup", "data1", "data2", "data3", "display_name", "account_type" };
        this.projectionNames = new String[] { "lookup", "data2", "data3", "data5" };
        this.contactsBook = new HashMap<String, Contact>();
        this.contactsBookSPhones = new HashMap<String, Contact>();
        this.phoneBookContacts = new ArrayList<Contact>();
        this.phoneBookSectionsDict = new HashMap<String, ArrayList<Object>>();
        this.phoneBookSectionsArray = new ArrayList<String>();
        this.contacts = new ArrayList<TLRPC.TL_contact>();
        this.contactsDict = new ConcurrentHashMap<Integer, TLRPC.TL_contact>(20, 1.0f, 2);
        this.usersSectionsDict = new HashMap<String, ArrayList<TLRPC.TL_contact>>();
        this.sortedUsersSectionsArray = new ArrayList<String>();
        this.usersMutualSectionsDict = new HashMap<String, ArrayList<TLRPC.TL_contact>>();
        this.sortedUsersMutualSectionsArray = new ArrayList<String>();
        this.contactsByPhone = new HashMap<String, TLRPC.TL_contact>();
        this.contactsByShortPhone = new HashMap<String, TLRPC.TL_contact>();
        this.currentAccount = currentAccount;
        if (MessagesController.getMainSettings(this.currentAccount).getBoolean("needGetStatuses", false)) {
            this.reloadContactsStatuses();
        }
        this.sectionsToReplace.put("\u00c0", "A");
        this.sectionsToReplace.put("\u00c1", "A");
        this.sectionsToReplace.put("\u00c4", "A");
        this.sectionsToReplace.put("\u00d9", "U");
        this.sectionsToReplace.put("\u00da", "U");
        this.sectionsToReplace.put("\u00dc", "U");
        this.sectionsToReplace.put("\u00cc", "I");
        this.sectionsToReplace.put("\u00cd", "I");
        this.sectionsToReplace.put("\u00cf", "I");
        this.sectionsToReplace.put("\u00c8", "E");
        this.sectionsToReplace.put("\u00c9", "E");
        this.sectionsToReplace.put("\u00ca", "E");
        this.sectionsToReplace.put("\u00cb", "E");
        this.sectionsToReplace.put("\u00d2", "O");
        this.sectionsToReplace.put("\u00d3", "O");
        this.sectionsToReplace.put("\u00d6", "O");
        this.sectionsToReplace.put("\u00c7", "C");
        this.sectionsToReplace.put("\u00d1", "N");
        this.sectionsToReplace.put("\u0178", "Y");
        this.sectionsToReplace.put("\u00dd", "Y");
        this.sectionsToReplace.put("\u0162", "Y");
        if (currentAccount == 0) {
            Utilities.globalQueue.postRunnable(new _$$Lambda$ContactsController$IIQVqTpQCvnIY8_p91gfnj3he0s(this));
        }
    }
    
    private void applyContactsUpdates(final ArrayList<Integer> list, final ConcurrentHashMap<Integer, TLRPC.User> concurrentHashMap, final ArrayList<TLRPC.TL_contact> list2, final ArrayList<Integer> list3) {
        final int n = 0;
        ArrayList<TLRPC.TL_contact> list4 = null;
        ArrayList<Integer> list5 = null;
        Label_0130: {
            if (list2 != null) {
                list4 = list2;
                if ((list5 = list3) != null) {
                    break Label_0130;
                }
            }
            final ArrayList<TLRPC.TL_contact> list6 = new ArrayList<TLRPC.TL_contact>();
            final ArrayList<Integer> list7 = new ArrayList<Integer>();
            int index = 0;
            while (true) {
                list4 = list6;
                list5 = list7;
                if (index >= list.size()) {
                    break;
                }
                final Integer n2 = list.get(index);
                if (n2 > 0) {
                    final TLRPC.TL_contact e = new TLRPC.TL_contact();
                    e.user_id = n2;
                    list6.add(e);
                }
                else if (n2 < 0) {
                    list7.add(-n2);
                }
                ++index;
            }
        }
        if (BuildVars.LOGS_ENABLED) {
            final StringBuilder sb = new StringBuilder();
            sb.append("process update - contacts add = ");
            sb.append(list4.size());
            sb.append(" delete = ");
            sb.append(list5.size());
            FileLog.d(sb.toString());
        }
        final StringBuilder sb2 = new StringBuilder();
        final StringBuilder sb3 = new StringBuilder();
        int index2 = 0;
        int n3 = 0;
        int i;
        int n4;
        while (true) {
            final int size = list4.size();
            TLRPC.User user = null;
            i = n;
            n4 = n3;
            if (index2 >= size) {
                break;
            }
            final TLRPC.TL_contact tl_contact = list4.get(index2);
            if (concurrentHashMap != null) {
                user = concurrentHashMap.get(tl_contact.user_id);
            }
            if (user == null) {
                user = MessagesController.getInstance(this.currentAccount).getUser(tl_contact.user_id);
            }
            else {
                MessagesController.getInstance(this.currentAccount).putUser(user, true);
            }
            if (user != null && !TextUtils.isEmpty((CharSequence)user.phone)) {
                final Contact contact = this.contactsBookSPhones.get(user.phone);
                if (contact != null) {
                    final int index3 = contact.shortPhones.indexOf(user.phone);
                    if (index3 != -1) {
                        contact.phoneDeleted.set(index3, 0);
                    }
                }
                if (sb2.length() != 0) {
                    sb2.append(",");
                }
                sb2.append(user.phone);
            }
            else {
                n3 = 1;
            }
            ++index2;
        }
        while (i < list5.size()) {
            final Integer key = list5.get(i);
            Utilities.phoneBookQueue.postRunnable(new _$$Lambda$ContactsController$HjkvfPeYk7Ow3aL58FFYm929xXw(this, key));
            TLRPC.User user2;
            if (concurrentHashMap != null) {
                user2 = concurrentHashMap.get(key);
            }
            else {
                user2 = null;
            }
            if (user2 == null) {
                user2 = MessagesController.getInstance(this.currentAccount).getUser(key);
            }
            else {
                MessagesController.getInstance(this.currentAccount).putUser(user2, true);
            }
            int n5;
            if (user2 == null) {
                n5 = 1;
            }
            else {
                n5 = n4;
                if (!TextUtils.isEmpty((CharSequence)user2.phone)) {
                    final Contact contact2 = this.contactsBookSPhones.get(user2.phone);
                    if (contact2 != null) {
                        final int index4 = contact2.shortPhones.indexOf(user2.phone);
                        if (index4 != -1) {
                            contact2.phoneDeleted.set(index4, 1);
                        }
                    }
                    if (sb3.length() != 0) {
                        sb3.append(",");
                    }
                    sb3.append(user2.phone);
                    n5 = n4;
                }
            }
            ++i;
            n4 = n5;
        }
        if (sb2.length() != 0 || sb3.length() != 0) {
            MessagesStorage.getInstance(this.currentAccount).applyPhoneBookUpdates(sb2.toString(), sb3.toString());
        }
        if (n4 != 0) {
            Utilities.stageQueue.postRunnable(new _$$Lambda$ContactsController$zT0R4MDQT_YLqu_ka0J_Safa99M(this));
        }
        else {
            AndroidUtilities.runOnUIThread(new _$$Lambda$ContactsController$8jHuChSQV9WMksUcSKrM56MxPqE(this, list4, list5));
        }
    }
    
    private void buildContactsSectionsArrays(final boolean b) {
        if (b) {
            Collections.sort(this.contacts, new _$$Lambda$ContactsController$l3XEyiXk02DazId_mQdpRpST3Co(this));
        }
        final HashMap<String, ArrayList<TLRPC.TL_contact>> usersSectionsDict = new HashMap<String, ArrayList<TLRPC.TL_contact>>();
        final ArrayList<Object> list = new ArrayList<Object>();
        for (int i = 0; i < this.contacts.size(); ++i) {
            final TLRPC.TL_contact e = this.contacts.get(i);
            final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(e.user_id);
            if (user != null) {
                String s2;
                final String s = s2 = UserObject.getFirstName(user);
                if (s.length() > 1) {
                    s2 = s.substring(0, 1);
                }
                String upperCase;
                if (s2.length() == 0) {
                    upperCase = "#";
                }
                else {
                    upperCase = s2.toUpperCase();
                }
                final String s3 = this.sectionsToReplace.get(upperCase);
                if (s3 != null) {
                    upperCase = s3;
                }
                ArrayList<TLRPC.TL_contact> value;
                if ((value = usersSectionsDict.get(upperCase)) == null) {
                    value = new ArrayList<TLRPC.TL_contact>();
                    usersSectionsDict.put(upperCase, value);
                    list.add(upperCase);
                }
                value.add(e);
            }
        }
        Collections.sort(list, (Comparator<? super Object>)_$$Lambda$ContactsController$4fSnP4tj8Rx2FPjE7t10auDnEhg.INSTANCE);
        this.usersSectionsDict = usersSectionsDict;
        this.sortedUsersSectionsArray = (ArrayList<String>)list;
    }
    
    private boolean checkContactsInternal() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: istore_1       
        //     2: iconst_0       
        //     3: istore_2       
        //     4: iconst_0       
        //     5: istore_3       
        //     6: iconst_0       
        //     7: istore          4
        //     9: iconst_0       
        //    10: istore          5
        //    12: iconst_0       
        //    13: istore          6
        //    15: iload           5
        //    17: istore          7
        //    19: aload_0        
        //    20: invokespecial   org/telegram/messenger/ContactsController.hasContactsPermission:()Z
        //    23: ifne            28
        //    26: iconst_0       
        //    27: ireturn        
        //    28: iload           5
        //    30: istore          7
        //    32: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //    35: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
        //    38: astore          8
        //    40: aload           8
        //    42: getstatic       android/provider/ContactsContract$RawContacts.CONTENT_URI:Landroid/net/Uri;
        //    45: iconst_1       
        //    46: anewarray       Ljava/lang/String;
        //    49: dup            
        //    50: iconst_0       
        //    51: ldc_w           "version"
        //    54: aastore        
        //    55: aconst_null    
        //    56: aconst_null    
        //    57: aconst_null    
        //    58: invokevirtual   android/content/ContentResolver.query:(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //    61: astore          9
        //    63: aconst_null    
        //    64: astore          10
        //    66: iload_3        
        //    67: istore          7
        //    69: aload           9
        //    71: ifnull          302
        //    74: iload_1        
        //    75: istore          7
        //    77: aload           10
        //    79: astore          8
        //    81: iload_2        
        //    82: istore_3       
        //    83: new             Ljava/lang/StringBuilder;
        //    86: astore          11
        //    88: iload_1        
        //    89: istore          7
        //    91: aload           10
        //    93: astore          8
        //    95: iload_2        
        //    96: istore_3       
        //    97: aload           11
        //    99: invokespecial   java/lang/StringBuilder.<init>:()V
        //   102: iload_1        
        //   103: istore          7
        //   105: aload           10
        //   107: astore          8
        //   109: iload_2        
        //   110: istore_3       
        //   111: aload           9
        //   113: invokeinterface android/database/Cursor.moveToNext:()Z
        //   118: ifeq            156
        //   121: iload_1        
        //   122: istore          7
        //   124: aload           10
        //   126: astore          8
        //   128: iload_2        
        //   129: istore_3       
        //   130: aload           11
        //   132: aload           9
        //   134: aload           9
        //   136: ldc_w           "version"
        //   139: invokeinterface android/database/Cursor.getColumnIndex:(Ljava/lang/String;)I
        //   144: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   149: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   152: pop            
        //   153: goto            102
        //   156: iload_1        
        //   157: istore          7
        //   159: aload           10
        //   161: astore          8
        //   163: iload_2        
        //   164: istore_3       
        //   165: aload           11
        //   167: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   170: astore          11
        //   172: iload           6
        //   174: istore          4
        //   176: iload_1        
        //   177: istore          7
        //   179: aload           10
        //   181: astore          8
        //   183: iload_2        
        //   184: istore_3       
        //   185: aload_0        
        //   186: getfield        org/telegram/messenger/ContactsController.lastContactsVersions:Ljava/lang/String;
        //   189: invokevirtual   java/lang/String.length:()I
        //   192: ifeq            223
        //   195: iload           6
        //   197: istore          4
        //   199: iload_1        
        //   200: istore          7
        //   202: aload           10
        //   204: astore          8
        //   206: iload_2        
        //   207: istore_3       
        //   208: aload_0        
        //   209: getfield        org/telegram/messenger/ContactsController.lastContactsVersions:Ljava/lang/String;
        //   212: aload           11
        //   214: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   217: ifne            223
        //   220: iconst_1       
        //   221: istore          4
        //   223: iload           4
        //   225: istore          7
        //   227: aload           10
        //   229: astore          8
        //   231: iload           4
        //   233: istore_3       
        //   234: aload_0        
        //   235: aload           11
        //   237: putfield        org/telegram/messenger/ContactsController.lastContactsVersions:Ljava/lang/String;
        //   240: iload           4
        //   242: istore          7
        //   244: goto            302
        //   247: astore          10
        //   249: goto            264
        //   252: astore          10
        //   254: iload_3        
        //   255: istore          7
        //   257: aload           10
        //   259: astore          8
        //   261: aload           10
        //   263: athrow         
        //   264: aload           9
        //   266: ifnull          295
        //   269: aload           8
        //   271: ifnull          284
        //   274: aload           9
        //   276: invokeinterface android/database/Cursor.close:()V
        //   281: goto            295
        //   284: iload           7
        //   286: istore          4
        //   288: aload           9
        //   290: invokeinterface android/database/Cursor.close:()V
        //   295: iload           7
        //   297: istore          4
        //   299: aload           10
        //   301: athrow         
        //   302: iload           7
        //   304: istore          4
        //   306: aload           9
        //   308: ifnull          354
        //   311: iload           7
        //   313: istore          4
        //   315: aload           9
        //   317: invokeinterface android/database/Cursor.close:()V
        //   322: iload           7
        //   324: istore          4
        //   326: goto            354
        //   329: astore          8
        //   331: iload           4
        //   333: istore          7
        //   335: aload           8
        //   337: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   340: goto            354
        //   343: astore          8
        //   345: aload           8
        //   347: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   350: iload           7
        //   352: istore          4
        //   354: iload           4
        //   356: ireturn        
        //   357: astore          8
        //   359: goto            295
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  19     26     343    354    Ljava/lang/Exception;
        //  32     40     343    354    Ljava/lang/Exception;
        //  40     63     329    343    Ljava/lang/Exception;
        //  83     88     252    264    Ljava/lang/Throwable;
        //  83     88     247    302    Any
        //  97     102    252    264    Ljava/lang/Throwable;
        //  97     102    247    302    Any
        //  111    121    252    264    Ljava/lang/Throwable;
        //  111    121    247    302    Any
        //  130    153    252    264    Ljava/lang/Throwable;
        //  130    153    247    302    Any
        //  165    172    252    264    Ljava/lang/Throwable;
        //  165    172    247    302    Any
        //  185    195    252    264    Ljava/lang/Throwable;
        //  185    195    247    302    Any
        //  208    220    252    264    Ljava/lang/Throwable;
        //  208    220    247    302    Any
        //  234    240    252    264    Ljava/lang/Throwable;
        //  234    240    247    302    Any
        //  261    264    247    302    Any
        //  274    281    357    362    Ljava/lang/Throwable;
        //  288    295    329    343    Ljava/lang/Exception;
        //  299    302    329    343    Ljava/lang/Exception;
        //  315    322    329    343    Ljava/lang/Exception;
        //  335    340    343    354    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0284:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private void deleteContactFromPhoneBook(final int i) {
        if (!this.hasContactsPermission()) {
            return;
        }
        Object o = this.observerLock;
        synchronized (o) {
            this.ignoreChanges = true;
            // monitorexit(o)
            try {
                final ContentResolver contentResolver = ApplicationLoader.applicationContext.getContentResolver();
                o = ContactsContract$RawContacts.CONTENT_URI.buildUpon().appendQueryParameter("caller_is_syncadapter", "true").appendQueryParameter("account_name", this.systemAccount.name).appendQueryParameter("account_type", this.systemAccount.type).build();
                final StringBuilder sb = new StringBuilder();
                sb.append("sync2 = ");
                sb.append(i);
                contentResolver.delete((Uri)o, sb.toString(), (String[])null);
            }
            catch (Exception o) {
                FileLog.e((Throwable)o);
            }
            synchronized (this.observerLock) {
                this.ignoreChanges = false;
            }
        }
    }
    
    public static String formatName(String trim, final String s) {
        String trim2 = trim;
        if (trim != null) {
            trim2 = trim.trim();
        }
        if ((trim = s) != null) {
            trim = s.trim();
        }
        int length = 0;
        int length2;
        if (trim2 != null) {
            length2 = trim2.length();
        }
        else {
            length2 = 0;
        }
        if (trim != null) {
            length = trim.length();
        }
        final StringBuilder sb = new StringBuilder(length2 + length + 1);
        if (LocaleController.nameDisplayOrder == 1) {
            if (trim2 != null && trim2.length() > 0) {
                sb.append(trim2);
                if (trim != null && trim.length() > 0) {
                    sb.append(" ");
                    sb.append(trim);
                }
            }
            else if (trim != null && trim.length() > 0) {
                sb.append(trim);
            }
        }
        else if (trim != null && trim.length() > 0) {
            sb.append(trim);
            if (trim2 != null && trim2.length() > 0) {
                sb.append(" ");
                sb.append(trim2);
            }
        }
        else if (trim2 != null && trim2.length() > 0) {
            sb.append(trim2);
        }
        return sb.toString();
    }
    
    private int getContactsHash(final ArrayList<TLRPC.TL_contact> c) {
        final ArrayList<TLRPC.TL_contact> list = (ArrayList<TLRPC.TL_contact>)new ArrayList<Object>(c);
        Collections.sort((List<Object>)list, (Comparator<? super Object>)_$$Lambda$ContactsController$TrjXA3zXxBZ5H4pd0ZldkUjIm4Y.INSTANCE);
        final int size = list.size();
        long n = 0L;
        for (int i = -1; i < size; ++i) {
            if (i == -1) {
                n = (n * 20261L + 2147483648L + UserConfig.getInstance(this.currentAccount).contactsSavedCount) % 2147483648L;
            }
            else {
                n = (n * 20261L + 2147483648L + list.get(i).user_id) % 2147483648L;
            }
        }
        return (int)n;
    }
    
    public static ContactsController getInstance(final int n) {
        final ContactsController contactsController;
        if ((contactsController = ContactsController.Instance[n]) == null) {
            synchronized (ContactsController.class) {
                if (ContactsController.Instance[n] == null) {
                    ContactsController.Instance[n] = new ContactsController(n);
                }
            }
        }
        return contactsController;
    }
    
    private boolean hasContactsPermission() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: istore_1       
        //     4: iconst_1       
        //     5: istore_2       
        //     6: iload_1        
        //     7: bipush          23
        //     9: if_icmplt       31
        //    12: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //    15: ldc_w           "android.permission.READ_CONTACTS"
        //    18: invokevirtual   android/content/Context.checkSelfPermission:(Ljava/lang/String;)I
        //    21: ifne            27
        //    24: goto            29
        //    27: iconst_0       
        //    28: istore_2       
        //    29: iload_2        
        //    30: ireturn        
        //    31: aconst_null    
        //    32: astore_3       
        //    33: aconst_null    
        //    34: astore          4
        //    36: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //    39: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
        //    42: getstatic       android/provider/ContactsContract$CommonDataKinds$Phone.CONTENT_URI:Landroid/net/Uri;
        //    45: aload_0        
        //    46: getfield        org/telegram/messenger/ContactsController.projectionPhones:[Ljava/lang/String;
        //    49: aconst_null    
        //    50: aconst_null    
        //    51: aconst_null    
        //    52: invokevirtual   android/content/ContentResolver.query:(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //    55: astore          5
        //    57: aload           5
        //    59: ifnull          99
        //    62: aload           5
        //    64: astore          4
        //    66: aload           5
        //    68: astore_3       
        //    69: aload           5
        //    71: invokeinterface android/database/Cursor.getCount:()I
        //    76: istore_1       
        //    77: iload_1        
        //    78: ifne            84
        //    81: goto            99
        //    84: aload           5
        //    86: ifnull          157
        //    89: aload           5
        //    91: invokeinterface android/database/Cursor.close:()V
        //    96: goto            157
        //    99: aload           5
        //   101: ifnull          121
        //   104: aload           5
        //   106: invokeinterface android/database/Cursor.close:()V
        //   111: goto            121
        //   114: astore          4
        //   116: aload           4
        //   118: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   121: iconst_0       
        //   122: ireturn        
        //   123: astore_3       
        //   124: goto            159
        //   127: astore          5
        //   129: aload_3        
        //   130: astore          4
        //   132: aload           5
        //   134: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   137: aload_3        
        //   138: ifnull          157
        //   141: aload_3        
        //   142: invokeinterface android/database/Cursor.close:()V
        //   147: goto            157
        //   150: astore          4
        //   152: aload           4
        //   154: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   157: iconst_1       
        //   158: ireturn        
        //   159: aload           4
        //   161: ifnull          181
        //   164: aload           4
        //   166: invokeinterface android/database/Cursor.close:()V
        //   171: goto            181
        //   174: astore          4
        //   176: aload           4
        //   178: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //   181: aload_3        
        //   182: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  36     57     127    150    Ljava/lang/Throwable;
        //  36     57     123    183    Any
        //  69     77     127    150    Ljava/lang/Throwable;
        //  69     77     123    183    Any
        //  89     96     150    157    Ljava/lang/Exception;
        //  104    111    114    121    Ljava/lang/Exception;
        //  132    137    123    183    Any
        //  141    147    150    157    Ljava/lang/Exception;
        //  164    171    174    181    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 84 out-of-bounds for length 84
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private boolean isNotValidNameString(final String s) {
        final boolean empty = TextUtils.isEmpty((CharSequence)s);
        boolean b = true;
        if (empty) {
            return true;
        }
        final int length = s.length();
        int i = 0;
        int n = 0;
        while (i < length) {
            final char char1 = s.charAt(i);
            int n2 = n;
            if (char1 >= '0') {
                n2 = n;
                if (char1 <= '9') {
                    n2 = n + 1;
                }
            }
            ++i;
            n = n2;
        }
        if (n <= 3) {
            b = false;
        }
        return b;
    }
    
    private void mergePhonebookAndTelegramContacts(final HashMap<String, ArrayList<Object>> hashMap, final ArrayList<String> list, final HashMap<String, Contact> hashMap2) {
        Utilities.globalQueue.postRunnable(new _$$Lambda$ContactsController$gajOA1_r5XMonBIZ6Lf97P0bD9I(this, new ArrayList((Collection<? extends E>)this.contacts), hashMap2, hashMap, list));
    }
    
    private void performWriteContactsToPhoneBook() {
        Utilities.phoneBookQueue.postRunnable(new _$$Lambda$ContactsController$E0GDxDm4XOysG7SHcvpj5BVOSkQ(this, new ArrayList((Collection<? extends E>)this.contacts)));
    }
    
    private void performWriteContactsToPhoneBookInternal(final ArrayList<TLRPC.TL_contact> list) {
        final Cursor cursor = null;
        final Cursor cursor2 = null;
        final Uri uri = null;
        Object o = cursor;
        Object o2 = null;
        Cursor cursor3;
        try {
            try {
                if (!this.hasContactsPermission()) {
                    return;
                }
                o = cursor;
                o2 = ContactsContract$RawContacts.CONTENT_URI.buildUpon().appendQueryParameter("account_name", this.systemAccount.name).appendQueryParameter("account_type", this.systemAccount.type).build();
                o = cursor;
                o2 = ApplicationLoader.applicationContext.getContentResolver().query((Uri)o2, new String[] { "_id", "sync2" }, (String)null, (String[])null, (String)null);
                try {
                    final SparseLongArray sparseLongArray = new SparseLongArray();
                    if (o2 != null) {
                        while (((Cursor)o2).moveToNext()) {
                            sparseLongArray.put(((Cursor)o2).getInt(1), ((Cursor)o2).getLong(0));
                        }
                        ((Cursor)o2).close();
                        int index = 0;
                        while (true) {
                            o2 = uri;
                            o = cursor;
                            if (index >= list.size()) {
                                break;
                            }
                            o = cursor;
                            o2 = list.get(index);
                            o = cursor;
                            if (sparseLongArray.indexOfKey(((TLRPC.TL_contact)o2).user_id) < 0) {
                                o = cursor;
                                this.addContactToPhoneBook(MessagesController.getInstance(this.currentAccount).getUser(((TLRPC.TL_contact)o2).user_id), false);
                            }
                            ++index;
                        }
                    }
                    if (o2 == null) {
                        return;
                    }
                }
                catch (Exception o) {}
                finally {
                    o = o2;
                }
            }
            finally {}
        }
        catch (Exception o2) {
            cursor3 = cursor2;
        }
        FileLog.e((Throwable)o2);
        if (cursor3 != null) {
            cursor3.close();
        }
        return;
        if (o != null) {
            ((Cursor)o).close();
        }
    }
    
    private HashMap<String, Contact> readContactsFromPhoneBook() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: astore_1       
        //     2: aload_1        
        //     3: getfield        org/telegram/messenger/ContactsController.currentAccount:I
        //     6: invokestatic    org/telegram/messenger/UserConfig.getInstance:(I)Lorg/telegram/messenger/UserConfig;
        //     9: getfield        org/telegram/messenger/UserConfig.syncContacts:Z
        //    12: ifne            35
        //    15: getstatic       org/telegram/messenger/BuildVars.LOGS_ENABLED:Z
        //    18: ifeq            27
        //    21: ldc_w           "contacts sync disabled"
        //    24: invokestatic    org/telegram/messenger/FileLog.d:(Ljava/lang/String;)V
        //    27: new             Ljava/util/HashMap;
        //    30: dup            
        //    31: invokespecial   java/util/HashMap.<init>:()V
        //    34: areturn        
        //    35: aload_0        
        //    36: invokespecial   org/telegram/messenger/ContactsController.hasContactsPermission:()Z
        //    39: ifne            62
        //    42: getstatic       org/telegram/messenger/BuildVars.LOGS_ENABLED:Z
        //    45: ifeq            54
        //    48: ldc_w           "app has no contacts permissions"
        //    51: invokestatic    org/telegram/messenger/FileLog.d:(Ljava/lang/String;)V
        //    54: new             Ljava/util/HashMap;
        //    57: dup            
        //    58: invokespecial   java/util/HashMap.<init>:()V
        //    61: areturn        
        //    62: new             Ljava/lang/StringBuilder;
        //    65: astore_2       
        //    66: aload_2        
        //    67: invokespecial   java/lang/StringBuilder.<init>:()V
        //    70: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //    73: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
        //    76: astore_3       
        //    77: new             Ljava/util/HashMap;
        //    80: astore          4
        //    82: aload           4
        //    84: invokespecial   java/util/HashMap.<init>:()V
        //    87: new             Ljava/util/ArrayList;
        //    90: astore          5
        //    92: aload           5
        //    94: invokespecial   java/util/ArrayList.<init>:()V
        //    97: aload_3        
        //    98: getstatic       android/provider/ContactsContract$CommonDataKinds$Phone.CONTENT_URI:Landroid/net/Uri;
        //   101: aload_1        
        //   102: getfield        org/telegram/messenger/ContactsController.projectionPhones:[Ljava/lang/String;
        //   105: aconst_null    
        //   106: aconst_null    
        //   107: aconst_null    
        //   108: invokevirtual   android/content/ContentResolver.query:(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //   111: astore          6
        //   113: aload           6
        //   115: ifnull          1192
        //   118: aload           6
        //   120: invokeinterface android/database/Cursor.getCount:()I
        //   125: istore          7
        //   127: iload           7
        //   129: ifle            1127
        //   132: new             Ljava/util/HashMap;
        //   135: astore          8
        //   137: aload           8
        //   139: iload           7
        //   141: invokespecial   java/util/HashMap.<init>:(I)V
        //   144: iconst_1       
        //   145: istore          7
        //   147: aload           8
        //   149: astore          9
        //   151: aload           6
        //   153: invokeinterface android/database/Cursor.moveToNext:()Z
        //   158: ifeq            1116
        //   161: aload           8
        //   163: astore          9
        //   165: aload           6
        //   167: iconst_1       
        //   168: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   173: astore          10
        //   175: aload           8
        //   177: astore          9
        //   179: aload           6
        //   181: iconst_5       
        //   182: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   187: astore          11
        //   189: aload           11
        //   191: astore          12
        //   193: aload           11
        //   195: ifnonnull       202
        //   198: ldc             ""
        //   200: astore          12
        //   202: aload           8
        //   204: astore          9
        //   206: aload           12
        //   208: ldc_w           ".sim"
        //   211: invokevirtual   java/lang/String.indexOf:(Ljava/lang/String;)I
        //   214: ifeq            223
        //   217: iconst_1       
        //   218: istore          13
        //   220: goto            226
        //   223: iconst_0       
        //   224: istore          13
        //   226: aload           8
        //   228: astore          9
        //   230: aload           10
        //   232: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //   235: ifeq            254
        //   238: aload_2        
        //   239: astore          9
        //   241: aload_1        
        //   242: astore          11
        //   244: aload_3        
        //   245: astore          10
        //   247: iload           7
        //   249: istore          14
        //   251: goto            513
        //   254: aload           8
        //   256: astore          9
        //   258: aload           10
        //   260: iconst_1       
        //   261: invokestatic    org/telegram/PhoneFormat/PhoneFormat.stripExceptNumbers:(Ljava/lang/String;Z)Ljava/lang/String;
        //   264: astore          15
        //   266: aload           8
        //   268: astore          9
        //   270: aload           15
        //   272: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //   275: ifeq            294
        //   278: aload_2        
        //   279: astore          9
        //   281: aload_1        
        //   282: astore          11
        //   284: aload_3        
        //   285: astore          10
        //   287: iload           7
        //   289: istore          14
        //   291: goto            513
        //   294: aload           8
        //   296: astore          9
        //   298: aload           15
        //   300: ldc_w           "+"
        //   303: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   306: istore          16
        //   308: iload           16
        //   310: ifeq            332
        //   313: aload           15
        //   315: iconst_1       
        //   316: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   319: astore          11
        //   321: goto            336
        //   324: astore_3       
        //   325: goto            1176
        //   328: astore_3       
        //   329: goto            1152
        //   332: aload           15
        //   334: astore          11
        //   336: aload           8
        //   338: astore          9
        //   340: aload           6
        //   342: iconst_0       
        //   343: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   348: astore          17
        //   350: aload           8
        //   352: astore          9
        //   354: aload_2        
        //   355: iconst_0       
        //   356: invokevirtual   java/lang/StringBuilder.setLength:(I)V
        //   359: aload           8
        //   361: astore          9
        //   363: aload_2        
        //   364: aload           17
        //   366: invokestatic    android/database/DatabaseUtils.appendEscapedSQLString:(Ljava/lang/StringBuilder;Ljava/lang/String;)V
        //   369: aload           8
        //   371: astore          9
        //   373: aload_2        
        //   374: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   377: astore          18
        //   379: aload           8
        //   381: astore          9
        //   383: aload           4
        //   385: aload           11
        //   387: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   390: checkcast       Lorg/telegram/messenger/ContactsController$Contact;
        //   393: astore          19
        //   395: aload           19
        //   397: ifnull          529
        //   400: aload_2        
        //   401: astore          9
        //   403: aload_1        
        //   404: astore          11
        //   406: aload_3        
        //   407: astore          10
        //   409: iload           7
        //   411: istore          14
        //   413: aload           19
        //   415: getfield        org/telegram/messenger/ContactsController$Contact.isGoodProvider:Z
        //   418: ifne            513
        //   421: aload_2        
        //   422: astore          9
        //   424: aload_1        
        //   425: astore          11
        //   427: aload_3        
        //   428: astore          10
        //   430: iload           7
        //   432: istore          14
        //   434: aload           12
        //   436: aload           19
        //   438: getfield        org/telegram/messenger/ContactsController$Contact.provider:Ljava/lang/String;
        //   441: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   444: ifne            513
        //   447: aload_2        
        //   448: iconst_0       
        //   449: invokevirtual   java/lang/StringBuilder.setLength:(I)V
        //   452: aload_2        
        //   453: aload           19
        //   455: getfield        org/telegram/messenger/ContactsController$Contact.key:Ljava/lang/String;
        //   458: invokestatic    android/database/DatabaseUtils.appendEscapedSQLString:(Ljava/lang/StringBuilder;Ljava/lang/String;)V
        //   461: aload           5
        //   463: aload_2        
        //   464: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   467: invokevirtual   java/util/ArrayList.remove:(Ljava/lang/Object;)Z
        //   470: pop            
        //   471: aload           5
        //   473: aload           18
        //   475: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   478: pop            
        //   479: aload           19
        //   481: aload           17
        //   483: putfield        org/telegram/messenger/ContactsController$Contact.key:Ljava/lang/String;
        //   486: aload           19
        //   488: iload           13
        //   490: putfield        org/telegram/messenger/ContactsController$Contact.isGoodProvider:Z
        //   493: aload           19
        //   495: aload           12
        //   497: putfield        org/telegram/messenger/ContactsController$Contact.provider:Ljava/lang/String;
        //   500: iload           7
        //   502: istore          14
        //   504: aload_3        
        //   505: astore          10
        //   507: aload_1        
        //   508: astore          11
        //   510: aload_2        
        //   511: astore          9
        //   513: aload           9
        //   515: astore_2       
        //   516: aload           11
        //   518: astore_1       
        //   519: aload           10
        //   521: astore_3       
        //   522: iload           14
        //   524: istore          7
        //   526: goto            147
        //   529: aload           8
        //   531: astore          9
        //   533: aload           5
        //   535: aload           18
        //   537: invokevirtual   java/util/ArrayList.contains:(Ljava/lang/Object;)Z
        //   540: istore          16
        //   542: iload           16
        //   544: ifne            555
        //   547: aload           5
        //   549: aload           18
        //   551: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   554: pop            
        //   555: aload           8
        //   557: astore          9
        //   559: aload           6
        //   561: iconst_2       
        //   562: invokeinterface android/database/Cursor.getInt:(I)I
        //   567: istore          14
        //   569: aload           8
        //   571: astore          9
        //   573: aload           8
        //   575: aload           17
        //   577: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   580: checkcast       Lorg/telegram/messenger/ContactsController$Contact;
        //   583: astore          10
        //   585: aload           10
        //   587: ifnonnull       831
        //   590: aload           8
        //   592: astore          9
        //   594: new             Lorg/telegram/messenger/ContactsController$Contact;
        //   597: astore          19
        //   599: aload           8
        //   601: astore          9
        //   603: aload           19
        //   605: invokespecial   org/telegram/messenger/ContactsController$Contact.<init>:()V
        //   608: aload           8
        //   610: astore          9
        //   612: aload           6
        //   614: iconst_4       
        //   615: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   620: astore          10
        //   622: aload           10
        //   624: ifnonnull       634
        //   627: ldc             ""
        //   629: astore          10
        //   631: goto            645
        //   634: aload           8
        //   636: astore          9
        //   638: aload           10
        //   640: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //   643: astore          10
        //   645: aload           8
        //   647: astore          9
        //   649: aload_1        
        //   650: aload           10
        //   652: invokespecial   org/telegram/messenger/ContactsController.isNotValidNameString:(Ljava/lang/String;)Z
        //   655: istore          16
        //   657: iload           16
        //   659: ifeq            679
        //   662: aload           19
        //   664: aload           10
        //   666: putfield        org/telegram/messenger/ContactsController$Contact.first_name:Ljava/lang/String;
        //   669: aload           19
        //   671: ldc             ""
        //   673: putfield        org/telegram/messenger/ContactsController$Contact.last_name:Ljava/lang/String;
        //   676: goto            764
        //   679: aload           8
        //   681: astore          9
        //   683: aload           10
        //   685: bipush          32
        //   687: invokevirtual   java/lang/String.lastIndexOf:(I)I
        //   690: istore          20
        //   692: iload           20
        //   694: iconst_m1      
        //   695: if_icmpeq       742
        //   698: aload           8
        //   700: astore          9
        //   702: aload           19
        //   704: aload           10
        //   706: iconst_0       
        //   707: iload           20
        //   709: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   712: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //   715: putfield        org/telegram/messenger/ContactsController$Contact.first_name:Ljava/lang/String;
        //   718: aload           8
        //   720: astore          9
        //   722: aload           19
        //   724: aload           10
        //   726: iload           20
        //   728: iconst_1       
        //   729: iadd           
        //   730: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   733: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //   736: putfield        org/telegram/messenger/ContactsController$Contact.last_name:Ljava/lang/String;
        //   739: goto            764
        //   742: aload           8
        //   744: astore          9
        //   746: aload           19
        //   748: aload           10
        //   750: putfield        org/telegram/messenger/ContactsController$Contact.first_name:Ljava/lang/String;
        //   753: aload           8
        //   755: astore          9
        //   757: aload           19
        //   759: ldc             ""
        //   761: putfield        org/telegram/messenger/ContactsController$Contact.last_name:Ljava/lang/String;
        //   764: aload           8
        //   766: astore          9
        //   768: aload           19
        //   770: aload           12
        //   772: putfield        org/telegram/messenger/ContactsController$Contact.provider:Ljava/lang/String;
        //   775: aload           8
        //   777: astore          9
        //   779: aload           19
        //   781: iload           13
        //   783: putfield        org/telegram/messenger/ContactsController$Contact.isGoodProvider:Z
        //   786: aload           8
        //   788: astore          9
        //   790: aload           19
        //   792: aload           17
        //   794: putfield        org/telegram/messenger/ContactsController$Contact.key:Ljava/lang/String;
        //   797: aload           8
        //   799: astore          9
        //   801: aload           19
        //   803: iload           7
        //   805: putfield        org/telegram/messenger/ContactsController$Contact.contact_id:I
        //   808: aload           8
        //   810: astore          9
        //   812: aload           8
        //   814: aload           17
        //   816: aload           19
        //   818: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   821: pop            
        //   822: iinc            7, 1
        //   825: aload           19
        //   827: astore_1       
        //   828: goto            834
        //   831: aload           10
        //   833: astore_1       
        //   834: aload           8
        //   836: astore          9
        //   838: aload_1        
        //   839: getfield        org/telegram/messenger/ContactsController$Contact.shortPhones:Ljava/util/ArrayList;
        //   842: aload           11
        //   844: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   847: pop            
        //   848: aload           8
        //   850: astore          9
        //   852: aload_1        
        //   853: getfield        org/telegram/messenger/ContactsController$Contact.phones:Ljava/util/ArrayList;
        //   856: aload           15
        //   858: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   861: pop            
        //   862: aload           8
        //   864: astore          9
        //   866: aload_1        
        //   867: getfield        org/telegram/messenger/ContactsController$Contact.phoneDeleted:Ljava/util/ArrayList;
        //   870: iconst_0       
        //   871: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   874: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   877: pop            
        //   878: iload           14
        //   880: ifne            945
        //   883: aload           8
        //   885: astore          9
        //   887: aload           6
        //   889: iconst_3       
        //   890: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   895: astore          12
        //   897: aload           8
        //   899: astore          9
        //   901: aload_1        
        //   902: getfield        org/telegram/messenger/ContactsController$Contact.phoneTypes:Ljava/util/ArrayList;
        //   905: astore          10
        //   907: aload           12
        //   909: ifnull          915
        //   912: goto            930
        //   915: aload           8
        //   917: astore          9
        //   919: ldc_w           "PhoneMobile"
        //   922: ldc_w           2131560427
        //   925: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
        //   928: astore          12
        //   930: aload           8
        //   932: astore          9
        //   934: aload           10
        //   936: aload           12
        //   938: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   941: pop            
        //   942: goto            1087
        //   945: iload           14
        //   947: iconst_1       
        //   948: if_icmpne       975
        //   951: aload           8
        //   953: astore          9
        //   955: aload_1        
        //   956: getfield        org/telegram/messenger/ContactsController$Contact.phoneTypes:Ljava/util/ArrayList;
        //   959: ldc_w           "PhoneHome"
        //   962: ldc_w           2131560425
        //   965: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
        //   968: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   971: pop            
        //   972: goto            1087
        //   975: iload           14
        //   977: iconst_2       
        //   978: if_icmpne       1005
        //   981: aload           8
        //   983: astore          9
        //   985: aload_1        
        //   986: getfield        org/telegram/messenger/ContactsController$Contact.phoneTypes:Ljava/util/ArrayList;
        //   989: ldc_w           "PhoneMobile"
        //   992: ldc_w           2131560427
        //   995: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
        //   998: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  1001: pop            
        //  1002: goto            1087
        //  1005: iload           14
        //  1007: iconst_3       
        //  1008: if_icmpne       1035
        //  1011: aload           8
        //  1013: astore          9
        //  1015: aload_1        
        //  1016: getfield        org/telegram/messenger/ContactsController$Contact.phoneTypes:Ljava/util/ArrayList;
        //  1019: ldc_w           "PhoneWork"
        //  1022: ldc_w           2131560433
        //  1025: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
        //  1028: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  1031: pop            
        //  1032: goto            1087
        //  1035: iload           14
        //  1037: bipush          12
        //  1039: if_icmpne       1066
        //  1042: aload           8
        //  1044: astore          9
        //  1046: aload_1        
        //  1047: getfield        org/telegram/messenger/ContactsController$Contact.phoneTypes:Ljava/util/ArrayList;
        //  1050: ldc_w           "PhoneMain"
        //  1053: ldc_w           2131560426
        //  1056: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
        //  1059: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  1062: pop            
        //  1063: goto            1087
        //  1066: aload           8
        //  1068: astore          9
        //  1070: aload_1        
        //  1071: getfield        org/telegram/messenger/ContactsController$Contact.phoneTypes:Ljava/util/ArrayList;
        //  1074: ldc_w           "PhoneOther"
        //  1077: ldc_w           2131560432
        //  1080: invokestatic    org/telegram/messenger/LocaleController.getString:(Ljava/lang/String;I)Ljava/lang/String;
        //  1083: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //  1086: pop            
        //  1087: aload           8
        //  1089: astore          9
        //  1091: aload           4
        //  1093: aload           11
        //  1095: aload_1        
        //  1096: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1099: pop            
        //  1100: aload_0        
        //  1101: astore          11
        //  1103: aload_2        
        //  1104: astore          9
        //  1106: aload_3        
        //  1107: astore          10
        //  1109: iload           7
        //  1111: istore          14
        //  1113: goto            513
        //  1116: aload_3        
        //  1117: astore_2       
        //  1118: aload           8
        //  1120: astore_3       
        //  1121: aload_2        
        //  1122: astore          8
        //  1124: goto            1134
        //  1127: aconst_null    
        //  1128: astore_2       
        //  1129: aload_3        
        //  1130: astore          8
        //  1132: aload_2        
        //  1133: astore_3       
        //  1134: aload_3        
        //  1135: astore          9
        //  1137: aload           6
        //  1139: invokeinterface android/database/Cursor.close:()V
        //  1144: goto            1167
        //  1147: astore_3       
        //  1148: aload           9
        //  1150: astore          8
        //  1152: aload           6
        //  1154: astore_2       
        //  1155: aload           8
        //  1157: astore          6
        //  1159: aload_3        
        //  1160: astore          8
        //  1162: aload_2        
        //  1163: astore_3       
        //  1164: goto            1920
        //  1167: aload_3        
        //  1168: astore          6
        //  1170: aconst_null    
        //  1171: astore_3       
        //  1172: goto            1203
        //  1175: astore_3       
        //  1176: goto            1985
        //  1179: astore          8
        //  1181: aconst_null    
        //  1182: astore_2       
        //  1183: aload           6
        //  1185: astore_3       
        //  1186: aload_2        
        //  1187: astore          6
        //  1189: goto            1920
        //  1192: aconst_null    
        //  1193: astore_2       
        //  1194: aload_3        
        //  1195: astore          8
        //  1197: aload           6
        //  1199: astore_3       
        //  1200: aload_2        
        //  1201: astore          6
        //  1203: ldc_w           ","
        //  1206: aload           5
        //  1208: invokestatic    android/text/TextUtils.join:(Ljava/lang/CharSequence;Ljava/lang/Iterable;)Ljava/lang/String;
        //  1211: astore_2       
        //  1212: getstatic       android/provider/ContactsContract$Data.CONTENT_URI:Landroid/net/Uri;
        //  1215: astore_1       
        //  1216: aload_0        
        //  1217: getfield        org/telegram/messenger/ContactsController.projectionNames:[Ljava/lang/String;
        //  1220: astore          12
        //  1222: new             Ljava/lang/StringBuilder;
        //  1225: astore          9
        //  1227: aload           9
        //  1229: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1232: aload           9
        //  1234: ldc_w           "lookup IN ("
        //  1237: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1240: pop            
        //  1241: aload           9
        //  1243: aload_2        
        //  1244: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1247: pop            
        //  1248: aload           9
        //  1250: ldc_w           ") AND "
        //  1253: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1256: pop            
        //  1257: aload           9
        //  1259: ldc_w           "mimetype"
        //  1262: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1265: pop            
        //  1266: aload           9
        //  1268: ldc_w           " = '"
        //  1271: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1274: pop            
        //  1275: aload           9
        //  1277: ldc_w           "vnd.android.cursor.item/name"
        //  1280: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1283: pop            
        //  1284: aload           9
        //  1286: ldc_w           "'"
        //  1289: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1292: pop            
        //  1293: aload           8
        //  1295: aload_1        
        //  1296: aload           12
        //  1298: aload           9
        //  1300: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1303: aconst_null    
        //  1304: aconst_null    
        //  1305: invokevirtual   android/content/ContentResolver.query:(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //  1308: astore          9
        //  1310: aload           9
        //  1312: ifnull          1838
        //  1315: aload           9
        //  1317: astore_2       
        //  1318: aload           9
        //  1320: invokeinterface android/database/Cursor.moveToNext:()Z
        //  1325: ifeq            1815
        //  1328: aload           9
        //  1330: astore_2       
        //  1331: aload           9
        //  1333: iconst_0       
        //  1334: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //  1339: astore_1       
        //  1340: aload           9
        //  1342: astore_2       
        //  1343: aload           9
        //  1345: iconst_1       
        //  1346: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //  1351: astore          12
        //  1353: aload           9
        //  1355: astore_2       
        //  1356: aload           9
        //  1358: iconst_2       
        //  1359: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //  1364: astore          8
        //  1366: aload           9
        //  1368: astore_2       
        //  1369: aload           9
        //  1371: iconst_3       
        //  1372: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //  1377: astore_3       
        //  1378: aload           9
        //  1380: astore_2       
        //  1381: aload           6
        //  1383: aload_1        
        //  1384: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //  1387: checkcast       Lorg/telegram/messenger/ContactsController$Contact;
        //  1390: astore_1       
        //  1391: aload_1        
        //  1392: ifnull          1315
        //  1395: aload           9
        //  1397: astore_2       
        //  1398: aload_1        
        //  1399: getfield        org/telegram/messenger/ContactsController$Contact.namesFilled:Z
        //  1402: ifne            1315
        //  1405: aload           9
        //  1407: astore_2       
        //  1408: aload_1        
        //  1409: getfield        org/telegram/messenger/ContactsController$Contact.isGoodProvider:Z
        //  1412: istore          13
        //  1414: iload           13
        //  1416: ifeq            1571
        //  1419: aload           12
        //  1421: ifnull          1436
        //  1424: aload           9
        //  1426: astore_2       
        //  1427: aload_1        
        //  1428: aload           12
        //  1430: putfield        org/telegram/messenger/ContactsController$Contact.first_name:Ljava/lang/String;
        //  1433: goto            1445
        //  1436: aload           9
        //  1438: astore_2       
        //  1439: aload_1        
        //  1440: ldc             ""
        //  1442: putfield        org/telegram/messenger/ContactsController$Contact.first_name:Ljava/lang/String;
        //  1445: aload           8
        //  1447: ifnull          1462
        //  1450: aload           9
        //  1452: astore_2       
        //  1453: aload_1        
        //  1454: aload           8
        //  1456: putfield        org/telegram/messenger/ContactsController$Contact.last_name:Ljava/lang/String;
        //  1459: goto            1471
        //  1462: aload           9
        //  1464: astore_2       
        //  1465: aload_1        
        //  1466: ldc             ""
        //  1468: putfield        org/telegram/messenger/ContactsController$Contact.last_name:Ljava/lang/String;
        //  1471: aload           9
        //  1473: astore_2       
        //  1474: aload_3        
        //  1475: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //  1478: ifne            1804
        //  1481: aload           9
        //  1483: astore_2       
        //  1484: aload_1        
        //  1485: getfield        org/telegram/messenger/ContactsController$Contact.first_name:Ljava/lang/String;
        //  1488: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //  1491: ifne            1560
        //  1494: aload           9
        //  1496: astore_2       
        //  1497: new             Ljava/lang/StringBuilder;
        //  1500: astore          8
        //  1502: aload           9
        //  1504: astore_2       
        //  1505: aload           8
        //  1507: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1510: aload           9
        //  1512: astore_2       
        //  1513: aload           8
        //  1515: aload_1        
        //  1516: getfield        org/telegram/messenger/ContactsController$Contact.first_name:Ljava/lang/String;
        //  1519: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1522: pop            
        //  1523: aload           9
        //  1525: astore_2       
        //  1526: aload           8
        //  1528: ldc_w           " "
        //  1531: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1534: pop            
        //  1535: aload           9
        //  1537: astore_2       
        //  1538: aload           8
        //  1540: aload_3        
        //  1541: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1544: pop            
        //  1545: aload           9
        //  1547: astore_2       
        //  1548: aload_1        
        //  1549: aload           8
        //  1551: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1554: putfield        org/telegram/messenger/ContactsController$Contact.first_name:Ljava/lang/String;
        //  1557: goto            1804
        //  1560: aload           9
        //  1562: astore_2       
        //  1563: aload_1        
        //  1564: aload_3        
        //  1565: putfield        org/telegram/messenger/ContactsController$Contact.first_name:Ljava/lang/String;
        //  1568: goto            1804
        //  1571: aload           9
        //  1573: astore_2       
        //  1574: aload_0        
        //  1575: aload           12
        //  1577: invokespecial   org/telegram/messenger/ContactsController.isNotValidNameString:(Ljava/lang/String;)Z
        //  1580: ifne            1613
        //  1583: aload           9
        //  1585: astore_2       
        //  1586: aload_1        
        //  1587: getfield        org/telegram/messenger/ContactsController$Contact.first_name:Ljava/lang/String;
        //  1590: aload           12
        //  1592: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //  1595: ifne            1655
        //  1598: aload           9
        //  1600: astore_2       
        //  1601: aload           12
        //  1603: aload_1        
        //  1604: getfield        org/telegram/messenger/ContactsController$Contact.first_name:Ljava/lang/String;
        //  1607: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //  1610: ifne            1655
        //  1613: aload           9
        //  1615: astore_2       
        //  1616: aload_0        
        //  1617: aload           8
        //  1619: invokespecial   org/telegram/messenger/ContactsController.isNotValidNameString:(Ljava/lang/String;)Z
        //  1622: ifne            1804
        //  1625: aload           9
        //  1627: astore_2       
        //  1628: aload_1        
        //  1629: getfield        org/telegram/messenger/ContactsController$Contact.last_name:Ljava/lang/String;
        //  1632: aload           8
        //  1634: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //  1637: ifne            1655
        //  1640: aload           9
        //  1642: astore_2       
        //  1643: aload           12
        //  1645: aload_1        
        //  1646: getfield        org/telegram/messenger/ContactsController$Contact.last_name:Ljava/lang/String;
        //  1649: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //  1652: ifeq            1804
        //  1655: aload           12
        //  1657: ifnull          1672
        //  1660: aload           9
        //  1662: astore_2       
        //  1663: aload_1        
        //  1664: aload           12
        //  1666: putfield        org/telegram/messenger/ContactsController$Contact.first_name:Ljava/lang/String;
        //  1669: goto            1681
        //  1672: aload           9
        //  1674: astore_2       
        //  1675: aload_1        
        //  1676: ldc             ""
        //  1678: putfield        org/telegram/messenger/ContactsController$Contact.first_name:Ljava/lang/String;
        //  1681: aload           9
        //  1683: astore_2       
        //  1684: aload_3        
        //  1685: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //  1688: ifne            1778
        //  1691: aload           9
        //  1693: astore_2       
        //  1694: aload_1        
        //  1695: getfield        org/telegram/messenger/ContactsController$Contact.first_name:Ljava/lang/String;
        //  1698: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //  1701: ifne            1770
        //  1704: aload           9
        //  1706: astore_2       
        //  1707: new             Ljava/lang/StringBuilder;
        //  1710: astore          12
        //  1712: aload           9
        //  1714: astore_2       
        //  1715: aload           12
        //  1717: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1720: aload           9
        //  1722: astore_2       
        //  1723: aload           12
        //  1725: aload_1        
        //  1726: getfield        org/telegram/messenger/ContactsController$Contact.first_name:Ljava/lang/String;
        //  1729: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1732: pop            
        //  1733: aload           9
        //  1735: astore_2       
        //  1736: aload           12
        //  1738: ldc_w           " "
        //  1741: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1744: pop            
        //  1745: aload           9
        //  1747: astore_2       
        //  1748: aload           12
        //  1750: aload_3        
        //  1751: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1754: pop            
        //  1755: aload           9
        //  1757: astore_2       
        //  1758: aload_1        
        //  1759: aload           12
        //  1761: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1764: putfield        org/telegram/messenger/ContactsController$Contact.first_name:Ljava/lang/String;
        //  1767: goto            1778
        //  1770: aload           9
        //  1772: astore_2       
        //  1773: aload_1        
        //  1774: aload_3        
        //  1775: putfield        org/telegram/messenger/ContactsController$Contact.first_name:Ljava/lang/String;
        //  1778: aload           8
        //  1780: ifnull          1795
        //  1783: aload           9
        //  1785: astore_2       
        //  1786: aload_1        
        //  1787: aload           8
        //  1789: putfield        org/telegram/messenger/ContactsController$Contact.last_name:Ljava/lang/String;
        //  1792: goto            1804
        //  1795: aload           9
        //  1797: astore_2       
        //  1798: aload_1        
        //  1799: ldc             ""
        //  1801: putfield        org/telegram/messenger/ContactsController$Contact.last_name:Ljava/lang/String;
        //  1804: aload           9
        //  1806: astore_2       
        //  1807: aload_1        
        //  1808: iconst_1       
        //  1809: putfield        org/telegram/messenger/ContactsController$Contact.namesFilled:Z
        //  1812: goto            1315
        //  1815: aload           9
        //  1817: astore_2       
        //  1818: aload           9
        //  1820: invokeinterface android/database/Cursor.close:()V
        //  1825: aconst_null    
        //  1826: astore_3       
        //  1827: goto            1841
        //  1830: astore          8
        //  1832: aload           9
        //  1834: astore_3       
        //  1835: goto            1920
        //  1838: aload           9
        //  1840: astore_3       
        //  1841: aload           6
        //  1843: astore          8
        //  1845: aload_3        
        //  1846: ifnull          1961
        //  1849: aload           6
        //  1851: astore          8
        //  1853: aload_3        
        //  1854: invokeinterface android/database/Cursor.close:()V
        //  1859: aload           6
        //  1861: astore          8
        //  1863: goto            1961
        //  1866: astore          6
        //  1868: aload           6
        //  1870: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //  1873: goto            1961
        //  1876: astore          6
        //  1878: goto            1888
        //  1881: astore          8
        //  1883: goto            1903
        //  1886: astore          6
        //  1888: aload           6
        //  1890: astore          8
        //  1892: aload_3        
        //  1893: astore          6
        //  1895: aload           8
        //  1897: astore_3       
        //  1898: goto            1985
        //  1901: astore          8
        //  1903: goto            1920
        //  1906: astore_3       
        //  1907: aconst_null    
        //  1908: astore          6
        //  1910: goto            1985
        //  1913: astore          8
        //  1915: aconst_null    
        //  1916: astore          6
        //  1918: aconst_null    
        //  1919: astore_3       
        //  1920: aload_3        
        //  1921: astore_2       
        //  1922: aload           8
        //  1924: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //  1927: aload           6
        //  1929: ifnull          1939
        //  1932: aload_3        
        //  1933: astore_2       
        //  1934: aload           6
        //  1936: invokevirtual   java/util/HashMap.clear:()V
        //  1939: aload           6
        //  1941: astore          8
        //  1943: aload_3        
        //  1944: ifnull          1961
        //  1947: aload           6
        //  1949: astore          8
        //  1951: aload_3        
        //  1952: invokeinterface android/database/Cursor.close:()V
        //  1957: aload           6
        //  1959: astore          8
        //  1961: aload           8
        //  1963: ifnull          1969
        //  1966: goto            1978
        //  1969: new             Ljava/util/HashMap;
        //  1972: dup            
        //  1973: invokespecial   java/util/HashMap.<init>:()V
        //  1976: astore          8
        //  1978: aload           8
        //  1980: areturn        
        //  1981: astore_3       
        //  1982: aload_2        
        //  1983: astore          6
        //  1985: aload           6
        //  1987: ifnull          2007
        //  1990: aload           6
        //  1992: invokeinterface android/database/Cursor.close:()V
        //  1997: goto            2007
        //  2000: astore          6
        //  2002: aload           6
        //  2004: invokestatic    org/telegram/messenger/FileLog.e:(Ljava/lang/Throwable;)V
        //  2007: aload_3        
        //  2008: athrow         
        //  2009: astore          6
        //  2011: goto            1167
        //  2014: astore_3       
        //  2015: goto            1825
        //    Signature:
        //  ()Ljava/util/HashMap<Ljava/lang/String;Lorg/telegram/messenger/ContactsController$Contact;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  62     113    1913   1920   Ljava/lang/Throwable;
        //  62     113    1906   1913   Any
        //  118    127    1179   1192   Ljava/lang/Throwable;
        //  118    127    1175   1176   Any
        //  132    144    1179   1192   Ljava/lang/Throwable;
        //  132    144    1175   1176   Any
        //  151    161    1147   1152   Ljava/lang/Throwable;
        //  151    161    1175   1176   Any
        //  165    175    1147   1152   Ljava/lang/Throwable;
        //  165    175    1175   1176   Any
        //  179    189    1147   1152   Ljava/lang/Throwable;
        //  179    189    1175   1176   Any
        //  206    217    1147   1152   Ljava/lang/Throwable;
        //  206    217    1175   1176   Any
        //  230    238    1147   1152   Ljava/lang/Throwable;
        //  230    238    1175   1176   Any
        //  258    266    1147   1152   Ljava/lang/Throwable;
        //  258    266    1175   1176   Any
        //  270    278    1147   1152   Ljava/lang/Throwable;
        //  270    278    1175   1176   Any
        //  298    308    1147   1152   Ljava/lang/Throwable;
        //  298    308    1175   1176   Any
        //  313    321    328    332    Ljava/lang/Throwable;
        //  313    321    324    328    Any
        //  340    350    1147   1152   Ljava/lang/Throwable;
        //  340    350    1175   1176   Any
        //  354    359    1147   1152   Ljava/lang/Throwable;
        //  354    359    1175   1176   Any
        //  363    369    1147   1152   Ljava/lang/Throwable;
        //  363    369    1175   1176   Any
        //  373    379    1147   1152   Ljava/lang/Throwable;
        //  373    379    1175   1176   Any
        //  383    395    1147   1152   Ljava/lang/Throwable;
        //  383    395    1175   1176   Any
        //  413    421    328    332    Ljava/lang/Throwable;
        //  413    421    324    328    Any
        //  434    500    328    332    Ljava/lang/Throwable;
        //  434    500    324    328    Any
        //  533    542    1147   1152   Ljava/lang/Throwable;
        //  533    542    1175   1176   Any
        //  547    555    328    332    Ljava/lang/Throwable;
        //  547    555    324    328    Any
        //  559    569    1147   1152   Ljava/lang/Throwable;
        //  559    569    1175   1176   Any
        //  573    585    1147   1152   Ljava/lang/Throwable;
        //  573    585    1175   1176   Any
        //  594    599    1147   1152   Ljava/lang/Throwable;
        //  594    599    1175   1176   Any
        //  603    608    1147   1152   Ljava/lang/Throwable;
        //  603    608    1175   1176   Any
        //  612    622    1147   1152   Ljava/lang/Throwable;
        //  612    622    1175   1176   Any
        //  638    645    1147   1152   Ljava/lang/Throwable;
        //  638    645    1175   1176   Any
        //  649    657    1147   1152   Ljava/lang/Throwable;
        //  649    657    1175   1176   Any
        //  662    676    328    332    Ljava/lang/Throwable;
        //  662    676    324    328    Any
        //  683    692    1147   1152   Ljava/lang/Throwable;
        //  683    692    1175   1176   Any
        //  702    718    1147   1152   Ljava/lang/Throwable;
        //  702    718    1175   1176   Any
        //  722    739    1147   1152   Ljava/lang/Throwable;
        //  722    739    1175   1176   Any
        //  746    753    1147   1152   Ljava/lang/Throwable;
        //  746    753    1175   1176   Any
        //  757    764    1147   1152   Ljava/lang/Throwable;
        //  757    764    1175   1176   Any
        //  768    775    1147   1152   Ljava/lang/Throwable;
        //  768    775    1175   1176   Any
        //  779    786    1147   1152   Ljava/lang/Throwable;
        //  779    786    1175   1176   Any
        //  790    797    1147   1152   Ljava/lang/Throwable;
        //  790    797    1175   1176   Any
        //  801    808    1147   1152   Ljava/lang/Throwable;
        //  801    808    1175   1176   Any
        //  812    822    1147   1152   Ljava/lang/Throwable;
        //  812    822    1175   1176   Any
        //  838    848    1147   1152   Ljava/lang/Throwable;
        //  838    848    1175   1176   Any
        //  852    862    1147   1152   Ljava/lang/Throwable;
        //  852    862    1175   1176   Any
        //  866    878    1147   1152   Ljava/lang/Throwable;
        //  866    878    1175   1176   Any
        //  887    897    1147   1152   Ljava/lang/Throwable;
        //  887    897    1175   1176   Any
        //  901    907    1147   1152   Ljava/lang/Throwable;
        //  901    907    1175   1176   Any
        //  919    930    1147   1152   Ljava/lang/Throwable;
        //  919    930    1175   1176   Any
        //  934    942    1147   1152   Ljava/lang/Throwable;
        //  934    942    1175   1176   Any
        //  955    972    1147   1152   Ljava/lang/Throwable;
        //  955    972    1175   1176   Any
        //  985    1002   1147   1152   Ljava/lang/Throwable;
        //  985    1002   1175   1176   Any
        //  1015   1032   1147   1152   Ljava/lang/Throwable;
        //  1015   1032   1175   1176   Any
        //  1046   1063   1147   1152   Ljava/lang/Throwable;
        //  1046   1063   1175   1176   Any
        //  1070   1087   1147   1152   Ljava/lang/Throwable;
        //  1070   1087   1175   1176   Any
        //  1091   1100   1147   1152   Ljava/lang/Throwable;
        //  1091   1100   1175   1176   Any
        //  1137   1144   2009   2014   Ljava/lang/Exception;
        //  1137   1144   1147   1152   Ljava/lang/Throwable;
        //  1137   1144   1175   1176   Any
        //  1203   1216   1901   1903   Ljava/lang/Throwable;
        //  1203   1216   1886   1888   Any
        //  1216   1310   1881   1886   Ljava/lang/Throwable;
        //  1216   1310   1876   1881   Any
        //  1318   1328   1830   1838   Ljava/lang/Throwable;
        //  1318   1328   1981   1985   Any
        //  1331   1340   1830   1838   Ljava/lang/Throwable;
        //  1331   1340   1981   1985   Any
        //  1343   1353   1830   1838   Ljava/lang/Throwable;
        //  1343   1353   1981   1985   Any
        //  1356   1366   1830   1838   Ljava/lang/Throwable;
        //  1356   1366   1981   1985   Any
        //  1369   1378   1830   1838   Ljava/lang/Throwable;
        //  1369   1378   1981   1985   Any
        //  1381   1391   1830   1838   Ljava/lang/Throwable;
        //  1381   1391   1981   1985   Any
        //  1398   1405   1830   1838   Ljava/lang/Throwable;
        //  1398   1405   1981   1985   Any
        //  1408   1414   1830   1838   Ljava/lang/Throwable;
        //  1408   1414   1981   1985   Any
        //  1427   1433   1830   1838   Ljava/lang/Throwable;
        //  1427   1433   1981   1985   Any
        //  1439   1445   1830   1838   Ljava/lang/Throwable;
        //  1439   1445   1981   1985   Any
        //  1453   1459   1830   1838   Ljava/lang/Throwable;
        //  1453   1459   1981   1985   Any
        //  1465   1471   1830   1838   Ljava/lang/Throwable;
        //  1465   1471   1981   1985   Any
        //  1474   1481   1830   1838   Ljava/lang/Throwable;
        //  1474   1481   1981   1985   Any
        //  1484   1494   1830   1838   Ljava/lang/Throwable;
        //  1484   1494   1981   1985   Any
        //  1497   1502   1830   1838   Ljava/lang/Throwable;
        //  1497   1502   1981   1985   Any
        //  1505   1510   1830   1838   Ljava/lang/Throwable;
        //  1505   1510   1981   1985   Any
        //  1513   1523   1830   1838   Ljava/lang/Throwable;
        //  1513   1523   1981   1985   Any
        //  1526   1535   1830   1838   Ljava/lang/Throwable;
        //  1526   1535   1981   1985   Any
        //  1538   1545   1830   1838   Ljava/lang/Throwable;
        //  1538   1545   1981   1985   Any
        //  1548   1557   1830   1838   Ljava/lang/Throwable;
        //  1548   1557   1981   1985   Any
        //  1563   1568   1830   1838   Ljava/lang/Throwable;
        //  1563   1568   1981   1985   Any
        //  1574   1583   1830   1838   Ljava/lang/Throwable;
        //  1574   1583   1981   1985   Any
        //  1586   1598   1830   1838   Ljava/lang/Throwable;
        //  1586   1598   1981   1985   Any
        //  1601   1613   1830   1838   Ljava/lang/Throwable;
        //  1601   1613   1981   1985   Any
        //  1616   1625   1830   1838   Ljava/lang/Throwable;
        //  1616   1625   1981   1985   Any
        //  1628   1640   1830   1838   Ljava/lang/Throwable;
        //  1628   1640   1981   1985   Any
        //  1643   1655   1830   1838   Ljava/lang/Throwable;
        //  1643   1655   1981   1985   Any
        //  1663   1669   1830   1838   Ljava/lang/Throwable;
        //  1663   1669   1981   1985   Any
        //  1675   1681   1830   1838   Ljava/lang/Throwable;
        //  1675   1681   1981   1985   Any
        //  1684   1691   1830   1838   Ljava/lang/Throwable;
        //  1684   1691   1981   1985   Any
        //  1694   1704   1830   1838   Ljava/lang/Throwable;
        //  1694   1704   1981   1985   Any
        //  1707   1712   1830   1838   Ljava/lang/Throwable;
        //  1707   1712   1981   1985   Any
        //  1715   1720   1830   1838   Ljava/lang/Throwable;
        //  1715   1720   1981   1985   Any
        //  1723   1733   1830   1838   Ljava/lang/Throwable;
        //  1723   1733   1981   1985   Any
        //  1736   1745   1830   1838   Ljava/lang/Throwable;
        //  1736   1745   1981   1985   Any
        //  1748   1755   1830   1838   Ljava/lang/Throwable;
        //  1748   1755   1981   1985   Any
        //  1758   1767   1830   1838   Ljava/lang/Throwable;
        //  1758   1767   1981   1985   Any
        //  1773   1778   1830   1838   Ljava/lang/Throwable;
        //  1773   1778   1981   1985   Any
        //  1786   1792   1830   1838   Ljava/lang/Throwable;
        //  1786   1792   1981   1985   Any
        //  1798   1804   1830   1838   Ljava/lang/Throwable;
        //  1798   1804   1981   1985   Any
        //  1807   1812   1830   1838   Ljava/lang/Throwable;
        //  1807   1812   1981   1985   Any
        //  1818   1825   2014   2018   Ljava/lang/Exception;
        //  1818   1825   1830   1838   Ljava/lang/Throwable;
        //  1818   1825   1981   1985   Any
        //  1853   1859   1866   1876   Ljava/lang/Exception;
        //  1922   1927   1981   1985   Any
        //  1934   1939   1981   1985   Any
        //  1951   1957   1866   1876   Ljava/lang/Exception;
        //  1990   1997   2000   2007   Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.util.ConcurrentModificationException
        //     at java.base/java.util.ArrayList$Itr.checkForComodification(ArrayList.java:937)
        //     at java.base/java.util.ArrayList$Itr.next(ArrayList.java:891)
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2863)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2445)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private void reloadContactsStatusesMaybe() {
        try {
            if (MessagesController.getMainSettings(this.currentAccount).getLong("lastReloadStatusTime", 0L) < System.currentTimeMillis() - 86400000L) {
                this.reloadContactsStatuses();
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    private void saveContactsLoadTime() {
        try {
            MessagesController.getMainSettings(this.currentAccount).edit().putLong("lastReloadStatusTime", System.currentTimeMillis()).commit();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    private void updateUnregisteredContacts() {
        final HashMap<String, TLRPC.TL_contact> hashMap = new HashMap<String, TLRPC.TL_contact>();
        for (int size = this.contacts.size(), i = 0; i < size; ++i) {
            final TLRPC.TL_contact value = this.contacts.get(i);
            final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(value.user_id);
            if (user != null) {
                if (!TextUtils.isEmpty((CharSequence)user.phone)) {
                    hashMap.put(user.phone, value);
                }
            }
        }
        final ArrayList<Object> list = new ArrayList<Object>();
        final Iterator<Map.Entry<String, Contact>> iterator = this.contactsBook.entrySet().iterator();
        while (iterator.hasNext()) {
            final Contact e = iterator.next().getValue();
            int n = 0;
            int n2;
            while (true) {
                final int size2 = e.phones.size();
                final boolean b = true;
                if (n >= size2) {
                    n2 = 0;
                    break;
                }
                n2 = (b ? 1 : 0);
                if (hashMap.containsKey(e.shortPhones.get(n))) {
                    break;
                }
                if (e.phoneDeleted.get(n) == 1) {
                    n2 = (b ? 1 : 0);
                    break;
                }
                ++n;
            }
            if (n2 != 0) {
                continue;
            }
            list.add(e);
        }
        Collections.sort(list, (Comparator<? super Object>)_$$Lambda$ContactsController$mHBJOOEPuO6QrX7ZbJrnWuc_NBQ.INSTANCE);
        this.phoneBookContacts = (ArrayList<Contact>)list;
    }
    
    public void addContact(final TLRPC.User user) {
        if (user != null) {
            if (!TextUtils.isEmpty((CharSequence)user.phone)) {
                final TLRPC.TL_contacts_importContacts tl_contacts_importContacts = new TLRPC.TL_contacts_importContacts();
                final ArrayList<TLRPC.TL_inputPhoneContact> contacts = new ArrayList<TLRPC.TL_inputPhoneContact>();
                final TLRPC.TL_inputPhoneContact e = new TLRPC.TL_inputPhoneContact();
                e.phone = user.phone;
                if (!e.phone.startsWith("+")) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("+");
                    sb.append(e.phone);
                    e.phone = sb.toString();
                }
                e.first_name = user.first_name;
                e.last_name = user.last_name;
                e.client_id = 0L;
                contacts.add(e);
                tl_contacts_importContacts.contacts = contacts;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_contacts_importContacts, new _$$Lambda$ContactsController$_Vi_J8_93cHW3trChoxTwI17XeA(this), 6);
            }
        }
    }
    
    public long addContactToPhoneBook(final TLRPC.User p0, final boolean p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        org/telegram/messenger/ContactsController.systemAccount:Landroid/accounts/Account;
        //     4: astore_3       
        //     5: ldc2_w          -1
        //     8: lstore          4
        //    10: aload_3        
        //    11: ifnull          512
        //    14: aload_1        
        //    15: ifnull          512
        //    18: aload_1        
        //    19: getfield        org/telegram/tgnet/TLRPC$User.phone:Ljava/lang/String;
        //    22: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //    25: ifeq            31
        //    28: goto            512
        //    31: aload_0        
        //    32: invokespecial   org/telegram/messenger/ContactsController.hasContactsPermission:()Z
        //    35: ifne            42
        //    38: ldc2_w          -1
        //    41: lreturn        
        //    42: aload_0        
        //    43: getfield        org/telegram/messenger/ContactsController.observerLock:Ljava/lang/Object;
        //    46: astore_3       
        //    47: aload_3        
        //    48: monitorenter   
        //    49: aload_0        
        //    50: iconst_1       
        //    51: putfield        org/telegram/messenger/ContactsController.ignoreChanges:Z
        //    54: aload_3        
        //    55: monitorexit    
        //    56: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //    59: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
        //    62: astore_3       
        //    63: iload_2        
        //    64: ifeq            154
        //    67: getstatic       android/provider/ContactsContract$RawContacts.CONTENT_URI:Landroid/net/Uri;
        //    70: invokevirtual   android/net/Uri.buildUpon:()Landroid/net/Uri$Builder;
        //    73: ldc_w           "caller_is_syncadapter"
        //    76: ldc_w           "true"
        //    79: invokevirtual   android/net/Uri$Builder.appendQueryParameter:(Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri$Builder;
        //    82: ldc_w           "account_name"
        //    85: aload_0        
        //    86: getfield        org/telegram/messenger/ContactsController.systemAccount:Landroid/accounts/Account;
        //    89: getfield        android/accounts/Account.name:Ljava/lang/String;
        //    92: invokevirtual   android/net/Uri$Builder.appendQueryParameter:(Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri$Builder;
        //    95: ldc             "account_type"
        //    97: aload_0        
        //    98: getfield        org/telegram/messenger/ContactsController.systemAccount:Landroid/accounts/Account;
        //   101: getfield        android/accounts/Account.type:Ljava/lang/String;
        //   104: invokevirtual   android/net/Uri$Builder.appendQueryParameter:(Ljava/lang/String;Ljava/lang/String;)Landroid/net/Uri$Builder;
        //   107: invokevirtual   android/net/Uri$Builder.build:()Landroid/net/Uri;
        //   110: astore          6
        //   112: new             Ljava/lang/StringBuilder;
        //   115: astore          7
        //   117: aload           7
        //   119: invokespecial   java/lang/StringBuilder.<init>:()V
        //   122: aload           7
        //   124: ldc_w           "sync2 = "
        //   127: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   130: pop            
        //   131: aload           7
        //   133: aload_1        
        //   134: getfield        org/telegram/tgnet/TLRPC$User.id:I
        //   137: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   140: pop            
        //   141: aload_3        
        //   142: aload           6
        //   144: aload           7
        //   146: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   149: aconst_null    
        //   150: invokevirtual   android/content/ContentResolver.delete:(Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)I
        //   153: pop            
        //   154: new             Ljava/util/ArrayList;
        //   157: dup            
        //   158: invokespecial   java/util/ArrayList.<init>:()V
        //   161: astore          7
        //   163: getstatic       android/provider/ContactsContract$RawContacts.CONTENT_URI:Landroid/net/Uri;
        //   166: invokestatic    android/content/ContentProviderOperation.newInsert:(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
        //   169: astore          6
        //   171: aload           6
        //   173: ldc_w           "account_name"
        //   176: aload_0        
        //   177: getfield        org/telegram/messenger/ContactsController.systemAccount:Landroid/accounts/Account;
        //   180: getfield        android/accounts/Account.name:Ljava/lang/String;
        //   183: invokevirtual   android/content/ContentProviderOperation$Builder.withValue:(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
        //   186: pop            
        //   187: aload           6
        //   189: ldc             "account_type"
        //   191: aload_0        
        //   192: getfield        org/telegram/messenger/ContactsController.systemAccount:Landroid/accounts/Account;
        //   195: getfield        android/accounts/Account.type:Ljava/lang/String;
        //   198: invokevirtual   android/content/ContentProviderOperation$Builder.withValue:(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
        //   201: pop            
        //   202: aload           6
        //   204: ldc_w           "sync1"
        //   207: aload_1        
        //   208: getfield        org/telegram/tgnet/TLRPC$User.phone:Ljava/lang/String;
        //   211: invokevirtual   android/content/ContentProviderOperation$Builder.withValue:(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
        //   214: pop            
        //   215: aload           6
        //   217: ldc_w           "sync2"
        //   220: aload_1        
        //   221: getfield        org/telegram/tgnet/TLRPC$User.id:I
        //   224: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   227: invokevirtual   android/content/ContentProviderOperation$Builder.withValue:(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
        //   230: pop            
        //   231: aload           7
        //   233: aload           6
        //   235: invokevirtual   android/content/ContentProviderOperation$Builder.build:()Landroid/content/ContentProviderOperation;
        //   238: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   241: pop            
        //   242: getstatic       android/provider/ContactsContract$Data.CONTENT_URI:Landroid/net/Uri;
        //   245: invokestatic    android/content/ContentProviderOperation.newInsert:(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
        //   248: astore          6
        //   250: aload           6
        //   252: ldc_w           "raw_contact_id"
        //   255: iconst_0       
        //   256: invokevirtual   android/content/ContentProviderOperation$Builder.withValueBackReference:(Ljava/lang/String;I)Landroid/content/ContentProviderOperation$Builder;
        //   259: pop            
        //   260: aload           6
        //   262: ldc_w           "mimetype"
        //   265: ldc_w           "vnd.android.cursor.item/name"
        //   268: invokevirtual   android/content/ContentProviderOperation$Builder.withValue:(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
        //   271: pop            
        //   272: aload           6
        //   274: ldc             "data2"
        //   276: aload_1        
        //   277: getfield        org/telegram/tgnet/TLRPC$User.first_name:Ljava/lang/String;
        //   280: invokevirtual   android/content/ContentProviderOperation$Builder.withValue:(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
        //   283: pop            
        //   284: aload           6
        //   286: ldc             "data3"
        //   288: aload_1        
        //   289: getfield        org/telegram/tgnet/TLRPC$User.last_name:Ljava/lang/String;
        //   292: invokevirtual   android/content/ContentProviderOperation$Builder.withValue:(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
        //   295: pop            
        //   296: aload           7
        //   298: aload           6
        //   300: invokevirtual   android/content/ContentProviderOperation$Builder.build:()Landroid/content/ContentProviderOperation;
        //   303: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   306: pop            
        //   307: getstatic       android/provider/ContactsContract$Data.CONTENT_URI:Landroid/net/Uri;
        //   310: invokestatic    android/content/ContentProviderOperation.newInsert:(Landroid/net/Uri;)Landroid/content/ContentProviderOperation$Builder;
        //   313: astore          8
        //   315: aload           8
        //   317: ldc_w           "raw_contact_id"
        //   320: iconst_0       
        //   321: invokevirtual   android/content/ContentProviderOperation$Builder.withValueBackReference:(Ljava/lang/String;I)Landroid/content/ContentProviderOperation$Builder;
        //   324: pop            
        //   325: aload           8
        //   327: ldc_w           "mimetype"
        //   330: ldc_w           "vnd.android.cursor.item/vnd.org.telegram.messenger.android.profile"
        //   333: invokevirtual   android/content/ContentProviderOperation$Builder.withValue:(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
        //   336: pop            
        //   337: aload           8
        //   339: ldc             "data1"
        //   341: aload_1        
        //   342: getfield        org/telegram/tgnet/TLRPC$User.id:I
        //   345: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   348: invokevirtual   android/content/ContentProviderOperation$Builder.withValue:(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
        //   351: pop            
        //   352: aload           8
        //   354: ldc             "data2"
        //   356: ldc_w           "Telegram Profile"
        //   359: invokevirtual   android/content/ContentProviderOperation$Builder.withValue:(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
        //   362: pop            
        //   363: new             Ljava/lang/StringBuilder;
        //   366: dup            
        //   367: invokespecial   java/lang/StringBuilder.<init>:()V
        //   370: astore          6
        //   372: aload           6
        //   374: ldc_w           "+"
        //   377: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   380: pop            
        //   381: aload           6
        //   383: aload_1        
        //   384: getfield        org/telegram/tgnet/TLRPC$User.phone:Ljava/lang/String;
        //   387: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   390: pop            
        //   391: aload           8
        //   393: ldc             "data3"
        //   395: aload           6
        //   397: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   400: invokevirtual   android/content/ContentProviderOperation$Builder.withValue:(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
        //   403: pop            
        //   404: aload           8
        //   406: ldc_w           "data4"
        //   409: aload_1        
        //   410: getfield        org/telegram/tgnet/TLRPC$User.id:I
        //   413: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   416: invokevirtual   android/content/ContentProviderOperation$Builder.withValue:(Ljava/lang/String;Ljava/lang/Object;)Landroid/content/ContentProviderOperation$Builder;
        //   419: pop            
        //   420: aload           7
        //   422: aload           8
        //   424: invokevirtual   android/content/ContentProviderOperation$Builder.build:()Landroid/content/ContentProviderOperation;
        //   427: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   430: pop            
        //   431: aload_3        
        //   432: ldc_w           "com.android.contacts"
        //   435: aload           7
        //   437: invokevirtual   android/content/ContentResolver.applyBatch:(Ljava/lang/String;Ljava/util/ArrayList;)[Landroid/content/ContentProviderResult;
        //   440: astore_1       
        //   441: lload           4
        //   443: lstore          9
        //   445: aload_1        
        //   446: ifnull          485
        //   449: lload           4
        //   451: lstore          9
        //   453: aload_1        
        //   454: arraylength    
        //   455: ifle            485
        //   458: lload           4
        //   460: lstore          9
        //   462: aload_1        
        //   463: iconst_0       
        //   464: aaload         
        //   465: getfield        android/content/ContentProviderResult.uri:Landroid/net/Uri;
        //   468: ifnull          485
        //   471: aload_1        
        //   472: iconst_0       
        //   473: aaload         
        //   474: getfield        android/content/ContentProviderResult.uri:Landroid/net/Uri;
        //   477: invokevirtual   android/net/Uri.getLastPathSegment:()Ljava/lang/String;
        //   480: invokestatic    java/lang/Long.parseLong:(Ljava/lang/String;)J
        //   483: lstore          9
        //   485: aload_0        
        //   486: getfield        org/telegram/messenger/ContactsController.observerLock:Ljava/lang/Object;
        //   489: astore_3       
        //   490: aload_3        
        //   491: monitorenter   
        //   492: aload_0        
        //   493: iconst_0       
        //   494: putfield        org/telegram/messenger/ContactsController.ignoreChanges:Z
        //   497: aload_3        
        //   498: monitorexit    
        //   499: lload           9
        //   501: lreturn        
        //   502: astore_1       
        //   503: aload_3        
        //   504: monitorexit    
        //   505: aload_1        
        //   506: athrow         
        //   507: astore_1       
        //   508: aload_3        
        //   509: monitorexit    
        //   510: aload_1        
        //   511: athrow         
        //   512: ldc2_w          -1
        //   515: lreturn        
        //   516: astore          7
        //   518: goto            154
        //   521: astore_1       
        //   522: lload           4
        //   524: lstore          9
        //   526: goto            485
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  49     56     507    512    Any
        //  67     154    516    521    Ljava/lang/Exception;
        //  431    441    521    529    Ljava/lang/Exception;
        //  453    458    521    529    Ljava/lang/Exception;
        //  462    485    521    529    Ljava/lang/Exception;
        //  492    499    502    507    Any
        //  503    505    502    507    Any
        //  508    510    507    512    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 254 out-of-bounds for length 254
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:439)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public void checkAppAccount() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: invokestatic    android/accounts/AccountManager.get:(Landroid/content/Context;)Landroid/accounts/AccountManager;
        //     6: astore_1       
        //     7: aload_1        
        //     8: ldc_w           "org.telegram.messenger"
        //    11: invokevirtual   android/accounts/AccountManager.getAccountsByType:(Ljava/lang/String;)[Landroid/accounts/Account;
        //    14: astore_2       
        //    15: aload_0        
        //    16: aconst_null    
        //    17: putfield        org/telegram/messenger/ContactsController.systemAccount:Landroid/accounts/Account;
        //    20: iconst_0       
        //    21: istore_3       
        //    22: iload_3        
        //    23: aload_2        
        //    24: arraylength    
        //    25: if_icmpge       157
        //    28: aload_2        
        //    29: iload_3        
        //    30: aaload         
        //    31: astore          4
        //    33: iconst_0       
        //    34: istore          5
        //    36: iload           5
        //    38: iconst_3       
        //    39: if_icmpge       133
        //    42: iload           5
        //    44: invokestatic    org/telegram/messenger/UserConfig.getInstance:(I)Lorg/telegram/messenger/UserConfig;
        //    47: invokevirtual   org/telegram/messenger/UserConfig.getCurrentUser:()Lorg/telegram/tgnet/TLRPC$User;
        //    50: astore          6
        //    52: aload           6
        //    54: ifnull          127
        //    57: aload           4
        //    59: getfield        android/accounts/Account.name:Ljava/lang/String;
        //    62: astore          7
        //    64: new             Ljava/lang/StringBuilder;
        //    67: astore          8
        //    69: aload           8
        //    71: invokespecial   java/lang/StringBuilder.<init>:()V
        //    74: aload           8
        //    76: ldc             ""
        //    78: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    81: pop            
        //    82: aload           8
        //    84: aload           6
        //    86: getfield        org/telegram/tgnet/TLRPC$User.id:I
        //    89: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //    92: pop            
        //    93: aload           7
        //    95: aload           8
        //    97: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   100: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   103: ifeq            127
        //   106: iload           5
        //   108: aload_0        
        //   109: getfield        org/telegram/messenger/ContactsController.currentAccount:I
        //   112: if_icmpne       121
        //   115: aload_0        
        //   116: aload           4
        //   118: putfield        org/telegram/messenger/ContactsController.systemAccount:Landroid/accounts/Account;
        //   121: iconst_1       
        //   122: istore          5
        //   124: goto            136
        //   127: iinc            5, 1
        //   130: goto            36
        //   133: iconst_0       
        //   134: istore          5
        //   136: iload           5
        //   138: ifne            151
        //   141: aload_1        
        //   142: aload_2        
        //   143: iload_3        
        //   144: aaload         
        //   145: aconst_null    
        //   146: aconst_null    
        //   147: invokevirtual   android/accounts/AccountManager.removeAccount:(Landroid/accounts/Account;Landroid/accounts/AccountManagerCallback;Landroid/os/Handler;)Landroid/accounts/AccountManagerFuture;
        //   150: pop            
        //   151: iinc            3, 1
        //   154: goto            22
        //   157: aload_0        
        //   158: getfield        org/telegram/messenger/ContactsController.currentAccount:I
        //   161: invokestatic    org/telegram/messenger/UserConfig.getInstance:(I)Lorg/telegram/messenger/UserConfig;
        //   164: invokevirtual   org/telegram/messenger/UserConfig.isClientActivated:()Z
        //   167: ifeq            248
        //   170: aload_0        
        //   171: invokevirtual   org/telegram/messenger/ContactsController.readContacts:()V
        //   174: aload_0        
        //   175: getfield        org/telegram/messenger/ContactsController.systemAccount:Landroid/accounts/Account;
        //   178: ifnonnull       248
        //   181: new             Landroid/accounts/Account;
        //   184: astore_2       
        //   185: new             Ljava/lang/StringBuilder;
        //   188: astore          6
        //   190: aload           6
        //   192: invokespecial   java/lang/StringBuilder.<init>:()V
        //   195: aload           6
        //   197: ldc             ""
        //   199: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   202: pop            
        //   203: aload           6
        //   205: aload_0        
        //   206: getfield        org/telegram/messenger/ContactsController.currentAccount:I
        //   209: invokestatic    org/telegram/messenger/UserConfig.getInstance:(I)Lorg/telegram/messenger/UserConfig;
        //   212: invokevirtual   org/telegram/messenger/UserConfig.getClientUserId:()I
        //   215: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   218: pop            
        //   219: aload_2        
        //   220: aload           6
        //   222: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   225: ldc_w           "org.telegram.messenger"
        //   228: invokespecial   android/accounts/Account.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //   231: aload_0        
        //   232: aload_2        
        //   233: putfield        org/telegram/messenger/ContactsController.systemAccount:Landroid/accounts/Account;
        //   236: aload_1        
        //   237: aload_0        
        //   238: getfield        org/telegram/messenger/ContactsController.systemAccount:Landroid/accounts/Account;
        //   241: ldc             ""
        //   243: aconst_null    
        //   244: invokevirtual   android/accounts/AccountManager.addAccountExplicitly:(Landroid/accounts/Account;Ljava/lang/String;Landroid/os/Bundle;)Z
        //   247: pop            
        //   248: return         
        //   249: astore_2       
        //   250: goto            157
        //   253: astore          6
        //   255: goto            151
        //   258: astore_1       
        //   259: goto            248
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  7      20     249    253    Ljava/lang/Throwable;
        //  22     28     249    253    Ljava/lang/Throwable;
        //  42     52     249    253    Ljava/lang/Throwable;
        //  57     121    249    253    Ljava/lang/Throwable;
        //  141    151    253    258    Ljava/lang/Exception;
        //  141    151    249    253    Ljava/lang/Throwable;
        //  181    248    258    262    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public void checkContacts() {
        Utilities.globalQueue.postRunnable(new _$$Lambda$ContactsController$gch7bRXld1l_k0g1GAfFCoMhjIs(this));
    }
    
    public void checkInviteText() {
        final SharedPreferences mainSettings = MessagesController.getMainSettings(this.currentAccount);
        this.inviteLink = mainSettings.getString("invitelink", (String)null);
        final int int1 = mainSettings.getInt("invitelinktime", 0);
        if (!this.updatingInviteLink && (this.inviteLink == null || Math.abs(System.currentTimeMillis() / 1000L - int1) >= 86400L)) {
            this.updatingInviteLink = true;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC.TL_help_getInviteText(), new _$$Lambda$ContactsController$kcKNthDEzlD6nPSHQklW5Vw14V8(this), 2);
        }
    }
    
    public void cleanup() {
        this.contactsBook.clear();
        this.contactsBookSPhones.clear();
        this.phoneBookContacts.clear();
        this.contacts.clear();
        this.contactsDict.clear();
        this.usersSectionsDict.clear();
        this.usersMutualSectionsDict.clear();
        this.sortedUsersSectionsArray.clear();
        this.sortedUsersMutualSectionsArray.clear();
        this.delayedContactsUpdate.clear();
        this.contactsByPhone.clear();
        this.contactsByShortPhone.clear();
        this.phoneBookSectionsDict.clear();
        this.phoneBookSectionsArray.clear();
        this.loadingContacts = false;
        this.contactsSyncInProgress = false;
        this.contactsLoaded = false;
        this.contactsBookLoaded = false;
        this.lastContactsVersions = "";
        this.loadingDeleteInfo = 0;
        this.deleteAccountTTL = 0;
        int n = 0;
        while (true) {
            final int[] loadingPrivacyInfo = this.loadingPrivacyInfo;
            if (n >= loadingPrivacyInfo.length) {
                break;
            }
            loadingPrivacyInfo[n] = 0;
            ++n;
        }
        this.lastseenPrivacyRules = null;
        this.groupPrivacyRules = null;
        this.callPrivacyRules = null;
        this.p2pPrivacyRules = null;
        this.profilePhotoPrivacyRules = null;
        this.forwardsPrivacyRules = null;
        this.phonePrivacyRules = null;
        Utilities.globalQueue.postRunnable(new _$$Lambda$ContactsController$6JQLrbPbpNJhoOI_5BGOSFa9lbo(this));
    }
    
    public void createOrUpdateConnectionServiceContact(final int n, final String s, final String s2) {
        if (!this.hasContactsPermission()) {
            return;
        }
        try {
            final ContentResolver contentResolver = ApplicationLoader.applicationContext.getContentResolver();
            final ArrayList<ContentProviderOperation> list = new ArrayList<ContentProviderOperation>();
            final Uri build = ContactsContract$Groups.CONTENT_URI.buildUpon().appendQueryParameter("caller_is_syncadapter", "true").build();
            final Uri build2 = ContactsContract$RawContacts.CONTENT_URI.buildUpon().appendQueryParameter("caller_is_syncadapter", "true").build();
            final Cursor query = contentResolver.query(build, new String[] { "_id" }, "title=? AND account_type=? AND account_name=?", new String[] { "TelegramConnectionService", this.systemAccount.type, this.systemAccount.name }, (String)null);
            int n2;
            if (query != null && query.moveToFirst()) {
                n2 = query.getInt(0);
            }
            else {
                final ContentValues contentValues = new ContentValues();
                contentValues.put("account_type", this.systemAccount.type);
                contentValues.put("account_name", this.systemAccount.name);
                contentValues.put("group_visible", Integer.valueOf(0));
                contentValues.put("group_is_read_only", Integer.valueOf(1));
                contentValues.put("title", "TelegramConnectionService");
                n2 = Integer.parseInt(contentResolver.insert(build, contentValues).getLastPathSegment());
            }
            if (query != null) {
                query.close();
            }
            final Uri content_URI = ContactsContract$Data.CONTENT_URI;
            final StringBuilder sb = new StringBuilder();
            sb.append(n2);
            sb.append("");
            final Cursor query2 = contentResolver.query(content_URI, new String[] { "raw_contact_id" }, "mimetype=? AND data1=?", new String[] { "vnd.android.cursor.item/group_membership", sb.toString() }, (String)null);
            final int size = list.size();
            if (query2 != null && query2.moveToFirst()) {
                final int int1 = query2.getInt(0);
                final ContentProviderOperation$Builder update = ContentProviderOperation.newUpdate(build2);
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(int1);
                sb2.append("");
                final ContentProviderOperation build3 = update.withSelection("_id=?", new String[] { sb2.toString() }).withValue("deleted", (Object)0).build();
                final ArrayList<ContentProviderOperation> list2 = list;
                list2.add(build3);
                final ContentProviderOperation$Builder update2 = ContentProviderOperation.newUpdate(ContactsContract$Data.CONTENT_URI);
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(int1);
                sb3.append("");
                final ContentProviderOperation$Builder withSelection = update2.withSelection("raw_contact_id=? AND mimetype=?", new String[] { sb3.toString(), "vnd.android.cursor.item/phone_v2" });
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("+99084");
                sb4.append(n);
                list2.add(withSelection.withValue("data1", (Object)sb4.toString()).build());
                final ContentProviderOperation$Builder update3 = ContentProviderOperation.newUpdate(ContactsContract$Data.CONTENT_URI);
                final StringBuilder sb5 = new StringBuilder();
                sb5.append(int1);
                sb5.append("");
                list2.add(update3.withSelection("raw_contact_id=? AND mimetype=?", new String[] { sb5.toString(), "vnd.android.cursor.item/name" }).withValue("data2", (Object)s).withValue("data3", (Object)s2).build());
            }
            else {
                final ArrayList<ContentProviderOperation> list3 = list;
                list3.add(ContentProviderOperation.newInsert(build2).withValue("account_type", (Object)this.systemAccount.type).withValue("account_name", (Object)this.systemAccount.name).withValue("raw_contact_is_read_only", (Object)1).withValue("aggregation_mode", (Object)3).build());
                list3.add(ContentProviderOperation.newInsert(ContactsContract$Data.CONTENT_URI).withValueBackReference("raw_contact_id", size).withValue("mimetype", (Object)"vnd.android.cursor.item/name").withValue("data2", (Object)s).withValue("data3", (Object)s2).build());
                final ContentProviderOperation$Builder withValue = ContentProviderOperation.newInsert(ContactsContract$Data.CONTENT_URI).withValueBackReference("raw_contact_id", size).withValue("mimetype", (Object)"vnd.android.cursor.item/phone_v2");
                final StringBuilder sb6 = new StringBuilder();
                sb6.append("+99084");
                sb6.append(n);
                list3.add(withValue.withValue("data1", (Object)sb6.toString()).build());
                list3.add(ContentProviderOperation.newInsert(ContactsContract$Data.CONTENT_URI).withValueBackReference("raw_contact_id", size).withValue("mimetype", (Object)"vnd.android.cursor.item/group_membership").withValue("data1", (Object)n2).build());
            }
            if (query2 != null) {
                query2.close();
            }
            contentResolver.applyBatch("com.android.contacts", (ArrayList)list);
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public void deleteAllContacts(final Runnable runnable) {
        this.resetImportedContacts();
        final TLRPC.TL_contacts_deleteContacts tl_contacts_deleteContacts = new TLRPC.TL_contacts_deleteContacts();
        for (int size = this.contacts.size(), i = 0; i < size; ++i) {
            tl_contacts_deleteContacts.id.add(MessagesController.getInstance(this.currentAccount).getInputUser(this.contacts.get(i).user_id));
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_contacts_deleteContacts, new _$$Lambda$ContactsController$Ok5Vywi0AIsSfLPeC7ZAX0Eg0KM(this, runnable));
    }
    
    public void deleteConnectionServiceContact() {
        if (!this.hasContactsPermission()) {
            return;
        }
        try {
            final ContentResolver contentResolver = ApplicationLoader.applicationContext.getContentResolver();
            final Cursor query = contentResolver.query(ContactsContract$Groups.CONTENT_URI, new String[] { "_id" }, "title=? AND account_type=? AND account_name=?", new String[] { "TelegramConnectionService", this.systemAccount.type, this.systemAccount.name }, (String)null);
            if (query == null || !query.moveToFirst()) {
                if (query != null) {
                    query.close();
                }
                return;
            }
            final int int1 = query.getInt(0);
            query.close();
            final Uri content_URI = ContactsContract$Data.CONTENT_URI;
            final StringBuilder sb = new StringBuilder();
            sb.append(int1);
            sb.append("");
            final Cursor query2 = contentResolver.query(content_URI, new String[] { "raw_contact_id" }, "mimetype=? AND data1=?", new String[] { "vnd.android.cursor.item/group_membership", sb.toString() }, (String)null);
            if (query2 == null || !query2.moveToFirst()) {
                if (query2 != null) {
                    query2.close();
                }
                return;
            }
            final int int2 = query2.getInt(0);
            query2.close();
            final Uri content_URI2 = ContactsContract$RawContacts.CONTENT_URI;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(int2);
            sb2.append("");
            contentResolver.delete(content_URI2, "_id=?", new String[] { sb2.toString() });
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
    
    public void deleteContact(final ArrayList<TLRPC.User> list) {
        if (list != null) {
            if (!list.isEmpty()) {
                final TLRPC.TL_contacts_deleteContacts tl_contacts_deleteContacts = new TLRPC.TL_contacts_deleteContacts();
                final ArrayList<Integer> list2 = new ArrayList<Integer>();
                for (final TLRPC.User user : list) {
                    final TLRPC.InputUser inputUser = MessagesController.getInstance(this.currentAccount).getInputUser(user);
                    if (inputUser == null) {
                        continue;
                    }
                    list2.add(user.id);
                    tl_contacts_deleteContacts.id.add(inputUser);
                }
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_contacts_deleteContacts, new _$$Lambda$ContactsController$AcnaGQSIUUZcaRV7Rm4_aExKG2o(this, list2, list));
            }
        }
    }
    
    public void deleteUnknownAppAccounts() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aconst_null    
        //     2: putfield        org/telegram/messenger/ContactsController.systemAccount:Landroid/accounts/Account;
        //     5: getstatic       org/telegram/messenger/ApplicationLoader.applicationContext:Landroid/content/Context;
        //     8: invokestatic    android/accounts/AccountManager.get:(Landroid/content/Context;)Landroid/accounts/AccountManager;
        //    11: astore_1       
        //    12: aload_1        
        //    13: ldc_w           "org.telegram.messenger"
        //    16: invokevirtual   android/accounts/AccountManager.getAccountsByType:(Ljava/lang/String;)[Landroid/accounts/Account;
        //    19: astore_2       
        //    20: iconst_0       
        //    21: istore_3       
        //    22: iload_3        
        //    23: aload_2        
        //    24: arraylength    
        //    25: if_icmpge       151
        //    28: aload_2        
        //    29: iload_3        
        //    30: aaload         
        //    31: astore          4
        //    33: iconst_0       
        //    34: istore          5
        //    36: iload           5
        //    38: iconst_3       
        //    39: if_icmpge       122
        //    42: iload           5
        //    44: invokestatic    org/telegram/messenger/UserConfig.getInstance:(I)Lorg/telegram/messenger/UserConfig;
        //    47: invokevirtual   org/telegram/messenger/UserConfig.getCurrentUser:()Lorg/telegram/tgnet/TLRPC$User;
        //    50: astore          6
        //    52: aload           6
        //    54: ifnull          116
        //    57: aload           4
        //    59: getfield        android/accounts/Account.name:Ljava/lang/String;
        //    62: astore          7
        //    64: new             Ljava/lang/StringBuilder;
        //    67: astore          8
        //    69: aload           8
        //    71: invokespecial   java/lang/StringBuilder.<init>:()V
        //    74: aload           8
        //    76: ldc             ""
        //    78: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    81: pop            
        //    82: aload           8
        //    84: aload           6
        //    86: getfield        org/telegram/tgnet/TLRPC$User.id:I
        //    89: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //    92: pop            
        //    93: aload           7
        //    95: aload           8
        //    97: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   100: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   103: istore          9
        //   105: iload           9
        //   107: ifeq            116
        //   110: iconst_1       
        //   111: istore          5
        //   113: goto            125
        //   116: iinc            5, 1
        //   119: goto            36
        //   122: iconst_0       
        //   123: istore          5
        //   125: iload           5
        //   127: ifne            140
        //   130: aload_1        
        //   131: aload_2        
        //   132: iload_3        
        //   133: aaload         
        //   134: aconst_null    
        //   135: aconst_null    
        //   136: invokevirtual   android/accounts/AccountManager.removeAccount:(Landroid/accounts/Account;Landroid/accounts/AccountManagerCallback;Landroid/os/Handler;)Landroid/accounts/AccountManagerFuture;
        //   139: pop            
        //   140: iinc            3, 1
        //   143: goto            22
        //   146: astore_1       
        //   147: aload_1        
        //   148: invokevirtual   java/lang/Exception.printStackTrace:()V
        //   151: return         
        //   152: astore          4
        //   154: goto            140
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  0      20     146    151    Ljava/lang/Exception;
        //  22     28     146    151    Ljava/lang/Exception;
        //  42     52     146    151    Ljava/lang/Exception;
        //  57     105    146    151    Ljava/lang/Exception;
        //  130    140    152    157    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0140:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public void forceImportContacts() {
        Utilities.globalQueue.postRunnable(new _$$Lambda$ContactsController$PHwuGsXHnbafxq3b4_SbXpvFUkE(this));
    }
    
    public HashMap<String, Contact> getContactsCopy(final HashMap<String, Contact> hashMap) {
        final HashMap<String, Contact> hashMap2 = new HashMap<String, Contact>();
        for (final Map.Entry<String, Contact> entry : hashMap.entrySet()) {
            final Contact value = new Contact();
            final Contact contact = entry.getValue();
            value.phoneDeleted.addAll(contact.phoneDeleted);
            value.phones.addAll(contact.phones);
            value.phoneTypes.addAll(contact.phoneTypes);
            value.shortPhones.addAll(contact.shortPhones);
            value.first_name = contact.first_name;
            value.last_name = contact.last_name;
            value.contact_id = contact.contact_id;
            hashMap2.put(value.key = contact.key, value);
        }
        return hashMap2;
    }
    
    public int getDeleteAccountTTL() {
        return this.deleteAccountTTL;
    }
    
    public String getInviteText(final int i) {
        String inviteLink;
        if ((inviteLink = this.inviteLink) == null) {
            inviteLink = "https://f-droid.org/packages/org.telegram.messenger";
        }
        if (i <= 1) {
            return LocaleController.formatString("InviteText2", 2131559680, inviteLink);
        }
        try {
            return String.format(LocaleController.getPluralString("InviteTextNum", i), i, inviteLink);
        }
        catch (Exception ex) {
            return LocaleController.formatString("InviteText2", 2131559680, inviteLink);
        }
    }
    
    public boolean getLoadingDeleteInfo() {
        return this.loadingDeleteInfo != 2;
    }
    
    public boolean getLoadingPrivicyInfo(final int n) {
        return this.loadingPrivacyInfo[n] != 2;
    }
    
    public ArrayList<TLRPC.PrivacyRule> getPrivacyRules(final int n) {
        switch (n) {
            default: {
                return null;
            }
            case 6: {
                return this.phonePrivacyRules;
            }
            case 5: {
                return this.forwardsPrivacyRules;
            }
            case 4: {
                return this.profilePhotoPrivacyRules;
            }
            case 3: {
                return this.p2pPrivacyRules;
            }
            case 2: {
                return this.callPrivacyRules;
            }
            case 1: {
                return this.groupPrivacyRules;
            }
            case 0: {
                return this.lastseenPrivacyRules;
            }
        }
    }
    
    public boolean isContact(final int i) {
        return this.contactsDict.get(i) != null;
    }
    
    public boolean isLoadingContacts() {
        synchronized (this.loadContactsSync) {
            return this.loadingContacts;
        }
    }
    
    public void loadContacts(final boolean b, final int hash) {
        Object loadContactsSync = this.loadContactsSync;
        synchronized (loadContactsSync) {
            this.loadingContacts = true;
            // monitorexit(loadContactsSync)
            if (b) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("load contacts from cache");
                }
                MessagesStorage.getInstance(this.currentAccount).getContacts();
            }
            else {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("load contacts from server");
                }
                loadContactsSync = new TLRPC.TL_contacts_getContacts();
                ((TLRPC.TL_contacts_getContacts)loadContactsSync).hash = hash;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest((TLObject)loadContactsSync, new _$$Lambda$ContactsController$LgaPvpXbmtv6_BmnTvc8qVOudt4(this, hash));
            }
        }
    }
    
    public void loadPrivacySettings() {
        if (this.loadingDeleteInfo == 0) {
            this.loadingDeleteInfo = 1;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC.TL_account_getAccountTTL(), new _$$Lambda$ContactsController$UpZcB_L92bZrGJkH0CSwUF9nhJQ(this));
        }
        int n = 0;
        while (true) {
            final int[] loadingPrivacyInfo = this.loadingPrivacyInfo;
            if (n >= loadingPrivacyInfo.length) {
                break;
            }
            if (loadingPrivacyInfo[n] == 0) {
                loadingPrivacyInfo[n] = 1;
                final TLRPC.TL_account_getPrivacy tl_account_getPrivacy = new TLRPC.TL_account_getPrivacy();
                if (n != 0) {
                    if (n != 1) {
                        if (n != 2) {
                            if (n != 3) {
                                if (n != 4) {
                                    if (n != 5) {
                                        tl_account_getPrivacy.key = new TLRPC.TL_inputPrivacyKeyPhoneNumber();
                                    }
                                    else {
                                        tl_account_getPrivacy.key = new TLRPC.TL_inputPrivacyKeyForwards();
                                    }
                                }
                                else {
                                    tl_account_getPrivacy.key = new TLRPC.TL_inputPrivacyKeyProfilePhoto();
                                }
                            }
                            else {
                                tl_account_getPrivacy.key = new TLRPC.TL_inputPrivacyKeyPhoneP2P();
                            }
                        }
                        else {
                            tl_account_getPrivacy.key = new TLRPC.TL_inputPrivacyKeyPhoneCall();
                        }
                    }
                    else {
                        tl_account_getPrivacy.key = new TLRPC.TL_inputPrivacyKeyChatInvite();
                    }
                }
                else {
                    tl_account_getPrivacy.key = new TLRPC.TL_inputPrivacyKeyStatusTimestamp();
                }
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tl_account_getPrivacy, new _$$Lambda$ContactsController$AbTZFLT5AeZ8Xev0g7z_25zx9YM(this, n));
            }
            ++n;
        }
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.privacyRulesUpdated, new Object[0]);
    }
    
    protected void markAsContacted(final String s) {
        if (s == null) {
            return;
        }
        Utilities.phoneBookQueue.postRunnable(new _$$Lambda$ContactsController$ZSZ9C_4_dtPH1zqggEDwmuwv69A(s));
    }
    
    protected void migratePhoneBookToV7(final SparseArray<Contact> sparseArray) {
        Utilities.globalQueue.postRunnable(new _$$Lambda$ContactsController$BUiF7Jzba7DA47bmyOp4BvMiTjQ(this, sparseArray));
    }
    
    protected void performSyncPhoneBook(final HashMap<String, Contact> hashMap, final boolean b, final boolean b2, final boolean b3, final boolean b4, final boolean b5, final boolean b6) {
        if (!b2 && !this.contactsBookLoaded) {
            return;
        }
        Utilities.globalQueue.postRunnable(new _$$Lambda$ContactsController$bP_Wp_ENz0cZTKPV19hn5HbOllw(this, hashMap, b3, b, b2, b4, b5, b6));
    }
    
    public void processContactsUpdates(final ArrayList<Integer> c, final ConcurrentHashMap<Integer, TLRPC.User> concurrentHashMap) {
        final ArrayList<TLRPC.TL_contact> list = new ArrayList<TLRPC.TL_contact>();
        final ArrayList<Integer> list2 = new ArrayList<Integer>();
        for (final Integer n : c) {
            if (n > 0) {
                final TLRPC.TL_contact e = new TLRPC.TL_contact();
                e.user_id = n;
                list.add(e);
                if (this.delayedContactsUpdate.isEmpty()) {
                    continue;
                }
                final int index = this.delayedContactsUpdate.indexOf(-n);
                if (index == -1) {
                    continue;
                }
                this.delayedContactsUpdate.remove(index);
            }
            else {
                if (n >= 0) {
                    continue;
                }
                list2.add(-n);
                if (this.delayedContactsUpdate.isEmpty()) {
                    continue;
                }
                final int index2 = this.delayedContactsUpdate.indexOf(-n);
                if (index2 == -1) {
                    continue;
                }
                this.delayedContactsUpdate.remove(index2);
            }
        }
        if (!list2.isEmpty()) {
            MessagesStorage.getInstance(this.currentAccount).deleteContacts(list2);
        }
        if (!list.isEmpty()) {
            MessagesStorage.getInstance(this.currentAccount).putContacts(list, false);
        }
        if (this.contactsLoaded && this.contactsBookLoaded) {
            this.applyContactsUpdates(c, concurrentHashMap, list, list2);
        }
        else {
            this.delayedContactsUpdate.addAll(c);
            if (BuildVars.LOGS_ENABLED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("delay update - contacts add = ");
                sb.append(list.size());
                sb.append(" delete = ");
                sb.append(list2.size());
                FileLog.d(sb.toString());
            }
        }
    }
    
    public void processLoadedContacts(final ArrayList<TLRPC.TL_contact> list, final ArrayList<TLRPC.User> list2, final int n) {
        AndroidUtilities.runOnUIThread(new _$$Lambda$ContactsController$EZvJWephnINCisVyT9aioUlUdEQ(this, list2, n, list));
    }
    
    public void readContacts() {
        synchronized (this.loadContactsSync) {
            if (this.loadingContacts) {
                return;
            }
            this.loadingContacts = true;
            // monitorexit(this.loadContactsSync)
            Utilities.stageQueue.postRunnable(new _$$Lambda$ContactsController$DgnS7Gvt4et5oNJCSlLnfIbm4Ag(this));
        }
    }
    
    public void reloadContactsStatuses() {
        this.saveContactsLoadTime();
        MessagesController.getInstance(this.currentAccount).clearFullUsers();
        final SharedPreferences$Editor edit = MessagesController.getMainSettings(this.currentAccount).edit();
        edit.putBoolean("needGetStatuses", true).commit();
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC.TL_contacts_getStatuses(), new _$$Lambda$ContactsController$h5vCC_HpjKgEFkNX9o79KFdTfVk(this, edit));
    }
    
    public void resetImportedContacts() {
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC.TL_contacts_resetSaved(), (RequestDelegate)_$$Lambda$ContactsController$rEtEE7WzBCVDghotOSTBjLdLnsM.INSTANCE);
    }
    
    public void setDeleteAccountTTL(final int deleteAccountTTL) {
        this.deleteAccountTTL = deleteAccountTTL;
    }
    
    public void setPrivacyRules(final ArrayList<TLRPC.PrivacyRule> lastseenPrivacyRules, final int n) {
        switch (n) {
            case 6: {
                this.phonePrivacyRules = lastseenPrivacyRules;
                break;
            }
            case 5: {
                this.forwardsPrivacyRules = lastseenPrivacyRules;
                break;
            }
            case 4: {
                this.profilePhotoPrivacyRules = lastseenPrivacyRules;
                break;
            }
            case 3: {
                this.p2pPrivacyRules = lastseenPrivacyRules;
                break;
            }
            case 2: {
                this.callPrivacyRules = lastseenPrivacyRules;
                break;
            }
            case 1: {
                this.groupPrivacyRules = lastseenPrivacyRules;
                break;
            }
            case 0: {
                this.lastseenPrivacyRules = lastseenPrivacyRules;
                break;
            }
        }
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.privacyRulesUpdated, new Object[0]);
        this.reloadContactsStatuses();
    }
    
    public void syncPhoneBookByAlert(final HashMap<String, Contact> hashMap, final boolean b, final boolean b2, final boolean b3) {
        Utilities.globalQueue.postRunnable(new _$$Lambda$ContactsController$zqe6opQgPyDWpCXT4MV8TEtpNRg(this, hashMap, b, b2, b3));
    }
    
    public static class Contact
    {
        public int contact_id;
        public String first_name;
        public int imported;
        public boolean isGoodProvider;
        public String key;
        public String last_name;
        public boolean namesFilled;
        public ArrayList<Integer> phoneDeleted;
        public ArrayList<String> phoneTypes;
        public ArrayList<String> phones;
        public String provider;
        public ArrayList<String> shortPhones;
        public TLRPC.User user;
        
        public Contact() {
            this.phones = new ArrayList<String>(4);
            this.phoneTypes = new ArrayList<String>(4);
            this.shortPhones = new ArrayList<String>(4);
            this.phoneDeleted = new ArrayList<Integer>(4);
        }
        
        public static String getLetter(final String s, final String s2) {
            if (!TextUtils.isEmpty((CharSequence)s)) {
                return s.substring(0, 1);
            }
            if (!TextUtils.isEmpty((CharSequence)s2)) {
                return s2.substring(0, 1);
            }
            return "#";
        }
        
        public String getLetter() {
            return getLetter(this.first_name, this.last_name);
        }
    }
    
    private class MyContentObserver extends ContentObserver
    {
        private Runnable checkRunnable;
        
        public MyContentObserver() {
            super((Handler)null);
            this.checkRunnable = (Runnable)_$$Lambda$ContactsController$MyContentObserver$VmhFqLMqh0tD4jEQWkPIR_W56Bc.INSTANCE;
        }
        
        public boolean deliverSelfNotifications() {
            return false;
        }
        
        public void onChange(final boolean b) {
            super.onChange(b);
            synchronized (ContactsController.this.observerLock) {
                if (ContactsController.this.ignoreChanges) {
                    return;
                }
                // monitorexit(ContactsController.access$000(this.this$0))
                Utilities.globalQueue.cancelRunnable(this.checkRunnable);
                Utilities.globalQueue.postRunnable(this.checkRunnable, 500L);
            }
        }
    }
}

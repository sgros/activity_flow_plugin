// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import org.telegram.messenger.FileLog;
import org.telegram.messenger.AndroidUtilities;
import java.util.ArrayList;
import org.telegram.messenger.ContactsController;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.ContentValues;
import android.content.DialogInterface$OnClickListener;

class PhonebookShareActivity$5 implements DialogInterface$OnClickListener
{
    final /* synthetic */ PhonebookShareActivity this$0;
    
    PhonebookShareActivity$5(final PhonebookShareActivity this$0) {
        this.this$0 = this$0;
    }
    
    private void fillRowWithType(final String anotherString, final ContentValues contentValues) {
        final boolean startsWith = anotherString.startsWith("X-");
        final Integer value = 0;
        if (startsWith) {
            contentValues.put("data2", value);
            contentValues.put("data3", anotherString.substring(2));
        }
        else if ("PREF".equalsIgnoreCase(anotherString)) {
            contentValues.put("data2", Integer.valueOf(12));
        }
        else if ("HOME".equalsIgnoreCase(anotherString)) {
            contentValues.put("data2", Integer.valueOf(1));
        }
        else if (!"MOBILE".equalsIgnoreCase(anotherString) && !"CELL".equalsIgnoreCase(anotherString)) {
            if ("OTHER".equalsIgnoreCase(anotherString)) {
                contentValues.put("data2", Integer.valueOf(7));
            }
            else if ("WORK".equalsIgnoreCase(anotherString)) {
                contentValues.put("data2", Integer.valueOf(3));
            }
            else if (!"RADIO".equalsIgnoreCase(anotherString) && !"VOICE".equalsIgnoreCase(anotherString)) {
                if ("PAGER".equalsIgnoreCase(anotherString)) {
                    contentValues.put("data2", Integer.valueOf(6));
                }
                else if ("CALLBACK".equalsIgnoreCase(anotherString)) {
                    contentValues.put("data2", Integer.valueOf(8));
                }
                else if ("CAR".equalsIgnoreCase(anotherString)) {
                    contentValues.put("data2", Integer.valueOf(9));
                }
                else if ("ASSISTANT".equalsIgnoreCase(anotherString)) {
                    contentValues.put("data2", Integer.valueOf(19));
                }
                else if ("MMS".equalsIgnoreCase(anotherString)) {
                    contentValues.put("data2", Integer.valueOf(20));
                }
                else if (anotherString.startsWith("FAX")) {
                    contentValues.put("data2", Integer.valueOf(4));
                }
                else {
                    contentValues.put("data2", value);
                    contentValues.put("data3", anotherString);
                }
            }
            else {
                contentValues.put("data2", Integer.valueOf(14));
            }
        }
        else {
            contentValues.put("data2", Integer.valueOf(2));
        }
    }
    
    private void fillUrlRowWithType(final String anotherString, final ContentValues contentValues) {
        final boolean startsWith = anotherString.startsWith("X-");
        final Integer value = 0;
        if (startsWith) {
            contentValues.put("data2", value);
            contentValues.put("data3", anotherString.substring(2));
        }
        else if ("HOMEPAGE".equalsIgnoreCase(anotherString)) {
            contentValues.put("data2", Integer.valueOf(1));
        }
        else if ("BLOG".equalsIgnoreCase(anotherString)) {
            contentValues.put("data2", Integer.valueOf(2));
        }
        else if ("PROFILE".equalsIgnoreCase(anotherString)) {
            contentValues.put("data2", Integer.valueOf(3));
        }
        else if ("HOME".equalsIgnoreCase(anotherString)) {
            contentValues.put("data2", Integer.valueOf(4));
        }
        else if ("WORK".equalsIgnoreCase(anotherString)) {
            contentValues.put("data2", Integer.valueOf(5));
        }
        else if ("FTP".equalsIgnoreCase(anotherString)) {
            contentValues.put("data2", Integer.valueOf(6));
        }
        else if ("OTHER".equalsIgnoreCase(anotherString)) {
            contentValues.put("data2", Integer.valueOf(7));
        }
        else {
            contentValues.put("data2", value);
            contentValues.put("data3", anotherString);
        }
    }
    
    public void onClick(final DialogInterface dialogInterface, int i) {
        if (this.this$0.getParentActivity() == null) {
            return;
        }
        Intent intent = null;
        if (i == 0) {
            intent = new Intent("android.intent.action.INSERT");
            intent.setType("vnd.android.cursor.dir/raw_contact");
        }
        else if (i == 1) {
            intent = new Intent("android.intent.action.INSERT_OR_EDIT");
            intent.setType("vnd.android.cursor.item/contact");
        }
        intent.putExtra("name", ContactsController.formatName(this.this$0.currentUser.first_name, this.this$0.currentUser.last_name));
        final ArrayList<ContentValues> list = new ArrayList<ContentValues>();
        AndroidUtilities.VcardItem vcardItem;
        ContentValues e;
        for (i = 0; i < this.this$0.phones.size(); ++i) {
            vcardItem = this.this$0.phones.get(i);
            e = new ContentValues();
            e.put("mimetype", "vnd.android.cursor.item/phone_v2");
            e.put("data1", vcardItem.getValue(false));
            this.fillRowWithType(vcardItem.getRawType(false), e);
            list.add(e);
        }
        i = 0;
        int n = 0;
        while (i < this.this$0.other.size()) {
            final AndroidUtilities.VcardItem vcardItem2 = this.this$0.other.get(i);
            final int type = vcardItem2.type;
            if (type == 1) {
                final ContentValues e2 = new ContentValues();
                e2.put("mimetype", "vnd.android.cursor.item/email_v2");
                e2.put("data1", vcardItem2.getValue(false));
                this.fillRowWithType(vcardItem2.getRawType(false), e2);
                list.add(e2);
            }
            else if (type == 3) {
                final ContentValues e3 = new ContentValues();
                e3.put("mimetype", "vnd.android.cursor.item/website");
                e3.put("data1", vcardItem2.getValue(false));
                this.fillUrlRowWithType(vcardItem2.getRawType(false), e3);
                list.add(e3);
            }
            else if (type == 4) {
                final ContentValues e4 = new ContentValues();
                e4.put("mimetype", "vnd.android.cursor.item/note");
                e4.put("data1", vcardItem2.getValue(false));
                list.add(e4);
            }
            else if (type == 5) {
                final ContentValues e5 = new ContentValues();
                e5.put("mimetype", "vnd.android.cursor.item/contact_event");
                e5.put("data1", vcardItem2.getValue(false));
                e5.put("data2", Integer.valueOf(3));
                list.add(e5);
            }
            else if (type == 2) {
                final ContentValues e6 = new ContentValues();
                e6.put("mimetype", "vnd.android.cursor.item/postal-address_v2");
                final String[] rawValue = vcardItem2.getRawValue();
                if (rawValue.length > 0) {
                    e6.put("data5", rawValue[0]);
                }
                if (rawValue.length > 1) {
                    e6.put("data6", rawValue[1]);
                }
                if (rawValue.length > 2) {
                    e6.put("data4", rawValue[2]);
                }
                if (rawValue.length > 3) {
                    e6.put("data7", rawValue[3]);
                }
                if (rawValue.length > 4) {
                    e6.put("data8", rawValue[4]);
                }
                if (rawValue.length > 5) {
                    e6.put("data9", rawValue[5]);
                }
                if (rawValue.length > 6) {
                    e6.put("data10", rawValue[6]);
                }
                final String rawType = vcardItem2.getRawType(false);
                if ("HOME".equalsIgnoreCase(rawType)) {
                    e6.put("data2", Integer.valueOf(1));
                }
                else if ("WORK".equalsIgnoreCase(rawType)) {
                    e6.put("data2", Integer.valueOf(2));
                }
                else if ("OTHER".equalsIgnoreCase(rawType)) {
                    e6.put("data2", Integer.valueOf(3));
                }
                list.add(e6);
            }
            else if (type == 20) {
                final ContentValues e7 = new ContentValues();
                e7.put("mimetype", "vnd.android.cursor.item/im");
                final String rawType2 = vcardItem2.getRawType(true);
                final String rawType3 = vcardItem2.getRawType(false);
                e7.put("data1", vcardItem2.getValue(false));
                if ("AIM".equalsIgnoreCase(rawType2)) {
                    e7.put("data5", Integer.valueOf(0));
                }
                else if ("MSN".equalsIgnoreCase(rawType2)) {
                    e7.put("data5", Integer.valueOf(1));
                }
                else if ("YAHOO".equalsIgnoreCase(rawType2)) {
                    e7.put("data5", Integer.valueOf(2));
                }
                else if ("SKYPE".equalsIgnoreCase(rawType2)) {
                    e7.put("data5", Integer.valueOf(3));
                }
                else if ("QQ".equalsIgnoreCase(rawType2)) {
                    e7.put("data5", Integer.valueOf(4));
                }
                else if ("GOOGLE-TALK".equalsIgnoreCase(rawType2)) {
                    e7.put("data5", Integer.valueOf(5));
                }
                else if ("ICQ".equalsIgnoreCase(rawType2)) {
                    e7.put("data5", Integer.valueOf(6));
                }
                else if ("JABBER".equalsIgnoreCase(rawType2)) {
                    e7.put("data5", Integer.valueOf(7));
                }
                else if ("NETMEETING".equalsIgnoreCase(rawType2)) {
                    e7.put("data5", Integer.valueOf(8));
                }
                else {
                    e7.put("data5", Integer.valueOf(-1));
                    e7.put("data6", vcardItem2.getRawType(true));
                }
                if ("HOME".equalsIgnoreCase(rawType3)) {
                    e7.put("data2", Integer.valueOf(1));
                }
                else if ("WORK".equalsIgnoreCase(rawType3)) {
                    e7.put("data2", Integer.valueOf(2));
                }
                else if ("OTHER".equalsIgnoreCase(rawType3)) {
                    e7.put("data2", Integer.valueOf(3));
                }
                list.add(e7);
            }
            else if (type == 6) {
                if (n == 0) {
                    final ContentValues e8 = new ContentValues();
                    e8.put("mimetype", "vnd.android.cursor.item/organization");
                    for (int j = i; j < this.this$0.other.size(); ++j) {
                        final AndroidUtilities.VcardItem vcardItem3 = this.this$0.other.get(j);
                        if (vcardItem3.type == 6) {
                            final String rawType4 = vcardItem3.getRawType(true);
                            if ("ORG".equalsIgnoreCase(rawType4)) {
                                final String[] rawValue2 = vcardItem3.getRawValue();
                                if (rawValue2.length == 0) {
                                    continue;
                                }
                                if (rawValue2.length >= 1) {
                                    e8.put("data1", rawValue2[0]);
                                }
                                if (rawValue2.length >= 2) {
                                    e8.put("data5", rawValue2[1]);
                                }
                            }
                            else if ("TITLE".equalsIgnoreCase(rawType4)) {
                                e8.put("data4", vcardItem3.getValue(false));
                            }
                            else if ("ROLE".equalsIgnoreCase(rawType4)) {
                                e8.put("data4", vcardItem3.getValue(false));
                            }
                            final String rawType5 = vcardItem3.getRawType(true);
                            if ("WORK".equalsIgnoreCase(rawType5)) {
                                e8.put("data2", Integer.valueOf(1));
                            }
                            else if ("OTHER".equalsIgnoreCase(rawType5)) {
                                e8.put("data2", Integer.valueOf(2));
                            }
                        }
                    }
                    list.add(e8);
                    n = 1;
                }
            }
            ++i;
        }
        intent.putExtra("finishActivityOnSaveCompleted", true);
        intent.putParcelableArrayListExtra("data", (ArrayList)list);
        try {
            this.this$0.getParentActivity().startActivity(intent);
            this.this$0.finishFragment();
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
    }
}

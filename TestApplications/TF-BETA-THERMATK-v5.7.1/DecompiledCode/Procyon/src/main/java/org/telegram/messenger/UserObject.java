// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import org.telegram.PhoneFormat.PhoneFormat;
import android.text.TextUtils;
import org.telegram.tgnet.TLRPC;

public class UserObject
{
    public static String getFirstName(final TLRPC.User user) {
        return getFirstName(user, true);
    }
    
    public static String getFirstName(final TLRPC.User user, final boolean b) {
        if (user != null && !isDeleted(user)) {
            final String first_name = user.first_name;
            String s;
            if (TextUtils.isEmpty((CharSequence)first_name)) {
                s = user.last_name;
            }
            else {
                s = first_name;
                if (!b) {
                    s = first_name;
                    if (first_name.length() <= 2) {
                        return ContactsController.formatName(user.first_name, user.last_name);
                    }
                }
            }
            if (TextUtils.isEmpty((CharSequence)s)) {
                s = LocaleController.getString("HiddenName", 2131559636);
            }
            return s;
        }
        return "DELETED";
    }
    
    public static String getUserName(final TLRPC.User user) {
        if (user != null && !isDeleted(user)) {
            String s2;
            final String s = s2 = ContactsController.formatName(user.first_name, user.last_name);
            if (s.length() == 0) {
                final String phone = user.phone;
                s2 = s;
                if (phone != null) {
                    if (phone.length() == 0) {
                        s2 = s;
                    }
                    else {
                        final PhoneFormat instance = PhoneFormat.getInstance();
                        final StringBuilder sb = new StringBuilder();
                        sb.append("+");
                        sb.append(user.phone);
                        s2 = instance.format(sb.toString());
                    }
                }
            }
            return s2;
        }
        return LocaleController.getString("HiddenName", 2131559636);
    }
    
    public static boolean isContact(final TLRPC.User user) {
        return user != null && (user instanceof TLRPC.TL_userContact_old2 || user.contact || user.mutual_contact);
    }
    
    public static boolean isDeleted(final TLRPC.User user) {
        return user == null || user instanceof TLRPC.TL_userDeleted_old2 || user instanceof TLRPC.TL_userEmpty || user.deleted;
    }
    
    public static boolean isUserSelf(final TLRPC.User user) {
        return user != null && (user instanceof TLRPC.TL_userSelf_old3 || user.self);
    }
}

// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.PhoneFormat;

import java.util.Iterator;
import java.util.ArrayList;

public class CallingCodeInfo
{
    public String callingCode;
    public ArrayList<String> countries;
    public ArrayList<String> intlPrefixes;
    public ArrayList<RuleSet> ruleSets;
    public ArrayList<String> trunkPrefixes;
    
    public CallingCodeInfo() {
        this.countries = new ArrayList<String>();
        this.callingCode = "";
        this.trunkPrefixes = new ArrayList<String>();
        this.intlPrefixes = new ArrayList<String>();
        this.ruleSets = new ArrayList<RuleSet>();
    }
    
    String format(final String s) {
        final boolean startsWith = s.startsWith(this.callingCode);
        String s2 = null;
        String callingCode;
        String s3;
        if (startsWith) {
            callingCode = this.callingCode;
            s3 = s.substring(callingCode.length());
        }
        else {
            final String matchingTrunkCode = this.matchingTrunkCode(s);
            if (matchingTrunkCode != null) {
                s3 = s.substring(matchingTrunkCode.length());
                s2 = matchingTrunkCode;
                callingCode = null;
            }
            else {
                s3 = s;
                callingCode = null;
            }
        }
        final Iterator<RuleSet> iterator = this.ruleSets.iterator();
        while (iterator.hasNext()) {
            final String format = iterator.next().format(s3, callingCode, s2, true);
            if (format != null) {
                return format;
            }
        }
        final Iterator<RuleSet> iterator2 = this.ruleSets.iterator();
        while (iterator2.hasNext()) {
            final String format2 = iterator2.next().format(s3, callingCode, s2, false);
            if (format2 != null) {
                return format2;
            }
        }
        String format3 = s;
        if (callingCode != null) {
            format3 = s;
            if (s3.length() != 0) {
                format3 = String.format("%s %s", callingCode, s3);
            }
        }
        return format3;
    }
    
    boolean isValidPhoneNumber(String s) {
        final boolean startsWith = s.startsWith(this.callingCode);
        String s2 = null;
        String s3;
        if (startsWith) {
            final String callingCode = this.callingCode;
            s3 = s.substring(callingCode.length());
            s = callingCode;
        }
        else {
            final String matchingTrunkCode = this.matchingTrunkCode(s);
            if (matchingTrunkCode != null) {
                s3 = s.substring(matchingTrunkCode.length());
                s2 = matchingTrunkCode;
                s = null;
            }
            else {
                final String s4 = null;
                s3 = s;
                s = s4;
            }
        }
        final Iterator<RuleSet> iterator = this.ruleSets.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().isValid(s3, s, s2, true)) {
                return true;
            }
        }
        final Iterator<RuleSet> iterator2 = this.ruleSets.iterator();
        while (iterator2.hasNext()) {
            if (iterator2.next().isValid(s3, s, s2, false)) {
                return true;
            }
        }
        return false;
    }
    
    String matchingAccessCode(final String s) {
        for (final String prefix : this.intlPrefixes) {
            if (s.startsWith(prefix)) {
                return prefix;
            }
        }
        return null;
    }
    
    String matchingTrunkCode(final String s) {
        for (final String prefix : this.trunkPrefixes) {
            if (s.startsWith(prefix)) {
                return prefix;
            }
        }
        return null;
    }
}

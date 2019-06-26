// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.PhoneFormat;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class RuleSet
{
    public static Pattern pattern;
    public boolean hasRuleWithIntlPrefix;
    public boolean hasRuleWithTrunkPrefix;
    public int matchLen;
    public ArrayList<PhoneRule> rules;
    
    static {
        RuleSet.pattern = Pattern.compile("[0-9]+");
    }
    
    public RuleSet() {
        this.rules = new ArrayList<PhoneRule>();
    }
    
    String format(final String s, final String s2, final String s3, final boolean b) {
        final int length = s.length();
        final int matchLen = this.matchLen;
        if (length >= matchLen) {
            int int1 = 0;
            final Matcher matcher = RuleSet.pattern.matcher(s.substring(0, matchLen));
            if (matcher.find()) {
                int1 = Integer.parseInt(matcher.group(0));
            }
            for (final PhoneRule phoneRule : this.rules) {
                if (int1 >= phoneRule.minVal && int1 <= phoneRule.maxVal && s.length() <= phoneRule.maxLen) {
                    if (b) {
                        if (((phoneRule.flag12 & 0x3) == 0x0 && s3 == null && s2 == null) || (s3 != null && (phoneRule.flag12 & 0x1) != 0x0) || (s2 != null && (phoneRule.flag12 & 0x2) != 0x0)) {
                            return phoneRule.format(s, s2, s3);
                        }
                        continue;
                    }
                    else {
                        if ((s3 == null && s2 == null) || (s3 != null && (phoneRule.flag12 & 0x1) != 0x0) || (s2 != null && (phoneRule.flag12 & 0x2) != 0x0)) {
                            return phoneRule.format(s, s2, s3);
                        }
                        continue;
                    }
                }
            }
            if (!b) {
                if (s2 != null) {
                    for (final PhoneRule phoneRule2 : this.rules) {
                        if (int1 >= phoneRule2.minVal && int1 <= phoneRule2.maxVal && s.length() <= phoneRule2.maxLen && (s3 == null || (phoneRule2.flag12 & 0x1) != 0x0)) {
                            return phoneRule2.format(s, s2, s3);
                        }
                    }
                }
                else if (s3 != null) {
                    for (final PhoneRule phoneRule3 : this.rules) {
                        if (int1 >= phoneRule3.minVal && int1 <= phoneRule3.maxVal && s.length() <= phoneRule3.maxLen && (s2 == null || (phoneRule3.flag12 & 0x2) != 0x0)) {
                            return phoneRule3.format(s, s2, s3);
                        }
                    }
                }
            }
        }
        return null;
    }
    
    boolean isValid(final String s, final String s2, final String s3, final boolean b) {
        final int length = s.length();
        final int matchLen = this.matchLen;
        if (length >= matchLen) {
            final Matcher matcher = RuleSet.pattern.matcher(s.substring(0, matchLen));
            int int1;
            if (matcher.find()) {
                int1 = Integer.parseInt(matcher.group(0));
            }
            else {
                int1 = 0;
            }
            for (final PhoneRule phoneRule : this.rules) {
                if (int1 >= phoneRule.minVal && int1 <= phoneRule.maxVal && s.length() == phoneRule.maxLen) {
                    if (b) {
                        if (((phoneRule.flag12 & 0x3) == 0x0 && s3 == null && s2 == null) || (s3 != null && (phoneRule.flag12 & 0x1) != 0x0) || (s2 != null && (phoneRule.flag12 & 0x2) != 0x0)) {
                            return true;
                        }
                        continue;
                    }
                    else {
                        if ((s3 == null && s2 == null) || (s3 != null && (phoneRule.flag12 & 0x1) != 0x0) || (s2 != null && (phoneRule.flag12 & 0x2) != 0x0)) {
                            return true;
                        }
                        continue;
                    }
                }
            }
            if (!b) {
                if (s2 != null && !this.hasRuleWithIntlPrefix) {
                    for (final PhoneRule phoneRule2 : this.rules) {
                        if (int1 >= phoneRule2.minVal && int1 <= phoneRule2.maxVal && s.length() == phoneRule2.maxLen && (s3 == null || (phoneRule2.flag12 & 0x1) != 0x0)) {
                            return true;
                        }
                    }
                }
                else if (s3 != null && !this.hasRuleWithTrunkPrefix) {
                    for (final PhoneRule phoneRule3 : this.rules) {
                        if (int1 >= phoneRule3.minVal && int1 <= phoneRule3.maxVal && s.length() == phoneRule3.maxLen && (s2 == null || (phoneRule3.flag12 & 0x2) != 0x0)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}

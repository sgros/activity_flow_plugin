// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.utils;

import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.Enumeration;

public class StringToken implements Enumeration<Object>
{
    private String delimiters;
    private int position;
    private String string;
    
    public StringToken(final String string, final String delimiters) {
        if (string != null) {
            this.string = string;
            this.delimiters = delimiters;
            this.position = 0;
            return;
        }
        throw new NullPointerException();
    }
    
    public static ArrayList<String> parse(final String s, final String s2) {
        return parse(s, s2, new ArrayList<String>());
    }
    
    public static ArrayList<String> parse(final String s, final String s2, final ArrayList<String> list) {
        final StringToken stringToken = new StringToken(s.replace(s2, " " + s2), s2);
        while (stringToken.hasMoreTokens()) {
            list.add(stringToken.nextToken().trim());
        }
        return list;
    }
    
    public int countTokens() {
        int n = 0;
        int n2 = 0;
        int n3;
        int n4;
        for (int i = this.position; i < this.string.length(); ++i, n = n3, n2 = n4) {
            if (this.delimiters.indexOf(this.string.charAt(i), 0) >= 0) {
                n3 = n;
                if ((n4 = n2) != 0) {
                    n3 = n + 1;
                    n4 = 0;
                }
            }
            else {
                n4 = 1;
                n3 = n;
            }
        }
        int n5 = n;
        if (n2 != 0) {
            n5 = n + 1;
        }
        return n5;
    }
    
    @Override
    public boolean hasMoreElements() {
        return this.hasMoreTokens();
    }
    
    public boolean hasMoreTokens() {
        final boolean b = false;
        if (this.delimiters == null) {
            throw new NullPointerException();
        }
        final int length = this.string.length();
        boolean b2 = b;
        if (this.position < length) {
            int position = this.position;
            while (true) {
                b2 = b;
                if (position >= length) {
                    break;
                }
                if (this.delimiters.indexOf(this.string.charAt(position), 0) == -1) {
                    b2 = true;
                    break;
                }
                ++position;
            }
        }
        return b2;
    }
    
    @Override
    public Object nextElement() {
        return this.nextToken();
    }
    
    public String nextToken() {
        if (this.delimiters == null) {
            throw new NullPointerException();
        }
        int position = this.position;
        final int length = this.string.length();
        if (position < length) {
            while (position < length && this.delimiters.indexOf(this.string.charAt(position), 0) >= 0) {
                ++position;
            }
            if ((this.position = position) < length) {
                ++this.position;
                while (this.position < length) {
                    if (this.delimiters.indexOf(this.string.charAt(this.position), 0) >= 0) {
                        return this.string.substring(position, this.position);
                    }
                    ++this.position;
                }
                return this.string.substring(position);
            }
        }
        throw new NoSuchElementException();
    }
}

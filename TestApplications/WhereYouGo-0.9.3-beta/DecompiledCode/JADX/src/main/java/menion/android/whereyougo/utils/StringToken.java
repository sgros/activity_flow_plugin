package menion.android.whereyougo.utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.NoSuchElementException;

public class StringToken implements Enumeration<Object> {
    private String delimiters;
    private int position;
    private String string;

    public StringToken(String string, String delimiters) {
        if (string != null) {
            this.string = string;
            this.delimiters = delimiters;
            this.position = 0;
            return;
        }
        throw new NullPointerException();
    }

    public static ArrayList<String> parse(String data, String delimiters) {
        return parse(data, delimiters, new ArrayList());
    }

    public static ArrayList<String> parse(String data, String delimiters, ArrayList<String> tokens) {
        StringToken token = new StringToken(data.replace(delimiters, " " + delimiters), delimiters);
        while (token.hasMoreTokens()) {
            tokens.add(token.nextToken().trim());
        }
        return tokens;
    }

    public int countTokens() {
        int count = 0;
        boolean inToken = false;
        int length = this.string.length();
        for (int i = this.position; i < length; i++) {
            if (this.delimiters.indexOf(this.string.charAt(i), 0) < 0) {
                inToken = true;
            } else if (inToken) {
                count++;
                inToken = false;
            }
        }
        if (inToken) {
            return count + 1;
        }
        return count;
    }

    public boolean hasMoreElements() {
        return hasMoreTokens();
    }

    public boolean hasMoreTokens() {
        if (this.delimiters == null) {
            throw new NullPointerException();
        }
        int length = this.string.length();
        if (this.position >= length) {
            return false;
        }
        for (int i = this.position; i < length; i++) {
            if (this.delimiters.indexOf(this.string.charAt(i), 0) == -1) {
                return true;
            }
        }
        return false;
    }

    public Object nextElement() {
        return nextToken();
    }

    public String nextToken() {
        if (this.delimiters == null) {
            throw new NullPointerException();
        }
        int i = this.position;
        int length = this.string.length();
        if (i < length) {
            while (i < length && this.delimiters.indexOf(this.string.charAt(i), 0) >= 0) {
                i++;
            }
            this.position = i;
            if (i < length) {
                this.position++;
                while (this.position < length) {
                    if (this.delimiters.indexOf(this.string.charAt(this.position), 0) >= 0) {
                        return this.string.substring(i, this.position);
                    }
                    this.position++;
                }
                return this.string.substring(i);
            }
        }
        throw new NoSuchElementException();
    }
}

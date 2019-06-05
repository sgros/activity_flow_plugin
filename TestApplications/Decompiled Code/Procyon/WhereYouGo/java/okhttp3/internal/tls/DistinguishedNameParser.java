// 
// Decompiled by Procyon v0.5.34
// 

package okhttp3.internal.tls;

import javax.security.auth.x500.X500Principal;

final class DistinguishedNameParser
{
    private int beg;
    private char[] chars;
    private int cur;
    private final String dn;
    private int end;
    private final int length;
    private int pos;
    
    public DistinguishedNameParser(final X500Principal x500Principal) {
        this.dn = x500Principal.getName("RFC2253");
        this.length = this.dn.length();
    }
    
    private String escapedAV() {
        this.beg = this.pos;
        this.end = this.pos;
        while (this.pos < this.length) {
            String s = null;
            switch (this.chars[this.pos]) {
                default: {
                    this.chars[this.end++] = this.chars[this.pos];
                    ++this.pos;
                    continue;
                }
                case '+':
                case ',':
                case ';': {
                    s = new String(this.chars, this.beg, this.end - this.beg);
                    break;
                }
                case '\\': {
                    this.chars[this.end++] = this.getEscaped();
                    ++this.pos;
                    continue;
                }
                case ' ': {
                    this.cur = this.end;
                    ++this.pos;
                    this.chars[this.end++] = 32;
                    while (this.pos < this.length && this.chars[this.pos] == ' ') {
                        this.chars[this.end++] = 32;
                        ++this.pos;
                    }
                    if (this.pos == this.length || this.chars[this.pos] == ',' || this.chars[this.pos] == '+' || this.chars[this.pos] == ';') {
                        s = new String(this.chars, this.beg, this.cur - this.beg);
                        break;
                    }
                    continue;
                }
            }
            return s;
        }
        return new String(this.chars, this.beg, this.end - this.beg);
    }
    
    private int getByte(int n) {
        if (n + 1 >= this.length) {
            throw new IllegalStateException("Malformed DN: " + this.dn);
        }
        int n2 = this.chars[n];
        if (n2 >= 48 && n2 <= 57) {
            n2 -= 48;
        }
        else if (n2 >= 97 && n2 <= 102) {
            n2 -= 87;
        }
        else {
            if (n2 < 65 || n2 > 70) {
                throw new IllegalStateException("Malformed DN: " + this.dn);
            }
            n2 -= 55;
        }
        n = this.chars[n + 1];
        if (n >= 48 && n <= 57) {
            n -= 48;
        }
        else if (n >= 97 && n <= 102) {
            n -= 87;
        }
        else {
            if (n < 65 || n > 70) {
                throw new IllegalStateException("Malformed DN: " + this.dn);
            }
            n -= 55;
        }
        return (n2 << 4) + n;
    }
    
    private char getEscaped() {
        ++this.pos;
        if (this.pos == this.length) {
            throw new IllegalStateException("Unexpected end of DN: " + this.dn);
        }
        char utf8 = '\0';
        switch (this.chars[this.pos]) {
            default: {
                utf8 = this.getUTF8();
                break;
            }
            case ' ':
            case '\"':
            case '#':
            case '%':
            case '*':
            case '+':
            case ',':
            case ';':
            case '<':
            case '=':
            case '>':
            case '\\':
            case '_': {
                utf8 = this.chars[this.pos];
                break;
            }
        }
        return utf8;
    }
    
    private char getUTF8() {
        final char c = '?';
        final int byte1 = this.getByte(this.pos);
        ++this.pos;
        char c2;
        if (byte1 < 128) {
            c2 = (char)byte1;
        }
        else {
            c2 = c;
            if (byte1 >= 192) {
                c2 = c;
                if (byte1 <= 247) {
                    int n;
                    int n2;
                    if (byte1 <= 223) {
                        n = 1;
                        n2 = (byte1 & 0x1F);
                    }
                    else if (byte1 <= 239) {
                        n = 2;
                        n2 = (byte1 & 0xF);
                    }
                    else {
                        n = 3;
                        n2 = (byte1 & 0x7);
                    }
                    final int n3 = 0;
                    int n4 = n2;
                    for (int i = n3; i < n; ++i) {
                        ++this.pos;
                        c2 = c;
                        if (this.pos == this.length) {
                            return c2;
                        }
                        c2 = c;
                        if (this.chars[this.pos] != '\\') {
                            return c2;
                        }
                        ++this.pos;
                        final int byte2 = this.getByte(this.pos);
                        ++this.pos;
                        c2 = c;
                        if ((byte2 & 0xC0) != 0x80) {
                            return c2;
                        }
                        n4 = (n4 << 6) + (byte2 & 0x3F);
                    }
                    c2 = (char)n4;
                }
            }
        }
        return c2;
    }
    
    private String hexAV() {
        if (this.pos + 4 >= this.length) {
            throw new IllegalStateException("Unexpected end of DN: " + this.dn);
        }
        this.beg = this.pos;
        ++this.pos;
        while (true) {
            while (this.pos != this.length && this.chars[this.pos] != '+' && this.chars[this.pos] != ',' && this.chars[this.pos] != ';') {
                if (this.chars[this.pos] == ' ') {
                    this.end = this.pos;
                    ++this.pos;
                    while (this.pos < this.length && this.chars[this.pos] == ' ') {
                        ++this.pos;
                    }
                    final int count = this.end - this.beg;
                    if (count < 5 || (count & 0x1) == 0x0) {
                        throw new IllegalStateException("Unexpected end of DN: " + this.dn);
                    }
                    final byte[] array = new byte[count / 2];
                    int i = 0;
                    int n = this.beg + 1;
                    while (i < array.length) {
                        array[i] = (byte)this.getByte(n);
                        n += 2;
                        ++i;
                    }
                    return new String(this.chars, this.beg, count);
                }
                else {
                    if (this.chars[this.pos] >= 'A' && this.chars[this.pos] <= 'F') {
                        final char[] chars = this.chars;
                        final int pos = this.pos;
                        chars[pos] += ' ';
                    }
                    ++this.pos;
                }
            }
            this.end = this.pos;
            continue;
        }
    }
    
    private String nextAT() {
        while (this.pos < this.length && this.chars[this.pos] == ' ') {
            ++this.pos;
        }
        String s;
        if (this.pos == this.length) {
            s = null;
        }
        else {
            this.beg = this.pos;
            ++this.pos;
            while (this.pos < this.length && this.chars[this.pos] != '=' && this.chars[this.pos] != ' ') {
                ++this.pos;
            }
            if (this.pos >= this.length) {
                throw new IllegalStateException("Unexpected end of DN: " + this.dn);
            }
            this.end = this.pos;
            if (this.chars[this.pos] == ' ') {
                while (this.pos < this.length && this.chars[this.pos] != '=' && this.chars[this.pos] == ' ') {
                    ++this.pos;
                }
                if (this.chars[this.pos] != '=' || this.pos == this.length) {
                    throw new IllegalStateException("Unexpected end of DN: " + this.dn);
                }
            }
            ++this.pos;
            while (this.pos < this.length && this.chars[this.pos] == ' ') {
                ++this.pos;
            }
            if (this.end - this.beg > 4 && this.chars[this.beg + 3] == '.' && (this.chars[this.beg] == 'O' || this.chars[this.beg] == 'o') && (this.chars[this.beg + 1] == 'I' || this.chars[this.beg + 1] == 'i') && (this.chars[this.beg + 2] == 'D' || this.chars[this.beg + 2] == 'd')) {
                this.beg += 4;
            }
            s = new String(this.chars, this.beg, this.end - this.beg);
        }
        return s;
    }
    
    private String quotedAV() {
        ++this.pos;
        this.beg = this.pos;
        this.end = this.beg;
        while (this.pos != this.length) {
            if (this.chars[this.pos] == '\"') {
                ++this.pos;
                while (this.pos < this.length && this.chars[this.pos] == ' ') {
                    ++this.pos;
                }
                return new String(this.chars, this.beg, this.end - this.beg);
            }
            if (this.chars[this.pos] == '\\') {
                this.chars[this.end] = this.getEscaped();
            }
            else {
                this.chars[this.end] = this.chars[this.pos];
            }
            ++this.pos;
            ++this.end;
        }
        throw new IllegalStateException("Unexpected end of DN: " + this.dn);
    }
    
    public String findMostSpecific(final String s) {
        this.pos = 0;
        this.beg = 0;
        this.end = 0;
        this.cur = 0;
        this.chars = this.dn.toCharArray();
        String anotherString;
        if ((anotherString = this.nextAT()) != null) {
            do {
                String s2 = "";
                if (this.pos == this.length) {
                    s2 = null;
                    return s2;
                }
                Label_0129: {
                    switch (this.chars[this.pos]) {
                        default: {
                            s2 = this.escapedAV();
                            break Label_0129;
                        }
                        case '#': {
                            s2 = this.hexAV();
                            break Label_0129;
                        }
                        case '\"': {
                            s2 = this.quotedAV();
                        }
                        case '+':
                        case ',':
                        case ';': {
                            if (s.equalsIgnoreCase(anotherString)) {
                                return s2;
                            }
                            if (this.pos >= this.length) {
                                s2 = null;
                                return s2;
                            }
                            if (this.chars[this.pos] != ',' && this.chars[this.pos] != ';' && this.chars[this.pos] != '+') {
                                throw new IllegalStateException("Malformed DN: " + this.dn);
                            }
                            ++this.pos;
                            continue;
                        }
                    }
                }
            } while ((anotherString = this.nextAT()) != null);
            throw new IllegalStateException("Malformed DN: " + this.dn);
        }
        return null;
    }
}

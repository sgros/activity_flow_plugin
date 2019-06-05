// 
// Decompiled by Procyon v0.5.34
// 

package net.sqlcipher;

public class CursorIndexOutOfBoundsException extends IndexOutOfBoundsException
{
    public CursorIndexOutOfBoundsException(final int i, final int j) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Index ");
        sb.append(i);
        sb.append(" requested, with a size of ");
        sb.append(j);
        super(sb.toString());
    }
    
    public CursorIndexOutOfBoundsException(final String s) {
        super(s);
    }
}

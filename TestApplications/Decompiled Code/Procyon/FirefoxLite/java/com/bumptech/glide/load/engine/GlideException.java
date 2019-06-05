// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine;

import java.io.PrintWriter;
import java.io.PrintStream;
import android.util.Log;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Iterator;
import java.util.Collections;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.DataSource;
import java.util.List;

public final class GlideException extends Exception
{
    private static final StackTraceElement[] EMPTY_ELEMENTS;
    private final List<Exception> causes;
    private Class<?> dataClass;
    private DataSource dataSource;
    private Key key;
    
    static {
        EMPTY_ELEMENTS = new StackTraceElement[0];
    }
    
    public GlideException(final String s) {
        this(s, Collections.emptyList());
    }
    
    public GlideException(final String s, final Exception o) {
        this(s, Collections.singletonList(o));
    }
    
    public GlideException(final String message, final List<Exception> causes) {
        super(message);
        this.setStackTrace(GlideException.EMPTY_ELEMENTS);
        this.causes = causes;
    }
    
    private void addRootCauses(final Exception ex, final List<Exception> list) {
        if (ex instanceof GlideException) {
            final Iterator<Exception> iterator = ((GlideException)ex).getCauses().iterator();
            while (iterator.hasNext()) {
                this.addRootCauses(iterator.next(), list);
            }
        }
        else {
            list.add(ex);
        }
    }
    
    private static void appendCauses(final List<Exception> list, final Appendable appendable) {
        try {
            appendCausesWrapped(list, appendable);
        }
        catch (IOException cause) {
            throw new RuntimeException(cause);
        }
    }
    
    private static void appendCausesWrapped(final List<Exception> list, final Appendable appendable) throws IOException {
        int j;
        for (int size = list.size(), i = 0; i < size; i = j) {
            final Appendable append = appendable.append("Cause (");
            j = i + 1;
            append.append(String.valueOf(j)).append(" of ").append(String.valueOf(size)).append("): ");
            final Exception ex = list.get(i);
            if (ex instanceof GlideException) {
                ((GlideException)ex).printStackTrace(appendable);
            }
            else {
                appendExceptionMessage(ex, appendable);
            }
        }
    }
    
    private static void appendExceptionMessage(final Exception cause, final Appendable appendable) {
        try {
            appendable.append(cause.getClass().toString()).append(": ").append(cause.getMessage()).append('\n');
        }
        catch (IOException ex) {
            throw new RuntimeException(cause);
        }
    }
    
    private void printStackTrace(final Appendable appendable) {
        appendExceptionMessage(this, appendable);
        appendCauses(this.getCauses(), new IndentedAppendable(appendable));
    }
    
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
    
    public List<Exception> getCauses() {
        return this.causes;
    }
    
    @Override
    public String getMessage() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.getMessage());
        String string;
        if (this.dataClass != null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(", ");
            sb2.append(this.dataClass);
            string = sb2.toString();
        }
        else {
            string = "";
        }
        sb.append(string);
        String string2;
        if (this.dataSource != null) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(", ");
            sb3.append(this.dataSource);
            string2 = sb3.toString();
        }
        else {
            string2 = "";
        }
        sb.append(string2);
        String string3;
        if (this.key != null) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(", ");
            sb4.append(this.key);
            string3 = sb4.toString();
        }
        else {
            string3 = "";
        }
        sb.append(string3);
        return sb.toString();
    }
    
    public List<Exception> getRootCauses() {
        final ArrayList<Exception> list = new ArrayList<Exception>();
        this.addRootCauses(this, list);
        return list;
    }
    
    public void logRootCauses(final String s) {
        final List<Exception> rootCauses = this.getRootCauses();
        int j;
        for (int size = rootCauses.size(), i = 0; i < size; i = j) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Root cause (");
            j = i + 1;
            sb.append(j);
            sb.append(" of ");
            sb.append(size);
            sb.append(")");
            Log.i(s, sb.toString(), (Throwable)rootCauses.get(i));
        }
    }
    
    @Override
    public void printStackTrace() {
        this.printStackTrace(System.err);
    }
    
    @Override
    public void printStackTrace(final PrintStream printStream) {
        this.printStackTrace((Appendable)printStream);
    }
    
    @Override
    public void printStackTrace(final PrintWriter printWriter) {
        this.printStackTrace((Appendable)printWriter);
    }
    
    void setLoggingDetails(final Key key, final DataSource dataSource) {
        this.setLoggingDetails(key, dataSource, null);
    }
    
    void setLoggingDetails(final Key key, final DataSource dataSource, final Class<?> dataClass) {
        this.key = key;
        this.dataSource = dataSource;
        this.dataClass = dataClass;
    }
    
    private static final class IndentedAppendable implements Appendable
    {
        private final Appendable appendable;
        private boolean printedNewLine;
        
        IndentedAppendable(final Appendable appendable) {
            this.printedNewLine = true;
            this.appendable = appendable;
        }
        
        private CharSequence safeSequence(final CharSequence charSequence) {
            if (charSequence == null) {
                return "";
            }
            return charSequence;
        }
        
        @Override
        public Appendable append(final char c) throws IOException {
            final boolean printedNewLine = this.printedNewLine;
            boolean printedNewLine2 = false;
            if (printedNewLine) {
                this.printedNewLine = false;
                this.appendable.append("  ");
            }
            if (c == '\n') {
                printedNewLine2 = true;
            }
            this.printedNewLine = printedNewLine2;
            this.appendable.append(c);
            return this;
        }
        
        @Override
        public Appendable append(CharSequence safeSequence) throws IOException {
            safeSequence = this.safeSequence(safeSequence);
            return this.append(safeSequence, 0, safeSequence.length());
        }
        
        @Override
        public Appendable append(CharSequence safeSequence, final int n, final int n2) throws IOException {
            safeSequence = this.safeSequence(safeSequence);
            final boolean printedNewLine = this.printedNewLine;
            final boolean b = false;
            if (printedNewLine) {
                this.printedNewLine = false;
                this.appendable.append("  ");
            }
            boolean printedNewLine2 = b;
            if (safeSequence.length() > 0) {
                printedNewLine2 = b;
                if (safeSequence.charAt(n2 - 1) == '\n') {
                    printedNewLine2 = true;
                }
            }
            this.printedNewLine = printedNewLine2;
            this.appendable.append(safeSequence, n, n2);
            return this;
        }
    }
}

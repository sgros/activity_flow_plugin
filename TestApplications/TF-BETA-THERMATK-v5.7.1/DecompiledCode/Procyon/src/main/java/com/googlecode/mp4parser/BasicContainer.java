// 
// Decompiled by Procyon v0.5.34
// 

package com.googlecode.mp4parser;

import java.nio.channels.WritableByteChannel;
import java.io.EOFException;
import java.util.NoSuchElementException;
import com.googlecode.mp4parser.util.LazyList;
import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;
import java.nio.ByteBuffer;
import java.util.List;
import com.coremedia.iso.BoxParser;
import com.googlecode.mp4parser.util.Logger;
import java.io.Closeable;
import com.coremedia.iso.boxes.Box;
import java.util.Iterator;
import com.coremedia.iso.boxes.Container;

public class BasicContainer implements Container, Iterator<Box>, Closeable
{
    private static final Box EOF;
    private static Logger LOG;
    protected BoxParser boxParser;
    private List<Box> boxes;
    protected DataSource dataSource;
    long endPosition;
    Box lookahead;
    long parsePosition;
    long startPosition;
    
    static {
        EOF = new AbstractBox() {
            @Override
            protected void _parseDetails(final ByteBuffer byteBuffer) {
            }
            
            @Override
            protected void getContent(final ByteBuffer byteBuffer) {
            }
            
            @Override
            protected long getContentSize() {
                return 0L;
            }
        };
        BasicContainer.LOG = Logger.getLogger(BasicContainer.class);
    }
    
    public BasicContainer() {
        this.lookahead = null;
        this.parsePosition = 0L;
        this.startPosition = 0L;
        this.endPosition = 0L;
        this.boxes = new ArrayList<Box>();
    }
    
    public void addBox(final Box box) {
        if (box != null) {
            this.boxes = new ArrayList<Box>(this.getBoxes());
            box.setParent(this);
            this.boxes.add(box);
        }
    }
    
    @Override
    public void close() throws IOException {
        this.dataSource.close();
    }
    
    public List<Box> getBoxes() {
        if (this.dataSource != null && this.lookahead != BasicContainer.EOF) {
            return new LazyList<Box>(this.boxes, this);
        }
        return this.boxes;
    }
    
    protected long getContainerSize() {
        long n = 0L;
        for (int i = 0; i < this.getBoxes().size(); ++i) {
            n += this.boxes.get(i).getSize();
        }
        return n;
    }
    
    @Override
    public boolean hasNext() {
        final Box lookahead = this.lookahead;
        if (lookahead == BasicContainer.EOF) {
            return false;
        }
        if (lookahead != null) {
            return true;
        }
        try {
            this.lookahead = this.next();
            return true;
        }
        catch (NoSuchElementException ex) {
            this.lookahead = BasicContainer.EOF;
            return false;
        }
    }
    
    @Override
    public Box next() {
        final Box lookahead = this.lookahead;
        if (lookahead != null && lookahead != BasicContainer.EOF) {
            this.lookahead = null;
            return lookahead;
        }
        final DataSource dataSource = this.dataSource;
        if (dataSource != null && this.parsePosition < this.endPosition) {
            try {
                synchronized (dataSource) {
                    this.dataSource.position(this.parsePosition);
                    final Box box = this.boxParser.parseBox(this.dataSource, this);
                    this.parsePosition = this.dataSource.position();
                    return box;
                }
            }
            catch (IOException ex) {
                throw new NoSuchElementException();
            }
            catch (EOFException ex2) {
                throw new NoSuchElementException();
            }
        }
        this.lookahead = BasicContainer.EOF;
        throw new NoSuchElementException();
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName());
        sb.append("[");
        for (int i = 0; i < this.boxes.size(); ++i) {
            if (i > 0) {
                sb.append(";");
            }
            sb.append(this.boxes.get(i).toString());
        }
        sb.append("]");
        return sb.toString();
    }
    
    public final void writeContainer(final WritableByteChannel writableByteChannel) throws IOException {
        final Iterator<Box> iterator = this.getBoxes().iterator();
        while (iterator.hasNext()) {
            iterator.next().getBox(writableByteChannel);
        }
    }
}

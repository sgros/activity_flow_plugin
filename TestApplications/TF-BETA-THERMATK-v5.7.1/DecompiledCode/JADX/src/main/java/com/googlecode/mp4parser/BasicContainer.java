package com.googlecode.mp4parser;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.util.LazyList;
import com.googlecode.mp4parser.util.Logger;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class BasicContainer implements Container, Iterator<Box>, Closeable {
    private static final Box EOF = new AbstractBox("eof ") {
        /* Access modifiers changed, original: protected */
        public void _parseDetails(ByteBuffer byteBuffer) {
        }

        /* Access modifiers changed, original: protected */
        public void getContent(ByteBuffer byteBuffer) {
        }

        /* Access modifiers changed, original: protected */
        public long getContentSize() {
            return 0;
        }
    };
    private static Logger LOG = Logger.getLogger(BasicContainer.class);
    protected BoxParser boxParser;
    private List<Box> boxes = new ArrayList();
    protected DataSource dataSource;
    long endPosition = 0;
    Box lookahead = null;
    long parsePosition = 0;
    long startPosition = 0;

    public List<Box> getBoxes() {
        if (this.dataSource == null || this.lookahead == EOF) {
            return this.boxes;
        }
        return new LazyList(this.boxes, this);
    }

    /* Access modifiers changed, original: protected */
    public long getContainerSize() {
        long j = 0;
        for (int i = 0; i < getBoxes().size(); i++) {
            j += ((Box) this.boxes.get(i)).getSize();
        }
        return j;
    }

    public void addBox(Box box) {
        if (box != null) {
            this.boxes = new ArrayList(getBoxes());
            box.setParent(this);
            this.boxes.add(box);
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public boolean hasNext() {
        Box box = this.lookahead;
        if (box == EOF) {
            return false;
        }
        if (box != null) {
            return true;
        }
        try {
            this.lookahead = next();
            return true;
        } catch (NoSuchElementException unused) {
            this.lookahead = EOF;
            return false;
        }
    }

    public Box next() {
        Box box = this.lookahead;
        if (box == null || box == EOF) {
            DataSource dataSource = this.dataSource;
            if (dataSource == null || this.parsePosition >= this.endPosition) {
                this.lookahead = EOF;
                throw new NoSuchElementException();
            }
            try {
                Box parseBox;
                synchronized (dataSource) {
                    this.dataSource.position(this.parsePosition);
                    parseBox = this.boxParser.parseBox(this.dataSource, this);
                    this.parsePosition = this.dataSource.position();
                }
                return parseBox;
            } catch (EOFException unused) {
                throw new NoSuchElementException();
            } catch (IOException unused2) {
                throw new NoSuchElementException();
            }
        }
        this.lookahead = null;
        return box;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getClass().getSimpleName());
        stringBuilder.append("[");
        for (int i = 0; i < this.boxes.size(); i++) {
            if (i > 0) {
                stringBuilder.append(";");
            }
            stringBuilder.append(((Box) this.boxes.get(i)).toString());
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public final void writeContainer(WritableByteChannel writableByteChannel) throws IOException {
        for (Box box : getBoxes()) {
            box.getBox(writableByteChannel);
        }
    }

    public void close() throws IOException {
        this.dataSource.close();
    }
}

package com.googlecode.mp4parser;

import com.coremedia.iso.BoxParser;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.util.LazyList;
import com.googlecode.mp4parser.util.Logger;
import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class BasicContainer implements Container, Iterator, Closeable {
   private static final Box EOF = new AbstractBox("eof ") {
      protected void _parseDetails(ByteBuffer var1) {
      }

      protected void getContent(ByteBuffer var1) {
      }

      protected long getContentSize() {
         return 0L;
      }
   };
   private static Logger LOG = Logger.getLogger(BasicContainer.class);
   protected BoxParser boxParser;
   private List boxes = new ArrayList();
   protected DataSource dataSource;
   long endPosition = 0L;
   Box lookahead = null;
   long parsePosition = 0L;
   long startPosition = 0L;

   public void addBox(Box var1) {
      if (var1 != null) {
         this.boxes = new ArrayList(this.getBoxes());
         var1.setParent(this);
         this.boxes.add(var1);
      }

   }

   public void close() throws IOException {
      this.dataSource.close();
   }

   public List getBoxes() {
      return (List)(this.dataSource != null && this.lookahead != EOF ? new LazyList(this.boxes, this) : this.boxes);
   }

   protected long getContainerSize() {
      long var1 = 0L;

      for(int var3 = 0; var3 < this.getBoxes().size(); ++var3) {
         var1 += ((Box)this.boxes.get(var3)).getSize();
      }

      return var1;
   }

   public boolean hasNext() {
      Box var1 = this.lookahead;
      if (var1 == EOF) {
         return false;
      } else if (var1 != null) {
         return true;
      } else {
         try {
            this.lookahead = this.next();
            return true;
         } catch (NoSuchElementException var2) {
            this.lookahead = EOF;
            return false;
         }
      }
   }

   public Box next() {
      // $FF: Couldn't be decompiled
   }

   public void remove() {
      throw new UnsupportedOperationException();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getClass().getSimpleName());
      var1.append("[");

      for(int var2 = 0; var2 < this.boxes.size(); ++var2) {
         if (var2 > 0) {
            var1.append(";");
         }

         var1.append(((Box)this.boxes.get(var2)).toString());
      }

      var1.append("]");
      return var1.toString();
   }

   public final void writeContainer(WritableByteChannel var1) throws IOException {
      Iterator var2 = this.getBoxes().iterator();

      while(var2.hasNext()) {
         ((Box)var2.next()).getBox(var1);
      }

   }
}

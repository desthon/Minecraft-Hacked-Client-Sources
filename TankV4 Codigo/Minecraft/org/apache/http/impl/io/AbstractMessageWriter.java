package org.apache.http.impl.io;

import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.HttpMessageWriter;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.message.BasicLineFormatter;
import org.apache.http.message.LineFormatter;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public abstract class AbstractMessageWriter implements HttpMessageWriter {
   protected final SessionOutputBuffer sessionBuffer;
   protected final CharArrayBuffer lineBuf;
   protected final LineFormatter lineFormatter;

   /** @deprecated */
   @Deprecated
   public AbstractMessageWriter(SessionOutputBuffer var1, LineFormatter var2, HttpParams var3) {
      Args.notNull(var1, "Session input buffer");
      this.sessionBuffer = var1;
      this.lineBuf = new CharArrayBuffer(128);
      this.lineFormatter = (LineFormatter)(var2 != null ? var2 : BasicLineFormatter.INSTANCE);
   }

   public AbstractMessageWriter(SessionOutputBuffer var1, LineFormatter var2) {
      this.sessionBuffer = (SessionOutputBuffer)Args.notNull(var1, "Session input buffer");
      this.lineFormatter = (LineFormatter)(var2 != null ? var2 : BasicLineFormatter.INSTANCE);
      this.lineBuf = new CharArrayBuffer(128);
   }

   protected abstract void writeHeadLine(HttpMessage var1) throws IOException;

   public void write(HttpMessage var1) throws IOException, HttpException {
      Args.notNull(var1, "HTTP message");
      this.writeHeadLine(var1);
      HeaderIterator var2 = var1.headerIterator();

      while(var2.hasNext()) {
         Header var3 = var2.nextHeader();
         this.sessionBuffer.writeLine(this.lineFormatter.formatHeader(this.lineBuf, var3));
      }

      this.lineBuf.clear();
      this.sessionBuffer.writeLine(this.lineBuf);
   }
}

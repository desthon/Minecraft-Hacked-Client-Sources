package org.lwjgl.opengl;

import java.awt.Canvas;
import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;

final class LinuxAWTGLCanvasPeerInfo extends LinuxPeerInfo {
   private final Canvas component;
   private final AWTSurfaceLock awt_surface = new AWTSurfaceLock();
   private int screen = -1;

   LinuxAWTGLCanvasPeerInfo(Canvas var1) {
      this.component = var1;
   }

   protected void doLockAndInitHandle() throws LWJGLException {
      ByteBuffer var1 = this.awt_surface.lockAndGetHandle(this.component);
      if (this.screen == -1) {
         try {
            this.screen = getScreenFromSurfaceInfo(var1);
         } catch (LWJGLException var3) {
            LWJGLUtil.log("Got exception while trying to determine screen: " + var3);
            this.screen = 0;
         }
      }

      nInitHandle(this.screen, var1, this.getHandle());
   }

   private static native int getScreenFromSurfaceInfo(ByteBuffer var0) throws LWJGLException;

   private static native void nInitHandle(int var0, ByteBuffer var1, ByteBuffer var2) throws LWJGLException;

   protected void doUnlock() throws LWJGLException {
      this.awt_surface.unlock();
   }
}

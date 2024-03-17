package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferChecks;
import org.lwjgl.MemoryUtil;

public final class GL42 {
   public static final int GL_COMPRESSED_RGBA_BPTC_UNORM = 36492;
   public static final int GL_COMPRESSED_SRGB_ALPHA_BPTC_UNORM = 36493;
   public static final int GL_COMPRESSED_RGB_BPTC_SIGNED_FLOAT = 36494;
   public static final int GL_COMPRESSED_RGB_BPTC_UNSIGNED_FLOAT = 36495;
   public static final int GL_UNPACK_COMPRESSED_BLOCK_WIDTH = 37159;
   public static final int GL_UNPACK_COMPRESSED_BLOCK_HEIGHT = 37160;
   public static final int GL_UNPACK_COMPRESSED_BLOCK_DEPTH = 37161;
   public static final int GL_UNPACK_COMPRESSED_BLOCK_SIZE = 37162;
   public static final int GL_PACK_COMPRESSED_BLOCK_WIDTH = 37163;
   public static final int GL_PACK_COMPRESSED_BLOCK_HEIGHT = 37164;
   public static final int GL_PACK_COMPRESSED_BLOCK_DEPTH = 37165;
   public static final int GL_PACK_COMPRESSED_BLOCK_SIZE = 37166;
   public static final int GL_ATOMIC_COUNTER_BUFFER = 37568;
   public static final int GL_ATOMIC_COUNTER_BUFFER_BINDING = 37569;
   public static final int GL_ATOMIC_COUNTER_BUFFER_START = 37570;
   public static final int GL_ATOMIC_COUNTER_BUFFER_SIZE = 37571;
   public static final int GL_ATOMIC_COUNTER_BUFFER_DATA_SIZE = 37572;
   public static final int GL_ATOMIC_COUNTER_BUFFER_ACTIVE_ATOMIC_COUNTERS = 37573;
   public static final int GL_ATOMIC_COUNTER_BUFFER_ACTIVE_ATOMIC_COUNTER_INDICES = 37574;
   public static final int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_VERTEX_SHADER = 37575;
   public static final int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_TESS_CONTROL_SHADER = 37576;
   public static final int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_TESS_EVALUATION_SHADER = 37577;
   public static final int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_GEOMETRY_SHADER = 37578;
   public static final int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_FRAGMENT_SHADER = 37579;
   public static final int GL_MAX_VERTEX_ATOMIC_COUNTER_BUFFERS = 37580;
   public static final int GL_MAX_TESS_CONTROL_ATOMIC_COUNTER_BUFFERS = 37581;
   public static final int GL_MAX_TESS_EVALUATION_ATOMIC_COUNTER_BUFFERS = 37582;
   public static final int GL_MAX_GEOMETRY_ATOMIC_COUNTER_BUFFERS = 37583;
   public static final int GL_MAX_FRAGMENT_ATOMIC_COUNTER_BUFFERS = 37584;
   public static final int GL_MAX_COMBINED_ATOMIC_COUNTER_BUFFERS = 37585;
   public static final int GL_MAX_VERTEX_ATOMIC_COUNTERS = 37586;
   public static final int GL_MAX_TESS_CONTROL_ATOMIC_COUNTERS = 37587;
   public static final int GL_MAX_TESS_EVALUATION_ATOMIC_COUNTERS = 37588;
   public static final int GL_MAX_GEOMETRY_ATOMIC_COUNTERS = 37589;
   public static final int GL_MAX_FRAGMENT_ATOMIC_COUNTERS = 37590;
   public static final int GL_MAX_COMBINED_ATOMIC_COUNTERS = 37591;
   public static final int GL_MAX_ATOMIC_COUNTER_BUFFER_SIZE = 37592;
   public static final int GL_MAX_ATOMIC_COUNTER_BUFFER_BINDINGS = 37596;
   public static final int GL_ACTIVE_ATOMIC_COUNTER_BUFFERS = 37593;
   public static final int GL_UNIFORM_ATOMIC_COUNTER_BUFFER_INDEX = 37594;
   public static final int GL_UNSIGNED_INT_ATOMIC_COUNTER = 37595;
   public static final int GL_TEXTURE_IMMUTABLE_FORMAT = 37167;
   public static final int GL_MAX_IMAGE_UNITS = 36664;
   public static final int GL_MAX_COMBINED_IMAGE_UNITS_AND_FRAGMENT_OUTPUTS = 36665;
   public static final int GL_MAX_IMAGE_SAMPLES = 36973;
   public static final int GL_MAX_VERTEX_IMAGE_UNIFORMS = 37066;
   public static final int GL_MAX_TESS_CONTROL_IMAGE_UNIFORMS = 37067;
   public static final int GL_MAX_TESS_EVALUATION_IMAGE_UNIFORMS = 37068;
   public static final int GL_MAX_GEOMETRY_IMAGE_UNIFORMS = 37069;
   public static final int GL_MAX_FRAGMENT_IMAGE_UNIFORMS = 37070;
   public static final int GL_MAX_COMBINED_IMAGE_UNIFORMS = 37071;
   public static final int GL_IMAGE_BINDING_NAME = 36666;
   public static final int GL_IMAGE_BINDING_LEVEL = 36667;
   public static final int GL_IMAGE_BINDING_LAYERED = 36668;
   public static final int GL_IMAGE_BINDING_LAYER = 36669;
   public static final int GL_IMAGE_BINDING_ACCESS = 36670;
   public static final int GL_IMAGE_BINDING_FORMAT = 36974;
   public static final int GL_VERTEX_ATTRIB_ARRAY_BARRIER_BIT = 1;
   public static final int GL_ELEMENT_ARRAY_BARRIER_BIT = 2;
   public static final int GL_UNIFORM_BARRIER_BIT = 4;
   public static final int GL_TEXTURE_FETCH_BARRIER_BIT = 8;
   public static final int GL_SHADER_IMAGE_ACCESS_BARRIER_BIT = 32;
   public static final int GL_COMMAND_BARRIER_BIT = 64;
   public static final int GL_PIXEL_BUFFER_BARRIER_BIT = 128;
   public static final int GL_TEXTURE_UPDATE_BARRIER_BIT = 256;
   public static final int GL_BUFFER_UPDATE_BARRIER_BIT = 512;
   public static final int GL_FRAMEBUFFER_BARRIER_BIT = 1024;
   public static final int GL_TRANSFORM_FEEDBACK_BARRIER_BIT = 2048;
   public static final int GL_ATOMIC_COUNTER_BARRIER_BIT = 4096;
   public static final int GL_ALL_BARRIER_BITS = -1;
   public static final int GL_IMAGE_1D = 36940;
   public static final int GL_IMAGE_2D = 36941;
   public static final int GL_IMAGE_3D = 36942;
   public static final int GL_IMAGE_2D_RECT = 36943;
   public static final int GL_IMAGE_CUBE = 36944;
   public static final int GL_IMAGE_BUFFER = 36945;
   public static final int GL_IMAGE_1D_ARRAY = 36946;
   public static final int GL_IMAGE_2D_ARRAY = 36947;
   public static final int GL_IMAGE_CUBE_MAP_ARRAY = 36948;
   public static final int GL_IMAGE_2D_MULTISAMPLE = 36949;
   public static final int GL_IMAGE_2D_MULTISAMPLE_ARRAY = 36950;
   public static final int GL_INT_IMAGE_1D = 36951;
   public static final int GL_INT_IMAGE_2D = 36952;
   public static final int GL_INT_IMAGE_3D = 36953;
   public static final int GL_INT_IMAGE_2D_RECT = 36954;
   public static final int GL_INT_IMAGE_CUBE = 36955;
   public static final int GL_INT_IMAGE_BUFFER = 36956;
   public static final int GL_INT_IMAGE_1D_ARRAY = 36957;
   public static final int GL_INT_IMAGE_2D_ARRAY = 36958;
   public static final int GL_INT_IMAGE_CUBE_MAP_ARRAY = 36959;
   public static final int GL_INT_IMAGE_2D_MULTISAMPLE = 36960;
   public static final int GL_INT_IMAGE_2D_MULTISAMPLE_ARRAY = 36961;
   public static final int GL_UNSIGNED_INT_IMAGE_1D = 36962;
   public static final int GL_UNSIGNED_INT_IMAGE_2D = 36963;
   public static final int GL_UNSIGNED_INT_IMAGE_3D = 36964;
   public static final int GL_UNSIGNED_INT_IMAGE_2D_RECT = 36965;
   public static final int GL_UNSIGNED_INT_IMAGE_CUBE = 36966;
   public static final int GL_UNSIGNED_INT_IMAGE_BUFFER = 36967;
   public static final int GL_UNSIGNED_INT_IMAGE_1D_ARRAY = 36968;
   public static final int GL_UNSIGNED_INT_IMAGE_2D_ARRAY = 36969;
   public static final int GL_UNSIGNED_INT_IMAGE_CUBE_MAP_ARRAY = 36970;
   public static final int GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE = 36971;
   public static final int GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY = 36972;
   public static final int GL_IMAGE_FORMAT_COMPATIBILITY_TYPE = 37063;
   public static final int GL_IMAGE_FORMAT_COMPATIBILITY_BY_SIZE = 37064;
   public static final int IMAGE_FORMAT_COMPATIBILITY_BY_CLASS = 37065;
   public static final int GL_NUM_SAMPLE_COUNTS = 37760;
   public static final int GL_MIN_MAP_BUFFER_ALIGNMENT = 37052;

   private GL42() {
   }

   public static void glGetActiveAtomicCounterBuffer(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetActiveAtomicCounterBufferiv;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkBuffer((IntBuffer)var3, 1);
      nglGetActiveAtomicCounterBufferiv(var0, var1, var2, MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetActiveAtomicCounterBufferiv(int var0, int var1, int var2, long var3, long var5);

   public static int glGetActiveAtomicCounterBuffer(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetActiveAtomicCounterBufferiv;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getBufferInt(var3);
      nglGetActiveAtomicCounterBufferiv(var0, var1, var2, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }

   public static void glTexStorage1D(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glTexStorage1D;
      BufferChecks.checkFunctionAddress(var5);
      nglTexStorage1D(var0, var1, var2, var3, var5);
   }

   static native void nglTexStorage1D(int var0, int var1, int var2, int var3, long var4);

   public static void glTexStorage2D(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glTexStorage2D;
      BufferChecks.checkFunctionAddress(var6);
      nglTexStorage2D(var0, var1, var2, var3, var4, var6);
   }

   static native void nglTexStorage2D(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glTexStorage3D(int var0, int var1, int var2, int var3, int var4, int var5) {
      ContextCapabilities var6 = GLContext.getCapabilities();
      long var7 = var6.glTexStorage3D;
      BufferChecks.checkFunctionAddress(var7);
      nglTexStorage3D(var0, var1, var2, var3, var4, var5, var7);
   }

   static native void nglTexStorage3D(int var0, int var1, int var2, int var3, int var4, int var5, long var6);

   public static void glDrawTransformFeedbackInstanced(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glDrawTransformFeedbackInstanced;
      BufferChecks.checkFunctionAddress(var4);
      nglDrawTransformFeedbackInstanced(var0, var1, var2, var4);
   }

   static native void nglDrawTransformFeedbackInstanced(int var0, int var1, int var2, long var3);

   public static void glDrawTransformFeedbackStreamInstanced(int var0, int var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glDrawTransformFeedbackStreamInstanced;
      BufferChecks.checkFunctionAddress(var5);
      nglDrawTransformFeedbackStreamInstanced(var0, var1, var2, var3, var5);
   }

   static native void nglDrawTransformFeedbackStreamInstanced(int var0, int var1, int var2, int var3, long var4);

   public static void glDrawArraysInstancedBaseInstance(int var0, int var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glDrawArraysInstancedBaseInstance;
      BufferChecks.checkFunctionAddress(var6);
      nglDrawArraysInstancedBaseInstance(var0, var1, var2, var3, var4, var6);
   }

   static native void nglDrawArraysInstancedBaseInstance(int var0, int var1, int var2, int var3, int var4, long var5);

   public static void glDrawElementsInstancedBaseInstance(int var0, ByteBuffer var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glDrawElementsInstancedBaseInstance;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureElementVBOdisabled(var4);
      BufferChecks.checkDirect(var1);
      nglDrawElementsInstancedBaseInstance(var0, var1.remaining(), 5121, MemoryUtil.getAddress(var1), var2, var3, var5);
   }

   public static void glDrawElementsInstancedBaseInstance(int var0, IntBuffer var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glDrawElementsInstancedBaseInstance;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureElementVBOdisabled(var4);
      BufferChecks.checkDirect(var1);
      nglDrawElementsInstancedBaseInstance(var0, var1.remaining(), 5125, MemoryUtil.getAddress(var1), var2, var3, var5);
   }

   public static void glDrawElementsInstancedBaseInstance(int var0, ShortBuffer var1, int var2, int var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glDrawElementsInstancedBaseInstance;
      BufferChecks.checkFunctionAddress(var5);
      GLChecks.ensureElementVBOdisabled(var4);
      BufferChecks.checkDirect(var1);
      nglDrawElementsInstancedBaseInstance(var0, var1.remaining(), 5123, MemoryUtil.getAddress(var1), var2, var3, var5);
   }

   static native void nglDrawElementsInstancedBaseInstance(int var0, int var1, int var2, long var3, int var5, int var6, long var7);

   public static void glDrawElementsInstancedBaseInstance(int var0, int var1, int var2, long var3, int var5, int var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glDrawElementsInstancedBaseInstance;
      BufferChecks.checkFunctionAddress(var8);
      GLChecks.ensureElementVBOenabled(var7);
      nglDrawElementsInstancedBaseInstanceBO(var0, var1, var2, var3, var5, var6, var8);
   }

   static native void nglDrawElementsInstancedBaseInstanceBO(int var0, int var1, int var2, long var3, int var5, int var6, long var7);

   public static void glDrawElementsInstancedBaseVertexBaseInstance(int var0, ByteBuffer var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glDrawElementsInstancedBaseVertexBaseInstance;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureElementVBOdisabled(var5);
      BufferChecks.checkDirect(var1);
      nglDrawElementsInstancedBaseVertexBaseInstance(var0, var1.remaining(), 5121, MemoryUtil.getAddress(var1), var2, var3, var4, var6);
   }

   public static void glDrawElementsInstancedBaseVertexBaseInstance(int var0, IntBuffer var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glDrawElementsInstancedBaseVertexBaseInstance;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureElementVBOdisabled(var5);
      BufferChecks.checkDirect(var1);
      nglDrawElementsInstancedBaseVertexBaseInstance(var0, var1.remaining(), 5125, MemoryUtil.getAddress(var1), var2, var3, var4, var6);
   }

   public static void glDrawElementsInstancedBaseVertexBaseInstance(int var0, ShortBuffer var1, int var2, int var3, int var4) {
      ContextCapabilities var5 = GLContext.getCapabilities();
      long var6 = var5.glDrawElementsInstancedBaseVertexBaseInstance;
      BufferChecks.checkFunctionAddress(var6);
      GLChecks.ensureElementVBOdisabled(var5);
      BufferChecks.checkDirect(var1);
      nglDrawElementsInstancedBaseVertexBaseInstance(var0, var1.remaining(), 5123, MemoryUtil.getAddress(var1), var2, var3, var4, var6);
   }

   static native void nglDrawElementsInstancedBaseVertexBaseInstance(int var0, int var1, int var2, long var3, int var5, int var6, int var7, long var8);

   public static void glDrawElementsInstancedBaseVertexBaseInstance(int var0, int var1, int var2, long var3, int var5, int var6, int var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glDrawElementsInstancedBaseVertexBaseInstance;
      BufferChecks.checkFunctionAddress(var9);
      GLChecks.ensureElementVBOenabled(var8);
      nglDrawElementsInstancedBaseVertexBaseInstanceBO(var0, var1, var2, var3, var5, var6, var7, var9);
   }

   static native void nglDrawElementsInstancedBaseVertexBaseInstanceBO(int var0, int var1, int var2, long var3, int var5, int var6, int var7, long var8);

   public static void glBindImageTexture(int var0, int var1, int var2, boolean var3, int var4, int var5, int var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glBindImageTexture;
      BufferChecks.checkFunctionAddress(var8);
      nglBindImageTexture(var0, var1, var2, var3, var4, var5, var6, var8);
   }

   static native void nglBindImageTexture(int var0, int var1, int var2, boolean var3, int var4, int var5, int var6, long var7);

   public static void glMemoryBarrier(int var0) {
      ContextCapabilities var1 = GLContext.getCapabilities();
      long var2 = var1.glMemoryBarrier;
      BufferChecks.checkFunctionAddress(var2);
      nglMemoryBarrier(var0, var2);
   }

   static native void nglMemoryBarrier(int var0, long var1);

   public static void glGetInternalformat(int var0, int var1, int var2, IntBuffer var3) {
      ContextCapabilities var4 = GLContext.getCapabilities();
      long var5 = var4.glGetInternalformativ;
      BufferChecks.checkFunctionAddress(var5);
      BufferChecks.checkDirect(var3);
      nglGetInternalformativ(var0, var1, var2, var3.remaining(), MemoryUtil.getAddress(var3), var5);
   }

   static native void nglGetInternalformativ(int var0, int var1, int var2, int var3, long var4, long var6);

   public static int glGetInternalformat(int var0, int var1, int var2) {
      ContextCapabilities var3 = GLContext.getCapabilities();
      long var4 = var3.glGetInternalformativ;
      BufferChecks.checkFunctionAddress(var4);
      IntBuffer var6 = APIUtil.getBufferInt(var3);
      nglGetInternalformativ(var0, var1, var2, 1, MemoryUtil.getAddress(var6), var4);
      return var6.get(0);
   }
}

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class AMDSparseTexture {
   public static final int GL_TEXTURE_STORAGE_SPARSE_BIT_AMD = 1;
   public static final int GL_VIRTUAL_PAGE_SIZE_X_AMD = 37269;
   public static final int GL_VIRTUAL_PAGE_SIZE_Y_AMD = 37270;
   public static final int GL_VIRTUAL_PAGE_SIZE_Z_AMD = 37271;
   public static final int GL_MAX_SPARSE_TEXTURE_SIZE_AMD = 37272;
   public static final int GL_MAX_SPARSE_3D_TEXTURE_SIZE_AMD = 37273;
   public static final int GL_MAX_SPARSE_ARRAY_TEXTURE_LAYERS = 37274;
   public static final int GL_MIN_SPARSE_LEVEL_AMD = 37275;
   public static final int GL_MIN_LOD_WARNING_AMD = 37276;

   private AMDSparseTexture() {
   }

   public static void glTexStorageSparseAMD(int var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      ContextCapabilities var7 = GLContext.getCapabilities();
      long var8 = var7.glTexStorageSparseAMD;
      BufferChecks.checkFunctionAddress(var8);
      nglTexStorageSparseAMD(var0, var1, var2, var3, var4, var5, var6, var8);
   }

   static native void nglTexStorageSparseAMD(int var0, int var1, int var2, int var3, int var4, int var5, int var6, long var7);

   public static void glTextureStorageSparseAMD(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      ContextCapabilities var8 = GLContext.getCapabilities();
      long var9 = var8.glTextureStorageSparseAMD;
      BufferChecks.checkFunctionAddress(var9);
      nglTextureStorageSparseAMD(var0, var1, var2, var3, var4, var5, var6, var7, var9);
   }

   static native void nglTextureStorageSparseAMD(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, long var8);
}

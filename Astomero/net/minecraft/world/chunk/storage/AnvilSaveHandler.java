package net.minecraft.world.chunk.storage;

import java.io.*;
import net.minecraft.world.*;
import net.minecraft.nbt.*;
import net.minecraft.world.storage.*;

public class AnvilSaveHandler extends SaveHandler
{
    public AnvilSaveHandler(final File savesDirectory, final String p_i2142_2_, final boolean storePlayerdata) {
        super(savesDirectory, p_i2142_2_, storePlayerdata);
    }
    
    @Override
    public IChunkLoader getChunkLoader(final WorldProvider provider) {
        final File file1 = this.getWorldDirectory();
        if (provider instanceof WorldProviderHell) {
            final File file2 = new File(file1, "DIM-1");
            file2.mkdirs();
            return new AnvilChunkLoader(file2);
        }
        if (provider instanceof WorldProviderEnd) {
            final File file3 = new File(file1, "DIM1");
            file3.mkdirs();
            return new AnvilChunkLoader(file3);
        }
        return new AnvilChunkLoader(file1);
    }
    
    @Override
    public void saveWorldInfoWithPlayer(final WorldInfo worldInformation, final NBTTagCompound tagCompound) {
        worldInformation.setSaveVersion(19133);
        super.saveWorldInfoWithPlayer(worldInformation, tagCompound);
    }
    
    @Override
    public void flush() {
        try {
            ThreadedFileIOBase.getThreadedIOInstance().waitForFinish();
        }
        catch (InterruptedException interruptedexception) {
            interruptedexception.printStackTrace();
        }
        RegionFileCache.clearRegionFileReferences();
    }
}
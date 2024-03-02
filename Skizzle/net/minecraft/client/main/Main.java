/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.GsonBuilder
 *  com.mojang.authlib.properties.PropertyMap
 *  com.mojang.authlib.properties.PropertyMap$Serializer
 *  joptsimple.ArgumentAcceptingOptionSpec
 *  joptsimple.NonOptionArgumentSpec
 *  joptsimple.OptionParser
 *  joptsimple.OptionSet
 *  joptsimple.OptionSpec
 */
package net.minecraft.client.main;

import com.google.gson.GsonBuilder;
import com.mojang.authlib.properties.PropertyMap;
import java.io.File;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.List;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.NonOptionArgumentSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.util.Session;

public class Main {
    private static final String __OBFID = "CL_00001461";

    public static void main(String[] p_main_0_) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        OptionParser var1 = new OptionParser();
        var1.allowsUnrecognizedOptions();
        var1.accepts("demo");
        var1.accepts("fullscreen");
        var1.accepts("checkGlErrors");
        ArgumentAcceptingOptionSpec var2 = var1.accepts("server").withRequiredArg();
        ArgumentAcceptingOptionSpec var3 = var1.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo((Object)25565, (Object[])new Integer[0]);
        ArgumentAcceptingOptionSpec var4 = var1.accepts("gameDir").withRequiredArg().ofType(File.class).defaultsTo((Object)new File("."), (Object[])new File[0]);
        ArgumentAcceptingOptionSpec var5 = var1.accepts("assetsDir").withRequiredArg().ofType(File.class);
        ArgumentAcceptingOptionSpec var6 = var1.accepts("resourcePackDir").withRequiredArg().ofType(File.class);
        ArgumentAcceptingOptionSpec var7 = var1.accepts("proxyHost").withRequiredArg();
        ArgumentAcceptingOptionSpec var8 = var1.accepts("proxyPort").withRequiredArg().defaultsTo((Object)"8080", (Object[])new String[0]).ofType(Integer.class);
        ArgumentAcceptingOptionSpec var9 = var1.accepts("proxyUser").withRequiredArg();
        ArgumentAcceptingOptionSpec var10 = var1.accepts("proxyPass").withRequiredArg();
        ArgumentAcceptingOptionSpec var11 = var1.accepts("username").withRequiredArg().defaultsTo((Object)("Player" + Minecraft.getSystemTime() % 1000L), (Object[])new String[0]);
        ArgumentAcceptingOptionSpec var12 = var1.accepts("uuid").withRequiredArg();
        ArgumentAcceptingOptionSpec var13 = var1.accepts("accessToken").withRequiredArg().required();
        ArgumentAcceptingOptionSpec var14 = var1.accepts("version").withRequiredArg().required();
        ArgumentAcceptingOptionSpec var15 = var1.accepts("width").withRequiredArg().ofType(Integer.class).defaultsTo((Object)854, (Object[])new Integer[0]);
        ArgumentAcceptingOptionSpec var16 = var1.accepts("height").withRequiredArg().ofType(Integer.class).defaultsTo((Object)480, (Object[])new Integer[0]);
        ArgumentAcceptingOptionSpec var17 = var1.accepts("userProperties").withRequiredArg().required();
        ArgumentAcceptingOptionSpec var18 = var1.accepts("assetIndex").withRequiredArg();
        ArgumentAcceptingOptionSpec var19 = var1.accepts("userType").withRequiredArg().defaultsTo((Object)"legacy", (Object[])new String[0]);
        NonOptionArgumentSpec var20 = var1.nonOptions();
        OptionSet var21 = var1.parse(p_main_0_);
        List var22 = var21.valuesOf((OptionSpec)var20);
        if (!var22.isEmpty()) {
            System.out.println("Completely ignored arguments: " + var22);
        }
        String var23 = (String)var21.valueOf((OptionSpec)var7);
        Proxy var24 = Proxy.NO_PROXY;
        if (var23 != null) {
            try {
                var24 = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(var23, (int)((Integer)var21.valueOf((OptionSpec)var8))));
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        final String var25 = (String)var21.valueOf((OptionSpec)var9);
        final String var26 = (String)var21.valueOf((OptionSpec)var10);
        if (!var24.equals(Proxy.NO_PROXY) && Main.func_110121_a(var25) && Main.func_110121_a(var26)) {
            Authenticator.setDefault(new Authenticator(){
                private static final String __OBFID = "CL_00000828";

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(var25, var26.toCharArray());
                }
            });
        }
        int var27 = (Integer)var21.valueOf((OptionSpec)var15);
        int var28 = (Integer)var21.valueOf((OptionSpec)var16);
        boolean var29 = var21.has("fullscreen");
        boolean var30 = var21.has("checkGlErrors");
        boolean var31 = var21.has("demo");
        String var32 = (String)var21.valueOf((OptionSpec)var14);
        PropertyMap var33 = (PropertyMap)new GsonBuilder().registerTypeAdapter(PropertyMap.class, (Object)new PropertyMap.Serializer()).create().fromJson((String)var21.valueOf((OptionSpec)var17), PropertyMap.class);
        File var34 = (File)var21.valueOf((OptionSpec)var4);
        File var35 = var21.has((OptionSpec)var5) ? (File)var21.valueOf((OptionSpec)var5) : new File(var34, "assets/");
        File var36 = var21.has((OptionSpec)var6) ? (File)var21.valueOf((OptionSpec)var6) : new File(var34, "resourcepacks/");
        String var37 = var21.has((OptionSpec)var12) ? (String)var12.value(var21) : (String)var11.value(var21);
        String var38 = var21.has((OptionSpec)var18) ? (String)var18.value(var21) : null;
        String var39 = (String)var21.valueOf((OptionSpec)var2);
        Integer var40 = (Integer)var21.valueOf((OptionSpec)var3);
        Session var41 = new Session((String)var11.value(var21), var37, (String)var13.value(var21), (String)var19.value(var21));
        GameConfiguration var42 = new GameConfiguration(new GameConfiguration.UserInformation(var41, var33, var24), new GameConfiguration.DisplayInformation(var27, var28, var29, var30), new GameConfiguration.FolderInformation(var34, var36, var35, var38), new GameConfiguration.GameInformation(var31, var32), new GameConfiguration.ServerInformation(var39, var40));
        Runtime.getRuntime().addShutdownHook(new Thread("Client Shutdown Thread"){
            private static final String __OBFID = "CL_00000829";

            @Override
            public void run() {
                Minecraft.stopIntegratedServer();
            }
        });
        Thread.currentThread().setName("Client thread");
        new Minecraft(var42).run();
    }

    private static boolean func_110121_a(String p_110121_0_) {
        return p_110121_0_ != null && !p_110121_0_.isEmpty();
    }
}


package com.ibm.icu.lang;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.UCharacterProperty;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.util.BitSet;
import java.util.Locale;
import java.util.MissingResourceException;

public final class UScript {
   public static final int INVALID_CODE = -1;
   public static final int COMMON = 0;
   public static final int INHERITED = 1;
   public static final int ARABIC = 2;
   public static final int ARMENIAN = 3;
   public static final int BENGALI = 4;
   public static final int BOPOMOFO = 5;
   public static final int CHEROKEE = 6;
   public static final int COPTIC = 7;
   public static final int CYRILLIC = 8;
   public static final int DESERET = 9;
   public static final int DEVANAGARI = 10;
   public static final int ETHIOPIC = 11;
   public static final int GEORGIAN = 12;
   public static final int GOTHIC = 13;
   public static final int GREEK = 14;
   public static final int GUJARATI = 15;
   public static final int GURMUKHI = 16;
   public static final int HAN = 17;
   public static final int HANGUL = 18;
   public static final int HEBREW = 19;
   public static final int HIRAGANA = 20;
   public static final int KANNADA = 21;
   public static final int KATAKANA = 22;
   public static final int KHMER = 23;
   public static final int LAO = 24;
   public static final int LATIN = 25;
   public static final int MALAYALAM = 26;
   public static final int MONGOLIAN = 27;
   public static final int MYANMAR = 28;
   public static final int OGHAM = 29;
   public static final int OLD_ITALIC = 30;
   public static final int ORIYA = 31;
   public static final int RUNIC = 32;
   public static final int SINHALA = 33;
   public static final int SYRIAC = 34;
   public static final int TAMIL = 35;
   public static final int TELUGU = 36;
   public static final int THAANA = 37;
   public static final int THAI = 38;
   public static final int TIBETAN = 39;
   public static final int CANADIAN_ABORIGINAL = 40;
   public static final int UCAS = 40;
   public static final int YI = 41;
   public static final int TAGALOG = 42;
   public static final int HANUNOO = 43;
   public static final int BUHID = 44;
   public static final int TAGBANWA = 45;
   public static final int BRAILLE = 46;
   public static final int CYPRIOT = 47;
   public static final int LIMBU = 48;
   public static final int LINEAR_B = 49;
   public static final int OSMANYA = 50;
   public static final int SHAVIAN = 51;
   public static final int TAI_LE = 52;
   public static final int UGARITIC = 53;
   public static final int KATAKANA_OR_HIRAGANA = 54;
   public static final int BUGINESE = 55;
   public static final int GLAGOLITIC = 56;
   public static final int KHAROSHTHI = 57;
   public static final int SYLOTI_NAGRI = 58;
   public static final int NEW_TAI_LUE = 59;
   public static final int TIFINAGH = 60;
   public static final int OLD_PERSIAN = 61;
   public static final int BALINESE = 62;
   public static final int BATAK = 63;
   public static final int BLISSYMBOLS = 64;
   public static final int BRAHMI = 65;
   public static final int CHAM = 66;
   public static final int CIRTH = 67;
   public static final int OLD_CHURCH_SLAVONIC_CYRILLIC = 68;
   public static final int DEMOTIC_EGYPTIAN = 69;
   public static final int HIERATIC_EGYPTIAN = 70;
   public static final int EGYPTIAN_HIEROGLYPHS = 71;
   public static final int KHUTSURI = 72;
   public static final int SIMPLIFIED_HAN = 73;
   public static final int TRADITIONAL_HAN = 74;
   public static final int PAHAWH_HMONG = 75;
   public static final int OLD_HUNGARIAN = 76;
   public static final int HARAPPAN_INDUS = 77;
   public static final int JAVANESE = 78;
   public static final int KAYAH_LI = 79;
   public static final int LATIN_FRAKTUR = 80;
   public static final int LATIN_GAELIC = 81;
   public static final int LEPCHA = 82;
   public static final int LINEAR_A = 83;
   public static final int MANDAIC = 84;
   public static final int MANDAEAN = 84;
   public static final int MAYAN_HIEROGLYPHS = 85;
   public static final int MEROITIC_HIEROGLYPHS = 86;
   public static final int MEROITIC = 86;
   public static final int NKO = 87;
   public static final int ORKHON = 88;
   public static final int OLD_PERMIC = 89;
   public static final int PHAGS_PA = 90;
   public static final int PHOENICIAN = 91;
   public static final int PHONETIC_POLLARD = 92;
   public static final int RONGORONGO = 93;
   public static final int SARATI = 94;
   public static final int ESTRANGELO_SYRIAC = 95;
   public static final int WESTERN_SYRIAC = 96;
   public static final int EASTERN_SYRIAC = 97;
   public static final int TENGWAR = 98;
   public static final int VAI = 99;
   public static final int VISIBLE_SPEECH = 100;
   public static final int CUNEIFORM = 101;
   public static final int UNWRITTEN_LANGUAGES = 102;
   public static final int UNKNOWN = 103;
   public static final int CARIAN = 104;
   public static final int JAPANESE = 105;
   public static final int LANNA = 106;
   public static final int LYCIAN = 107;
   public static final int LYDIAN = 108;
   public static final int OL_CHIKI = 109;
   public static final int REJANG = 110;
   public static final int SAURASHTRA = 111;
   public static final int SIGN_WRITING = 112;
   public static final int SUNDANESE = 113;
   public static final int MOON = 114;
   public static final int MEITEI_MAYEK = 115;
   public static final int IMPERIAL_ARAMAIC = 116;
   public static final int AVESTAN = 117;
   public static final int CHAKMA = 118;
   public static final int KOREAN = 119;
   public static final int KAITHI = 120;
   public static final int MANICHAEAN = 121;
   public static final int INSCRIPTIONAL_PAHLAVI = 122;
   public static final int PSALTER_PAHLAVI = 123;
   public static final int BOOK_PAHLAVI = 124;
   public static final int INSCRIPTIONAL_PARTHIAN = 125;
   public static final int SAMARITAN = 126;
   public static final int TAI_VIET = 127;
   public static final int MATHEMATICAL_NOTATION = 128;
   public static final int SYMBOLS = 129;
   public static final int BAMUM = 130;
   public static final int LISU = 131;
   public static final int NAKHI_GEBA = 132;
   public static final int OLD_SOUTH_ARABIAN = 133;
   public static final int BASSA_VAH = 134;
   public static final int DUPLOYAN_SHORTAND = 135;
   public static final int ELBASAN = 136;
   public static final int GRANTHA = 137;
   public static final int KPELLE = 138;
   public static final int LOMA = 139;
   public static final int MENDE = 140;
   public static final int MEROITIC_CURSIVE = 141;
   public static final int OLD_NORTH_ARABIAN = 142;
   public static final int NABATAEAN = 143;
   public static final int PALMYRENE = 144;
   public static final int SINDHI = 145;
   public static final int WARANG_CITI = 146;
   public static final int AFAKA = 147;
   public static final int JURCHEN = 148;
   public static final int MRO = 149;
   public static final int NUSHU = 150;
   public static final int SHARADA = 151;
   public static final int SORA_SOMPENG = 152;
   public static final int TAKRI = 153;
   public static final int TANGUT = 154;
   public static final int WOLEAI = 155;
   public static final int ANATOLIAN_HIEROGLYPHS = 156;
   public static final int KHOJKI = 157;
   public static final int TIRHUTA = 158;
   public static final int CODE_LIMIT = 159;
   private static final String kLocaleScript = "LocaleScript";
   private static final UScript.ScriptUsage[] usageValues = UScript.ScriptUsage.values();

   private static int[] findCodeFromLocale(ULocale var0) {
      ICUResourceBundle var1;
      try {
         var1 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", var0);
      } catch (MissingResourceException var7) {
         return null;
      }

      var1 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", var0);
      if (var1.getLoadingStatus() == 3 && !var0.equals(ULocale.getDefault())) {
         return null;
      } else {
         UResourceBundle var2 = var1.get("LocaleScript");
         int[] var3 = new int[var2.getSize()];
         int var4 = 0;

         for(int var5 = 0; var5 < var3.length; ++var5) {
            int var6 = UCharacter.getPropertyValueEnum(4106, var2.getString(var5));
            var3[var4++] = var6;
         }

         if (var4 < var3.length) {
            throw new IllegalStateException("bad locale data, listed " + var3.length + " scripts but found only " + var4);
         } else {
            return var3;
         }
      }
   }

   public static final int[] getCode(Locale var0) {
      return findCodeFromLocale(ULocale.forLocale(var0));
   }

   public static final int[] getCode(ULocale var0) {
      return findCodeFromLocale(var0);
   }

   public static final int[] getCode(String var0) {
      try {
         return new int[]{UCharacter.getPropertyValueEnum(4106, var0)};
      } catch (IllegalArgumentException var2) {
         return findCodeFromLocale(new ULocale(var0));
      }
   }

   /** @deprecated */
   public static final int getCodeFromName(String var0) {
      try {
         return UCharacter.getPropertyValueEnum(4106, var0);
      } catch (IllegalArgumentException var2) {
         return -1;
      }
   }

   public static final int getScript(int var0) {
      if (var0 >= 0 & var0 <= 1114111) {
         int var1 = UCharacterProperty.INSTANCE.getAdditional(var0, 0) & 12583167;
         if (var1 < 4194304) {
            return var1;
         } else if (var1 < 8388608) {
            return 0;
         } else {
            return var1 < 12582912 ? 1 : UCharacterProperty.INSTANCE.m_scriptExtensions_[var1 & 255];
         }
      } else {
         throw new IllegalArgumentException(Integer.toString(var0));
      }
   }

   public static final boolean hasScript(int var0, int var1) {
      int var2 = UCharacterProperty.INSTANCE.getAdditional(var0, 0) & 12583167;
      if (var2 < 4194304) {
         return var1 == var2;
      } else {
         char[] var3 = UCharacterProperty.INSTANCE.m_scriptExtensions_;
         int var4 = var2 & 255;
         if (var2 >= 12582912) {
            var4 = var3[var4 + 1];
         }

         if (var1 > 32767) {
            return false;
         } else {
            while(var1 > var3[var4]) {
               ++var4;
            }

            return var1 == (var3[var4] & 32767);
         }
      }
   }

   public static final int getScriptExtensions(int var0, BitSet var1) {
      var1.clear();
      int var2 = UCharacterProperty.INSTANCE.getAdditional(var0, 0) & 12583167;
      if (var2 < 4194304) {
         var1.set(var2);
         return var2;
      } else {
         char[] var3 = UCharacterProperty.INSTANCE.m_scriptExtensions_;
         int var4 = var2 & 255;
         if (var2 >= 12582912) {
            var4 = var3[var4 + 1];
         }

         int var5 = 0;

         char var6;
         do {
            var6 = var3[var4++];
            var1.set(var6 & 32767);
            ++var5;
         } while(var6 < '耀');

         return -var5;
      }
   }

   public static final String getName(int var0) {
      return UCharacter.getPropertyValueName(4106, var0, 1);
   }

   public static final String getShortName(int var0) {
      return UCharacter.getPropertyValueName(4106, var0, 0);
   }

   public static final String getSampleString(int var0) {
      int var1 = UScript.ScriptMetadata.access$000(var0) & 2097151;
      return var1 != 0 ? (new StringBuilder()).appendCodePoint(var1).toString() : "";
   }

   public static final UScript.ScriptUsage getUsage(int var0) {
      return usageValues[UScript.ScriptMetadata.access$000(var0) >> 21 & 7];
   }

   public static final boolean isRightToLeft(int var0) {
      return (UScript.ScriptMetadata.access$000(var0) & 16777216) != 0;
   }

   public static final boolean breaksBetweenLetters(int var0) {
      return (UScript.ScriptMetadata.access$000(var0) & 33554432) != 0;
   }

   public static final boolean isCased(int var0) {
      return (UScript.ScriptMetadata.access$000(var0) & 67108864) != 0;
   }

   private UScript() {
   }

   public static enum ScriptUsage {
      NOT_ENCODED,
      UNKNOWN,
      EXCLUDED,
      LIMITED_USE,
      ASPIRATIONAL,
      RECOMMENDED;

      private static final UScript.ScriptUsage[] $VALUES = new UScript.ScriptUsage[]{NOT_ENCODED, UNKNOWN, EXCLUDED, LIMITED_USE, ASPIRATIONAL, RECOMMENDED};
   }

   private static final class ScriptMetadata {
      private static final int UNKNOWN = 2097152;
      private static final int EXCLUSION = 4194304;
      private static final int LIMITED_USE = 6291456;
      private static final int ASPIRATIONAL = 8388608;
      private static final int RECOMMENDED = 10485760;
      private static final int RTL = 16777216;
      private static final int LB_LETTERS = 33554432;
      private static final int CASED = 67108864;
      private static final int[] SCRIPT_PROPS = new int[]{2097216, 2097928, 27264552, 77595953, 10488213, 44052741, 6296516, 71304162, 77595695, 71369748, 10488069, 10490528, 10490067, 4260656, 77595561, 10488469, 10488341, 44063575, 10529792, 27264464, 44052555, 10488981, 44052651, 44046208, 44043941, 77594700, 10489109, 8394790, 44044288, 4200079, 4260608, 10488597, 4200096, 10489221, 23070480, 10488725, 10488853, 27264908, 44043799, 10489664, 8393920, 41984648, 4200195, 4200227, 4200259, 4200291, 2107392, 21039104, 6297856, 4259840, 4260992, 4260944, 39852368, 4260736, 0, 4200960, 71314432, 21039616, 6334464, 39852416, 8400176, 4260768, 39852805, 6298560, 0, 4263941, 6334976, 0, 0, 0, 0, 4272467, 0, 44063575, 44063575, 0, 0, 0, 39889284, 6334730, 0, 0, 6298624, 0, 23070784, 0, 21039488, 23070666, 21040128, 0, 4237376, 21039360, 8482560, 0, 0, 0, 0, 0, 0, 6333769, 0, 4268032, 0, 2162128, 4260512, 44052555, 39852576, 4260480, 21039392, 6298714, 4237616, 6334594, 0, 6298499, 0, 6335424, 21039168, 21039872, 6361347, 10529792, 4264067, 0, 21039968, 0, 0, 21039936, 20973568, 39889536, 0, 0, 6334112, 6333648, 0, 21039712, 0, 0, 0, 0, 0, 0, 0, 21039520, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4264323, 4264144, 4265600, 0, 0, 0, 0, 0};

      private static final int getScriptProps(int var0) {
         return 0 <= var0 && var0 < SCRIPT_PROPS.length ? SCRIPT_PROPS[var0] : 0;
      }

      static int access$000(int var0) {
         return getScriptProps(var0);
      }
   }
}
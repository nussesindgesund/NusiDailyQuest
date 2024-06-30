package pl.nusse.nusidailyquest.util;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorFixer {

    private static final Pattern hexPattern = Pattern.compile("#[a-fA-F0-9]{6}");

    public static String fixColor(String message) {
        Matcher matcher = hexPattern.matcher(message);
        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, ChatColor.of(color) + "");
            matcher = hexPattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}

//    public static String fixColor(String string) {
//        Pattern pattern = Pattern.compile("&(#[a-fA-F0-9]{6})");
//        for (Matcher matcher = pattern.matcher(string); matcher.find(); matcher = pattern.matcher(string)) {
//            String color = string.substring(matcher.start() + 1, matcher.end());
//            string = string.replace("&" + color, String.valueOf(ChatColor.of(color)));
//        }
//        return ChatColor.translateAlternateColorCodes('&', string)
//                .replace(">>", "»")
//                .replace("<<", "«")
//                .replace("->", "→")
//                .replace("<-", "←")
//                .replace("**", "•")
//                .replace("a", "ᴀ")
//                .replace("b", "ʙ")
//                .replace("c", "ᴄ")
//                .replace("d", "ᴅ")
//                .replace("e", "ᴇ")
//                .replace("f", "ꜰ")
//                .replace("g", "ɢ")
//                .replace("h", "ʜ")
//                .replace("i", "ɪ")
//                .replace("j", "ᴊ")
//                .replace("k", "ᴋ")
//                .replace("l", "ʟ")
//                .replace("m", "ᴍ")
//                .replace("n", "ɴ")
//                .replace("o", "ᴏ")
//                .replace("p", "ᴘ")
//                .replace("q", "ǫ")
//                .replace("r", "ʀ")
//                .replace("s", "ꜱ")
//                .replace("t", "ᴛ")
//                .replace("u", "ᴜ")
//                .replace("v", "ᴠ")
//                .replace("w", "ᴡ")
//                .replace("x", "x")
//                .replace("y", "ʏ")
//                .replace("z", "ᴢ");
//    }

//    public static void sendMessage(CommandSender player, String message) {
//        player.sendMessage(fixColor(message));
//    }
//}

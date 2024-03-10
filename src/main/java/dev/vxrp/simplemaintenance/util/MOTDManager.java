package dev.vxrp.simplemaintenance.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class MOTDManager  {
    public static Component motd(String motd) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(motd.replace("%newline%", "\n"));
    }
}

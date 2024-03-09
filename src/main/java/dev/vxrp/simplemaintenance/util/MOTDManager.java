package dev.vxrp.simplemaintenance.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class MOTDManager  {
    private final String motd;

    public MOTDManager(String motd) {
        this.motd = motd;
    }
    public Component build() {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(motd.replace("%newline%", "\n"));
    }
}

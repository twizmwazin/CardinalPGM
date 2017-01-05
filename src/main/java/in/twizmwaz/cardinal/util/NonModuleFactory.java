package in.twizmwaz.cardinal.util;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.tabList.TabList;
import in.twizmwaz.cardinal.util.bossBar.BossBars;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;

public class NonModuleFactory {

    private final List<Listener> nonModules =
            Arrays.asList(
                    new TabList(),
                    new BossBars(),
                    new Fireworks()
            );

    public NonModuleFactory() {
        nonModules.forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, Cardinal.getInstance()));
    }

    public <T> T getNonModule(Class<T> clazz) {
        return nonModules.stream().filter(clazz::isInstance).map(l -> (T) l).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Could not find non-module " + clazz.getSimpleName()));
    }

}

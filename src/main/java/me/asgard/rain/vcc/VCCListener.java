package me.asgard.rain.vcc;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * me.asgard.rain.vcc.VCCListener
 * VersionConnectControl
 *
 * @author 寒雨
 * @since 2021/12/28 23:14
 **/
public class VCCListener implements Listener {

    public final List<Pair<Integer, String>> registry;

    {
        List<Pair<Integer, String>> list = new ArrayList<>();
        Configuration conf = VersionConnectionControl.conf;
        conf.getSection("version-server").getKeys().forEach(s -> {
            list.add(new Pair<>(Integer.parseInt(s), conf.getString("version-server." + s)));
        });
        registry = list.stream()
                .sorted(Comparator.comparing(Pair<Integer, String>::getFirst).reversed())
                .collect(Collectors.toList());
    }

    @EventHandler
    public void e(ServerConnectEvent event) {
        ProxiedPlayer p = event.getPlayer();
        int version = p.getPendingConnection().getVersion();
        // 初次加入
        if (event.getReason() == ServerConnectEvent.Reason.JOIN_PROXY) {
            registry.stream().filter(pair -> version >= pair.first)
                    .findFirst()
                    .ifPresent(pair -> event.setTarget(ProxyServer.getInstance().getServerInfo(pair.second)));
        } else if (event.getReason() == ServerConnectEvent.Reason.LOBBY_FALLBACK) {
            // 阻止重定向
            registry.stream().filter(pair -> version >= pair.first)
                    .findFirst()
                    .ifPresent(pair -> event.setCancelled(true));
        }
    }
}

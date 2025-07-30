package org.dslofficial;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import org.dslofficial.tasks.*;
import org.dslofficial.commands.*;
import org.dslofficial.commands.ReloadResource;
import org.dslofficial.commands.completes.*;
import org.dslofficial.commands.replaces.*;
import org.dslofficial.event.Event;
import org.dslofficial.event.UIEvent;
import org.dslofficial.util.PrintHeader;
import org.dslofficial.util.GetPlayer;
import org.dslofficial.util.SharedData;

import java.io.File;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public final class DSLPlugin extends JavaPlugin {
    public static File dataFolder;
    public static File GetFolder() {
        return dataFolder;
    }

    public static Server server;
    public static final SharedData sharedData = new SharedData();
    public static final Set<UUID> alreadyIn = new HashSet<>();
    public static final String resourcepackURL = "https://dl.dropboxusercontent.com/scl/fi/6hyva2q0gh807qthc1qp9/DSLResource.zip?rlkey=kwarip9ky2byj7d9tb3xkle4y&st=bykv6xhh&dl=0";

    public static volatile byte[] resourcepackHash;

    @Override
    public void onEnable() {
        server = getServer();
        dataFolder = getDataFolder();

        for (Player p : server.getOnlinePlayers()) {
            if (GetPlayer.run(p.getName()).get("id") == null) {
                p.kickPlayer(PrintHeader.header("exitmsg/kick", "서버에 아직 가입되지 않으셨습니다. 가입을 먼저 한 후, 다시 시도해 주세요.\n(아이디가 바뀌였다면 관리자에게 문의 바랍니다)"));
            } else alreadyIn.add(p.getUniqueId());
        }

        getServer().broadcastMessage(PrintHeader.header("info", "플러그인이 활성화되었습니다."));
        System.out.println(
            """
            ***********************************************************
            DSL플러그인이 활성화되었습니다.
            DSLPlugin has been enabled.

            이 프로그램은 DSL-OFFICIAL 마인크래프트 서버 전용으로 개발된 플러그인으로,
            다른 서버에서의 적용 및 플레이는 금지되어 있습니다.
            Copyright 2023-2024. Dwk0910 All rights reserved.
            ***********************************************************
            """
        );

        // Event
        getServer().getPluginManager().registerEvents(new Event(), this);
        getServer().getPluginManager().registerEvents(new UIEvent(), this);

        // Tasks
        new AutoItemClear(this).runTaskTimer(this, 0L, 12000L);
        new AutoReload().runTaskTimer(this, 0L, 120L);

        // Command

        //서버관리자 전용 명령어
        Objects.requireNonNull(getCommand("permission")).setExecutor(new Permission());
        Objects.requireNonNull(getCommand("permission")).setTabCompleter(new PermissionComplete());

        // 관리자 전용 명령어
        Objects.requireNonNull(getCommand("getitem")).setExecutor(new GiveItem());
        Objects.requireNonNull(getCommand("getitem")).setTabCompleter(new GiveItemComplete());

        Objects.requireNonNull(getCommand("point")).setTabCompleter(new PointComplete());
        Objects.requireNonNull(getCommand("point")).setExecutor(new Point());

        Objects.requireNonNull(getCommand("kill")).setTabCompleter(new KillComplete());
        Objects.requireNonNull(getCommand("kill")).setExecutor(new Kill());

        // Console 전용 명령어
        Objects.requireNonNull(getCommand("mm")).setExecutor(new MoneyManagement());
        Objects.requireNonNull(getCommand("stop")).setExecutor(new Stop());
        sharedData.setBoolean("isrunning_Stop", false);
        Objects.requireNonNull(getCommand("addplayer")).setExecutor(new AddPlayer());
        Objects.requireNonNull(getCommand("reloadresource")).setExecutor(new ReloadResource());

        // Client 전용 명령어
        Objects.requireNonNull(getCommand("menu")).setExecutor(new Menu());

        // 공개 명령어
        Objects.requireNonNull(getCommand("money")).setExecutor(new Money());
        Objects.requireNonNull(getCommand("money")).setTabCompleter(new MoneyComplete());

        Objects.requireNonNull(getCommand("clearitem")).setExecutor(new ClearItem());
        sharedData.setBoolean("isrunning_ClearItem", false);

        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(this.getClass()), org.dslofficial.tasks.ReloadResource::refreshResourceHash);
    }

    @Override
    public void onDisable() {
        getServer().broadcastMessage(PrintHeader.header("warning", "플러그인이 비활성화 되었습니다. 동작을 멈춰주시기 바랍니다."));
        System.out.println(
                """
                ***********************************************************
                DSL플러그인이 비활성화되었습니다.
                DSLPlugin has been disabled.
    
                이 프로그램은 DSL-OFFICIAL 마인크래프트 서버 전용으로 개발된 플러그인으로,
                다른 서버에서의 적용 및 플레이는 금지되어 있습니다.
                Copyright 2023-2024. Dwk0910 All rights reserved.
                ***********************************************************
                """
        );
    }
}
package org.dslofficial.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import org.dslofficial.DSLPlugin;
import org.dslofficial.util.PrintHeader;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.URL;
import java.net.URI;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.security.MessageDigest;

import java.util.Objects;

@SuppressWarnings("CallToPrintStackTrace")
public class ReloadResource {
    public static void run() {
        Bukkit.getScheduler().runTaskAsynchronously(DSLPlugin.getPlugin(DSLPlugin.class), () -> {
            DSLPlugin.server.broadcastMessage(PrintHeader.header("info", "리소스팩 적용이 호출되었습니다."));
            DSLPlugin.server.broadcastMessage(PrintHeader.header("info", "리소스 다운로드 중입니다..."));
            refreshResourceHash();
            DSLPlugin.server.broadcastMessage(PrintHeader.header("info", "준비 완료."));
            try {
                for (int i = 3; i >= 1; i--) {
                    DSLPlugin.server.broadcastMessage(PrintHeader.header("warning", ChatColor.RED + "" + ChatColor.BOLD + "%d초 후 새로운 리소스팩을 적용합니다.".formatted(i)));
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            DSLPlugin.server.broadcastMessage(PrintHeader.header("info", "적용 중입니다..."));
            DSLPlugin.alreadyIn.forEach((k) -> {
                Player player = Objects.requireNonNull(DSLPlugin.server.getPlayer(k));
                player.setResourcePack(DSLPlugin.resourcepackURL, DSLPlugin.resourcepackHash, true);
            });
        });
    }

    public static void refreshResourceHash() {
        try {
            URL url = new URI(DSLPlugin.resourcepackURL).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();

            String path = DSLPlugin.dataFolder + File.separator + "resourcepack.zip";
            try (OutputStream out = new FileOutputStream(path)) {
                int bufferSize;
                byte[] bytes = new byte[16384];
                while ((bufferSize = is.read(bytes)) != -1) {
                    out.write(bytes, 0, bufferSize);
                }
            }

            DSLPlugin.resourcepackHash = calculateSHA1(Paths.get(path));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private static byte[] calculateSHA1(Path filePath) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            try (var fis = Files.newInputStream(filePath)) {
                byte[] buffer = new byte[8192];
                int read;
                while ((read = fis.read(buffer)) != -1) {
                    digest.update(buffer, 0, read);
                }
            }
            return digest.digest();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

}

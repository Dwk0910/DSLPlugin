package org.dslofficial.commands.completes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.dslofficial.DSLPlugin;
import org.dslofficial.util.PrintHeader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class PointComplete implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        switch (args.length) {
            case 1 -> {
                ArrayList<String> result = new ArrayList<>();
                result.add("set");
                result.add("remove");
                result.add("move");
                result.add("list");

                return result;
            }

            case 2 -> {
                ArrayList<String> result = new ArrayList<>();
                if (args[0].equalsIgnoreCase("set")) {
                    result.add("[<포인트 이름>]");
                    return result;
                } else if (args[0].equalsIgnoreCase("list")) {
                    return result;
                }

                try {
                    JSONParser parser = new JSONParser();

                    File f = new File(DSLPlugin.GetFolder() + File.separator + "point.dat");
                    FileReader reader = new FileReader(f);
                    JSONObject data = (JSONObject) parser.parse(reader);

                    for (Object key : data.keySet()) {
                        if (key.toString().contains(args[1])) result.add(key.toString());
                    }
                } catch (FileNotFoundException e) {
                    sender.sendMessage(PrintHeader.header("error", "point.dat가 없습니다."));
                    result.add("");
                    return result;
                } catch (ParseException e) {
                    sender.sendMessage(PrintHeader.header("error", "point.dat가 손상되었습니다."));
                    result.add("");
                    return result;
                } catch (IOException e) {
                    sender.sendMessage(PrintHeader.header("error", "point.dat를 읽을 수 없습니다."));
                    result.add("");
                    return result;
                }

                return result;
            }

            default -> {
                ArrayList<String> result = new ArrayList<>();
                result.add("");
                return result;
            }
        }
    }
}

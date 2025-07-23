package org.dslofficial.util;

import org.dslofficial.DSLPlugin;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"CallToPrintStackTrace", "unchecked"})
public class SetPlayer {
    public static void edit(String name, String targetKey, String value) {
        JSONParser parser = new JSONParser();

        if (findKey(targetKey) & /* 기존 플레이어용 -> */ !targetKey.equals("permission")) {
            throw new Error("key '" + targetKey + "' is invalid.");
        }

        try {
            File f = new File(DSLPlugin.dataFolder + File.separator + "playerlist.dat");
            FileReader reader = new FileReader(f);
            JSONObject obj_org = (JSONObject) parser.parse(reader);
            JSONObject obj = (JSONObject) obj_org.get(name);

            // 기존 플레이어용
            if (targetKey.equals("permission")) {
                //noinspection unchecked
                obj.put("permission", value);
                //noinspection unchecked
                obj_org.replace(name, obj);

                FileWriter writer = new FileWriter(f);
                writer.write(obj_org.toJSONString());
                writer.flush();
                writer.close();
                return;
            }

            //noinspection unchecked
            obj.replace(targetKey, value);
            //noinspection unchecked
            obj_org.replace(name, obj);

            FileWriter writer = new FileWriter(f);
            writer.write(obj_org.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
   }

   public static void add(String name, String defaultRole) {
        if (defaultRole.equals("member") | defaultRole.equals("manager") | defaultRole.equals("v.leader") | defaultRole.equals("leader")) {
            Map<String, String> valueMap = new HashMap<>();
            valueMap.put("id", name);
            valueMap.put("pwd", "abcd1234");
            valueMap.put("role", defaultRole);
            valueMap.put("permission", "[]");
            valueMap.put("money", "0");

            JSONParser parser = new JSONParser();

            try {
                File f = new File(DSLPlugin.dataFolder + File.separator + "playerlist.dat");
                FileReader reader = new FileReader(f);
                JSONObject obj = (JSONObject) parser.parse(reader);

                // reg
                Map<String, Object> newMap = new HashMap<>();
                for (Object o : obj.keySet()) {
                    String i = (String) o;
                    newMap.put(i, obj.get(i));
                }

                newMap.put(name, valueMap);
                JSONObject newObj = new JSONObject(newMap);

                FileWriter writer = new FileWriter(f);
                writer.write(newObj.toJSONString());
                writer.flush();
                writer.close();
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        } else {
            throw new Error("role '" + defaultRole + "' isn't valid role name.");
        }
   }

    private static boolean findKey(String key) {
        ArrayList<String> keys = new ArrayList<>();
        keys.add("id");
        keys.add("pwd");
        keys.add("role");
        keys.add("money");
        for (String i : keys) {
            if (i.equals(key)) return false;
        }
        return true;
    }
}

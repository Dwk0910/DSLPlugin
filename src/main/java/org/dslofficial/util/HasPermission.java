package org.dslofficial.util;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HasPermission {
    public static boolean check(String playerName, String permission) {
        // 기존 플레이어용
        if (GetPlayer.run(playerName).get("permission") == null) {
            SetPlayer.edit(playerName, "permission", "[]");
        }

        try {
            JSONParser parser = new JSONParser();
            JSONArray permissionList = (JSONArray) parser.parse(GetPlayer.run(playerName).get("permission").toString());
            for (Object o : permissionList) {
                String perm = (String) o;
                if (perm.equals(permission)) return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }
}
package org.dslofficial.util;

import org.dslofficial.DSLPlugin;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class EditPermission {
    public List<String> getPermission(String name) {
        try {
            File f = new File(DSLPlugin.dataFolder + File.separator + "permission.dat");
            FileReader reader = new FileReader(f);
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(reader);
            JSONArray permissions = (JSONArray) data.get(name);

            if (data.get(name) == null) {
                throw new Error("player '" + name + "' is not registered.");
            }

            List<String> result = new ArrayList<>();

            for (Object o : permissions) {
                String permission = (String) o;
                result.add(permission);
            }

            return result;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void addPermission(String name, String permissionType) {

    }
}

package org.dslofficial.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.dslofficial.DSLPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GetPlayer {
    public static JSONObject run(String name) {
        JSONParser parser = new JSONParser();
        try {
            File f = new File(DSLPlugin.dataFolder + File.separator + "playerlist.dat");
            FileReader reader = new FileReader(f);
            JSONObject playerObject = (JSONObject) parser.parse(reader);
            JSONObject result = (JSONObject) playerObject.get(name);

            if (result == null) return new JSONObject();

            return result;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            DSLPlugin.server.broadcastMessage(PrintHeader.header("error", "playerlist.dat파일이 손상되었습니다!!"));
            throw new Error("File 'playerlist.dat' has been damaeged.");
        }
    }
}
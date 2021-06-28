package si.f5.pa_union;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class JsonObject {

    String value;

    public JsonObject(String[] strings) {
        StringBuilder builder = new StringBuilder();
        for(String str: strings) {
            builder.append(str);
        }
        value = builder.toString();
    }

    public JsonObject(String string) {
        value = string;
    }

    public JsonObject(List<String> strings) {
        StringBuilder builder = new StringBuilder();
        for(String str: strings) {
            builder.append(str);
        }
        value = builder.toString();
    }

    public JsonObject(InputStreamReader InputReader) throws IOException {
        BufferedReader reader = new BufferedReader(InputReader);
        String str;
        StringBuilder builder = new StringBuilder();
        while ((str = reader.readLine()) != null) {
            builder.append(str);
        }
        value = builder.toString();
    }

    public JsonObject get(String str) {
        if(value.contains(str)) {
            String tempString = value.split("\"" + str + "\"", 2)[1];
            int tempInt = 0;

            StringBuilder builder = new StringBuilder();
            for(char chr: tempString.toCharArray()) {
                if(tempInt == 0) {
                    if (chr == '}' || chr == ']' || chr == ',')
                        return new JsonObject(builder.toString().replaceFirst(":", ""));
                }
                if (chr == '}' || chr == ']') {
                    tempInt--;
                }
                if (chr == '{' || chr == '[') {
                    tempInt++;
                }
                builder.append(chr);
            }
        }
        return null;
    }
}

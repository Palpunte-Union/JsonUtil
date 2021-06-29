package si.f5.pa_union;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author eight_y_88
 */
public class JsonObject {

    String value;

    HashMap<String, String> hashMap = null;
    List<String> arrayList = null;

    public JsonObject(String[] strings) {
        StringBuilder builder = new StringBuilder();
        for(String str: strings) {
            builder.append(str);
        }
        init(builder.toString());
    }

    public JsonObject(String string) {
        init(string);
    }

    public JsonObject(List<String> strings) {
        StringBuilder builder = new StringBuilder();
        for(String str: strings) {
            builder.append(str);
        }
        init(builder.toString());
    }

    public JsonObject(InputStreamReader InputReader) throws IOException {
        BufferedReader reader = new BufferedReader(InputReader);
        String str;
        StringBuilder builder = new StringBuilder();
        while ((str = reader.readLine()) != null) {
            builder.append(str);
        }
        init(builder.toString());
    }

    public JsonObject get(String str) {
        return new JsonObject(hashMap.get(str));
    }

    public String asString() {
        return value.replace("\\\"", "${__double_q}").replace("\"", "").replace("${__double_q}", "\"");
    }

    public int asInt() {
        return Integer.parseInt(value);
    }

    public List<JsonObject> asArray() {
        List<JsonObject> result = new ArrayList<>();
        for(String str: arrayList) result.add(new JsonObject(str));
        return result;
    }

    @Override
    public String toString() {
        if (arrayList != null) {
            StringBuilder builder = new StringBuilder("[\n");
            for (String str : arrayList) {
                builder.append("\t")
                        .append(new JsonObject(str)).append("\n");
            }
            builder.append("]");
            return builder.toString();
        } else if (hashMap != null) {
            int TempInt = 1;
            StringBuilder builder = new StringBuilder("{\n");
            for(String key: hashMap.keySet()) {
                builder.append("\t").append(key).append(": ").append(new JsonObject(hashMap.get(key)).toString().replace("\n", "\n\t")).append(",").append("\n");
            }
            builder.append("}");
            return builder.toString().replaceFirst(",\n}$", "\n}");
        } else if (value != null) {
            return value;
        }
        return null;
    }

    private void init(String v) {
        String temp = v.replace("\\\"", "${__double_q}").replaceAll("\\s+","");
        temp = EraseSpace(temp);
        boolean list = false;

        if(temp.startsWith("[")) {
            list = true;
            arrayList = new ArrayList<>();
        } else if (temp.startsWith("{")){
            hashMap = new HashMap<>();
        } else {
            value = temp;
            return;
        }

        StringBuilder builder = new StringBuilder();
        int tempInt = 0;
        boolean b = false;
        for(char chr: temp.toCharArray()) {
            if(chr == '"') b = !b;
            if(tempInt == 1) {
                if (chr == '}' || chr == ']' || chr == ',') {
                    if (list) {
                        arrayList.add(builder.toString());
                    } else {
                        hashMap.put(builder.toString().split(":", 2)[0], builder.toString().split(":", 2)[1]);
                    }
                    builder = new StringBuilder();
                    continue;
                }
            }
            if (chr == '}' || chr == ']') tempInt--;
            if (chr == '{' || chr == '[') tempInt++;
            if(!b) builder.append(chr);
        }
    }

    private String EraseSpace(String v) {
        StringBuilder builder = new StringBuilder();
        boolean b = false;
        for(char chr: v.toCharArray()) {
            if(chr == '"') b = !b;
            if(chr != ' ' && b) builder.append(chr);
        }
        return builder.toString();
    }
}

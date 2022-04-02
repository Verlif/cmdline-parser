package idea.verlif.parser.cmdline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/4/2 14:42
 */
public class ArgValues implements Iterable<String> {

    private final List<String> keys;
    private final List<String> values;

    public ArgValues() {
        keys = new ArrayList<>();
        values = new ArrayList<>();
    }

    @Override
    public Iterator<String> iterator() {
        return keys.listIterator();
    }

    public void add(String key, String value) {
        keys.add(key);
        values.add(value);
    }

    public void remove(String key) {
        int i = keys.indexOf(key);
        if (i > -1) {
            keys.remove(i);
            values.remove(i);
        }
    }

    public String get(String key) {
        int i = keys.indexOf(key);
        if (i > -1) {
            return values.get(i);
        } else {
            return null;
        }
    }

    public String get(int index) {
        return values.get(index);
    }

    public int size() {
        return keys.size();
    }

    @Override
    public String toString() {
        if (keys.size() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("ArgValues[");
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                sb.append("{\"key\":\"").append(key).append("\",\"value\":");
                String value = get(i);
                if (value == null) {
                    sb.append("null");
                } else {
                    sb.append("\"").append(value).append("\"");
                }
                sb.append("},");
            }
            sb.setLength(sb.length() - 1);
            sb.append("]");
            return sb.toString();
        } else {
            return "ArgValues[]";
        }
    }
}

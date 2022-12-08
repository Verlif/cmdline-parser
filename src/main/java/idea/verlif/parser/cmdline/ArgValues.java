package idea.verlif.parser.cmdline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Verlif
 * @version 1.0
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

    public synchronized void add(String key, String value) {
        keys.add(key);
        values.add(value);
    }

    public synchronized void remove(String key) {
        int i = keys.indexOf(key);
        if (i > -1) {
            keys.remove(i);
            values.remove(i);
        }
    }

    public synchronized void remove(int index) {
        keys.remove(index);
        values.remove(index);
    }

    public String getKey(int index) {
        return keys.get(index);
    }

    public String getValue(String key) {
        int i = keys.indexOf(key);
        if (i > -1) {
            return values.get(i);
        } else {
            return null;
        }
    }

    public String getValue(int index) {
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
                String value = getValue(i);
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

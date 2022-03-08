package idea.verlif.parser.cmdline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/3/8 14:28
 */
public class CmdlineParser {

    /**
     * 指令前缀
     */
    private final String prefix;

    /**
     * 指令Map
     */
    private final Map<String, CmdHandler> handlerMap;

    /**
     * 忽略未知指令
     */
    private boolean ignoredUnknownKey = false;

    public CmdlineParser(String prefix) {
        this.prefix = prefix;
        this.handlerMap = new HashMap<>();
    }

    public void addHandler(String key, CmdHandler handler) {
        handlerMap.put(key, handler);
    }

    public void removeKey(String key) {
        handlerMap.remove(key);
    }

    /**
     * 忽略未知命令；否则当遇到未知命令时，会抛出{@linkplain UnknownCmdKeyException}异常
     */
    public void ignoredUnknownKey() {
        ignoredUnknownKey = true;
    }

    public void exec(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String key = getKey(args[i]);
            if (key != null) {
                CmdHandler handler = handlerMap.get(key);
                if (handler != null) {
                    int next = i + 1;
                    if (next == args.length) {
                        handler.handle(null);
                    } else {
                        if (getKey(args[next]) == null) {
                            handler.handle(args[next]);
                            i++;
                        } else {
                            handler.handle(null);
                        }
                    }
                } else if (!ignoredUnknownKey) {
                    throw new UnknownCmdKeyException(key);
                }
            }
        }
    }

    private String getKey(String arg) {
        if (arg.startsWith(prefix)) {
            String key = arg.substring(prefix.length());
            if (key.length() > 0) {
                return key;
            }
        }
        return null;
    }

    public void exec(String cmdline) {
        ArrayList<String> list = new ArrayList<>();
        boolean isOneParam = false;
        StringBuilder sb = new StringBuilder();
        for (char c : cmdline.toCharArray()) {
            if (isOneParam) {
                if (c == '\"') {
                    isOneParam = false;
                    list.add(sb.toString());
                    sb.delete(0, sb.length());
                } else {
                    sb.append(c);
                }
            } else {
                if (c == '\"') {
                    isOneParam = true;
                } else if (c == ' ') {
                    if (sb.length() > 0) {
                        list.add(sb.toString());
                        sb.delete(0, sb.length());
                    }
                } else {
                    sb.append(c);
                }
            }
        }
        if (sb.length() > 0) {
            list.add(sb.toString());
            sb.delete(0, sb.length());
        }

        exec(list.toArray(new String[]{}));
    }
}

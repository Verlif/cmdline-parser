package idea.verlif.parser.cmdline;

import java.util.*;

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
    private boolean ignoreUnknownKey = false;

    /**
     * 忽略大小写
     */
    private boolean ignoreCase = false;

    public CmdlineParser(String prefix) {
        this.prefix = prefix;
        this.handlerMap = new HashMap<>();
    }

    public void addHandler(String key, CmdHandler handler) {
        key = transKey(key);
        handlerMap.put(key, handler);
    }

    public void removeKey(String key) {
        key = transKey(key);
        handlerMap.remove(key);
    }

    /**
     * 忽略未知命令；否则当遇到未知命令时，会抛出{@linkplain UnknownCmdKeyException}异常
     */
    public void ignoreUnknownKey() {
        ignoreUnknownKey = true;
    }

    /**
     * 忽略关键词大小写
     */
    public void ignoreCase() {
        ignoreCase = true;
        Set<String> keys = new HashSet<>(handlerMap.keySet());
        for (String s : keys) {
            CmdHandler handler = handlerMap.get(s);
            if (handler != null) {
                handlerMap.remove(s);
                handlerMap.put(transKey(s), handler);
            }
        }
    }

    /**
     * 获取所有的指令关键词
     *
     * @return 指令关键词集
     */
    public Set<String> getCmdKeys() {
        return handlerMap.keySet();
    }

    public void exec(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String key = getKeyFromArg(args[i]);
            if (key != null) {
                key = transKey(key);
                CmdHandler handler = handlerMap.get(key);
                if (handler != null) {
                    int next = i + 1;
                    if (next == args.length) {
                        handler.handle(null);
                    } else {
                        if (getKeyFromArg(args[next]) == null) {
                            handler.handle(args[next]);
                            i++;
                        } else {
                            handler.handle(null);
                        }
                    }
                } else if (!ignoreUnknownKey) {
                    throw new UnknownCmdKeyException(key);
                }
            }
        }
    }

    private String getKeyFromArg(String arg) {
        if (arg.startsWith(prefix)) {
            String key = arg.substring(prefix.length());
            if (key.length() > 0) {
                return key;
            }
        }
        return null;
    }

    public void exec(String cmdline) {
        exec(lineToArray(cmdline));
    }

    /**
     * 将一行数据转换成指令参数数组
     *
     * @param line 行字符串
     * @return 指令参数数组
     */
    public String[] lineToArray(String line) {
        ArrayList<String> list = new ArrayList<>();
        boolean isOneParam = false;
        StringBuilder sb = new StringBuilder();
        for (char c : line.toCharArray()) {
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
        return list.toArray(new String[]{});
    }

    private String transKey(String key) {
        if (ignoreCase) {
            return key.toLowerCase(Locale.ROOT);
        }
        return key;
    }
}

package idea.verlif.parser.cmdline;

import idea.verlif.parser.cmdline.exception.NoArgParserException;
import idea.verlif.parser.cmdline.exception.UnknownCmdKeyException;

import java.util.*;

/**
 * @author Verlif
 * @version 1.0
 */
public class CmdlineParser {

    /**
     * 指令Map
     */
    private final Map<String, CmdHandler> handlerMap;

    private ArgParser argParser;

    /**
     * 忽略未知指令
     */
    private boolean ignoreUnknownKey = false;

    /**
     * 忽略大小写
     */
    private boolean ignoreCase = false;

    public CmdlineParser() {
        this.handlerMap = new HashMap<>();
    }

    public void setArgParser(ArgParser argParser) {
        this.argParser = argParser;
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

    /**
     * 执行指令<br>
     * 由于Map的遍历顺序无法控制，所以如果需要控制指令执行顺序的话，建议使用{@link #exec(ArgValues)}
     *
     * @param paramMap 指令参数集
     */
    public void exec(Map<String, String> paramMap) {
        ArgValues argValues = new ArgValues();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            argValues.add(entry.getKey(), entry.getValue());
        }
        exec(argValues);
    }

    /**
     * 通过参数解析器解析后执行指令行
     *
     * @param cmdline 指令行
     */
    public void exec(String cmdline) {
        // 检测参数解析器
        if (argParser == null) {
            throw new NoArgParserException();
        }
        // 检测指令行格式
        if (cmdline == null) {
            return;
        }
        cmdline = cmdline.trim();
        if (cmdline.isEmpty()) {
            return;
        }
        ArgValues argValues = argParser.parseLine(cmdline.trim());
        exec(argValues);
    }

    /**
     * 执行参数值对象
     *
     * @param argValues 参数值对象
     */
    public void exec(ArgValues argValues) {
        // 检查指令集
        if (!ignoreUnknownKey) {
            for (String key : argValues) {
                if (!handlerMap.containsKey(key)) {
                    throw new UnknownCmdKeyException(key);
                }
            }
        }
        for (int i = 0, size = argValues.size(); i < size; i++) {
            String key = argValues.getKey(i);
            if (key != null) {
                key = transKey(key);
                CmdHandler handler = handlerMap.get(key);
                if (handler != null) {
                    handler.handle(argValues.getValue(i));
                }
            }
        }
    }

    private String transKey(String key) {
        if (ignoreCase) {
            return key.toLowerCase(Locale.ROOT);
        }
        return key;
    }

}

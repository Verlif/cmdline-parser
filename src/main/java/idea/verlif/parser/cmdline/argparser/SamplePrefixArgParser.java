package idea.verlif.parser.cmdline.argparser;

import idea.verlif.parser.cmdline.ArgParser;
import idea.verlif.parser.cmdline.ArgValues;

import java.util.ArrayList;

/**
 * 简单前缀指令解析器
 *
 * @author Verlif
 */
public class SamplePrefixArgParser implements ArgParser {

    /**
     * 指令前缀
     */
    protected final String prefix;

    public SamplePrefixArgParser(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public ArgValues parseLine(String line) {
        return parser(lineToArray(line, ' '));
    }

    /**
     * 解析指令行
     *
     * @param args 指令参数集
     * @return 指令参数
     */
    public ArgValues parser(String[] args) {
        ArgValues argValues = new ArgValues();
        for (int i = 0; i < args.length; i++) {
            String key = getKeyFromArg(args[i]);
            if (key == null) {
                continue;
            }
            int next = i + 1;
            if (next == args.length) {
                argValues.add(key, null);
            } else {
                if (getKeyFromArg(args[next]) == null) {
                    argValues.add(key, args[next]);
                    i++;
                } else {
                    argValues.add(key, null);
                }
            }
        }
        return argValues;
    }

    protected String getKeyFromArg(String arg) {
        if (arg.startsWith(prefix)) {
            String key = arg.substring(prefix.length());
            if (key.length() > 0) {
                return key;
            }
        }
        return null;
    }
}

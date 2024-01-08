package idea.verlif.parser.cmdline.argparser;

import idea.verlif.parser.cmdline.ArgValues;

/**
 * 带配置的前缀参数解析器。<br>
 * 例如：-sgSb --username Verlif --password 12345..<br>
 * 这里有参数"s", "g", "S"，"b", "username", "password"，其中前四个是无值参数，后两个参数值分别为"Verlif"和"12345.."
 *
 * @author Verlif
 */
public class PrefixWithConfigArgParser extends SamplePrefixArgParser {

    /**
     * 配置参数前缀
     */
    private String configPrefix = "-";

    public PrefixWithConfigArgParser(String prefix) {
        super(prefix);
    }

    public void setConfigPrefix(String configPrefix) {
        this.configPrefix = configPrefix;
    }

    @Override
    public ArgValues parseLine(String line) {
        if (line.startsWith(prefix)) {
            return super.parseLine(line);
        }
        ArgValues argValues = new ArgValues();
        if (line.startsWith(configPrefix)) {
            char[] chars = line.toCharArray();
            for (int i = configPrefix.length(); i < chars.length; i++) {
                char c = chars[i];
                if (c == ' ') {
                    argValues.append(super.parseLine(line.substring(i)));
                    break;
                }
                argValues.add(String.valueOf(c), null);
            }
        }
        return argValues;
    }
}

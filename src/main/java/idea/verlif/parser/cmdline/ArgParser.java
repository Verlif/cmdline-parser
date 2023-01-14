package idea.verlif.parser.cmdline;

/**
 * @author Verlif
 */
public interface ArgParser {

    /**
     * 解析指令行参数
     *
     * @param line 指令行字符串
     * @return 参数值
     */
    ArgValues parseLine(String line);
}

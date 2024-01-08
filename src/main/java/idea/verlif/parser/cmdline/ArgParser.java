package idea.verlif.parser.cmdline;

import java.util.ArrayList;

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

    /**
     * 将一行数据转换成指令参数数组
     *
     * @param line 行字符串
     * @return 指令参数数组
     */
    default String[] lineToArray(String line, char split) {
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
                } else if (c == split) {
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
}

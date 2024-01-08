package idea.verlif.parser.cmdline.argparser;

import idea.verlif.parser.cmdline.ArgParser;
import idea.verlif.parser.cmdline.ArgValues;

/**
 * 冒号参数解析器，解析类似于“abc:123 def:"123 321"”的指令行。<br/>
 */
public class ColonArgParser implements ArgParser {

    private char colon = ':';

    public ColonArgParser() {
    }

    public ColonArgParser(char colon) {
        this.colon = colon;
    }

    public void setColon(char colon) {
        this.colon = colon;
    }

    @Override
    public ArgValues parseLine(String line) {
        ArgValues argValues = new ArgValues();
        String[] array = lineToArray(line, ' ');
        for (String arg : array) {
            int i = arg.indexOf(colon);
            if (i > -1) {
                argValues.add(arg.substring(0, i), arg.substring(i + 1));
            }
        }
        return argValues;
    }

}

package idea.verlif.parser.cmdline;

import idea.verlif.parser.cmdline.argparser.ColonArgParser;
import idea.verlif.parser.cmdline.argparser.HtmlUrlArgParser;
import idea.verlif.parser.cmdline.argparser.PrefixWithConfigArgParser;
import idea.verlif.parser.cmdline.argparser.SamplePrefixArgParser;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Verlif
 */
public class Main {

    public static void main(String[] args) {
        String line = "-abc --username Verlif --commit \"hello world!\" --allowed";
        CmdlineParser parser = new CmdlineParser();
        // 忽略未知命令，否则会抛出UnknownCmdKeyException异常
        parser.ignoreUnknownKey();
        // 忽略关键词大小写，可以让"--KEy"也能匹配到"key"
        parser.ignoreCase();
        // 添加指令执行器
        parser.setArgParser(new PrefixWithConfigArgParser("--"));
        parser.addHandler("username", param -> {
            System.out.println("解析到username - "  + param);
        });
        parser.addHandler("allowed", param -> {
            System.out.println("解析到allowed - "  + param);
        });
        parser.addHandler("a", param -> {
            System.out.println("解析到a - " + param);
        });
        parser.addHandler("b", param -> {
            System.out.println("解析到 - " + param);
        });
        parser.addHandler("c", param -> {
            System.out.println("解析到 - " + param);
        });
        // 执行指令
        parser.exec(line);
        // 上方指令结果与下方相同
        ArgValues argValues = new ArgValues();
        argValues.add("a", null);
        argValues.add("b", null);
        argValues.add("c", null);
        argValues.add("username", "Verlif");
        argValues.add("commit", "hello world!");
        argValues.add("allowed", null);
        parser.exec(argValues);
        // 甚至相当于这样
        HtmlUrlArgParser htmlUrlArgParser = new HtmlUrlArgParser();
        parser.setArgParser(htmlUrlArgParser);
        parser.exec("127.0.0.1:81/queue/queue?a&b&c&username=Verlif&commit=hello world&allowed");
    }

    @Test
    public void samplePrefixArgParser() {
        SamplePrefixArgParser argParser = new SamplePrefixArgParser("--");
        System.out.println(argParser.parseLine("-abcd --name Verlif --password \"pass word\""));
    }

    @Test
    public void prefixWithConfigArgParser() {
        PrefixWithConfigArgParser argParser = new PrefixWithConfigArgParser("--");
        System.out.println(argParser.parseLine("-abcd --name Verlif --password \"pass word\""));
    }

    @Test
    public void colonArgParser() {
        ColonArgParser colonArgParser = new ColonArgParser('-');
        System.out.println(colonArgParser.parseLine("abc-123 def-\"123 321\""));
    }

    @Test
    public void HtmlUrlArgParser() {
        HtmlUrlArgParser htmlUrlArgParser = new HtmlUrlArgParser();
        System.out.println(htmlUrlArgParser.parseLine("https://www.baidu.com/s?ie=UTF-8&wd=%E5%86%92%E5%8F%B7%20%E8%8B%B1%E6%96%87"));
    }
}

package idea.verlif.parser.cmdline;

/**
 * 指令执行器
 *
 * @author Verlif
 * @version 1.0
 * @date 2022/3/8 14:29
 */
public interface CmdHandler {

    /**
     * 执行器执行核心
     *
     * @param param 参数；可能为null
     */
    void handle(String param);
}

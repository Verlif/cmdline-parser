package idea.verlif.parser.cmdline.exception;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/3/8 14:49
 */
public class UnknownCmdKeyException extends RuntimeException {

    public UnknownCmdKeyException(String key) {
        super("Unknown cmd key: " + key);
    }
}

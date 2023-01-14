package idea.verlif.parser.cmdline.exception;

/**
 * @author Verlif
 */
public class NoArgParserException extends RuntimeException {

    public NoArgParserException() {
        super("Not set the ArgParser!");
    }
}

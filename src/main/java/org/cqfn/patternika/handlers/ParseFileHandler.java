package org.cqfn.patternika.handlers;

import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.parser.ParserException;
import org.cqfn.patternika.parser.Parsers;
import org.cqfn.patternika.util.cmdline.CmdLineException;
import org.cqfn.patternika.util.cmdline.Handler;
import org.cqfn.patternika.util.cmdline.HandlerException;
import org.cqfn.patternika.visualizer.ImageVisualizer;
import org.cqfn.patternika.visualizer.Visualizer;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Handler for the "parse-file" command-line action.
 *
 * @since 2021/03/05
 */
public class ParseFileHandler implements Handler {
    /** Parsers. */
    private final Parsers parsers;

    /**
     * Constructor.
     *
     * @param parsers the parsers to be used to parse code.
     */
    public ParseFileHandler(final Parsers parsers) {
        this.parsers = Objects.requireNonNull(parsers);
    }

    /**
     * Handles a command-line action with specified arguments and options.
     *
     * @param arguments the argument by names.
     * @param options the options by names (one option can have multiple values).
     * @throws CmdLineException if arguments or options are invalid.
     * @throws HandlerException if the handler failed due to some error.
     */
    @Override
    public void handle(
            final Map<String, String> arguments,
            final Map<String, List<String>> options) throws CmdLineException, HandlerException {
        final String path = arguments.get("file");
        if (!new File(path).exists()) {
            throw new CmdLineException("File does not exist: " + path);
        }
        final String dotPath = getOption("dot", options);
        final Node root = parseFile(path);
        final String imagePath = path + ".png";
        visualize(root, dotPath, imagePath);
    }

    private void visualize(
            final Node root,
            final String dotPath,
            final String imagePath) throws HandlerException {
        final Visualizer visualizer =
                new ImageVisualizer(dotPath, imagePath, root, Collections.emptyMap());
        try {
            visualizer.visualize();
        } catch (final IOException ex) {
            throw new HandlerException("Failed to create image file " + imagePath, ex);
        }
    }

    private Node parseFile(final String path) throws HandlerException {
        try {
            return parsers.parseFile(path);
        } catch (final ParserException ex) {
            throw new HandlerException("Failed to parse file " + path, ex);
        }
    }

    private String getOption(
            final String name,
            final Map<String, List<String>> options) throws CmdLineException {
        final List<String> value = options.get(name);
        if (value == null) {
            throw new CmdLineException(
                    String.format("The %s option is not specified!", name));
        }
        return value.get(0);
    }

}

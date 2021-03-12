package org.cqfn.patternika.handlers;

import org.cqfn.patternika.ast.Node;
import org.cqfn.patternika.parser.ParserException;
import org.cqfn.patternika.parser.Parsers;
import org.cqfn.patternika.util.cmdline.CmdLine;
import org.cqfn.patternika.util.cmdline.CmdLineException;
import org.cqfn.patternika.util.cmdline.CmdLineHandler;
import org.cqfn.patternika.util.cmdline.HandlerException;
import org.cqfn.patternika.visualizer.ImageVisualizer;
import org.cqfn.patternika.visualizer.Visualizer;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

/**
 * Handler for the "parse" command-line action.
 * <p>
 * The action parses source code files (single file or entire folder)
 * and optionally dumps the constructed ASTs to images or JSON-files.
 *
 * @since 2021/03/05
 */
public class ParseHandler implements CmdLineHandler {
    /** Parsers. */
    private final Parsers parsers;

    /** Structure for parse options. */
    private static class ParseOptions {
        /** Path to an DOT visualizer tool. */
        private String dot;

        /** Flag that specifies that the AST must be saved to an image. */
        private boolean dumpAst;

        /** Flag that specifies that the AST must be saved to a JSON file. */
        private boolean dumpJson;

        /** Folder to save the parse results. */
        private String workdir;
    }

    /**
     * Constructor.
     *
     * @param parsers the parsers to be used to parse code.
     */
    public ParseHandler(final Parsers parsers) {
        this.parsers = Objects.requireNonNull(parsers);
    }

    /**
     * Handles a command-line action with specified arguments and options.
     *
     * @param cmdLine the parsed command line.
     * @throws CmdLineException if arguments or options are invalid.
     * @throws HandlerException if the handler failed due to some error.
     */
    @Override
    public void handle(final CmdLine cmdLine) throws CmdLineException, HandlerException {
        final String path = cmdLine.getArgument("source");
        final File pathFile = new File(path);
        if (!pathFile.exists()) {
            throw new CmdLineException("Path does not exist: " + path);
        }
        final ParseOptions parseOptions = new ParseOptions();
        parseOptions.dot = cmdLine.getSingleOption("dot");
        parseOptions.dumpAst = cmdLine.hasOption("dump-ast");
        parseOptions.dumpJson = cmdLine.hasOption("dump-json");
        parseOptions.workdir = cmdLine.getSingleOption("workdir");
        handleFile(pathFile, parseOptions);
    }

    private void handleFile(
            final File file,
            final ParseOptions options) throws CmdLineException, HandlerException {
        final Node root = parseFile(file.getPath());
        if (options.dumpAst) {
            if (options.dot == null) {
                throw new CmdLineException("The 'dot' option is not specified!");
            }
            final String imagePath = file.getPath() + ".png";
            visualize(root, options.dot, imagePath);
        }
        if (options.dumpJson) {
            throw new UnsupportedOperationException();
        }
    }

    private Node parseFile(final String path) throws HandlerException {
        try {
            return parsers.parseFile(path);
        } catch (final ParserException ex) {
            throw new HandlerException("Failed to parse file " + path, ex);
        }
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

}

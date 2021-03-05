package org.cqfn.patternika;

import org.cqfn.patternika.util.cmdline.CmdLineApi;

/**
 * Describes the Patternika command-line API.
 *
 * @since 2021/03/05
 */
public class PatternikaApi {
    /** Command-line API. */
    private final CmdLineApi api;

    /**
     * Constructor.
     */
    public PatternikaApi() {
        this.api = new CmdLineApi();
    }

    /**
     * Returns the command-line API for Patternika.
     *
     * @return the command-line API for Patternika.
     */
    public CmdLineApi getCmdLineApi() {
        return api;
    }

}

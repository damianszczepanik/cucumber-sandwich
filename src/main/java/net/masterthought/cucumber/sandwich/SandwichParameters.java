package net.masterthought.cucumber.sandwich;

import com.beust.jcommander.Parameter;

/**
 * Collects parameters for the report generation.
 */
public class SandwichParameters {

    @Parameter(names = "-f", required = true, description = "Folder to monitor for cucumber.json report files")
    private String folder;

    @Parameter(names = "-o", required = true, description = "Output directory to generate the cucumber-html-reports into")
    private String outDir;

    @Parameter(names = "-n", required = false, description = "run once without file change listener")
    private Boolean withoutListener = false;

    public String getFolder() {
        return folder;
    }

    /**
     * Returns output directory.
     */
    public String getOutDir() {
        return outDir;
    }

    /**
     *
     * @return
     */
    public Boolean getWithoutListener() {
        return withoutListener;
    }
}

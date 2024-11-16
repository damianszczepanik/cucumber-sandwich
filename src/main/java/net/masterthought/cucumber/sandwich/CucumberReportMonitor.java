package net.masterthought.cucumber.sandwich;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;

public class CucumberReportMonitor {

    public static void main(String[] args) throws Exception {

        SandwichParameters params = new SandwichParameters();
        JCommander cmd = new JCommander(params);
        final long pollingInterval = 5 * 1000;

        try {
            cmd.parse(args);
            final File reportFolder = new File(params.getFolder());
            final File outputFolder = new File(params.getOutDir());
            createMonitorFolder(reportFolder);

            System.out.println("Starting Cucumber Sandwich.....");

            if (params.getWithoutListener()) {
                System.out.println("Running once only as -n flag supplied.....");
                generateReport(reportFolder, outputFolder);
            } else {
                System.out.println("Listening for change in folder: " + reportFolder.getAbsoluteFile());
                FileAlterationObserver observer = new FileAlterationObserver(reportFolder);
                FileAlterationMonitor monitor = new FileAlterationMonitor(pollingInterval);
                FileAlterationListener listener = new FileAlterationListenerAdaptor() {

                    @Override
                    public void onFileCreate(File file) {
                        try {
                            System.out.println("File created: " + file.getCanonicalPath());
                            generateReport(reportFolder, outputFolder);
                        } catch (Exception e) {
                            e.printStackTrace(System.err);
                        }
                    }

                    @Override
                    public void onFileChange(File file) {
                        try {
                            System.out.println("File changed: " + file.getCanonicalPath());
                            generateReport(reportFolder, outputFolder);
                        } catch (IOException e) {
                            e.printStackTrace(System.err);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                observer.addListener(listener);
                monitor.addObserver(observer);
                monitor.start();
            }
        } catch (ParameterException ex) {
            System.out.println(ex.getMessage());
            cmd.usage();
        }

    }

    private static List findJsonFiles(File targetDirectory) {
        File[] files = targetDirectory.listFiles(file -> file.isFile());

        return Arrays.stream(files).map(File::getName).collect(Collectors.toList());
    }

    private static void generateReport(File reportFolder, File outputFolder) throws Exception {
        File rd = new File(outputFolder + "/cucumber-html-reports");
        List<String> jsonFileList = findJsonReports(reportFolder);

        System.out.println("About to generate Cucumber Report into: " + rd.getAbsoluteFile());

        Configuration configuration = new Configuration(rd, "cucumber-jvm");

        ReportBuilder reportBuilder = new ReportBuilder(jsonFileList, configuration);
        reportBuilder.generateReports();
        System.out.println("Finished generating Cucumber Report into: " + rd.getAbsoluteFile());
    }

    private static List<String> findJsonReports(File reportFolder) {
        List<String> jsonFiles = findJsonFiles(reportFolder);
        List<String> reports = new ArrayList<>();

        System.out.println("Found json reports: " + jsonFiles.size());
        String reportPath = reportFolder.getAbsolutePath();
        for (String file : jsonFiles) {
            String reportJson = reportPath + "/" + file;
            System.out.println(reportJson);
            reports.add(reportJson);
        }
        return reports;
    }

    private static void createMonitorFolder(File reportFolder) {
        if (!reportFolder.exists()) {
            System.out.println("creating report directory: " + reportFolder.getAbsolutePath());
            boolean result = reportFolder.mkdirs();
            if (result) {
                System.out.println("created report directory: " + reportFolder.getAbsoluteFile());
            }
        }
    }
}

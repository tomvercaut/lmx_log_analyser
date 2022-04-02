
package uzg.rt.lmx.apps.log_analyser;

import org.apache.commons.cli.*;
import uzg.rt.lmx.log.analysis.OutOfLicensePredicate;
import uzg.rt.lmx.log.io.ExcelWriter;
import uzg.rt.lmx.log.io.LogReader;
import uzg.rt.lmx.log.model.Log;
import uzg.rt.lmx.log.parser.ParserException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {
        final var appOptions = parseCommandLineArgs(args);

        Log log;
        try {
            log = LogReader.read(Path.of(appOptions.getInputPath()));
        } catch (IOException | ParserException e) {
            throw new RuntimeException(e);
        }

        var writer = new ExcelWriter();
        if (appOptions.getAnalyses().contains(AppOptions.Analysis.OutOfLicense)) {
            var predicate = new OutOfLicensePredicate();
            Log filtered = new Log(log.stream().filter(predicate).collect(Collectors.toList()));
            writer.writeSheet(filtered, "out-of-license");
        }
        try {
            writer.save(Path.of(appOptions.getOutputFilename()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Options getOptions() {
        var options = new Options();
        var inputOption = Option.builder("i")
                .argName("FILE")
                .hasArg(true)
                .longOpt("input")
                .desc("LMX log file")
                .required(true)
                .numberOfArgs(1)
                .build();
        var outputOption = Option.builder("o")
                .argName("FILE")
                .hasArg(true)
                .longOpt("output")
                .desc("Name of the output file.")
                .required(true)
                .numberOfArgs(1)
                .build();
        var helpOption = Option.builder("h")
                .hasArg(false)
                .required(false)
                .desc("Print help message to show how to use the application.")
                .longOpt("help")
                .build();
        var outOfLicenseAnalysis = Option.builder("ol")
                .hasArg(false)
                .longOpt("out-of-license")
                .required(false)
                .desc("Filter which licenses run out.").build();

        options.addOption(inputOption);
        options.addOption(outputOption);
        options.addOption(outOfLicenseAnalysis);
        options.addOption(helpOption);
        return options;
    }

    private static AppOptions parseCommandLineArgs(String[] args) {
        var parser = new DefaultParser();
        var options = getOptions();
        var formatter = new HelpFormatter();
        var appOptions = new AppOptions();
        try {
            var cl = parser.parse(options, args);
            appOptions.setInputPath(cl.hasOption("input") ? cl.getOptionValue("input") : "");
            appOptions.setOutputFilename(cl.hasOption("output") ? cl.getOptionValue("output") : "");
            if (cl.hasOption("out-of-license")) {
                appOptions.getAnalyses().add(AppOptions.Analysis.OutOfLicense);
            }
        } catch (ParseException e) {
            formatter.printHelp("lmx_log_analyser", options);
            System.exit(0);
        }
        return appOptions;
    }
}

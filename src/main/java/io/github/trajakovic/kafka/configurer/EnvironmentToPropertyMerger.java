package io.github.trajakovic.kafka.configurer;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class EnvironmentToPropertyMerger {

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();

        if (args.length != 2) {
            System.err.println("Expecting exactly 2 params:");
            System.err.println("  - first param: inputFile.properties [ignored if non-existing file]");
            System.err.println("  - second param: environment var prefix [put minus - if there is no prefix]");

            System.exit(1);
        }

        final File file = new File(args[0]);
        if (file.exists()) {
            props.load(new FileInputStream(file));
        } else {
            System.out.println("# INFO: Missing input file " + file.getAbsolutePath());
        }

        final String environmentVarPrefix = processArg1(args[1]);
        System.out.println("# INFO: Using environment variable prefix: " + (environmentVarPrefix.isEmpty() ? "(empty)" : environmentVarPrefix));

        Map<String, String> kafkaVars = System.getenv().entrySet()
                .stream()
                .filter(entry -> environmentVarPrefix.isEmpty() || entry.getKey().toLowerCase().startsWith(environmentVarPrefix))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        for (Map.Entry<String, String> kafkaEntry : kafkaVars.entrySet()) {
            String propsKey = fromEnvironmentToPropKey(kafkaEntry.getKey().toLowerCase(), environmentVarPrefix, "_", ".");
            props.setProperty(propsKey, kafkaEntry.getValue());
        }

        final StringWriter sw = new StringWriter();
        props.store(sw, " MERGED_WITH: https://github.com/trajakovic/environment-to-property-merger");

        System.out.print(sw.toString());
    }

    private static String processArg1(String arg) {
        if (arg == null || arg.trim().equals("-")) {
            arg = "";
        }

        return arg.toLowerCase().trim();
    }

    private static String fromEnvironmentToPropKey(String environmentVariable, String prefix, String delimiterRegex, String delimiterReplacement) {
        if (prefix != null) {
            String strippedPrefix = environmentVariable.substring(Math.min(prefix.length(), environmentVariable.length()));
            return strippedPrefix.replaceAll(delimiterRegex, delimiterReplacement).toLowerCase();
        }

        return null;
    }
}

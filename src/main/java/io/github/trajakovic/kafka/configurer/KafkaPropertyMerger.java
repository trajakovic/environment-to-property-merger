package io.github.trajakovic.kafka.configurer;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class KafkaPropertyMerger {

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();

        if (args.length >= 1) {
            File file = new File(args[0]);
            if (file.exists()) {
                props.load(new FileInputStream(file));

                System.out.println("# Loaded " + file.getAbsolutePath());
            } else {
                System.out.println("# Missing " + file.getAbsolutePath());
            }
        }

        Map<String, String> kafkaVars = System.getenv().entrySet()
                .stream()
                .filter(entry -> entry.getKey().toLowerCase().startsWith("kafka_"))
                .collect(Collectors.toMap(
                        e -> e.getKey(), e -> e.getValue()
                ));

        for (Map.Entry<String, String> kafkaEntry : kafkaVars.entrySet()) {
            String propsKey = fromEnvironmentToPropKey(kafkaEntry.getKey().toLowerCase(), "kafka_", "_", ".");
            props.setProperty(propsKey, kafkaEntry.getValue());
        }

        StringWriter sw = new StringWriter();
        props.store(sw, "kafka-property-configurer");

        System.out.println(sw.toString());
    }

    private static String fromEnvironmentToPropKey(String environmentVariable, String prefix, String delimiterRegex, String delimiterReplacement) {
        if (prefix != null) {
            String strippedPrefix = environmentVariable.substring(Math.min(prefix.length(), environmentVariable.length()));
            return strippedPrefix.replaceAll(delimiterRegex, delimiterReplacement).toLowerCase();
        }

        return null;
    }
}

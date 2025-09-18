// LsUtf8.java
// Simple ls-like command for Windows, outputs filenames in UTF-8
// Compile: javac LsUtf8.java
// Run: java LsUtf8 [directory]

import java.io.File;
import java.nio.charset.StandardCharsets;

public class LsUtf8 {
    public static void main(String[] args) {
        String dirPath = ".";
        if (args.length >= 1) {
            dirPath = args[0];
        }

        File dir = new File(dirPath);
        if (!dir.isDirectory()) {
            System.err.println("Not a directory: " + dirPath);
            System.exit(1);
        }

        File[] files = dir.listFiles();
        if (files == null) {
            System.err.println("Cannot access: " + dirPath);
            System.exit(1);
        }

        for (File f : files) {
            String mark = f.isDirectory() ? "d" : "-";
            // ensure UTF-8 output
            String name = new String(f.getName().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            System.out.println(mark + "\t" + name);
        }
    }
}

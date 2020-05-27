package io.github.laskowski.shell.script;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TXTWriter {
    private BufferedWriter writer;
    private File outputFile;

    public TXTWriter(File dir, String fileName) {
        try {
            outputFile = new File(dir, fileName);
            outputFile.getParentFile().mkdirs();
            outputFile.createNewFile();

            writer = new BufferedWriter(new FileWriter(outputFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(String string, Object... arguments) {
        doWrite(String.format(string, arguments));
    }

    public void newLine() {
        doWrite(System.lineSeparator());
    }

    public File save() {
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        outputFile.setExecutable(true);
        return outputFile;
    }

    private void doWrite(String string) {
        try {
            writer.write(string);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
import java.io.*;
import java.nio.file.*;

public class FfmpegConvert {
    public static void main(String[] args) {
        // �o�̓��O�t�@�C�� (��΃p�X�Ŏw��)
        Path logFile = Paths.get("output.txt").toAbsolutePath();

        try (BufferedWriter log = Files.newBufferedWriter(logFile)) {
            // �J�����g�f�B���N�g�����΃p�X�Ŏ擾
            Path currentDir = Paths.get(".").toAbsolutePath().normalize();

            try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentDir)) {
                for (Path entry : stream) {
                    if (Files.isRegularFile(entry)) {
                        // entry ���΃p�X�ɕϊ�
                        Path absPath = entry.toAbsolutePath();
                        String fileName = absPath.toString();

                        if (fileName.endsWith(".mp4") || fileName.endsWith(".wav")) {
                            // �o�̓t�@�C������΃p�X
                            String outputName = fileName + ".mp3";

                            ProcessBuilder pb = new ProcessBuilder(
                                    "ffmpeg", "-y", "-i", fileName, outputName);

                            pb.redirectErrorStream(true);

                            Process process = pb.start();

                            try (BufferedReader reader =
                                         new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    log.write(line);
                                    log.newLine();
                                }
                            }

                            int exitCode = process.waitFor();
                            log.write("Finished: " + fileName + " -> " + outputName + " (exit=" + exitCode + ")");
                            log.newLine();
                            log.flush();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

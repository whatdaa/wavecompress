package compression;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

public class Compress {
    public static void main(String[] args) throws IOException{
        Compress test = new Compress();
        test.generate();
    }
    // generates a file of random 8 byte numbers
    // this will be to test its general compression effectiveness, as randomness should not be as easy to compress compared to normal binary with its naturally occurring redundancy
    void generate() throws IOException{
            File file = new File("src/compression/binary.txt");
            file.createNewFile();
        try (FileOutputStream writer = new FileOutputStream(file)) {
            SecureRandom random = new SecureRandom(); // apparently Random does not generate all possible 8-byte numbers
            byte[] bytes = new byte[1];
            for (int i = 0; i < 1024; i++) {
                random.nextBytes(bytes);
                writer.write(bytes);
            }
        }

    }
}
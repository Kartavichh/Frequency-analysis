import java.io.*;
import java.util.Map;


public class Main {
    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        Reader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        System.out.println("Введите шифротекст для частотного анализа");
        Decryption decryption = new Decryption(bufferedReader.readLine());

        // Анализ частот символов
        Map<Character, Float> encryptedFrequencies = decryption.sortData(decryption.getEncryptionText());

        // Поиск ключа шифра
        int caesarKey = decryption.findCaesarKey(encryptedFrequencies);

        // Расшифровка текста
        String decryptedText = decryption.decryptWithCaesar(caesarKey);
        System.out.println("Расшифрованный текст: " + decryptedText);
    }
}

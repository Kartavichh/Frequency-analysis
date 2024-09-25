import java.util.*;

import static java.lang.Character.toLowerCase;

public class Decryption {
    private String encryptionText;
    public Map<Character, Float> defValue = new HashMap<>();

    // Частоты букв русского языка (приблизительные данные)
    private static final Map<Character, Float> RUSSIAN_LETTER_FREQUENCIES;

    static {
        RUSSIAN_LETTER_FREQUENCIES = new HashMap<>();
        RUSSIAN_LETTER_FREQUENCIES.put('о', 0.1097f);
        RUSSIAN_LETTER_FREQUENCIES.put('е', 0.0845f);
        RUSSIAN_LETTER_FREQUENCIES.put('а', 0.0801f);
        RUSSIAN_LETTER_FREQUENCIES.put('и', 0.0735f);
        RUSSIAN_LETTER_FREQUENCIES.put('н', 0.0670f);
        RUSSIAN_LETTER_FREQUENCIES.put('т', 0.0626f);
        RUSSIAN_LETTER_FREQUENCIES.put('с', 0.0547f);
        RUSSIAN_LETTER_FREQUENCIES.put('р', 0.0473f);
        RUSSIAN_LETTER_FREQUENCIES.put('в', 0.0454f);
        RUSSIAN_LETTER_FREQUENCIES.put('л', 0.0440f);
        RUSSIAN_LETTER_FREQUENCIES.put('к', 0.0349f);
        RUSSIAN_LETTER_FREQUENCIES.put('м', 0.0321f);
        RUSSIAN_LETTER_FREQUENCIES.put('д', 0.0298f);
        RUSSIAN_LETTER_FREQUENCIES.put('п', 0.0281f);
        RUSSIAN_LETTER_FREQUENCIES.put('у', 0.0262f);
        RUSSIAN_LETTER_FREQUENCIES.put('я', 0.0201f);
        RUSSIAN_LETTER_FREQUENCIES.put('ы', 0.0190f);
        RUSSIAN_LETTER_FREQUENCIES.put('ь', 0.0174f);
        RUSSIAN_LETTER_FREQUENCIES.put('з', 0.0165f);
        RUSSIAN_LETTER_FREQUENCIES.put('б', 0.0159f);
        RUSSIAN_LETTER_FREQUENCIES.put('г', 0.0170f);
        RUSSIAN_LETTER_FREQUENCIES.put('ч', 0.0144f);
        RUSSIAN_LETTER_FREQUENCIES.put('й', 0.0121f);
        RUSSIAN_LETTER_FREQUENCIES.put('х', 0.0097f);
        RUSSIAN_LETTER_FREQUENCIES.put('ж', 0.0094f);
        RUSSIAN_LETTER_FREQUENCIES.put('ш', 0.0073f);
        RUSSIAN_LETTER_FREQUENCIES.put('ю', 0.0064f);
        RUSSIAN_LETTER_FREQUENCIES.put('ц', 0.0048f);
        RUSSIAN_LETTER_FREQUENCIES.put('щ', 0.0036f);
        RUSSIAN_LETTER_FREQUENCIES.put('э', 0.0032f);
        RUSSIAN_LETTER_FREQUENCIES.put('ф', 0.0026f);
        RUSSIAN_LETTER_FREQUENCIES.put('ъ', 0.0004f);
        RUSSIAN_LETTER_FREQUENCIES.put('ё', 0.0004f);
    }

    public Decryption(String encryptionText) {
        this.encryptionText = encryptionText;
    }

    public void setEncryptionText(String encryptionText) {
        this.encryptionText = encryptionText;
    }

    public String getEncryptionText() {
        return encryptionText;
    }

    @Override
    public String toString() {
        return "encryptionText = " + "'" + encryptionText + "'";
    }

    public Map<Character, Float> sortData(String encryptionText) {
        encryptionText = encryptionText.replaceAll("[^А-Яа-я]", "");
        char[] chars = encryptionText.toCharArray();
        Map<Character, Float> counters = new HashMap<>();
        for (char aChar : chars) {
            counters.compute(toLowerCase(aChar), (key, count) -> count == null ? 1 : count + 1);
        }

        int totalLetters = chars.length;
        // Преобразуем частоты в проценты
        counters.replaceAll((key, value) -> value / totalLetters);

        counters.forEach((key, value) ->
                System.out.println(Arrays.toString(new Character[]{key}) + " - " + value));
        return counters;
    }

    // Метод для поиска ключа Цезаря
    public int findCaesarKey(Map<Character, Float> encryptedFrequencies) {
        int bestShift = 0;
        double bestCorrelation = -Double.MAX_VALUE;

        // Попробуем все возможные сдвиги от 0 до 32 (т.к. в русском алфавите 33 буквы)
        for (int shift = 0; shift < 33; shift++) {
            double correlation = 0;

            // Для каждого символа в зашифрованном тексте
            for (Map.Entry<Character, Float> entry : encryptedFrequencies.entrySet()) {
                char shiftedChar = shiftChar(entry.getKey(), shift);
                Float referenceFrequency = RUSSIAN_LETTER_FREQUENCIES.getOrDefault(shiftedChar, 0.0f);
                correlation += entry.getValue() * referenceFrequency;
            }

            // Сравниваем с лучшей корреляцией
            if (correlation > bestCorrelation) {
                bestCorrelation = correlation;
                bestShift = shift;
            }
        }

        System.out.println("Наиболее вероятный сдвиг: " + bestShift);
        return bestShift;
    }

    // Метод для расшифровки текста с использованием найденного ключа
    public String decryptWithCaesar(int shift) {
        StringBuilder decryptedText = new StringBuilder();
        for (char c : encryptionText.toCharArray()) {
            if (Character.isLetter(c)) {
                decryptedText.append(shiftChar(c, shift));
            } else {
                decryptedText.append(c);
            }
        }
        return decryptedText.toString();
    }

    // Вспомогательный метод для сдвига символа
    private char shiftChar(char c, int shift) {
        char base = Character.isUpperCase(c) ? 'А' : 'а';
        int alphabetSize = 33;  // Размер русского алфавита
        return (char) ((c - base + shift) % alphabetSize + base);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Decryption that = (Decryption) o;
        return Objects.equals(encryptionText, that.encryptionText);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(encryptionText);
    }
}

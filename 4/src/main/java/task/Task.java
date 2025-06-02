package task;
import java.util.Scanner;

public class Task {
    private static boolean isConsonant(char c) {
        String vowels = "аеєиіїоуюяaeiouyАЕЄИІЇОУЮЯAEIOUY";
        return Character.isLetter(c) && !vowels.contains(String.valueOf(c));
    }
    public static void processText() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введіть ваш текст:");
        String input = scanner.nextLine();
        System.out.println("Введіть кількість букв для видалення:");
        int targetLength = scanner.nextInt();
        String[] words = input.split("\\s+");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            String cleanWord = word.replaceAll("[^a-zA-Zа-яА-ЯїЇєЄіІґҐ']", "");
            if (!(cleanWord.length() == targetLength && isConsonant(cleanWord.charAt(0)))) {
                result.append(word).append(" ");
            }
        }
        System.out.println("Оригінальний: " + input);
        System.out.println("Очищений: " + result.toString().trim());
    }
}

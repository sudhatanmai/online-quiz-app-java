import java.util.*;

/**
 * Console-based Online Quiz App
 * Demonstrates: loops, control flow, collections, iterators, sorting, shuffling, input validation.
 *
 * How to run:
 *   javac QuizApp.java
 *   java QuizApp
 */
public class QuizApp {

    // ---- Question model ----
    static class Question {
        final String topic;          // e.g., "Loops", "Collections"
        final String prompt;         // question text
        final List<String> options;  // options (size >= 2)
        final int correctIndex;      // 0-based index of correct option

        Question(String topic, String prompt, List<String> options, int correctIndex) {
            this.topic = topic;
            this.prompt = prompt;
            this.options = options;
            this.correctIndex = correctIndex;
        }

        boolean isCorrect(int answerIndex1Based) {
            return (answerIndex1Based - 1) == correctIndex;
        }
    }

    // ---- Bank of questions ----
    static class QuestionBank {
        static List<Question> getQuestions() {
            List<Question> qs = new ArrayList<>();

            qs.add(new Question(
                "Loops",
                "1. What are Java loops?",
                Arrays.asList(
                    "They repeat a set of statements while a condition holds.",
                    "They are mechanisms to store key-value pairs.",
                    "They are blocks used to handle exceptions.",
                    "They are special methods to initialize objects."
                ),
                0
            ));

            qs.add(new Question(
                "Loops",
                "2. What is an enhanced for-loop?",
                Arrays.asList(
                    "A loop to iterate over arrays/collections without using an index.",
                    "A while loop with labels.",
                    "A do-while loop alias.",
                    "A loop that supports multiple initializations and updates."
                ),
                0
            ));

            qs.add(new Question(
                "I/O",
                "3. How do you handle multiple user inputs?",
                Arrays.asList(
                    "Use Scanner in a loop and validate each input.",
                    "Use System.out to read input.",
                    "Use Random to generate inputs.",
                    "It is not possible in Java."
                ),
                0
            ));

            qs.add(new Question(
                "Control Flow",
                "4. How is switch-case different from if-else?",
                Arrays.asList(
                    "switch tests one expression against constant cases; if-else can test complex boolean conditions.",
                    "switch is always slower than if-else.",
                    "switch can evaluate ranges directly.",
                    "if-else cannot be nested."
                ),
                0
            ));

            qs.add(new Question(
                "Collections",
                "5. What are collections in Java?",
                Arrays.asList(
                    "A framework providing data structures like List, Set, and Map.",
                    "GUI components for building apps.",
                    "File handling classes.",
                    "Networking classes."
                ),
                0
            ));

            qs.add(new Question(
                "ArrayList",
                "6. What is an ArrayList?",
                Arrays.asList(
                    "A resizable array implementation of the List interface.",
                    "A linked list implementation of Queue.",
                    "A hash table implementation of Map.",
                    "An immutable array type."
                ),
                0
            ));

            qs.add(new Question(
                "Iterators",
                "7. How do you iterate using iterators?",
                Arrays.asList(
                    "Obtain iterator() and use hasNext()/next().",
                    "Use an index with get(i).",
                    "Use stream().map(...).",
                    "Call foreach method directly."
                ),
                0
            ));

            qs.add(new Question(
                "Map",
                "8. What is a Map?",
                Arrays.asList(
                    "An object that maps keys to values with no duplicate keys.",
                    "An ordered list that allows duplicates.",
                    "A sorted set of unique elements.",
                    "A class for file I/O mapping only."
                ),
                0
            ));

            qs.add(new Question(
                "Sorting",
                "9. How do you sort a List?",
                Arrays.asList(
                    "Use Collections.sort(list) or list.sort(Comparator).",
                    "Use Math.sort(list).",
                    "Call list.shuffle().",
                    "Sorting happens automatically."
                ),
                0
            ));

            qs.add(new Question(
                "Shuffling",
                "10. How do you shuffle elements?",
                Arrays.asList(
                    "Use Collections.shuffle(list).",
                    "Use list.sort(random).",
                    "Use Collections.rotate(list, 0).",
                    "Use Random.nextInt(list)."
                ),
                0
            ));

            return qs;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("===== Java Console Quiz App =====");
        System.out.print("Enter your name: ");
        String name = sc.nextLine().trim();

        boolean running = true;
        while (running) { // <-- while-loop (control flow)
            System.out.println("\nHi " + name + "! Choose an option:");
            System.out.println("1) Start Quiz");
            System.out.println("2) Practice (view questions sorted by text)");
            System.out.println("3) Exit");
            int choice = readIntInRange(sc, "Your choice (1-3): ", 1, 3);

            switch (choice) { // <-- switch-case (control flow)
                case 1:
                    startQuiz(sc, name);
                    break;
                case 2:
                    practiceMode();
                    break;
                case 3:
                    running = false;
                    System.out.println("Goodbye, " + name + "!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        sc.close();
    }

    // ---- Start quiz flow ----
    private static void startQuiz(Scanner sc, String name) {
        List<Question> questions = QuestionBank.getQuestions();   // <-- List (collection)
        Collections.shuffle(questions);                           // <-- shuffle elements

        int score = 0;
        Map<Question, Integer> userAnswers = new LinkedHashMap<>(); // <-- Map to keep answers in order

        System.out.println("\n--- Quiz Started ---");
        int qNo = 1;

        // enhanced for-loop over questions
        for (Question q : questions) {
            System.out.println("\n" + q.prompt);
            // show options (enhanced for-loop)
            int idx = 1;
            for (String opt : q.options) {
                System.out.println("  " + (idx++) + ") " + opt);
            }

            int ans = readIntInRange(sc, "Your answer (1-" + q.options.size() + "): ", 1, q.options.size());
            userAnswers.put(q, ans);

            if (q.isCorrect(ans)) {
                score++;
                System.out.println("✅ Correct!");
            } else {
                System.out.println("❌ Incorrect.");
            }
        }

        int total = questions.size();
        double percent = (score * 100.0) / total;

        System.out.println("\n--- Results ---");
        System.out.println("Name      : " + name);
        System.out.println("Score     : " + score + " / " + total);
        System.out.printf ("Percentage: %.2f%%\n", percent);
        System.out.println( percent >= 60.0 ? "Status    : PASS" : "Status    : TRY AGAIN" );

        // Build a list of wrong answers for review
        List<Question> wrong = new ArrayList<>();
        for (Map.Entry<Question, Integer> entry : userAnswers.entrySet()) {
            Question q = entry.getKey();
            int userAns = entry.getValue();
            if (!q.isCorrect(userAns)) wrong.add(q);
        }

        if (!wrong.isEmpty()) {
            // sort wrong questions alphabetically by prompt (sorting demo)
            wrong.sort(Comparator.comparing(q -> q.prompt.toLowerCase()));

            System.out.println("\n--- Review (Wrong Answers Sorted) ---");
            // Iterate using an Iterator (iterator demo)
            Iterator<Question> it = wrong.iterator();
            while (it.hasNext()) {
                Question q = it.next();
                System.out.println(q.prompt);
                System.out.println("  Correct: " + q.options.get(q.correctIndex));
            }
        } else {
            System.out.println("\n🎉 Perfect! All answers are correct.");
        }
    }

    // ---- Practice mode: shows questions sorted by text ----
    private static void practiceMode() {
        List<Question> qs = QuestionBank.getQuestions();
        // sort by prompt (alphabetical)
        qs.sort(Comparator.comparing(q -> q.prompt.toLowerCase()));

        System.out.println("\n--- Practice Mode (Questions Sorted by Text) ---");
        for (Question q : qs) {
            System.out.println(q.prompt + "  [" + q.topic + "]");
        }
        System.out.println("(Tip: Start the quiz from the main menu when ready.)");
    }

    // ---- Input helper with validation (handles multiple user inputs safely) ----
    private static int readIntInRange(Scanner sc, String label, int min, int max) {
        while (true) { // input validation loop
            System.out.print(label);
            if (sc.hasNextInt()) {
                int v = sc.nextInt();
                sc.nextLine(); // consume newline
                if (v >= min && v <= max) return v;
            } else {
                sc.nextLine(); // discard invalid token
            }
            System.out.println("Please enter a number between " + min + " and " + max + ".");
        }
    }
}

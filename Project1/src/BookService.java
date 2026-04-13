import java.io.*;
import java.util.*;

public class BookService {

    // إخفاء مسار الملف
    private final String FILE_PATH = "books.csv";

    // دالة عامة لتحميل الكتب
    public List<Book> loadBooks() {
        List<Book> books = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // تخطي العناوين

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Book b = new Book(
                    Integer.parseInt(data[0]),
                    data[1],
                    data[2],
                    Integer.parseInt(data[3]),
                    Integer.parseInt(data[4])
                );
                books.add(b);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return books;
    }

    // دالة عامة لإضافة كتاب
    public void addBook(Book b) {
        try (FileWriter fw = new FileWriter(FILE_PATH, true)) {
            fw.write(
                b.getId() + "," +
                b.getTitle() + "," +
                b.getAuthor() + "," +
                b.getYear() + "," +
                b.getQuantity() + "\n"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
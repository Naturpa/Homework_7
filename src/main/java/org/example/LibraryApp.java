//// Библиотека Gson используется для удобной работы с JSON-данными
// (для чтения списка посетителей библиотеки из файла "books.json"
import com.google.gson.Gson;
//TypeToken используется для создания типа List для корректного чтения данных из JSON
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
class Book {
    private String name;
    private String author;
    private int publishingYear;
    private String isbn;
    private String publisher;
    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public int getPublishingYear() {
        return publishingYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getPublisher() {
        return publisher;
    }
}

@Data
@AllArgsConstructor
class Visitor {
    private String name;
    private String surname;
    private String phone;
    private boolean subscribed;
    private List<Book> favoriteBooks;

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public List<Book> getFavoriteBooks() {
        return favoriteBooks;
    }
}


    @Data
    @AllArgsConstructor
    class SMS {
        private String phoneNumber;
        private String message;

        public SMS(String phoneNumber, String message) {
            this.phoneNumber = phoneNumber;
            this.message = message;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getMessage() {
            return message;
        }

    }

    public class LibraryApp {
        public static void main(String[] args) {
            Gson gson = new Gson();
            try (FileReader reader = new FileReader("books.json")) {
                Type type = new TypeToken<List<Visitor>>() {
                }.getType();
                List<Visitor> visitors = gson.fromJson(reader, type);

                // Задание 1
                Map<String, Long> visitorsCountByName = visitors.stream()
                        .collect(Collectors.groupingBy(Visitor::getName, Collectors.counting()));
                System.out.println("\n"+"Number 1:");
                System.out.println("/n The list of visitors and their number: " + visitorsCountByName);

                // Задание 2
                List<Book> uniqueFavoriteBooks = visitors.stream()
                        .flatMap(visitor -> visitor.getFavoriteBooks().stream())
                        .distinct()
                        .collect(Collectors.toList());
                System.out.println("\n"+ "Number 2:");
                System.out.println("List and number of unique books in favorites: " + uniqueFavoriteBooks.size());

                // Задание 3
                System.out.println("\n"+ "Number 3:");
                visitors.stream()
                        .flatMap(visitor -> visitor.getFavoriteBooks().stream())
                        .sorted((b1, b2) -> Integer.compare(b1.getPublishingYear(), b2.getPublishingYear()))
                        .forEach(book -> System.out.println(book.getName() + " - " + book.getPublishingYear()));

                // Задание 4
                boolean janeAustenFavorite = visitors.stream()
                        .anyMatch(visitor -> visitor.getFavoriteBooks().stream()
                                .anyMatch(book -> book.getAuthor().equals("Jane Austen")));
                System.out.println("\n"+ "Number 4:");
                System.out.println("Does anyone have a book by Jane Austen in their favorites: " + janeAustenFavorite);

                // Задание 5
                int maxFavoriteBooksCount = visitors.stream()
                        .mapToInt(visitor -> visitor.getFavoriteBooks().size())
                        .max().orElse(0);
                System.out.println("\n"+ "Number 5:");
                System.out.println("Maximum number of books in favorites: " + maxFavoriteBooksCount);

                // Задание 6
                double averageFavoriteBooksCount = visitors.stream()
                        .mapToInt(visitor -> visitor.getFavoriteBooks().size())
                        .average().orElse(0);
                System.out.println("\n"+ "Number 6:");
                for (Visitor visitor : visitors) {
                    String message;
                    if (visitor.getFavoriteBooks().size() > averageFavoriteBooksCount) {
                        message = "you are a bookworm";
                    } else if (visitor.getFavoriteBooks().size() < averageFavoriteBooksCount) {
                        message = "read more";
                    } else {
                        message = "fine";
                    }
                    SMS sms = new SMS(visitor.getPhone(), message);
                    System.out.println("Visitor " + visitor.getName() + " " + visitor.getSurname() + ": " + sms.getMessage());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }





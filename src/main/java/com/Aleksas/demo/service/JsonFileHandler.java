package com.Aleksas.demo.service;

import com.Aleksas.demo.entity.Book;
import com.Aleksas.demo.entity.Books;
import com.Aleksas.demo.entity.LibraryUser;
import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class JsonFileHandler {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final String jsonFileLocation = "D:\\spirng boot projects\\DemoFileSystem\\books.json";
    private final String jsonUserWithBooks = "D:\\spirng boot projects\\DemoFileSystem\\users\\";

    public void addNewBook(Book book) {
        try {
            book.setGUID(java.util.UUID.randomUUID().toString());

            JsonElement element = JsonParser.parseReader(new FileReader(jsonFileLocation));
            JsonArray array = (JsonArray) element.getAsJsonObject().get("listOfBooks");
            Iterator<JsonElement> iterator = array.iterator();


            FileWriter fileWriter = new FileWriter(jsonFileLocation);
            JsonWriter jsonWriter = new JsonWriter(fileWriter);
            Books books = new Books();
            while (iterator.hasNext()){
                books.addBook(gson.fromJson(iterator.next().toString(), Book.class));
            }
            books.addBook(book);
            gson.toJson(books, Books.class, jsonWriter);
            fileWriter.close();
            jsonWriter.close();

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public long timeBetween(String date){
        String pattern = "yyyy-MM-dd";
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
        LocalDate until = LocalDate.parse(date, format);
        System.out.println(localDate.until(until,ChronoUnit.MONTHS));
        return localDate.until(until,ChronoUnit.MONTHS);

    }

    public String takeBook(String name, String guid, String date){
        try {
            if(timeBetween(date)>=2)
                return "Can't take book longer than two months";
            String userFileString = "D:\\spirng boot projects\\DemoFileSystem\\users\\" + name +".json";
            File userFile = new File(userFileString);
            if(userFile.exists())
                return this.takeBookForExistingUser(name, guid);
            else
                 return this.takeBookForNewUser(name, guid);
        } catch (Exception e){
            e.printStackTrace();
        }

        return "Error! Unable to take books :(";
    }

    private String takeBookForNewUser(String name, String guid) {
        try {
            String userFileString = jsonUserWithBooks + name +".json";
            FileWriter fileWriter = new FileWriter(userFileString);
            JsonWriter jsonWriter = new JsonWriter(fileWriter);
            LibraryUser user = new LibraryUser();
            Book book = this.getBook(guid);
            user.addBook(book);
            user.setName(name);
            gson.toJson(user, LibraryUser.class, jsonWriter);
            jsonWriter.close();
            fileWriter.close();
            return "Book " + book.getName() + " has been taken for new user.";

        } catch (Exception e){
            e.printStackTrace();
        }
        return "Error! taking book for new user";
    }

    private String takeBookForExistingUser(String name, String guid) {
        try {
            String userFileString = jsonUserWithBooks + name +".json";

            JsonElement element = JsonParser.parseReader(new FileReader(userFileString));
            JsonArray bookArray = element.getAsJsonObject().get("userBooks").getAsJsonObject().get("listOfBooks").getAsJsonArray();

            int index = 0;
            for(JsonElement bookJsonElement : bookArray){
                index++;
                if(index==2)
                    return name + " Already have 3 books";
            }
            LibraryUser user = new LibraryUser();
            Book book = this.getBook(guid);
            for (JsonElement jsonElement : bookArray) {
                user.addBook(gson.fromJson(jsonElement.toString(), Book.class));
            }
            user.addBook(this.getBook(guid));
            FileWriter fileWriter = new FileWriter(userFileString);
            JsonWriter jsonWriter = new JsonWriter(fileWriter);
            gson.toJson(user, LibraryUser.class, jsonWriter);
            fileWriter.close();
            jsonWriter.close();
            return "Book " + book.getName() + " has been taken.";

        } catch (Exception e){
            e.printStackTrace();
        }
        return "error! taking book for current user";
    }

    public Books getBooksByAuthor(String author){
        try {
            JsonElement element = JsonParser.parseReader(new FileReader(jsonFileLocation));
            JsonArray array = element.getAsJsonObject().get("listOfBooks").getAsJsonArray();
            Books books = new Books();
            for (JsonElement item : array) {
                JsonObject object = item.getAsJsonObject();
                String bookAuthor = object.get("author").getAsString();
                if(author.equals(bookAuthor)) {
                    Book book = gson.fromJson(object.toString(), Book.class);
                    books.addBook(book);
                }
            }
            return books;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Book getBook(String ID){
        try {
            JsonElement element = JsonParser.parseReader(new FileReader(jsonFileLocation));
            JsonArray array = element.getAsJsonObject().get("listOfBooks").getAsJsonArray();
            for (JsonElement item : array) {
                JsonObject object = item.getAsJsonObject();
                String GUID = object.get("GUID").getAsString();
                if(GUID.equals(ID)) {
                    return  gson.fromJson(object.toString(), Book.class);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Books getBooks(){
        try {
            JsonElement element = JsonParser.parseReader(new FileReader(jsonFileLocation));
            JsonObject object = element.getAsJsonObject();
            return gson.fromJson(object.toString(), Books.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String deleteBook(String id){
        try {
            if(this.getBook(id) == null)
                return "error!";
            JsonElement element = JsonParser.parseReader(new FileReader(jsonFileLocation));
            JsonArray array = (JsonArray) element.getAsJsonObject().get("listOfBooks");
            Iterator<JsonElement> iterator = array.iterator();

            FileWriter fileWriter = new FileWriter(jsonFileLocation);
            JsonWriter jsonWriter = new JsonWriter(fileWriter);
            Books books = new Books();
            while (iterator.hasNext()){
                Book book = gson.fromJson(iterator.next().toString(), Book.class);
                if(!book.getGUID().equals(id))
                    books.addBook(book);
            }
            gson.toJson(books, Books.class, jsonWriter);
            fileWriter.close();
            jsonWriter.close();
            return "deleted!";

        } catch (Exception e){
            e.printStackTrace();
        }
        return "error!";
    }

    public Books getBooksByCategory(String category) {
        try {
            JsonElement element = JsonParser.parseReader(new FileReader(jsonFileLocation));
            JsonArray array = element.getAsJsonObject().get("listOfBooks").getAsJsonArray();
            Books books = new Books();
            for (JsonElement item : array) {
                JsonObject object = item.getAsJsonObject();
                String bookCategory = object.get("category").getAsString();
                if(category.equals(bookCategory)) {
                    Book book = gson.fromJson(object.toString(), Book.class);
                    books.addBook(book);
                }
            }
            return books;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Books getBooksByLanguage(String language) {
        try {
            JsonElement element = JsonParser.parseReader(new FileReader(jsonFileLocation));
            JsonArray array = element.getAsJsonObject().get("listOfBooks").getAsJsonArray();
            Books books = new Books();
            for (JsonElement item : array) {
                JsonObject object = item.getAsJsonObject();
                String bookLanguage = object.get("language").getAsString();
                if(language.equals(bookLanguage)) {
                    Book book = gson.fromJson(object.toString(), Book.class);
                    books.addBook(book);
                }
            }
            return books;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Books getBooksByISBN(long isbn) {
        try {
            JsonElement element = JsonParser.parseReader(new FileReader(jsonFileLocation));
            JsonArray array = element.getAsJsonObject().get("listOfBooks").getAsJsonArray();
            Books books = new Books();
            for (JsonElement item : array) {
                JsonObject object = item.getAsJsonObject();
                long bookISBN = object.get("isbn").getAsLong();
                if(isbn == bookISBN) {
                    Book book = gson.fromJson(object.toString(), Book.class);
                    books.addBook(book);
                }
            }
            return books;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Books getBooksByName(String name) {
        try {
            JsonElement element = JsonParser.parseReader(new FileReader(jsonFileLocation));
            JsonArray array = element.getAsJsonObject().get("listOfBooks").getAsJsonArray();
            Books books = new Books();
            for (JsonElement item : array) {
                JsonObject object = item.getAsJsonObject();
                String bookName = object.get("name").getAsString();
                if(name.equals(bookName)) {
                    Book book = gson.fromJson(object.toString(), Book.class);
                    books.addBook(book);
                }
            }
            return books;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Books getBooksByAvailable(boolean available) {
        try {
            if(available)
                return this.getBooks();
            else
                return this.getTakenBooks();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Books getTakenBooks(){
        try {
            Set<String> fileList = listFilesUsingJavaIO(jsonUserWithBooks);
            Iterator<String> iterator = fileList.iterator();
            Books books = new Books();
            while (iterator.hasNext()){
                JsonElement element = JsonParser.parseReader(new FileReader(jsonUserWithBooks+iterator.next()));
                JsonArray jsonArray =  element.getAsJsonObject().get("userBooks").getAsJsonObject().get("listOfBooks").getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    Book book = gson.fromJson(jsonElement.toString(), Book.class);
                    books.addBook(book);
                }

            }

            return books;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Set<String> listFilesUsingJavaIO(String dir) {
        return Stream.of(Objects.requireNonNull(new File(dir).listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }

}

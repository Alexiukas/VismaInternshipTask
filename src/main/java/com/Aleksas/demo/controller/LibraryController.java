package com.Aleksas.demo.controller;

import com.Aleksas.demo.entity.Book;
import com.Aleksas.demo.entity.Books;
import com.Aleksas.demo.service.JsonFileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class LibraryController {

    @Autowired
    private JsonFileHandler jsonFileHandler;

    @GetMapping("/book")
    public Book getBook(@RequestParam String id){
        return jsonFileHandler.getBook(id);
    }

    @GetMapping("/books")
    public Books getBooks(){
        return jsonFileHandler.getBooks();
    }


    @GetMapping("/books/filter/author/{author}")
    public Books getBooksByAuthor(@PathVariable("author")String author){
        return jsonFileHandler.getBooksByAuthor(author);
    }

    @GetMapping("/books/filter/category/{category}")
    public Books getBooksByCategory(@PathVariable("category")String category){
        return jsonFileHandler.getBooksByCategory(category);
    }

    @GetMapping("/books/filter/language/{language}")
    public Books getBooksByLanguage(@PathVariable("language")String language){
        return jsonFileHandler.getBooksByLanguage(language);
    }

    @GetMapping("/books/filter/isbn/{isbn}")
    public Books getBooksByISBN(@PathVariable("isbn")long isbn){
        return jsonFileHandler.getBooksByISBN(isbn);
    }

    @GetMapping("/books/filter/name/{name}")
    public Books getBooksByName(@PathVariable("name")String name){
        return jsonFileHandler.getBooksByName(name);
    }

    @GetMapping("/books/filter/available/{available}")
    public Books getBooksByAvailable(@PathVariable("available")boolean available){
        return jsonFileHandler.getBooksByAvailable(available);
    }

    @GetMapping("/take")
    public String takeBook(@RequestParam(value = "name")String name,
                           @RequestParam(value = "guid")String guid,
                           @RequestParam(value = "until")String date){
        return jsonFileHandler.takeBook(name, guid, date);
    }

    @PostMapping("/add/book")
    public void addBook(@RequestBody Book book){
        jsonFileHandler.addNewBook(book);
    }

    @DeleteMapping("/delete/{guid}")
    public String deleteBook(@PathVariable("guid")String id){
        return jsonFileHandler.deleteBook(id);
    }

}

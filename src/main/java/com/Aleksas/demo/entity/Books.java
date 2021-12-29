package com.Aleksas.demo.entity;

import java.util.ArrayList;
import java.util.List;

public class Books {
    private final List<Book> listOfBooks = new ArrayList<>();

    public void addBook(Book book){
        listOfBooks.add(book);
    }

    public List<Book> getBooks(){
        return listOfBooks;
    }
}

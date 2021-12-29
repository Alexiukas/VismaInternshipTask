package com.Aleksas.demo.entity;

public class LibraryUser {
    private String name;
    private final Books userBooks = new Books();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addBook(Book book){
        userBooks.addBook(book);
    }


}

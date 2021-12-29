package com.Aleksas.demo.entity;

public class Book {
    private String name;
    private String author;
    private String language;
    private String category;
    private String publicationDate;
    private long ISBN;
    private String GUID;

    public Book(String name, String author, String language, String category, String publicationDate, long isbn) {
        this.name = name;
        this.author = author;
        this.language = language;
        this.category = category;
        this.publicationDate = publicationDate;
        ISBN = isbn;
    }


    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getISBN() {
        return ISBN;
    }

    public void setISBN(long ISBN) {
        this.ISBN = ISBN;
    }

    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }
}

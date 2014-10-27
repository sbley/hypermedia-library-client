package de.saxsys.hypermedia.jsf;

import javax.enterprise.inject.Model;

@Model
public class Book {

    private String title;
    private String author;
    private String desc;

    public Book(String title, String author, String desc) {
        super();
        this.title = title;
        this.author = author;
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDesc() {
        return desc;
    }

}

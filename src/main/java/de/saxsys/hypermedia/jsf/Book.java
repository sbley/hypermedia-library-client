package de.saxsys.hypermedia.jsf;

import javax.enterprise.inject.Model;

@Model
public class Book {

    private String href;
    private String title;
    private String author;
    private String desc;
    private int borrower;

    public Book(String href, String title, String author, String desc, int borrower) {
        super();
        this.href = href;
        this.title = title;
        this.author = author;
        this.desc = desc;
        this.borrower = borrower;
    }

    public String getHref() {
        return href;
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

    public int getBorrower() {
        return borrower;
    }
}

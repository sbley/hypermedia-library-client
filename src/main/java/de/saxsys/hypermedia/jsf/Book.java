package de.saxsys.hypermedia.jsf;

import javax.enterprise.inject.Model;

import com.theoryinpractise.halbuilder.api.Link;

@Model
public class Book {

    private String href;
    private String title;
    private String author;
    private String desc;
    private Integer borrower;
    private Link relLend;
    private Link relReturn;

    public Book(String href, String title, String author, String desc, Link relLend, Link relReturn) {
        super();
        this.href = href;
        this.title = title;
        this.author = author;
        this.desc = desc;
        this.relLend = relLend;
        this.relReturn = relReturn;
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

    public Integer getBorrower() {
        return borrower;
    }

    public void setBorrower(Integer borrower) {
        this.borrower = borrower;
    }

    public Link getRelLend() {
        return relLend;
    }

    public Link getRelReturn() {
        return relReturn;
    }
}

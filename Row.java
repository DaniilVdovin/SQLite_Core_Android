package com.daniilvdovin.tableapp;

public class Row{
    public String
            article,
            product,
            description;
    public  int
            in_stock,
            price;

    public Row(String article, String product, int price, String description, int in_stock) {
        this.article = article;
        this.product = product;
        this.price = price;
        this.description = description;
        this.in_stock = in_stock;
    }
}

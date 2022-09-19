package org.exemple.magazin;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

public class Basket implements Serializable {
    private static final long serialVersionUID = 928815022945L;
    String[] products;
    int[] prices;
    int[] countOfProducts;

    public Basket(String[] products, int[] prices) {
        this.products = products;
        this.prices = prices;
        countOfProducts = new int[products.length];
    }

    public void addToCart(int productNum, int amount) {
        countOfProducts[productNum - 1] += amount;
    }

    public void printCart() {
        int sumProducts = 0;

        System.out.println("Ваша корзина:");

        for (int i = 0; i < products.length; i++) {
            if (countOfProducts[i] > 0) {
                sumProducts += countOfProducts[i] * prices[i];
                System.out.println(products[i] + " " + countOfProducts[i] + " шт " + prices[i] + " руб/шт. "
                        + countOfProducts[i] * prices[i] + " руб в сумме");
            }
        }
        System.out.println("Итого: " + sumProducts + " руб ");

    }

    public void saveToJSON (Basket basket){
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File("basket.json"), basket);
        } catch (IOException e) {
            System.out.println("файл не может быть прочитан");
            throw new RuntimeException(e);
        }
    }


}

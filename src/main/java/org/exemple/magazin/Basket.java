package org.exemple.magazin;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

public class Basket {

    String[] products;
    int[] prices;
    int[] countOfProducts;

    public Basket() {

    }

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

    public void saveToJSON(Basket basket) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File("basket.json"), basket);
        } catch (IOException e) {
            System.out.println("файл не может быть прочитан");
            throw new RuntimeException(e);
        }
    }

    public void saveTxt(File textFile) {
        try (BufferedWriter wr = new BufferedWriter(new FileWriter(textFile))) {
            for (String product : products) {
                wr.write(product + "&");
            }
            wr.write("\n");
            for (int price : prices) {
                wr.write(price + "&");
            }
            wr.write("\n");
            for (int count : countOfProducts) {
                wr.write(count + "&");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public static Basket loadFromTxtFile(File textFile) {
        Basket basket;
        String[] products = new String[0];
        int[] prices = new int[0];
        int[] countOfProducts = new int[0];
        try (BufferedReader br = new BufferedReader(new FileReader(textFile))) {
            String[] s;
            int lineNum = 0;
            while (br.ready()) {
                if (lineNum == 0) {
                    products = br.readLine().split("&");
                } else if (lineNum == 1) {
                    s = br.readLine().split("&");
                    prices = new int[s.length];
                    for (int i = 0; i < s.length; i++) {
                        prices[i] = Integer.parseInt(s[i]);
                    }

                } else {
                    s = br.readLine().split("&");
                    countOfProducts = new int[s.length];
                    for (int i = 0; i < s.length; i++) {
                        countOfProducts[i] = Integer.parseInt(s[i]);
                    }
                }
                lineNum++;
            }
            basket = new Basket(products, prices);
            for (int i = 0; i < countOfProducts.length; i++) {
                basket.addToCart((i + 1), countOfProducts[i]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
        return basket;
    }

    public String[] getProducts() {
        return products;
    }

    public int[] getPrices() {
        return prices;
    }

    public int[] getCountOfProducts() {
        return countOfProducts;
    }


}

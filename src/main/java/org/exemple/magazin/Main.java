package org.exemple.magazin;

import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String[] products = {"Хлеб", "Соль", "Мука", "Яйца", "Овсянка"};
        int[] prices = {27, 30, 33, 80, 70};

        String basket_file = "basket.json";
        File file = new File(basket_file);
        File txtFile = new File("log.csv");
        Basket basket;
        ClientLog clientLog = new ClientLog();
        if (file.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                basket = mapper.readValue(new File(basket_file), Basket.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            basket = new Basket(products, prices);
        }


        System.out.println("Список возможных товаров для покупки:");

        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + "." + " " + products[i] + " " + prices[i] + " " + "руб.");
        }

        while (true) {
            System.out.println("Выберите продукт и его количество или введите end для завершения");
            String input = scanner.nextLine();
            int productNumber = 0;
            int productCount = 0;

            if ("end".equals(input)) {
                clientLog.exportAsCSV(txtFile);
                break;
            }

            String[] parts = input.split(" ");
            try {
                productNumber = Integer.parseInt(parts[0]);
                productCount = Integer.parseInt(parts[1]);
            } catch (Exception e) {
                System.out.println("Некорректный ввод. Введите два числа.");
                continue;
            }
            basket.addToCart(productNumber, productCount);
            clientLog.log(productNumber, productCount);
            basket.saveToJSON(basket);
        }
        basket.printCart();
    }
}

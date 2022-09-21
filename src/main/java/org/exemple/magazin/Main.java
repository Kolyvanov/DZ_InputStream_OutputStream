package org.exemple.magazin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.*;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String[] products = {"Хлеб", "Соль", "Мука", "Яйца", "Овсянка"};
        int[] prices = {27, 30, 33, 80, 70};

        boolean loadEnable = false;
        String basketLoadFileName = null;
        String loadFileFormat = null;

        boolean saveEnable = false;
        String basketSaveFileName = null;
        String saveFileFormat = null;

        boolean logEnable = false;
        String logFileName = null;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc;
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new File("shop.xml"));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }

        String textLoadTag = doc.getElementsByTagName("load").item(0).getTextContent().replace(" ", "").trim();
        String[] loadText = textLoadTag.split("\n");
        if ("true".equals(loadText[0])) {
            loadEnable = true;
            basketLoadFileName = loadText[1];
            loadFileFormat = loadText[2];
        }

        String textSaveTag = doc.getElementsByTagName("save").item(0).getTextContent().replace(" ", "").trim();
        String[] saveText = textSaveTag.split("\n");
        if ("true".equals(saveText[0])) {
            saveEnable = true;
            basketSaveFileName = saveText[1];
            saveFileFormat = saveText[2];
        }

        String textLogTag = doc.getElementsByTagName("log").item(0).getTextContent().replace(" ", "").trim();
        String[] logText = textLogTag.split("\n");
        if ("true".equals(logText[0])) {
            logEnable = true;
            logFileName = logText[1];
        }

        Basket basket = null;

        if (loadEnable) {
            File file = new File(basketLoadFileName);
            if ("text".equals(loadFileFormat)) {
                basket = Basket.loadFromTxtFile(file);
            } else if ("json".equals(loadFileFormat)) {
                if (file.exists()) {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        basket = mapper.readValue(file, Basket.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    basket = new Basket(products, prices);
                }
            }
        } else {
            basket = new Basket(products, prices);
        }

        ClientLog clientLog = null;
        File txtFile = null;

        if (logEnable) {
            clientLog = new ClientLog();
            txtFile = new File(logFileName);
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
            if (logEnable) {
                clientLog.log(productNumber, productCount);
            }
            if (saveEnable) {
                if (saveFileFormat.equals("text")) {
                    basket.saveTxt(new File(basketSaveFileName));
                } else if (saveFileFormat.equals("json")) {
                    basket.saveToJSON(basket, basketSaveFileName);
                }
            }
        }

        basket.printCart();
        if (clientLog != null) {
            clientLog.exportAsCSV(txtFile);
        }
    }
}

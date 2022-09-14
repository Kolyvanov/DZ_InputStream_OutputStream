import java.io.File;
import java.util.Scanner;

public class Main {
    public static String basket_file = "basket.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] products = {"����", "����", "����", "����", "�������"};
        int[] prices = {27, 30, 33, 80, 70};

        File file = new File(basket_file);
        Basket basket;
        if (file.exists()) {
            basket = Basket.loadFromTxtFile(file);
        } else {
            basket = new Basket(products, prices);
        }


        System.out.println("������ ��������� ������� ��� �������:");

        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + "." + " " + products[i] + " " + prices[i] + " " + "���.");
        }

        while (true) {
            System.out.println("�������� ������� � ��� ���������� ��� ������� end ��� ����������");
            String input = scanner.nextLine();
            int productNumber = 0;
            int productCount = 0;

            if ("end".equals(input)) {
                break;
            }
            try {
                String[] parts = input.split(" ");
                productNumber = Integer.parseInt(parts[0]);
                productCount = Integer.parseInt(parts[1]);
            } catch (Exception e) {
                System.out.println("������������ ����. ������� ��� �����.");
                continue;
            }
                basket.addToCart(productNumber, productCount);

        }
        basket.printCart();


    }
}

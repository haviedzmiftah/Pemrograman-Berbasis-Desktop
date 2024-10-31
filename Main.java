import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Menu {
    String name;
    double price;
    String category;

    public Menu(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }
}

public class Main {
    static final double TAX_RATE = 0.1;
    static final double SERVICE_CHARGE = 20000.0;
    static final double DISCOUNT_THRESHOLD = 100000.0;
    static final double DRINK_DISCOUNT_THRESHOLD = 50000.0;
    static final double DISCOUNT_RATE = 0.1;

    static Menu[] menuItems = {
        new Menu("Nasi Padang", 30000, "Makanan"),
        new Menu("Ayam Penyet", 25000, "Makanan"),
        new Menu("Sate Ayam", 35000, "Makanan"),
        new Menu("Gado-Gado", 20000, "Makanan"),
        new Menu("Es Teh", 5000, "Minuman"),
        new Menu("Kopi", 15000, "Minuman"),
        new Menu("Jus Jeruk", 20000, "Minuman"),
        new Menu("Teh Manis", 7000, "Minuman")
    };

    static Map<String, Integer> orders = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        displayMenu();

        takeOrder(scanner, 1);
        takeOrder(scanner, 2);
        takeOrder(scanner, 3);
        takeOrder(scanner, 4);

        printReceipt();
    }

    static void displayMenu() {
        System.out.println("Daftar Menu Makanan:");
        for (int i = 0; i < 4; i++) {
            System.out.printf("%s (Rp %.2f)\n", menuItems[i].name, menuItems[i].price);
        }

        System.out.println("\nDaftar Menu Minuman:");
        for (int i = 4; i < menuItems.length; i++) {
            System.out.printf("%s (Rp %.2f)\n", menuItems[i].name, menuItems[i].price);
        }
    }

    static void takeOrder(Scanner scanner, int orderNumber) {
        System.out.printf("Masukkan pesanan %d (format: NamaMenu = jumlah), ketik 'selesai' untuk selesai:\n", orderNumber);
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("selesai")) {
            return;
        }

        String[] parts = input.split("=");
        if (parts.length != 2) {
            System.out.println("Format input salah, silakan coba lagi.");
            return;
        }

        String menuName = parts[0].trim();
        int quantity;
        try {
            quantity = Integer.parseInt(parts[1].trim());
            orders.put(menuName, orders.getOrDefault(menuName, 0) + quantity);
        } catch (NumberFormatException e) {
            System.out.println("Jumlah harus berupa angka, silakan coba lagi.");
        }
    }

    static void printReceipt() {
        double totalCost = 0.0;

        System.out.println("\nStruk Pesanan:");
        for (String menuName : orders.keySet()) {
            int quantity = orders.get(menuName);

            for (Menu item : menuItems) {
                if (item.name.equalsIgnoreCase(menuName)) {
                    double itemTotal = item.price * quantity;
                    totalCost += itemTotal;
                    System.out.printf("%s x %d = Rp %.2f\n", item.name, quantity, itemTotal);
                    break;
                }
            }
        }

        double tax = totalCost * TAX_RATE;
        double serviceCharge = SERVICE_CHARGE;
        double discount = 0.0;
        boolean drinkDiscountApplied = false;

        if (totalCost > DISCOUNT_THRESHOLD) {
            discount = totalCost * DISCOUNT_RATE;
        }
        
        if (totalCost > DRINK_DISCOUNT_THRESHOLD) {
            for (String menuName : orders.keySet()) {
                for (Menu item : menuItems) {
                    if (item.name.equalsIgnoreCase(menuName) && item.category.equals("Minuman")) {
                        drinkDiscountApplied = true;
                        break;
                    }
                }
            }
        }

        double totalAfterDiscount = totalCost + tax + serviceCharge - discount;
        if (drinkDiscountApplied) {
            totalAfterDiscount -= totalAfterDiscount / 2; // Beli satu gratis satu
        }

        System.out.printf("Subtotal: Rp %.2f\n", totalCost);
        System.out.printf("Pajak (10%%): Rp %.2f\n", tax);
        System.out.printf("Biaya Pelayanan: Rp %.2f\n", serviceCharge);
        
        if (discount > 0) {
            System.out.printf("Diskon: Rp %.2f\n", discount);
        }
        if (drinkDiscountApplied) {
            System.out.println("Penawaran: Beli satu gratis satu untuk minuman!");
        }
        
        System.out.printf("Total Biaya: Rp %.2f\n", totalAfterDiscount);
    }
}

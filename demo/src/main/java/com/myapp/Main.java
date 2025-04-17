package com.myapp;

import java.util.Scanner;

import com.myapp.controller.AccountController;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AccountController accountController = new AccountController();
        
        while (true) {
            System.out.println("\n--- My Bank App ---");
            System.out.println("1. Hesap Aç");
            System.out.println("2. Para Transferi");
            System.out.println("3. Hesap Bilgisi Görüntüle");
            System.out.println("0. Çıkış");
            System.out.print("Seçiminiz: ");
            
            String secim = scanner.nextLine();
            switch (secim) {
                case "1" -> accountController.createAccount(scanner);
                case "2" -> accountController.transferMoney(scanner);
                case "3" -> accountController.printAccountInfo(scanner);
                case "0" -> {
                    System.out.println("Çıkılıyor...");
                    return;
                }
                default -> System.out.println("Geçersiz seçim. Tekrar deneyin.");
            }
        }
    }
}

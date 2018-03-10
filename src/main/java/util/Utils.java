package util;

import java.util.Scanner;

/**
 * Created by Skillkiller on 10.03.2018.
 */
public class Utils {

    public static String pad(String message, int length) {
        if (message.length() == length - 1) {
            return message + " ";
        } else if(message.length() > length - 4) {
            return message.substring(0, length - 4) + "... ";
        } else {
            StringBuilder stringBuilder = new StringBuilder(message);
            for(int zwLaenge = message.length(); zwLaenge < length; zwLaenge++) {
                stringBuilder.append(" ");
            }

            return stringBuilder.toString();
        }
    }

    public static boolean userQuestionBoolean(String message, boolean fallback) {
        System.out.println(message);
        System.out.print(fallback ? "[Y/n] " : "[y/N] ");
        switch (new Scanner(System.in).nextLine().toLowerCase()) {
            case "ja":
            case "yes":
            case "j":
            case "y":
                return true;

            case "nein":
            case "no":
            case "n":
                return false;

                default:
                    return fallback;
        }
    }

    public static boolean userQuestionBoolean(String message) {
        System.out.println(message);
        System.out.print("[y/n]:");
        switch (new Scanner(System.in).nextLine().toLowerCase()) {
            case "ja":
            case "yes":
            case "j":
            case "y":
                return true;

            case "nein":
            case "no":
            case "n":
                return false;

            default:
                return userQuestionBoolean(message);
        }
    }

    public static String userQuestionString(String message) {
        System.out.println(message);
        System.out.print("Eingabe: ");
        return new Scanner(System.in).nextLine();
    }

    public static String userQuestionString(String message, int minlength) {
        System.out.println(message);
        System.out.print("Eingabe[min " + minlength + "]: ");
        String input = new Scanner(System.in).nextLine();
        if (input.length() >= minlength) {
            return input;
        } else {
            return userQuestionString(message, minlength);
        }
    }

    public static void UserQuestionOk(String message) {
        System.out.print(message);
        new Scanner(System.in).nextLine();
    }
}

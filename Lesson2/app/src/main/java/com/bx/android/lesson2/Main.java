package com.bx.android.lesson2;

import java.util.Arrays;

/**
 * Created by pjohnson on 2/23/17.
 */

public class Main {

    public static void main(String[] args) {
        insertionSortPart2(new int[]{1, 4, 3, 5, 6, 2});
    }

    public static void insertIntoSorted(int[] ar) {
        int v = ar[ar.length - 1];
        for (int i = ar.length - 1; i > 0; i--) {
            if (ar[i - 1] > v) {
                ar[i] = ar[i - 1];
                System.out.println(Arrays.toString(ar).replace(",", "").replaceAll("\\[|\\]|,", ""));
            } else {
                ar[i] = v;
                System.out.println(Arrays.toString(ar).replace(",", "").replaceAll("\\[|\\]|,", ""));

                break;
            }
        }
    }

    public static void insertionSortPart2(int[] ar) {
        for (int i = 1; i < ar.length; i++) {
            int n = ar[i];
            boolean inserted = false;
            for (int j = i; j > 0; j--) {
                if (ar[j - 1] > n) {
                    ar[j] = ar[j - 1];
                } else {
                    ar[j] = n;
                    inserted = true;
                    break;
                }
            }
            if (!inserted) {
                ar[0] = n;
            }
            printArray(ar);
        }
    }


    private static void printArray(int[] ar) {
        for (int n : ar) {
            System.out.print(n + " ");
        }
        System.out.println("");
    }
}

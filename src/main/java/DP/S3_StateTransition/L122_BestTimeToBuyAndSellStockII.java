package DP.S3_StateTransition;

import static Utils.Helpers.*;

/*
 * Best Time to Buy and Sell Stock II
 *
 * - Say you have an array for which the ith element is the price of a given stock on day i. Design an
 *   algorithm to find the maximum profit.
 *
 * - You may do as many transactions as you like (i.e., buy one and sell one share of the stock multiple times).
 *
 * - Note you may not engage in multiple transactions at the same time (i.e., you must sell the stock before
 *   you buy again).
 * */

public class L122_BestTimeToBuyAndSellStockII {
    public static int maxProfit(int[] prices) {
        return 0;
    }

    public static void main(String[] args) {
        log(maxProfit(new int[]{7, 1, 5, 3, 6, 4}));  // expects 7. [-, buy, sell, buy, sell, -]
        log(maxProfit(new int[]{1, 2, 3, 4, 5}));     // expects 4. [buy, -, -, -, sell]
        log(maxProfit(new int[]{7, 6, 4, 3, 1}));     // expects 0. no transaction.
    }
}
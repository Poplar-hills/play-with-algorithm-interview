package DP.S3_StateTransition;

import static Utils.Helpers.*;

/*
 * Best Time to Buy and Sell Stock with Cooldown
 *
 * - 给定一个数组，表示一只股票在某几天内的价格。设计一个使利益最大化的交易算法，条件：
 *   1. You may do as many transactions as you like (ie, buy one and sell one share of the stock multiple times).
 *   2. You can't engage in multiple transactions at the same time (ie, must sell the stock before you buy again).
 *   3. After you sell your stock, you cannot buy stock on next day. (ie, cooldown 1 day).
 * */

public class L309_BestTimeToBuyAndSellStockWithCooldown {
    /*
     * 解法1：DP（多状态递推）
     * - 思路：∵ 该问题具有明显的状态转移特点，且有4种行为能使状态发生改变 ∴ 可以采用 L198_HouseRobber 解法4的多状态递推的思路，
     *   即通过梳理不同行为对状态的影响写出不同状态的递推表达式。由题中可知，共有4种行为：buy、sell、hold1、hold0。由此进行梳理：
     *                               +-----> sell <------+
     *                               |        |          |  ↗—↘
     *                              buy ------|------> hold1   |
     *                               ↑        ↓  ↗—↘        ↖_↙
     *                               +----- hold0   |
     *                                           ↖_↙
     *   - 另外该问题符合最优子结构性质 —— 通过求前 i-1 天的最大收益可获得前 i 天的最大收益。
     *   - 由以上两点可写出状态递推表达式：
     *       buy[i] = hold0[i-1] - prices[i]                  // 第i天买入后的最大收益 = 前一天持有后的收益 - 第i天的股价
     *       sell[i] = max(hold1[i-1], buy[i-1]) + prices[i]  // 第i天卖出后的最大收益 = max(前一天持有后的收益, 前一天买入后的收益) + 第i天的股价
     *       hold1[i] = max(hold1[i-1], buy[i-1])
     *       hold0[i] = max(hold0[i-1], sell[i-1])
     *
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int maxProfit(int[] prices) {
        if (prices == null || prices.length < 2) return 0;

        int n = prices.length;
        int[] buy = new int[n];
        int[] sell = new int[n];
        int[] hold0 = new int[n];
        int[] hold1 = new int[n];

        buy[0] = -prices[0];    // 第0天买入后的收益
        sell[0] = 0;            // 第0天卖出后的收益（∵ 不可能第0天就卖出 ∴ 设为0）
        hold0[0] = 0;           // 第0天什么都不做的收益
        hold1[0] = -prices[0];  // 第0天持有1股的收益（∵ 不可能第0天就持有股票 ∴ 设为负的第0天的股价）

        for (int i = 1; i < n; i++) {  // 通过前一天的值递推后一天的值
            buy[i] = hold0[i - 1] - prices[i];
            sell[i] = Math.max(hold1[i - 1], buy[i - 1]) + prices[i];
            hold1[i] = Math.max(hold1[i - 1], buy[i - 1]);
            hold0[i] = Math.max(hold0[i - 1], sell[i - 1]);
        }

        return Math.max(sell[n - 1], hold0[n - 1]);  // 若要获得最大收益，则最后一天只能卖出或处于已经卖出状态
    }

    /*
     * 解法2：DP（解法1的空间优化版）
     * - 思路：在解法1的基础上 ∵ 每个数组都只用到 i-1 处的值 ∴ 不需要维护整个数组，只需记录单个值即可。
     * - 时间复杂度 O(n)，空间复杂度 O(1)。
     * */
    public static int maxProfit2(int[] prices) {
        if (prices == null || prices.length < 2) return 0;

        int lastBuy = -prices[0];
        int lastSell = 0;
        int lastHold1 = -prices[0];
        int lastHold0 = 0;

        for (int price : prices) {
            int buy = lastHold0 - price;
            int sell = Math.max(lastHold1, lastBuy) + price;
            int hold1 = Math.max(lastHold1, lastBuy);
            int hold0 = Math.max(lastHold0, lastSell);

            lastBuy = buy;
            lastSell = sell;
            lastHold1 = hold1;
            lastHold0 = hold0;
        }

        return Math.max(lastSell, lastHold0);
    }

    /*
     * 解法3：DP
     * - 思路：在 L123_BestTimeToBuyAndSellStockIII 解法2基础上进行改造：
     *   - ∵ 可以交易任意多次 ∴ 不再需要交易次数 k 的维度；
     *   - ∵ 在卖出之后需要 cooldown ∴ “能否做卖出操作”取决于前一天“是否在 cooldown”这一状态 ∴ 需要给 dp 数组增加“是否在
     *     cooldown”这一维度，即 maxProfit[d][c][h] 表示“第 d 天、是/否在 cooldown、是/否持有股票时所能获得的最大收益”。
     *   - ∴ 有递推表达式：
     *     maxProfit[d][0][0] = max(maxProfit[d-1][0][0], maxProfit[d-1][1][0])
     *     maxProfit[d][0][1] = max(maxProfit[d-1][0][1], maxProfit[d-1][0][0] - prices[d])
     *     maxProfit[d][1][0] = maxProfit[d-1][0][1] + prices[d]
     *     ∵ cooldown 一定发生在 sell 之后 ∴ 不会有 maxProfit[d][1][1] 的状态。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static int maxProfit3(int[] prices) {
        if (prices == null || prices.length < 2) return 0;

        int n = prices.length;
        int[][][] dp = new int[n][2][2];  // dp[d][c][h] 表示“第 d 天、是/否需要 cooldown、是/否持有股票时的最大收益”
        dp[0][0][1] = -prices[0];

        for (int d = 1; d < n; d++) {
            dp[d][0][0] = Math.max(dp[d-1][0][0], dp[d-1][1][0]);
            dp[d][0][1] = Math.max(dp[d-1][0][1], dp[d-1][0][0] - prices[d]);
            dp[d][1][0] = dp[d-1][0][1] + prices[d];
        }

        return Math.max(dp[n-1][0][0], dp[n-1][1][0]);
    }

    public static void main(String[] args) {
        log(maxProfit3(new int[]{1, 2, 3, 0, 2}));  // expects 3. [buy, sell, hold0, buy, sell]
        log(maxProfit3(new int[]{1, 4, 2}));        // expects 3. [buy, sell, hold0]
        log(maxProfit3(new int[]{1, 2}));           // expects 1. [buy, sell]
        log(maxProfit3(new int[]{2, 1}));           // expects 0. [hold0, hold0]
        log(maxProfit3(new int[]{3, 3}));           // expects 0. [hold0, hold0] or [buy, sell]
    }
}

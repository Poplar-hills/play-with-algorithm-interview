package RecursionAndBackTracking.Permutations;

import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.List;

/*
 * Permutations
 *
 * - Given a collection of distinct integers, return all possible permutations.
 * */

public class L46_Permutations {
    /*
     * 解法1：
     * - 思路：采用类似 L17_LetterCombinationsOfPhoneNumber 解法2的思路，对于 nums 中的每个元素，都放到 res 中的每个列表
     *   里的每个插入点上，生成一个新的排列。例如，对于 [1,2,3] 来说：
     *                           /--> [3,2,1]
     *                         3/
     *                 /-> [2,1] -3-> [2,3,1]
     *                /        3\
     *         1    2/           \--> [2,1,3]
     *     [] --> [1]
     *              2\           /--> [3,1,2]
     *                \        3/
     *                 \-> [1,2] -3-> [1,3,2]
     *                         3\
     *                           \--> [1,2,3]
     *
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums.length == 0) return res;
        res.add(new ArrayList<>());                        // 需要一个 trigger 元素

        for (int n : nums) {
            List<List<Integer>> temp = new ArrayList<>();
            for (List<Integer> list : res) {
                for (int j = 0; j <= list.size(); j++) {   // 遍历 list 的每一个插入点（包括尾部的插入点）
                    List<Integer> newList = new ArrayList<>(list);
                    newList.add(j, n);                     // 往复制出来的 list 的插入点 j 处放入元素 n
                    temp.add(newList);
                }
            }
            res = temp;                                    // 替换原 res
        }

        return res;
    }

    /*
     * 解法2：
     * - 思路：采用 L17_LetterCombinationsOfPhoneNumber 解法3的思路
     * - 时间复杂度 O()，空间复杂度 O()。
     * */
    public static List<List<Integer>> permute2(int[] nums) {
        return null;
    }

    public static void main(String[] args) {
        log(permute(new int[]{1, 2, 3}));  // expects [[1,2,3], [1,3,2], [2,1,3], [2,3,1], [3,1,2], [3,2,1]]
        log(permute(new int[]{1, 2}));     // expects [[1,2], [2,1]]
        log(permute(new int[]{1}));        // expects [[1]]
        log(permute(new int[]{}));         // expects []
    }
}

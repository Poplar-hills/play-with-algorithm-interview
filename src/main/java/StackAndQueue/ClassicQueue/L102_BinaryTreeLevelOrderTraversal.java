package StackAndQueue.ClassicQueue;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static Utils.Helpers.*;

/*
* Binary Tree Level Order Traversal
*
* - Given a binary tree, return the level order traversal of its nodes' values. (ie, from left to right, level by level).
* */

public class L102_BinaryTreeLevelOrderTraversal {
    /*
    * 复习1：二叉树非递归层序遍历，用于和解法1进行对比。
    * */
    public static List<Integer> simpleLevelOrder(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Queue<TreeNode> q = new LinkedList<>();
        if (root == null) return res;

        q.offer(root);
        while (!q.isEmpty()) {
            TreeNode curr = q.poll();
            res.add(curr.val);
            if (curr.left != null) q.offer(curr.left);
            if (curr.right != null) q.offer(curr.right);
        }
        return res;
    }

    /*
    * 解法1：迭代
    * - 思路：在复习1的基础上实现，区别在于队列中以 Pair 形式同时保存节点和节点的层级信息。
    * - 时间复杂度 O(n)，空间复杂度 O(n)。
    * */
    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        Queue<Pair<TreeNode, Integer>> q = new LinkedList<>();  // 因为结果要求同一层的节点放在一个列表中，因此这里队列中除了保存节点之外还需要保存层级信息
        if (root == null) return res;

        q.offer(new Pair<>(root, 0));  // 层数从0开始
        while (!q.isEmpty()) {
            Pair<TreeNode, Integer> pair = q.poll();
            TreeNode node = pair.getKey();
            Integer level = pair.getValue();

            if (level == res.size()) {  // 此时需要在 res 中创建新的列表存储新一层的节点值（比如上面 q.poll 出来的是 level = 0 的根节点，此时 res 中还没有任何列表，因此需要创建）
                List<Integer> l = new ArrayList<>();
                l.add(node.val);
                res.add(l);
            } else {                    // 若 level < res.size()，则说明 res 中已存在这一层的列表，直接将节点值推进即可
                res.get(level).add(node.val);
            }

            if (node.left != null) q.offer(new Pair<>(node.left, level + 1));
            if (node.right != null) q.offer(new Pair<>(node.right, level + 1));
        }

        return res;
    }

    /*
    * 复习2：二叉树递归层序遍历，用于和解法2进行对比。
    * */
    public static List<Integer> simpleLevelOrder2(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        res.add(root.val);
        simpleLevelOrder2(root, res);
        return res;
    }

    private static void simpleLevelOrder2(TreeNode node, List<Integer> res) {
        if (node.left != null)
            res.add(node.left.val);
        if (node.right != null)
            res.add(node.right.val);
        if (node.left != null)
            simpleLevelOrder2(node.left, res);
        if (node.right != null)
            simpleLevelOrder2(node.right, res);
    }

    public static void main(String[] args) {
        TreeNode t = createBinaryTreeBreadthFirst(new Integer[]{3, 9, 20, null, 8, 15, 7});

        log(simpleLevelOrder(t));  // expects [3, 9, 20, 8, 15, 7]
        log(simpleLevelOrder2(t));  // expects [3, 9, 20, 8, 15, 7]

        log(levelOrder(t));  // expects [[3], [9,20], [8,15,7]]
//        log(levelOrder2(t));  // expects [[3], [9,20], [8,15,7]]
    }
}

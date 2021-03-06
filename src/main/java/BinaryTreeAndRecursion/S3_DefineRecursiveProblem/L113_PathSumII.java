package BinaryTreeAndRecursion.S3_DefineRecursiveProblem;

import static Utils.Helpers.createBinaryTreeBreadthFirst;
import static Utils.Helpers.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import Utils.Helpers.TreeNode;

/*
 * Path Sum II
 *
 * - Given a binary tree and a sum, find all root-to-leaf paths where each path's sum equals the given sum.
 *
 * - Path Sum 系列：
 *   - L112_PathSum 判断是否存在节点和为 sum 的 root-to-leaf 路径；
 *   - L113_PathSumII 打印所有节点和为 sum 的 root-to-leaf 路径；
 *   - L437_PathSumIII 统计节点和为 sum 的路径个数（不管是否是 root-to-leaf 都算）。
 * */

public class L113_PathSumII {
    /*
     * 解法1：DFS (Pre-order traversal)
     * - 思路：从根节点开始递归生成路径 path，若到达叶子节点且剩余 sum 为0，则说明该 path 是符合要求的路径，添加到结果集 res 中。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static List<List<Integer>> pathSum(TreeNode root, int sum) {
        List<List<Integer>> res = new ArrayList<>();
        helper(root, sum, new ArrayList<>(), res);
        return res;
    }

    private static void helper(TreeNode root, int sum, List<Integer> path, List<List<Integer>> res) {
        if (root == null) return;
        path.add(root.val);

        if (root.left == null && root.right == null && root.val == sum) {
            res.add(path);
            return;
        }
        helper(root.left, sum - root.val, new ArrayList<>(path), res);
        helper(root.right, sum - root.val, new ArrayList<>(path), res);
	}

    /*
     * 解法2：DFS + Backtracking (Pre-order traversal) (解法1的性能优化版)
     * - 思路：与解法1一致。
     * - 实现：与解法1不同之处在于该解法：
     *   1. 使用回溯技巧使得 path 从始至终都是复用的 —— ∵ 递归会先往左下递归到底再返回上层递归右子树 ∴ 若要继续复用 path 对象，
     *      则需在递归返回上一层之前将 path 恢复原状（这也是回溯的关键）；
     *   2. 只有在确定该 path 符合条件时才会被复制进 res（这也是该解法比其他解法快的原因）。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static List<List<Integer>> pathSum2(TreeNode root, int sum) {
        List<List<Integer>> res = new ArrayList<>();
        helper2(root, sum, new ArrayList<>(), res);
        return res;
    }

    private static void helper2(TreeNode root, int sum, List<Integer> path, List<List<Integer>> res) {
        if (root == null) return;
        path.add(root.val);

        if (root.left == null && root.right == null && root.val == sum) {
            res.add(new ArrayList<>(path));  // 若是符合要求的 path，则复制进 res 里
            path.remove(path.size() - 1);    // 返回上层递归之前将添加的元素移除，让 path 恢复原状，这样回到上层后才能继续复用 path
            return;
        }
        helper2(root.left, sum - root.val, path, res);  // 则继续递归并复用 path
        helper2(root.right, sum - root.val, path, res);
        path.remove(path.size() - 1);        // 同样在返回上层递归之前要将 path 恢复原状
    }

    /*
     * 解法3：DFS + Concat path (Post-order traversal)
     * - 思路：
     *   1. 不同于解法1、2中的递归是从上到下的，该解法采用从下到上的递归思路，即先递归到底，找到符合要求的 path，然后在回程路上
     *      开始自底向上拼接 path（∴ 是 Post-order traversal，与 L257_BinaryTreePaths 解法1一致）；
     *   2. ∵ 递归思路不同 ∴ 递归函数的语义也与不同：f(n, sum) 返回以 n 为根的二叉树上节点值之和为 sum 的 root-to-leaf paths。
     *   👉 采用从上到下、从下到上两种递归思路的题目还有 L111_MinimumDepthOfBinaryTree。
     * - 💎 总结：对比解法2、3可加深对前序、后续遍历的理解。
     * - 时间复杂度 O(n)，空间复杂度 O(h)，其中 h 为树高（平衡树时 h=logn；退化为链表时 h=n）。
     * */
    public static List<List<Integer>> pathSum3(TreeNode root, int sum) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;

        if (root.left == null && root.right == null && sum == root.val) {  // 若找到解则创建 path 并塞入 res
            List<Integer> path = new ArrayList<>();
            path.add(root.val);
            res.add(path);
            return res;
        }

        List<List<Integer>> leftPaths = pathSum3(root.left, sum - root.val);  // 分别对左右子树进行递归
        List<List<Integer>> rightPaths = pathSum3(root.right, sum - root.val);

        return Stream.of(leftPaths, rightPaths)
            .flatMap(Collection::stream)      // 将左右子树返回的结果合起来（或者 leftPaths.addAll(rightPaths) 也可以）
            .map(path -> { path.add(0, root.val); return path; })  // 向连接后的 res 中的每个 path 头部添加当前节点值
            .collect(Collectors.toList());
    }

	/*
     * 解法4：DFS (Iteration) (解法1的迭代版)
     * - 思路：与 L257_BinaryTreePaths 解法3一致。
     * - 同理：只需将 Stack 替换为 Queue 就得到了 BFS 解法。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> pathSum4(TreeNode root, int sum) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;

        Stack<Pair<List<TreeNode>, Integer>> stack = new Stack<>();  // 保存 <path 节点列表, path 节点之和>
        List<TreeNode> initialList = new ArrayList<>();
        initialList.add(root);
        stack.push(new Pair<>(initialList, root.val));

        while (!stack.isEmpty()) {
            Pair<List<TreeNode>, Integer> pair = stack.pop();
            List<TreeNode> list = pair.getKey();
            int currSum = pair.getValue();
            TreeNode lastNode = list.get(list.size() - 1);

            if (currSum == sum && lastNode.left == null && lastNode.right == null) {
                List<Integer> valList = list.stream().map(n -> n.val).collect(Collectors.toList());  // 将节点列表转化为整型列表
                res.add(valList);
                continue;
            }

            Consumer<TreeNode> fn = node -> {
                List<TreeNode> newList = new ArrayList<>(list);
                newList.add(node);
                stack.push(new Pair<>(newList, currSum + node.val));
            };

            if (lastNode.left != null) fn.accept(lastNode.left);
            if (lastNode.right != null) fn.accept(lastNode.right);
        }

        return res;
    }

	/*
     * 解法5：DFS (Iteration) (解法4的性能优化版)
     * - 思路：与解法4一致。
     * - 实现：
     *   1. 与解法4的不同点在于：stack 中不再存储由节点组成的 List<TreeNode> 而是存储由节点值组成的 List<Integer>，这样
     *      在找到解之后无需再像解法4中那样遍历取出每个节点的值；
     *   2. 但也因此需要单独记录每个 path 的最后一个节点 lastNode；
     *   3. ∴ 将节点值列表、path 最后一个节点、path 当前的 sum 封装到 Path 类中。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    private static class Path {
        List<Integer> valList;
        TreeNode lastNode;
        int currSum;

        public Path(List<Integer> valList, TreeNode lastNode, int currSum) {
            this.valList = valList;
            this.lastNode = lastNode;
            this.currSum = currSum;
        }
    }

    public static List<List<Integer>> pathSum5(TreeNode root, int sum) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;

        Stack<Path> stack = new Stack<>();
        List<Integer> initialList = new ArrayList<>();
        initialList.add(root.val);
        stack.push(new Path(initialList, root, root.val));

        while (!stack.isEmpty()) {
            Path path = stack.pop();
            TreeNode lastNode = path.lastNode;

            if (path.currSum == sum && lastNode.left == null && lastNode.right == null) {
                res.add(path.valList);
                continue;
            }

            Consumer<TreeNode> fn = node -> {
                List<Integer> newList = new ArrayList<>(path.valList);
                newList.add(node.val);
                stack.push(new Path(newList, node, path.currSum + node.val));
            };

            if (lastNode.left != null) fn.accept(lastNode.left);
            if (lastNode.right != null) fn.accept(lastNode.right);
        }

        return res;
    }

	/*
     * 解法6：DFS (Iteration, post-order traversal)
     * - 思路：采用后续遍历的思路，在往下遍历的过程中形成 path。
     * - 实现：若到达叶子节点时 sum=0，则该 path 有效，此时将 path 中的最后一个元素去除，恢复 path 在父节点时的状态，转而去
     *   访问右子树（有点类似回溯）。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static List<List<Integer>> pathSum6(TreeNode root, int sum) {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> path = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<TreeNode>();

        int currSum = 0;
        TreeNode prev = null, curr = root;

        while (curr != null || !stack.isEmpty()) {
            while (curr != null) {  // 先往左遍历到最左节点（不一定是叶子节点），一路上入栈每个节点、记录路径、累加节点值
                stack.push(curr);
                path.add(curr.val);
                currSum += curr.val;
                curr = curr.left;
            }
            curr = stack.peek();    // 取栈顶节点
            if (curr.right != null && curr.right != prev) {  // 若栈顶节点有右子树，且右子树没访问过
                curr = curr.right;                           // 转向开始用同样的方式处理右子树
                continue;
            }
            if (curr.left == null && curr.right == null && currSum == sum)  // 若到达叶子节点，将该路径放入结果集
                res.add(new ArrayList<>(path));
            prev = curr;                   // 访问完当前节点后，用 prev 标记其为已访问
            stack.pop();                   // 将当前节点出栈
            path.remove(path.size() - 1);  // 将 path 恢复到父节点的状态（因为要复用）
            currSum -= curr.val;           // 将 currSum 也恢复到父节点的状态
            curr = null;
        }

        return res;
    }

    public static void main(String[] args) {
        TreeNode t1 = createBinaryTreeBreadthFirst(new Integer[]{1, 2, 3, 6, null, 5, -2, 2, 8, null, null, 7, 9});
        log(pathSum5(t1, 9));
        /*
         * expects [[1,3,-2,7], [1,3,5]].（注意 [1,2,6] 不是）
         *            1
         *           / \
         *          2   3
         *         /   / \
         *        6   5  -2
         *       / \     / \
         *      2   8   7   9
         * */

        TreeNode t2 = createBinaryTreeBreadthFirst(new Integer[]{});
        log(pathSum5(t2, 1));
        /*
         * expects [].
         * */
    }
}

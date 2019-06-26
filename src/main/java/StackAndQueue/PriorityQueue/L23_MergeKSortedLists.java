package StackAndQueue.PriorityQueue;

import java.util.*;

import static Utils.Helpers.*;

/*
* Merge k Sorted Lists
*
* - Merge k sorted linked lists and return it as one sorted list. Analyze and describe its complexity.
* */

public class L23_MergeKSortedLists {
    /*
     * 解法1：PriorityQueue 全排序
     * - 思路：该题的本质是排序，因此容易想到将 lists 中的所有链表的所有节点进行排序，排序方案有：
     *   1. 借助 PriorityQueue 进行堆排序；
     *   2. 借助 TreeSet 进行 BST 排序（但不适用该题，∵ BST 不允许重复节点，且该题也不像 L347 解法3那样在 value 相同的情况下可根据 key 排序）；
     *   3. 直接排序
     *   - 该解法使用 PriorityQueue 进行堆排序。
     * - 时间复杂度 O(nlogn)，空间复杂度 O(n)。其中 n 为所有节点数。
     * */
    public static ListNode mergeKLists(ListNode[] lists) {
        PriorityQueue<ListNode> pq = new PriorityQueue<>(Comparator.comparingInt(a  ->  a.val));  // 创建最小堆
        for (ListNode l : lists)  // 将 lists 中的所有链表的所有节点添加到 pq 中，O(nlogn) 操作
            for (ListNode curr = l; curr != null; curr = curr.next)
                pq.offer(curr);

        ListNode dummyHead = new ListNode(), curr = dummyHead;
        while (!pq.isEmpty()) {
            curr.next = pq.poll();
            curr = curr.next;
        }
        curr.next = null;  // ∵ 是将 lists 中的各个链表的节点重新拼接 ∴ 需要将拼接后的链表的最后一个节点的 next 置空，否则可能成环

        return dummyHead.next;
    }

    /*
     * 解法2：merge sort
     * - 思路：另一种 intuitive 的排序方案就是 merge sort。因为 lists 可能有2个以上的链表，因此可采用 reduce 的思路，即将多个链表
     *   的归并 reduce 成两两链表的归并（或者说是 generalize merge sort to sort k arrays）。
     * - todo: 时间复杂度怎么分析？？？时间复杂度 O(?)，空间复杂度 O(n)，（其中 n 为 lists 中所有链表的节点数之和）。
     * */
    public static ListNode mergeKLists2(ListNode[] lists) {
        if (lists.length == 0) return null;
        ListNode merged = lists[0];
        for (int i = 1; i < lists.length; i++)  // 将 lists 中的所有链表 reduce 成一个链表
            merged = merge2List(merged, lists[i]);
        return merged;
    }

    private static ListNode merge2List(ListNode l1, ListNode l2) {
        ListNode dummyHead = new ListNode(), curr = dummyHead;

        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                curr = curr.next = l1;  // 从右往左赋值，相当于 curr.next = l1; curr = curr.next;
                l1 = l1.next;
            } else {
                curr = curr.next = l2;
                l2 = l2.next;
            }
        }
        if (l1 != null) curr.next = l1;
        if (l2 != null) curr.next = l2;

        return dummyHead.next;
    }

    /*
     * 解法3：merge sort (改进版)
     * - 思路：解法2中，将多个链表 reduce 成一个的过程不是二分的（即不是将 lists 中的链表两两 merge，而是每个链表都和上一次 merge 的
     *   结果进行 merge），因此效率较低，该解法中对此进行改进 -- Should pair up lists and merge them without handling any
     *   list more than once。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。其中 n 为所有节点数。
     * */
    public static ListNode mergeKLists3(ListNode[] lists) {
        int len = lists.length;
        if (len == 0) return null;

        while (len > 1) {         // 当 len = 1 时说明已经 merge 了所有链表
            for (int i = 0; i < len / 2; i++)  // 遍历 lists 中的前一半的链表
                lists[i] = merge2List(lists[i], lists[len - 1 - i]);  // 对第 i 个和倒数第 i 个链表进行 merge，并将结果放回 i 上
            len = (len + 1) / 2;  // merge 完后将 len 砍半（注意 len 要+1，因为若 len 为奇数，则经过上面的 for 循环后，lists 中间
        }                         // 的链表没有和其他链表进行过 merge，因此需要放到下一轮中继续 merge）
        return lists[0];
    }

    /*
     * 解法4：使用 PriorityQueue 模拟 merge sort
     * - 思路：与解法1不同，该解法不将所有节点放入 PriorityQueue 中进行排序，而是只放入 k 个节点，但每个节点实际上都是一个链表。
     *   在 PriorityQueue 中我们只比较链表的首节点大小，不断将 k 个节点中最小的出队，再将其下一个节点入队，以此方式模拟 merge sort
     *   的过程。
     * - 时间复杂度 O(nlogk)，空间复杂度 O(k)。其中 k 为链表个数，n 为所有节点数。
     *   -
     * */
    public static ListNode mergeKLists4(ListNode[] lists) {
        PriorityQueue<ListNode> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.val));
        for (ListNode l : lists)    // 把 k 个链表装入最小堆中，因此堆操作的复杂度为 O(logk)
            if (l != null)          // 跳过 lists 中可能存在的空链表
                pq.offer(l);        // 链表（实际上是链表的首节点）装入堆中后会进行排序

        ListNode dummyHead = new ListNode(), curr = dummyHead;
        while (!pq.isEmpty()) {     // 该循环会遍历所有节点，且每次会有出队、入队操作，因此是 O(nlogk)
            curr.next = pq.poll();  // 吐出 k 个链表中首节点最小的那个，接到 curr 后面
            curr = curr.next;
            if (curr.next != null) pq.offer(curr.next);  // 将第二个节点装入堆中
        }

        return dummyHead.next;
    }

    public static void main(String[] args) {
        ListNode l1 = createLinkedListFromArray(new int[]{1, 4, 5});
        ListNode l2 = createLinkedListFromArray(new int[]{1, 3, 4});
        ListNode l3 = createLinkedListFromArray(new int[]{2, 6});
        ListNode res = mergeKLists4(new ListNode[]{l1, l2, l3});
        printLinkedList(res);  // expects 1 -> 1 -> 2 -> 3 -> 4 -> 4 -> 5 -> 6

        ListNode l4 = createLinkedListFromArray(new int[]{-2, -1, -1, -1});
        ListNode l5 = createLinkedListFromArray(new int[]{});
        ListNode res2 = mergeKLists4(new ListNode[]{l4, l5});
        printLinkedList(res2);  // expects -2 -> -1 -> -1 -> -1
    }
}

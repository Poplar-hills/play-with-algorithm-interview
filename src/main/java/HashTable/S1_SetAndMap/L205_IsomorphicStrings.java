package HashTable.S1_SetAndMap;

import static Utils.Helpers.log;

import java.util.HashMap;
import java.util.Map;

/*
* Isomorphic Strings
*
* - 判断两个字符串是否同构（isomorphic），即是否可以通过替换 s 中的字符来得到 t。
* */

public class L205_IsomorphicStrings {
    /*
     * 解法1：双查找表
     * - 思路：根据题意中的“同构”和对 test case 4 的纸上演算可知，若只用一个 Map 记录 s -> t 的字符映射是不够的，同时还需要
     *   记录 t -> s 的字符映射，保证双向都能匹配上才行（例如 test case 4）∴ 使用双查找表。
     * - 时间复杂度 O(n)，空间复杂度 O(n)。
     * */
    public static boolean isIsomorphic(String s, String t) {
        return helper(s, t, new HashMap<>(), new HashMap<>());
    }

    private static boolean helper(String s, String t, Map<Character, Character> sMap, Map<Character, Character> tMap) {
        if (s.length() == 0 && t.length() == 0) return true;
        char sc = s.charAt(0), tc = t.charAt(0);
        if (!sMap.containsKey(sc) && !tMap.containsKey(tc)) {
            sMap.put(sc, tc);
            tMap.put(tc, sc);
        }
        else if ((sMap.containsKey(sc) && sMap.get(sc) != tc) || (tMap.containsKey(tc) && tMap.get(tc) != sc))  // 若只有单向匹配上了则不是同构
            return false;
        return helper(s.substring(1), t.substring(1), sMap, tMap);  // 这里采用截取字符串的方式，也可以传递索引 i
    }

    /*
     * 解法2：双查找表（解法1的 char[256] 版）
     * - 思路：与解法1一致。
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
     * */
    public static boolean isIsomorphic2(String s, String t) {
        if (s.length() != t.length()) return false;

        char[] sMap = new char[256];
        char[] tMap = new char[256];

        for (int i = 0; i < s.length(); i++) {
            char sc = s.charAt(i), tc = t.charAt(i);
            if (sMap[sc] == 0 && tMap[tc] == 0) {  // char[] 的默认值是'\u0000'，即十进制的0 ∴ 可以直接用 == 0 判断
                sMap[sc] = tc;
                tMap[tc] = sc;
            }
            else if (sMap[sc] != tc || tMap[tc] != sc)  // 若已记录过，但记录中的频次不一样，则说明不是 isomorphic
                return false;
        }

        return true;
    }

    /*
     * 解法3：双查找表
     * - 思路：不同于解法1、2中将 s、t 中的字符互相映射，该解法将 s、t 中的字符映射到索引+1上（∵ 不能映射成0，否则会跟 int[]
     *   的默认值一样），这样一来，每次只需检查 s、t 对应位置上的字符是被否映射到了相同的数字上即可：
     *       例如对于 s="egg", t="add"：       而对于 s="aba", t="baa"：
     *          e -> 1 <- a                      a ->    1   <- b    - sMap[a]=1，tMap[b]=1
     *          g -> 2 <- d                      b ->    2   <- a    - sMap[b]=2，tMap[a]=2
     *          g -> 3 <- d                      a -> 1 != 2 <- a    - sMap[a] != tMap[a] ∴ return false
     * - 时间复杂度 O(n)，空间复杂度 O(len(charset))。
     * */
    public static boolean isIsomorphic3(String s, String t) {
        if (s.length() != t.length()) return false;

        int[] sMap = new int[256];
        int[] tMap = new int[256];

        for (int i = 0; i < s.length(); i++) {
            char sc = s.charAt(i), tc = t.charAt(i);
            if (sMap[sc] != tMap[tc]) return false;
            sMap[sc] = i + 1;  // （这里包含一个隐式转换：sc 是 char，sMap[sc] 是在去 sc 的 ASCII 值）
            tMap[tc] = i + 1;
        }

        return true;
    }

    /*
     * 解法4：单查找表
     * - 思路：对于 s 和 t 中的每个字符 s[i]、t[i] 来说，若他们上次在字符串中出现的索引相同，即说明 s 和 t 是 isomorphic 的。
     * - 实现：一个查找表分成两部分使用，两部分分别记录 s[i] -> i 和 t[i] -> i 的映射。
     * - 时间复杂度 O(n)，空间复杂度 O(2len(charset))。
     * */
    public static boolean isIsomorphic4(String s, String t) {
        int[] map = new int[512];      // ∵ 要分成两部分使用 ∴ 无法用 Map 实现
        for (int i = 0; i < s.length(); i++) {
            if (map[s.charAt(i)] != map[t.charAt(i) + 256])
                return false;
            map[s.charAt(i)] = map[t.charAt(i) + 256] = i + 1;  // 记录索引（+1 是因为要避免0，因为 int[] 的默认值是0）
        }
        return true;
    }

    public static void main(String[] args) {
        log(isIsomorphic3("egg", "add"));      // expects true
        log(isIsomorphic3("paper", "title"));  // expects true
        log(isIsomorphic3("foo", "bar"));      // expects false
        log(isIsomorphic3("ab", "aa"));        // expects false
        log(isIsomorphic3("aba", "baa"));      // expects false
    }
}
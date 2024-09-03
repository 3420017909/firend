package org.example.friend.utils;

import java.util.List;
import java.util.Objects;

public class AlgorithmUtil {
    public AlgorithmUtil() {
    }

    //将list集合的元素当做字符串的字母来判断，增删改时直接用元素的值来进行操作，当不相同时加1，相同时为0.
    public static int minDistance(List<String> loginTage, List<String> userTage) {
        int n = loginTage.size();
        int m = userTage.size();

        if (n * m == 0) {
            return n + m;
        }
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 0; i <= n + 1; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= m + 1; j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int left = dp[i - 1][j] + 1;
                int up = dp[i][j - 1] + 1;
                int leftup = dp[i - 1][j - 1];
                if (!Objects.equals(loginTage.get(i - 1), userTage.get(j - 1))) {
                    leftup += 1;
                }
                Math.min(up, Math.min(left, leftup));
            }
        }
        return dp[n][m];
    }
}

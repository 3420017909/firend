package org.example.friend.constant;

public class BloomFilterConstant {

    /**
     * 用户布隆前缀
     */
    public static final String USER_BLOOM_PREFIX = "super:user:name:";
    /**
     * 队伍布隆前缀
     */
    public static final String TEAM_BLOOM_PREFIX = "super:team:name:";
    /**
     * 博客布隆前缀
     */
    public static final String BLOG_BLOOM_PREFIX = "super:blog:name:";

    /**
     * 预先打开的最大包含记录
     */
    public static final int PRE_OPENED_MAXIMUM_INCLUSION_RECORD = 2000;

    /**
     * 预期包含记录
     */
    public static final int EXPECTED_INCLUSION_RECORD = 1000;

    /**
     * 散列函数数
     */
    public static final int HASH_FUNCTION_NUMBER = 2;
}

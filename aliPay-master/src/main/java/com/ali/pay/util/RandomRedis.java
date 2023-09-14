package com.ali.pay.util;

import java.util.Random;

public class RandomRedis {
    public static int RandomRedis_second(int random_num, int start_second){
        Random random = new Random();
        int randomSeconds = random.nextInt(random_num); // 生成0到59之间的随机秒数
        return start_second + randomSeconds;
    }
}

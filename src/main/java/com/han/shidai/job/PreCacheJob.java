package com.han.shidai.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.han.shidai.model.domain.User;
import com.han.shidai.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.han.shidai.contant.UserConstant.*;

/**
 * 缓存预热任务
 *
 * @author han
 */
@Component
@Slf4j
public class PreCacheJob {
    @Resource
    UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;
    // 重点用户
    private List<Long> mainUserList = Arrays.asList(1L);

    //每天执行，预热推荐用户
    @Scheduled(cron = "10 35 13 * * *")
    public void doCacheRecommendUser() {
        RLock lock = redissonClient.getLock(SHIDAI_PRECACHEJOB_DOCACHE_LOCK);
        try {
            //只有一个线程能获取到锁
            if (lock.tryLock(0,10000, TimeUnit.MILLISECONDS)) {
                System.out.println("getLock:"+Thread.currentThread().getId());
                for(Long userId:mainUserList){
                    //查数据库
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapper);
                    String redisKey = String.format(SHIDAI_USER_RECOMMEND+ userId);
                    ValueOperations valueOperations = redisTemplate.opsForValue();
                    //写缓存,30s过期
                    try {
                        valueOperations.set(redisKey, userPage, 30000, TimeUnit.MILLISECONDS);
                    } catch (Exception e) {
                        log.error("redis set key error", e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("doCacheRecommendUser error",e);
            throw new RuntimeException(e);
        } finally {
            //是否是自己的锁，只能释放自己的锁
            if (lock.isHeldByCurrentThread()){
                System.out.println("unlock:"+Thread.currentThread().getId());
                lock.unlock();
            }
        }
    }
}

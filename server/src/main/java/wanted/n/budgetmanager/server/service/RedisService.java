package wanted.n.budgetmanager.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate stringRedisTemplate;

    private final static String KEY_REFRESH_TOKEN = "refreshToken: ";
    private final static String KEY_BUDGET_USERS_RATE = "budgetUserRate: ";

    private final static Integer REFRESH_TOKEN_TTL_MINUTES = 60 * 60; // TTL 1시간으로 설정
    private final static Integer BUDGET_USERS_RATE_MINUTES = 60 * 60; // TTL 1시간으로 설정

    @Transactional
    public void saveRefreshToken(Long id, String refreshToken) {
        // key : id
        String key = KEY_REFRESH_TOKEN + id.toString();

        stringRedisTemplate.opsForValue().set(key, refreshToken);
        // TTL 3시간 설정
        stringRedisTemplate.expire(key, REFRESH_TOKEN_TTL_MINUTES, TimeUnit.MINUTES);
    }

    @Transactional
    public String getRefreshToken(Long id){
        String key = KEY_REFRESH_TOKEN + id.toString();

        return stringRedisTemplate.opsForValue().get(key);
    }

    @Transactional
    public void deleteRefreshToken(Long id){
        String key = KEY_REFRESH_TOKEN + id.toString();

        stringRedisTemplate.delete(key);
    }

    @Transactional
    public void saveBudgetUsersRate(Integer rate) {
        // key
        String key = KEY_BUDGET_USERS_RATE;

        stringRedisTemplate.opsForValue().set(key, rate.toString());
        // TTL 3시간 설정
        stringRedisTemplate.expire(key, BUDGET_USERS_RATE_MINUTES, TimeUnit.MINUTES);
    }

    @Transactional
    public String getBudgetUsersRate(){
        String key = KEY_BUDGET_USERS_RATE;

        return stringRedisTemplate.opsForValue().get(key);
    }
}
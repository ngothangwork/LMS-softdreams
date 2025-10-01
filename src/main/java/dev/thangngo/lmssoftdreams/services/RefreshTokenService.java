package dev.thangngo.lmssoftdreams.services;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RefreshTokenService {

    // Key prefix trong Redis để dễ phân biệt
    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";

    // Sử dụng StringRedisTemplate của Spring Data Redis
    private final StringRedisTemplate redisTemplate;

    public RefreshTokenService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Lưu trữ Refresh Token ID (JTI) vào Redis.
     * @param jti JWT ID của Refresh Token
     * @param duration Thời gian hết hạn của token
     */
    public void saveRefreshToken(String jti, Duration duration) {
        // Key là refresh_token:JTI. Value có thể là ID người dùng hoặc đơn giản là 'valid'
        redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + jti, "valid", duration);
    }

    /**
     * Kiểm tra xem Refresh Token có còn hợp lệ trong Redis không.
     * @param jti JWT ID của Refresh Token
     * @return true nếu token tồn tại và hợp lệ
     */
    public boolean isValidRefreshToken(String jti) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(REFRESH_TOKEN_PREFIX + jti));
    }

    /**
     * Thu hồi (Revoke) Refresh Token bằng cách xóa nó khỏi Redis.
     * @param jti JWT ID của Refresh Token
     */
    public void revokeRefreshToken(String jti) {
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + jti);
    }
}

package com.liuhuang.voteprogram.repository;

import com.liuhuang.voteprogram.model.Polls;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PollRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;  // 用于模拟数据库操作[6,7](@ref)

    @Autowired
    private PollRepository pollRepository;

    // 测试方法1：验证返回进行中的投票
    @Test
    void findActivePolls_WhenCurrentTimeIsInRange_ShouldReturnActivePolls() {
        // 准备测试数据
        Polls activePoll = new Polls();
        activePoll.setTitle("Active Test Poll");
        activePoll.setStartTime(LocalDateTime.now().minusDays(1));  // 开始时间为1天前
        activePoll.setEndTime(LocalDateTime.now().plusDays(1));    // 结束时间为1天后
        entityManager.persist(activePoll);  // 持久化到内存数据库[6](@ref)

        // 执行查询
        List<Polls> activePolls = pollRepository.findActivePolls();

        // 验证结果
        assertThat(activePolls).hasSize(1);
        assertThat(activePolls.get(0).getTitle()).isEqualTo("Active Test Poll");
    }

    // 测试方法2：验证不返回已结束的投票
    @Test
    void findActivePolls_WhenCurrentTimeIsAfterEndTime_ShouldReturnEmpty() {
        Polls expiredPoll = new Polls();
        expiredPoll.setStartTime(LocalDateTime.now().minusDays(2));
        expiredPoll.setEndTime(LocalDateTime.now().minusDays(1));  // 结束时间为1天前
        entityManager.persist(expiredPoll);

        List<Polls> activePolls = pollRepository.findActivePolls();
        assertThat(activePolls).isEmpty();  // 断言无结果[7](@ref)
    }

    // 测试方法3：验证时间边界条件（当前时间等于开始时间）
    @Test
    void findActivePolls_WhenCurrentTimeIsExactlyStartTime_ShouldIncludePoll() {
        LocalDateTime now = LocalDateTime.now();
        Polls poll = new Polls();
        poll.setStartTime(now);
        poll.setEndTime(now.plusHours(1));
        entityManager.persist(poll);

        List<Polls> activePolls = pollRepository.findActivePolls();
        assertThat(activePolls).hasSize(1);  // 包含边界条件[8](@ref)
    }
}
package com.itmuch.usercenter.service.user;

import com.itmuch.usercenter.dao.user.BonusEventLogMapper;
import com.itmuch.usercenter.dao.user.UserMapper;
import com.itmuch.usercenter.domain.dto.messaging.UserAddBonusMsgDTO;
import com.itmuch.usercenter.domain.entity.user.BonusEventLog;
import com.itmuch.usercenter.domain.entity.user.User;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UserService {

    private final UserMapper userMapper;
    private final BonusEventLogMapper bonusEventLogMapper;

    public User findById(Integer id) {
        return this.userMapper.selectByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addBonus(UserAddBonusMsgDTO userAddBonusMsgDTO) {
        Integer userId = userAddBonusMsgDTO.getUserId();
        User user = this.userMapper.selectByPrimaryKey(userId);
        Integer bonus = userAddBonusMsgDTO.getBonus();
        user.setBonus(user.getBonus() + bonus);
        this.userMapper.updateByPrimaryKeySelective(user);
        this.bonusEventLogMapper.insert(
            BonusEventLog.builder()
                .userId(userId)
                .value(bonus)
                .event("CONTRIBUTE")
                .createTime(new Date())
                .description("投稿加积分..")
                .build()
        );
        log.info("积分添加完毕...");
    }

}

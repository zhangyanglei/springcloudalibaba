package com.itmuch.usercenter.controller.user;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.itmuch.usercenter.auth.CheckLogin;
import com.itmuch.usercenter.domain.dto.user.JwtTokenRespDTO;
import com.itmuch.usercenter.domain.dto.user.LoginRespDTO;
import com.itmuch.usercenter.domain.dto.user.UserLoginDTO;
import com.itmuch.usercenter.domain.dto.user.UserRespDTO;
import com.itmuch.usercenter.domain.entity.user.User;
import com.itmuch.usercenter.service.user.UserService;
import com.itmuch.usercenter.util.JwtOperator;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UserController {

    private final UserService userService;
    private final WxMaService wxMaService;
    private final JwtOperator jwtOperator;

    @GetMapping("{id}")
    @CheckLogin
    public User findById(@PathVariable Integer id) {
        log.info("我被请求了...");
        return this.userService.findById(id);
    }


    @GetMapping("/gen-token")
    public String genToken() {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", 1);
        userInfo.put("wxNickname", "大目");
        userInfo.put("role", "admin");
        return this.jwtOperator.generateToken(userInfo);
    }

    @PostMapping("/login")
    public LoginRespDTO login(@RequestBody UserLoginDTO loginDTO) throws WxErrorException {
        WxMaJscode2SessionResult result = this.wxMaService.getUserService()
            .getSessionInfo(loginDTO.getCode());
        String openid = result.getOpenid();
        User user = this.userService.login(loginDTO, openid);
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("wxNickname", user.getWxNickname());
        userInfo.put("role", user.getRoles());
        String token = jwtOperator.generateToken(userInfo);
        log.info(
            "用户{}登陆成功, 生成的token = {}, 有效期到:{}",
            loginDTO.getWxNickname(),
            token,
            jwtOperator.getExpirationTime()
        );
        return LoginRespDTO.builder()
            .user(
                UserRespDTO.builder()
                    .id(user.getId())
                    .avatarUrl(user.getAvatarUrl())
                    .bonus(user.getBonus())
                    .wxNickname(user.getWxNickname())
                    .build()
            )
            .token(JwtTokenRespDTO.builder()
                .expirationTime(jwtOperator.getExpirationTime().getTime())
                .token(token)
                .build()
            )
            .build();
    }


}

package cn.zeong.service.User;

import cn.zeong.pojo.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {
    UserServiceImpl userService = new UserServiceImpl();
    String userCode = "admin";
    String password = null;
    User user = null;
    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void login() {
        user = userService.login(userCode, password);
        System.out.println(user.getUserPassword());
    }
}
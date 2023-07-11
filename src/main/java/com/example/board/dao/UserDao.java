package com.example.board.dao;

import com.example.board.dto.User;
import org.springframework.stereotype.Repository;

@Repository // @Repository는 @Component를 상속받고 있어서 스프링이 관리하는 Bean이다.
public class UserDao {
    public User addUser(String email, String name, String password){
    // insert into user(email, name, password, regdate) values(?, ?, ?, now());

        return null;
    }

    public int getLastInsertId() {
        // SELECT LAST_INSERT_ID();
        return 0;
    }

    public void mappingUserRole(int userId) {
    //insert into user_role(user_id, role_id) values(? 1);

    }
}
 /*
* insert into user(email, name, password, regdate) values(?, ?, ?, now());
SELECT LAST_INSERT_ID();
insert into user_role(user_id, role_id) values(? 1-일반 유저 role);
* */
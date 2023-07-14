package com.example.board.dao;

import com.example.board.dto.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.time.LocalDateTime;

@Repository // @Repository는 @Component를 상속받고 있어서 스프링이 관리하는 Bean이다.
public class UserDao {

    private final NamedParameterJdbcTemplate jdbcTemplate; //jdbcTemplate을 사용하기 위한 변수
    //final 변수는 생성자에서 값을 초기화 해야 한다.

    private SimpleJdbcInsertOperations insertUser; // sql의 insert를 하게 해주는 인터페이스

    //jdbcTemplate을 초기화 하려면 DataSource가 필요하다.
    public UserDao(DataSource dataSource) {
    //application.yml에 db접속 관련 설정을 해주면 DataSource의 Hikari라 불리는 DataSource를 구현하고 있는 객체가 생성된다.
    //=> 해당 객체를 생성자에 넣어주면 스프링이 자동으로 빈을 생성하면서 final 변수에 넣어준다.
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("user")
                .usingGeneratedKeyColumns("user_id"); // 자동으로 증가되는 id를 설정

    }

    //Spring JDBC를 이용한 코드를 쓸 자리

    @Transactional // 하나의 트랜잭션으로 처리되기 위해
    public User addUser(String email, String name, String password){
    //Service에서 이미 트랜잭션이 시작했기 때문에 그 트랜잭션에 포함된다.
    // insert into user(email, name, password, regdate) values(:email, :name, :password, :regDate); #user_id auto gen
    // SELECT LAST_INSERT_ID();

        User user = new User();
        user.setName(name); // name 칼럼
        user.setEmail(email);
        user.setPassword(password);
        user.setRegDate(LocalDateTime.now());//Date 객체를 생성하고 문자열로 변환해서 저장
        SqlParameterSource params = new BeanPropertySqlParameterSource(user);
        //BeanPropertySqlParameterSource: DTO에 있는 값을 자동으로 sql parameter source로 넣어주는 객체.
        // 인터페이스인 SqlParameterSource를 구현하는 객체

       Number number = insertUser.executeAndReturnKey(params); // insert를 실행하고, 자동으로 생성된 id를 가져온다.
        int userId = number.intValue();
        user.setUserId(userId);


        return user; // insert된 전체 데이터를 user 객체가 가지고 리턴한다.
    }

    @Transactional
    public void mappingUserRole(int userId) {
    //Service에서 이미 트랜잭션이 시작했기 때문에 그 트랜잭션에 포함된다.
    //insert into user_role(user_id, role_id) values(? 1);

        String sql = "insert into user_role(user_id, role_id) values(:userId, 1)";
        SqlParameterSource params = new MapSqlParameterSource("userId", userId);
        //MapSqlParameterSource의 인자로 parameter name과 value를 넣을 수도 있고, 여러개일 땐 Map자체를 넣어줄 수도 있다.

        jdbcTemplate.update(sql, params);
    }
    @Transactional
    public User getUser(String email) {
        //user_id => setUserId, email => setEmail ...
        String sql = "select user_id, email, name, password, regdate from user where email = :email"; // 유저가 입력한 email에 해당하는 정보를 읽어오도록
        SqlParameterSource params = new MapSqlParameterSource("email", email);
        RowMapper<User> rowMapper = BeanPropertyRowMapper.newInstance(User.class); // User클래스 정보를 통해 각각의 컬럼을 매핑해주는 rowMapper가 생성됨
         User user =  jdbcTemplate.queryForObject(sql, params, rowMapper);
         return user;
    }
}
 /*
* insert into user(email, name, password, regdate) values(?, ?, ?, now());
SELECT LAST_INSERT_ID();
insert into user_role(user_id, role_id) values(? 1-일반 유저 role);
* */
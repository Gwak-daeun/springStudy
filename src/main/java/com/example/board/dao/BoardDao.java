package com.example.board.dao;

import com.example.board.dto.Board;
import com.example.board.dto.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository // dao는 @Repository 어노테이션을 붙인다.
public class BoardDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsertOperations insertBoard;

    public BoardDao(DataSource dataSource) { // 생성자 주입. 스프링이 자동으로 HikariCP Bean을 주입한다.
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        insertBoard = new SimpleJdbcInsert(dataSource)
                .withTableName("board")
                .usingGeneratedKeyColumns("board_id");

    }

    @Transactional
    public void addBoard(int userId, String title, String content) {
        Board board = new Board();
        board.setUserId(userId);
        board.setTitle(title);
        board.setContent(content);
        board.setRegdate(LocalDateTime.now());
        SqlParameterSource params = new BeanPropertySqlParameterSource(board);
        insertBoard.execute(params);
    }

    @Transactional(readOnly = true)
    public int getTotalCount() {
        String sql = "select count(*) as total_count from board"; // 무조건 1건의 데이터가 나온다.
        Integer totalCount = jdbcTemplate.queryForObject(sql, Map.of(), Integer.class);
        //Map.of(): 비어있는 맵을 리턴함
        return totalCount.intValue();
    }

    @Transactional(readOnly = true)
    public List<Board> getBoards(int page) {
        //start는 0(1page), 10(2page), 20(3page)
        int start = (page - 1) * 10;
        String sql = "select b.user_id, b.board_id, b.title, b.regdate, b.view_cnt, u.name from board b, user u where b.user_id = u.user_id order by board_id desc limit :start, 10";
        RowMapper<Board> rowMapper = BeanPropertyRowMapper.newInstance(Board.class);
        List<Board> list = jdbcTemplate.query(sql, Map.of("start", start), rowMapper);
        //여러건을 구할 땐 query()를 쓴다.
        return list;
    }

    @Transactional(readOnly = true)
    public Board getBoard(int boardId) {
        String sql = "select b.user_id, b.board_id, b.title, b.regdate, b.view_cnt, u.name, b.content from board b, user u where b.user_id = u.user_id and b.board_id = :boardId";
        // ⇒ 1건 또는 0건 나오는 쿼리
        RowMapper<Board> rowMapper = BeanPropertyRowMapper.newInstance(Board.class);
        //Board라는 dto에 값을 자동으로 담을 수 있는 rowMapper을 만듦
       Board board = jdbcTemplate.queryForObject(sql, Map.of("boardId", boardId), rowMapper);
        return board;
    }

    @Transactional
    public void updateViewCount(int boardId) {
        String sql = "update board\n" +
                "set view_cnt = view_cnt +1\n" +
                "where board_id = :boardId";
        jdbcTemplate.update(sql, Map.of("boardId", boardId));
    }


    public void deleteBoard(int boardId) {
        String sql = "delete from board where board_id = :boardId";
        jdbcTemplate.update(sql, Map.of("boardId", boardId));
    }

    @Transactional
    public void updateBoard(int boardId, String title, String content) {
        String sql = "update board\n" +
                "\n" +
                "set title = :title, content = :content\n" +
                "\n" +
                "where board_id = :boardId";

//        jdbcTemplate.update(sql, Map.of("boardId", boardId, "title", title, "content", content));
        Board board = new Board();
        board.setBoardId(boardId);
        board.setTitle(title);
        board.setContent(content);
        SqlParameterSource params = new BeanPropertySqlParameterSource(board);
        //board객체가 갖고 있는 값을 BeanPropertySqlParameterSource로 바꾼다.
        jdbcTemplate.update(sql, params);

    }
}

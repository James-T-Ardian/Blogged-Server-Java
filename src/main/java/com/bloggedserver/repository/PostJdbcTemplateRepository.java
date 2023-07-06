package com.bloggedserver.repository;

import com.bloggedserver.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.OptionalInt;


@Repository
public class PostJdbcTemplateRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        Post post = new Post(OptionalInt.of(rs.getInt("post_id")), rs.getString("title"), rs.getString("body"),
                rs.getString("created_at"), rs.getString("uploader"));

        return post;

    }

    public List<Post> getByUsername(String uploaderUsername) {
        String sql = "SELECT * FROM posts WHERE uploader = ? ORDER BY post_id DESC";
        List<Post> sqlResult = jdbcTemplate.query(sql, new Object[]{uploaderUsername},
                PostJdbcTemplateRepository::mapRow);
        return sqlResult;
    }

    public List<Post> getById(int postId) {
        String sql = "SELECT * FROM posts WHERE post_id = ?";
        List<Post> sqlResult = jdbcTemplate.query(sql, new Object[]{postId}, PostJdbcTemplateRepository::mapRow);
        return sqlResult;
    }

    public int create(String title, String body, String createdAt, String uploaderUsername) {
        String sql = "INSERT INTO posts(title, body, created_at, uploader) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, title, body, createdAt, uploaderUsername);
    }

    public int update(String title, String body, int postId) {
        String sql = "UPDATE posts SET title = ?, body = ? WHERE post_id  = ?";
        return jdbcTemplate.update(sql, title, body, postId);
    }

    public int delete(int postId) {
        String sql = "DELETE FROM posts WHERE post_id  = ?";
        return jdbcTemplate.update(sql, postId);
    }
}

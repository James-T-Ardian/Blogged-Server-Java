package com.bloggedserver.post;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;


@Repository
public class PostJdbcTemplateRepositoryImpl implements PostJdbcTemplateRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostJdbcTemplateRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        Post post = new Post(OptionalInt.of(rs.getInt("post_id")), rs.getString("title"), rs.getString("body"),
                Optional.of(rs.getString("created_at")), rs.getString("uploader"));

        return post;

    }

    @Override
    public List<Post> findAll(String uploaderUsername) {
        String sql = "SELECT * FROM posts WHERE uploader = ? ORDER BY post_id DESC";
        List<Post> sqlResult = jdbcTemplate.query(sql, new Object[]{uploaderUsername},
                PostJdbcTemplateRepositoryImpl::mapRow);
        return sqlResult;
    }

    @Override
    public <S extends Post> S save(S post) {
        String sql;
        if (post.post_id()
                .isPresent()) {
            sql = "UPDATE posts SET title = ?, body = ? WHERE post_id  = ? AND uploader = ?";
            jdbcTemplate.update(sql, post.title(), post.body(), post.post_id()
                    .getAsInt(), post.uploader());
        } else {
            sql = "INSERT INTO posts(title, body, created_at, uploader) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql, post.title(), post.body(), post.created_at()
                    .get(), post.uploader());
        }
        return post;
    }

    @Override
    public Optional<Post> findById(Integer postId, String uploaderUsername) {
        String sql = "SELECT * FROM posts WHERE post_id = ? AND uploader = ?";
        Optional<Post> optionalSqlResult = jdbcTemplate.query(sql, new Object[]{postId, uploaderUsername},
                        PostJdbcTemplateRepositoryImpl::mapRow)
                .stream()
                .findFirst();
        return optionalSqlResult;
    }

    @Override
    public void delete(Integer postId, String uploaderUsername) {
        String sql = "DELETE FROM posts WHERE post_id  = ? AND uploader = ?";
        jdbcTemplate.update(sql, postId, uploaderUsername);
    }
}


package com.bloggedserver.post;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.ListCrudRepository;
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
public class PostJdbcTemplateRepository implements ListCrudRepository<Post, Integer> {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        Post post = new Post(OptionalInt.of(rs.getInt("post_id")), rs.getString("title"), rs.getString("body"),
                Optional.of(rs.getString("created_at")), rs.getString("uploader"));

        return post;

    }

    public List<Post> findAllByUsername(String uploaderUsername) {
        String sql = "SELECT * FROM posts WHERE uploader = ? ORDER BY post_id DESC";
        List<Post> sqlResult = jdbcTemplate.query(sql, new Object[]{uploaderUsername},
                PostJdbcTemplateRepository::mapRow);
        return sqlResult;
    }

    @Override
    public <S extends Post> S save(S post) {
        String sql;
        if (post.post_id().isPresent()) {
            sql = "UPDATE posts SET title = ?, body = ? WHERE post_id  = ?";
            jdbcTemplate.update(sql, post.title(), post.body(), post.post_id().getAsInt());
        } else {
            sql = "INSERT INTO posts(title, body, created_at, uploader) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql, post.title(), post.body(), post.created_at().get(), post.uploader());
        }
        return post;
    }

    @Override
    public <S extends Post> List<S> saveAll(Iterable<S> posts) {
        List<S> savedPosts = new ArrayList<S>();
        for (S post : posts) {
            S savedPost = save(post);
            savedPosts.add(savedPost);
        }
        return savedPosts;
    }

    @Override
    public Optional<Post> findById(Integer postId) {
        String sql = "SELECT * FROM posts WHERE post_id = ?";
        Optional<Post> optionalSqlResult = jdbcTemplate.query(sql, new Object[]{postId},
                PostJdbcTemplateRepository::mapRow).stream().findFirst();
        return optionalSqlResult;
    }

    @Override
    public boolean existsById(Integer postId) {
        return findById(postId).isPresent();
    }

    @Override
    public List<Post> findAll() {
        String sql = "SELECT * FROM posts";
        List<Post> sqlResult = jdbcTemplate.query(sql, PostJdbcTemplateRepository::mapRow);
        return sqlResult;
    }

    @Override
    public List<Post> findAllById(Iterable<Integer> postIds) {
        List<Integer> postIdsIntegerList = new ArrayList<Integer>();
        postIds.forEach(postIdsIntegerList::add);

        List<String> postIdsStringList =
                postIdsIntegerList.stream().map(postId -> postId.toString()).collect(Collectors.toList());

        String sql = "SELECT * FROM posts WHERE post_id in (" + StringUtils.join(postIdsStringList, ',') + ")";
        List<Post> sqlResult = jdbcTemplate.query(sql, PostJdbcTemplateRepository::mapRow);
        return sqlResult;
    }

    @Override
    public long count() {
        long size = findAll().spliterator().getExactSizeIfKnown();
        if (findAll().spliterator().getExactSizeIfKnown() == -1) {
            return 0;
        } else {
            return size;
        }
    }

    @Override
    public void deleteById(Integer postId) {
        String sql = "DELETE FROM posts WHERE post_id  = ?";
        jdbcTemplate.update(sql, postId);
    }

    @Override
    public void delete(Post post) {
        if (post.post_id().isPresent()) {
            deleteById(post.post_id().getAsInt());
        }
    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> postIds) {
        for (int postId : postIds) {
            deleteById(postId);
        }
    }

    @Override
    public void deleteAll(Iterable<? extends Post> posts) {
        for (Post post : posts) {
            delete(post);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM posts";
        jdbcTemplate.update(sql);
    }
}

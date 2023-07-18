package com.bloggedserver.post;

import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface PostJdbcTemplateRepository extends ListCrudRepository<Post, Integer> {
    List<Post> findAllByUsername(String uploaderUsername);

}

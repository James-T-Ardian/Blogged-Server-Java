package com.bloggedserver.post;

import java.util.List;
import java.util.Optional;


public interface PostJdbcTemplateRepository {
    List<Post> findAll(String uploaderUsername);

    <S extends Post> S save(S post);


    Optional<Post> findById(Integer postId, String uploaderUsername);


    void delete(Integer postId, String uploaderUsername);



}

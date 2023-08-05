package com.bloggedserver.post;

import java.util.List;
import java.util.Optional;


public interface PostJdbcTemplateRepository {
    List<Post> findAllByUsername(String uploaderUsername);

    <S extends Post> S save(S post);


    Optional<Post> findById(Integer postId);


    void deleteByIdAndUsername(Integer postId, String uploaderUsername);



}

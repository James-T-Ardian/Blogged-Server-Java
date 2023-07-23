package com.bloggedserver.post;

import java.util.List;
import java.util.Optional;


public interface PostJdbcTemplateRepository {
    List<Post> findAllByUsername(String uploaderUsername);

    <S extends Post> S save(S post);


    <S extends Post> List<S> saveAll(Iterable<S> posts);


    Optional<Post> findById(Integer postId);


    boolean existsById(Integer postId);


    List<Post> findAll();


    List<Post> findAllById(Iterable<Integer> postIds);


    long count();


    void deleteById(Integer postId);


    void delete(Post post);


    void deleteAllById(Iterable<? extends Integer> postIds);


    void deleteAll(Iterable<? extends Post> posts);


    void deleteAll();

}

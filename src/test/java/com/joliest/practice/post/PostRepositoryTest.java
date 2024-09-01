package com.joliest.practice.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@DataJdbcTest
// ↓ test database to prevent errors
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryTest {

    // docker container
    // use the same postgre version you use
    @Container
    @ServiceConnection // remove the boilerplates, allow springboot to configure itself
    // ↓  this is a throwaway container, separate container for our test
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    PostRepository postRepository;

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @BeforeEach
    void setUp() {
        List<Post> posts = List.of(new Post(1,1,"Hello, World!", "This is my first post!",null));
        postRepository.saveAll(posts);
    }

    @Test
    void shouldReturnPostByTitle() {
//        make sure to add spring.sql.init.mode=always in the application.yml
        Post post = postRepository.findByTitle("Hello, World!");;
        assertThat(post).isNotNull();
    }
}
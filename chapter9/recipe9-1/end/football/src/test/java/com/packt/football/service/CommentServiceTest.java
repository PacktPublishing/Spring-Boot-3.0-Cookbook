package com.packt.football.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Random;
import java.util.Set;

import javax.management.MBeanServer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.packt.football.domain.CommentPost;
import com.packt.football.repo.Comment;

@Testcontainers
@SpringBootTest
class CommentServiceTest {

    @SuppressWarnings("resource")
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("football")
            .withUsername("football")
            .withPassword("football")
            .withReuse(false);

    @SuppressWarnings({ "rawtypes", "resource" })
    static CassandraContainer cassandraContainer = (CassandraContainer) new CassandraContainer("cassandra")
            .withInitScript("createKeyspace.cql")
            .withExposedPorts(9042)
            .withReuse(false);

    @DynamicPropertySource
    static void setCassandraProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.cassandra.keyspace-name", () -> "footballKeyspace");
        registry.add("spring.data.cassandra.contact-points", () -> cassandraContainer.getContactPoint().getAddress());
        registry.add("spring.data.cassandra.port", () -> cassandraContainer.getMappedPort(9042));
        registry.add("spring.data.cassandra.local-datacenter", () -> cassandraContainer.getLocalDatacenter());
        registry.add("spring.datasource.url", () -> postgreSQLContainer.getJdbcUrl());
        registry.add("spring.datasource.username", () -> postgreSQLContainer.getUsername());
        registry.add("spring.datasource.password", () -> postgreSQLContainer.getPassword());
    }

    @BeforeAll
    public static void startContainer() {
        cassandraContainer.start();
        postgreSQLContainer.start();
    }

    @AfterAll
    public static void stopContainer() {
        cassandraContainer.stop();
        postgreSQLContainer.stop();
    }

    @MockBean
    MBeanServer mbeanServer;

    @Autowired
    CommentService commentService;

    static Random random = new Random();

    @Test
    void postCommentTest() {
        CommentPost comment = new CommentPost("user1", "player", "1", "The best!", Set.of("label1", "label2"));
        Comment result = commentService.postComment(comment);
        assertNotNull(result);
        assertNotNull(result.getCommentId());
    }

    @Test
    void getPlayerCommentsTest() {
        // ARRANGE
        String playerId = "player" + random.nextInt();
        CommentPost comment = new CommentPost("user1", "player", playerId, "The best!", Set.of("label1", "label2"));
        Comment result = commentService.postComment(comment);
        // ACT
        var comments = commentService.getComments("player", playerId);
        // ASSERT
        assertNotNull(comments);
        assertThat(comments, hasSize(1));
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(result.getCommentId())));
    }

    @Test
    void getAllComments() {
        // ARRANGE
        CommentPost comment = new CommentPost("user1", "player", "1", "The best!", Set.of("label1", "label2"));
        Comment result = commentService.postComment(comment);
        // ACT
        var comments = commentService.getComments();
        // ASSERT
        assertNotNull(comments);
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(result.getCommentId())));
    }

    @Test
    void getCommentsTest_open_search() {
        // ARRANGE
        String playerId = "player" + random.nextInt(100000);
        CommentPost comment = new CommentPost("user1", "player", playerId, "The best!", Set.of("label1", "label2"));
        Comment resultUser1 = commentService.postComment(comment);
        comment = new CommentPost("user2", "player", playerId, "The best!", Set.of("label3", "label4"));
        Comment resultUser2 = commentService.postComment(comment);

        // ACT
        var comments = commentService.getComments("player", playerId, Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty());
        // ASSERT
        assertThat(comments, hasSize(2));
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(resultUser1.getCommentId())));
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(resultUser2.getCommentId())));

        // ACT
        comments = commentService.getComments("player", playerId, Optional.of("user1"), Optional.empty(),
                Optional.empty(), Optional.empty());
        // ASSERT
        assertThat(comments, hasSize(1));
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(resultUser1.getCommentId())));

        // ACT
        comments = commentService.getComments("player", playerId, Optional.of("user2"), Optional.empty(),
                Optional.empty(), Optional.empty());
        // ASSERT
        assertThat(comments, hasSize(1));
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(resultUser2.getCommentId())));

        // ACT
        comments = commentService.getComments("player", playerId, Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.of(Set.of("label1")));
        // ASSERT
        assertThat(comments, hasSize(1));
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(resultUser1.getCommentId())));

        // ACT
        comments = commentService.getComments("player", playerId, Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.of(Set.of("label4")));
        // ASSERT
        assertThat(comments, hasSize(1));
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(resultUser2.getCommentId())));
    }

    @Test
    void getCommentsString_open_search() {
        // ARRANGE
        String playerId = "player" + random.nextInt(100000);
        CommentPost comment = new CommentPost("user1", "player", playerId, "The best!", Set.of("label1", "label2"));
        Comment resultUser1 = commentService.postComment(comment);
        comment = new CommentPost("user2", "player", playerId, "The best!", Set.of("label3", "label4"));
        Comment resultUser2 = commentService.postComment(comment);

        // ACT
        var comments = commentService.getCommentsString("player", playerId, Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty());
        // ASSERT
        assertThat(comments, hasSize(2));
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(resultUser1.getCommentId())));
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(resultUser2.getCommentId())));

        // ACT
        comments = commentService.getCommentsString("player", playerId, Optional.of("user1"), Optional.empty(),
                Optional.empty(), Optional.empty());
        // ASSERT
        assertThat(comments, hasSize(1));
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(resultUser1.getCommentId())));

        // ACT
        comments = commentService.getCommentsString("player", playerId, Optional.of("user2"), Optional.empty(),
                Optional.empty(), Optional.empty());
        // ASSERT
        assertThat(comments, hasSize(1));
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(resultUser2.getCommentId())));

        // ACT
        comments = commentService.getCommentsString("player", playerId, Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.of(Set.of("label1")));
        // ASSERT
        assertThat(comments, hasSize(1));
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(resultUser1.getCommentId())));

        // ACT
        comments = commentService.getCommentsString("player", playerId, Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.of(Set.of("label4")));
        // ASSERT
        assertThat(comments, hasSize(1));
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(resultUser2.getCommentId())));
    }

    @Test
    void upvoteCommentTest() {
        // ARRANGE
        String playerId = "player" + random.nextInt(100000);
        CommentPost comment = new CommentPost("user1", "player", playerId, "The best!", Set.of("label1", "label2"));
        Comment resultUser1 = commentService.postComment(comment);
        assertThat(resultUser1.getUpvotes(), nullValue());
        Comment upvotedComment = commentService.upvoteComment(resultUser1.getCommentId());
        assertEquals(1, upvotedComment.getUpvotes());
    }
}
package com.packt.footballcdb.service;

import com.packt.footballcdb.model.CommentPost;
import com.packt.footballcdb.repository.Comment;
import org.HdrHistogram.packedarray.PackedLongArray;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.random.RandomGenerator;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class CommentServiceTest {

    static CassandraContainer cassandraContainer = (CassandraContainer) new CassandraContainer("cassandra").withInitScript("createKeyspace.cql").withExposedPorts(9042);

    @BeforeAll
    static void startContainer() throws IOException, InterruptedException {
        cassandraContainer.start();
    }

    @DynamicPropertySource
    static void setCassandraProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cassandra.keyspace-name", () -> "footballKeyspace");
        registry.add("spring.cassandra.contact-points", () -> cassandraContainer.getContactPoint().getAddress());
        registry.add("spring.cassandra.port", () -> cassandraContainer.getMappedPort(9042));
        registry.add("spring.cassandra.local-datacenter", () -> cassandraContainer.getLocalDatacenter());
    }

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
        LocalDateTime now = LocalDateTime.now();
        String playerId = "player" + random.nextInt(100000);
        CommentPost comment = new CommentPost("user1", "player", playerId, "The best!", Set.of("label1", "label2"));
        Comment resultUser1 = commentService.postComment(comment);
        comment = new CommentPost("user2", "player", playerId, "The best!", Set.of("label3", "label4"));
        Comment resultUser2 = commentService.postComment(comment);

        // ACT
        var comments = commentService.getComments("player", playerId, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        // ASSERT
        assertThat(comments, hasSize(2));
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(resultUser1.getCommentId())));
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(resultUser2.getCommentId())));

        // ACT
        comments = commentService.getComments("player", playerId, Optional.of("user1"), Optional.empty(), Optional.empty(), Optional.empty());
        // ASSERT
        assertThat(comments, hasSize(1));
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(resultUser1.getCommentId())));

        // ACT
        comments = commentService.getComments("player", playerId, Optional.of("user2"), Optional.empty(), Optional.empty(), Optional.empty());
        // ASSERT
        assertThat(comments, hasSize(1));
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(resultUser2.getCommentId())));

        // ACT
        comments = commentService.getComments("player", playerId, Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(Set.of("label1")));
        // ASSERT
        assertThat(comments, hasSize(1));
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(resultUser1.getCommentId())));

        // ACT
        comments = commentService.getComments("player", playerId, Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(Set.of("label4")));
        // ASSERT
        assertThat(comments, hasSize(1));
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(resultUser2.getCommentId())));
    }

    @Test
    void getCommentsString_open_search() {
        // ARRANGE
        LocalDateTime now = LocalDateTime.now();
        String playerId = "player" + random.nextInt(100000);
        CommentPost comment = new CommentPost("user1", "player", playerId, "The best!", Set.of("label1", "label2"));
        Comment resultUser1 = commentService.postComment(comment);
        comment = new CommentPost("user2", "player", playerId, "The best!", Set.of("label3", "label4"));
        Comment resultUser2 = commentService.postComment(comment);

        // ACT
        var comments = commentService.getCommentsString("player", playerId, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        // ASSERT
        assertThat(comments, hasSize(2));
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(resultUser1.getCommentId())));
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(resultUser2.getCommentId())));

        // ACT
        comments = commentService.getCommentsString("player", playerId, Optional.of("user1"), Optional.empty(), Optional.empty(), Optional.empty());
        // ASSERT
        assertThat(comments, hasSize(1));
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(resultUser1.getCommentId())));

        // ACT
        comments = commentService.getCommentsString("player", playerId, Optional.of("user2"), Optional.empty(), Optional.empty(), Optional.empty());
        // ASSERT
        assertThat(comments, hasSize(1));
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(resultUser2.getCommentId())));

        // ACT
        comments = commentService.getCommentsString("player", playerId, Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(Set.of("label1")));
        // ASSERT
        assertThat(comments, hasSize(1));
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(resultUser1.getCommentId())));

        // ACT
        comments = commentService.getCommentsString("player", playerId, Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(Set.of("label4")));
        // ASSERT
        assertThat(comments, hasSize(1));
        assertTrue(comments.stream().anyMatch(c -> c.getCommentId().equals(resultUser2.getCommentId())));
    }

    @Test
    void upvoteCommentTest() {
        // ARRANGE
        LocalDateTime now = LocalDateTime.now();
        String playerId = "player" + random.nextInt(100000);
        CommentPost comment = new CommentPost("user1", "player", playerId, "The best!", Set.of("label1", "label2"));
        Comment resultUser1 = commentService.postComment(comment);
        assertThat(resultUser1.getUpvotes(), nullValue());
        Comment upvotedComment = commentService.upvoteComment(resultUser1.getCommentId());
        assertEquals(1, upvotedComment.getUpvotes());
    }
}
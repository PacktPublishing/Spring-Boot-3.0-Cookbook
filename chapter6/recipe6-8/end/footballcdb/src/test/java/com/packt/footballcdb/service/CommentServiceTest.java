package com.packt.footballcdb.service;

import com.packt.footballcdb.model.CommentPost;
import com.packt.footballcdb.repository.Comment;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class CommentServiceTest {

    static CassandraContainer cassandraContainer = (CassandraContainer) new CassandraContainer("cassandra")
            .withInitScript("createKeyspace.cql")
            .withExposedPorts(9042);

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

    @Test
    void postCommentTest() {
        CommentPost comment = new CommentPost("user1", "player", "1", "The best!", Set.of("label1", "label2"));
        Comment result = commentService.postComment(comment);
        assertNotNull(result);
        assertNotNull(result.getCommentId());
    }

}
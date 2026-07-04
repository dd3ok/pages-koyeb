package com.dd3ok.pageskoyeb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dd3ok.pageskoyeb.config.MongoIndexInitializer;
import com.dd3ok.pageskoyeb.repository.wedding.WeddingCommentEntity;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexDefinition;
import org.springframework.data.mongodb.core.index.IndexOperations;

class MongoIndexInitializerTest {

    @Test
    void createsCompoundIndexForWeddingListQuery() {
        MongoTemplate mongoTemplate = mock(MongoTemplate.class);
        IndexOperations weddingIndexes = mock(IndexOperations.class);
        when(mongoTemplate.indexOps(WeddingCommentEntity.class)).thenReturn(weddingIndexes);

        new MongoIndexInitializer(mongoTemplate).ensureIndexes();

        ArgumentCaptor<IndexDefinition> weddingCaptor = ArgumentCaptor.forClass(IndexDefinition.class);
        verify(weddingIndexes).createIndex(weddingCaptor.capture());

        Document keys = weddingCaptor.getValue().getIndexKeys();
        Document options = weddingCaptor.getValue().getIndexOptions();

        assertThat(keys).containsEntry("created_at", -1);
        assertThat(keys).containsEntry("_id", -1);
        assertThat(options).containsEntry("name", "wedding_comments_created_at_id_desc");
        assertThat(options).containsEntry("background", true);
    }

    @Test
    void doesNotBlockApplicationStartupWhenMongoIndexCreationFails() {
        MongoTemplate mongoTemplate = mock(MongoTemplate.class);
        IndexOperations weddingIndexes = mock(IndexOperations.class);
        when(mongoTemplate.indexOps(WeddingCommentEntity.class)).thenReturn(weddingIndexes);
        when(weddingIndexes.createIndex(any(IndexDefinition.class))).thenThrow(new RuntimeException("mongo unavailable"));

        MongoIndexInitializer initializer = new MongoIndexInitializer(mongoTemplate);

        assertThatCode(initializer::ensureIndexes).doesNotThrowAnyException();
    }
}

package com.dd3ok.pageskoyeb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dd3ok.pageskoyeb.config.MongoIndexInitializer;
import com.dd3ok.pageskoyeb.repository.home.HomeContactEntity;
import com.dd3ok.pageskoyeb.repository.wedding.WeddingCommentEntity;
import java.util.List;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexDefinition;
import org.springframework.data.mongodb.core.index.IndexOperations;

class MongoIndexInitializerTest {

    @Test
    void createsIndexesUsedByListAndEmailQueries() {
        MongoTemplate mongoTemplate = mock(MongoTemplate.class);
        IndexOperations homeIndexes = mock(IndexOperations.class);
        IndexOperations weddingIndexes = mock(IndexOperations.class);
        when(mongoTemplate.indexOps(HomeContactEntity.class)).thenReturn(homeIndexes);
        when(mongoTemplate.indexOps(WeddingCommentEntity.class)).thenReturn(weddingIndexes);

        new MongoIndexInitializer(mongoTemplate).ensureIndexes();

        ArgumentCaptor<IndexDefinition> homeCaptor = ArgumentCaptor.forClass(IndexDefinition.class);
        ArgumentCaptor<IndexDefinition> weddingCaptor = ArgumentCaptor.forClass(IndexDefinition.class);
        verify(homeIndexes, times(2)).createIndex(homeCaptor.capture());
        verify(weddingIndexes).createIndex(weddingCaptor.capture());

        List<Document> homeIndexKeys = homeCaptor.getAllValues().stream()
            .map(IndexDefinition::getIndexKeys)
            .toList();

        assertThat(homeIndexKeys).anySatisfy(keys -> assertThat(keys).containsEntry("created_at", -1));
        assertThat(homeIndexKeys).anySatisfy(keys -> assertThat(keys).containsEntry("email", 1));
        assertThat(weddingCaptor.getValue().getIndexKeys()).containsEntry("created_at", -1);
    }

    @Test
    void doesNotBlockApplicationStartupWhenMongoIndexCreationFails() {
        MongoTemplate mongoTemplate = mock(MongoTemplate.class);
        IndexOperations homeIndexes = mock(IndexOperations.class);
        when(mongoTemplate.indexOps(HomeContactEntity.class)).thenReturn(homeIndexes);
        when(homeIndexes.createIndex(any(IndexDefinition.class))).thenThrow(new RuntimeException("mongo unavailable"));

        MongoIndexInitializer initializer = new MongoIndexInitializer(mongoTemplate);

        assertThatCode(initializer::ensureIndexes).doesNotThrowAnyException();
    }
}

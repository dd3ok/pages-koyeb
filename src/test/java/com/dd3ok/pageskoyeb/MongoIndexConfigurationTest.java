package com.dd3ok.pageskoyeb;

import static org.assertj.core.api.Assertions.assertThat;

import com.dd3ok.pageskoyeb.repository.home.HomeContactEntity;
import com.dd3ok.pageskoyeb.repository.wedding.WeddingCommentEntity;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.index.Indexed;

class MongoIndexConfigurationTest {

    @Test
    void homeContactsHaveIndexesForListAndEmailQueries() throws NoSuchFieldException {
        Indexed createdAtIndex = HomeContactEntity.class
            .getDeclaredField("createdAt")
            .getAnnotation(Indexed.class);
        Indexed emailIndex = HomeContactEntity.class
            .getDeclaredField("email")
            .getAnnotation(Indexed.class);

        assertThat(createdAtIndex).isNotNull();
        assertThat(createdAtIndex.direction().name()).isEqualTo("DESCENDING");
        assertThat(emailIndex).isNotNull();
    }

    @Test
    void weddingCommentsHaveIndexForListQuery() throws NoSuchFieldException {
        Indexed createdAtIndex = WeddingCommentEntity.class
            .getDeclaredField("createdAt")
            .getAnnotation(Indexed.class);

        assertThat(createdAtIndex).isNotNull();
        assertThat(createdAtIndex.direction().name()).isEqualTo("DESCENDING");
    }
}

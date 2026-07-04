package com.dd3ok.pageskoyeb;

import static org.assertj.core.api.Assertions.assertThat;

import com.dd3ok.pageskoyeb.domain.wedding.WeddingComment;
import com.dd3ok.pageskoyeb.domain.wedding.WeddingCommentRepository;
import com.dd3ok.pageskoyeb.service.wedding.WeddingCommentService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class PaginationLimitTest {

    @Test
    void weddingCommentListCapsLargePageSize() {
        CapturingWeddingRepository repository = new CapturingWeddingRepository();
        WeddingCommentService service = new WeddingCommentService(repository, new BCryptPasswordEncoder());

        service.getComments(0, 500);

        assertThat(repository.pageable.getPageNumber()).isZero();
        assertThat(repository.pageable.getPageSize()).isEqualTo(50);
    }

    @Test
    void weddingCommentListNormalizesInvalidPageRequest() {
        CapturingWeddingRepository repository = new CapturingWeddingRepository();
        WeddingCommentService service = new WeddingCommentService(repository, new BCryptPasswordEncoder());

        service.getComments(-2, 0);

        assertThat(repository.pageable.getPageNumber()).isZero();
        assertThat(repository.pageable.getPageSize()).isEqualTo(1);
    }

    private static class CapturingWeddingRepository implements WeddingCommentRepository {
        private Pageable pageable;

        @Override
        public WeddingComment save(WeddingComment comment) {
            return comment;
        }

        @Override
        public WeddingComment findById(String id) {
            return null;
        }

        @Override
        public Page<WeddingComment> findAll(Pageable pageable) {
            this.pageable = pageable;
            return new PageImpl<>(List.of(), pageable, 0);
        }

        @Override
        public void deleteById(String id) {
        }

        @Override
        public boolean existsById(String id) {
            return false;
        }
    }
}

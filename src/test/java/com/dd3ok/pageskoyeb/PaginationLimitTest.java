package com.dd3ok.pageskoyeb;

import static org.assertj.core.api.Assertions.assertThat;

import com.dd3ok.pageskoyeb.domain.wedding.WeddingComment;
import com.dd3ok.pageskoyeb.domain.wedding.WeddingCommentRepository;
import com.dd3ok.pageskoyeb.service.wedding.dto.CommentResponse;
import com.dd3ok.pageskoyeb.service.wedding.WeddingCommentService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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

    @Test
    void weddingCommentListUsesSliceWithoutTotalCountContract() {
        CapturingWeddingRepository repository = new CapturingWeddingRepository(true);
        WeddingCommentService service = new WeddingCommentService(repository, new BCryptPasswordEncoder());

        Slice<CommentResponse> response = service.getComments(0, 10);

        assertThat(response.hasNext()).isTrue();
    }

    private static class CapturingWeddingRepository implements WeddingCommentRepository {
        private Pageable pageable;
        private final boolean hasNext;

        private CapturingWeddingRepository() {
            this(false);
        }

        private CapturingWeddingRepository(boolean hasNext) {
            this.hasNext = hasNext;
        }

        @Override
        public WeddingComment save(WeddingComment comment) {
            return comment;
        }

        @Override
        public WeddingComment findById(String id) {
            return null;
        }

        @Override
        public Slice<WeddingComment> findAll(Pageable pageable) {
            this.pageable = pageable;
            return new SliceImpl<>(List.of(), pageable, hasNext);
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

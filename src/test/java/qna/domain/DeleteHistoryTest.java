package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.ContentType.QUESTION;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryTest {
    private static final DeleteHistory deleteHistoryTest = new DeleteHistory(QUESTION, 1L, 1L, LocalDateTime.now());

    @Test
    void 값_검증() {
        assertAll(
                () -> assertThat(deleteHistoryTest.getContentType()).isEqualTo(QUESTION),
                () -> assertThat(deleteHistoryTest.getContentId()).isEqualTo(1L),
                () -> assertThat(deleteHistoryTest.getDeletedById()).isEqualTo(1L)
        );
    }
}

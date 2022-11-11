package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import qna.enumType.ContentType;

public class DeleteHistories {
    private List<DeleteHistory> deleteHistories = new ArrayList<>();

    public DeleteHistories() {
    }

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
    }

    public void addQuestionDeleteHistory(Question question, User loginUser) {
        question.setDeleted();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, question.getId(), loginUser));
    }

    public void addAnswersDeleteHistory(List<Answer> answers, User loginUser) {
        answers.stream()
                .peek(Answer::setDeleted)
                .map(answer -> new DeleteHistory(ContentType.ANSWER, answer.getId(), loginUser))
                .forEach(deleteHistories::add);
    }

    public int size() {
        return deleteHistories.size();
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeleteHistories that = (DeleteHistories) o;
        return Objects.equals(deleteHistories, that.deleteHistories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deleteHistories);
    }
}

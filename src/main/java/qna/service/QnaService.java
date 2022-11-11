package qna.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.common.exception.CannotDeleteException;
import qna.common.exception.NotFoundException;
import qna.domain.Answer;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.User;
import qna.enumType.ContentType;
import qna.repository.AnswerRepository;
import qna.repository.QuestionRepository;

@Service
public class QnaService {
    private static final Logger log = LoggerFactory.getLogger(QnaService.class);

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final DeleteHistoryService deleteHistoryService;

    @Autowired
    public QnaService(QuestionRepository questionRepository, AnswerRepository answerRepository, DeleteHistoryService deleteHistoryService) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.deleteHistoryService = deleteHistoryService;
    }

    @Transactional(readOnly = true)
    public Question findQuestionById(Long id) {
        return questionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void deleteQuestion(User loginUser, Long questionId) {
        Question question = findQuestionById(questionId);
        question.isOwner(loginUser);

        List<Answer> answers = answerRepository.findByQuestion_IdAndDeletedFalse(questionId);
        answers.forEach(answer -> answer.isOwner(loginUser));

        List<DeleteHistory> deleteHistories = new ArrayList<>();
        question.setDeleted();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, questionId, loginUser, LocalDateTime.now()));
        for (Answer answer : answers) {
            answer.setDeleted();
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), loginUser, LocalDateTime.now()));
        }
        deleteHistoryService.saveAll(deleteHistories);
    }
}

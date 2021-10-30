package com.dodonov.oogosu.service.impl;

import com.dodonov.oogosu.config.security.UserRole;
import com.dodonov.oogosu.domain.Appeal;
import com.dodonov.oogosu.domain.Citizen;
import com.dodonov.oogosu.domain.dict.Employee;
import com.dodonov.oogosu.domain.enums.Difficulty;
import com.dodonov.oogosu.domain.enums.Qualification;
import com.dodonov.oogosu.domain.enums.State;
import com.dodonov.oogosu.dto.appeal.*;
import com.dodonov.oogosu.repository.AppealRepository;
import com.dodonov.oogosu.repository.CitizenRepository;
import com.dodonov.oogosu.repository.EmployeeRepository;
import com.dodonov.oogosu.repository.TopicRepository;
import com.dodonov.oogosu.service.AppealService;
import com.dodonov.oogosu.service.SecurityService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dodonov.oogosu.domain.enums.State.IN_WORK;
import static com.dodonov.oogosu.domain.enums.State.ON_REVIEW;

@Service
public class AppealServiceImpl implements AppealService {
    private static final String MESSAGE_ANSWER = "Уважаемый(-ая) %s,\nна ваше обращение от %s №%s дан ответ:\n%s\nС уважением, %s";
    private final AppealRepository appealRepository;
    private final CitizenRepository citizenRepository;
    private final TopicRepository topicRepository;
    private final EmployeeRepository employeeRepository;
    private final String emailFrom;
    private final SecurityService securityService;
    private final JavaMailSender javaMailSender;

    @Autowired
    public AppealServiceImpl(AppealRepository appealRepository,
                             CitizenRepository citizenRepository,
                             TopicRepository topicRepository,
                             EmployeeRepository employeeRepository,
                             @Value("${emailservice.smtp.user-name}")
                                     String emailFrom,
                             JavaMailSender javaMailSender,
                             SecurityService securityService) {
        this.appealRepository = appealRepository;
        this.citizenRepository = citizenRepository;
        this.topicRepository = topicRepository;
        this.employeeRepository = employeeRepository;
        this.javaMailSender = javaMailSender;
        this.emailFrom = emailFrom;
        this.securityService = securityService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createAppeal(Appeal appeal) {
        var topic = topicRepository.findById(appeal.getTopic().getId()).orElseThrow(EntityNotFoundException::new);
        appeal = Appeal.builder()
                .state(State.NEW)
                .creationDate(LocalDateTime.now())
                .citizen(citizenRepository.save(appeal.getCitizen()))
                .topic(topic)
                .department(topic.getDepartment())
                .isProlonged(false)
                .question(appeal.getQuestion())
                .build();
        return appealRepository.save(appeal).getId();
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public AppealCheckStatusDto checkStatus(AppealCheckStatusDto dto) {
        var appeal = appealRepository.findByIdAndCitizen_lastNameIgnoreCase(dto.getId(), dto.getCitizenLastName())
                .orElseThrow(EntityNotFoundException::new);
        dto.setState(appeal.getState());
        return dto;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public Page<AppealDto> findByCriteria(AppealCriteria dto) {
        return appealRepository.findByCriteria(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Appeal prolong(Long id, LocalDateTime dueDate) {
        var fromDb = appealRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (fromDb.getDueDate().toEpochSecond(ZoneOffset.UTC) > dueDate.toEpochSecond(ZoneOffset.UTC)) {
            throw new RuntimeException("Дата, на которую собираетесь продлить, раньше чем уже установленный срок");
        }
        fromDb.setDueDate(dueDate);
        fromDb.setIsProlonged(true);
        return appealRepository.save(fromDb);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Appeal appoint(AppealAppointmentDto dto) {
        var emp = employeeRepository.findById(dto.getExecutor().getId()).orElseThrow(EntityNotFoundException::new);
        var appeal = appealRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);
        var current = securityService.getCurrentEmployee();
        if (!securityService.hasRole(UserRole.ADMIN) && ((!appeal.getDepartment().equals(current.getDepartment())) || (!emp.getDepartment().equals(securityService.getCurrentDepartment())))) {
            throw new RuntimeException("Назначать можно только в рамках собственного департамента");
        }
        appeal.setState(IN_WORK);
        appeal.setIsComplaint(dto.getIsComplaint());
        appeal.setDifficulty(dto.getDifficulty());
        appeal.setExecutor(emp);
        appeal.setDueDate(dto.getDueDate());
        return appealRepository.save(appeal);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Appeal answer(AppealAnswerDto appealDto) {
        var appeal = appealRepository.findById(appealDto.getId()).orElseThrow(EntityNotFoundException::new);
        if (!IN_WORK.equals(appeal.getState())){
            throw new RuntimeException("Подготовить ответ можно только на обращение, находящееся в работе");
        }
        if (StringUtils.isBlank(appealDto.getAnswer())) {
            throw new RuntimeException("Необходимо написать ответ");
        }
        appeal.setAnswer(appealDto.getAnswer());
        appeal.setState(State.ON_REVIEW);
        appeal.setAnswerDate(LocalDateTime.now());
        if (appealDto.getDecision() == null) {
            throw new RuntimeException("Необходимо указать решение");
        }
        appeal.setDecision(appealDto.getDecision());
        return appealRepository.save(appeal);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Appeal returnToExecutor(AppealReturnDto appealDto) {
        var appeal = appealRepository.findById(appealDto.getId()).orElseThrow(EntityNotFoundException::new);
        if (!ON_REVIEW.equals(appeal.getState())) {
            throw new RuntimeException("Вернуть можно только из статуса На проверке");
        }
        if (StringUtils.isBlank(appealDto.getReturnReason())) {
            throw new RuntimeException("Необходимо указать причину возврата");
        }
        appeal.setState(IN_WORK);
        if (appeal.getDueDate().toEpochSecond(ZoneOffset.UTC) < appealDto.getDueDate().toEpochSecond(ZoneOffset.UTC)) {
            appeal.setIsProlonged(true);
            appeal.setDueDate(appealDto.getDueDate());
        }
        appeal.setAnswerDate(null);
        appeal.setAnswer(null);
        appeal.setIsReturned(true);
        appeal.setDecision(null);
        return appealRepository.save(appeal);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public Appeal sendAnswer(Long id) {
        var appeal = appealRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (!ON_REVIEW.equals(appeal.getState())){
            throw new RuntimeException("Отправить ответ можно только из статуса На проверке");
        }
        String messageText = String.format(MESSAGE_ANSWER,
                getAuthorFio(appeal.getCitizen()),
                appeal.getCreationDate(),
                appeal.getId(),
                appeal.getAnswer(),
                getEmployeeRequiusites(employeeRepository.findByQualificationAndDepartment_id(Qualification.LEAD, appeal.getDepartment().getId()), appeal.getDepartment().getName()));
        appeal.setAnswer(messageText);
        appeal.setState(State.SENT);
        appeal.setAnswerDate(LocalDateTime.now());

        var message = javaMailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, true);
        helper.setFrom(emailFrom);
        helper.setSubject("Ответ на обращение");
        helper.setTo(appeal.getCitizen().getEmail());
        helper.setText(messageText);
        javaMailSender.send(message);

        return appealRepository.save(appeal);
    }

    private String getEmployeeRequiusites(Optional<Employee> lead, String departmentName) {
        var emp = lead.orElseThrow(EntityNotFoundException::new);
        return Stream.of(emp.getLastName(), emp.getFirstName(), emp.getMiddleName(),"-", departmentName + ", начальник")
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(" "));
    }

    private String getAuthorFio(Citizen citizen) {
        return Stream.of(citizen.getLastName(), citizen.getFirstName(), citizen.getMiddleName())
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(" "));
    }
}

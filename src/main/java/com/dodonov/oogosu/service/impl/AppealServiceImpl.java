package com.dodonov.oogosu.service.impl;

import com.dodonov.oogosu.domain.Appeal;
import com.dodonov.oogosu.domain.Citizen;
import com.dodonov.oogosu.domain.dict.Employee;
import com.dodonov.oogosu.domain.enums.Qualification;
import com.dodonov.oogosu.domain.enums.State;
import com.dodonov.oogosu.dto.appeal.AppealCheckStatusDto;
import com.dodonov.oogosu.dto.appeal.AppealCriteria;
import com.dodonov.oogosu.dto.appeal.AppealDto;
import com.dodonov.oogosu.repository.AppealRepository;
import com.dodonov.oogosu.repository.CitizenRepository;
import com.dodonov.oogosu.repository.EmployeeRepository;
import com.dodonov.oogosu.repository.TopicRepository;
import com.dodonov.oogosu.service.AppealService;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AppealServiceImpl implements AppealService {
    private static final String MESSAGE_ANSWER = "Уважаемый(-ая) %s,\nна ваше обращение от %s №%s дан ответ:\n%s\nС уважением, %s";
    private AppealRepository appealRepository;
    private CitizenRepository citizenRepository;
    private TopicRepository topicRepository;
    private EmployeeRepository employeeRepository;
    private Session session;
    private String emailFrom;

    @Autowired
    public AppealServiceImpl(AppealRepository appealRepository,
                             CitizenRepository citizenRepository,
                             TopicRepository topicRepository,
                             EmployeeRepository employeeRepository,
                             @Value("${emailservice.smtp.user-name}")
                             String emailFrom,
                             Session session) {
        this.appealRepository = appealRepository;
        this.citizenRepository = citizenRepository;
        this.topicRepository = topicRepository;
        this.employeeRepository = employeeRepository;
        this.session = session;
        this.emailFrom = emailFrom;
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
    public Appeal appoint(Employee employee, Long appealId, Boolean isComplaint) {
        var emp = employeeRepository.findById(employee.getId()).orElseThrow(EntityNotFoundException::new);
        var appeal = appealRepository.findById(appealId).orElseThrow(EntityNotFoundException::new);
        appeal.setState(State.IN_WORK);
        appeal.setIsComplaint(isComplaint);
        appeal.setExecutor(emp);
        return appealRepository.save(appeal);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Appeal answer(AppealDto appealDto) {
        var appeal = appealRepository.findById(appealDto.getId()).orElseThrow(EntityNotFoundException::new);
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
    public Appeal returnToExecutor(AppealDto appealDto) {
        var appeal = appealRepository.findById(appealDto.getId()).orElseThrow(EntityNotFoundException::new);
        if (StringUtils.isBlank(appealDto.getReturnReason())) {
            throw new RuntimeException("Необходимо указать причину возврата");
        }
        appeal.setState(State.IN_WORK);
        if (appeal.getDueDate().toEpochSecond(ZoneOffset.UTC) < appealDto.getDueDate().toEpochSecond(ZoneOffset.UTC)) {
            appeal.setIsProlonged(true);
            appeal.setDueDate(appeal.getDueDate());
        }
        appeal.setIsReturned(true);
        return appealRepository.save(appeal);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public Appeal sendAnswer(AppealDto appealDto) {
        var appeal = appealRepository.findById(appealDto.getId()).orElseThrow(EntityNotFoundException::new);

        String messageText = String.format(MESSAGE_ANSWER,
                getAuthorFio(appeal.getCitizen()),
                appeal.getCreationDate(),
                appeal.getId(),
                appeal.getAnswer(),
                getEmployeeRequiusites(employeeRepository.findByQualificationAndDepartment_id(Qualification.LEAD, appeal.getDepartment().getId()), appeal.getDepartment().getName()));
        appeal.setAnswer(messageText);
        appeal.setState(State.SENT);
        appeal.setAnswerDate(LocalDateTime.now());

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailFrom));
        message.setSubject("Ответ на обращение");
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(appeal.getCitizen().getEmail()));
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setText(messageText, "UTF-8", "html");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        message.setContent(multipart);
        Transport.send(message);

        return appealRepository.save(appeal);
    }

    private String getEmployeeRequiusites(Optional<Employee> lead, String departmentName) {
        var emp = lead.orElseThrow(EntityNotFoundException::new);
        return Stream.of(emp.getLastName(), emp.getFirstName(), emp.getMiddleName(), "- начальник департамента", departmentName)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(" "));
    }

    private String getAuthorFio(Citizen citizen) {
        return Stream.of(citizen.getLastName(), citizen.getFirstName(), citizen.getMiddleName())
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(" "));
    }
}

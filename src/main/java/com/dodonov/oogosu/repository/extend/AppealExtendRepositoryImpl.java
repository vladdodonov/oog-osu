package com.dodonov.oogosu.repository.extend;

import com.dodonov.oogosu.config.security.UserRole;
import com.dodonov.oogosu.domain.Appeal;
import com.dodonov.oogosu.domain.BaseEntity;
import com.dodonov.oogosu.domain.Citizen;
import com.dodonov.oogosu.domain.dict.Department;
import com.dodonov.oogosu.domain.dict.Employee;
import com.dodonov.oogosu.domain.dict.Topic;
import com.dodonov.oogosu.dto.RangeDto;
import com.dodonov.oogosu.dto.appeal.AppealCriteria;
import com.dodonov.oogosu.dto.appeal.AppealDto;
import com.dodonov.oogosu.mapstruct.appeal.AppealDtoMapper;
import com.dodonov.oogosu.service.SecurityService;
import com.dodonov.oogosu.service.TopicService;
import com.dodonov.oogosu.utils.FetchUtil;
import com.dodonov.oogosu.utils.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.dodonov.oogosu.config.security.UserRole.ADMIN;
import static com.dodonov.oogosu.config.security.UserRole.INSPECTOR;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Repository
public class AppealExtendRepositoryImpl implements AppealExtendRepository {
    private final Set<UserRole> ADMIN_INSPECTOR_ROLES = Set.of(ADMIN, INSPECTOR);
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private TopicService topicService;

    @Override
    @Transactional(readOnly = true)
    public Page<AppealDto> findByCriteria(AppealCriteria criteria) {
        criteria.setFetch("citizen", "department", "executor", "topic");
        List<Long> ids = findIdsByCriteria(criteria);
        var range = Range.of(criteria.getRange().getSkip(), criteria.getRange().getTake(), criteria.getRange().getDirection());
        if (ids != null && !ids.isEmpty()) {
            return new PageImpl<>(AppealDtoMapper.INSTANCE.toDto(findByIds(ids, criteria)), range, getCount(criteria));
        }
        return new PageImpl<>(Collections.EMPTY_LIST, range, 0);
    }

    private List<Appeal> findByIds(List<Long> ids, AppealCriteria criteria) {
        var cb = entityManager.getCriteriaBuilder();
        var criteriaQuery = cb.createQuery(Appeal.class);
        var root = criteriaQuery.from(Appeal.class);
        FetchUtil.setFetch(root, criteria.getFetch());
        criteriaQuery.select(root);
        criteriaQuery.where(cb.in(root.get("id")).value(ids));
        criteriaQuery.orderBy(getOrder(criteria.getRange(), cb, root));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    private List<Long> findIdsByCriteria(AppealCriteria criteria) {
        var cb = entityManager.getCriteriaBuilder();
        var criteriaQuery = cb.createQuery(Long.class);
        var root = criteriaQuery.from(Appeal.class);
        criteriaQuery.select(root.get("id"));
        criteriaQuery.where(buildPredicate(cb, root, criteria));
        criteriaQuery.orderBy(getOrder(criteria.getRange(), cb, root));
        var typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(criteria.getRange().getSkip()).setMaxResults(criteria.getRange().getTake());
        return typedQuery.getResultList();
    }

    private Order getOrder(RangeDto range, CriteriaBuilder cb, Root root) {
        if (range.getDirection() != null && DESC.name().equals(range.getDirection().name())) {
            return cb.desc((root.get("id")));
        } else {
            return cb.asc((root.get("id")));
        }
    }

    private Long getCount(AppealCriteria criteria) {
        var cb = entityManager.getCriteriaBuilder();
        var criteriaQuery = cb.createQuery(Long.class);
        var root = criteriaQuery.from(Appeal.class);
        criteriaQuery.select(cb.count(root.get("id")));
        criteriaQuery.where(buildPredicate(cb, root, criteria));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    private Predicate buildPredicate(CriteriaBuilder cb, Root<Appeal> root, AppealCriteria criteria) {
        List<Predicate> predicateList = new ArrayList<>();
        buildDatePredicate(cb, root, predicateList, criteria.getAnswerDateFrom(), criteria.getAnswerDateTo(), "answerDate");
        buildDatePredicate(cb, root, predicateList, criteria.getDueDateFrom(), criteria.getDueDateTo(), "dueDate");
        buildDatePredicate(cb, root, predicateList, criteria.getCreationDateFrom(), criteria.getCreationDateTo(), "creationDate");
        if (criteria.getCitizen() != null) {
            Join<Appeal, Citizen> citizenJoin = root.join("citizen", JoinType.LEFT);
            var citizen = criteria.getCitizen();
            if (isNotBlank(citizen.getFirstName())) {
                var value = "%" + citizen.getFirstName().trim().replaceAll(" +", " ") + "%";
                predicateList.add(cb.like(cb.lower(citizenJoin.get("firstName")), value.toLowerCase()));
            }
            if (isNotBlank(citizen.getLastName())) {
                var value = "%" + citizen.getLastName().trim().replaceAll(" +", " ") + "%";
                predicateList.add(cb.like(cb.lower(citizenJoin.get("lastName")), value.toLowerCase()));
            }
            if (isNotBlank(citizen.getMiddleName())) {
                var value = "%" + citizen.getMiddleName().trim().replaceAll(" +", " ") + "%";
                predicateList.add(cb.like(cb.lower(citizenJoin.get("middleName")), value.toLowerCase()));
            }
            if (isNotBlank(citizen.getAddress())) {
                var value = "%" + citizen.getAddress().trim().replaceAll(" +", " ") + "%";
                predicateList.add(cb.like(cb.lower(citizenJoin.get("address")), value.toLowerCase()));
            }
            if (isNotBlank(citizen.getEmail())) {
                var value = "%" + citizen.getEmail().trim().replaceAll(" +", " ") + "%";
                predicateList.add(cb.like(cb.lower(citizenJoin.get("email")), value.toLowerCase()));
            }
            if (isNotBlank(citizen.getPhone())) {
                predicateList.add(cb.equal(cb.lower(citizenJoin.get("phone")), citizen.getPhone().trim()));
            }
        }

        if (isNotEmpty(criteria.getDecisions())) {
            predicateList.add(cb.in(root.get("decision")).value(criteria.getDecisions()));
        }

        if (isNotEmpty(criteria.getStates())) {
            predicateList.add(cb.in(root.get("state")).value(criteria.getStates()));
        }

        if (isNotEmpty(criteria.getDepartmentIds()) && securityService.hasAnyRole(ADMIN_INSPECTOR_ROLES)) {
            predicateList.add(cb.in(root.get("department")).value(criteria.getDepartmentIds().stream().map(id -> Department.builder().id(id).build()).collect(toSet())));

        } else {
            predicateList.add(cb.in(root.get("department")).value(Department.builder().id(securityService.getCurrentDepartment().getId()).build()));
        }

        if (isNotEmpty(criteria.getTopicIds())) {
            if (securityService.hasAnyRole(ADMIN_INSPECTOR_ROLES)) {
                predicateList.add(cb.in(root.get("topic")).value(criteria.getTopicIds().stream().map(id -> Topic.builder().id(id).build()).collect(toSet())));
            } else {
                var departmentTopicIds = topicService.findAllByDepartment(securityService.getCurrentDepartment()).stream()
                        .map(BaseEntity::getId)
                        .collect(toSet());
                var filteredTopicIds = criteria.getTopicIds().stream()
                        .filter(departmentTopicIds::contains)
                        .collect(toSet());
                predicateList.add(cb.in(root.get("topic")).value(filteredTopicIds.stream().map(id -> Topic.builder().id(id).build()).collect(toSet())));
            }
        }

        if (isNotEmpty(criteria.getExecutorIds())) {
            predicateList.add(cb.in(root.get("executor")).value(criteria.getExecutorIds().stream().map(id -> Employee.builder().id(id)).collect(toSet())));
        }

        if (criteria.getIsProlonged() != null) {
            predicateList.add(criteria.getIsProlonged()
                    ? cb.isTrue(root.get("isProlonged"))
                    : cb.isFalse(root.get("isProlonged")));
        }
        return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
    }

    private void buildDatePredicate(CriteriaBuilder cb, Root root, List<Predicate> predicateList, LocalDateTime from, LocalDateTime to, String field) {
        if (from != null) {
            if (to != null) {
                predicateList.add(cb.between(root.get(field), from, to));
            } else {
                predicateList.add(cb.greaterThan(root.get(field), from));
            }
        } else if (to != null) {
            predicateList.add(cb.lessThan(root.get(field), to));
        }
    }
}

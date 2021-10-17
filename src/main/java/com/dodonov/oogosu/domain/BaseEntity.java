package com.dodonov.oogosu.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Comparator;

import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    /**
     * Если хоть один из идентификаторов == null, сущности должны считаться различными.
     * В противном случае будет невозможно добавить несколько новых (состояние Transient) сущностей в связь to-many,
     * реализованную через Set
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        BaseEntity that = (BaseEntity) o;
        return id.equals(that.getId());
    }

    /**
     * Хэш код постоянный в пределах класса согласно статье
     * https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/.
     * Имплементация на идентификаторе работает некорректно: для одной и той же сущности значения будут различаться
     * в сотояниях Transient и остальных, так как в Transient идентификатор == null
     */
    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    public static final Comparator<BaseEntity> comparatorAsc = nullsLast(
            comparing(BaseEntity::getId, nullsLast(naturalOrder()))
    );
}

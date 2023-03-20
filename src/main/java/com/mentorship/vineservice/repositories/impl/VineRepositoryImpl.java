package com.mentorship.vineservice.repositories.impl;

import com.mentorship.vineservice.domain.Vine;
import com.mentorship.vineservice.dto.PaginationDto;
import com.mentorship.vineservice.repositories.VineRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class VineRepositoryImpl implements VineRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Page<Vine> findVinesBySugarAndColorAndTypeAndGrapePaginated(PaginationDto pagination, String sugar,
        String color, String name, String grape) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Vine> cq = criteriaBuilder.createQuery(Vine.class);
        Root<Vine> vineRoot = cq.from(Vine.class);
        List<Predicate> predicates = new ArrayList<>();

        if (sugar != null  && !sugar.isEmpty()) {
            predicates.add(criteriaBuilder.equal(vineRoot.get("sugar"), sugar.trim()));
        }
        if (color != null && !color.isEmpty()) {
            predicates.add(criteriaBuilder.equal(vineRoot.get("color"), color.trim()));
        }

        if (name != null && !name.isEmpty()) {
            predicates.add(criteriaBuilder.equal(vineRoot.get("name"), name.trim()));
        }
        if (grape != null && !grape.isEmpty()) {
            predicates.add(criteriaBuilder.equal(vineRoot.get("grapeName"), grape.trim()));
        }

        cq.select(vineRoot)
            .distinct(true)
            .where(predicates.toArray(new Predicate[]{}))
            .orderBy(criteriaBuilder.asc(vineRoot.get("id")));

        TypedQuery<Vine> query = entityManager.createQuery(cq);
        int countFilteredVines = query.getResultList().size();

        List<Vine> vineQueryResult;
        if (pagination.getPage() != null && pagination.getSize() != null) {
            query.setFirstResult(pagination.getPage() * pagination.getSize());
            query.setMaxResults(pagination.getSize());
        }
        vineQueryResult = query.getResultList();

        return new PageImpl<>(vineQueryResult, Pageable.unpaged(), countFilteredVines);

    }
}

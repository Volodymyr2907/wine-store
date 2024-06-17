package com.mentorship.vineservice.repository.impl;

import com.mentorship.vineservice.domain.DeliveryDetails;
import com.mentorship.vineservice.domain.Order;
import com.mentorship.vineservice.domain.OrderVine;
import com.mentorship.vineservice.domain.PostOffice;
import com.mentorship.vineservice.domain.Vine;
import com.mentorship.vineservice.dto.OrderPostOfficeDto;
import com.mentorship.vineservice.repository.VineRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaCteCriteria;
import org.hibernate.query.criteria.JpaJoinedFrom;
import org.hibernate.query.criteria.JpaRoot;
import org.springframework.stereotype.Repository;

@Repository
public class VineRepositoryCustomImpl implements VineRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Vine> getAllVines() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Vine> criteriaQuery = criteriaBuilder.createQuery(Vine.class);
        Root<Vine> root = criteriaQuery.from(Vine.class);
        criteriaQuery.select(root);
        return entityManager.createQuery(criteriaQuery).getResultList();

    }

    @Override
    public List<Order> getAllOrdersWithDeliveryDetails() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        root.fetch("deliveryDetails", JoinType.LEFT);
        criteriaQuery.select(root);
        return entityManager.createQuery(criteriaQuery).getResultList();

    }

    @Override
    public Long getVineIdWithFiltering(Long vineId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Vine> root = criteriaQuery.from(Vine.class);

        Predicate predicate = criteriaBuilder.equal(root.get("id"), vineId);
        criteriaQuery.select(root.get("id"))
            .where(predicate);
        return entityManager.createQuery(criteriaQuery).getSingleResult();

    }

    @Override
    public List<OrderVine> getVinesWithFilteringInSubQuery() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderVine> criteriaQuery = criteriaBuilder.createQuery(OrderVine.class);
        Root<OrderVine> root = criteriaQuery.from(OrderVine.class);

        Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
        Root<Vine> subqueryRoot = subquery.from(Vine.class);
        subquery.select(subqueryRoot.get("id")).where(criteriaBuilder.between(subqueryRoot.get("year"), 2012, 2021));

        criteriaQuery.select(root)
            .where(root.get("vine").get("id").in(subquery));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<OrderVine> getVinesWithFilteringInCte() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        JpaCriteriaQuery<Tuple> tupleJpaCriteriaQuery = (JpaCriteriaQuery<Tuple>) criteriaBuilder.createTupleQuery();
        JpaRoot<Vine> vineJpaRoot = tupleJpaCriteriaQuery.from(Vine.class);
        tupleJpaCriteriaQuery.multiselect(
            vineJpaRoot.get("id").alias("vine_id"),
            vineJpaRoot.get("name").alias("vine_name")
        ).where(criteriaBuilder.between(vineJpaRoot.get("year"), 2012, 2021));

        JpaCriteriaQuery<OrderVine> jpaCriteriaQuery = (JpaCriteriaQuery<OrderVine>) criteriaBuilder.createQuery(
            OrderVine.class);
        JpaCteCriteria<Tuple> cteCriteria = jpaCriteriaQuery.with(tupleJpaCriteriaQuery);

        JpaRoot<OrderVine> root = jpaCriteriaQuery.from(OrderVine.class);
        JpaJoinedFrom<?, Tuple> joinTuple = root.join(cteCriteria);
        joinTuple.on(root.get("vine").get("id").in(joinTuple.get("vine_id")));

        jpaCriteriaQuery.select(root);
        return entityManager.createQuery(jpaCriteriaQuery).getResultList();
    }

    public Map<String, Long> getCountedSoldWines() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
        Root<Vine> root = criteriaQuery.from(Vine.class);
        Join<Vine, OrderVine> orderVineJoin = root.join("orders", JoinType.LEFT);

        criteriaQuery.select(criteriaBuilder.tuple(
            root.get("name"),
            criteriaBuilder.count(orderVineJoin.get("vine").get("id"))
        )).groupBy(root.get("name"));

        List<Tuple> tupleResult = entityManager.createQuery(criteriaQuery).getResultList();

        Map<String, Long> resultMap = new HashMap<>();

        for (Tuple tuple : tupleResult) {
            String key = tuple.get(0, String.class);
            Long value = tuple.get(1, Long.class);

            resultMap.put(key, value);
        }
        return resultMap;
    }

    @Override
    public Long getVineIdFilteredByJoinId() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Vine> root = criteriaQuery.from(Vine.class);
        Join<Vine, OrderVine> joinOrders = root.join("orders", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(joinOrders.get("order").get("id"), 1));

        criteriaQuery.select(root.get("id"))
            .where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public List<OrderPostOfficeDto> getAllOrdersIdWithDeliveryInfo() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
        Root<Order> root = criteriaQuery.from(Order.class);
        Join<Order, DeliveryDetails> joinDeliveryDetails = root.join("deliveryDetails");
        Join<DeliveryDetails, PostOffice> joinPostOffice = joinDeliveryDetails.join("postOffice");

        CriteriaQuery<Tuple> select = criteriaQuery.select(
            criteriaBuilder.tuple(
                root.get("id"),
                joinPostOffice.get("city"),
                joinPostOffice.get("officeNumber")
            )
        );

        List<Tuple> tupleResult = entityManager.createQuery(select).getResultList();
        List<OrderPostOfficeDto> orderPostOfficeDtos = new ArrayList<>();
        tupleResult.forEach(tuple -> orderPostOfficeDtos.add(
            new OrderPostOfficeDto(
                (Long) tuple.get(0),
                (String) tuple.get(1),
                (Integer) tuple.get(2)
            )));

        // construct wi
//        CriteriaQuery<OrderPostOfficeDto> criteriaQuery = criteriaBuilder.createQuery(OrderPostOfficeDto.class);
//        Root<Order> root = criteriaQuery.from(Order.class);
//        Join<Order, DeliveryDetails> joinDeliveryDetails = root.join("deliveryDetails");
//        Join<DeliveryDetails, PostOffice> joinPostOffice = joinDeliveryDetails.join("postOffice");
//        criteriaQuery.select(criteriaBuilder.construct(
//            OrderPostOfficeDto.class,
//            root.get("id"),
//            joinPostOffice.get("city"),
//            joinPostOffice.get("officeNumber")
//        ));
//        TypedQuery<OrderPostOfficeDto> typedQuery = entityManager.createQuery(criteriaQuery);
//        return typedQuery.getResultList();

        return orderPostOfficeDtos;
    }

    @Override
    public void updateVineNameById(String newName, Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaUpdate<Vine> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Vine.class);
        Root<Vine> root = criteriaUpdate.from(Vine.class);
        criteriaUpdate.set(root.get("name"), newName)
            .where(criteriaBuilder.equal(root.get("id"), id));

        entityManager.createQuery(criteriaUpdate).executeUpdate();

    }

    @Override
    public void deletePostOfficeById(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaDelete<PostOffice> criteriaDelete = criteriaBuilder.createCriteriaDelete(PostOffice.class);
        Root<PostOffice> root = criteriaDelete.from(PostOffice.class);
        criteriaDelete
            .where(criteriaBuilder.equal(root.get("id"), id));

        entityManager.createQuery(criteriaDelete).executeUpdate();

    }


}

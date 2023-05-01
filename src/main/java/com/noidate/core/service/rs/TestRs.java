package com.noidate.core.service.rs;

import com.noidate.api.service.RsResponseService;
import com.noidate.core.model.Buyer;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

import static com.noidate.core.management.AppConstants.BUYERS_PATH;

@Path(BUYERS_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class TestRs extends RsResponseService {

    @Inject
    EntityManager entityManager;

    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    public Response test(@DefaultValue("0") @QueryParam("startRow") Integer startRow,
                         @DefaultValue("10") @QueryParam("pageSize") Integer pageSize,
                         @QueryParam("orderBy") String orderBy) throws Exception {
        CriteriaBuilder cbl = criteriaBuilder();

        CriteriaQuery<Long> cql = cbl.createQuery(Long.class);
        Root<Buyer> rootl = cql.from(Buyer.class);
        Predicate[] predicates = query(cbl, rootl);
        cql.select(cbl.count(rootl));
        if (predicates != null && predicates.length > 0) {
            cql.where(predicates);
        }
        Long listSize = entityManager.createQuery(cql).getSingleResult();

        CriteriaQuery<Buyer> cq = cbl.createQuery(Buyer.class);
        Root<Buyer> root = cq.from(Buyer.class);
        predicates = query(cbl, root);
        if (predicates != null && predicates.length > 0) {
            cq.where(predicates);
        }
        Order[] orders = sort(orderBy, root);
        if (orders != null && orders.length > 0) {
            cq.orderBy(orders);
        }
        List<Buyer> list = entityManager.createQuery(cq).getResultList();
        if (list == null) {
            list = new ArrayList<>();
        }

        return Response
                .status(Response.Status.OK)
                .entity(list)
                .header("Access-Control-Expose-Headers", "startRow, pageSize, listSize")
                .header("startRow", startRow)
                .header("pageSize", pageSize)
                .header("listSize", listSize)
                .build();
    }


    public Predicate[] query(CriteriaBuilder criteriaBuilder, Root<Buyer> root) {
        var predicates = new ArrayList<Predicate>();

        if (nn("from.creation_date")) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("creation_date"), _localDate("from.creation_date")));
        }
        return predicates.toArray(new Predicate[]{});
    }


    CriteriaBuilder criteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    protected Order[] sort(String orderBy, Root<Buyer> root) throws Exception {
        List<Order> orders = null;
        if (orderBy != null && !orderBy.trim().isEmpty()) {
            orderBy = orderBy.toLowerCase();
            if (orderBy != null && orderBy.contains(",")) {
                String[] orderByClause = orderBy.split(",");
                for (String pz : orderByClause) {
                    orders.add(single(pz, root));
                }
                return orders.toArray(new Order[]{});
            } else {
                return new Order[]{single(orderBy, root)};
            }
        }
        return null;
    }

    private Order single(String orderBy, Root<Buyer> root) throws Exception {
        String[] orderByClause;
        if (orderBy.contains(":")) {
            orderByClause = orderBy.split(":");
        } else {
            orderByClause = orderBy.split(" ");
        }
        if (orderByClause.length > 1) {
            if (orderByClause[1].equalsIgnoreCase("asc")) {
                return criteriaBuilder().asc(root.get(orderByClause[0]));
            } else if (orderByClause[1].equalsIgnoreCase("desc")) {
                return criteriaBuilder().desc(root.get(orderByClause[0]));
            }
        }
        return criteriaBuilder().asc(root.get(orderBy));
    }
}

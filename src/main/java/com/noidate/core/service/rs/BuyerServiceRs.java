package com.noidate.core.service.rs;

import com.noidate.api.service.RsRepositoryServiceV4;
import com.noidate.core.model.Buyer;
import jakarta.inject.Singleton;
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
public class BuyerServiceRs extends RsRepositoryServiceV4<Buyer, String> {

    public BuyerServiceRs() {
        super(Buyer.class);
    }

    @Override
    protected CriteriaBuilder criteriaBuilder() {
        return getEntityManager().getCriteriaBuilder();
    }

    @Override
    protected CriteriaQuery<Buyer> criteriaQuery(CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.createQuery(Buyer.class);
    }

    @Override
    protected Root<Buyer> root(CriteriaQuery<Buyer> criteriaQuery) {
        return criteriaQuery.from(Buyer.class);
    }

    @Override
    protected String getDefaultOrderBy() {
        return "surname asc";
    }

    @Override
    public Predicate[] query() throws Exception {
        return new Predicate[0];
    }


    public Predicate[] query(CriteriaBuilder criteriaBuilder, Root<Buyer> root) {
        var predicates = new ArrayList<Predicate>();
        if (nn("obj.uuid")) {
            predicates.add(criteriaBuilder.equal(root.get("uuid"), get("obj.uuid")));
        }
        if (nn("obj.fiscal_code")) {
            predicates.add(criteriaBuilder.equal(root.get("fiscal_code"), get("obj.fiscal_code")));
        }
        if (nn("like.name")) {
            predicates.add(criteriaBuilder.like(root.get("name"), likeParam("like.name")));
        }
        if (nn("like.surname")) {
            predicates.add(criteriaBuilder.like(root.get("surname"), likeParam("like.surname")));
        }
        if (nn("like.email")) {
            predicates.add(criteriaBuilder.like(root.get("email"), likeParam("obj.email")));
        }
        return predicates.toArray(new Predicate[]{});
    }


    @Path("/test")
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
        cql.select(cbl.count(cql.from(getEntityClass())));
        if (predicates != null && predicates.length > 0) {
            cql.where(predicates);
        }
        Long listSize = getEntityManager().createQuery(cql).getSingleResult();

        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<Buyer> cq = cb.createQuery(Buyer.class);
        Root<Buyer> root = cq.from(Buyer.class);
        predicates = query(cb, root);
        if (predicates != null && predicates.length > 0) {
            cq.where(predicates);
        }
        Order[] orders = sort(orderBy, root);
        if (orders != null && orders.length > 0) {
            cq.orderBy(orders);
        }
        List<Buyer> list = getEntityManager().createQuery(cq).getResultList();
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
}

package com.noidate.core.service.rs;

import com.noidate.api.service.RsRepositoryServiceV4;
import com.noidate.core.model.Buyer;
import com.noidate.core.model.enums.BuyerType;
import jakarta.inject.Singleton;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;

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
    protected String getDefaultOrderBy() {
        return "surname asc";
    }


    @Override
    public Predicate[] query(CriteriaBuilder criteriaBuilder, Root<Buyer> root) {
        var predicates = new ArrayList<Predicate>();
        if (true) {
            predicates.add(criteriaBuilder.equal(root.get("active"), true));
        }
        if (nn("obj.uuids")) {
            CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get("uuid"));
            for (String uuid : asList("obj.uuids")) {
                inClause.value(uuid);
            }
            predicates.add(inClause);
        }
        if (nn("from.creation_date")) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("creation_date"), _localDate("from.creation_date")));
        }
        if (nn("to.creation_date")) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("creation_date"), _localDate("to.creation_date")));
        }
        if (nn("obj.buyerType")) {
            predicates.add(criteriaBuilder.equal(root.get("buyerType"),
                    BuyerType.valueOf(get("obj.buyerType"))));
        }
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
}

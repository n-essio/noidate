package com.noidate.core.service.rs;

import com.noidate.api.service.RsRepositoryServiceV4;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import com.noidate.api.service.RsRepositoryServiceV3;
import com.noidate.core.model.Customer;

import jakarta.inject.Singleton;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.util.ArrayList;

import static com.noidate.core.management.AppConstants.CUSTOMERS_PATH;

@Path(CUSTOMERS_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class CustomerServiceRs extends RsRepositoryServiceV4<Customer, String> {

    public CustomerServiceRs() {
        super(Customer.class);
    }

    @Override
    protected String getDefaultOrderBy() {
        return "name asc";
    }

    @Override
    public Predicate[] query(CriteriaBuilder criteriaBuilder, Root<Customer> root) throws Exception {
        var predicates = new ArrayList<Predicate>();

        if (true) {
            predicates.add(criteriaBuilder.equal(root.get("active"), true));
        }
        if (nn("obj.code")) {
            predicates.add(criteriaBuilder.equal(root.get("code"), get("obj.code")));
        }
        if (nn("like.name")) {
            predicates.add(criteriaBuilder.like(root.get("name"), likeParam("like.name")));
        }
        return predicates.toArray(new Predicate[]{});
    }

}

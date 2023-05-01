package com.noidate.api.service;

import com.noidate.core.model.Buyer;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.logging.Logger;

import java.io.Serializable;
import java.util.List;

public abstract class RsRepositoryServiceV4<T extends PanacheEntityBase, U> extends RsResponseService implements
        Serializable {

    private static final long serialVersionUID = 1L;

    protected Logger logger = Logger.getLogger(getClass());

    private Class<T> entityClass;

    @Inject
    EntityManager entityManager;

    //  @Inject
    //  SecurityIdentity securityIdentity;

    protected Class<T> getEntityClass() {
        return entityClass;
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    protected SecurityIdentity getCurrentUser() {
        return null;
    }

    public RsRepositoryServiceV4(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public RsRepositoryServiceV4() {
    }

    protected void prePersist(T object) throws Exception {
    }


    @POST
    @Transactional
    public Response persist(T object) {
        logger.info("persist");
        try {
            prePersist(object);
        } catch (Exception e) {
            logger.errorv(e, "persist");
            return jsonMessageResponse(Status.BAD_REQUEST, e);
        }

        try {
            entityManager.persist(object);
            if (object == null) {
                logger.error("Failed to create resource: " + object);
                return jsonErrorMessageResponse(object);
            } else {
                return Response.status(Status.OK).entity(object).build();
            }
        } catch (Exception e) {
            logger.errorv(e, "persist");
            return jsonErrorMessageResponse(object);
        } finally {
            try {
                postPersist(object);
            } catch (Exception e) {
                logger.errorv(e, "persist");
            }
        }
    }

    protected void postPersist(T object) throws Exception {
    }

    protected void postFetch(T object) throws Exception {
    }

    @GET
    @Path("/{id}")
    @Transactional
    public Response fetch(@PathParam("id") U id) {
        logger.info("fetch: " + id);

        try {
            T t = find(id);
            if (t == null) {
                return handleObjectNotFoundRequest(id);
            } else {
                try {
                    postFetch(t);
                } catch (Exception e) {
                    logger.errorv(e, "fetch: " + id);
                }
                return Response.status(Status.OK).entity(t).build();
            }
        } catch (NoResultException e) {
            logger.errorv(e, "fetch: " + id);
            return jsonMessageResponse(Status.NOT_FOUND, id);
        } catch (Exception e) {
            logger.errorv(e, "fetch: " + id);
            return jsonErrorMessageResponse(e);
        }
    }

    protected T preUpdate(T object) throws Exception {
        return object;
    }

    protected abstract CriteriaBuilder criteriaBuilder();
//    {
//        return getEntityManager().getCriteriaBuilder();
//    }

    protected abstract CriteriaQuery<T> criteriaQuery(CriteriaBuilder criteriaBuilder);
//    {
//        return criteriaBuilder().createQuery(getEntityClass());
//    }

    protected abstract Root<T> root(CriteriaQuery<T> criteriaQuery);
//    {
//        return criteriaQuery().from(getEntityClass());
//    }


    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") U id, T object) {
        logger.info("update:" + id);

        try {
            object = preUpdate(object);
        } catch (Exception e) {
            logger.errorv(e, "update:" + id);
            return jsonMessageResponse(Status.BAD_REQUEST, e);
        }
        try {
            entityManager.merge(object);
            return Response.status(Status.OK).entity(object).build();
        } catch (Exception e) {
            logger.errorv(e, "update:" + id);
            return jsonErrorMessageResponse(object);
        } finally {
            try {
                postUpdate(object);
            } catch (Exception e) {
                logger.errorv(e, "update:" + id);
            }
        }
    }

    /**
     * concepita per chiamare robe async dopo l'update (o cmq robe fuori dalla tx principale che non rollbacka se erorri qui)
     *
     * @param object
     * @throws Exception
     */
    protected void postUpdate(T object) throws Exception {
    }

    protected void preDelete(U id) throws Exception {
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") U id) {
        logger.info("delete: " + id);

        try {
            preDelete(id);
        } catch (Exception e) {
            logger.errorv(e, "delete: " + id);
            return jsonMessageResponse(Status.BAD_REQUEST, e);
        }
        T t;
        try {
            t = find(id);
            if (t == null) {
                return handleObjectNotFoundRequest(id);
            }
        } catch (Exception e) {
            return jsonMessageResponse(Status.BAD_REQUEST, e);
        }
        try {
            toDelete(t);
            postDelete(id);
            return jsonMessageResponse(Status.NO_CONTENT, id);
        } catch (NoResultException e) {
            logger.errorv(e, "delete: " + id);
            return jsonMessageResponse(Status.NOT_FOUND, id);
        } catch (Exception e) {
            logger.errorv(e, "delete: " + id);
            return jsonErrorMessageResponse(e);
        }
    }

    protected void postDelete(U id) throws Exception {
    }

    @GET
    @Path("/{id}/exist")
    public Response exist(@PathParam("id") U id) {
        logger.info("exist: " + id);

        try {
            boolean exist = find(id) != null;
            if (!exist) {
                return handleObjectNotFoundRequest(id);
            } else {
                return jsonMessageResponse(Status.OK, id);
            }
        } catch (Exception e) {
            logger.errorv(e, "exist: " + id);
            return jsonErrorMessageResponse(e);
        }
    }

    @GET
    @Path("/listSize")
    @Transactional
    public Response getListSize(@Context UriInfo ui) {
        logger.info("getListSize");
        try {
            CriteriaQuery<Long> cql = criteriaBuilder().createQuery(Long.class);
            cql.select(criteriaBuilder().count(cql.from(getEntityClass()))).where(query());
            Long listSize = entityManager.createQuery(cql).getSingleResult();
            return Response.status(Status.OK).entity(listSize)
                    .header("Access-Control-Expose-Headers", "listSize")
                    .header("listSize", listSize).build();
        } catch (Exception e) {
            logger.errorv(e, "getListSize");
            return jsonErrorMessageResponse(e);
        }
    }

    @GET
    @Transactional
    public Response getList(
            @DefaultValue("0") @QueryParam("startRow") Integer startRow,
            @DefaultValue("10") @QueryParam("pageSize") Integer pageSize,
            @QueryParam("orderBy") String orderBy, @Context UriInfo ui) {

        logger.info("getList");
        try {
            Predicate[] predicates = query();
            var cb = criteriaBuilder();
            var cq = criteriaQuery(cb);
            var root = root(cq);
            CriteriaQuery<Long> cql = cb.createQuery(Long.class);
            cql.select(cb.count(cql.from(getEntityClass())));
//            if (predicates != null && predicates.length > 0) {
//                cql.where(predicates);
//            }
            Long listSize = entityManager.createQuery(cql).getSingleResult();
            cq.select(root(cq));
            if (predicates != null && predicates.length > 0) {
                cq.where(criteriaBuilder().like(root(cq).get("surname"), likeParam("like.surname")));
            }
            Order[] orders = sort(orderBy, root);
            if (orders != null && orders.length > 0) {
                cq.orderBy();
            }
            var query = entityManager.createQuery(cq);
            List<T> list = query
                    .setFirstResult(startRow)
                    .setMaxResults(pageSize)
                    .getResultList();
            postList(list);
            return Response
                    .status(Status.OK)
                    .entity(list)
                    .header("Access-Control-Expose-Headers", "startRow, pageSize, listSize")
                    .header("startRow", startRow)
                    .header("pageSize", pageSize)
                    .header("listSize", listSize)
                    .build();
        } catch (Exception e) {
            logger.errorv(e, "getList");
            return jsonErrorMessageResponse(e);
        }
    }

    protected void postList(List<T> list) throws Exception {
    }

    /**
     * Gestisce la risposta a seguito di un oggetto non trovato
     *
     * @param id
     * @return
     */
    protected Response handleObjectNotFoundRequest(U id) {
        String errorMessage = String.format("Object [{0}] with id [{1}] not found",
                entityClass.getCanonicalName(), id);
        return jsonMessageResponse(Status.NOT_FOUND, errorMessage);
    }

    protected Response handleObjectNotFoundRequest(U id, String name) {
        String errorMessage = String.format("Object [{0}] with id [{1}] not found",
                name, id);
        return jsonMessageResponse(Status.NOT_FOUND, errorMessage);
    }


    public T find(U id) {
        return getEntityManager().find(getEntityClass(), id);
    }

    public void toDelete(T t) {
        t.delete();
    }

    protected abstract String getDefaultOrderBy();

    public abstract Predicate[] query() throws Exception;

    protected Order[] sort(String orderBy, Root<T> root) throws Exception {
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
        if (getDefaultOrderBy() != null && !getDefaultOrderBy().trim().isEmpty()) {
            if (getDefaultOrderBy().toLowerCase().contains("asc")) {
                String orderString = getDefaultOrderBy().toLowerCase().replace("asc", "").trim();
                return new Order[]{criteriaBuilder().asc(root.get(orderString))};
            }
            if (getDefaultOrderBy().toLowerCase().contains("desc")) {
                String orderString = getDefaultOrderBy().toLowerCase().replace("desc", "").trim();
                return new Order[]{criteriaBuilder().desc(root.get(orderString))};
            }
        }
        return null;
    }

    private Order single(String orderBy, Root<T> root) throws Exception {
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

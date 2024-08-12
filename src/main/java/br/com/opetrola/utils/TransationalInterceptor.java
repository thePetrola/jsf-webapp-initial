package br.com.opetrola.utils;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;

@Interceptor
@Transational
@Priority(Interceptor.Priority.APPLICATION)
public class TransationalInterceptor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager em;

    @AroundInvoke
    public Object aroundInvoke(InvocationContext ctx) throws Exception {
        EntityTransaction tx = em.getTransaction();
        boolean criador = false;

        try {
            if (!tx.isActive()) {
                tx.begin();
                tx.rollback();

                tx.begin();

                criador = true;
            }

            return ctx.proceed();
        } catch (Exception e) {
            if (tx != null && criador) {
                tx.rollback();
            }

            throw e;
        } finally {
            if (tx != null && tx.isActive() && criador) {
                tx.commit();
            }
        }
    }
}

package si.strimr.track.metadata.services;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.strimr.track.metadata.models.entities.TrackMetadata;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;


@RequestScoped
public class TrackMetadataBean {

    private Logger log = Logger.getLogger(TrackMetadataBean.class.getName());

    @Inject
    private EntityManager em;

    @Inject
    private TrackMetadataBean trackMetadataBean;

    public List<TrackMetadata> getTrackMetadataFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, TrackMetadata.class, queryParameters);
    }

    public TrackMetadata getTrackMetadata(Integer trackId) {

        TrackMetadata trackMetadata = em.find(TrackMetadata.class, trackId);

        if (trackMetadata == null) {
            throw new NotFoundException();
        }

        return trackMetadata;
    }

    public TrackMetadata createTrackMetadata(TrackMetadata trackMetadata) {

        try {
            beginTx();
            em.persist(trackMetadata);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return trackMetadata;
    }

    public TrackMetadata putTrackMetadata(String trackId, TrackMetadata trackMetadata) {

        TrackMetadata trackMetadata1 = em.find(TrackMetadata.class, trackId);

        if (trackMetadata1 == null) {
            return null;
        }

        try {
            beginTx();
            trackMetadata.setId(trackMetadata1.getId());
            trackMetadata = em.merge(trackMetadata);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return trackMetadata;
    }

    public boolean deleteTrackMetadata(String trackId) {

        TrackMetadata customer = em.find(TrackMetadata.class, trackId);

        if (customer != null) {
            try {
                beginTx();
                em.remove(customer);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else
            return false;

        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }
}
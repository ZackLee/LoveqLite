package us.friends.loveqlite.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramList {

    private List<Datum> data = new ArrayList<Datum>();
    private Status status;
    private Paginated paginated;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The data
     */
    public List<Datum> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(List<Datum> data) {
        this.data = data;
    }

    public ProgramList withData(List<Datum> data) {
        this.data = data;
        return this;
    }

    /**
     * @return The status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    public ProgramList withStatus(Status status) {
        this.status = status;
        return this;
    }

    /**
     * @return The paginated
     */
    public Paginated getPaginated() {
        return paginated;
    }

    /**
     * @param paginated The paginated
     */
    public void setPaginated(Paginated paginated) {
        this.paginated = paginated;
    }

    public ProgramList withPaginated(Paginated paginated) {
        this.paginated = paginated;
        return this;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public ProgramList withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
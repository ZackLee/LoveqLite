package us.friends.loveqlite.bean;


import java.util.HashMap;
import java.util.Map;

public class Paginated {

    private Integer total;
    private Integer count;
    private Integer more;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * @param total The total
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    public Paginated withTotal(Integer total) {
        this.total = total;
        return this;
    }

    /**
     * @return The count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * @param count The count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    public Paginated withCount(Integer count) {
        this.count = count;
        return this;
    }

    /**
     * @return The more
     */
    public Integer getMore() {
        return more;
    }

    /**
     * @param more The more
     */
    public void setMore(Integer more) {
        this.more = more;
    }

    public Paginated withMore(Integer more) {
        this.more = more;
        return this;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Paginated withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
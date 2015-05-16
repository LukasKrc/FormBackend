package lt.ktu.formbackend.model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Lukas
 */
public class SearchQuery {
    private String query;
    private ArrayList<String> tags;
    private String sort;
    private int limit;
    private int skip;
    private String order;
    private String author;
    private Boolean allowAnon;
    private Boolean finished;
    
    public SearchQuery(Boolean allowAnon, String author, Boolean finished, int limit, String order, String query, int skip, String sort, String tags) {
        this.allowAnon = allowAnon;
        this.author = author;
        this.finished = finished;
        this.limit = limit;
        this.order = order;
        this.query = query;
        this.skip = skip;
        this.sort = sort;
        ArrayList<String> tagsArray = new ArrayList();
        if (tags != null && !tags.equals(""))
            tagsArray.addAll(Arrays.asList(tags.split(",")));
        this.tags = tagsArray;
    }
    
    public String hasMandatoryFields() {
        if (query == null)
            return "query";
        if (sort == null)
            return "sort";
        if (order == null)
            return "order";
        if (author == null)
            return "author";
        if (allowAnon == null)
            return "allowAnon";
        return null;
    }

    //<editor-fold desc="Getters and setters">
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Boolean getAllowAnon() {
        return allowAnon;
    }

    public void setAllowAnon(Boolean allowAnon) {
        this.allowAnon = allowAnon;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
    //</editor-fold>
    
}

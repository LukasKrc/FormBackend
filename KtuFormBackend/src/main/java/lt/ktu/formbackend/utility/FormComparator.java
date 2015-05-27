package lt.ktu.formbackend.utility;

import java.util.Comparator;
import lt.ktu.formbackend.model.Form;

/**
 *
 * @author Lukas
 */
public class FormComparator implements Comparator<Form> {

    private final String sortType;
    private final boolean sortOrder; // true: ascending, false: descending

    public FormComparator(String orderType, String sortOrder) {
        this.sortType =  orderType != null ? orderType : "";
        this.sortOrder = sortOrder != null && sortOrder.equals("ascending");
    }

    @Override
    public int compare(Form o1, Form o2) {
        switch(sortType)
        {
            case "relevance":
                /* TODO */
                /* FALL THROUGH */
            case "popularity":
            {
                int votediff = o1.getVotes() - o2.getVotes();
                return sortOrder ? votediff : -votediff;
            }
            case "date":
            default:
            {
                long date1 = Long.parseLong(o1.getDate().replace(" ", "").replace(":", "").replace("-", ""));
                long date2 = Long.parseLong(o2.getDate().replace(" ", "").replace(":", "").replace("-", ""));
                return (int)(sortOrder ? date1 - date2 : date2 - date1);
            }
        }
    }

}

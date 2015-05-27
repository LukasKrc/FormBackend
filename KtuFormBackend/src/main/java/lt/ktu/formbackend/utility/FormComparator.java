package lt.ktu.formbackend.utility;

import java.util.Comparator;
import lt.ktu.formbackend.model.Form;

/**
 *
 * @author Lukas
 */
public class FormComparator implements Comparator<Form> {

    private final String sortType;
    private final String sortOrder;

    public FormComparator(String orderType, String sortOrder) {
        this.sortType = orderType;
        this.sortOrder = sortOrder;
    }

    @Override
    public int compare(Form o1, Form o2) {
        boolean so = !sortOrder.equals("descending"); /* ascending or unspecified order */
        final String st = sortType != null ? sortType : "";
        switch(st)
        {
            case "relevance":
                /* TODO */
                /* FALL THROUGH */
            case "popularity":
            {
                int votediff = o1.getVotes() - o2.getVotes();
                return so ? votediff : -votediff;
            }
            case "date":
            default:
            {
                long date1 = Long.parseLong(o1.getDate().replace(" ", "").replace(":", ""));
                long date2 = Long.parseLong(o2.getDate().replace(" ", "").replace(":", ""));
                return (int)(so ? date1 - date2 : date2 - date1);
            }
        }
    }

}

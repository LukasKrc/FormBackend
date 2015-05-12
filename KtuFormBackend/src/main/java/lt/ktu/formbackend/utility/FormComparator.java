package lt.ktu.formbackend.utility;

import java.util.Comparator;
import lt.ktu.formbackend.model.Form;

/**
 *
 * @author Lukas
 */
public class FormComparator implements Comparator<Form> {

    private String orderType;

    public FormComparator(String orderType) {
        this.orderType = orderType;
    }

    @Override
    public int compare(Form o1, Form o2) {
        long date1 = Long.parseLong(o1.getDate().replace(" ", "").replace(":", ""));
        long date2 = Long.parseLong(o2.getDate().replace(" ", "").replace(":", ""));
        if (orderType == null) {
            long difference = (date1 - date2);
            if (difference == 0) {
                return 0;
            } else if (difference < 0) {
                return -1;
            } else {
                return 1;
            }
        }
        switch (orderType) {
            case "popularity":
                return o1.getVotes() - o2.getVotes();

            case "date":
                long difference = (date1 - date2);
                if (difference == 0) {
                    return 0;
                } else if (difference < 0) {
                    return -1;
                } else {
                    return 1;
                }

            default:
                long defaultDifference = (date1 - date2);
                if (defaultDifference == 0) {
                    return 0;
                } else if (defaultDifference < 0) {
                    return -1;
                } else {
                    return 1;
                }
        }
    }

}

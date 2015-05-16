package lt.ktu.formbackend.utility;

import java.util.Comparator;
import lt.ktu.formbackend.model.Form;

/**
 *
 * @author Lukas
 */
public class FormComparator implements Comparator<Form> {

    private String sortType;
    private String sortOrder;

    public FormComparator(String orderType, String sortOrder) {
        this.sortType = orderType;
        this.sortOrder = sortOrder;
    }

    @Override
    public int compare(Form o1, Form o2) {
        long date1 = Long.parseLong(o1.getDate().replace(" ", "").replace(":", ""));
        long date2 = Long.parseLong(o2.getDate().replace(" ", "").replace(":", ""));
        if (sortOrder.equals("descending")) {
            if (sortType == null) {
                long difference = (date2 - date1);
                if (difference == 0) {
                    return 0;
                } else if (difference < 0) {
                    return -1;
                } else {
                    return 1;
                }
            }
            switch (sortType) {
                case "popularity":
                    return o2.getVotes() - o1.getVotes();

                case "date":
                    long difference = (date2 - date1);
                    if (difference == 0) {
                        return 0;
                    } else if (difference < 0) {
                        return -1;
                    } else {
                        return 1;
                    }

                default:
                    long defaultDifference = (date2 - date1);
                    if (defaultDifference == 0) {
                        return 0;
                    } else if (defaultDifference < 0) {
                        return -1;
                    } else {
                        return 1;
                    }
            }
        } else {
            if (sortType == null) {
                long difference = (date1 - date2);
                if (difference == 0) {
                    return 0;
                } else if (difference < 0) {
                    return -1;
                } else {
                    return 1;
                }
            }
            switch (sortType) {
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

}

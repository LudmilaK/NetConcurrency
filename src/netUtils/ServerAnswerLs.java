package netUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Людмила on 08.05.2017.
 */
public class ServerAnswerLs implements Serializable {
    private static final long serialVersionUID = 1;
    private ArrayList<String> answer = new ArrayList<String>();
    public ArrayList<String> getArrayList() {
        return answer;
    }
}

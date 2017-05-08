package netUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Людмила on 08.05.2017.
 */
public class ServerAnswerCd implements Serializable {
    private static final long serialVersionUID = 1;
    private String answer = "";
    public ServerAnswerCd(String path){
        answer = path;
    }
    public String getAnswer() {
        return answer;
    }
}

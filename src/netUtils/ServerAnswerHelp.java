package netUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Людмила on 04.05.2017.
 */
public class ServerAnswerHelp implements Serializable {
    private static final long serialVersionUID = 1;
    private ArrayList<String> answer = new ArrayList<String>();
    public ServerAnswerHelp(){
        answer.add("cd - перейти в каталог");
        answer.add("help - помощь");
        answer.add("ls - показать файлы в текущей дирректории");
        answer.add("rm - удалить файл");
    }
    public ArrayList<String> getArrayList() {
        return answer;
    }

}

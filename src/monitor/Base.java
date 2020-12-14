package monitor;

import java.util.List;


public abstract class Base {
        //TODO: Usunąć
        Integer type;
        String user = null;
        String password = null;
        String url = null;

        List<String> statementsList;

        public Integer getType() {
                return type;
        }
}

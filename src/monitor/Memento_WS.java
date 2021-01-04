package monitor;

import java.util.List;

/**
 * The type Memento_WS
 * Class stores data that was in the web service before making changes and data newly added to web service.
 */
public class Memento_WS{

     /**
      * The Values.
      */
     List<Object> values;

     /**
      * The Type.
      */
     String type;


     /**
      * Instantiates a new Memento ws.
      *
      * @param values the values
      * @param type   the type
      */
     Memento_WS (List<Object> values, String type){
          this.values = values;
          this.type = type;
     }
}


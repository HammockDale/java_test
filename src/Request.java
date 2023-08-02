import java.io.Serializable;
import java.io.*;


abstract class Request implements Serializable {


    public  abstract String requestName();
}


class DropRecRequest  extends Request  implements Serializable {
    public int id = -1;

     public  String requestName(){
         return "drop row id: "+id;
     }
}



class ReloadRecRequest  extends Request  implements Serializable {

    public  String requestName(){
        return "reload table ";
    }
}


class AddRecRequest  extends Request  implements Serializable {

    public Object[] row = null;

    public  String requestName(){
        return "add row ";
    }
}

class RowRangeRequest  extends Request  implements Serializable {

    public int m;
    public int n;

    public  String requestName(){
        return "range request from " +n + " to " + m ;
    }
}
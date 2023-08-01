import java.io.Serializable;
import java.io.*;


abstract class Request implements Serializable {


    public  abstract String requestName();
}


abstract class Response implements Serializable {
    public String errStatus = null;

    public  abstract String respName();
}

class EmptyResponse extends Response implements Serializable {
    public  String respName(){
        return (errStatus == null) ?"RESP OK" : "RESP ERROR:  "+errStatus;
    }
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
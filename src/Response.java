import java.io.Serializable;

abstract class Response implements Serializable {
    public String errStatus = null;

    public  abstract String respName();
}


class EmptyResponse extends Response implements Serializable {
    public  String respName(){
        return (errStatus == null) ?"RESP OK" : "RESP ERROR:  "+errStatus;
    }
}


class RowRangeResponse  extends Response  implements Serializable {

    public Object[] rows = new Object[0];

    public  String respName(){
        return "range response, lengtr = " + rows.length;
    }
}
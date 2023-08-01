import java.io.*;
import java.net.*;

public class Client {
    public MemoryTable mt;//TODO: remove this field and use network callinstead

    private String host ="localhost";
    private int port = 9999;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public Client(){
        this("localhost",9999);
    }
    public Client(String host, int port){
        this.host = host;
        this.port = port;
    }

    public void connect(){
        try {

            if(socket != null) {
                System.out.println("Client: closing socket");
                socket.close();
            }
            socket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Client: connecting to server "+host+":"+port);
            socket = new Socket(host, port);
            objectOutputStream = new  ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

        }catch(Exception ee){
            ee.printStackTrace();
        }
    }

    public Response call(Request req){
        try {
            if(socket == null || socket.isConnected()) connect();
            return internalCall(req);
        } catch (Exception e) {
            e.printStackTrace();
            connect();
        }
        EmptyResponse ret = new EmptyResponse();
        ret.errStatus = "Server call error";
        return ret;
    }

    private Response internalCall(Request req) throws IOException, ClassNotFoundException {
        Response ret = new EmptyResponse();

        System.out.println("Client: sending request "+req.requestName());

        if(socket == null || !socket.isConnected() || socket.isClosed()){
            connect();
        }




/*     //// через файл для прокерки
        //Сериализация в файл с помощью класса ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                new FileOutputStream("req.out"));
        objectOutputStream.writeObject(req);
        objectOutputStream.close();

        // Востановление из файла с помощью класса ObjectInputStream
        ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream("req.out"));
        req = (Request) objectInputStream.readObject();
        objectInputStream.close();
*/



/*  //// тест вызова через локально доустпную таблицу баазы данных
        if(req instanceof DropRecRequest){
           DropRecRequest dropReq=  (DropRecRequest)req;
           mt.dropRecord(dropReq.id);
        }
*/
        System.out.println("Client:  sending to server request"+req.requestName());
        objectOutputStream.writeObject(req);
        System.out.println("Client:  request sent to server, flushing stream");
        objectOutputStream.flush();
        Response rsp = (Response)objectInputStream.readObject();
        System.out.println("CLient: received response from sever "+rsp.getClass().getName());


        return ret;
    }


}

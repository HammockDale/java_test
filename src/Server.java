import java.io.*;
import java.net.*;

public class Server implements MyDebug {

    private int port = 9999;
    private final ServerSocket serverSocket;

    MemoryTable mt;


    public Server(int port) throws IOException {
        this.port = port;
        serverSocket = new ServerSocket(port);
    }

    public int getPort(){
        return port;
    }

    public void startService(){
        if (DEBUG > 0) System.out.println("Server: starting service");
        Thread thr = new Thread(){
            public void run(){
                if (DEBUG > 0) System.out.println("Server: connection accept thread run");
                while (true) {
                    if (DEBUG > 0) System.out.println("Server: waiting fro client connection");
                    try (Socket socket = serverSocket.accept()){
                       if (DEBUG > 0) System.out.println("Server: accepted connection from client: sock buff size="+socket.getSendBufferSize());


                        try {
                            socket.setTcpNoDelay(true);

                           ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                            while(socket != null && socket.isConnected() && !socket.isClosed()) {
                                Response ret = new EmptyResponse();
                                Request req = (Request) objectInputStream.readObject();
                                if (DEBUG > 0) System.out.println("Server: received request " + req.requestName());
                                try {
                                    if (DEBUG > 0) System.out.println("Server: processing request");

                                    if (req instanceof DropRecRequest) {
                                        if (DEBUG > 0) System.out.println("Server: drop");
                                        DropRecRequest dropReq = (DropRecRequest) req;
                                        mt.dropRecord(dropReq.id);
                                    } else if (req instanceof ReloadRecRequest) {
                                        if (DEBUG > 0) System.out.println("Server: reload");
                                        ReloadRecRequest reloadReq = (ReloadRecRequest) req;
                                        mt.reload();
                                    } else if (req instanceof AddRecRequest) {
                                        if (DEBUG > 0) System.out.println("Server: add row");
                                        AddRecRequest addReq = (AddRecRequest) req;
                                        int id = mt.addRecord(addReq.row);
                                        AddRecResponse addReqResponse = new AddRecResponse();
                                        addReqResponse.id = id;
                                        ret = addReqResponse;
                                    } else if (req instanceof RowRangeRequest) {
                                        if (DEBUG > 0) System.out.println("Server: getRange");
                                        RowRangeRequest rangeReq = (RowRangeRequest) req;
                                        RowRangeResponse rangeResp = new RowRangeResponse();
                                        rangeResp.rows = mt.getRangeMT(rangeReq.n, rangeReq.m);
                                        ret = rangeResp;
                                    } else if (req instanceof SortRequest) {
                                        if (DEBUG > 0) System.out.println("Server: add row");
                                        SortRequest sortReq = (SortRequest) req;
                                        mt.sort(sortReq.fn, sortReq.dir);
                                    } if (req instanceof GetRangeRowByIDRequest) {
                                        if (DEBUG > 0) System.out.println("Server: get row number by id");
                                        GetRangeRowByIDRequest rowByIdReq = (GetRangeRowByIDRequest) req;
                                        GetRangeRowByIDResponse rowByIdResp = new GetRangeRowByIDResponse();
                                        rowByIdResp.row = mt.getRangeRowByID(rowByIdReq.id);
                                        ret = rowByIdResp;
                                    }

                                    if (DEBUG > 0) System.out.println("Server: request processed");
                                } finally {
                                    if (DEBUG > 0) System.out.println("Server: sending response");
                                    objectOutputStream.writeObject(ret);
                                    if (DEBUG > 0) System.out.println("Server: flushing socket output");
                                    objectOutputStream.flush();
                                }

                            }
                        } catch(Exception e2){
                            e2.printStackTrace();
                            if(socket != null) {
                                if (DEBUG > 0) System.out.println("Server: closing socket");
                                socket.close();
                            }
                        } finally {
                            if (DEBUG > 0) System.out.println("Server: client socket communication loop is broken, finishing");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thr.start();
    }
}

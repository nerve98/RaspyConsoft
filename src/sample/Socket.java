package sample;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class Socket implements Runnable {

    private static String lastSend;
    private static final int PORT = 5764;
    private IoConnector connector;
    private MyMinaClientHandler handler;
    private boolean invia=false;

    public Socket() {

        connector = new NioSocketConnector();
        connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 50);
        connector.getSessionConfig().setReadBufferSize(2048);

        //Logger
        connector.getFilterChain().addLast("logger", new LoggingFilter());

        //ProtocolFilter TextLineCodecFilter
        connector.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
        handler = new MyMinaClientHandler("");
        connector.setHandler(handler);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        lastSend = sdf.format(timestamp);

    }


    @Override
    public void run() {
        JsonSocket json = new JsonSocket();
        while (true) {
            //Set della classe Handler MyMinaClientHandler con il testo in input
            //String listaBadges = Sqlite.getBadgeQuery(lastSend);

            if (connector.isDisposed()) {
                System.out.println("La connessione è chiusa");

                //System.out.println("Connessione riaperta");
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String lastQuery = sdf.format(timestamp);

            ArrayList<Movement> result = Sqlite.getBadgeQuery(lastSend);
            if(result.size()>0) {
                for (int i = 0; i < result.size(); i++) {

                    json.appendJson(result.get(i));

                }

                handler.setMessage(json.cleanAndGet(), lastQuery);
                invia=true;


            }

            if(invia){
                ConnectFuture future = connector.connect(new InetSocketAddress("10.0.1.153", PORT));
                future.awaitUninterruptibly();

                if (future.isConnected()) {
                    System.out.println("Il socket è connesso!!!");
                    IoSession session = future.getSession();
                    session.getConfig().setUseReadOperation(true);
                    session.getCloseFuture().awaitUninterruptibly();

                    System.out.println("After Writing");
                    invia=false;
                }
                else{
                    System.out.println("Il socket non è connesso");
                }
            }


            /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            lastSend = sdf.format(timestamp);*/
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }/*connector.setHandler(new MyMinaClientHandler(Lista.remove()));
                ConnectFuture future = connector.connect(new InetSocketAddress("10.0.1.153", PORT));
                future.awaitUninterruptibly();

                if (!future.isConnected()) {
                    return;
                }
                IoSession session = future.getSession();
                session.getConfig().setUseReadOperation(true);
                session.getCloseFuture().awaitUninterruptibly();

                System.out.println("After Writing");
            }

            //connector.dispose();
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }

    public static void setLastSend(String last){
        lastSend=last;
    }
}

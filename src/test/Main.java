public class Main 
{
    public static void main(String[] arg) throws Exception {
        AioServer server = new AioServer();
        try {
            server.Start((short)12345);
        } catch(Exception e) {
            server.Close();
        }
    }
}
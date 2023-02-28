import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.*;
import java.io.*;
import java.lang.*;
public class server extends JFrame {

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    private JLabel heading= new JLabel("Client Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);

    public server()
    {
        try{
server=new ServerSocket(7779);
            System.out.println("server is ready");
            System.out.println("Waiting");
            socket=server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //byte to char
            out=new PrintWriter(socket.getOutputStream());
            creategui();
            eventhandle();
            startreading();
            //startwriting();
    }
    catch(Exception e)
    {
        System.out.println("server port isuue");
    }
    }
    public  void startreading()
    {
        Runnable r1=()->{
            System.out.println("reader started");
            try{
            while(true) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("client terminating");
                        JOptionPane.showMessageDialog(this,"Server terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    //System.out.println("Client :" + msg);
                messageArea.append("Client : "+msg+"\n");
                messageArea.setEditable(false);
                }
            }
            catch(Exception e)
            {
                System.out.println("Connection closed");
            }
        };
        new Thread(r1).start();
    }

    private void eventhandle()
    {
        messageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==10)
                {
                    String contenttosend=messageInput.getText();
                    messageArea.append("Me :"+contenttosend+"\n");
                    out.println(contenttosend);
                    if(contenttosend=="exit")
                    {messageInput.setEnabled(false);}
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }

            }
        });
    }
    private void creategui()
    {
        this.setTitle("Server Messager[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //on clicking close of jframe it will exit
        this.setVisible(true);

        //coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
      //  heading.setIcon(new ImageIcon("img.png"));
//        heading.setHorizontalTextPosition(SwingConstants.CENTER);
//        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        messageArea.setEditable(false);
        messageArea.setCaretPosition(messageArea.getDocument().getLength());
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        //frame ka border layout
        this.setLayout(new BorderLayout());
        JScrollPane shivam=new JScrollPane(messageArea);
        //  this.add(messageArea,BorderLayout.CENTER);
        this.add(heading,BorderLayout.NORTH);
        this.add(messageInput,BorderLayout.SOUTH);
        this.add(shivam,BorderLayout.CENTER);
        // this.add(messageInput,BorderLayout.SOUTH);

    }
    public void startwriting()
    {
        Runnable r2=()->
        {
try {
    while (!socket.isClosed()) {
        BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
        String content = br1.readLine();
        out.println(content);
        out.flush();
        if(content.equals("exit"))
        {
            socket.close();
            break;
        }

    }
}
            catch(Exception e)
            {
                System.out.println("connection closed");
            }

        };
        new Thread(r2).start();
    }

    public static void main(String[] args)
    {
        System.out.println("this is server");
        new server();
    }
}

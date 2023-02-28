import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class client extends JFrame{
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    //declare components
    private JLabel heading= new JLabel("Server Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);


    public client()
    {
        try{
            System.out.println("sending request to server");
            socket=new Socket("127.0.0.2",7779);
            System.out.println("connection done");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            //byte to char
           out=new PrintWriter(socket.getOutputStream());
            creategui();
            eventhandle();
           startreading();
//            startwriting();


        }
        catch (Exception e)
        {
            System.out.println("Connection closed");
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
                        System.out.println("server terminating");
                        JOptionPane.showMessageDialog(this,"Server terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    //System.out.println("Server :" + msg);
                    messageArea.append("Server :"+msg+"\n");
                   // messageInput.requestFocus();
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
                    if(contenttosend=="exit")
                    { messageInput.setEnabled(false);
                    }
                    out.println(contenttosend);
                    if(contenttosend=="exit")
                    { messageInput.setEnabled(false);
                    }
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }

            }
        });
    }
    private void creategui()
    {
        this.setTitle("Client Messager[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //on clicking close of jframe it will exit
        this.setVisible(true);

        //coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
       heading.setIcon(new ImageIcon("img.png"));
//        heading.setHorizontalTextPosition(SwingConstants.CENTER);
//        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        messageArea.setEditable(false);
      // messageArea.setCaretPosition(messageArea.getDocument().getLength());
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));


        //frame ka border layout
        this.setLayout(new BorderLayout());
       JScrollPane shivam=new JScrollPane(messageArea);
      //  this.add(messageArea,BorderLayout.CENTER);
      //  JScrollBar v=shivam.getVerticalScrollBar();
      //  v.setValue(v.getMaximum());
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
                    System.out.println("Connection closed");
                }

        };
        new Thread(r2).start();
    }
    public static void main(String[] args)
    {
        System.out.println("this is client");
        new client();
    }
}

package server;


import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ChatServer implements Runnable,ActionListener{
	private JFrame frame;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private JTextArea jta;
	private JScrollPane jscrlp;
	private JTextField jtfInput;
	private JButton jBtnSend;
	
	public ChatServer(){
		frame =new JFrame("Chat Server");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		frame.setSize(300, 320);
		Thread myThread=new Thread(this);//this will invoke the thread corresponding to the
		//chat server as and when an object of chat server is made.
		myThread.start();
		jta=new JTextArea(15,15);
		jta.setEditable(false);
		jta.setLineWrap(true);
		jscrlp=new JScrollPane(jta);
		jtfInput=new JTextField(15);
		jtfInput.addActionListener(this);
		jBtnSend=new JButton("Send");
		jBtnSend.addActionListener(this);
		frame.getContentPane().add(jscrlp);
		frame.getContentPane().add(jBtnSend);
		frame.getContentPane().add(jtfInput);
		frame.setVisible(true);
	}

	public void run() {
		try {
			serverSocket=new ServerSocket(4444);
			clientSocket=serverSocket.accept();
			//here we can consider serversocket as a client which will 
			//accept from the actual client and send to it(serverSocket.accept())
			outputStream=new ObjectOutputStream(clientSocket.getOutputStream());
			inputStream=new ObjectInputStream(clientSocket.getInputStream());
			while(true){
				Object input=inputStream.readObject();
				jta.setText(jta.getText()+"Client says :" +(String)input+"\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent ae) {
			if(ae.getActionCommand().equals("Send") || ae.getSource() instanceof JTextField){
				try {
					outputStream.writeObject(jtfInput.getText());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				jta.setText(jta.getText()+"You say: "+ jtfInput.getText()+"\n");
				jtfInput.setText("");
			}
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){
			//this method invokes the thread that is related to the specified object after
			//the swing operations are performed.
			public void run(){
				new ChatServer();
			}
		});
	}
}

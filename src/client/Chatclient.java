package client;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Chatclient implements Runnable,ActionListener{
	private JFrame frame;
	private Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private JTextArea jta;
	private JScrollPane jscrlp;
	private JTextField jtfInput;
	private JButton jBtnSend;
	
	public Chatclient(){
		frame =new JFrame("Chat Client");
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
			socket=new Socket("localhost",4444);
			outputStream=new ObjectOutputStream(socket.getOutputStream());
			inputStream=new ObjectInputStream(socket.getInputStream());
			while(true){
				Object input=inputStream.readObject();
				jta.setText(jta.getText()+"Server says: " +(String)input+"\n");
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
				//ae.getSource() instanceof JTextField ..this performs the action performed when we press enter 
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
				new Chatclient();
			}
		});
	}
}



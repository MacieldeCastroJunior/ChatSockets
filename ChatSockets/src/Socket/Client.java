package Socket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Maciel
 */
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;


public class Client extends JFrame implements ActionListener, KeyListener {

    private static final long serialVersionUID = 1L;
    private JTextArea text;
    private JTextField txtMsg;
    private JButton btnSend;
    private JButton btnLogout;
    private JLabel lblHistorico;
    private JLabel lblMsg;
    private JPanel pnlContent;
    private Socket socket;
    private OutputStream ou;
    private Writer ouw;
    private BufferedWriter bfw;
    private JTextField txtIP;
    private JTextField txtPort;
    private JTextField txtName;

    /*
    *   Bloco de Criação do Frame
    *
    */
    public Client() throws IOException {
        JLabel lblMessage = new JLabel("Configure a Conexão");
        txtIP = new JTextField("IP");
        txtPort = new JTextField("Porta");
        txtName = new JTextField("Nome");
        Object[] texts = {lblMessage, txtIP, txtPort, txtName};
        JOptionPane.showMessageDialog(null, texts);
        pnlContent = new JPanel();
        text = new JTextArea(10, 20);
        text.setEditable(false);
        text.setBackground(new Color(240, 240, 240));
        txtMsg = new JTextField(20);
        lblHistorico = new JLabel("Historico");
        lblMsg = new JLabel("Messagem");
        btnSend = new JButton("Enviar");
        btnSend.setToolTipText("Mensagem Enviado");
        btnLogout = new JButton("Desconectar");
        btnLogout.setToolTipText("Desconectador do chat");
        btnSend.addActionListener(this);
        btnLogout.addActionListener(this);
        btnSend.addKeyListener(this);
        txtMsg.addKeyListener(this);
        JScrollPane scroll = new JScrollPane(text);
        text.setLineWrap(true);
        pnlContent.add(lblHistorico);
        pnlContent.add(scroll);
        pnlContent.add(lblMsg);
        pnlContent.add(txtMsg);
        pnlContent.add(btnLogout);
        pnlContent.add(btnSend);
        pnlContent.setBackground(Color.LIGHT_GRAY);
        text.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
        txtMsg.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
        setTitle(txtName.getText());
        setContentPane(pnlContent);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(250, 300);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void connect() throws IOException {
        socket = new Socket(txtIP.getText(), Integer.parseInt(txtPort.getText()));
        ou = socket.getOutputStream();
        ouw = new OutputStreamWriter(ou);
        bfw = new BufferedWriter(ouw);
        bfw.write(txtName.getText() + "\r\n");
        bfw.flush();
    }

    public void sendMessage(String msg) throws IOException {
        if (msg.equals("Conectado")) {
            bfw.write("Desconectado \r\n");
            text.append("Desconectado \r\n");
        } else {
            bfw.write(msg + "\r\n");
            text.append(txtName.getText() + " diz -> " + txtMsg.getText() + "\r\n");
        }
        bfw.flush();
        txtMsg.setText("");
    }

    public void listen() throws IOException {
        InputStream in = socket.getInputStream();
        InputStreamReader inr = new InputStreamReader(in);
        BufferedReader bfr = new BufferedReader(inr);
        String msg = "";
        while (!"Logout".equalsIgnoreCase(msg)) {
            if (bfr.ready()) {
                msg = bfr.readLine();
                if (msg.equals("Saiu")) {
                    text.append("O Servidor Desconectou! \r\n");
                } else {
                    text.append(msg + "\r\n");
                }
            }
        }
    }

    /**
     * * * Método usado quando o usuário clica em sair
     *
     * @throws IOException retorna IO Exception caso dê algum erro.
     */
    public void sair() throws IOException {
        sendMessage("Logout");
        bfw.close();
        ouw.close();
        ou.close();
        socket.close();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getActionCommand().equals(btnSend.getActionCommand())) {
                sendMessage(txtMsg.getText());
            } else if (e.getActionCommand().equals(btnLogout.getActionCommand())) {
                sair();
            }
        } catch (IOException e1) {
            // método gerado automaticamente
            e1.printStackTrace();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                sendMessage(txtMsg.getText());
            } catch (IOException e1) {
               //método gerado automaticamente
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
// TODO Auto-generated method stub 
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
// método gerado automaticamente
    }
    //Método Main 
    public static void main(String[] args) throws IOException {
        Client app = new Client();
        app.connect();
        app.listen();
    }

}

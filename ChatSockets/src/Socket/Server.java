package Socket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Maciel
 */
public class Server extends Thread {

    private static ArrayList<BufferedWriter> clients;
    private static ServerSocket server;
    private String nome;
    private Socket con;
    private InputStream in;
    private InputStreamReader inr;
    private BufferedReader bfr;

    public Server(Socket con) {
        this.con = con;
        try {
            in = con.getInputStream();
            inr = new InputStreamReader(in);
            bfr = new BufferedReader(inr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * Método de Execução da Thread
     */
    public void run() {
        try {
            String msg;
            OutputStream ou = this.con.getOutputStream();
            Writer ouw = new OutputStreamWriter(ou);
            BufferedWriter bfw = new BufferedWriter(ouw);
            clients.add(bfw);
            nome = msg = bfr.readLine();
            while (!"Logout".equalsIgnoreCase(msg) && msg != null) {
                msg = bfr.readLine();
                sendToAll(bfw, msg);
                System.out.println(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToAll(BufferedWriter bwOutput, String msg) throws IOException {
        BufferedWriter bwS;
        for (BufferedWriter bw : clients) {
            bwS = (BufferedWriter) bw;
            if (!(bwOutput == bwS)) {
                bw.write(nome + " -> " + msg + "\r\n");
                bw.flush();
            }
        }
    }

    /***
     * Método Main
     * @param args
     */
    public static void main(String[] args) {
        try {
            //Cria os objetos necessário para instânciar o server 
            JLabel lblMessage = new JLabel("Porta de Conexão:");
            JTextField txtPort = new JTextField("8000");
            Object[] texts = {lblMessage, txtPort};
            JOptionPane.showMessageDialog(null, texts);
            server = new ServerSocket(Integer.parseInt(txtPort.getText()));//Método Recebe apenas porta de conexão, não foi informato IP de conexão pois vai atribir o ip atual do servidor
            clients = new ArrayList<BufferedWriter>();
            JOptionPane.showMessageDialog(null, "Servidor operando na porta: " + txtPort.getText());
            while (true) {
                System.out.println("Aguardando Conexões...");
                Socket con = server.accept();
                System.out.println("Clientes Conectado...");
                Thread t = new Server(con);
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// Fim método Main
}



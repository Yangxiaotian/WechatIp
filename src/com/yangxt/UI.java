package com.yangxt;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class UI implements ActionListener{
	JFrame frame = new JFrame("获取微信公众号服务器IP");
	JPanel panel = new JPanel();
	
	JLabel label1 = new JLabel("appid");
	JTextField appidFld  = new JTextField();
	JLabel label2 = new JLabel("secret");
	JTextField secretFld  = new JTextField();
	
	
	JScrollPane scroll = new JScrollPane();
	JLabel label4 = new JLabel("IP");
	JTextArea ipArea = new JTextArea();
	
	
	JButton sendBtn = new JButton("获取");
	public void createUI(){
		frame.setSize(250,680);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		panel.setLayout(new FlowLayout());
		panel.add(label1);
		appidFld.setColumns(15);
		panel.add(appidFld);
		panel.add(label2);
		secretFld.setColumns(15);
		panel.add(secretFld);
		
		ipArea.setColumns(20);
		ipArea.setRows(30);
		ipArea.setEditable(false);
		ipArea.setText("IP列表");
		panel.add(new JScrollPane(ipArea));
		sendBtn.addActionListener(this);
		panel.add(sendBtn);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null); 
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		String appid = appidFld.getText();
		String secret = secretFld.getText();
		String result = HttpRequest.sendGet("https://api.weixin.qq.com/cgi-bin/token", "grant_type=client_credential&appid="+appid+"&secret="+secret);
		Pattern p = Pattern.compile("\"access_token\":\"(.*?)\"");
	    Matcher m = p.matcher(result);
	    if(m.find()) {
	    	String access_token = m.group(1);
	    	String result2 = HttpRequest.sendGet("https://api.weixin.qq.com/cgi-bin/getcallbackip", "access_token="+access_token);
	    	Pattern p2 = Pattern.compile("\\[(.*\\s*)\\]");
	    	Matcher m2 = p2.matcher(result2);
	    	if(m2.find()) {
	    		String ipStr = m2.group(1);
	    		String[] ipArr = ipStr.split(",");
	    		String ip = "";
	    		for(String a: ipArr) {
	    			ip+=a+"\n";
	    		}
	    		ipArea.setText(ip);
	    	}
	    }
	}
}

package Pantallas;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;

import Clases.AdministradorSgt;
import Clases.TotemSgt;

import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollBar;

public class Principal extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldContrato;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal frame = new Principal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Principal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 643, 422);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textFieldContrato = new JTextField();
		textFieldContrato.setBounds(10, 24, 158, 20);
		contentPane.add(textFieldContrato);
		textFieldContrato.setColumns(10);
		
		JLabel lblContrato = new JLabel("Contrato");
		lblContrato.setBounds(10, 11, 118, 14);
		contentPane.add(lblContrato);
		
		
		
		final JTextArea txtrLogs = new JTextArea();		
		txtrLogs.setBounds(21, 87, 584, 113);
		contentPane.add(txtrLogs);
		
		
		
		
		final JButton btnMigrarContrato = new JButton("Migrar Contrato");
		btnMigrarContrato.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String contrato=textFieldContrato.getText();
				if(!contrato.isEmpty()) {	
					
					 Thread thread = new Thread(new Runnable() {
				            @Override
				            public void run() {
				              				 
				                    try {
				                    	btnMigrarContrato.setEnabled(false);
				                    	TotemSgt totem=new TotemSgt(textFieldContrato.getText(),txtrLogs); 
										AdministradorSgt.guardarTotem(totem,txtrLogs);
										 btnMigrarContrato.setEnabled(true);
				                    } catch (Exception e) {
				                    	e.printStackTrace();
				                    }
				                
				            }
				        });
				                
				            	
				            
					 thread.start();
					
					
					
					
					
					
				}else {
					String contenidoViejo=txtrLogs.getText();
					txtrLogs.setText(contenidoViejo+"\nCampo no puede estar vacío");
				}
				
				
			}
		});
		btnMigrarContrato.setBounds(447, 23, 158, 23);
		contentPane.add(btnMigrarContrato);
		
		JScrollBar scrollBar = new JScrollBar();
		scrollBar.setBounds(23, 72, 594, 149);
		contentPane.add(scrollBar);
		
	
	}
}

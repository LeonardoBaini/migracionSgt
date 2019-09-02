package Pantallas;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import com.jgoodies.forms.factories.DefaultComponentFactory;

import Clases.AdministradorSgt;
import Clases.TotemSgt;

import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.ScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

import java.awt.Color;
import java.awt.Font;
import javax.swing.border.MatteBorder;
import java.awt.Window.Type;
import java.awt.SystemColor;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;

public class ppal extends JFrame {

	public static JTextArea txtrJtextarealog =null;
	public static JTextArea txtrJtextareasystemlog=null;
	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ppal frame = new ppal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});*/

	/**
	 * Create the frame.
	 */
	public ppal() {
		setFont(new Font("Dialog", Font.BOLD, 13));
		setIconImage(Toolkit.getDefaultToolkit().getImage(ppal.class.getResource("/Imagenes/LogoProse1.png")));
		setResizable(false);
		setAlwaysOnTop(true);
		setType(Type.POPUP);
		setTitle("CUSTOM TO SGT MIGRATOR V1.0");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1194, 550);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.info);
		contentPane.setForeground(Color.BLACK);
		contentPane.setBorder(new TitledBorder(new MatteBorder(4, 4, 4, 4, (Color) new Color(255, 200, 0)), "CUSTOM TO SGT MIGRATOR", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(33, 37, 187, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblContrato = DefaultComponentFactory.getInstance().createLabel("Contrato");
		lblContrato.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblContrato.setBounds(33, 22, 95, 14);
		contentPane.add(lblContrato);
		
		JButton btnMigrarCustomA = new JButton("Migrar Custom a SGT");
		
		btnMigrarCustomA.setIcon(new ImageIcon(ppal.class.getResource("/Imagenes/icons8-up-down-arrow-34.png")));
		btnMigrarCustomA.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnMigrarCustomA.setForeground(SystemColor.inactiveCaptionText);
		btnMigrarCustomA.setBackground(SystemColor.scrollbar);
		btnMigrarCustomA.setBounds(233, 22, 213, 37);
		contentPane.add(btnMigrarCustomA);
		
		JButton btnRollback = new JButton("Rollback");
		btnRollback.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String contrato=textField.getText();
				if(contrato.isEmpty()) {
					txtrJtextareasystemlog.setText("No se admiten contratos vacíos...");
				}else {
					AdministradorSgt.hacerRollback(contrato,txtrJtextareasystemlog);
				}
				
			}
		});
		btnRollback.setIcon(new ImageIcon(ppal.class.getResource("/Imagenes/icons8-rollback-24.png")));
		btnRollback.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnRollback.setForeground(SystemColor.inactiveCaptionText);
		btnRollback.setBackground(SystemColor.scrollbar);
		btnRollback.setBounds(517, 22, 138, 35);
		contentPane.add(btnRollback);
		
		
		
		
		JLabel lblLiveLog = DefaultComponentFactory.getInstance().createTitle("Live log");
		lblLiveLog.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblLiveLog.setBounds(217, 68, 95, 14);
		contentPane.add(lblLiveLog);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(33, 94, 166, 337);
		contentPane.add(scrollPane);
		
		txtrJtextarealog = new JTextArea();
		txtrJtextarealog.setEditable(false);
		scrollPane.setViewportView(txtrJtextarealog);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(216, 94, 933, 337);
		contentPane.add(scrollPane_1);
		
		txtrJtextareasystemlog = new JTextArea();
		scrollPane_1.setViewportView(txtrJtextareasystemlog);
		//DefaultCaret caret = (DefaultCaret)txtrJtextarealog.getCaret();
		//caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		//txtrJtextarealog.setText("jTextAreaLog");
		
		btnMigrarCustomA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				txtrJtextareasystemlog.setText("Iniciando");
				
				String contrato=textField.getText();
				if(contrato.isEmpty()) {
					txtrJtextareasystemlog.setText("No se admiten contratos vacíos...");
				}
				else {
				if(AdministradorSgt.testearConexionBases(txtrJtextareasystemlog)) {
					
					TotemSgt totem=new TotemSgt(contrato); 					
					AdministradorSgt.guardarTotem(totem, txtrJtextareasystemlog);	
					AdministradorSgt.guardarServiceCameras(totem,txtrJtextareasystemlog);
					AdministradorSgt.guardarServiceDevices(totem,txtrJtextareasystemlog);	
					
					
				}
				}// fin sino está  vacío
				txtrJtextareasystemlog.append("\n***************FIN DEL PROGRAMA***************");
			}
		});
	}
}

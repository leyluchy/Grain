package grain;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.io.File;
import java.io.IOException;
import java.awt.GridLayout;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.Image;
public class GrainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GrainFrame frame = new GrainFrame();
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
	public GrainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnGetFile = new JButton("Get File");
		btnGetFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
		        int result = fc.showOpenDialog(null);
		        if (result == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            //String sname = file.getAbsolutePath();
		            BufferedImage img = null;
		            try {
		                img = ImageIO.read(file);
		            } catch (IOException ex) {
		                ex.printStackTrace();
		            }
		            Image dimg = img.getScaledInstance(124,145,Image.SCALE_SMOOTH);
		            ImageIcon imageIcon = new ImageIcon(dimg);
		            JLabel imageEncriptada = new JLabel("",imageIcon, JLabel.CENTER);
		    		imageEncriptada.setBounds(26,25,124,145);
		    		contentPane.add(imageEncriptada);
		            contentPane.revalidate();
		            contentPane.repaint();
		        }
			}
		});
		btnGetFile.setBounds(37, 203, 89, 23);
		contentPane.add(btnGetFile);
		
		JButton btnSaveFile = new JButton("Save File");
		btnSaveFile.setBounds(309, 203, 89, 23);
		contentPane.add(btnSaveFile);
		
		JButton btnEncryptdecrypt = new JButton("Encrypt/Decrypt");
		btnEncryptdecrypt.setBounds(150, 203, 138, 23);
		contentPane.add(btnEncryptdecrypt);
		
		textField = new JTextField();
		textField.setBounds(176, 51, 86, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(176, 122, 86, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(197, 26, 46, 14);
		contentPane.add(lblPassword);
		
		JLabel lblIv = new JLabel("IV");
		lblIv.setHorizontalAlignment(SwingConstants.CENTER);
		lblIv.setBounds(197, 97, 46, 14);
		contentPane.add(lblIv);
		
		JLabel imageDesencriptada = new JLabel("");
		imageDesencriptada.setBounds(287, 26, 124, 145);
		contentPane.add(imageDesencriptada);
	}
}

package grain;

import java.awt.BorderLayout;
//import ImageHandler.java;
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
	private JTextField iv;
	private JTextField key;
	BufferedImage imgOriginal = null;
	BufferedImage imgCifrada = null;
	byte[] imageInByte;

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
		
		iv = new JTextField();
		iv.setBounds(176, 51, 86, 20);
		contentPane.add(iv);
		iv.setColumns(10);
		
		key = new JTextField();
		key.setBounds(176, 122, 86, 20);
		contentPane.add(key);
		key.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(197, 26, 46, 14);
		contentPane.add(lblPassword);
		
		JLabel lblIv = new JLabel("IV");
		lblIv.setHorizontalAlignment(SwingConstants.CENTER);
		lblIv.setBounds(197, 97, 46, 14);
		contentPane.add(lblIv);
		
		
		//Boton que obtiene la imagen desde el archivo
		JButton btnGetFile = new JButton("Get File");
		btnGetFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
		        int result = fc.showOpenDialog(null);
		        if (result == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            //String sname = file.getAbsolutePath();
		            //BufferedImage img = null;
		            try {
		            	imgOriginal = ImageIO.read(file);
		            } catch (IOException ex) {
		                ex.printStackTrace();
		            }
		            Image dimg = imgOriginal.getScaledInstance(124,145,Image.SCALE_SMOOTH);
		            ImageIcon imageIcon = new ImageIcon(dimg);
		            //se crea el Jlabel q contiene la imagen original ya con la imagen adentro
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
		
		//Boton de cifrado/descifrado
		JButton btnEncryptdecrypt = new JButton("Encrypt/Decrypt");
		btnEncryptdecrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 try {
					 	//paso la imagen original de BufferedImage a ByteArray
					 	imageInByte = ImageHandler.imageToByteArray(imgOriginal);
					 	//creo el cipher
					 	Cypher c = new Cypher(StringHandler.StringToByteArray(key.getText()),StringHandler.StringToByteArray(iv.getText()),imageInByte);
					 	//obtengo la imagen cifrada
					 	imgCifrada = ImageHandler.byteArrayToImage(c.getXored());
					 	
		            } catch (IOException ex) {
		                ex.printStackTrace();
		            } catch (MuchosOPocosBytesException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (LargosDiferentesException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 	Image cimg = imgCifrada.getScaledInstance(124, 145,Image.SCALE_SMOOTH);
		            ImageIcon imageIcon = new ImageIcon(cimg);
		            //creo el JLabel q contiene la imagen encriptada con la imagen encriptada
		            JLabel imageDesencriptada = new JLabel("",imageIcon, JLabel.CENTER);
		    		imageDesencriptada.setBounds(287, 26, 124, 145);
		    		contentPane.add(imageDesencriptada);
		            contentPane.revalidate();
		            contentPane.repaint();
			}
		});
		btnEncryptdecrypt.setBounds(150, 203, 138, 23);
		contentPane.add(btnEncryptdecrypt);
		
		//Boton que guarda la imagen cifrada
		JButton btnSaveFile = new JButton("Save File");
		btnSaveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//aca lo que pasa cuando clickeo Guardar
			}
		});
		btnSaveFile.setBounds(309, 203, 89, 23);
		contentPane.add(btnSaveFile);
		
		
		
	}
}

package grain;

import java.awt.BorderLayout;
import java.awt.Component;
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
import javax.swing.JOptionPane;
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
	private BufferedImage imgOriginal = null;
	private BufferedImage imgCifrada = null;
	byte[] imageInByte;
	Cypher cypher;
	long bytes=0;
	/*
	JLabel lblPassword;
	JLabel lblIv;
	JLabel imageEncriptada;
	JLabel imageDesencriptada;
	JButton btnGetFile;
	JButton btnEncryptdecrypt;
	JButton btnSaveFile;
	ImageIcon imageIcon;
	*/
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
		
		
		key = new JTextField();
		key.setBounds(176, 51, 86, 20);
		contentPane.add(key);
		key.setColumns(10);
		
		iv = new JTextField();
		iv.setBounds(176, 107, 86, 20);
		contentPane.add(iv);
		iv.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setBounds(176, 26, 86, 14);
		contentPane.add(lblPassword);
		
		JLabel lblIv = new JLabel("IV");
		lblIv.setHorizontalAlignment(SwingConstants.CENTER);
		lblIv.setBounds(197, 82, 46, 14);
		contentPane.add(lblIv);
		
		JLabel imageEncriptada = new JLabel("", JLabel.CENTER);
		imageEncriptada.setBounds(26,25,124,145);
		contentPane.add(imageEncriptada);
        contentPane.revalidate();
        contentPane.repaint();
        
        JLabel imageDesencriptada = new JLabel("", JLabel.CENTER);
		imageDesencriptada.setBounds(287, 26, 124, 145);
		contentPane.add(imageDesencriptada);
        contentPane.revalidate();
        contentPane.repaint();
        
        JLabel label = new JLabel("");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(37, 236, 361, 14);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(176, 172, 86, 14);
		contentPane.add(label_1);
		
		
		//Boton que obtiene la imagen desde el archivo
		JButton btnGetFile = new JButton("Get File");
		btnGetFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
		        int result = fc.showOpenDialog(null);
		        if (result == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            try {
		            	imgOriginal = ImageIO.read(file);
		            } catch (IOException ex) {
		            	JOptionPane.showMessageDialog(GrainFrame.this, "Error al leer el archivo bmp", "Error", JOptionPane.ERROR_MESSAGE);
		                //ex.printStackTrace();
		            }
		            
		            ImageIcon imageIcon = new ImageIcon(imgOriginal.getScaledInstance(124,145,Image.SCALE_SMOOTH));
		            imageEncriptada.setIcon(imageIcon);
		            //Se saca la imagen encriptada anterior cuando se carga un nuevo archivo
		            imageDesencriptada.setIcon(null);
		            label_1.setText("");
		        }
			}
		});
		btnGetFile.setBounds(37, 203, 89, 23);
		contentPane.add(btnGetFile);
		
		//Boton de cifrado/descifrado
		JButton btnEncryptdecrypt = new JButton("Encrypt/Decrypt");
		btnEncryptdecrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(imgOriginal == null){
				    JOptionPane.showMessageDialog(null, "Debe cargar una imagen para cifrar", "Imagen Inválida", JOptionPane.ERROR_MESSAGE);
				    return;
				} 
				try {
					 	//paso la imagen original de BufferedImage a ByteArray
					 	imageInByte = ImageHandler.imageToByteArray(imgOriginal);
					 	//creo el cipher
					 	bytes=bytes+cypher.prepareCypher(imageInByte);
					 	//obtengo la imagen cifrada
					 	imgCifrada = ImageHandler.byteArrayToImage(cypher.getXored());
					 	label.setText("Bytes procesados: "+bytes);
					 	
		            } catch (IOException ex) {
		                ex.printStackTrace();
					} catch (LargosDiferentesException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            ImageIcon imageIcon = new ImageIcon(imgCifrada.getScaledInstance(124, 145,Image.SCALE_SMOOTH));
		            //creo el JLabel q contiene la imagen encriptada con la imagen encriptada
		            imageDesencriptada.setIcon(imageIcon);
			}
		});
		btnEncryptdecrypt.setBounds(150, 203, 138, 23);
		contentPane.add(btnEncryptdecrypt);
		
		//Boton que guarda la imagen cifrada
		JButton btnSaveFile = new JButton("Save File");
		btnSaveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//aca lo que pasa cuando clickeo Guardar
				if(imgCifrada==null) {
					JOptionPane.showMessageDialog(null, "Debe encriptar la imagen para poder guardarla", "Imposible guardar", JOptionPane.ERROR_MESSAGE);
				    return;	
				}
				JFileChooser fc = new JFileChooser();
		        int result = fc.showSaveDialog(null);
		        File file = null;
		        if (result == JFileChooser.APPROVE_OPTION) {
		            file = new File(fc.getSelectedFile().getAbsolutePath() + ".bmp ");
		        }
				try {
					ImageIO.write(imgCifrada, "bmp", file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnSaveFile.setBounds(309, 203, 89, 23);
		contentPane.add(btnSaveFile);
		
		JButton btnPreparaKeystream = new JButton("Prepare");
		btnPreparaKeystream.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if( key.getDocument().getLength() != 10 ){
				    JOptionPane.showMessageDialog(null, "La clave debe ser de 10 caracteres", "Clave Inválida", JOptionPane.ERROR_MESSAGE);
				    return;
				} 
				if( iv.getDocument().getLength() != 8 ){
				    JOptionPane.showMessageDialog(null, "La semilla debe ser de 8 caracteres", "Semilla Inválida", JOptionPane.ERROR_MESSAGE);
				    return;
				}
				try {
					cypher = new Cypher(StringHandler.StringToByteArray(key.getText()),StringHandler.StringToByteArray(iv.getText()));
					imageEncriptada.setIcon(null);
		            imageDesencriptada.setIcon(null);
		            bytes=0;
		            label.setText("Bytes procesados: "+bytes);
		            label_1.setText("Inicializado");
				} catch (MuchosOPocosBytesException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnPreparaKeystream.setBounds(176, 138, 86, 23);
		contentPane.add(btnPreparaKeystream);
	}
}

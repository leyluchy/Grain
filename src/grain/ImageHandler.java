package grain;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ImageHandler {
	
	public static byte[] imageToByteArray(BufferedImage originalImage) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write( originalImage, "bmp", baos );
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();
		return imageInByte;
	}
	
	public static BufferedImage byteArrayToImage(byte[] originalArray) throws IOException{
		InputStream bis = new ByteArrayInputStream(originalArray);
	    BufferedImage image = ImageIO.read(bis);
	    bis.close();
	    return image;
	}
}

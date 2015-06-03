package opengl.main;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

public class Textures {
	
	private static HashMap<String, Integer> textures = new HashMap<String, Integer>();
	
	public static void loadTexture(String path) {
		if (textures.containsKey(path)) {
			glBindTexture(GL_TEXTURE_2D, textures.get(path));
		}
		else {
			int handle = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, handle);
			
			textures.put(path, handle);
		
			InputStream in;
			BufferedImage image;
			try {
				in = new FileInputStream(path);
				image = ImageIO.read(in);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		
			AffineTransform transform = AffineTransform.getScaleInstance(1f, -1f);
			transform.translate(0, -image.getHeight());
			AffineTransformOp operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			image = operation.filter(image, null);
		
			int width = image.getWidth();
			int height = image.getHeight();

			int[] pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		
			ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					/* Pixel as RGBA: 0xAARRGGBB */
					int pixel = pixels[y * width + x];

					/* Red component 0xAARRGGBB >> (4 * 4) = 0x0000AARR */
					buffer.put((byte) ((pixel >> 16) & 0xFF));

					/* Green component 0xAARRGGBB >> (2 * 4) = 0x00AARRGG */
					buffer.put((byte) ((pixel >> 8) & 0xFF));

					/* Blue component 0xAARRGGBB >> 0 = 0xAARRGGBB */
					buffer.put((byte) (pixel & 0xFF));

					/* Alpha component 0xAARRGGBB >> (6 * 4) = 0x000000AA */
					buffer.put((byte) ((pixel >> 24) & 0xFF));
				}
			}
			buffer.flip();
		
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			glGenerateMipmap(GL_TEXTURE_2D);
		}
	}	
}

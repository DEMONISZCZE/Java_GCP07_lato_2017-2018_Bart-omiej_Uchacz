package Tests;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

class TestGUI extends JFrame {
	
	BufferedImage image;
	
	int x=10;
	int y=10;
	TestGUI() throws IOException{
		
		image=ImageIO.read(new URL("http://spplthumb.blob.core.windows.net/540x405/6e/6b/45/portret-dowolny-obraz-ze-zdjecia-ma-zamowienie-gliwice-126454167.jpg"));
		
	}

	public void update() {
		x+=10;
		y+=10;
		this.repaint();
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(Color.PINK);
		g.drawImage(image, x, y, null);
		g.drawLine(0, 0, 100, 100);
		
	}
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		TestGUI x=new TestGUI();
		
		x.setSize(600, 600);
		x.setVisible(true);
		x.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Thread t = new Thread(() ->	{
			for(;;)	{
				x.update();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		t.start();
		
	}

}

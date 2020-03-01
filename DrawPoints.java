/* Tim Yi
 * AP Computer Science
 * 01/31/2018
 * Project Draw Points - Main Panel
 */

package apcsjava;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
//import javax.swing.JSlider;
//import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.Timer;
//import javax.swing.event.DocumentEvent;
//import javax.swing.event.DocumentListener;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

@SuppressWarnings("serial")
public class DrawPoints extends JPanel implements ActionListener {

	private Image pic;
	private JTextField polygonRadiusTxt;
	private JTextField numOfVertexTxt;
	private JTextField skipGenTxt;
	private JTextField dotRadiusTxt;
	private JTextField distanceTxt;
	private JButton startBtn;
	private JButton stopBtn;
	private JButton resetBtn;
	private JButton imageBtn;
//	private JSlider polygonRadiusSlider = new JSlider(1, 10, 8);
	private double polygonRadius = 8;
	private double distance = 0.5;
	private static int numOfVertex = 3;
	private int skipGen = 10;
	private int dotRadius = 0;
	private JLabel picLabel;
	private int[][] cells;
	private ArrayList<Point> pointsToDraw = new ArrayList<Point>();
	private ArrayList<Point2D.Double> vertexCoordinates = new ArrayList<Point2D.Double>();
	private Point2D.Double newDot;
	private Timer timer;
	private boolean isRunning;

	private final double SIZE = 10; // how much of the coordinate plane from the origin to begin with

	public DrawPoints(int xSize, int ySize) {
		super(new GridBagLayout()); // set up graphics window
		setBackground(Color.LIGHT_GRAY);
//		addMouseListener(new MAdapter());
//		addMouseMotionListener(new MAdapter());
		setFocusable(true);
		setDoubleBuffered(true);
		initBtns();
		initTxt();
		initLabels();
		pic = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_RGB);
		cells = new int[xSize][ySize];
		initCells();
		picLabel = new JLabel(new ImageIcon(pic));
		timer = new Timer(1, this); // initialize the timer
		timer.start();
		drawNewCells(pic.getGraphics());
		isRunning = false;
		addThingsToPanel();
	}

	// more of that annoying placement code
	public void addThingsToPanel() {
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 1, 0, 1);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 6;
		c.gridheight = 14;
		add(picLabel, c);
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(0, 2, 0, 2);
		c.gridx = 0;
		c.gridy = 0;
		add(startBtn, c);
		c.gridx = 1;
		c.gridy = 0;
		add(stopBtn, c);
		c.gridx = 2;
		add(resetBtn, c);
		c.gridx = 3;
		// add(speedBtn, c);
		c.insets = new Insets(0, 10, 0, 10);
		c.gridx = 4;
		c.gridy = 0;
		c.fill = GridBagConstraints.VERTICAL;
		// add(generations, c);
		c.gridx = 6;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(imageBtn, c);
		c.gridy = 2;
		c.fill = GridBagConstraints.BOTH;
		add(new JLabel(" "), c);
		c.gridy = 3;
		add(new JLabel("Polygon Radius: "), c);
		c.gridy = 4;
		add(new JLabel("Number of Vertexes: "), c);
		c.gridy = 5;
		add(new JLabel("Skip Generation: "), c);
		c.gridy = 6;
		add(new JLabel("Dot Additional Radius: "), c);
		c.gridy = 7;
		add(new JLabel("Distance Factor: "), c);
		c.gridy = 8;
		add(new JLabel(" "), c);
		c.gridy = 9;
		add(new JLabel(" "), c);
		c.gridy = 10;
		add(new JLabel(" "), c);
		c.gridy = 11;
		add(new JLabel(" "), c);
		c.gridx = 7;
		c.gridy = 3;
		add(polygonRadiusTxt, c);
//		add(polygonRadiusSlider, c);
		c.gridy = 4;
		add(numOfVertexTxt, c);
		c.gridy = 5;
		add(skipGenTxt, c);
		c.gridy = 6;
		add(dotRadiusTxt, c);
		c.gridy = 7;
		add(distanceTxt, c);
	}

	public void initTxt() {
		polygonRadiusTxt = new JTextField("8", 4);
		polygonRadiusTxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					polygonRadius = Double.parseDouble(polygonRadiusTxt.getText());
					if (polygonRadius <= 10) {
						isRunning = false;
						for (int i = 0; i < cells.length; i++) {
							for (int j = 0; j < cells[i].length; j++) {
								cells[i][j] = 0;
							}
						}
						initCells();
						isRunning = true;
					}
				} catch (NumberFormatException e) {
				}
			}
		});
		numOfVertexTxt = new JTextField("3", 4);
		numOfVertexTxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					numOfVertex = Integer.parseInt(numOfVertexTxt.getText());
					if (numOfVertex > 0) {
						isRunning = false;
						for (int i = 0; i < cells.length; i++) {
							for (int j = 0; j < cells[i].length; j++) {
								cells[i][j] = 0;
							}
						}
						initCells();
						isRunning = true;
					}
				} catch (NumberFormatException e) {
				}
			}
		});
		skipGenTxt = new JTextField("10", 4);
		skipGenTxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					skipGen = Integer.parseInt(skipGenTxt.getText());
				} catch (NumberFormatException e) {
				}
			}
		});
		dotRadiusTxt = new JTextField("0", 4);
		dotRadiusTxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					dotRadius = Integer.parseInt(dotRadiusTxt.getText());
					isRunning = false;
					for (int i = 0; i < cells.length; i++) {
						for (int j = 0; j < cells[i].length; j++) {
							cells[i][j] = 0;
						}
					}
					initCells();
					isRunning = true;
				} catch (NumberFormatException e) {
				}
			}
		});
		distanceTxt = new JTextField("0.5", 4);
		distanceTxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					distance = Double.parseDouble(distanceTxt.getText());
//					isRunning = false;
//					for (int i = 0; i < cells.length; i++) {
//						for (int j = 0; j < cells[i].length; j++) {
//							cells[i][j] = 0;
//						}
//					}
//					initCells();
//					isRunning = true;
				} catch (NumberFormatException e) {
				}
			}
		});
	}

	public void initBtns() {
		startBtn = new JButton("Start");
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isRunning = true;
			}
		});
		stopBtn = new JButton("Stop");
		stopBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isRunning = false;
			}
		});
		resetBtn = new JButton("Reset");
		resetBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isRunning = false;
				for (int i = 0; i < cells.length; i++) {
					for (int j = 0; j < cells[i].length; j++) {
						cells[i][j] = 0;
					}
				}
				drawAllCells(pic.getGraphics());
			}
		});
		imageBtn = new JButton("Save Picture");
		imageBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Calendar c = Calendar.getInstance();
					String fileName = ".\\vertexNumber=" + numOfVertex + "_polygonRadius=" + polygonRadius + "@" + c.get(Calendar.HOUR) + "."
							+ c.get(Calendar.MINUTE) + "." + c.get(Calendar.SECOND) + ".png";
					System.out.println(fileName);
					File outputFile = new File(fileName);
					outputFile.createNewFile();
					ImageIO.write((RenderedImage) pic, "png", outputFile);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	private void initLabels() {
	}

	public static Color makeColor(int input) {
		if (input == 0) {
			return Color.WHITE;
		}
		if (numOfVertex <= 3) {
			if (input == 1) {
				return Color.RED;
			} else if (input == 2) {
				return Color.GREEN;
			} else if (input == 3) {
				return Color.BLUE;
			}
		} else {
			input = input%7;
			if (input == 1) {
				return Color.RED;
			} else if (input == 2) {
				return Color.ORANGE;
			} else if (input == 3) {
				return Color.YELLOW;
			} else if (input == 4) {
				return Color.GREEN;
			} else if (input == 5) {
				return Color.BLUE;
			} else if (input == 6) {
				return Color.MAGENTA;
			} else if (input == 0) {
				return Color.PINK;
			}
		}
		return Color.BLACK;

		//alternative way of doing colors (doesn't work yet)
//		int r = 255, g = 255, b = 255;
//		if (input == 0) {
//		return Color.WHITE;
//		} else {
//			r = (Math.round(input/3) + input%3 == 1 ? 1 : 0) * 51;
//			g = (Math.round(input/3) + input%3 == 2 ? 1 : 0) * 51;
//			b = (Math.round(input/3) + input%3 == 0 ? 1 : 0) * 51;
////			System.out.println(r + " " + g + " " + b);
//		}
//		return new Color(r, g, b);
	}

	public void paintComponent(Graphics g) { // draw graphics in the panel
		super.paintComponent(g); // call superclass to make panel display correctly
	}

	@Override
	public void actionPerformed(ActionEvent e) { // things to change every timer tick
		for (int i = 0; i <= skipGen; i++) {
			updateCells();
		}
		drawNewCells(pic.getGraphics());
		repaint();
	}

	public void initCells() {
		vertexCoordinates.clear();
		for (int i = 1; i <= numOfVertex; i++) {
			vertexCoordinates.add(new Point2D.Double(
					Math.round(1000000 * (polygonRadius * Math.sin(Math.PI / numOfVertex + ((Math.PI * 2) / numOfVertex) * (i - 1))))/ 1000000.0,
					Math.round(1000000 * (-1 * polygonRadius * Math.cos(Math.PI / numOfVertex + ((Math.PI * 2) / numOfVertex) * (i - 1))))/ 1000000.0
					));
		}
		newDot = vertexCoordinates.get(0);
		
		for (int i = 0; i < vertexCoordinates.size(); i++) {
			transferToCell(vertexCoordinates.get(i), i + 1);
		}
		drawAllCells(pic.getGraphics());
	}
	
	public void transferToCell(Point2D.Double coords, int color){
		Point2D.Double dotCenter = new Point2D.Double((coords.getX() + SIZE)/(2 * SIZE) * cells.length, (SIZE - coords.getY())/(2 * SIZE) * cells[0].length);
		for (int i = (int)(dotCenter.getX()) - dotRadius; i <= (int)(dotCenter.getX()) + dotRadius; i++) {
			for (int j = (int)(dotCenter.getY()) - dotRadius; j <= (int)(dotCenter.getY()) + dotRadius; j++) {
				if (Math.pow(i - (int)(dotCenter.getX()), 2) + Math.pow(j - (int)(dotCenter.getY()), 2) <= Math.pow(dotRadius, 2)) {
					cells[i][j] = color;
					pointsToDraw.add(new Point(i, j));
					
					//alternative method
//					cells[(int)(dotCenter.getX())][(int)(dotCenter.getY())] = color;
//					pointsToDraw.add(new Point((int)(dotCenter.getX()), (int)(dotCenter.getY())));
				}
			}
		}
	}
	
	public Point2D.Double findNew(Point2D.Double loc1, Point2D.Double loc2, double distanceFactor){
		return new Point2D.Double(loc1.getX() + (loc2.getX() - loc1.getX())*distanceFactor, loc1.getY() + (loc2.getY() - loc1.getY())*distanceFactor);
	}

	// use setColor and fillRect (or drawRect) to adjust the corresponding graphics
	// to cells in the pic variable
	private void drawAllCells(Graphics g) {
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				g.setColor(makeColor(cells[i][j])); // feel free to create your own color function
				g.drawRect(i, j, 1, 1);
			}
		}
	}

	private void drawNewCells(Graphics g) {
		for (Point draw:pointsToDraw) {
			g.setColor(makeColor(cells[(int) draw.getX()][(int) draw.getY()]));
			g.drawRect((int) draw.getX(), (int) draw.getY(), 1, 1);
		}
		pointsToDraw.clear();
	}
	
	private void updateCells() {
		if (isRunning) {
			int randomVertex = (int)(Math.random() * numOfVertex);
			newDot = findNew(newDot, vertexCoordinates.get(randomVertex), distance);
			transferToCell(newDot, randomVertex + 1);
		}
	}

	// mouse input
//	private class MAdapter extends MouseAdapter {

		// @Override
		// public void mousePressed(MouseEvent e) {
		// // things for when the left mouse button is pressed
		// }

		// @Override
		// public void mouseMoved(MouseEvent e) {
		// Point p = new Point(e.getX() - hOffset, e.getY() - vOffset);
		// }

		// @Override
		// public void mouseDragged(MouseEvent e) {
		// //things for when the mouse is dragged (pressed and held down while moving)
		// }

		// @Override
		// public void mouseClicked(MouseEvent e) {
		// // a click is a press and then a release
		// }

		// @Override
		// public void mouseReleased(MouseEvent e) {
		// // things for when the mouse button is released
		// }
//	}
}
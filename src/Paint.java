import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Paint {

    private MyFrame frame;
    private float brushWidth, sprayWidth;
    float[] dashl = {5, 100};
    private int mode, xPad, yPad, modeBrush, xf, yf, xPad2, yPad2, textSize;
    int xStart, yStart, xEnd, yEnd;
    private int width = 1000, height = 600;
    private MyPanel panel;
    private BufferedImage imag, picture;
    private JToolBar tools, colorTools;
    private Color curColor, bgColor;
    private Color frameColor = new Color(204, 188, 222, 222);
    private boolean loading = false, pressed = false;
    private JButton colors;
    private JFileChooser fileChooser;
    private String fileName;
    private Graphics2D d2;
    private JSlider widthSlider;

    // float[] dash2 = {5, 10, 10, 10};


    public Paint() {

        frame = new MyFrame("Paint");
        frame.setSize(width, height);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        curColor = Color.black;

        frame.add(toolMenu());
        frame.add(toolColor());


        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        menuBar.setBounds(0, 0, width, 45);
        menuBar.setBackground(frameColor);
        menuBar.add(fileMenu());
        menuBar.add(editMenu());
        menuBar.add(exitMenu());

        Font font = new Font("Verdana", Font.BOLD, frame.getWidth() / 30);


        panel = new MyPanel();
        panel.setBounds(55, 55, 100, 100);
        panel.setOpaque(true);
        frame.add(panel);

        //work when user moves the mouse
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {

                if (pressed == true) {

                    Graphics g = imag.getGraphics();
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(curColor);

                    switch (mode) {
                        //pencil
                        case 0:
                            g2.drawLine(xPad, yPad, e.getX(), e.getY());
                            break;
                        // brush modes
                        case 1:
                            switch (modeBrush) {
                                // round brush
                                case 0:
                                    brushWidth = (float) widthSlider.getValue();
                                    //BasicStroke pen1 = new BasicStroke(10, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10, dash2, 0);
                                    BasicStroke pen1 = new BasicStroke(brushWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                                    g2.setStroke(pen1);
                                    g2.drawLine(xPad, yPad, e.getX(), e.getY());
                                    break;
                                //square brush
                                case 1:
                                    brushWidth = (float) widthSlider.getValue();
                                    BasicStroke pen2 = new BasicStroke(brushWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL);
                                    g2.setStroke(pen2);
                                    g2.drawLine(xPad, yPad, e.getX(), e.getY());
                                    break;
                                // sausage brush
                                case 2:
                                    brushWidth = (float) widthSlider.getValue();
                                    BasicStroke pen3 = new BasicStroke(brushWidth, BasicStroke.CAP_ROUND,
                                            BasicStroke.JOIN_BEVEL, 10, dashl, 0);
                                    g2.setStroke(pen3);
                                    g2.drawLine(xPad, yPad, e.getX(), e.getY());
                                    break;
                            }
                            break;
                        //erase
                        case 2:
                            brushWidth = (float) widthSlider.getValue();
                            g2.setColor(Color.white);
                            BasicStroke pen1 = new BasicStroke(brushWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                            g2.setStroke(pen1);
                            g2.drawLine(xPad, yPad, e.getX(), e.getY());
                            break;
                        //spray with color
                        case 3:
                            g2.drawOval(xPad + 1, yPad + 4, 1, 1);
                            g2.drawOval(xPad - 1, yPad - 5, 1, 1);
                            g2.drawOval(xPad + 4, yPad - 6, 1, 1);
                            g2.drawOval(xPad - 4, yPad + 5, 1, 1);
                            g2.drawOval(xPad + 7, yPad + 5, 1, 1);
                            g2.drawOval(xPad - 7, yPad + 5, 1, 1);
                            g2.drawOval(xPad + 8, yPad + 5, 1, 1);
                            g2.drawOval(xPad - 8, yPad + 1, 1, 1);
                            g2.drawOval(xPad + 1, yPad - 8, 1, 1);
                            g2.drawOval(xPad - 4, yPad - 8, 1, 1);
                            g2.drawOval(xPad + 5, yPad - 8, 1, 1);
                            g2.drawOval(xPad - 1, yPad - 7, 1, 1);
                            g2.drawOval(xPad , yPad , 1, 1);
                            g2.drawOval(xPad +2, yPad - 7, 1, 1);
                            g2.drawOval(xPad - 3, yPad + 7, 1, 1);
                            g2.drawOval(xPad +3, yPad + 7, 1, 1);
                            g2.drawOval(xPad - 2, yPad - 7, 1, 1);
                            break;
                    }
                    xPad = e.getX();
                    yPad = e.getY();
                }
                panel.repaint();
            }
        });

        // works when user just clicks the mouse
        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                Graphics g = imag.getGraphics();
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(curColor);

                switch (mode) {
                    //pencil
                    case 0:
                        g2.setStroke(new BasicStroke(BasicStroke.CAP_ROUND));
                        g2.drawLine(xPad, yPad, xPad + 1, yPad + 1);
                        break;
                    // brush modes
                    case 1:
                        switch (modeBrush) {
                            //round brush
                            case 0:
                                brushWidth = (float) widthSlider.getValue();
                                BasicStroke pen1 = new BasicStroke(brushWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                                g2.setStroke(pen1);
                                g2.drawLine(xPad, yPad, xPad + 1, yPad + 1);
                                break;
                            //square brush
                            case 1:
                                brushWidth = (float) widthSlider.getValue();
                                BasicStroke pen2 = new BasicStroke(brushWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL);
                                g2.setStroke(pen2);
                                g2.drawLine(xPad, yPad, xPad + 1, yPad + 1);
                                break;
                            //sausage brush
                            case 2:
                                brushWidth = (float) widthSlider.getValue();
                                BasicStroke pen3 = new BasicStroke(brushWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 10, dashl, 0);
                                g2.setStroke(pen3);
                                g2.drawLine(xPad, yPad, xPad + 1, yPad + 1);
                                break;
                        }
                        break;
                    //erase
                    case 2:
                        brushWidth = (float) widthSlider.getValue();
                        g2.setColor(bgColor);
                        g2.setStroke(new BasicStroke(brushWidth));
                        g2.drawLine(xPad, yPad, xPad + 1, yPad + 1);
                        break;
                    //spray with color
                    case 3:

                        g2.drawOval(xPad + 1, yPad + 4, 1, 1);
                        g2.drawOval(xPad - 1, yPad - 5, 1, 1);
                        g2.drawOval(xPad + 4, yPad - 6, 1, 1);
                        g2.drawOval(xPad - 4, yPad + 5, 1, 1);
                        g2.drawOval(xPad + 7, yPad + 5, 1, 1);
                        g2.drawOval(xPad - 7, yPad + 5, 1, 1);
                        g2.drawOval(xPad + 8, yPad + 5, 1, 1);
                        g2.drawOval(xPad - 8, yPad + 1, 1, 1);
                        g2.drawOval(xPad + 1, yPad - 8, 1, 1);
                        g2.drawOval(xPad - 4, yPad - 8, 1, 1);
                        g2.drawOval(xPad + 5, yPad - 8, 1, 1);
                        g2.drawOval(xPad - 1, yPad - 7, 1, 1);
                        g2.drawOval(xPad , yPad , 1, 1);
                        g2.drawOval(xPad +2, yPad - 7, 1, 1);
                        g2.drawOval(xPad - 3, yPad + 7, 1, 1);
                        g2.drawOval(xPad +3, yPad + 7, 1, 1);
                        g2.drawOval(xPad - 2, yPad - 7, 1, 1);
                        break;

                    case 4:
                        panel.requestFocus();
                        break;
                    case 10:
                        panel.requestFocus();
                        break;
                }
                xPad = e.getX();
                yPad = e.getY();
                pressed = true;
                panel.repaint();
            }

            public void mousePressed(MouseEvent e) {
                xPad = e.getX();
                yPad = e.getY();
                xf = e.getX();
                yf = e.getY();
                pressed = true;
            }

            // for geom figures
            public void mouseReleased(MouseEvent e) {
                Graphics g = imag.getGraphics();
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(curColor);
                int x1 = xf, x2 = xPad, y1 = yf, y2 = yPad;
                if (xf < xPad && yf < yPad) {
                    xStart = xf;
                    yStart = yf;
                    xEnd = xPad;
                    yEnd = yPad;
                } else if (xf < xPad && yf > yPad) {
                    xStart = xf;
                    yStart = yPad;
                    xEnd = xPad;
                    yEnd = yf;
                } else if (xf > xPad && yf < yPad) {
                    xStart = xPad;
                    yStart = yf;
                    xEnd = xf;
                    yEnd = yPad;
                } else if (xf > xPad && yf > yPad) {
                    xStart = xPad;
                    yStart = yPad;
                    xEnd = xf;
                    yEnd = yf;
                }
                switch (mode) {

                    //line
                    case 5:
                        brushWidth = (float) widthSlider.getValue();
                        g2.setStroke(new BasicStroke(brushWidth));
                        g2.drawLine(xf, yf, e.getX(), e.getY());
                        break;
                    //oval
                    case 6:
                        brushWidth = (float) widthSlider.getValue();
                        g2.setStroke(new BasicStroke(brushWidth));
                        g2.drawOval(x1, y1, (x2 - x1), (y2 - y1));
                        break;
                    //filled oval
                    case 7:
                        brushWidth = (float) widthSlider.getValue();
                        g2.setStroke(new BasicStroke(brushWidth));
                        g2.fillOval(x1, y1, (x2 - x1), (y2 - y1));
                        break;
                    //rect
                    case 8:
                        brushWidth = (float) widthSlider.getValue();
                        g2.setStroke(new BasicStroke(brushWidth));
                        g2.drawRect(x1, y1, (x2 - x1), (y2 - y1));
                        break;
                    //filled rect
                    case 9:
                        brushWidth = (float) widthSlider.getValue();
                        g2.setStroke(new BasicStroke(brushWidth));
                        g2.fillRect(x1, y1, (x2 - x1), (y2 - y1));
                        break;
                    case 10:
                        // g2.drawRect(xStart,yStart,xEnd-xStart,yEnd-yStart);
                        picture = imag.getSubimage(xStart, yStart, xEnd - xStart, yEnd - yStart);

                        break;

                }

                xf = 0;
                yf = 0;
                pressed = false;
                panel.repaint();
            }
        });
        panel.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                panel.requestFocus();
            }

            public void keyPressed(KeyEvent e) {
                if (mode == 10) {
                    if (e.getKeyCode() == KeyEvent.VK_X) {
                        Graphics2D g2 = (Graphics2D) imag.getGraphics();
                        g2.setColor(Color.white);
                        picture = imag.getSubimage(xStart, yStart, xEnd - xStart, yEnd - yStart);
                        try {
                            saveImage("D:\\KateGor\\savePicture.png", picture);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        g2.fillRect(xStart, yStart, xEnd - xStart, yEnd - yStart);
                        panel.requestFocus();
                        panel.repaint();
                    }
                    if (e.getKeyCode() == KeyEvent.VK_V) {
                        Graphics2D g2 = (Graphics2D) imag.getGraphics();
                        try {
                            picture = ImageIO.read(new File("D:\\KateGor\\savePicture.png"));
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        g2.drawImage(picture, xPad, yPad, xEnd - xStart, yEnd - yStart, null);
                        // g2.fillRect(xPad,yPad,xEnd-xStart,yEnd-yStart);
                        panel.requestFocus();
                        panel.repaint();
                    }
                }
            }

            public void keyTyped(KeyEvent e) {
                if (mode == 4) {
                    Graphics g = imag.getGraphics();
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(curColor);
                    g2.setStroke(new BasicStroke(50f));
                    String str = "";
                    str += e.getKeyChar();
                    // String textFont = (String) cmbTextFont.getSelectedItem();

                    textSize = widthSlider.getValue();
                    Font font = new Font("Arial", 0, textSize);
                    g2.setFont(font);
                    g2.drawString(str, xPad, yPad);
                    xPad += textSize - textSize / 3;
                    // focusing on the panel to add text
                    panel.requestFocus();
                    panel.repaint();
                }
            }

        });


        // if user load photo the panel resize to correct size automatically
        // if user change size of panel using mouse the panel resize automatically
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                if (loading == false) {

                    panel.setSize(frame.getWidth(), frame.getHeight());
                    BufferedImage tempImage = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
                    d2 = (Graphics2D) tempImage.createGraphics();
                    d2.setColor(bgColor);
                    d2.fillRect(0, 0, panel.getWidth(), panel.getHeight());
                    tempImage.setData(imag.getRaster());
                    imag = tempImage;
                    panel.repaint();
                }
                loading = false;
            }
        });
        frame.setLayout(null);
        frame.setVisible(true);
    }


    //JMenu with JSlider for changing brush/geom figures width
    private JMenu widthMenu() {
        JMenu widthMenu = new JMenu();
        widthMenu.setIcon(new ImageIcon("width.png"));
        BoundedRangeModel model1 = new DefaultBoundedRangeModel(10, 1, 0, 200);
        widthSlider = new JSlider(model1);
        widthSlider.setBackground(frameColor);
        widthSlider.setPaintTicks(true);
        widthSlider.setPaintLabels(true);
//        widthLine.setMinorTickSpacing(10);
        widthSlider.setMajorTickSpacing(30);

        brushWidth = (float) widthSlider.getValue();
        widthMenu.add(widthSlider);
        return widthMenu;
    }

    //JMenu that allow to choose diff brush
    private JMenu brushMenu() {
        JMenu brushMenu = new JMenu();

        brushMenu.setIcon(new ImageIcon("brush.png"));

        JMenuItem brush1 = new JMenuItem(new ImageIcon("brushRound.png"));
        brush1.setBackground(frameColor);
        brush1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = 1;
                modeBrush = 0;
            }
        });
        JMenuItem brush2 = new JMenuItem(new ImageIcon("brushRect.png"));
        brush2.setBackground(frameColor);
        brush2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = 1;
                modeBrush = 1;
            }
        });
        JMenuItem brush3 = new JMenuItem(new ImageIcon("brushPunkt.png"));
        brush3.setBackground(frameColor);
        brush3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = 1;
                modeBrush = 2;
            }
        });
        brushMenu.add(brush1);
        brushMenu.add(brush2);
        brushMenu.add(brush3);
        return brushMenu;
    }

    //JMenu that allow to create line, oval, rect
    private JMenu geomMenu() {
        JMenu geomMenu = new JMenu();
        geomMenu.setIcon(new ImageIcon("geom.png"));

        JMenuItem line = new JMenuItem(new ImageIcon("line.png"));
        line.setBackground(frameColor);
        line.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = 5;
            }
        });
        geomMenu.add(line);
        JMenuItem round = new JMenuItem(new ImageIcon("round.png"));
        round.setBackground(frameColor);
        round.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = 6;
            }
        });
        geomMenu.add(round);
        JMenuItem roundFill = new JMenuItem(new ImageIcon("roundFill.png"));
        roundFill.setBackground(frameColor);
        roundFill.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = 7;
            }
        });
        geomMenu.add(roundFill);
        JMenuItem rect = new JMenuItem(new ImageIcon("rect.png"));
        rect.setBackground(frameColor);
        rect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = 8;
            }
        });
        geomMenu.add(rect);
        JMenuItem rectFill = new JMenuItem(new ImageIcon("rectFill.png"));
        rectFill.setBackground(frameColor);
        rectFill.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = 9;
            }
        });
        geomMenu.add(rectFill);


        return geomMenu;
    }


    //JToolBar with all instruments
    private JToolBar toolMenu() {
        tools = new JToolBar("Toolbar");
        tools.setBackground(frameColor);

        tools.addSeparator();
        JMenuBar widthBar = new JMenuBar();
        widthBar.add(widthMenu());
        tools.add(widthBar);

        tools.addSeparator();

        JMenuBar geomBar = new JMenuBar();
        geomBar.add(geomMenu());
        tools.add(geomBar);

        tools.addSeparator();

        JMenuBar brushBar = new JMenuBar();
        brushBar.add(brushMenu());
        tools.add(brushBar);

        tools.addSeparator();

        JButton pencil = new JButton(new ImageIcon("pencil.png"));
        pencil.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                mode = 0;
            }
        });
        tools.add(pencil);

        tools.addSeparator();

        JButton spray = new JButton(new ImageIcon("spray.png"));
        spray.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = 3;
            }
        });
        tools.add(spray);

        tools.addSeparator();

        JButton eraser = new JButton(new ImageIcon("eraser.png"));
        eraser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                mode = 2;
            }
        });
        tools.add(eraser);
        tools.addSeparator();

        JButton fill = new JButton(new ImageIcon("fill.png"));
        fill.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                bgColor = JColorChooser.showDialog(frame, "Choose", Color.CYAN);
               if (bgColor!=null){
                d2.setColor(bgColor);
                d2.fillRect(0, 0, panel.getWidth(), panel.getHeight());
                panel.repaint();

            }}
        });

        tools.add(fill);

        tools.addSeparator();

        JButton text = new JButton(new ImageIcon("text.png"));
        text.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                mode = 4;
            }
        });
        tools.add(text);

//
//        JMenuBar fontBar = new JMenuBar();
//        fontBar.add(fontEdit());
//        tools.add(fontBar);

        tools.addSeparator();

//        JButton copy = new JButton(new ImageIcon("copy.png"));
//        copy.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//            }
//        });
//        tools.add(copy);
//        tools.addSeparator();

        JButton cut = new JButton(new ImageIcon("cut.png"));
        cut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = 10;
            }
        });
        tools.add(cut);
//        tools.addSeparator();
//        JButton paste = new JButton(new ImageIcon("paste.png"));
//        paste.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//            }
//        });
//        tools.add(paste);
        tools.setFloatable(false);
        tools.setBounds(55, 0, 2000, 55);
        // tools.setLayout(WEst);

        return tools;
    }

    //JToolBar with 10 basic color and opportunity to choose any color.
    private JToolBar toolColor() {
        colorTools = new JToolBar("Colors", JToolBar.VERTICAL);
        colorTools.setBounds(0, 0, 55, 2000);
        colorTools.setBackground(frameColor);

        colors = new JButton(new ImageIcon("colors.png"));

        colors.setBackground(curColor);
        colors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                curColor = JColorChooser.showDialog(frame, "Choose", Color.CYAN);
                colors.setBackground(curColor);
            }
        });
        colorTools.add(colors);

        colorTools.addSeparator();

        JButton whiteColor = new JButton(new ImageIcon("allcolors.png"));

        whiteColor.setBackground(Color.white);
        whiteColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                curColor = Color.white;
                colors.setBackground(Color.white);

            }
        });
        colorTools.add(whiteColor);

        colorTools.addSeparator();

        JButton yellowColor = new JButton(new ImageIcon("allcolors.png"));

        yellowColor.setBackground(Color.yellow);
        yellowColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                curColor = Color.yellow;
                colors.setBackground(Color.yellow);

            }
        });
        colorTools.add(yellowColor);

        colorTools.addSeparator();

        JButton orange = new JButton(new ImageIcon("allcolors.png"));

        orange.setBackground(Color.orange);
        orange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                curColor = Color.orange;
                colors.setBackground(Color.orange);

            }
        });
        colorTools.add(orange);

        colorTools.addSeparator();

        JButton pinkColor = new JButton(new ImageIcon("allcolors.png"));

        pinkColor.setBackground(Color.pink);
        pinkColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                curColor = Color.pink;
                colors.setBackground(Color.pink);

            }
        });
        colorTools.add(pinkColor);

        colorTools.addSeparator();


        JButton magColor = new JButton(new ImageIcon("allcolors.png"));

        magColor.setBackground(Color.MAGENTA);
        magColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                curColor = Color.magenta;
                colors.setBackground(Color.magenta);

            }
        });
        colorTools.add(magColor);


        colorTools.addSeparator();
        JButton redColor = new JButton(new ImageIcon("allcolors.png"));

        redColor.setBackground(Color.red);
        redColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                curColor = Color.red;
                colors.setBackground(Color.red);

            }
        });
        colorTools.add(redColor);

        colorTools.addSeparator();

        JButton greenColor = new JButton(new ImageIcon("allcolors.png"));

        greenColor.setBackground(Color.green);
        greenColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                curColor = Color.green;
                colors.setBackground(Color.green);

            }
        });
        colorTools.add(greenColor);
        colorTools.addSeparator();


        JButton cyanColor = new JButton(new ImageIcon("allcolors.png"));

        cyanColor.setBackground(Color.cyan);
        cyanColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                curColor = Color.cyan;
                colors.setBackground(Color.cyan);

            }
        });
        colorTools.add(cyanColor);

        colorTools.addSeparator();

        JButton blueColor = new JButton(new ImageIcon("allcolors.png"));

        blueColor.setBackground(Color.blue);
        blueColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                curColor = Color.blue;
                colors.setBackground(Color.blue);

            }
        });
        colorTools.add(blueColor);

        colorTools.addSeparator();
        JButton blackColor = new JButton(new ImageIcon("allcolors.png"));

        blackColor.setBackground(Color.black);
        blackColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                curColor = Color.black;
                colors.setBackground(Color.black);

            }
        });
        colorTools.add(blackColor);

        colorTools.setFloatable(false);
//        colorTools.setBounds(4, 10, 55, height);
        return colorTools;
    }

    private JMenu editMenu() {
        JMenu edit = new JMenu("Edit");

        JMenuItem create = new JMenuItem("Clean", new ImageIcon("create.png"));
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                d2.setColor(Color.white);
                d2.fillRect(0, 0, panel.getWidth(), panel.getHeight());
                panel.repaint();

            }
        });
        edit.add(create);
        edit.setFont(new Font("Arial", Font.PLAIN, 20));
        return edit;
    }

    private JMenu exitMenu() {
        JMenu exit = new JMenu("Exit");

        JMenuItem exitB = new JMenuItem("Exit", new ImageIcon("exit3.png"));
        exitB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        exit.add(exitB);
        exit.setFont(new Font("Arial", Font.PLAIN, 20));


        return exit;
    }


    private JMenu fileMenu() {
        JMenu file = new JMenu("File");


        JMenuItem open = new JMenuItem("Open", new ImageIcon("open.png"));
        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JFileChooser jf = new JFileChooser();
                int result = jf.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        // при выборе изображения подстраиваем размеры формы
                        // и панели под размеры данного изображения
                        fileName = jf.getSelectedFile().getAbsolutePath();
                        File iF = new File(fileName);
                        jf.addChoosableFileFilter(new TextFileFilter(".png"));
                        jf.addChoosableFileFilter(new TextFileFilter(".jpg"));
                        imag = ImageIO.read(iF);
                        loading = true;
                        frame.setSize(imag.getWidth() + 40, imag.getWidth() + 80);
                        panel.setSize(imag.getWidth(), imag.getWidth());
                        panel.repaint();
                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(frame, "Такого файла не существует");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Исключение ввода-вывода");
                    } catch (Exception ex) {
                    }
                }
            }
        });


        JMenuItem save = new JMenuItem("Save", new ImageIcon("save.png"));
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    JFileChooser jf = new JFileChooser();
                    // Создаем фильтры  файлов
                    TextFileFilter pngFilter = new TextFileFilter(".png");
                    TextFileFilter jpgFilter = new TextFileFilter(".jpg");
                    if (fileName == null) {
                        // Добавляем фильтры
                        jf.addChoosableFileFilter(pngFilter);
                        jf.addChoosableFileFilter(jpgFilter);
                        int result = jf.showSaveDialog(null);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            fileName = jf.getSelectedFile().getAbsolutePath();
                        }
                    }
                    // Смотрим какой фильтр выбран
                    if (jf.getFileFilter() == pngFilter) {
                        ImageIO.write(imag, "png", new File(fileName + ".png"));
                    } else {
                        ImageIO.write(imag, "jpeg", new File(fileName + ".jpg"));
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Ошибка ввода-вывода");
                }
            }
        });


        JMenuItem saveAs = new JMenuItem("Save as", new ImageIcon("saveAs.png"));
        saveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser jf = new JFileChooser();
                    // Создаем фильтры для файлов
                    TextFileFilter pngFilter = new TextFileFilter(".png");
                    TextFileFilter jpgFilter = new TextFileFilter(".jpg");
                    // Добавляем фильтры
                    jf.addChoosableFileFilter(pngFilter);
                    jf.addChoosableFileFilter(jpgFilter);
                    int result = jf.showSaveDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        fileName = jf.getSelectedFile().getAbsolutePath();
                    }
                    // Смотрим какой фильтр выбран
                    if (jf.getFileFilter() == pngFilter) {
                        ImageIO.write(imag, "png", new File(fileName + ".png"));
                    } else {
                        ImageIO.write(imag, "jpeg", new File(fileName + ".jpg"));
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Ошибка ввода-вывода");
                }
            }

        });


        file.setFont(new Font("Arial", Font.PLAIN, 20));

        file.add(open);
        file.add(save);
        file.add(saveAs);

        return file;
    }


    class MyFrame extends JFrame {
        public void paint(Graphics g) {
            super.paint(g);
        }

        public MyFrame(String title) {
            super(title);
        }
    }

    class MyPanel extends JPanel {
        public MyPanel() {
        }

        public void paintComponent(Graphics g) {
            if (imag == null) {
                imag = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D d2 = (Graphics2D) imag.createGraphics();
                d2.setColor(bgColor);
                d2.fillRect(0, 0, this.getWidth(), this.getHeight());
            }
            super.paintComponent(g);
            g.drawImage(imag, 0, 0, this);
        }
    }

    private void saveImage(String filePath, BufferedImage image) throws IOException {
        ImageIO.write(image, "png", new File(filePath + ".png"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Paint();
            }
        });
    }

    class TextFileFilter extends FileFilter {
        private String ext;

        public TextFileFilter(String ext) {
            this.ext = ext;
        }

        public boolean accept(java.io.File file) {
            if (file.isDirectory()) return true;
            return (file.getName().endsWith(ext));
        }

        public String getDescription() {
            return "*" + ext;
        }
    }
}


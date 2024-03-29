package morpheus.view.state;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import morpheus.controller.AudioPlayer;
import morpheus.model.Element;
import morpheus.model.Model;
import morpheus.model.ModelImpl;
import morpheus.model.exceptions.IllegalNameException;
import morpheus.model.exceptions.NoElementsException;

/**
 *
 * @author Luca Mengozzi
 * 
 */
public class DeathState implements State {

    private JFrame mainFrame = new JFrame();
    private JLabel labelScore;

    private JFrame recordFrame = new JFrame();
    private JPanel panelRecord = new JPanel();
    private JTextField name = new JTextField();
    private JButton ok = new JButton("Ok");

    JDialog dialog = new JDialog();
    JPanel panelDialog = new JPanel();
    JButton yes = new JButton("Yes");
    JButton no = new JButton("No");

    private Model model = new ModelImpl();
    
    private final URL url = DeathState.class.getResource("/morpheus.png");
    private final ImageIcon img = new ImageIcon(url);

    /**
     *
	 * If is true, it exit from the state		
	 * 
	 * @author Luca Mengozzi
	 * 		 
	 */
    private boolean exit;

    @Override
    public void init() {

        labelScore = new JLabel("SCORE: " + Integer.toString(GameState.score));
        labelScore.setHorizontalAlignment(JLabel.CENTER);
        labelScore.setFont(new Font("TimesNewRoman", Font.BOLD, 30));
        BackgroundDeathState background = new BackgroundDeathState();
        background.setLayout(new BorderLayout());
        background.add(labelScore, BorderLayout.SOUTH);
        mainFrame.getContentPane().add(background);
        mainFrame.setSize(500, 300);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setIconImage(img.getImage());

        recordFrame.getContentPane().add(panelRecord);
        panelRecord.add(new JLabel("Wow! You have scored a new RECORD!"));
        panelRecord.add(new JLabel("Champion, tell us what is your name"));
        name.setColumns(23);
        panelRecord.add(name);
        panelRecord.add(ok);
        recordFrame.setIconImage(img.getImage());

        panelDialog.add(new JLabel("This name is used yet. Are you sure do you want continue?"));
        panelDialog.add(new JLabel("\"Yes\" will delete old result with the same name"));
        panelDialog.add(yes);
        panelDialog.add(no);
        dialog.getContentPane().add(panelDialog);
        dialog.setSize(700, 75);
        dialog.setLocationRelativeTo(null);
        dialog.setIconImage(img.getImage());
        
        ok.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    model.getRanking().add(new Element(name.getText(), GameState.score));
                    try {

                        model.getRanking().close();
                    } catch (IOException e2) {

                        e2.printStackTrace();
                    }
                    dialog.setVisible(false);
                    recordFrame.setVisible(false);
                } catch (IllegalNameException e1) {

                    yes.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {

                            model.getRanking().forceAdd(new Element(name.getText(), GameState.score));
                            try {

                                model.getRanking().close();
                            } catch (IOException e2) {

                                e2.printStackTrace();
                            }
                            dialog.setVisible(false);
                            recordFrame.setVisible(false);
                        }
                    });
                    no.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {

                            dialog.setVisible(false);
                        }
                    });

                    dialog.setVisible(true);
                } catch (IllegalArgumentException e1) {

                    e1.printStackTrace();
                }
            }
        });

        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {

                exit = true;
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }
        });
    }

    @Override
    public void enter(StateManager stateManager) {

        exit = false;
        labelScore.setText("SCORE: " + Integer.toString(GameState.score));

        mainFrame.setVisible(true);

        try {

            if (GameState.score > model.getRanking().getPosition(5).getScore()) {

                recordFrame.setSize(300, 121);
                recordFrame.setLocationRelativeTo(null);
                recordFrame.setVisible(true);
            }
        } catch (NoElementsException e1) {

            recordFrame.setSize(300, 121);
            recordFrame.setLocationRelativeTo(null);
            recordFrame.setVisible(true);
        }
    }

    @Override
    public void tick(StateManager stateManager) {

        if (exit == true) {

            stateManager.setState("MENU");
        }
        stateManager.getState("GAME").getMusic().setVolume(0);
    }

    @Override
    public void render(Graphics2D g) {

    }

    @Override
    public void exit() {

        mainFrame.dispose();
        recordFrame.dispose();
        dialog.dispose();
    }

    @Override
    public String getName() {

        return "Death";
    }

    @Override
    public AudioPlayer getMusic() {

        return null;
    }
}

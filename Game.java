import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Code creates wack-a-mole game using GUI.
 * @author kirabonsereko
 * Andrew ID: knsereko
 */
public class Game {
    /**
     * Up string constant.
     */
    private static final String UP_STRING = ":-)";
    /**
     * Up color constant.
     */
    private static final Color UP_COLOR = Color.ORANGE;
    /**
     * Hit string constant.
     */
    private static final String HIT_STRING = ":-(";
    /**
     * Hit color constant.
     */
    private static final Color HIT_COLOR = Color.GREEN;
    /**
     * Down string constant.
     */
    private static final String DOWN_STRING = "   ";
    /**
     * Down color constant.
     */
    private static final Color DOWN_COLOR = Color.LIGHT_GRAY;
    /**
     * Number of moles constant.
     */
    private static final int NUM_OF_MOLES = 20;
    /**
     * Instance variable for array of moleButtons.
     */
    private JButton[] moleButton;
    /**
     * Instance variable for startButton.
     */
    private JButton startButton;
    /**
     * Instance variable for timeField.
     */
    private JTextField timeField;
    /**
     * Instance variable for timeField.
     */
    private JTextField scoreField;
    /**
     * Instance variable for score.
     */
    private int score;
    /**
     * Instance variable for count.
     */
    private int count = 20;

    /*
     * Constructor.
     */
    public Game() {
        Font font = new Font(Font.MONOSPACED, Font.BOLD, 16);

        JFrame frame = new JFrame("Wack-a-mole GUI");
        //frame.setSize(650, 630);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        startButton = new JButton("Start");
        startButton.addActionListener(new StartButtonClicked());
        JLabel timeLabel = new JLabel("Time Left:");
        timeField = new JTextField(4);
        timeField.setEditable(false);
        JLabel scoreLabel = new JLabel("Score:");
        scoreField = new JTextField(4);
        scoreField.setEditable(false);
        JPanel panel1 = new JPanel();

        moleButton = new JButton[NUM_OF_MOLES];

        for (int i = 0; i < NUM_OF_MOLES; i++) {
            moleButton[i] = new JButton(DOWN_STRING);
            moleButton[i].setBackground(Color.LIGHT_GRAY);
            moleButton[i].setFont(font);
            moleButton[i].setOpaque(true);
            moleButton[i].addActionListener(new MoleClicked(i));
            panel1.add(moleButton[i]);
        }

        JPanel panel2 = new JPanel();
        panel2.add(startButton);
        panel2.add(timeLabel);
        panel2.add(timeField);
        panel2.add(scoreLabel);
        panel2.add(scoreField);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.add(panel2);
        mainPanel.add(panel1);
        frame.setContentPane(mainPanel);
        frame.pack();
        frame.setVisible(true);
        startButton.isFocusable();
        startButton.requestFocusInWindow();
    }

    /**
     * Private nested class used to perform action performed method when moleButton clicked.
     * @author kirabonsereko
     *
     */
    private class MoleClicked implements ActionListener {
        /**
         * Instance variable moleButton index.
         */
        private int k;

        /*
         * Constructor.
         * @param i
         */
        MoleClicked(int i) {
            k = i;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //System.out.println(Thread.currentThread().getName());
            JButton button = moleButton[k];
            //System.out.println(k);
             synchronized (button) {
                 if (button.getText().equals(UP_STRING) && count != 0) {
                     button.setText(HIT_STRING);
                     button.setBackground(HIT_COLOR);
                     score = score + 1;
                     scoreField.setText(String.valueOf(score));
                 }
            }
        }
    }

    /**
     * Private static nested class used to perform actionPerformed method when start button clicked.
     * @author kirabonsereko
     *
     */
    private class StartButtonClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            startButton.setEnabled(false);
            Thread timer = new Thread(new Timer());
            timer.start();
            Thread[] buttonThreads = new Thread[NUM_OF_MOLES];

            for (int i = 0; i < buttonThreads.length; i++) {
                buttonThreads[i] = new Thread(new Moles(moleButton[i], i));
                buttonThreads[i].start();
            }
        }
    }

    /**
     * Private nested class used to perform run method for each moleButton thread.
     * @author kirabonsereko
     *
     */
    private class Moles implements Runnable {
        /**
         * Instance variable button.
         */
        private JButton button;
        /**
         * Instance variable i.
         */
        private int i;

        /*
         * Constructor.
         * @param b
         * @param k
         */
        Moles(JButton b, int k) {
            button = b;
            i = k;
        }

        @Override
        public void run() {
           try {
               while (count > 0) {
                   //System.out.println(count);
                   //Thread.sleep(200);
                   long elapsedTime = 0;
                   long startTime = System.currentTimeMillis();
                   long randomNum = ThreadLocalRandom.current().nextLong(520, 3000);

                   while (elapsedTime <= randomNum) {
                       synchronized (button) {
                           if (button.getText().equals(DOWN_STRING)) {
                               button.setText(UP_STRING);
                               button.setBackground(UP_COLOR);
                               Thread.sleep(20);
                           }
                       }

                       elapsedTime = System.currentTimeMillis() - startTime;
                   }

/*                   while (elapsedTime <= 4000) {
                       //Thread.sleep(200);
                       if (button.getText().equals(DOWN_STRING)) {
                           button.setText(UP_STRING);
                           button.setBackground(UP_COLOR);
                           Thread.sleep(200);
                       }

                       elapsedTime = System.currentTimeMillis() - startTime;
                   }*/

                   button.setText(DOWN_STRING);
                   button.setBackground(DOWN_COLOR);

                   long startTime2 = System.currentTimeMillis();
                   long restTime = 0;
                   Thread.sleep(2000);
   /*                while(restTime <= 2000) {
                       restTime = System.currentTimeMillis() - startTime2;
                   }*/
               }
           } catch (InterruptedException e) {
               throw new AssertionError(e);
           }
        }
    }

    /**
     * Private nested class used to start timer thread.
     * @author kirabonsereko
     *
     */
    private class Timer implements Runnable {
        //int count;

        Timer() {
            //count = cnt;
        }

        @Override
        public void run() {
            timeField.setText(String.valueOf(count));
            long startTime = System.currentTimeMillis();
            long timeElapsed = 0;

            try {
                //while (count > 0) {
                while (timeElapsed < 20000) {
                   Thread.sleep(1000);
                   count = count - 1;
                   timeField.setText(String.valueOf(count));
                   timeElapsed = System.currentTimeMillis() - startTime;
                }

                Thread.sleep(5000);
                count = 20;
                score = 0;
                scoreField.setText(String.valueOf(score));
                startButton.setEnabled(true);

            } catch (InterruptedException e) {
                throw new AssertionError(e);
            }
        }
    }

    /*
     * Main method.
     * @param args
     */
    public static void main(String[] args) {
        new Game();
    }
}

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LOG {
    private int refer = 0;
    private int max_r = 100;
    LBOX box;

    public LOG() {
        System.out.println("Start log...");
    }

    public void nextUP() {
        this.refer += 1;
    }

    public void check() {
        if (this.refer == this.max_r) {
            System.exit(0);
        }
    }
}


class LBOX {
    JFrame mainFrame = new JFrame();
    JPanel poolFrame = new PoolFrame();

    LBOX() {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);

        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(poolFrame, BorderLayout.WEST);

        mainFrame.setTitle("BattleshipGame");
        mainFrame.setSize(1200, 700);
        mainFrame.setVisible(true);
    }


    class PoolFrame extends JPanel {
        JPanel poolPanel;
        JButton[][] buttons;

        PoolFrame() {
            poolPanel = new JPanel();
            poolPanel.setLayout(new GridLayout(10, 10));
            poolPanel.setBackground(Color.GRAY);

            buttons = new JButton[10][10];
            createButtons();

            poolPanel.setPreferredSize(new Dimension(400, 400));
            this.add(poolPanel);
        }

        private void createButtons() {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    buttons[i][j] = new JButton();
                    buttons[i][j].setPreferredSize(new Dimension(40, 40));
                    poolPanel.add(buttons[i][j]);
                }
            }
        }

        public void markButtons(HashMap<Integer, List<Integer>> cells) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    buttons[i][j].setBackground(Color.GRAY);

                    for (Map.Entry<Integer, List<Integer>> cell : cells.entrySet()) {
                        for (Integer value : cell.getValue()) {
                            this.buttons[cell.getKey()][value].setBackground(Color.BLUE);
                        }
                    }
                }
            }
        }
    }
}

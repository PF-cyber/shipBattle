import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class JBOX {
    JUDGE judge;
    MainFrame.PoolFrame ppFrame;
    MainFrame.PoolFrame bpFrame;

    JBOX(Player player, Bot bot, JUDGE judge) {
        new MainFrame(player, bot, judge);
    }
}

class MainFrame {
    JFrame mainFrame = new JFrame();
    PoolFrame ppFrame;
    PoolFrame bpFrame;

    MainFrame(Player player, Bot bot, JUDGE judge) {

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);

        this.ppFrame = new PoolFrame(player, judge);
        player.poolFrame = ppFrame;
        this.bpFrame = new PoolFrame(bot, judge);
        bot.poolFrame = bpFrame;

        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(this.ppFrame, BorderLayout.WEST);
        mainFrame.add(this.bpFrame, BorderLayout.EAST);

        bpFrame.setterButtons(bot.enemy, judge);

        mainFrame.setTitle("BattleshipGame");
        mainFrame.setSize(1200, 700);
        mainFrame.setVisible(true);
    }

    static class PoolFrame extends JPanel {
        JPanel poolPanel = new JPanel();
        private final JButton[][] buttons;
        boolean EnButtons = true;

        PoolFrame(APlayer player, JUDGE judge) {
            poolPanel.setLayout(new GridLayout(10, 10));
            poolPanel.setBackground(Color.GRAY);

            buttons = new JButton[10][10];

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    buttons[i][j] = new JButton();
                    buttons[i][j].setPreferredSize(new Dimension(40, 40));
                    buttons[i][j].setEnabled(false);
                    poolPanel.add(buttons[i][j]);
                }
            }
            this.add(poolPanel);
            setShips(player);
            visualShips(player);
        }

        private void setShips(APlayer player) {
            for (Ship ship : player.pool.ships) {
                for (Map.Entry<Integer, List<Integer>> entry : ship.location.entrySet()) {
                    int x = entry.getKey();
                    for (Integer y : entry.getValue()) {
                        buttons[x][y].setBackground(Color.BLUE);
                    }
                }
            }
        }

        public void visualShips(APlayer player) {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    if (player.pool.pool.get(x).get(y) instanceof Ship) {
                        buttons[x][y].setBackground(Color.BLUE);
                    }
                }
            }
        }

        public void setterButtons(APlayer player, JUDGE judge) {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    int finalX = x;
                    int finalY = y;
                    buttons[x][y].setEnabled(true);
                    buttons[x][y].addActionListener(e -> {
                        int result = player.shoot(finalX, finalY);
                        if (result == 2){
                            buttons[finalX][finalY].setBackground(Color.RED);
                        }
                        judge.shoot_evaluetion(result);
                        buttons[finalX][finalY].setEnabled(false);
                    });
                }
            }
        }

        public void statusButtons(boolean on){
            for(JButton[] button_mes : buttons){
                for(JButton button : button_mes){
                    button.setEnabled(on);
                }
            }
        }
    }
}
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class JBOX {
    public static void main(String[] args) {
        JUDGE Judge = new JUDGE();
        new MainFrame(Judge.player, Judge.bot);
    }
}

class MainFrame {
    JFrame mainFrame = new JFrame();

    MainFrame(Player player, Bot bot) {

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);

        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(new PoolFrame(player), BorderLayout.WEST);
        mainFrame.add(new PoolFrame(bot), BorderLayout.EAST);

        mainFrame.setTitle("BattleshipGame");
        mainFrame.setSize(1200, 700);
        mainFrame.setVisible(true);
    }

    static class PoolFrame extends JPanel {
        JPanel poolPanel = new JPanel();
        private final JButton[][] buttons;
        boolean EnButtons = true;

        PoolFrame(APlayer player) {
            poolPanel.setLayout(new GridLayout(10, 10));
            poolPanel.setBackground(Color.GRAY);

            buttons = new JButton[10][10];

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    buttons[i][j] = new JButton();
                    buttons[i][j].setPreferredSize(new Dimension(40, 40));
                    poolPanel.add(buttons[i][j]);
                }
            }
            this.add(poolPanel);
            setShips(player);
            visualShips(player);
        }

        private void setShips(APlayer player) {
            System.out.println(player.name);
            for (Ship ship : player.pool.ships) {
                System.out.println("New ship");
                for (Map.Entry<Integer, List<Integer>> entry : ship.location.entrySet()) {
                    int x = entry.getKey();
                    System.out.println("Entry: " + entry);
                    for (Integer y : entry.getValue()) {
                        buttons[x][y].setBackground(Color.BLUE);
                        System.out.println("Cell: " + x + " " + y);
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
    }
}
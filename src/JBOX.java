import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JBOX {
    public static void main(String[] args) {
        SDCTD sdctd = new SDCTD();
        MainFrame mainFrame = new MainFrame(sdctd.player);
        System.out.println("___Ship: create_ship: pool.ships: " + sdctd.player.pool.ships.entrySet());
        System.out.println("___Ship: create_ship: pool.ships: " + sdctd.player.pool.pool.entrySet());
    }
}

class MainFrame {
    JFrame mainFrame = new JFrame();

    MainFrame(Player player) {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);

        PoolFrame pp = new PoolFrame(player);
        pp.EnButtons = false;
        PoolFrame pe = new PoolFrame(player);

        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(new PoolFrame(player), BorderLayout.WEST);
        mainFrame.add(new PoolFrame(player), BorderLayout.EAST);

        mainFrame.setTitle("BattleshipGame");
        mainFrame.setSize(1200, 700);
        mainFrame.setVisible(true);
    }


    static class PoolFrame extends JPanel {
        JPanel poolPanel = new JPanel();
        private final JButton[][] buttons = new JButton[10][10];
        boolean EnButtons = true;

        PoolFrame(Player player) {
            poolPanel.setLayout(new GridLayout(10, 10));
            poolPanel.setBackground(Color.GRAY);

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    buttons[i][j] = new JButton();
                    buttons[i][j].setEnabled(this.EnButtons);
                    buttons[i][j].setPreferredSize(new Dimension(40, 40));
                    poolPanel.add(buttons[i][j]);
                }
            }
            visual_ships(player);
            poolPanel.setPreferredSize(new Dimension(400, 400));
            this.add(poolPanel);
        }

        public void visual_ships(Player player) {
            System.out.println("___visual_ships: started");
            System.out.println("___visual_ships: started" + player.pool.ships.entrySet());
            List<String> tag_abc= List.of(player.pool.tag_abc);
            List<String> tag_nums = List.of(player.pool.tag_nums);

            for (Map.Entry<String, HashMap<String, Ship>> a : player.pool.ships.entrySet()) {
                int n = tag_abc.indexOf(a.getKey());
                for (Map.Entry<String, Ship> b : a.getValue().entrySet()) {
                    int j = tag_nums.indexOf(b.getKey());
                    buttons[n][j].setBackground(Color.BLUE);
                }
            }
        }
    }
}

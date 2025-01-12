import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JBOX {
    public static void main(String[] args) {
        SDCTD sdctd = new SDCTD();
        MainFrame MFrame = new MainFrame(sdctd.player);
    }
}

class MainFrame {
    JFrame mainFrame = new JFrame();

    MainFrame(Player player) {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);

        PoolFrame pp = new PoolFrame(player);
        pp.visualShipsPOOL(player);
        pp.EnButtons = false;
        PoolFrame pe = new PoolFrame(player);

        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(pp, BorderLayout.WEST);
        mainFrame.add(pe, BorderLayout.EAST);

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
            poolPanel.setPreferredSize(new Dimension(400, 400));
            this.add(poolPanel);
        }

        public void visualShipsLOC(Player player) {
            for (Ship ship : player.pool.ships) {
                for (Map.Entry<Integer, List<Integer>> s_ship : ship.location.entrySet()) {
                    int key = s_ship.getKey();
                    for (Integer value : s_ship.getValue()) {
                        buttons[key][value].setBackground(Color.BLUE);
                    }
                }
            }
        }

        public void visualShipsPOOL(Player player) {

            for (Map.Entry<Integer, HashMap<Integer, Object>> ship : player.pool.pool.entrySet()) {
                int key = ship.getKey();
                for (Map.Entry<Integer, Object> s_ship : ship.getValue().entrySet()) {
                    Integer vey = s_ship.getKey();
                    Object value = s_ship.getValue();

                    if (value instanceof Ship) {
                        buttons[key][vey].setBackground(Color.BLUE);
                    } else if (!((boolean) value)) {
                        buttons[key][vey].setBackground(Color.GREEN);
                    }

                }
            }
        }
    }
}

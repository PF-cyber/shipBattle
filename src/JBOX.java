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

    class PoolFrame extends JPanel {
        JPanel poolPanel = new JPanel();
        private JButton[][] buttons;
        boolean EnButtons = true;

        PoolFrame(Player player) {
            poolPanel.setLayout(new GridLayout(10, 10));
            poolPanel.setBackground(Color.GRAY);

            buttons = new JButton[10][10];

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    buttons[i][j] = new JButton();
                    buttons[i][j].setEnabled(this.EnButtons);
                    buttons[i][j].setPreferredSize(new Dimension(40, 40));
                    poolPanel.add(buttons[i][j]);

                    visual_ships(player);
                }
            }

            poolPanel.setPreferredSize(new Dimension(400, 400));

            this.add(poolPanel);
        }

        public void visual_ships(Player player) {
            System.out.println("___visual_ships: started");
            System.out.println("___visual_ships: started" + player.pool.ships.entrySet());
            List<Integer> tag_abc = new ArrayList<>();
            List<Integer> tag_nums = new ArrayList<>();

            System.out.println(player.pool.ships.entrySet());
            for (Map.Entry<String, HashMap<String, Ship>> a : player.pool.ships.entrySet()) {
                tag_abc.add(List.of(player.pool.tag_abc).indexOf(a.getKey()));
                System.out.println("___ABCletters:");
                System.out.println(a.getKey());
                System.out.println(tag_abc);
                for (Map.Entry<String, Ship> b : a.getValue().entrySet()) {
                    System.out.println("___NUMSletters:");
                    System.out.println(b.getKey());
                    System.out.println(tag_nums);
                    tag_nums.add(List.of(player.pool.tag_nums).indexOf(b.getKey()));

                }

            for (int i = 0; i < tag_abc.size() - 2; i++) {
                buttons[tag_abc.get(i)][tag_nums.get(i)].setBackground(Color.BLUE);
            }
            }
        }
    }
}

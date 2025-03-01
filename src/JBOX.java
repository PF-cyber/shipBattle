import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JBOX {
    public static void main(String[] args) {
        SDCTD sdctd = new SDCTD();
        MainFrame MFrame = new MainFrame(sdctd);
    }
}

class MainFrame {
    JFrame mainFrame = new JFrame();

    MainFrame(SDCTD sdctd) {
        Player currentPlayer = sdctd.player;
        Player enemyPlayer =sdctd.enemy;

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);

        PoolFrame playerFrame = new PoolFrame(currentPlayer);
        playerFrame.visualShips(currentPlayer);
        playerFrame.EnButtons = false;

        PoolFrame enemyFrame = new PoolFrame(enemyPlayer);
        enemyFrame.visualShips(enemyPlayer);

        RemoteFrame xyremote = new RemoteFrame(currentPlayer);

        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(playerFrame, BorderLayout.WEST);
        mainFrame.add(enemyFrame, BorderLayout.EAST);

        mainFrame.add(xyremote, BorderLayout.SOUTH);

        mainFrame.setTitle("BattleshipGame");
        mainFrame.setSize(1200, 750);
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

        public void visualPool(Player player) {
            HashMap<Integer, HashMap<Integer, Object>> pool = player.pool.pool;
            System.out.println(pool);
            for (Map.Entry<Integer, HashMap<Integer, Object>> entry : pool.entrySet()){
                int x = entry.getKey();
                for (Map.Entry<Integer, Object> entry1 : entry.getValue().entrySet()){
                    int y = entry1.getKey();
                    if (entry1.getValue() instanceof Ship){
                        buttons[x][y].setBackground(Color.BLUE);
                    }
                }
            }
        }

        public void visualShips(Player player) {
            List<Ship> list_ship = player.pool.ships;
            for (Ship ship : list_ship){
                for (Map.Entry<Integer, List<Integer>> entry : ship.location.entrySet()){
                    int x = entry.getKey();
                    for (int y : entry.getValue()){
                        buttons[x][y].setBackground(Color.BLUE);
                    }
                }
            }
        }
    }
    static class RemoteFrame extends JPanel{
        JPanel remoteX = new JPanel();

        RemoteFrame (Player player){
            final JButton[][] remote_buttons = new JButton[2][10];
            remoteX.setLayout(new GridLayout(2, 10));
            remoteX.setBackground(Color.GRAY);

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 10; j++) {
                    remote_buttons[i][j] = new JButton();
                    remote_buttons[i][j].setEnabled(true);
                    remote_buttons[i][j].setPreferredSize(new Dimension(50, 50));
                    remote_buttons[i][j].setText(String.valueOf(j+1));
                    remote_buttons[i][j].setBackground(Color.WHITE);
                    remoteX.add(remote_buttons[i][j]);
                }
            }
            this.add(remoteX);
        }
    }
}

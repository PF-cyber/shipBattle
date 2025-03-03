import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class JBOX {
    JBOX() {
        SDCTD sdctd = new SDCTD();
        new MainFrame(sdctd);
    }
}

class MainFrame {
    JFrame mainFrame = new JFrame();

    MainFrame(SDCTD sdctd) {
        Player currentPlayer = sdctd.player;
        Player enemyPlayer = sdctd.enemy;

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);

//        try {
//            mainFrame.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("./static/sea.jpg")))));
//        } catch (IOException e) {
//            System.out.println("Background file not found");
//        }
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    g.drawImage(new ImageIcon(ImageIO.read(new File("./static/sea.jpg"))).getImage(), 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        mainFrame.setContentPane(backgroundPanel);

        ScoreFrame scoreFrame = new ScoreFrame(enemyPlayer);

        PlayerFrame playerFrame = new PlayerFrame();
        playerFrame.visualShips(currentPlayer);

        EnemyFrame enemyFrame = new EnemyFrame(enemyPlayer, scoreFrame);
        enemyFrame.visualShips(enemyPlayer);

        backgroundPanel.add(playerFrame, BorderLayout.WEST);
        backgroundPanel.add(enemyFrame, BorderLayout.EAST);
        backgroundPanel.add(scoreFrame, BorderLayout.SOUTH);

        mainFrame.setTitle("BattleshipGame");
        backgroundPanel.setSize(1200, 800);
        mainFrame.setIconImage(new ImageIcon("./static/ship.png").getImage());

        mainFrame.pack();
        mainFrame.setVisible(true);
    }


    static class EnemyFrame extends JPanel {
        JPanel enemyFrame = new JPanel();

        private final JButton[][] buttons = new JButton[10][10];

        EnemyFrame(Player player, ScoreFrame scoreFrame) {

            enemyFrame.setLayout(new GridLayout(10, 10));
            enemyFrame.setBackground(Color.GRAY);
            this.setOpaque(false);

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    buttons[i][j] = new JButton();
                    buttons[i][j].setPreferredSize(new Dimension(40, 40));
                    enemyFrame.add(buttons[i][j]);

                    buttons[i][j].setMargin(new Insets(0, 0, 0, 0));

                    final int x = i;
                    final int y = j;
                    buttons[i][j].addActionListener(_ -> shoot(x, y, player, scoreFrame));
                }
            }

            enemyFrame.setPreferredSize(new Dimension(400, 400));
            this.add(enemyFrame);
        }

        public void visualPool(Player player) {
            HashMap<Integer, HashMap<Integer, Object>> pool = player.pool.pool;
            System.out.println(pool);
            for (Map.Entry<Integer, HashMap<Integer, Object>> entry : pool.entrySet()) {
                int x = entry.getKey();
                for (Map.Entry<Integer, Object> entry1 : entry.getValue().entrySet()) {
                    int y = entry1.getKey();
                    if (entry1.getValue() instanceof Ship) {
                        buttons[x][y].setBackground(Color.BLUE);
                    }
                }
            }
        }

        private void shoot(int x, int y, Player player, ScoreFrame scoreFrame) {
            String hash = "✖";
            int accurate = player.shoot(x, y);

            buttons[x][y].setText(hash);
            buttons[x][y].setFont(new Font("FontAwesome", Font.PLAIN, 20));
            buttons[x][y].setForeground(Color.red);
            if (accurate == 1) {
                buttons[x][y].setBackground(Color.BLUE);
            } else if (accurate == 2) {
                Object ship = player.pool.pool.get(x).get(y);
                HashMap<Integer, List<Integer>> location = ((Ship) ship).location;

                for (Map.Entry<Integer, List<Integer>> entry : location.entrySet()) {
                    int i = entry.getKey();
                    for (int j : entry.getValue()) {
                        System.out.println("RED: " + i + " " + j);
                        buttons[i][j].setBackground(Color.RED);
                    }
                }
                scoreFrame.updateShipsCount(((Ship) ship).ship_size);
            }
            buttons[x][y].setEnabled(false);
        }

        public void visualShips(Player player) {
            List<Ship> list_ship = player.pool.ships;
            for (Ship ship : list_ship) {
                Color color = new Color(
                        new Random().nextInt(256),
                        new Random().nextInt(256),
                        new Random().nextInt(256));

                for (Map.Entry<Integer, List<Integer>> entry : ship.location.entrySet()) {
                    int x = entry.getKey();
                    for (int y : entry.getValue()) {
                        buttons[x][y].setBackground(color);
                    }
                }
            }
        }
    }


    static class PlayerFrame extends JPanel {
        JPanel playerFrame = new JPanel();
        private final JLabel[][] CellLabel = new JLabel[10][10];

        PlayerFrame() {
            playerFrame.setLayout(new GridLayout(10, 10));
            this.setOpaque(false);


            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    CellLabel[x][y] = new JLabel();
                    CellLabel[x][y].setOpaque(true);
                    CellLabel[x][y].setBorder(BorderFactory.createBevelBorder(1));
                    CellLabel[x][y].setPreferredSize(new Dimension(40, 40));
                    playerFrame.add(CellLabel[x][y]);
                }
            }
            this.add(playerFrame);
            playerFrame.setPreferredSize(new Dimension(400, 400));
        }

        public void visualShips(Player player) {
            List<Ship> list_ship = player.pool.ships;
            for (Ship ship : list_ship) {
                for (Map.Entry<Integer, List<Integer>> entry : ship.location.entrySet()) {
                    int x = entry.getKey();
                    for (int y : entry.getValue()) {
                        CellLabel[x][y].setBackground(Color.BLUE);
                    }
                }
            }
        }
    }


    static class ScoreFrame extends JPanel {
        private final JLabel[] sizeLabels = new JLabel[4];
        HashMap<Integer,Integer> ship_hash = new HashMap<>();
        ScoreFrame(Player enemy) {
            setLayout(new GridLayout(4, 1, 5, 5));
            setOpaque(false);

            for (int i = 4; i > 0; i--) {
                JPanel shipPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                shipPanel.setOpaque(false);

                JLabel titleLabel = new JLabel((5-i) + "-палубные:");
                titleLabel.setForeground(Color.WHITE);

                sizeLabels[4-i] = createShipLabel("0");

                shipPanel.add(titleLabel);
                shipPanel.add(sizeLabels[4-i]);
                add(shipPanel);
            }

            for (int i=1; i<5; i++){
                ship_hash.put(5-i, i);
            }
        }

        private JLabel createShipLabel(String text) {
            JLabel label = new JLabel(text);
            label.setPreferredSize(new Dimension(40, 40));
            label.setOpaque(true);
//            label.setBackground(new Color(200, 200, 200, 150));
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            return label;
        }

        public void updateShipsCount(int ship_size) {
            int count = Integer.parseInt(sizeLabels[ship_size-1].getText());
            count += 1;
            sizeLabels[ship_size-1].setText(String.valueOf(count));
            if (count == ship_hash.get(ship_size)){
                sizeLabels[ship_size-1].setBackground(Color.RED);
                sizeLabels[ship_size-1].setText("✖");

            }
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JBOX {
    MainFrame mainFrame;

    JBOX(Player player, Bot bot, JUDGE judge) {
        mainFrame = new MainFrame(player, bot, judge);
    }
}

class MainFrame {
    JFrame mainFrame = new JFrame();

    MainFrame(Player player, Bot bot, JUDGE judge) {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);

        player.poolFrame = new PoolFrame(player);
        bot.poolFrame = new PoolFrame(bot);

        JPanel leftContainer = new JPanel();
        leftContainer.setLayout(new BoxLayout(leftContainer, BoxLayout.Y_AXIS));
        leftContainer.add(Box.createVerticalGlue());
        leftContainer.add(player.poolFrame);
        leftContainer.add(Box.createVerticalGlue());

        JPanel rightContainer = new JPanel();
        rightContainer.setLayout(new BoxLayout(rightContainer, BoxLayout.Y_AXIS));
        rightContainer.add(Box.createVerticalGlue());
        rightContainer.add(bot.poolFrame);
        rightContainer.add(Box.createVerticalGlue());

        JPanel contentPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 20, 0, 20);
        contentPane.add(leftContainer, gbc);

        gbc.gridx = 1;
        contentPane.add(rightContainer, gbc);

        mainFrame.setContentPane(contentPane);
        bot.poolFrame.setterButtons(judge, true);
        player.poolFrame.setterButtons(judge, false);

        mainFrame.setTitle("BattleshipGame");
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    public void showVictory(String winnerName) {
        JDialog victoryDialog = new JDialog(mainFrame, "Победа!", true);
        victoryDialog.setLayout(new BorderLayout());
        victoryDialog.setSize(300, 200);
        victoryDialog.setLocationRelativeTo(mainFrame);

        JLabel label = new JLabel("🏆 " + winnerName + " побеждает!", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(new Color(0, 100, 0));

        JButton closeButton = new JButton("Закрыть");
        closeButton.addActionListener(e -> System.exit(0));

        victoryDialog.add(label, BorderLayout.CENTER);
        victoryDialog.add(closeButton, BorderLayout.SOUTH);
        victoryDialog.setVisible(true);
    }

    static class PoolFrame extends JPanel {
        private final Color SEA_COLOR = new Color(0, 105, 148);
        private final Color SHIP_COLOR = new Color(139, 69, 19);
        private final Color HIT_COLOR = new Color(255, 0, 0);
        private final Color SUNK_COLOR = new Color(69, 3, 3);
        private final Color MISS_COLOR = new Color(200, 200, 3, 200);
        private final Color GRID_COLOR = new Color(64, 164, 223);

        private final JButton[][] buttons;
        JPanel poolPanel = new JPanel();
        JPanel blockPanel = new WavePanel();

        APlayer player;


        PoolFrame(APlayer player) {
            this.player = player;

            this.setLayout(new OverlayLayout(this));
            this.setOpaque(false);

            poolPanel.setLayout(new GridLayout(10, 10));
            poolPanel.setBackground(SEA_COLOR);
            poolPanel.setAlignmentX(0.5f);
            poolPanel.setAlignmentY(0.5f);

            poolPanel.setBorder(BorderFactory.createLineBorder(GRID_COLOR, 2));

            blockPanel.setOpaque(false);
            blockPanel.setBackground(new Color(0, 255, 0));
            blockPanel.setAlignmentX(0.5f);
            blockPanel.setAlignmentY(0.5f);

            buttons = new JButton[10][10];
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    buttons[i][j] = new JButton();
                    setButtonIcons(i, j);
                    buttons[i][j].setEnabled(false);
                    poolPanel.add(buttons[i][j]);
                }
            }

            this.add(poolPanel, JLayeredPane.DEFAULT_LAYER);
            this.add(blockPanel, JLayeredPane.PALETTE_LAYER);
        }

        public void statusBlockPanel(boolean on) {
            blockPanel.setVisible(on);
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

        public void damageCell(int x, int y, int result) {
            JButton button = buttons[x][y];

            if (result == 2) {

                Timer timer = new Timer(100, new ActionListener() {
                    int count = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (count % 2 == 0) {
                            button.setBackground(Color.RED);
                        } else {
                            button.setBackground(Color.ORANGE);
                        }
                        count++;
                        if (count > 6) {
                            ((Timer) e.getSource()).stop();
                            button.setBackground(HIT_COLOR);
                        }
                    }
                });
                timer.start();
            } else if (result <= 1) {
                button.setBackground(MISS_COLOR);
                button.setText("●");
            } else if (result == 3) {
                sunkShip(x, y);
            }
            button.setEnabled(false);
        }

        public void sunkShip(int x, int y) {
            HashMap<Integer, List<Integer>> location = ((Ship) player.pool.pool.get(x).get(y)).location;
            for (Map.Entry<Integer, List<Integer>> entry : location.entrySet()) {
                int i = entry.getKey();
                for (int j : entry.getValue()) {
                    buttons[i][j].setBackground(SUNK_COLOR);
                }
            }
        }

        ;

        public void setterButtons(JUDGE judge, boolean on) {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    int finalX = x;
                    int finalY = y;
                    buttons[x][y].setEnabled(on);
                    buttons[x][y].addActionListener(e -> {
                        judge.shoot(finalX, finalY);
                        buttons[finalX][finalY].setEnabled(false);
                    });
                }
            }
        }

        private void setButtonIcons(int i, int j) {
            buttons[i][j].setPreferredSize(new Dimension(40, 40));
            buttons[i][j].setContentAreaFilled(false);
            buttons[i][j].setBorder(BorderFactory.createLineBorder(GRID_COLOR, 1));
            buttons[i][j].setOpaque(true);
            buttons[i][j].setBackground(SEA_COLOR);
        }

        @Override
        public Dimension getPreferredSize() {

            int buttonSize = 40;
            int gap = 2;
            int size = 10 * (buttonSize + gap) + gap;
            return new Dimension(size, size);
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        @Override
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        class WavePanel extends JPanel {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;


                g2d.setColor(new Color(0, 0, 255, 50));
                g2d.fillRect(0, 0, getWidth(), getHeight());


                g2d.setColor(new Color(255, 255, 255, 100));
                for (int i = 0; i < getWidth(); i += 20) {
                    g2d.drawArc(i, 10, 30, 10, 0, 180);
                    g2d.drawArc(i + 15, 15, 30, 8, 0, 180);
                }
            }
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class JBOX {
    JBOX(Player player, Bot bot, JUDGE judge){
        new MainFrame(player, bot, judge);
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

    static class PoolFrame extends JPanel {
        private final JButton[][] buttons;
        JPanel poolPanel = new JPanel();
        JPanel blockPanel = new JPanel();

        PoolFrame(APlayer player) {

            this.setLayout(new OverlayLayout(this));
            this.setOpaque(false);

            poolPanel.setLayout(new GridLayout(10, 10));
            poolPanel.setBackground(Color.GRAY);
            poolPanel.setAlignmentX(0.5f);
            poolPanel.setAlignmentY(0.5f);

            blockPanel.setBackground(new Color(0, 255, 0));
            blockPanel.setAlignmentX(0.5f);
            blockPanel.setAlignmentY(0.5f);

            buttons = new JButton[10][10];
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    buttons[i][j] = new JButton();
                    buttons[i][j].setPreferredSize(new Dimension(40, 40));
                    buttons[i][j].setEnabled(false);
                    poolPanel.add(buttons[i][j]);
                }
            }

            this.add(poolPanel, JLayeredPane.DEFAULT_LAYER);
            this.add(blockPanel, JLayeredPane.PALETTE_LAYER);
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

        public void damageCell(int x, int y, int result) {
            System.out.println("Damage result: " + result);
            JButton button = buttons[x][y];
            if (result >= 2) {
                button.setBackground(Color.RED);
            } else if(result <= 1){
                button.setBackground(Color.BLACK);
            }
            button.setEnabled(false);
        }

        public void statusButtons(boolean on) {
            for (JButton[] button_mes : buttons) {
                for (JButton button : button_mes) {
                    button.setEnabled(on);
                }
            }
        }

        public void statusBlockPanel(boolean on){
            blockPanel.setVisible(on);
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
    }
}

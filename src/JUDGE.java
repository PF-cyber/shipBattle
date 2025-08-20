import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JUDGE {
    List<APlayer> queue = new ArrayList<>();
    boolean AleaJacta;

    JUDGE(Player player, Bot bot) {
    }

    public void AleaJactaEst(APlayer player, Bot bot) {
        queue.add(player);
        queue.add(bot);
        AleaJacta = new Random().nextBoolean();
        if (!AleaJacta) {
            queue = queue.reversed();
        }
        System.out.println("First turn: " + queue.getFirst().name);
        System.out.println(queue);
        queue.getFirst().poolFrame.statusBlockPanel(false);
        firstTurn();
    }

    public void shoot(int x, int y) {
        int result = 0;
        APlayer assaulter = queue.getFirst();

        if(assaulter instanceof Player){
            result = assaulter.shoot(x, y);
        }
        if (result <= 1) {
            nextQueue();
            System.out.println(this.queue);
            assaulter = queue.getFirst();
        }
        if(assaulter instanceof Bot){
            do {
                result = ((Bot) assaulter).makeMove();
            } while (result > 1 );
            nextQueue();
        }
    }

    private void nextQueue() {
        queue.getFirst().poolFrame.statusBlockPanel(true);
        this.queue = queue.reversed();
        queue.getFirst().poolFrame.statusBlockPanel(false);
    }

    public void firstTurn() {
        APlayer assaulter = queue.getFirst();
        if(assaulter instanceof Bot){
            int result = 0;
            do {
                result = ((Bot) assaulter).makeMove();
            } while(result > 1);
            nextQueue();
        }
    }

    private void whoWin(){
        if(queue.getFirst().pool.ships.isEmpty()){
            System.out.println(queue.getLast().name + " is winner!");
        } else if(queue.getLast().pool.ships.isEmpty()){
            System.out.println(queue.getLast().name + " is winner!");
        }
    }
}
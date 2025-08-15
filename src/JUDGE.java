import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JUDGE {
    List<APlayer> queue = new ArrayList<>();
    boolean AleaJacta;


    JUDGE(Player player, Bot bot) {
        AleaJactaEst(player, bot);
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
        queue.getFirst().poolFrame.statusButtons(false);
    }

    public void shoot(int x, int y){
        int result = (queue.getFirst().shoot(x, y));
        if (result != 2){
            nextQueue();
            System.out.println(this.queue);
        }
    }

    private void nextQueue() {
        this.queue = queue.reversed();

    }

    public void blockTurn(APlayer player){

    }
}
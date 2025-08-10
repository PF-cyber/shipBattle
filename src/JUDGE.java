import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JUDGE {
    List<APlayer> queue = new ArrayList<>();
    boolean AleaJacta;

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

    public void shoot_evaluetion(int result){
        if (result == 1){
            nextQueue();
            System.out.println("Change queue: " + queue.getFirst().name + " " + queue.getLast().name);
        }
    }

    private void nextQueue() {
        this.queue = queue.reversed();
        blockTurn();
    }

    public void blockTurn(){
        this.queue.getFirst().poolFrame.statusButtons(false);
        this.queue.getLast().poolFrame.statusButtons(true);
    }
}
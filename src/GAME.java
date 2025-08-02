public class GAME {

    static void main() {
        MODELS models = new MODELS();
        Player player = models.player;
        Bot bot = models.bot;

        JUDGE judge = new JUDGE(player, bot);
        JBOX jbox = new JBOX(player, bot);


    }
}

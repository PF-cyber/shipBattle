import java.text.MessageFormat;
import java.util.*;
import java.util.List;

public class MODELS {
    Player player = new Player();
    Bot bot = new Bot();

    MODELS() {
        player.enemy = bot;
        bot.enemy = player;
    }
}

abstract class APlayer {
    String name;
    Pool pool;
    MainFrame.PoolFrame poolFrame;
    APlayer enemy;

    APlayer() {
        pool = new Pool();
    }

    public Integer shoot(int x, int y) {
        Object cell = this.enemy.pool.pool.get(x).get(y);
        System.out.println(MessageFormat.format("{0} shoot x:{1} y:{2}", this.name, x, y));
        if (cell instanceof Ship) {
            return ((Ship) cell).getDamage();
        } else if ((Boolean) this.enemy.pool.pool.get(x).get(y)) {
            this.enemy.pool.pool.get(x).put(y, false);
            System.out.println("Miss");
            return 1;
        } else {
            System.out.println("Again?");
            return 0;
        }
    }
}

class Player extends APlayer {
    Player() {
        this.name = "player_1";
    }
}

class Bot extends APlayer {
    int st;

    HashMap<Integer, List<Integer>> target_map;

    Bot() {
        this.name = "bot_1";
    }

    class HUNTER {
        HashMap<Integer, Integer> last_target;
        int st = 0;
        HashMap<String, Integer> o = new HashMap<>(Map.of("h", 0, "v", 0));
        ArrayList<Integer> last_cell;
        ArrayList<Integer> target_cell;
        HashMap<Integer, List<Integer>> target_map = new HashMap<>();
        int last_shoot_result;

        private void create_target_map() {
            for (int x = 0; x < 10; x++) {
                target_map.put(x, new ArrayList<>());
                for (int y = 0; y < 10; y++) {
                    target_map.get(x).add(y);
                }
            }
            System.out.println("HUNTER: target_map created");
        }

        private void search_cell() {
            if (st == 0) {
                int x = new Random()
                int y = new Random().nextInt(10);

                last_cell = new ArrayList<>(List.of(x, y));
                target_cell = new ArrayList<>(List.of(x, y));
            }
        }

        private void starct_target(int shoot_result) {
            int x;
            int y;
            if (st == 2 & shoot_result == 2) { //if target and hit

                last_cell = new ArrayList<>(List.of(x, y));
                target_cell = new ArrayList<>(List.of(x, y));
            }  else if(st == 2 & shoot_result == 1){ //if target and not hit

            }
        }

        private List<Object> geto(){
            for(Map.Entry<String, Integer> entry : o.entrySet()){
                if (entry.getValue() != 0){
                    return new ArrayList<>(List.of(entry.getKey(), entry.getValue()));
                }
            }
            return null;
        }

        private List<Integer> delta_cord(int x, int y){
            List<Object> d_o = geto();
            int dx = 0;
            int dy = 0;
            if(d_o.getFirst() == "h"){
               dy = y + (int)d_o.getLast();
               if (outrange(dy)){
                   dy = y;
               }
            } else if (d_o.getFirst() == "v"){
                dx = x + (int)d_o.getLast();
                if (outrange(dx)){
                    dx = x;
                }
            }
            return new ArrayList<>(List.of(dx, dy));
        }

        private HashMap<Integer, Integer> getRandom(){
            Random r = new Random();
            List<Map.Entry<Integer, List<Integer>>> entries = new ArrayList<>(target_map.entrySet());
            Map.Entry<Integer, List<Integer>> re = entries.get(r.nextInt(entries.size()));
            int x = re.getKey();
            int y = re.getValue().get(r.nextInt(re.getValue().size()));
            System.out.println("Random x:" + x +" y:"+ y);

            return new HashMap<Integer, Integer>(x, y);
        }

        private boolean outrange(int a){
            if (a < 0 || a > 10){
                return true;
            }
            return false;
        }
    }
}

class Pool {
    HashMap<Integer, HashMap<Integer, Object>> pool = new HashMap<>();
    List<Integer> tag_x = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    List<Integer> tag_y = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    List<Ship> ships = new ArrayList<>();

    Pool() {
        createPool();
        createShips();
        this.visualPool();
    }

    private void createPool() {
        for (int i = 0; i < 10; i++) {
            HashMap<Integer, Object> innerMap = new HashMap<>();
            for (int j = 0; j < 10; j++) {
                innerMap.put(j, true);
            }
            this.pool.put(i, innerMap);
        }
    }

    private boolean checkAround(HashMap<Integer, List<Integer>> cells) {
        int fx = Collections.min(new ArrayList<Integer>(cells.keySet()));
        int fy = cells.get(fx).getFirst();
        int lx = Collections.max(new ArrayList<Integer>(cells.keySet()));
        int ly = cells.get(lx).getLast();

        fy = (fy > 0) ? fy - 1 : fy;
        fx = (fx > 0) ? fx - 1 : fx;
        lx = (lx < 9) ? lx + 1 : lx;
        ly = (ly < 9) ? ly + 1 : ly;
        System.out.println("fy:" + fy + " fx:" + fx + " lx:" + lx + " ly" + ly);

        for (int sx = fx; sx <= lx; sx++) {
            for (int sy = fy; sy <= ly; sy++) {
                System.out.println("\t\t" + "x: " + sx + " y:" + sy + "  ");

                if ((sx < 0 || sx > 9) || (sy < 0 || sy > 9)) {
                    System.out.println("Out of range:" + "sx" + sx + "sy" + sy);
                    return false;
                } else if (pool.get(sx).get(sy) == null) {
                    System.out.println("Check is null");
                    return false;
                } else if (pool.get(sx).get(sy) instanceof Ship) {
                    System.out.println("Place is ship");
                    return false;
                } else if (!(boolean) pool.get(sx).get(sy)) {
                    System.out.println("Place is busy");
                    return false;
                }
            }
        }
        System.out.println();
        System.out.println("INSERT SHIP");
        System.out.println(cells);
        return true;
    }

    private boolean createVertical(int x, int y, int size) {
        HashMap<Integer, List<Integer>> map_string = new HashMap<>();
        for (int mx = x + size; x < mx; x++) {
            map_string.put(x, new ArrayList<>(List.of(y)));
        }
        if (checkAround(map_string)) {
            new Ship(size, map_string, this);
            return true;
        }
        return false;
    }

    private boolean createHorizontal(int x, int y, int size) {
        HashMap<Integer, List<Integer>> map_string = new HashMap<>();

        for (int my = y + size; y < my; y++) {
            if (!map_string.containsKey(x)) {
                map_string.put(x, new ArrayList<>());
            }
            map_string.get(x).add(y);
        }

        if (checkAround(map_string)) {
            new Ship(size, map_string, this);
            return true;
        } else {
            return false;
        }
    }

    public void visualPool() {
        for (int x : pool.keySet()) {
            System.out.print(x + ": ");
            for (int y : pool.get(x).keySet()) {
                if (pool.get(x).get(y) instanceof Ship) {
                    System.out.print(" S ");
                } else if ((boolean) pool.get(x).get(y)) {
                    System.out.print(" T ");
                } else if (!(boolean) pool.get(x).get(y)) {
                    System.out.print(" F ");
                } else {
                    System.out.print(" ? ");
                }
            }
            System.out.println("|");
        }
    }

    private void createShips() {
        HashMap<Integer, Integer> quantity_size = new HashMap<>();
        quantity_size.put(1, 4);
        quantity_size.put(2, 3);
        quantity_size.put(3, 2);
        quantity_size.put(4, 1);

        for (int quan : quantity_size.keySet()) {
            int size = quantity_size.get(quan);
            for (int i = 0; i < quan; i++) {
                if (new Random().nextBoolean()) {
                    while (!createVertical(new Random().nextInt(10),
                            new Random().nextInt(10), size)) ;
                } else {
                    while (!createHorizontal(new Random().nextInt(10),
                            new Random().nextInt(10), size)) ;
                }
            }
        }
    }
}

class Ship {
    HashMap<Integer, List<Integer>> location;
    Integer size;
    Integer hp;

    Ship(Integer size, HashMap<Integer, List<Integer>> cells, Pool pool) {
        if (checkAround(cells, pool.pool)) {
            createShip(size, cells, pool.pool);
            addList(pool);
        }
        this.location = cells;
        this.hp = size;
        this.size = size;
    }

    private void addList(Pool pool) {
        if (!pool.ships.contains(this)) {
            pool.ships.add(this);
        }
    }

    public Integer getDamage() {
        this.hp -= 1;
        if (this.hp == 0) {
            System.out.println("Sunk!");
            return 3;
        } else {
            System.out.println("Get hit!");
            return 2;
        }
    }

    private void createShip(Integer size,
                            HashMap<Integer, List<Integer>> cells,
                            HashMap<Integer, HashMap<Integer, Object>> pool) {
        for (Map.Entry<Integer, List<Integer>> entry : cells.entrySet()) {
            int key = entry.getKey();
            for (Integer value : entry.getValue()) {
                pool.get(key).put(value, this);
            }
        }
    }

    private boolean checkAround(HashMap<Integer, List<Integer>> cells,
                                HashMap<Integer, HashMap<Integer, Object>> pool) {
        HashMap<Integer, HashMap<Integer, Object>> map = new HashMap<>();

        int fx = Collections.min(new ArrayList<Integer>(cells.keySet()));
        int fy = cells.get(fx).getFirst();
        int lx = Collections.max(new ArrayList<Integer>(cells.keySet()));
        int ly = cells.get(lx).getLast();

        fy = (fy > 0) ? fy - 1 : fy;
        fx = (fx > 0) ? fx - 1 : fx;
        lx = (lx < 9) ? lx + 1 : lx;
        ly = (ly < 9) ? ly + 1 : ly;

        for (; fx <= lx; fx++) {
            for (; fy <= ly; fy++) {
                if (pool.get(fx).get(fy) instanceof Ship) {
                    return false;
                } else if (!(boolean) pool.get(fx).get(fy)) {
                    return false;
                } else if (pool.get(fx).get(fy) == null) {
                    return false;
                }
            }
        }
        return true;
    }
}
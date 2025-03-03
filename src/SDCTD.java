import java.net.InterfaceAddress;
import java.util.*;
import java.util.List;


public class SDCTD {
    Player player;
    Player enemy;

    public SDCTD() {
        this.player = new Player();
        this.enemy = create_bot();
    }

    public static Player create_bot() {
        Player bot = new Player();
        bot.name = "Bot";

        return bot;
    }
}


class Player {
    Pool pool;
    Pool E_pool;
    String name = "Player1";
    int score = 0;

    Player() {
        pool = new Pool();
        pool.preparePool();
        E_pool = new Pool();
    }

    public int shoot(Integer x, Integer y) {
        Object obj = this.pool.pool.get(x).get(y);

        if (obj instanceof Ship){
            return ((Ship) obj).getDamage();

        } else if (obj instanceof Boolean){
            System.out.println("Miss!");
            return 0;
        }
        return 0;
    }
}


class Pool {
    HashMap<Integer, HashMap<Integer, Object>> pool = new HashMap<>();
    List<Ship> ships = new ArrayList<>();
    Pool() {
    }

    public void preparePool(){
        for (int i = 0; i < 10; i++) {
            pool.put(i, createMapString());
        }
        prepareShips();
    }

    private HashMap<Integer, Object> createMapString() {
        HashMap<Integer, Object> map_string = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            map_string.put(i, true);
        }
        return map_string;
    }


    public void prepareShips() {

        HashMap<Integer, Integer> dict_ships = new HashMap<>();
        dict_ships.put(1, 4);
        dict_ships.put(2, 3);
        dict_ships.put(3, 2);
        dict_ships.put(4, 1);
        for (Map.Entry<Integer, Integer> ship : dict_ships.entrySet()) {
            for (int i = 0; i < ship.getKey(); i++) {
                this.createShip(ship.getValue());
            }
        }
    }


    private void createShip(int ship_size) {

        Random random = new Random();
        HashMap<Integer, List<Integer>> cells;
        boolean isHorizontal = random.nextBoolean();
        if (isHorizontal) {
            cells = checkCellH(ship_size);
            Ship a = new Ship(cells, this, ship_size);
        } else {
            cells = checkCellV(ship_size);
            Ship a = new Ship(cells, this, ship_size);
        }
    }

    public HashMap<Integer, List<Integer>> checkCellV(int ship_size) {
        Random r = new Random();
        for (int num = r.nextInt(0, 9); num < 10; num++) {
            HashMap<Integer, List<Integer>> ships_cells = new HashMap<>();
            List<Integer> map_string = new ArrayList<>();

            for (int abc = r.nextInt(0, 9); abc < 10 - ship_size; abc++) {
                for (int len = 0; len < ship_size; len++) {
                    if (BoolORShip(pool.get(abc + len).get(num))) {
                        ships_cells.put(abc + len, Collections.singletonList(num));
                        if (ships_cells.size() == ship_size) {
                            return ships_cells;
                        }
                    }
                }
            }
        }
        return checkCellH(ship_size);
    }


    public HashMap<Integer, List<Integer>> checkCellH(int ship_size) {
        HashMap<Integer, List<Integer>> cells = new HashMap<>();
        Random r = new Random();

        for (int abc = r.nextInt(1, 7); abc < 10; abc++) {
            List<Integer> map_string = new ArrayList<>();

            for (int num = 0; num <= 10 - ship_size; num++) {
                boolean isValid = true;


                for (int len = 0; len < ship_size; len++) {
                    int snum = num + len;
                    if (!BoolORShip(pool.get(abc).get(snum))) {
                        isValid = false;
                        break;
                    }
                }


                if (isValid) {
                    for (int len = 0; len < ship_size; len++) {
                        map_string.add(num + len);
                    }
                    cells.put(abc, new ArrayList<>(map_string));
                    return cells;
                }
                map_string.clear();
            }
        }

        return checkCellV(ship_size);
    }


    public boolean BoolORShip(Object value) {
        if (value instanceof Boolean) {
            return (boolean) value;
        }
        if (value instanceof Ship) {
            return false;
        }
        return false;
    }
}


class Ship {
    Integer live;
    Integer ship_size;
    HashMap<Integer, List<Integer>> location = new HashMap<>();

    public Ship(HashMap<Integer, List<Integer>> cells, Pool pool, Integer ship_size) {
        this.location = cells;
        this.live = ship_size;
        this.ship_size = ship_size;
        createShip(pool);
    }


    private void createShip(Pool pool) {

        blockCells(pool, location);
        for (Map.Entry<Integer, List<Integer>> t_ship : this.location.entrySet()) {
            for (Integer tt_ship : t_ship.getValue()) {
                pool.pool.get(t_ship.getKey()).put(tt_ship, this);
            }
        }
        pool.ships.add(this);
    }


    public int getDamage() {
        this.live -= 1;
//        player.pool.pool.get(x).put(y, true);

        if (this.live == 0) {
            System.out.println("Sunk!");
            System.out.println("location:" + this.location);
            return 2;
        } else {
            System.out.println("Got it!");
            return 1;
        }
    }


    public void blockCells(Pool pool, HashMap<Integer, List<Integer>> cells) {
        List<Integer> keys = new ArrayList<>(cells.keySet());
        List<Integer> values = new ArrayList<>(new ArrayList<>(cells.values()).getFirst());

        List<Integer> mKeys = bDist(keys);
        List<Integer> mValues = bDist(values);

        for (int k = mKeys.getFirst(); k <= mKeys.getLast(); k++) {
            for (int v = mValues.getFirst(); v <= mValues.getLast(); v++) {
                pool.pool.get(k).put(v, false);
            }
        }
    }


    public List<Integer> bDist(List<Integer> list) {
        List<Integer> radius = new ArrayList<>();
        int mx = list.stream().max(Integer::compareTo).orElse(100);
        int mn = list.stream().min(Integer::compareTo).orElse(100);

        if (0 < mn) {
            radius.add(mn - 1);
        } else {
            radius.add(mn);
        }

        if (mx < 9) {
            radius.add(mx + 1);
        } else {
            radius.add(mx);
        }

        return radius;
    }
}


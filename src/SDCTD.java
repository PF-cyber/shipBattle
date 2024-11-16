import javax.swing.text.StyledEditorKit;
import java.net.InterfaceAddress;
import java.util.*;


public class SDCTD {
    Player player;
    Player bot;

    public SDCTD() {
        System.out.println("___SDCTD: starting backends...");
        this.player = new Player();
    }

    public static Player create_bot() {
        Player bot = new Player();
        bot.name = "Bot";

        return bot;
    }
}


class Player {
    Pool pool;
    String name = "Player1";

    Player() {
        pool = new Pool();
    }

    public void shoot(Integer x, Integer y, Player player) {
        if ((x == null) || (y == null)) {
            Scanner sc = new Scanner(System.in);
            x = Integer.parseInt(sc.next());
            y = Integer.parseInt(sc.next());
            sc.close();
        }
        if (player.pool.ships.get(x).get(y) != null && player.pool.pool.get(x).get(y)) {
            player.pool.ships.get(x).get(y).getDamage(x, y, player);
        }
    }
}

class Ship {
    Integer live;
    HashMap<Integer, List<Integer>> location = new HashMap<>();

    public Ship(HashMap<Integer, List<Integer>> c_l, Pool pool) {
        create_ship(c_l, pool);
    }

    public void create_ship(HashMap<Integer, List<Integer>> c_l, Pool pool) {
        for (Map.Entry<Integer, List<Integer>> E_cell : c_l.entrySet()) {
            HashMap<Integer, Ship> p_cell = pool.ships.getOrDefault(E_cell.getKey(), new HashMap<>());

            for (Integer C_cell : E_cell.getValue()) {
                p_cell.put(C_cell, this);
                pool.ships.put(E_cell.getKey(), p_cell);

                System.out.println("___Ship: create_ship: E_cell.getKey(): " + E_cell.getKey());
                System.out.println("___Ship: create_ship: E_cell.getValue(): " + E_cell.getValue());
                System.out.println("___Ship: create_ship: p_cell: " + p_cell);
            }
        }
        this.live = c_l.size();
    }

    public void getDamage(Integer x, Integer y, Player player) {
        this.live -= 1;

        player.pool.ships.remove(x);
        player.pool.pool.get(x).put(y, true);

        if (this.live == 0) {
            System.out.println("Sunk!");
        } else {
            System.out.println("Got it!");
        }
    }
}


class Pool {
    HashMap<Integer, HashMap<Integer, Boolean>> pool = new HashMap<>();
    HashMap<Integer, HashMap<Integer, Ship>> ships = new HashMap<>();

    Pool() {
        HashMap<Integer, Boolean> map_string = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            for (int n = 0; n < 10; n++) {
                map_string.put(n, true);
            }
            pool.put(i, map_string);
        }
        System.out.println("Pool is created");
        prepare_ships();
    }

    public void prepare_ships() {
        HashMap<Integer, Integer> dict_ships = new HashMap<>();
        dict_ships.put(1, 4);
        dict_ships.put(2, 3);
        dict_ships.put(3, 2);
        dict_ships.put(4, 1);

        for (Map.Entry<Integer, Integer> t_ship : dict_ships.entrySet()) {
            for (int s_ship = 0; s_ship < t_ship.getKey(); s_ship++) {
                create_ship(t_ship.getValue());
            }
        }
    }


    public void create_ship(int ship_size) {
        Random random = new Random();
        HashMap<Integer, List<Integer>> cells;
        boolean isHorizontal = random.nextBoolean();
        if (isHorizontal) {
            cells = checkCellH(ship_size);
            Ship a = new Ship(cells, this);
        } else {
            cells = checkCellV(ship_size);
            Ship a = new Ship(cells, this);
        }
        System.out.println("___create_ship: " + cells);
        block_cells(cells);
    }

    public HashMap<Integer, List<Integer>> checkCellV(int ship_size) {
        for (int num = 0; num < 10; num++) {
            HashMap<Integer, List<Integer>> ships_cells = new HashMap<>();
            List<Integer> map_string = new ArrayList<>();

            for (int abc = 0; abc < 10 - ship_size; abc++) {
                for (int len = 0; len < ship_size; len++) {
                    if (pool.get(abc + len).get(num)) {
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
        HashMap<Integer, List<Integer>> ships_cells = new HashMap<>();
        List<Integer> map_string = new ArrayList<>();

        for (int abc = 0; abc < 10; abc++) {
            for (int num = 0; num < 10 - ship_size; num++) {
                for (int len = 0; len < ship_size; len++) {
                    if (pool.get(abc).get(num + len)) {
                        map_string.add(num + len);
                        if (map_string.size() == ship_size) {
                            ships_cells.put(abc, map_string);
                            return ships_cells;
                        }
                    } else {
                        map_string = new ArrayList<>();
                    }
                }
            }
        }
        return checkCellV(ship_size);
    }

    public void block_cells(HashMap<Integer, List<Integer>> cells) {
        List<Integer> listABC = new ArrayList<>();
        List<Integer> listNUMS = new ArrayList<>();

        for (Map.Entry<Integer, HashMap<Integer, Ship>> ship : this.ships.entrySet()) {
            listABC.add(ship.getKey());
        }
    }

    public List<Integer> bDist(List<Integer> list) {
        List<Integer> radius = new ArrayList<>();

        for (int item : list) {
            if (0 < item) {
                radius.add(item - 1);
            }
            if (item < 10) {
                radius.add(item + 1);
            }
            radius.add(item);
        }
        return radius;
    }
}
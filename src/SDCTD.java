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

    public void shoot(String x, String y, Player player) {
        if ((x == null) || (y == null)) {
            Scanner sc = new Scanner(System.in);
            x = sc.next();
            y = sc.next();
            sc.close();
        }
        if (!player.pool.pool.get(x).get("locations").get(y)) {
            if ((player.pool.ships.get(x).get(y) != null)) {
                player.pool.ships.get(x).get(y).getDamage(x, y, player);
            }

        }
    }
}

class Ship {
    Integer live;

    public Ship(HashMap<String, List<String>> c_l, Pool pool) {
        create_ship(c_l, pool);
    }

    public void create_ship(HashMap<String, List<String>> c_l, Pool pool) {
        for (Map.Entry<String, List<String>> E_cell : c_l.entrySet()) {
            HashMap<String, Ship> p_cell = pool.ships.getOrDefault(E_cell.getKey(), new HashMap<>());

            for (String C_cell : E_cell.getValue()) {
                p_cell.put(C_cell, this);
                pool.ships.put(E_cell.getKey(), p_cell);

                System.out.println("___Ship: create_ship: E_cell.getKey(): " + E_cell.getKey());
                System.out.println("___Ship: create_ship: E_cell.getValue(): " + E_cell.getValue());
                System.out.println("___Ship: create_ship: p_cell: " + p_cell);
            }
        }
        this.live = c_l.size();
    }

    public void getDamage(String x, String y, Player player) {
        this.live -= 1;

        player.pool.ships.remove(x);
        player.pool.pool.get(x).get("locations").put(y, true);

        if (this.live == 0) {
            System.out.println("Sunk!");
        } else {
            System.out.println("Got it!");
        }
    }
}


class Pool {
    String[] tag_nums = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};//vertical y
    String[] tag_abc = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};//horizon x
    HashMap<String, HashMap<String, HashMap<String, Boolean>>> pool = new HashMap<>();
    HashMap<String, HashMap<String, Ship>> ships = new HashMap<>();

    Pool() {
        HashMap<String, Boolean> map_string = new HashMap<>();
        for (String tag : tag_nums) {
            map_string.put(tag, false);
        }
        for (String tag : tag_abc) {
            HashMap<String, HashMap<String, Boolean>> nestedMap = new HashMap<>();
            nestedMap.put("locations", new HashMap<>(map_string));
            nestedMap.put("shoot", new HashMap<>(map_string));
            pool.put(tag, nestedMap);
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
            System.out.println("___prepare_ship: t_ship:" + t_ship);
            for (int s_ship = 0; s_ship < t_ship.getKey(); s_ship++) {
                System.out.println("___prepare_ship: s_ship:" + s_ship);
                System.out.println("___prepare_ship: t_ship.key:" + t_ship.getKey());
                create_ship(t_ship.getValue());
            }
        }
    }

    public void create_ship(int ship_size) {
        Random random = new Random();
        HashMap<String, List<String>> cells;
        boolean isHorizontal = random.nextBoolean();
        if (isHorizontal) {
            cells = check_cell_h(ship_size);
            Ship a = new Ship(cells, this);
        } else {
            cells = check_cell_v(ship_size);
            Ship a = new Ship(cells, this);
        }
        System.out.println("___create_ship: " + cells);
        block_cells(cells);
    }

    public HashMap<String, List<String>> check_cell_v(int ship_size) {
        for (String tag_n : tag_nums) {
            HashMap<String, List<String>> s_ship = new HashMap<>();
            List<String> cell = new ArrayList<>();

            for (int i = 0; i < 10 - ship_size; i++) {
                for (int n = 0; n < ship_size; n++) {
                    if (!pool.get(tag_abc[i + n]).get("locations").get(tag_n)) {
                        s_ship.put(tag_abc[i + n], Collections.singletonList(tag_n));
                        if (s_ship.size() == ship_size) {
                            return s_ship;
                        }
                    }
                }

            }
        }
        System.out.println("Use another orient");
        return check_cell_h(ship_size);
    }

    public HashMap<String, List<String>> check_cell_h(int ship_size) {
        for (Map.Entry<String, HashMap<String, HashMap<String, Boolean>>> tag : pool.entrySet()) {
            HashMap<String, List<String>> s_ship = new HashMap<>();
            List<String> cell = new ArrayList<>();

            for (int i = 0; i < 10 - ship_size; i++) {
                System.out.println("___check_cell_v: i = " + i);
                for (int n = 0; n < ship_size; n++) {
                    System.out.println("___check_cell_v: n = " + n);
                    System.out.println("___check_cell_v: " + tag.getValue().get("locations").get(tag_nums[i + n]));
                    if (!tag.getValue().get("locations").get(tag_nums[i + n])) {
                        System.out.println(tag.getKey() + tag_nums[i + n]);
                        cell.add(tag_nums[i + n]);
                        System.out.println("___check_cell_v: " + cell.size() + " to " + ship_size);
                        if (cell.size() == ship_size) {
                            s_ship.put(tag.getKey(), cell);
                            System.out.println("___check_cell_v: return");
                            return s_ship;
                        }
                    } else {
                        s_ship = new HashMap<>();
                        cell = new ArrayList<>();
                    }
                }
            }
        }
        System.out.println("Use another orient");
        return check_cell_v(ship_size);
    }

    public void block_cells(HashMap<String, List<String>> cells) {
        List<String> tag_a = new ArrayList<>();
        List<String> tag_n = new ArrayList<>();

        for (Map.Entry<String, List<String>> c : cells.entrySet()) {
            if (!tag_a.contains(c.getKey())) {
                tag_a.add(c.getKey());
            }
            for (String cc : c.getValue()) {
                if (!tag_n.contains(cc)) {
                    tag_n.add(cc);
                }
            }
        }
        List<String> ch_dist_a = ch_dist(tag_a, tag_abc);
        List<String> ch_dist_n = ch_dist(tag_n, tag_nums);
        for (String tag : ch_dist_a) {
            for (String num : ch_dist_n) {
                this.pool.get(tag).get("locations").put(num, true);
            }
        }
    }

    public List<String> ch_dist(List<String> list, String[] tags) {
        List<String> rd = new ArrayList<>();
        int n = List.of(tags).indexOf(list.getFirst());
        int nl = List.of(tags).indexOf(list.getLast());

        if (list.size() == 1) {
            if (n > 1 && n < (tags.length) - 1) {
                rd.add(tags[n - 1]);
                rd.add(tags[n]);
                rd.add(tags[n + 1]);
            } else if (n > 1) {
                rd.add(tags[n - 1]);
                rd.add(tags[n]);
            } else if (n < (tags.length) - 1) {
                rd.add(tags[n]);
                rd.add(tags[n + 1]);
            }
        } else if (n > 1 && n < (tags.length) - 1) {
            rd.add(tags[n - 1]);
            rd.addAll(list);
            rd.add(tags[nl + 1]);
        } else if (n > 1) {
            rd.add(tags[n - 1]);
            rd.addAll(list);
        } else if (n < (tags.length) - 1) {
            rd.addAll(list);
            rd.add(tags[nl + 1]);
        }
        return rd;
    }
}

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.InputStream;
import java.util.*;

public class Game {
    ArrayList<Account> accountList = new ArrayList<>();
    HashMap<Cell.CellType, ArrayList<String>> cellTypeAndStory = new HashMap<>();

    public static Game gameInstance;

    private Game() {}

    public static Game getInstance() {                   //imi creez o metoda in care voi apela constructorul privat
        if (gameInstance == null) {                     //conform Singleton
            gameInstance = new Game();
        }
        return gameInstance;
    }

    void run() throws JSONException {                   //porneste un joc nou, jucatorul va alege contul si personajul
        String resourceAccounts = "/accounts.json";
        InputStream is = Game.class.getResourceAsStream(resourceAccounts);      //incep parsarea Json-ului pentru account-uri

        if (is == null) {
            throw new NullPointerException("Cannot find resource file " + resourceAccounts);
        }

        JSONTokener tokenerAccounts = new JSONTokener(is);
        JSONObject objAccounts = new JSONObject(tokenerAccounts);
        JSONArray arrayAccounts = objAccounts.getJSONArray("accounts");

        for (int i = 0; i < arrayAccounts.length(); i++) {
            JSONObject credentials;
            credentials = arrayAccounts.getJSONObject(i).optJSONObject("credentials");

            Credentials credentials1 = new Credentials();
            credentials1.setEmail(credentials.getString("email"));
            credentials1.setPassword(credentials.getString("password"));

            Collection<String> favGames = new ArrayList<>();
            JSONArray fav = arrayAccounts.getJSONObject(i).getJSONArray("favorite_games");

            for (int j = 0; j < fav.length(); j++) {
                favGames.add(fav.getString(j));
            }

            String playerName = arrayAccounts.getJSONObject(i).getString("name");
            String playerCountry = arrayAccounts.getJSONObject(i).getString("country");
            int NOGames = arrayAccounts.getJSONObject(i).getInt("maps_completed");

            JSONArray character = (JSONArray) arrayAccounts.getJSONObject(i).get("characters");
            ArrayList<Character> chList = new ArrayList<>();
            for (int k = 0; k < character.length(); k++) {
                if (character.getJSONObject(k).get("profession").equals("Mage")) {
                    chList.add(new Mage(new Inventory(2, 50, new ArrayList<>()), Integer.parseInt(character.getJSONObject(k).get("level").toString()), Integer.parseInt(character.getJSONObject(k).get("experience").toString())));
                } else if (character.getJSONObject(k).get("profession").equals("Warrior")) {
                    chList.add(new Warrior(new Inventory(6, 100, new ArrayList<>()), Integer.parseInt(character.getJSONObject(k).get("level").toString()), Integer.parseInt(character.getJSONObject(k).get("experience").toString())));
                } else if (character.getJSONObject(k).get("profession").equals("Rogue")) {
                    chList.add(new Rogue(new Inventory(4, 70, new ArrayList<>()), Integer.parseInt(character.getJSONObject(k).get("level").toString()), Integer.parseInt(character.getJSONObject(k).get("experience").toString())));
                } else
                    chList.add(null);
            }

            Account accountElement = new Account(new Account.Information(credentials1, favGames, playerName, playerCountry), chList, NOGames);
            accountList.add(accountElement);
            //mi-am creat lista de conturi
        }

        String resourceStories = "/stories.json";                           //incep parsarea Json-ului pentru stories
        InputStream ok = Game.class.getResourceAsStream(resourceStories);

        if (ok == null) {
            throw new NullPointerException("Cannot find resource file " + resourceStories);
        }

        JSONTokener tokenerStories = new JSONTokener(ok);
        JSONObject objStories = new JSONObject(tokenerStories);
        JSONArray arrayStories = objStories.getJSONArray("stories");

        ArrayList<String> emptyList = new ArrayList<>();
        ArrayList<String> shopList = new ArrayList<>();
        ArrayList<String> enemyList = new ArrayList<>();
        ArrayList<String> finishList = new ArrayList<>();

        cellTypeAndStory.put(Cell.CellType.EMPTY, emptyList);
        cellTypeAndStory.put(Cell.CellType.ENEMY, enemyList);
        cellTypeAndStory.put(Cell.CellType.FINISH, finishList);
        cellTypeAndStory.put(Cell.CellType.SHOP, shopList);

        //adaug in dictionar pentru fiecare tip de celula, o lista de povesti
        for (int i = 0; i < arrayStories.length(); i++) {
            if (arrayStories.getJSONObject(i).get("type").equals("EMPTY")) {
                cellTypeAndStory.get(Cell.CellType.EMPTY).add(arrayStories.getJSONObject(i).getString("value"));
            } else if (arrayStories.getJSONObject(i).get("type").equals("ENEMY")) {
                cellTypeAndStory.get(Cell.CellType.ENEMY).add(arrayStories.getJSONObject(i).getString("value"));
            } else if (arrayStories.getJSONObject(i).get("type").equals("SHOP")) {
                cellTypeAndStory.get(Cell.CellType.SHOP).add(arrayStories.getJSONObject(i).getString("value"));
            } else
                cellTypeAndStory.get(Cell.CellType.FINISH).add(arrayStories.getJSONObject(i).getString("value"));
        }

        //citesc de la tastatura modul in care doresc sa se desfasoare jocul
        Scanner terminal = new Scanner(System.in);
        System.out.println("Choose what game mode do you want(terminal/graphic)");
        String mode = terminal.nextLine();

        if (mode.equals("terminal")) {
            Account chosenAccount = null;
            boolean check1 = false;
            while (!check1) {
                System.out.println("Enter your credentials");
                System.out.println("Email: ");
                Scanner email = new Scanner(System.in);
                String id = email.nextLine();

                System.out.println("Password: ");
                Scanner password = new Scanner(System.in);
                String userPassword = password.nextLine();

                //verific daca informatiile introduse corespund unui cont din lista de conturi
                //daca nu sunt corecte cer o noua introducere
                for (Account account : accountList) {
                    if (account.informationPlayer.credentials.getEmail().equals(id) && account.informationPlayer.credentials.getPassword().equals(userPassword)) {
                        chosenAccount = account;
                        check1 = true;
                        break;
                    }
                }
                if (!check1) {
                    System.out.println("Inorrect email or password, try again");
                }
            }

            System.out.println(chosenAccount.informationPlayer.name);

            Character chosenCharacter = null;
            System.out.println("Character list for your account is: ");
            //afisez lista de caractere aferente contului astfel incat jucatorul sa aleaga unul
            for (int i = 0; i < chosenAccount.characters.size(); i++) {
                System.out.println("Character  " + (i + 1) + "  " + chosenAccount.characters.get(i).characterName);
            }

            System.out.println("Choose one character, enter character's name!");

            int ok1 = 0;

            //citesc de la tastatura personajul introdus de player
            //daca numele este incorect trebuie sa introduca iar
            while (ok1 == 0) {
                Scanner character = new Scanner(System.in);
                String chosenCharacterInput = character.nextLine();
                for (Character ch : chosenAccount.characters) {
                    if (ch.characterName.equals(chosenCharacterInput)) {
                        chosenCharacter = ch;
                        ok1 = 1;
                    }
                }
                System.out.println("Please enter again the character name");
            }


            System.out.println(chosenCharacter.characterName + "'s details for this game is: \n" + "Coins: " +
                    chosenCharacter.potions.numberOfCoins + "\nCurrent life: " + chosenCharacter.currentLife + "\nCurrent mana: " +
                    chosenCharacter.currentMana + "\nCurrent level: " + chosenCharacter.currentLevel + "\nCurrent experience: " + chosenCharacter.currentExperience);

            System.out.println("Let's start the game");
            Grid map = Grid.getGrid();                      //imi creez mapa de joc

            chosenCharacter.coordonateX = map.currentCell.coordonateOx;         //sincronizez coordonatele personajului cu cele
            chosenCharacter.coordonateY = map.currentCell.coordonateOy;             //ale celulei curente

            map.getMap(5, 5, true);
            map.printMap(chosenCharacter);
            map.map[map.currentCell.coordonateOx].get(map.currentCell.coordonateOy).isChecked = true;     //vizez fiecare celula pe care este personajul
            getOptionList(map.currentCell, map, chosenCharacter);                      //obtin lista de optiuni pentru celula

            System.out.println(chosenCharacter.characterName + "'s detail for this game is: \n" + "Coins: " +
                    chosenCharacter.potions.numberOfCoins + "\nCurrent life: " + chosenCharacter.currentLife + "\nCurrent mana: " +
                    chosenCharacter.currentMana + "\nCurrent level: " + chosenCharacter.currentLevel + "\nCurrent experience: " + chosenCharacter.currentExperience);

            if (chosenCharacter.currentLife > 0) {
                System.out.println("Enter your next move");         //daca viata personajului este mai mare decat 0 citesc urmatoarea sa miscare
                terminal = new Scanner(System.in);
            }

            //incep parcurgerea hartii prin diferite miscari
            do {
                String move = terminal.nextLine();
                if (move.equals("go south")) {
                    map.goSouth();
                    chosenCharacter.coordonateX = map.currentCell.coordonateOx;
                    chosenCharacter.coordonateY = map.currentCell.coordonateOy;
                    chosenCharacter.potions.numberOfCoins += 5;
                    map.printMap(chosenCharacter);
                } else if (move.equals("go west")) {
                    map.goWest();
                    chosenCharacter.coordonateX = map.currentCell.coordonateOx;
                    chosenCharacter.coordonateY = map.currentCell.coordonateOy;
                    chosenCharacter.potions.numberOfCoins += 5;
                    map.printMap(chosenCharacter);
                } else if (move.equals("go north")) {
                    map.goNorth();
                    chosenCharacter.coordonateX = map.currentCell.coordonateOx;
                    chosenCharacter.coordonateY = map.currentCell.coordonateOy;
                    chosenCharacter.potions.numberOfCoins += 5;
                    map.printMap(chosenCharacter);
                } else if (move.equals("go east")) {
                    map.goEast();
                    chosenCharacter.coordonateX = map.currentCell.coordonateOx;
                    chosenCharacter.coordonateY = map.currentCell.coordonateOy;
                    chosenCharacter.potions.numberOfCoins += 5;
                    map.printMap(chosenCharacter);
                }

                //daca ajung pe celula de finish jocul s-a terminat
                if (map.map[map.currentCell.coordonateOx].get(map.currentCell.coordonateOy).type.equals(Cell.CellType.FINISH) || chosenCharacter.currentLife < 0) {
                    chosenCharacter.currentLevel += 1;
                    System.out.println(chosenCharacter.characterName + "'s details for this game is: \n" + "Coins: " +
                            chosenCharacter.potions.numberOfCoins + "\nCurrent life: " + chosenCharacter.currentLife + "\nCurrent mana: " +
                            chosenCharacter.currentMana + "\nCurrent level: " + chosenCharacter.currentLevel + "\nCurrent experience: " + chosenCharacter.currentExperience);
                    System.out.println("You finished the game");
                    break;
                }

                //pentru fiecare celula obtin povestea
                getStory(map.map[map.currentCell.coordonateOx].get(map.currentCell.coordonateOy), map);
                getOptionList(map.currentCell, map, chosenCharacter);

                //vizez fiecare celula pe care am fost
                map.map[map.currentCell.coordonateOx].get(map.currentCell.coordonateOy).isChecked = true;

                System.out.println(chosenCharacter.characterName + "'s details for this game is: \n" + "Coins: " +
                        chosenCharacter.potions.numberOfCoins + "\nCurrent life: " + chosenCharacter.currentLife + "\nCurrent mana: " +
                        chosenCharacter.currentMana + "\nCurrent level: " + chosenCharacter.currentLevel + "\nCurrent experience: " + chosenCharacter.currentExperience);

                if (chosenCharacter.currentLife > 0) {
                    System.out.println("\nEnter your next move");
                    terminal = new Scanner(System.in);
                } else
                    System.out.println("Please press ENTER to exit the game");

            } while (terminal.hasNextLine() && chosenCharacter.currentLife > 0);

            //Implementare interfata grafica


        } else if (mode.equals("graphic")) {
            //creez frame-ul de autentificare
            JFrame frame = new JFrame();
            JLabel usernamelabel = new JLabel("USERNAME");
            JLabel passwordLabel = new JLabel("PASSWORD");
            JLabel correctLabel = new JLabel();
            JTextField userTextField = new JTextField(30);
            JPasswordField passwordField = new JPasswordField(30);
            JButton loginButton = new JButton("LOGIN");
            Checkbox showPasswordButton = new Checkbox("Show password");
            JPanel userPanel = new JPanel();

            correctLabel.setForeground(Color.red);
            correctLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
            passwordField.setEchoChar('*');

            //adaug toate labelurile, fieldurile si butoanele in panel
            userPanel.add(usernamelabel);
            userPanel.add(userTextField);
            userPanel.add(passwordLabel);
            userPanel.add(passwordField);
            userPanel.add(loginButton);
            userPanel.add(showPasswordButton);
            userPanel.add(correctLabel);
            userPanel.setBackground(Color.YELLOW);
            userPanel.setVisible(true);

            //fac setarile frame-ului si adaug panelul
            frame.setTitle("Authentication");
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setBounds(0, 0, 400, 600);
            frame.setBackground(Color.YELLOW);
            frame.add(userPanel, BorderLayout.CENTER);
            frame.setVisible(true);

            //implementez functionalitatea pentru butonul de show password
            showPasswordButton.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (showPasswordButton.getState()) {
                        passwordField.setEchoChar((char) 0);
                    } else
                        passwordField.setEchoChar('*');
                }
            });

            //implementez functionalitatile pentru butonul de login
            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (Account account : accountList) {
                        Account characterAccount;
                        //verific daca datele introduse sunt corecte
                        if (userTextField.getText().equals(account.informationPlayer.credentials.getEmail()) && account.informationPlayer.credentials.getPassword().equals(passwordField.getText())) {
                            //daca sunt datele corecte contul va deveni cel aferent email ului si parolei
                            characterAccount = account;

                            JPanel characterPanel = new JPanel();
                            JLabel messageLabel = new JLabel("Choose your character from the list");
                            DefaultListModel<String> characterList = new DefaultListModel<>();
                            JScrollPane scrollPane;
                            JButton characterButton = new JButton("Get character");

                            messageLabel.setFont(new Font("Verdana", Font.PLAIN, 18));

                            //resetez frame-ul pentru a adauga noile elemente
                            frame.getContentPane().removeAll();
                            frame.repaint();
                            frame.setVisible(true);
                            frame.setResizable(false);
                            frame.setMaximizedBounds(new Rectangle());
                            frame.setTitle("Choose character");

                            GridLayout layout = new GridLayout(3, 0);
                            frame.setLayout(layout);

                            //incep sa creez un scrollPane cu lista de caractere
                            for (int i = 0; i < characterAccount.characters.size(); i++) {
                                characterList.add(i, characterAccount.characters.get(i).characterName);
                            }

                            JList<String> list = new JList<>(characterList);
                            characterPanel.add(list);
                            scrollPane = new JScrollPane(list);
                            characterPanel.add(scrollPane);
                            characterPanel.setVisible(true);

                            frame.add(messageLabel);
                            frame.add(characterPanel);
                            frame.add(characterButton);
                            frame.setVisible(true);

                            //implementez functionalitatea pentru
                            characterButton.addActionListener(new ActionListener() {
                                Character chosenCharacter = null;

                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    //iau datele din Json aferente contului si fiecarui caracter din el pentru a putea creea personajul
                                    //in functie de alegerea jucatorului
                                    for (int i = 0; i < arrayAccounts.length(); i++) {
                                        JSONArray character = null;
                                        try {
                                            character = (JSONArray) arrayAccounts.getJSONObject(i).get("characters");
                                        } catch (JSONException ex) {
                                            ex.printStackTrace();
                                        }

                                        try {
                                            if (arrayAccounts.getJSONObject(i).get("name").equals(characterAccount.informationPlayer.name)) {
                                                for (int k = 0; k < characterAccount.characters.size(); k++) {
                                                    if (list.getSelectedValue().equals("Mage")) {
                                                        try {
                                                            if (character != null && character.getJSONObject(k).get("profession").equals("Mage")) {
                                                                chosenCharacter = new Mage(new Inventory(2, 50, new ArrayList<>()),
                                                                        Integer.parseInt(character.getJSONObject(k).get("level").toString()),
                                                                        Integer.parseInt(character.getJSONObject(k).get("experience").toString()));
                                                            }
                                                        } catch (JSONException ex) {
                                                            ex.printStackTrace();
                                                        }
                                                    } else if (list.getSelectedValue().equals("Rogue")) {
                                                        try {
                                                            if (character != null && character.getJSONObject(k).get("profession").equals("Rogue")) {
                                                                chosenCharacter = new Rogue(new Inventory(4, 70, new ArrayList<>()),
                                                                        Integer.parseInt(character.getJSONObject(k).get("level").toString()),
                                                                        Integer.parseInt(character.getJSONObject(k).get("experience").toString()));
                                                            }
                                                        } catch (JSONException ex) {
                                                            ex.printStackTrace();
                                                        }
                                                    } else if (list.getSelectedValue().equals("Warrior")) {
                                                        try {
                                                            if (character != null && character.getJSONObject(k).get("profession").equals("Warrior")) {
                                                                chosenCharacter = new Warrior(new Inventory(6, 100, new ArrayList<>()), Integer.parseInt(character.getJSONObject(k).get("level").toString()), Integer.parseInt(character.getJSONObject(k).get("experience").toString()));
                                                            }
                                                        } catch (JSONException ex) {
                                                            ex.printStackTrace();
                                                        }
                                                    }
                                                }
                                            }
                                        } catch (JSONException ex) {
                                            ex.printStackTrace();
                                        }
                                    }

                                    //resetez frame-ul pentru a realiza pagina de joc
                                    frame.getContentPane().removeAll();
                                    frame.repaint();
                                    frame.setVisible(true);
                                    frame.setResizable(false);
                                    frame.setLayout(new GridLayout(4, 0));
                                    frame.setBounds(0, 0, 1000, 850);
                                    frame.setVisible(false);
                                    frame.setTitle("World of Marcel");
                                    Grid map = Grid.getGrid();
                                    GridLayout layoutGame = new GridLayout(map.latime, map.lungime);
                                    map.getMap(map.latime, map.lungime, true);                  //obtin map-ul curent
                                    map.currentCell.isChecked = true;                                   //vizez celula pe care sunt

                                    JPanel detailsPanel = new JPanel();
                                    detailsPanel.setLayout(new GridLayout(5, 0));

                                    JLabel label2 = new JLabel(chosenCharacter.characterName + "'s " + "current details:");
                                    label2.setFont(new Font("Verdana", Font.PLAIN, 16));

                                    //creez un panel destinat detalilor despre personaj
                                    detailsPanel.setBackground(Color.YELLOW);
                                    detailsPanel.add(label2, BorderLayout.EAST);
                                    detailsPanel.add(new Label("Name: " + chosenCharacter.characterName), BorderLayout.EAST);
                                    detailsPanel.add(new Label("Coins: " + chosenCharacter.potions.numberOfCoins), BorderLayout.EAST);
                                    detailsPanel.add(new Label("Current weight: " + chosenCharacter.potions.getCurrentWeight()), BorderLayout.EAST);
                                    detailsPanel.add(new JLabel("Current level: " + chosenCharacter.currentLevel));
                                    detailsPanel.add(new Label("Current life: " + chosenCharacter.currentLife));
                                    detailsPanel.add(new Label("Current experience: " + chosenCharacter.currentExperience));

                                    //creez un panel pentru mapa de joc
                                    JPanel boardPanel = new JPanel();
                                    boardPanel.setLayout(layoutGame);
                                    getNewBoard(chosenCharacter, boardPanel, map);

                                    //creez un panel destinat butoanelor pentru mutari si listei de optiuni pentru fiecare celula
                                    JPanel movesPanel = new JPanel();
                                    movesPanel.setSize(new Dimension(50, 50));
                                    movesPanel.setSize(50, 50);

                                    JButton goSouth = new JButton("Go south");
                                    goSouth.setSize(50, 10);

                                    JButton goWest = new JButton("Go west");
                                    goWest.setSize(15, 30);

                                    JButton goNorth = new JButton("Go north");
                                    goNorth.setSize(50, 10);

                                    JButton goEast = new JButton("Go east");
                                    goEast.setSize(15, 20);

                                    JLabel movesLabel = new JLabel("Choose your move", JLabel.CENTER);
                                    movesLabel.setFont(new Font("Verdana", Font.BOLD, 18));
                                    movesPanel.setLayout(new BorderLayout());
                                    movesPanel.add(goSouth, BorderLayout.SOUTH);
                                    movesPanel.add(goNorth, BorderLayout.NORTH);
                                    movesPanel.add(goEast, BorderLayout.EAST);
                                    movesPanel.add(goWest, BorderLayout.WEST);
                                    movesPanel.add(movesLabel);

                                    JPanel optionsPanel = new JPanel();
                                    optionsPanel.setLayout(new GridLayout(6, 0));
                                    optionsPanel.setBackground(Color.YELLOW);

                                    frame.add(detailsPanel);
                                    frame.add(boardPanel);
                                    frame.add(movesPanel);
                                    frame.setVisible(true);
                                    frame.add(optionsPanel);

                                    getOptionPanel(map, frame, optionsPanel, detailsPanel, chosenCharacter);
                                    map.map[map.currentCell.coordonateOx].get(map.currentCell.coordonateOy).isChecked = true;

                                    //implementez functionalitatile pentru toate butoanele de moves
                                    //pentru fiecare obtin o lista de optiuni, actualizez mapa conform miscarilor
                                    //si detaliile curente ale personajului
                                    goSouth.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            map.goSouth();
                                            boardPanel.removeAll();
                                            boardPanel.repaint();
                                            getNewBoard(chosenCharacter, boardPanel, map);
                                            boardPanel.setVisible(true);
                                            frame.setVisible(true);
                                            getOptionPanel(map, frame, optionsPanel, detailsPanel, chosenCharacter);
                                            getDetailsPanel(frame, detailsPanel, chosenCharacter);
                                            map.map[map.currentCell.coordonateOx].get(map.currentCell.coordonateOy).isChecked = true;
                                        }
                                    });

                                    goNorth.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            map.goNorth();
                                            boardPanel.removeAll();
                                            boardPanel.repaint();
                                            getNewBoard(chosenCharacter, boardPanel, map);
                                            boardPanel.setVisible(true);
                                            frame.setVisible(true);
                                            getOptionPanel(map, frame, optionsPanel, detailsPanel, chosenCharacter);
                                            getDetailsPanel(frame, detailsPanel, chosenCharacter);
                                            map.map[map.currentCell.coordonateOx].get(map.currentCell.coordonateOy).isChecked = true;
                                        }
                                    });

                                    goEast.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            map.goEast();
                                            boardPanel.removeAll();
                                            boardPanel.repaint();
                                            getNewBoard(chosenCharacter, boardPanel, map);
                                            boardPanel.setVisible(true);
                                            frame.setVisible(true);
                                            getOptionPanel(map, frame, optionsPanel, detailsPanel, chosenCharacter);
                                            getDetailsPanel(frame, detailsPanel, chosenCharacter);
                                            map.map[map.currentCell.coordonateOx].get(map.currentCell.coordonateOy).isChecked = true;
                                        }
                                    });

                                    goWest.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            map.goWest();
                                            boardPanel.removeAll();
                                            boardPanel.repaint();
                                            getNewBoard(chosenCharacter, boardPanel, map);
                                            boardPanel.setVisible(true);
                                            frame.setVisible(true);
                                            getOptionPanel(map, frame, optionsPanel, detailsPanel, chosenCharacter);
                                            getDetailsPanel(frame, detailsPanel, chosenCharacter);
                                            map.map[map.currentCell.coordonateOx].get(map.currentCell.coordonateOy).isChecked = true;
                                        }
                                    });
                                }
                            });
                        } else {
                            //daca emailil si parola sunt gresite va aparea mesajul aferent iar playerul
                            //trebuie sa reintroduca datele
                            usernamelabel.setForeground(Color.red);
                            passwordLabel.setForeground(Color.red);
                            correctLabel.setText("Email/password isn't correct, try again!");
                        }
                    }
                }
            });
        }
    }

    //creez o metoda pentru obtinerea mapei noi in urma miscarilor
    void getNewBoard(Character chosenCharacter, JPanel panel, Grid map) {
        for (int i = 0; i < map.latime; i++) {
            for (int j = 0; j < map.lungime; j++) {
                if (map.currentCell.coordonateOx == i && map.currentCell.coordonateOy == j) {
                    JLabel label = new JLabel(chosenCharacter.characterName);
                    Border border = BorderFactory.createLineBorder(Color.BLACK, 5);

                    label.setForeground(Color.red);
                    label.setBorder(border);
                    label.setVisible(true);
                    panel.add(label, BorderLayout.CENTER);
                } else {
                    JLabel label = new JLabel();
                    Border border = BorderFactory.createLineBorder(Color.BLACK, 5);

                    label.setText(map.map[i].get(j).type.name());
                    label.setBorder(border);
                    panel.add(label);
                }
            }
        }
    }

    //creez o metoda pentru panelul de optiuni
    void getOptionPanel(Grid map, JFrame frame, JPanel optionsPanel, JPanel detailsPanel, Character chosenCharacter) {
        //primul caz este cel al celulei shop
        if (map.map[map.currentCell.coordonateOx].get(map.currentCell.coordonateOy).type.equals(Cell.CellType.SHOP)) {
            optionsPanel.removeAll();
            optionsPanel.invalidate();
            optionsPanel.repaint();
            optionsPanel.setBackground(Color.YELLOW);

            //afisez o poveste pentru celula daca nu a fost vizata
            if (!map.map[map.currentCell.coordonateOx].get(map.currentCell.coordonateOy).isChecked) {
                optionsPanel.add(new JLabel("A story for this place: " + cellTypeAndStory.get(Cell.CellType.SHOP).get(new Random().nextInt(cellTypeAndStory.get(Cell.CellType.SHOP).size()))));
            }

            optionsPanel.add(new JLabel("You can buy the potions from the above list:"));

            //creez doua butoane pentru potiunile disponibile
            JButton manaPotion = new JButton("1. Mana Potion");
            JButton healthPotion = new JButton("2. Health Potion");

            //daca apas butonul mana potion voi cumpara potiunea aferenta, la fel si pentru
            //butonul de health potion
            manaPotion.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    chosenCharacter.buyPotion(new ManaPotion(), chosenCharacter.potions);
                    getDetailsPanel(frame, detailsPanel, chosenCharacter);
                    if (new ManaPotion().getPrice() > chosenCharacter.potions.numberOfCoins) {
                        optionsPanel.add(new Label("You don't have money :("));
                    }
                }
            });

            healthPotion.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    chosenCharacter.buyPotion(new HealthPotion(), chosenCharacter.potions);
                    getDetailsPanel(frame, detailsPanel, chosenCharacter);
                    if (new HealthPotion().getPrice() > chosenCharacter.potions.numberOfCoins) {
                        optionsPanel.add(new Label("You don't have money :("));
                    }
                }
            });

            optionsPanel.add(manaPotion);
            optionsPanel.add(healthPotion);
            frame.add(optionsPanel);
            optionsPanel.setVisible(true);
            frame.setVisible(true);
            //al doilea caz este pentru celula enemy
        } else if (map.map[map.currentCell.coordonateOx].get(map.currentCell.coordonateOy).type.equals(Cell.CellType.ENEMY)) {
            optionsPanel.removeAll();
            optionsPanel.invalidate();
            optionsPanel.repaint();
            optionsPanel.setBackground(Color.YELLOW);

            if (!map.map[map.currentCell.coordonateOx].get(map.currentCell.coordonateOy).isChecked) {
                optionsPanel.add(new JLabel("A story for this place: " + cellTypeAndStory.get(Cell.CellType.ENEMY).get(new Random().nextInt(cellTypeAndStory.get(Cell.CellType.ENEMY).size()))));
            }

            optionsPanel.add(new JLabel("Choose how you'll fight with the enemy:"), BorderLayout.NORTH);

            //creez butoane pentru fiecare optiune si implementez functionalitatile
            JButton attackButton = new JButton("1. Attack Enemy");
            JButton usePotionButton = new JButton("2. Use Potion");
            JButton useAbilityButton = new JButton("3. Use Ability");

            attackButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Enemy enemy = new Enemy(new Random().nextInt(70, 101), new Random().nextInt(20, 41));            //de revizuit
                    enemy.receiveDamage(chosenCharacter.getDamage());
                    chosenCharacter.receiveDamage(enemy.getDamage());
                    getDetailsPanel(frame, detailsPanel, chosenCharacter);
                }
            });

            usePotionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!chosenCharacter.potions.potionList.isEmpty()) {
                        //in acest caz introduc inca doua butoane cu potiunile din lista de potiuni
                        //pentru a alege pe care sa le foloseasca
                        JButton healthButton = new JButton("Health Potion");
                        JButton manaButton = new JButton("Mana Potion");

                        healthButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                for (int i = 0; i < chosenCharacter.potions.potionList.size(); i++) {
                                    if (chosenCharacter.potions.potionList.get(i).getString().equals("Health Potion")) {
                                        new HealthPotion().usePotion(chosenCharacter);
                                        chosenCharacter.potions.deletePotion(chosenCharacter.potions.potionList.get(i));
                                    }
                                }
                                getDetailsPanel(frame, detailsPanel, chosenCharacter);
                                System.out.println(chosenCharacter.currentLife);
                            }
                        });

                        manaButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                for (int i = 0; i < chosenCharacter.potions.potionList.size(); i++) {
                                    if (chosenCharacter.potions.potionList.get(i).getString().equals("Mana Potion")) {
                                        new ManaPotion().usePotion(chosenCharacter);
                                        chosenCharacter.potions.deletePotion(chosenCharacter.potions.potionList.get(i));
                                    }
                                }
                                getDetailsPanel(frame, detailsPanel, chosenCharacter);
                            }
                        });

                        optionsPanel.add(healthButton);
                        optionsPanel.add(manaButton);
                        optionsPanel.setVisible(true);
                        frame.setVisible(true);
                    } else {
                        optionsPanel.add(new Label("You don't have potions :("));
                        optionsPanel.setVisible(true);
                        frame.setVisible(true);
                    }
                }
            });

            optionsPanel.add(attackButton, BorderLayout.CENTER);
            optionsPanel.add(usePotionButton, BorderLayout.SOUTH);
            optionsPanel.add(useAbilityButton, BorderLayout.SOUTH);
            frame.add(optionsPanel);
            optionsPanel.setVisible(true);
            frame.setVisible(true);
            //al reilea caz este cel al celulei empty
        } else if (map.map[map.currentCell.coordonateOx].get(map.currentCell.coordonateOy).type.equals(Cell.CellType.EMPTY)) {
            optionsPanel.removeAll();
            optionsPanel.invalidate();
            optionsPanel.repaint();
            optionsPanel.setBackground(Color.YELLOW);

            if (!map.map[map.currentCell.coordonateOx].get(map.currentCell.coordonateOy).isChecked) {
                optionsPanel.add(new JLabel("A story for this place: " + cellTypeAndStory.get(Cell.CellType.EMPTY).get(new Random().nextInt(cellTypeAndStory.get(Cell.CellType.EMPTY).size()))));
            }

            frame.setVisible(true);

            //cel de al patrulea caz este cel al celulei finish
        } else if (map.map[map.currentCell.coordonateOx].get(map.currentCell.coordonateOy).type.equals(Cell.CellType.FINISH)) {
            optionsPanel.removeAll();
            optionsPanel.invalidate();
            optionsPanel.repaint();
            optionsPanel.setBackground(Color.YELLOW);

            if (!map.map[map.currentCell.coordonateOx].get(map.currentCell.coordonateOy).isChecked) {
                optionsPanel.add(new JLabel("A story for this place: " + cellTypeAndStory.get(Cell.CellType.FINISH).get(new Random().nextInt(cellTypeAndStory.get(Cell.CellType.FINISH).size()))));
            }

            optionsPanel.add(new JLabel("You finished the game :)"));
            frame.setVisible(true);
        }
    }

    //creez o metoda pentru a obtine panelul de detalii curente al caracterului
    void getDetailsPanel(JFrame frame, JPanel detailsPanel, Character chosenCharacter) {
        detailsPanel.removeAll();
        detailsPanel.invalidate();
        detailsPanel.repaint();

        JLabel label2 = new JLabel(chosenCharacter.characterName + "'s " + "current details:");
        label2.setFont(new Font("Verdana", Font.PLAIN, 16));

        detailsPanel.setBackground(Color.YELLOW);
        detailsPanel.add(label2, BorderLayout.EAST);
        detailsPanel.add(new Label("Name: " + chosenCharacter.characterName), BorderLayout.EAST);
        detailsPanel.add(new Label("Coins: " + chosenCharacter.potions.numberOfCoins), BorderLayout.EAST);
        detailsPanel.add(new Label("Current weight: " + chosenCharacter.potions.getCurrentWeight()), BorderLayout.EAST);
        detailsPanel.add(new Label("Current level: " + chosenCharacter.currentLevel));
        detailsPanel.add(new Label("Current life: " + chosenCharacter.currentLife));
        detailsPanel.add(new Label("Current experience: " + chosenCharacter.currentExperience));
        detailsPanel.add(new Label("Current mana: " + chosenCharacter.currentMana));
        detailsPanel.setVisible(true);
        frame.setVisible(true);
    }

    void getOptionList(Cell currentCell, Grid map, Character chosenCharacter) {                 //afiseaza lista de optiuni pt casuta
        //afisez optiunile in fuctie de tipul celulei
        if (map.map[currentCell.coordonateOx].get(currentCell.coordonateOy).type.equals(Cell.CellType.SHOP)) {
            System.out.println("You can buy the potions from the above list:");
            System.out.println("1. Mana Potion");
            System.out.println("2. Health potion");

        } else if (map.map[currentCell.coordonateOx].get(currentCell.coordonateOy).type.equals(Cell.CellType.ENEMY)) {
            ArrayList<String> optionList = new ArrayList<>();

            System.out.println("You are going into an enemy, choose your next move to fight with him:");
            optionList.add("Attack enemy");
            optionList.add("Use potion");
            optionList.add("Use ability");
            for (int i = 0; i < optionList.size(); i++) {
                System.out.println((i + 1) + ". " + optionList.get(i));
            }
        }

        if (map.map[currentCell.coordonateOx].get(currentCell.coordonateOy).type.equals(Cell.CellType.SHOP)) {
            //citesc numarul introdus de la tastatura astfel incat sa cumpar potiunea cu indexul respectiv
            System.out.println("If you don't want to buy anything please enter 0 else enter the potion number :)");
            Scanner option = new Scanner(System.in);
            int optionInt = option.nextInt();
            while (optionInt >= 0) {
                if (optionInt > 0) {
                    chosenCharacter.buyPotion(new Shop(new ManaPotion(), new HealthPotion()).getPotion(optionInt - 1), chosenCharacter.potions);
                    System.out.println("Number of coins after buying: " + chosenCharacter.potions.numberOfCoins);
                    System.out.println("If you want to buy one more potion please enter its number else if you don't, please enter 0");
                    Scanner nextOptionInput = new Scanner(System.in);
                    optionInt = nextOptionInput.nextInt();
                } else {
                    System.out.println("You exit the shop");
                    break;
                }
            }
        }

        if (map.map[currentCell.coordonateOx].get(currentCell.coordonateOy).type.equals(Cell.CellType.ENEMY)) {
            Enemy enemy = new Enemy(new Random().nextInt(70, 101), new Random().nextInt(70, 101));
            while (chosenCharacter.currentLife > 0 && enemy.currentLife > 0) {
                //citesc optiunea aleasa de player
                System.out.println("Choose your option");
                Scanner option = new Scanner(System.in);
                String optionString = option.nextLine();

                if (optionString.equals("Attack enemy")) {
                    enemy.receiveDamage(chosenCharacter.getDamage());
                    chosenCharacter.receiveDamage(enemy.getDamage());
                    System.out.println(chosenCharacter.characterName + "'s current life is: " + chosenCharacter.currentLife);
                    System.out.println("Enemy's current life is: " + enemy.currentLife);

                    //verific daca jucatorul sau inamicul mai au viata pentru a vedea daca se termina jocul sau daca am ucis inamicul
                    if (chosenCharacter.currentLife < 0) {
                        System.out.println("You loose the game :(");
                        break;
                    }

                    if (enemy.currentLife <= 0) {
                        map.map[currentCell.coordonateOx].get(currentCell.coordonateOy).type = Cell.CellType.EMPTY;
                        System.out.println("You killed the enemy");
                        chosenCharacter.potions.numberOfCoins += 20;
                        chosenCharacter.currentExperience += 15;
                    }
                }

                if (optionString.equals("Use potion")) {
                    if (!chosenCharacter.potions.potionList.isEmpty()) {
                        for (int i = 0; i < chosenCharacter.potions.potionList.size(); i++) {
                            System.out.println(i + 1 + ". " + chosenCharacter.potions.potionList.get(i).getString());
                        }
                        System.out.println("Please enter the potion number");
                        Scanner potionInput = new Scanner(System.in);
                        int chosenPotionInt = potionInput.nextInt();
                        chosenCharacter.potions.potionList.get(chosenPotionInt - 1).usePotion(chosenCharacter);
                        chosenCharacter.potions.deletePotion(chosenCharacter.potions.potionList.get(chosenPotionInt - 1));
                        System.out.println(chosenCharacter.characterName + "'s current life is: " + chosenCharacter.currentLife);
                        System.out.println(chosenCharacter.characterName + "'s current mana is: " + chosenCharacter.currentMana);
                    } else {
                        System.out.println("You don't have potions :(");
                    }
                }

                if (optionString.equals("Use ability")) {
                    if (chosenCharacter.ice) {
                        chosenCharacter.useAbility(chosenCharacter.ice, enemy, chosenCharacter);
                    } else if (chosenCharacter.fire) {
                        chosenCharacter.useAbility(chosenCharacter.fire, enemy, chosenCharacter);
                    } else if (chosenCharacter.earth) {
                        chosenCharacter.useAbility(chosenCharacter.earth, enemy, chosenCharacter);
                    }

                    System.out.println(chosenCharacter.characterName + "'s mana is: " + chosenCharacter.currentMana);
                    System.out.println("Enemy's mana is: " + enemy.currentMana);
                    System.out.println(chosenCharacter.characterName + "'s current life is: " + chosenCharacter.currentLife);
                    System.out.println("Enemy's current life is: " + enemy.currentLife);

                    if (enemy.currentLife <= 0) {
                        System.out.println("You killed the enemy");
                        chosenCharacter.potions.numberOfCoins += 20;
                        chosenCharacter.currentExperience += 15;
                    }
                }
            }
        }
    }

    //metoda pentru obtinerea povestilor
    void getStory(Cell cell, Grid map) {
        if (!cell.isChecked) {
            if (map.map[cell.coordonateOx].get(cell.coordonateOy).type.equals(Cell.CellType.SHOP)) {
                System.out.println(cellTypeAndStory.get(Cell.CellType.SHOP).get(new Random().nextInt(cellTypeAndStory.get(Cell.CellType.SHOP).size())));
            }

            if (map.map[cell.coordonateOx].get(cell.coordonateOy).type.equals(Cell.CellType.ENEMY)) {
                System.out.println(cellTypeAndStory.get(Cell.CellType.ENEMY).get(new Random().nextInt(cellTypeAndStory.get(Cell.CellType.ENEMY).size())));
            }

            if (map.map[cell.coordonateOx].get(cell.coordonateOy).type.equals(Cell.CellType.FINISH)) {
                System.out.println(cellTypeAndStory.get(Cell.CellType.FINISH).get(new Random().nextInt(cellTypeAndStory.get(Cell.CellType.FINISH).size())));
            }

            if (map.map[cell.coordonateOx].get(cell.coordonateOy).type.equals(Cell.CellType.EMPTY)) {
                System.out.println(cellTypeAndStory.get(Cell.CellType.EMPTY).get(new Random().nextInt(cellTypeAndStory.get(Cell.CellType.EMPTY).size())));
            }
        }
    }


    public static void main(String[] args) throws JSONException {
        Game newGame = Game.getInstance();
        newGame.run();
    }
}

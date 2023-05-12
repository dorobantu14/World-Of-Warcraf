import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.Scanner;

public class Test {
    public void testGame() throws JSONException {
        Grid map = Grid.getGrid();
        map.getMap(5, 5, false);
        map.map[map.currentCell.coordonateOx].get(map.currentCell.coordonateOy).isChecked = true;
        //imi iau un index pentru a vedea ce miscare fac la apasarea literei P cu numarul index
        int commandIndex = 0;

        //parsez Json ul pentru a lua datele aferente contului
        //cu email-ul ales de mine: genna1951@hotmail.red
        String resourceAccounts = "/accounts.json";
        InputStream is = Game.class.getResourceAsStream(resourceAccounts);

        if (is == null) {
            throw new NullPointerException("Cannot find resource file " + resourceAccounts);
        }

        JSONTokener tokenerAccounts = new JSONTokener(is);
        JSONObject objAccounts = new JSONObject(tokenerAccounts);
        JSONArray arrayAccounts = objAccounts.getJSONArray("accounts");

        int accountIndex = 0;
        Credentials credentials1 = new Credentials();

        for (int i = 0; i < arrayAccounts.length(); i++) {
            JSONObject credentials;
            credentials = arrayAccounts.getJSONObject(i).optJSONObject("credentials");

            if (credentials.getString("email").equals("genna1951@hotmail.red")) {
                credentials1.setEmail(credentials.getString("email"));
                credentials1.setPassword(credentials.getString("password"));
                accountIndex = i;
            }
        }

        Collection<String> favGames = new ArrayList<>();
        JSONArray fav = arrayAccounts.getJSONObject(accountIndex).getJSONArray("favorite_games");

        for (int j = 0; j < fav.length(); j++) {
            favGames.add(fav.getString(j));
        }

        String playerName = arrayAccounts.getJSONObject(accountIndex).getString("name");
        String playerCountry = arrayAccounts.getJSONObject(accountIndex).getString("country");
        int NOGames = arrayAccounts.getJSONObject(accountIndex).getInt("maps_completed");

        //creez lista de caractere
        JSONArray characterArray = (JSONArray) arrayAccounts.getJSONObject(accountIndex).get("characters");
        ArrayList<Character> chList = new ArrayList<>();
        for (int k = 0; k < characterArray.length(); k++) {
            if (characterArray.getJSONObject(k).get("profession").equals("Mage")) {
                chList.add(new Mage(new Inventory(2, 50, new ArrayList<>()),
                        Integer.parseInt(characterArray.getJSONObject(k).get("level").toString()),
                        Integer.parseInt(characterArray.getJSONObject(k).get("experience").toString())));
            } else if (characterArray.getJSONObject(k).get("profession").equals("Warrior")) {
                chList.add(new Warrior(new Inventory(6, 100, new ArrayList<>()),
                        Integer.parseInt(characterArray.getJSONObject(k).get("level").toString()),
                        Integer.parseInt(characterArray.getJSONObject(k).get("experience").toString())));
            } else if (characterArray.getJSONObject(k).get("profession").equals("Rogue")) {
                chList.add(new Rogue(new Inventory(4, 70, new ArrayList<>()),
                        Integer.parseInt(characterArray.getJSONObject(k).get("level").toString()),
                        Integer.parseInt(characterArray.getJSONObject(k).get("experience").toString())));
            } else
                chList.add(null);
        }

        //creez contul
        Account chosenAccount = new Account(new Account.Information(credentials1, favGames, playerName, playerCountry), chList, NOGames);

        System.out.println(chosenAccount.informationPlayer.name);

        Character chosenCharacter = null;
        System.out.println("Character list for your account is: ");

        for (int i = 0; i < chosenAccount.characters.size(); i++) {
            System.out.println("Character  " + (i + 1) + "  " + chosenAccount.characters.get(i).characterName);
        }

        System.out.println("Choose one character, enter character's name!");

        //citesc de la tastatura caracterul ales de player
        int ok1 = 0;
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

        map.printMap(chosenCharacter);

        Enemy enemy = new Enemy(new Random().nextInt(70, 101), new Random().nextInt(20, 41));

        //citesc de la tastatura pentru a executa miscarile predefinite
        System.out.println("Please, enter P to see next move");
        Scanner commandInput = new Scanner(System.in);

        //incep implementarea miscarilor
        do {
            String command = commandInput.nextLine();
            if (commandIndex == 0 && command.equals("P")) {
                map.goEast();
                chosenCharacter.potions.numberOfCoins += 5;
                map.printMap(chosenCharacter);
                System.out.println(chosenCharacter.characterName + "'s details for this game is: \n" + "Coins: " +
                        chosenCharacter.potions.numberOfCoins + "\nCurrent life: " + chosenCharacter.currentLife + "\nCurrent mana: " +
                        chosenCharacter.currentMana + "\nCurrent level: " + chosenCharacter.currentLevel + "\nCurrent experience: " + chosenCharacter.currentExperience);
            }

            if (commandIndex == 1 && command.equals("P")) {
                map.goEast();
                chosenCharacter.potions.numberOfCoins += 5;
                map.printMap(chosenCharacter);
                System.out.println(chosenCharacter.characterName + "'s details for this game is: \n" + "Coins: " +
                        chosenCharacter.potions.numberOfCoins + "\nCurrent life: " + chosenCharacter.currentLife + "\nCurrent mana: " +
                        chosenCharacter.currentMana + "\nCurrent level: " + chosenCharacter.currentLevel + "\nCurrent experience: " + chosenCharacter.currentExperience);
            }

            if (commandIndex == 2 && command.equals("P")) {
                map.goEast();
                chosenCharacter.potions.numberOfCoins += 5;
                map.printMap(chosenCharacter);
                System.out.println(chosenCharacter.characterName + "'s details for this game is: \n" + "Coins: " +
                        chosenCharacter.potions.numberOfCoins + "\nCurrent life: " + chosenCharacter.currentLife + "\nCurrent mana: " +
                        chosenCharacter.currentMana + "\nCurrent level: " + chosenCharacter.currentLevel + "\nCurrent experience: " + chosenCharacter.currentExperience);
            }

            if (commandIndex == 3 && command.equals("P")) {
                System.out.println("You arrived in a shop");
                System.out.println("You can buy the potions from the above list:");
                System.out.println("1. Mana Potion");
                System.out.println("2. Health potion");
                Shop shop = new Shop(new ManaPotion(), new HealthPotion());
                for (int i = 0; i < shop.shopPotionList.size(); i++) {
                    if (shop.shopPotionList.get(i).getString().equals("Mana Potion")) {
                        chosenCharacter.buyPotion(shop.shopPotionList.get(i), chosenCharacter.potions);
                        System.out.println("Number of coins after buying: " + chosenCharacter.potions.numberOfCoins + "\nYou bought Mana Potion");
                    }

                    if (shop.shopPotionList.get(i).getString().equals("Health Potion")) {
                        chosenCharacter.buyPotion(shop.shopPotionList.get(i), chosenCharacter.potions);
                        System.out.println("Number of coins after buying: " + chosenCharacter.potions.numberOfCoins + "\nYou bought Mana Potion");
                    }
                }
            }

            if (commandIndex == 4 && command.equals("P")) {
                map.goEast();
                chosenCharacter.potions.numberOfCoins += 5;
                map.printMap(chosenCharacter);
                System.out.println(chosenCharacter.characterName + "'s details for this game is: \n" + "Coins: " +
                        chosenCharacter.potions.numberOfCoins + "\nCurrent life: " + chosenCharacter.currentLife + "\nCurrent mana: " +
                        chosenCharacter.currentMana + "\nCurrent level: " + chosenCharacter.currentLevel + "\nCurrent experience: " + chosenCharacter.currentExperience);
            }

            if (commandIndex == 5 && command.equals("P")) {
                map.goSouth();
                chosenCharacter.potions.numberOfCoins += 5;
                map.printMap(chosenCharacter);
                System.out.println(chosenCharacter.characterName + "'s details for this game is: \n" + "Coins: " +
                        chosenCharacter.potions.numberOfCoins + "\nCurrent life: " + chosenCharacter.currentLife + "\nCurrent mana: " +
                        chosenCharacter.currentMana + "\nCurrent level: " + chosenCharacter.currentLevel + "\nCurrent experience: " + chosenCharacter.currentExperience);
            }

            if (commandIndex == 6 && command.equals("P")) {
                map.goSouth();
                chosenCharacter.potions.numberOfCoins += 5;
                map.printMap(chosenCharacter);
                System.out.println(chosenCharacter.characterName + "'s details for this game is: \n" + "Coins: " +
                        chosenCharacter.potions.numberOfCoins + "\nCurrent life: " + chosenCharacter.currentLife + "\nCurrent mana: " +
                        chosenCharacter.currentMana + "\nCurrent level: " + chosenCharacter.currentLevel + "\nCurrent experience: " + chosenCharacter.currentExperience);
            }

            if (commandIndex == 7 && command.equals("P")) {
                map.goSouth();
                chosenCharacter.potions.numberOfCoins += 5;
                map.printMap(chosenCharacter);
                System.out.println(chosenCharacter.characterName + "'s details for this game is: \n" + "Coins: " +
                        chosenCharacter.potions.numberOfCoins + "\nCurrent life: " + chosenCharacter.currentLife + "\nCurrent mana: " +
                        chosenCharacter.currentMana + "\nCurrent level: " + chosenCharacter.currentLevel + "\nCurrent experience: " + chosenCharacter.currentExperience);
                System.out.println("You gone into an enemy :(");
            }

            if (commandIndex == 8 && command.equals("P")) {
                System.out.println("Now you'll gonna use your abilities");
                if (chosenCharacter.earth) {
                    chosenCharacter.useAbility(chosenCharacter.earth, enemy, chosenCharacter);

                }
                if (chosenCharacter.ice) {
                    chosenCharacter.useAbility(chosenCharacter.ice, enemy, chosenCharacter);

                }
                if (chosenCharacter.fire) {
                    chosenCharacter.useAbility(chosenCharacter.fire, enemy, chosenCharacter);
                }

                System.out.println(chosenCharacter.characterName + "'s mana is: " + chosenCharacter.currentMana);
                System.out.println("Enemy's mana is: " + enemy.currentMana);
                System.out.println(chosenCharacter.characterName + "'s current life is: " + chosenCharacter.currentLife);
                System.out.println("Enemy's current life is: " + enemy.currentLife);
            }

            if (commandIndex == 9 && command.equals("P")) {
                System.out.println("Now you'll gonna use your abilities");
                if (chosenCharacter.earth) {
                    chosenCharacter.useAbility(chosenCharacter.earth, enemy, chosenCharacter);

                }
                if (chosenCharacter.ice) {
                    chosenCharacter.useAbility(chosenCharacter.ice, enemy, chosenCharacter);

                }
                if (chosenCharacter.fire) {
                    chosenCharacter.useAbility(chosenCharacter.fire, enemy, chosenCharacter);
                }

                System.out.println(chosenCharacter.characterName + "'s mana is: " + chosenCharacter.currentMana);
                System.out.println("Enemy's mana is: " + enemy.currentMana);
                System.out.println(chosenCharacter.characterName + "'s current life is: " + chosenCharacter.currentLife);
                System.out.println("Enemy's current life is: " + enemy.currentLife);
                System.out.println("No more abilities :(\n");
            }

            if (commandIndex == 10 && command.equals("P")) {
                System.out.println("Now you'll gonna use your potions");
                for (int i = 0; i < chosenCharacter.potions.potionList.size(); i++) {
                    if (chosenCharacter.potions.potionList.get(i).getString().equals("Mana Potion")) {
                        chosenCharacter.potions.potionList.get(i).usePotion(chosenCharacter);
                        System.out.println(chosenCharacter.characterName + "'s mana after potion is: " + chosenCharacter.currentMana);
                    }

                    if (chosenCharacter.potions.potionList.get(i).getString().equals("Health Potion")) {
                        chosenCharacter.potions.potionList.get(i).usePotion(chosenCharacter);
                        System.out.println(chosenCharacter.characterName + "'s current life after potion is: " + chosenCharacter.currentLife);
                    }
                }
            }

            if (commandIndex == 11 && command.equals("P")) {
                System.out.println(chosenCharacter.characterName + "'s current life is: " + chosenCharacter.currentLife);
                System.out.println("Enemy's current life is: " + enemy.currentLife);
                while (enemy.currentLife > 0 || chosenCharacter.currentLife > 0) {
                    enemy.receiveDamage(chosenCharacter.getDamage());
                    chosenCharacter.receiveDamage(enemy.getDamage());
                    System.out.println(chosenCharacter.characterName + "'s current life is: " + chosenCharacter.currentLife);
                    System.out.println("Enemy's current life is: " + enemy.currentLife);
                    if (enemy.currentLife <= 0) {
                        System.out.println("You killed the enemy");
                        chosenCharacter.potions.numberOfCoins += 20;
                        chosenCharacter.currentExperience += 15;
                        break;
                    }

                    if (chosenCharacter.currentLife <= 0) {
                        System.out.println("You lost the game :(");
                        break;
                    }
                }
            }

            if (commandIndex == 12 && command.equals("P")) {
                map.goSouth();
                chosenCharacter.potions.numberOfCoins += 5;
                map.printMap(chosenCharacter);
                System.out.println(chosenCharacter.characterName + "'s details for this game is: \n" + "Coins: " +
                        chosenCharacter.potions.numberOfCoins + "\nCurrent life: " + chosenCharacter.currentLife + "\nCurrent mana: " +
                        chosenCharacter.currentMana + "\nCurrent level: " + chosenCharacter.currentLevel + "\nCurrent experience: " + chosenCharacter.currentExperience);
            }

            if (map.map[map.currentCell.coordonateOx].get(map.currentCell.coordonateOy).type.equals(Cell.CellType.FINISH)) {
                System.out.println("You finished the game :)");
                chosenCharacter.currentLevel += 1;
                System.out.println(chosenCharacter.characterName + "'s details for this game is: \n" + "Coins: " +
                        chosenCharacter.potions.numberOfCoins + "\nCurrent life: " + chosenCharacter.currentLife + "\nCurrent mana: " +
                        chosenCharacter.currentMana + "\nCurrent level: " + chosenCharacter.currentLevel + "\nCurrent experience: " + chosenCharacter.currentExperience);
                break;
            }

            map.map[map.currentCell.coordonateOx].get(map.currentCell.coordonateOy).isChecked = true;
            commandIndex += 1;
            System.out.println("Please, enter P to see next move");
            commandInput = new Scanner(System.in);
        } while (commandInput.hasNextLine() && chosenCharacter.currentLife > 0);
    }

    public static void main(String[] args) throws JSONException {
        Test test = new Test();
        test.testGame();
    }
}

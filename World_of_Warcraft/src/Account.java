import java.util.ArrayList;
import java.util.Collection;

public class Account {
    Information informationPlayer;
    ArrayList<Character> characters;
    int numberOfGames;

    Account(Information information, ArrayList<Character> characters, int numberOfGames){
        informationPlayer = information;
        this.characters = characters;
        this.numberOfGames = numberOfGames;
    }

    static class Information {
        final Credentials credentials;
        final Collection favoritesGames;
        final String name;
        final String country;

        public Information (Credentials credentials, Collection favoritesGames, String name, String country) {
            this.credentials = credentials;
            this.favoritesGames = favoritesGames;
            this.name = name;
            this.country = country;
        }
    }

    public static class Builder {
        private Credentials credentials;
        private Collection favoriteGames;
        private String name;
        private String country;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setCountry(String country) {
            this.country = country;
            return this;
        }

        public Builder setCredentials(Credentials credentials) {
            this.credentials = credentials;
            return this;
        }

        public Builder setFavoriteGames(Collection favoriteGames) {
            this.favoriteGames = favoriteGames;
            return this;
        }

        public Information build() {
            return new Information(credentials, favoriteGames, name, country);
        }
    }
}


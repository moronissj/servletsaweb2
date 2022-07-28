package mx.edu.utez.aweb.pokemonapp.service.auth;

import mx.edu.utez.aweb.pokemonapp.model.auth.BeanUser;
import mx.edu.utez.aweb.pokemonapp.model.auth.DaoAuth;

public class AuthService {

    DaoAuth auth = new DaoAuth();

    public BeanUser login(String username, String password) {
        return auth.validate(username, password);
    }
}

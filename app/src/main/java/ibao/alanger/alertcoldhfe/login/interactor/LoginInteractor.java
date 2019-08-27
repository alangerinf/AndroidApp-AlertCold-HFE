package ibao.alanger.alertcoldhfe.login.interactor;

import ibao.alanger.alertcoldhfe.model.User;

public interface LoginInteractor {

    void signIn(String user, String password);

    void signSuccess(User userTemp);

    void signError(String mensajeRespuesta);
}

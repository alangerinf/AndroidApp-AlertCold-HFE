package ibao.alanger.alertcoldhfe.login.interactor;


import android.content.Context;
import android.util.Log;

import ibao.alanger.alertcoldhfe.login.presenter.LoginPresenter;
import ibao.alanger.alertcoldhfe.login.repository.LoginRepository;
import ibao.alanger.alertcoldhfe.login.repository.LoginRepositoryImpl;
import ibao.alanger.alertcoldhfe.model.SharedPreferencesManager;
import ibao.alanger.alertcoldhfe.model.User;

public class LoginInteractorImpl implements LoginInteractor {

    private Context ctx;
    private LoginPresenter presenter;
    private LoginRepository repository;

    String TAG = LoginInteractorImpl.class.getSimpleName();

    public LoginInteractorImpl(Context ctx,LoginPresenter presenter) {
        this.presenter = presenter;
        this.ctx = ctx;
        this.repository = new LoginRepositoryImpl(this);
    }

    @Override
    public void signIn(String user, String password) {
        repository.signIn(user,password);
    }

    @Override
    public void signSuccess(User userTemp) {
        Log.d(TAG,userTemp.toString());
        if(SharedPreferencesManager.saveUser(ctx,userTemp)){
            presenter.loginSuccess();
        }else {
            presenter.loginError("No se pudo guardar el usuario");
        }


    }

    @Override
    public void signError(String mensajeRespuesta) {
        presenter.loginError(mensajeRespuesta);
    }




}

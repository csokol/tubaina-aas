package controllers;

import play.mvc.Action.Simple;
import play.mvc.Http.Context;
import play.mvc.Result;

public class LoggedAction extends Simple{

	@Override
	public Result call(Context ctx) throws Throwable {
		if(ctx.session().get("user")!=null){
			return delegate.call(ctx);
		}
		return redirect(controllers.routes.LoginController.notLogged());
	}

}

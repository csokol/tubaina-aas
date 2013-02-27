package controllers;

import java.util.List;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;

import play.Configuration;
import play.Play;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;
import play.libs.WS.WSRequestHolder;
import play.mvc.Controller;
import play.mvc.Result;

public class LoginController extends Controller{
	
	
	public static Result notLogged(){
		return ok(views.html.notLogged.render());
	}
	
	public static Result authenticationFailed(){
		return badRequest(views.html.authenticationFailed.render());
	}
	
	public static Result logout(){
		session().clear();
		return redirect(routes.Application.index());
	}

	public static Result login(){
		String githubCode = request().getQueryString("code");
		Configuration configuration = Play.application().configuration();
		String clientId = configuration.getString("github.client_id");
		String secretId = configuration.getString("github.client_secret");
		WSRequestHolder url = WS.url("https://github.com/login/oauth/access_token");
		url.setQueryParameter("client_id", clientId);
		url.setQueryParameter("client_secret", secretId);
		url.setQueryParameter("code", githubCode);
		Promise<Response> waitingGithub = url.post("");
		Promise<Result> waitingResult = waitingGithub.map(new Function<WS.Response, Result>() {

			@Override
			public Result apply(Response response) throws Throwable {
				String token = response.getBody().split("=")[1].split("&")[0];
				GitHubClient client = new GitHubClient();
				client.setOAuth2Token(token);
				RepositoryService service = new RepositoryService(client);
				List<Repository> all = service.getOrgRepositories("caelum");
				for (Repository repository : all) {
					if(repository.getName().equals("apostilas-novas")){
						session().put("user",client.getUser());
						return redirect(controllers.routes.Application.index());
					}
				}
				return redirect(controllers.routes.Application.index());
			}
		});
		return async(waitingResult);
	}
}

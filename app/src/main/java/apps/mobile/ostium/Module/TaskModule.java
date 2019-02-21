package apps.mobile.ostium.Module;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.tasks.TasksScopes;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskModule
{
    private Tasks tasks = null;
    private GoogleApiClient googleApiClient;
    private GoogleSignInAccount account;
    private Activity activity;
    GoogleSignInResult res;

    public TaskModule(Activity a, GoogleApiClient googleApiClient)
    {
        activity = a;
        this.googleApiClient = googleApiClient;
        signIn();
    }

    private void signIn()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        activity.startActivityForResult(signInIntent, 111);
    }

    public void GetAllTasks()
    {
        final AccountManager manager = AccountManager.get(activity.getApplicationContext());
        final Bundle options = new Bundle();
        if (res.isSuccess())
        {
            account = res.getSignInAccount();
            final String AUTH_TOKEN_TYPE = "Manage your tasks";
            @SuppressLint("StaticFieldLeak")
            AsyncTask task = new AsyncTask()
            {
                @Override
                protected Object doInBackground(Object[] objects)
                {
                    try
                    {
                        manager.getAuthToken(
                                account.getAccount(),
                                AUTH_TOKEN_TYPE,
                                options,
                                null,
                                new AccountManagerCallback<Bundle>()
                                {
                                    @Override
                                    public void run(AccountManagerFuture<Bundle> result)
                                    {
                                        try
                                        {
                                            // Get the result of the operation from the AccountManagerFuture.
                                            Bundle bundle = result.getResult();

                                            // The token is a named value in the bundle. The name of the value
                                            // is stored in the constant AccountManager.KEY_AUTHTOKEN.
                                            //String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
                                            final String[] scopes = {TasksScopes.TASKS};
                                            GetAllTaskDetails(GoogleAccountCredential.usingOAuth2(activity.getApplicationContext(), Arrays.asList(scopes))
                                                    .setBackOff(new ExponentialBackOff())
                                                    .setSelectedAccount(account.getAccount()));
                                        }
                                        catch (Exception e)
                                        {
                                            // TODO: The user has denied you access to the API, you should handle that
                                        }
                                    }
                                }, null).getResult();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            task.execute();
        }
    }

    private ArrayList<Task> GetAllTaskDetails(GoogleAccountCredential credential)
    {
        final ArrayList<Task> result = new ArrayList<>();
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        tasks = new Tasks.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Google Tasks API Android Quickstart")
                .build();

        @SuppressLint("StaticFieldLeak") final AsyncTask task = new AsyncTask()
        {
            @Override
            protected Object doInBackground(Object... objects)
            {
                try
                {
                    TaskLists x = tasks.tasklists().list().execute();
                    TaskList d = x.getItems().get(0);
                    List<Task> z = tasks.tasks().list(d.getId()).execute().getItems();

                    for (int i = 0; i < z.size(); i++)
                    {
                        result.add(z.get(i));
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                return null;
            }
        };

        try
        {
            task.execute();
            task.wait();
        }
        catch (Exception e){}

        return result;
    }
}
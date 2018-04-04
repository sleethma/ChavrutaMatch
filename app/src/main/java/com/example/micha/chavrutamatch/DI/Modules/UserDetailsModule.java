package com.example.micha.chavrutamatch.DI.Modules;

import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.DI.Scopes.AppScope;
import com.example.micha.chavrutamatch.DI.Scopes.MAScope;
import dagger.Module;
import dagger.Provides;

@Module
public class UserDetailsModule {

    @Provides
    @AppScope
    UserDetails providesUserDetails() {
        return new UserDetails(null, null, null, null);
    }
}

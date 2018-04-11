package com.example.micha.chavrutamatch.DI.Modules;

import com.example.micha.chavrutamatch.AcctLogin.UserDetails;
import com.example.micha.chavrutamatch.DI.Scopes.AppScope;
import dagger.Module;
import dagger.Provides;

@Module
public class UserDetailsModule {
private UserDetails testUserDetails;

    @Provides
    @AppScope
     UserDetails providesUserDetails() {
        testUserDetails = new UserDetails(null, null, null, null);
        return testUserDetails;
    }
}

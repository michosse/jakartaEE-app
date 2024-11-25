package com.example.app.configuration.authconfig;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

@ApplicationScoped
@BasicAuthenticationMechanismDefinition(realmName = "Ticket application")
@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "jdbc/TicketApplication",
        callerQuery = "select password from users where name = ?",
        groupsQuery = "select role from users__roles where id = (select id from users where name = ?)",
        hashAlgorithm = Pbkdf2PasswordHash.class
)
public class AuthenticationConfig {
}
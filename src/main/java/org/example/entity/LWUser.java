package org.example.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
@Data
@Document(collection = "Users")

public class LWUser implements UserDetails {
    @Id
    private String id;
    private String username;
    private String password;
    private boolean hasETHWallet;
    private String ETHAddress;
    private String ETHPrivateKey;
    private String mnemonicWords;

    public LWUser(String username, String password){
        this.username = username;
        this.password = password;
        this.hasETHWallet = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("USER"));
    }
    public boolean isHasETHWallet(){
        return this.hasETHWallet;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

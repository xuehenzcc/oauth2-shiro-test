package com.zcc.test.mapper;

import org.springframework.stereotype.Component;

import com.zcc.test.model.AccessToken;

@Component
public interface AccessMapper {

	public AccessToken findAccessTokenByTokenId(String tokenId);
	
}

package com.zcc.test.repository;

import com.zcc.test.model.AccessToken;
import com.zcc.test.model.ClientDetails;

/**
 * 2015/10/7
 *
 * @author Shengzhao Li
 */

public interface OAuthRSRepository  {

    AccessToken findAccessTokenByTokenId(String tokenId);

    ClientDetails findClientDetailsByClientIdAndResourceIds(String clientId, String resourceIds);
}
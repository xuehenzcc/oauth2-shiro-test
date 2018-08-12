package com.zcc.test.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcc.test.model.AccessToken;
import com.zcc.test.model.ClientDetails;
import com.zcc.test.repository.OAuthRSRepository;

/**
 * 2015/7/8
 *
 * @author Shengzhao Li
 */
@Service("oAuthRSService")
public class OAuthRSServiceImpl {


    private static final Logger LOG = LoggerFactory.getLogger(OAuthRSServiceImpl.class);


//    @Autowired
//    private OAuthRSCacheRepository oAuthRSRepository;

    @Autowired
    private OAuthRSRepository oAuthRSRepository;
    

    public AccessToken loadAccessTokenByTokenId(String tokenId) {
        return oAuthRSRepository.findAccessTokenByTokenId(tokenId);
    }


    public ClientDetails loadClientDetails(String clientId, String resourceIds) {
        LOG.debug("Load ClientDetails by clientId: {}, resourceIds: {}", clientId, resourceIds);
        return oAuthRSRepository.findClientDetailsByClientIdAndResourceIds(clientId, resourceIds);
    }


}

package com.zcc.test.shiro;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zcc.test.exception.OAuth2AuthenticationException;
import com.zcc.test.model.AccessToken;
import com.zcc.test.model.ClientDetails;
//import com.zcc.test.model.Permission;
//import com.zcc.test.model.Role;
//import com.zcc.test.model.User;
import com.zcc.test.service.OAuthRSServiceImpl;

public class MyShiroRealm extends AuthorizingRealm {

	private static final Logger LOG = LoggerFactory.getLogger(MyShiroRealm.class);
	
	@Autowired
	OAuthRSServiceImpl rsService;	
	
//	private OAuthRSRepository oauthRSJdbcRepository;
	
	@Override
	public boolean supports(AuthenticationToken token) {
		// TODO Auto-generated method stub
		return true;
	}
	/**
	 * 用户授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		//获取登录用户名
//        String name= (String) principalCollection.getPrimaryPrincipal();
//        //查询用户名称
////        User user = loginService.findByName(name);
//        User user = new User();
//        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
//        for (Role role:user.getRoles()) {
//            //添加角色
//            simpleAuthorizationInfo.addRole(role.getRoleName());
//            for (Permission permission:role.getPermissions()) {
//                //添加权限
//                simpleAuthorizationInfo.addStringPermission(permission.getPermission());
//            }
//        }
        return simpleAuthorizationInfo;
	}

	/**
	 * 用户认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo (
			AuthenticationToken token) throws AuthenticationException {
        
        OAuth2Token upToken = (OAuth2Token) token;
        final String accessToken = (String) upToken.getCredentials();

        if (StringUtils.isEmpty(accessToken)) {
            throw new OAuth2AuthenticationException("Invalid access_token: " + accessToken);
        }
        //Validate access token
        AccessToken aToken = rsService.loadAccessTokenByTokenId(accessToken);
//        AccessToken aToken = oauthRSJdbcRepository.findAccessTokenByTokenId(accessToken);
        
		validateToken(accessToken, aToken);

        //Validate client details by resource-id
        final ClientDetails clientDetails = rsService.loadClientDetails(aToken.clientId(), upToken.getResourceId());
//        final ClientDetails clientDetails=oauthRSJdbcRepository.findClientDetailsByClientIdAndResourceIds(aToken.clientId(), upToken.getResourceId());
		validateClientDetails(accessToken, aToken, clientDetails);

        String username = aToken.username();

        // Null username is invalid
        if (username == null) {
            throw new AccountException("Null usernames are not allowed by this realm.");
        }

        return new SimpleAuthenticationInfo(username, accessToken, getName());
        
	}

	private void validateToken(String token, AccessToken accessToken)  {
        if (accessToken == null) {
            LOG.debug("Invalid access_token: {}, because it is null", token);
        	throw new OAuth2AuthenticationException("Invalid access_token: " + token);
        }
        if (accessToken.tokenExpired()) {
            LOG.debug("Invalid access_token: {}, because it is expired", token);
        	throw new OAuth2AuthenticationException("Invalid access_token: " + token);
        }
    }

    private void validateClientDetails(String token, AccessToken accessToken, ClientDetails clientDetails)  {
        if (clientDetails == null || clientDetails.archived()) {
            LOG.debug("Invalid ClientDetails: {} by client_id: {}, it is null or archived", clientDetails, accessToken.clientId());
            throw new OAuth2AuthenticationException("Invalid client by token: " + token);
        }
    }
    
    
    
//	public void setOauthRSJdbcRepository(OAuthRSRepository oauthRSJdbcRepository) {
//		this.oauthRSJdbcRepository = oauthRSJdbcRepository;
//	}
	
//	@Override
//    public void afterPropertiesSet() throws Exception {
//        Assert.notNull(oauthRSJdbcRepository, "usersRepository is null");
//    }
    
	
}

package com.zcc.test.shiro.filter;

//import com.monkeyk.os.domain.oauth.AccessToken;
//import com.monkeyk.os.service.OAuthRSService;
//import org.apache.oltu.oauth2.common.OAuth;
//import org.apache.oltu.oauth2.common.error.OAuthError;
//import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
//import org.apache.oltu.oauth2.common.message.OAuthResponse;
//import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.zcc.test.mapper.AccessMapper;
import com.zcc.test.model.AccessToken;
import com.zcc.test.repository.OAuthRSJdbcRepository;
import com.zcc.test.service.OAuthRSServiceImpl;
import com.zcc.test.shiro.OAuth2Token;
import com.zcc.test.util.WebUtils;

/**
 * 2015/9/29
 *
 * @author Shengzhao Li
 */
public class OAuth2Filter extends AuthenticatingFilter implements InitializingBean {


    private static Logger logger = LoggerFactory.getLogger(OAuth2Filter.class);


    //   ParameterStyle.HEADER
//    private ParameterStyle[] parameterStyles = new ParameterStyle[]{ParameterStyle.QUERY};

    private String resourceId;
    private OAuthRSServiceImpl rsService;
//    private OAuthRSJdbcRepository jdbcRepository;

//    @Autowired
//    private AccessMapper accessMapper;
    
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        
//        ApplicationContext ac =
//                WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
            
//        OAuthRSServiceImpl userFilterService = (OAuthRSServiceImpl)ac.getBean("oAuthRSService");
           

            
        final String accessToken = httpRequest.getParameter(OAuth.OAUTH_ACCESS_TOKEN);
//        final AccessToken token = userFilterService.loadAccessTokenByTokenId(accessToken);
//        final AccessToken token = jdbcRepository.findAccessTokenByTokenId(accessToken);
//        final AccessToken token =  accessMapper.findAccessTokenByTokenId(accessToken);
//        AccessToken token=new AccessToken();
//        token.username("test");
        
        String username = null;
//        if (token != null) {
//            username = token.username();
//            logger.debug("Set username[{}] and clientId[{}] to request that from AccessToken: {}", username, token.clientId(), token);
//            httpRequest.setAttribute(OAuth.OAUTH_CLIENT_ID, token.clientId());
//        } else {
//            logger.debug("Not found AccessToken by access_token: {}", accessToken);
//        }

        return new OAuth2Token(accessToken, resourceId)
                .setUserId(username);
    }


    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return executeLogin(request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException ae, ServletRequest request,
                                     ServletResponse response) {
//        OAuth2Token oAuth2Token = (OAuth2Token) token;
//    	  System.out.println(ae.getMessage());
        final OAuthResponse oAuthResponse;
        try {
            oAuthResponse = OAuthRSResponse.errorResponse(401)
                    .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
                    .setErrorDescription(ae.getMessage())
                    .buildJSONMessage();

            WebUtils.writeOAuthJsonResponse((HttpServletResponse) response, oAuthResponse);

        } catch (OAuthSystemException e) {
            logger.error("Build JSON message error", e);
            throw new IllegalStateException(e);
        }


        return false;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public void setRsService(OAuthRSServiceImpl rsService) {
        this.rsService = rsService;
    }

//	public void setJdbcRepository(OAuthRSJdbcRepository jdbcRepository) {
//		this.jdbcRepository = jdbcRepository;
//	}
//
//	public void setAccessMapper(AccessMapper accessMapper) {
//		this.accessMapper = accessMapper;
//	}


	@Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(resourceId, "resourceId is null");
        Assert.notNull(rsService, "rsService is null");
//        Assert.notNull(jdbcRepository, "jdbcRepository is null");
//        Assert.notNull(accessMapper, "accessMapper is null");
    }
}

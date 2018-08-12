package com.zcc.test.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zcc.test.dao.AbstractJdbcRepository;
import com.zcc.test.dao.AccessTokenRowMapper;
import com.zcc.test.dao.ClientDetailsRowMapper;
import com.zcc.test.model.AccessToken;
import com.zcc.test.model.ClientDetails;

/**
 * 15-6-13
 *
 * @author Shengzhao Li
 */
@Repository("oauthRSJdbcRepository")
public class OAuthRSJdbcRepository extends AbstractJdbcRepository implements OAuthRSRepository {


    private static ClientDetailsRowMapper clientDetailsRowMapper = new ClientDetailsRowMapper();
    private AccessTokenRowMapper accessTokenRowMapper = new AccessTokenRowMapper();


    @Override
    public AccessToken findAccessTokenByTokenId(String tokenId) {
        final String sql = " select * from oauth_access_token where token_id = ?";
        final List<AccessToken> list = jdbcTemplate.query(sql, accessTokenRowMapper, tokenId);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public ClientDetails findClientDetailsByClientIdAndResourceIds(String clientId, String resourceIds) {
        final String sql = " select * from oauth_client_details where archived = 0 and client_id = ? and resource_ids = ? ";
        final List<ClientDetails> list = jdbcTemplate.query(sql, clientDetailsRowMapper, clientId, resourceIds);
        return list.isEmpty() ? null : list.get(0);
    }
}

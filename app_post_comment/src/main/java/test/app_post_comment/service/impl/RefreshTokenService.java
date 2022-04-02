package test.app_post_comment.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.app_post_comment.service.RefreshTokenService;

@Service
@Transactional(readOnly = true)
public class RefreshTokenServiceImpl implements RefreshTokenService {
}

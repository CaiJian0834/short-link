package com.cxj.link.controller;

import com.cxj.link.annotation.LogParamsResponse;
import com.cxj.link.model.ShortLinkInfoVO;
import com.cxj.link.service.ShortLinkStatisticsService;
import com.cxj.link.service.ShortUrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 访问地址
 *
 * @author cxj
 * @emall 735374036@qq.com
 */
@LogParamsResponse
@Slf4j
@RestController
@RequestMapping("/")
public class RedirectController {

    @Autowired
    private ShortUrlService shortUrlService;
    @Autowired
    private ShortLinkStatisticsService shortLinkStatisticsService;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @GetMapping(value = "/{hash}")
    public String redirect(HttpServletRequest request, HttpServletResponse response, @PathVariable("hash") String hash) throws IOException {

        ShortLinkInfoVO vo = shortUrlService.getReallyByHash(hash);
        if (vo == null) {
            log.info("短链接不存在,hash[{}]", hash);
            return "";
        }
        // 302 重定向
        response.sendRedirect(vo.getUrl());

        // 统计访问次数
        threadPoolTaskExecutor.execute(() -> shortLinkStatisticsService.incr(vo.getId()));

        return "";
    }
}

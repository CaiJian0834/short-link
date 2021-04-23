package com.cxj.link.controller;

import com.cxj.link.annotation.LogParamsResponse;
import com.cxj.link.model.ApiResultModel;
import com.cxj.link.model.ShortLinkRequestDTO;
import com.cxj.link.model.ShortLinkResultVO;
import com.cxj.link.model.ShortLinkInfoVO;
import com.cxj.link.service.ShortUrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 生成短链接
 *
 * @author cxj
 * @emall 735374036@qq.com
 */
@LogParamsResponse
@RestController
@RequestMapping("/short/link/url")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class IndexController {

    @Autowired
    private ShortUrlService shortUrlService;

    /**
     * 生成短链接
     *
     * @param dto
     * @return
     */
    @PostMapping("/create")
    public ApiResultModel<ShortLinkResultVO> create(@RequestBody ShortLinkRequestDTO dto) {
        return shortUrlService.create(dto);
    }

    @GetMapping("/getByHash/{hashValue}")
    public ApiResultModel<ShortLinkInfoVO> getByHash(@PathVariable("hashValue") String hash) {
        return shortUrlService.getByHash(hash);
    }


}

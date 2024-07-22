package com.yupi.springbootinit.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.dataSource.*;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.model.dto.post.PostQueryRequest;
import com.yupi.springbootinit.model.dto.search.SearchRequest;
import com.yupi.springbootinit.model.dto.user.UserQueryRequest;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.model.entity.Video;
import com.yupi.springbootinit.model.enums.SearchTypeEnum;
import com.yupi.springbootinit.model.vo.PostVO;
import com.yupi.springbootinit.model.vo.SearchVO;
import com.yupi.springbootinit.model.vo.UserVO;
import com.yupi.springbootinit.service.PictureService;
import com.yupi.springbootinit.service.PostService;
import com.yupi.springbootinit.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 设计模式: 门面模式
 * 搜索门面
 */
@Component
public class SearchFacade {

    @Resource
    private PostDataSource postDataSource;

    @Resource
    private PictureDataSource pictureDataSource;

    @Resource
    private UserDataSource userDataSource;

    @Resource
    private VideoDataSource videoDataSource;

    @Resource
    private DataSourceRegistry dataSourceRegistry;

    public SearchVO searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request){

        String type = searchRequest.getType();
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);
        ThrowUtils.throwIf(StringUtils.isBlank(type), ErrorCode.PARAMS_ERROR);
        String searchText = searchRequest.getSearchText();
        long pageNum = searchRequest.getCurrent();
        long pageSize = searchRequest.getPageSize();
        SearchVO searchVO = new SearchVO();

        if (searchTypeEnum == null){
            Page<PostVO> postVOPage = postDataSource.doSearch(searchText, pageNum, pageSize);
            searchVO.setPostList(postVOPage.getRecords());
            Page<Picture> picturePage = pictureDataSource.doSearch(searchText, pageNum, pageSize);
            searchVO.setPictureList(picturePage.getRecords());
            Page<UserVO> userVOPage = userDataSource.doSearch(searchText, pageNum, pageSize);
            searchVO.setUserList(userVOPage.getRecords());
            Page<Video> videoPage = videoDataSource.doSearch(searchText, pageNum, pageSize);
            searchVO.setVideoList(videoPage.getRecords());
        }else {
            DataSource dataSource = dataSourceRegistry.getDataSourceByType(type);
            Page page = dataSource.doSearch(searchText, pageNum, pageSize);
            searchVO.setDataList(page.getRecords());
        }
        return searchVO;

        // 并发平均快了 243ms, 并发695.2ms 优化前 719.5 但也不是很稳定，考虑短板效应
        // CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(() -> {
        //
        //     Page<Picture> picturePage = pictureService.searchPicture(searchText, 1, 10);
        //     return picturePage;
        // });
        //
        // CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(() -> {
        //
        //     PostQueryRequest postQueryRequest = new PostQueryRequest();
        //     postQueryRequest.setSearchText(searchText);
        //     Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);
        //     return postVOPage;
        // });
        //
        // CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(() -> {
        //
        //     UserQueryRequest userQueryRequest = new UserQueryRequest();
        //     userQueryRequest.setUserName(searchText);
        //     Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
        //     return userVOPage;
        // });
        //
        // CompletableFuture.allOf(userTask, postTask, pictureTask).join();
        // try {
        //     Page<UserVO> userVOPage = userTask.get();
        //     Page<PostVO> postVOPage = postTask.get();
        //     Page<Picture> picturePage = pictureTask.get();
        //     SearchVO searchVO = new SearchVO();
        //     searchVO.setPictureList(picturePage.getRecords());
        //     searchVO.setPostList(postVOPage.getRecords());
        //     searchVO.setUserList(userVOPage.getRecords());
        //     return ResultUtils.success(searchVO);
        // }catch (Exception e){
        //     log.error("查询异常", e);
        //     throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询异常");
        // }
    }
}

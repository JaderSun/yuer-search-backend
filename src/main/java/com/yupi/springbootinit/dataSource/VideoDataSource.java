package com.yupi.springbootinit.dataSource;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.model.entity.Video;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 视频服务实现类
 *
 * @author <a href="https://github.com/JaderSun">JaderSun</a>
 */
@Service
public class VideoDataSource implements DataSource<Video> {

    @Override
    public Page<Video> doSearch(String searchText, long pageNum, long pageSize) {

        String url = String.format("https://search.bilibili.com/all?keyword=%s", searchText);
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据获取异常");
        }
        Elements elements = doc.select(".bili-video-card__wrap");
        List<Video> videos = new ArrayList<>();
        for (Element element : elements) {

            String href = element.childNode(0).attr("href");
            String title = element.select(".bili-video-card__info--tit").attr("title");
            String src = element.select(".v-img.bili-video-card__cover").get(0).childNode(3).attr("src");
            Video video = new Video();
            video.setTitle(title);
            video.setUrl(href);
            video.setPic(src);
            videos.add(video);
            if (videos.size() >= pageSize) break;
        }
        Page<Video> videoPage = new Page<>(pageNum, pageSize);
        videoPage.setRecords(videos);
        return videoPage;
    }
}

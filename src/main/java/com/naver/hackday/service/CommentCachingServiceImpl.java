package com.naver.hackday.service;


import com.naver.hackday.dto.CommentDto;
import com.naver.hackday.dto.ValidCachingKey;
import com.naver.hackday.model.codingsquid.BaseCode;
import com.naver.hackday.model.codingsquid.BaseListRtn;
import com.naver.hackday.model.codingsquid.BaseResponse;
import com.naver.hackday.repository.cache.RedisRepository;
import com.naver.hackday.util.cache.CachingKeyHelper;
import com.naver.hackday.util.cache.CachingServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CommentCachingServiceImpl implements CommentListService {

    private final CommentListService commentListService;
    private final RedisRepository redisRepository;
    private final CachingServiceHelper<String, List<Object>> cachingServiceHelper;

    @Autowired
    public CommentCachingServiceImpl(@Qualifier("redisRepositoryImpl") RedisRepository redisRepository,
                                     @Qualifier("commentOriginServiceImpl") CommentListService commentListService) {
        this.redisRepository = redisRepository;
        this.cachingServiceHelper = new CachingServiceHelper<>();
        this.commentListService = commentListService;
    }

    @Transactional
    @SuppressWarnings("unchecked")
    @Override
    public BaseResponse<BaseListRtn<CommentDto>> doGet(int cursor, int size, int pageNo, String orderType, int postId, int userId) {
        BaseResponse<BaseListRtn<CommentDto>> result;
        String postKey = Integer.toString(postId);
        String pageKey = Integer.toString(pageNo);
        String cachingKey = CachingKeyHelper.getCommentListKey(postKey, pageKey);
        String cachingValidKey = CachingKeyHelper.getValidationCommentListKey(postKey, pageKey);


        //TODO 트랜잭션 && pipeline적용 && key가 존재하지 않을 경우 예외처리
        if (cachingServiceHelper.isNeedCaching(() -> redisRepository.getData(cachingValidKey))) {

            result = commentListService.doGet(cursor, size, pageNo, orderType, postId, userId);

            for (CommentDto comments : result.getResult().getDatas()) {
                redisRepository.setListToListRight(cachingKey, comments, 20000L);
            }
            redisRepository.setData(cachingValidKey,
                    new ValidCachingKey(cachingKey, true),
                    20000L);

            Stream<CommentDto> rtnList = result.getResult().getDatas().stream();
            List<CommentDto> dtos = rtnList.skip(cursor - 1)
                    .limit(size)
                    .collect(Collectors.toList());

            result.getResult().setDatas(dtos);
            result.getResult().setTotalSize(dtos.size());

            return result;
        }

        //캐싱된 데이터를 반환
        result = new BaseResponse<>();
        BaseListRtn<CommentDto> commentDtoBaseListRtn = new BaseListRtn<>();

        //TODO 데이터 크기 조정
        List<Object> datas = cachingServiceHelper.useCaching(key -> redisRepository.getRangeFromList(key, cursor, (cursor + size - 1)), cachingKey);

        commentDtoBaseListRtn.setDatas((List<CommentDto>)(Object) datas);
        commentDtoBaseListRtn.setTotalSize(datas.size());

        result.setResult(commentDtoBaseListRtn);
        result.setReturnMessage(BaseCode.SUCCESS.getMessage());
        result.setReturnCode(BaseCode.SUCCESS.getCode());

        return result;
    }

}

package com.wowmate.server.post.service;
import com.wowmate.server.post.domain.Post;
import com.wowmate.server.post.dto.PostInfoDto;
import com.wowmate.server.config.Response;
import com.wowmate.server.config.BaseException;
import com.wowmate.server.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;



@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    //게시글 전체 조회
    @Transactional
    public List<PostInfoDto> getPostList() {
        List<Post> postList;
        List<PostInfoDto> postInfoDtoList = new ArrayList<>();  //반환할 List를 생성
        //여기에 try로 null 인지 보고 catch하면 될듯?
        postList = postRepository.findAll();         //DB에서 값 전체를 불러옴

        for (int i=0;i<postList.size(); i++) {     //postList의 개수 만큼 필요한 값들만 List안에 추가
            Post p = postList.get(i);                   //전체 정보 임시 보관용 객체
            PostInfoDto postInfoDto = new PostInfoDto(  //필요한 정보만을 임시 보관용 객체
                    p.getTitle(),
                    p.getCategory().getName(),
                    p.getTag1(),
                    p.getTag2(),
                    p.getTag3(),
                    p.getTag4(),
                    p.getTag5(),
                    p.getLikeNumber(),
                    p.getUser().getSchool_id(),
                    p.getCreatedBy()
            );
            postInfoDtoList.add(postInfoDto);       //반환할 List안에 대입
        }
        return postInfoDtoList;                     //반환
    }

    //게시글 제목 검색
    @Transactional
    public List<PostInfoDto> getPostListByTitle(String postTitle) {
        List<Post> postList = postRepository.findByTitleContaining(postTitle); //제목으로 DB에서 검색후 리스트 생성

        List<PostInfoDto> postInfoDtoList = new ArrayList<>();      //반환할 List를 생성
        for(int i=0;i<postList.size();i++) {
            Post p = postList.get(i);

                PostInfoDto postInfoDto = new PostInfoDto(
                        p.getTitle(),
                        p.getCategory().getName(),
                        p.getTag1(),
                        p.getTag2(),
                        p.getTag3(),
                        p.getTag4(),
                        p.getTag5(),
                        p.getLikeNumber(),
                        p.getUser().getSchool_id(),
                        p.getCreatedBy()
                );
                postInfoDtoList.add(postInfoDto);           //반환할 List안에 대입
            }

        return postInfoDtoList;
    }



}
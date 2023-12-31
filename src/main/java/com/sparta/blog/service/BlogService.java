package com.sparta.blog.service;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
import com.sparta.blog.repository.BlogRepository;
import com.sparta.blog.entity.Blog;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class BlogService {
    private final BlogRepository blogRepository;

    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public List<BlogResponseDto> getBlogs() {
        return blogRepository.findAllByOrderByModifiedAtDesc().stream().map(BlogResponseDto::new).toList();
    }

    public BlogResponseDto createBlog(BlogRequestDto requestDto) {
        Blog blog = new Blog(requestDto);
        Blog saveBlog = blogRepository.save(blog);
        BlogResponseDto blogResponseDto = new BlogResponseDto(blog);
        return blogResponseDto;
    }

    @Transactional
    public Long updateBlog(Long id, BlogRequestDto requestDto) {
        // 해당 게시글이 DB에 존재하는지 확인
        Blog blog = findBlog(id);
        if (blog != null) {
            blog.update(requestDto);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.");
        }
    }

    public Long deleteBlog(Long id) {
        // 해당 게시글이 DB에 존재하는지 확인
        Blog blog =findBlog(id);
        if (blog != null) {
            // memo 삭제
            blogRepository.delete(blog);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.");
        }
    }

    public BlogResponseDto getBlog(Long id) {
        Blog blog = findBlog(id);
        if (blog != null) {

            return new BlogResponseDto(blog);
        } else {
            throw new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.");
        }

    }

    private Blog findBlog(Long id){
        return blogRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("선택한 메모는 존재하지 않습니다.")
        );
    }
}
